package com.example.communityoutbreakmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyCommunityActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = MyCommunityActivity.class.getSimpleName();
    private static final int COMMUNITY_BLOGS_LOADER_ID = 53;

    private String[] identityInformation;

    RecyclerView mRecyclerView;
    private MyCommunityAdapter mMyCommunityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community);

        Intent intentThatStartedMultiFunctionActivity = getIntent();

        if (intentThatStartedMultiFunctionActivity != null) {
            if (intentThatStartedMultiFunctionActivity.hasExtra("identityAuthentication")) {
                identityInformation = intentThatStartedMultiFunctionActivity.getStringArrayExtra("identityAuthentication");
                Toast.makeText(MyCommunityActivity.this,identityInformation[0] + identityInformation[1],Toast.LENGTH_SHORT).show();
                System.out.println(identityInformation[0] + identityInformation[1]);
            }
        }

        mRecyclerView = findViewById(R.id.my_community_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyCommunityAdapter = new MyCommunityAdapter(this);
        mRecyclerView.setAdapter(mMyCommunityAdapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.my_community_floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resident residentAB = new Resident("","","", "");
                Intent addBlogIntent = new Intent(MyCommunityActivity.this,AddBlogActivity.class);
                addBlogIntent.putExtra(residentAB.identityAuthentication, identityInformation);
                startActivity(addBlogIntent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int id = (int) viewHolder.itemView.getTag();
                String stringId = Integer.toString(id);

                Uri uri = CommunityBlogsContract.CommunityBlogsEntry.CONTENT_URI;

                TextView numberAndNameTextView = viewHolder.itemView.findViewById(R.id.community_blog_name);
                String numberAndName = numberAndNameTextView.getText().toString();
                if (numberAndName.contains(identityInformation[0])
                        && numberAndName.contains(identityInformation[1])) {

                    String where = CommunityBlogsContract.CommunityBlogsEntry._ID + " =? ";
                    String[] whereArgs = new String[]{stringId};
                    System.out.println("进行删除操作");

                    getContentResolver().delete(uri, where, whereArgs);
                }
                else {
                    Toast.makeText(MyCommunityActivity.this, "没有操作权限", Toast.LENGTH_SHORT).show();
                    System.out.println("没有操作权限");
                }
                getSupportLoaderManager().restartLoader(
                        COMMUNITY_BLOGS_LOADER_ID, null, MyCommunityActivity.this);

            }
        }).attachToRecyclerView(mRecyclerView);

        getSupportLoaderManager().initLoader(COMMUNITY_BLOGS_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(COMMUNITY_BLOGS_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mCommunityBlog = null;

            @Override
            protected void onStartLoading() {
                if (mCommunityBlog != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mCommunityBlog);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    mCommunityBlog = getContentResolver().query(
                            CommunityBlogsContract.CommunityBlogsEntry.CONTENT_URI,
                            null, null, null,
                            CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_TIME + " desc");
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

                return mCommunityBlog;
            }

            public void deliverResult(Cursor data) {
                mCommunityBlog = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor data) {
        mMyCommunityAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMyCommunityAdapter.swapCursor(null);
    }


}
