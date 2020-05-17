package com.example.communityoutbreakmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TemperatureReportActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> , View.OnClickListener {

    private static final String TAG = TemperatureReportActivity.class.getSimpleName();
    private static final int TEMPERATURE_REPORT_LOADER_ID = 27;

    private String[] identityInformation;

    private EditText mTodayTemperatureEditText;
    private Button mCommitRecordButton;

    RecyclerView mRecyclerView;
    private TemperatureReportAdapter mTemperatureReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_report);

        Intent intentThatStartedMultiFunctionActivity = getIntent();

        if (intentThatStartedMultiFunctionActivity != null) {
            if (intentThatStartedMultiFunctionActivity.hasExtra("identityAuthentication")) {
                identityInformation = intentThatStartedMultiFunctionActivity.getStringArrayExtra("identityAuthentication");
                Toast.makeText(TemperatureReportActivity.this,identityInformation[0] + identityInformation[1],Toast.LENGTH_SHORT).show();
                System.out.println(identityInformation[0] + identityInformation[1]);
            }
        }

        mTodayTemperatureEditText = findViewById(R.id.temperature_report_today_temperature_value);
        mCommitRecordButton = findViewById(R.id.temperature_report_today_temperature_commit);
        mCommitRecordButton.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.temperature_report_history_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTemperatureReportAdapter = new TemperatureReportAdapter(this);
        mRecyclerView.setAdapter(mTemperatureReportAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
//                int id = (int) viewHolder.itemView.getTag();
//
//                String stringId = Integer.toString(id);
                TextView textViewTemp = viewHolder.itemView.findViewById(R.id.temperature_records_temperature);
                TextView textViewDate = viewHolder.itemView.findViewById(R.id.temperature_records_date);
                String temperature = textViewTemp.getText().toString();
                String date = textViewDate.getText().toString();
                Uri uri = TemperatureRecordsContract.TemperatureRecordsEntry.CONTENT_URI;
                uri = uri.buildUpon()
                        .appendPath(identityInformation[0])
                        .appendPath(identityInformation[1])
                        .appendPath(temperature)
                        .appendPath(date)
                        .build();
                if (uri != null) {
                    System.out.println(uri.toString());
                    Toast.makeText( TemperatureReportActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
                }

                getContentResolver().delete(uri, null, null);

                getSupportLoaderManager().restartLoader(
                        TEMPERATURE_REPORT_LOADER_ID, null, TemperatureReportActivity.this);
            }
        }).attachToRecyclerView(mRecyclerView);

        getSupportLoaderManager().initLoader(TEMPERATURE_REPORT_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(TEMPERATURE_REPORT_LOADER_ID, null, this);
    }

//    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id,  Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTemperatureReport = null;

            @Override
            protected void onStartLoading() {
                if (mTemperatureReport != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTemperatureReport);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                String mSelection = ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER +
                        " = ? and " + ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME +
                        " = ?";
                String[] mSelectionArgs = {identityInformation[0], identityInformation[1]};

                try {
                    mTemperatureReport = getContentResolver().query(
                            TemperatureRecordsContract.TemperatureRecordsEntry.CONTENT_URI,
                            null, mSelection, mSelectionArgs,
                            TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME + " desc");
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

                return mTemperatureReport;
            }

            public void deliverResult(Cursor data) {
                mTemperatureReport = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor data) {
        mTemperatureReportAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTemperatureReportAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.temperature_report_today_temperature_commit:
                String sTemperature = mTodayTemperatureEditText.getText().toString();
                if (sTemperature.isEmpty())
                {
                    Toast.makeText(this, "输入体温为空", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "输入体温为空");
                    break;
                }
                Double dTemperature = Double.parseDouble(sTemperature);
                if ( dTemperature > 30 && dTemperature < 50) {
                    Toast.makeText(this, "输入正确", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "输入正确");

                    SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
                    sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                    Date date = new Date();// 获取当前时间
                    String currentTime = sdf.format(date);
                    System.out.println("现在时间：" + currentTime); // 输出已经格式化的现在时间（24小时制）

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER, identityInformation[0]);
                    contentValues.put(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME, identityInformation[1]);
                    contentValues.put(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_TEMPERATURE, sTemperature + "℃");
                    contentValues.put(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME, currentTime);
                    Uri uri = getContentResolver().insert(TemperatureRecordsContract.
                            TemperatureRecordsEntry.CONTENT_URI, contentValues);
                    if (uri != null) {
                        System.out.println(uri.toString());
                        Toast.makeText( this, uri.toString(), Toast.LENGTH_LONG).show();
                    }
                    getSupportLoaderManager().restartLoader(TEMPERATURE_REPORT_LOADER_ID, null, this);
                    break;
                }
                Toast.makeText(this, "输入错误", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "输入错误");
                break;
        }
    }

}
