package com.example.communityoutbreakmanagement;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyCommunityAdapter extends
        RecyclerView.Adapter<MyCommunityAdapter.MyCommunityViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public MyCommunityAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyCommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.community_blogs_layout, parent, false);
        return new MyCommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyCommunityViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry._ID);
        int numberIndex = mCursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_HOUSE_NUMBER);
        int nameIndex = mCursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_RESIDENT_NAME);
        int titleIndex = mCursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_TITLE);
        int contentIndex = mCursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_CONTENT);
        int labelIndex = mCursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_LABEL);
        int dateIndex = mCursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_TIME);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String number = mCursor.getString(numberIndex);
        String name = mCursor.getString(nameIndex);
        String title = mCursor.getString(titleIndex);
        String content = mCursor.getString(contentIndex);
        String label = "#" + mCursor.getString(labelIndex);
        String date = mCursor.getString(dateIndex);

        String numberAndName = number + "  " + name;

        holder.itemView.setTag(id);
        holder.mNumberAndNameTextView.setText(numberAndName);
        holder.mBlogTitleTextView.setText(title);
        holder.mBlogContentTextView.setText(content);
        holder.mBlogLabelTextView.setText(label);
        holder.mBlogTimeTextView.setText(date);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    class MyCommunityViewHolder extends RecyclerView.ViewHolder {
        TextView mNumberAndNameTextView;
        TextView mBlogTitleTextView;
        TextView mBlogContentTextView;
        TextView mBlogLabelTextView;
        TextView mBlogTimeTextView;

        public MyCommunityViewHolder(View view) {
            super(view);

            mNumberAndNameTextView = view.findViewById(R.id.community_blog_name);
            mBlogTitleTextView = view.findViewById(R.id.community_blog_title);
            mBlogContentTextView = view.findViewById(R.id.community_blog_contend);
            mBlogLabelTextView = view.findViewById(R.id.community_blog_label);
            mBlogTimeTextView = view.findViewById(R.id.community_blog_time);
        }
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
