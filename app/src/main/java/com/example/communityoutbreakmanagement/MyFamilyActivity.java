package com.example.communityoutbreakmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyFamilyActivity extends AppCompatActivity {

    private static final String TAG = MyFamilyActivity.class.getSimpleName();

    private String[] identityInformation;

    TextView mHouseNumberTextView;

    RecyclerView mRecyclerView;
    private MyFamilyInformationAdapter mMyFamilyInformationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_family);

        Intent intentThatStartedMultiFunctionActivity = getIntent();

        if (intentThatStartedMultiFunctionActivity != null) {
            if (intentThatStartedMultiFunctionActivity.hasExtra("identityAuthentication")) {
                identityInformation = intentThatStartedMultiFunctionActivity.getStringArrayExtra("identityAuthentication");
                Toast.makeText(MyFamilyActivity.this,identityInformation[0] + identityInformation[1],Toast.LENGTH_SHORT).show();
                System.out.println(identityInformation[0] + identityInformation[1]);
            }
        }

        mHouseNumberTextView = findViewById(R.id.my_family_house_number_value);

        mRecyclerView = findViewById(R.id.my_family_relevant_information_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyFamilyInformationAdapter = new MyFamilyInformationAdapter(this);
        mRecyclerView.setAdapter(mMyFamilyInformationAdapter);

        Uri uri = TemperatureRecordsContract.TemperatureRecordsEntry.CONTENT_URI;
        uri = uri.buildUpon()
                .appendPath(identityInformation[0])
                .build();
        if (uri != null) {
            System.out.println(uri.toString());
            Toast.makeText( MyFamilyActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
        }

        String mSelection = TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER + " =? and "
                + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME + " !=?";
        String[] mSelectionArgs = new String[]{identityInformation[0], identityInformation[1]};

//        ArrayList<String> familiesList = new ArrayList<>();

        Cursor mFamiliesCursor = null;     //查找家庭成员

        mFamiliesCursor = getContentResolver().query(
                uri,null,
                mSelection, mSelectionArgs,
                TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME + " desc");

//        mFamiliesCursor = getContentResolver().query(
//                TemperatureRecordsContract.TemperatureRecordsEntry.CONTENT_URI,
//                null,
//                mSelection,
//                mSelectionArgs,
//                TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME + " desc");

        if (mFamiliesCursor.moveToFirst()) {
            do {
                String familiesName = mFamiliesCursor.getString(mFamiliesCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME));
//                if (!familiesName.equals(identityInformation[1])) {
//                    familiesList.add(familiesName);
//                }
                String familiesDate = mFamiliesCursor.getString(mFamiliesCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME));
                System.out.println(TAG + familiesName + "    " + familiesDate);
            } while (mFamiliesCursor.moveToNext());
        }
//        mFamiliesCursor.close();

        mMyFamilyInformationAdapter.swapCursor(mFamiliesCursor);

    }
}
