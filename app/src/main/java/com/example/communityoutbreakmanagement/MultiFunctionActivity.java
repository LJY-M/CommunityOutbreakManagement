package com.example.communityoutbreakmanagement;

import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.example.communityoutbreakmanagement.PersonCenterActivity.getTotalCacheSize;

public class MultiFunctionActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<EpidemicData>, View.OnClickListener {

    private String[] identityInformation;

    private static final int EPIDEMIC_DATA_SEARCH_LOADER = 23;

    private static final String TAG = MultiFunctionActivity.class.getSimpleName();

    private int[] epidemicShowViewId = {R.id.multi_epi_today_storeConfirm_value, R.id.multi_epi_today_dead_value,
            R.id.multi_epi_today_heal_value, R.id.multi_epi_today_input_value,
            R.id.multi_epi_today_noSymptom_value, R.id.multi_epi_today_confirm_value,
            R.id.multi_epi_total_storeConfirm_value,  R.id.multi_epi_total_dead_value,
            R.id.multi_epi_total_heal_value, R.id.multi_epi_total_input_value,
            R.id.multi_epi_total_noSymptom_value, R.id.multi_epi_total_confirm_value};
    private TextView[] epidemicShowTextView = new TextView[12];

    private ImageButton mPersonCenterImageButton;   //imageButton控件--个人中心（待补全）
    private TextView mPersonCenterHouseNumberTextView;
    private TextView mPersonCenterResidentNameTextView;

    private ImageButton mTemperatureReportImageButton;
    private ImageButton mMyFamilyImageButton;
    private ImageButton mMyCommunityImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_function);

        Intent intentThatStartedMultiFunctionActivity = getIntent();

        if (intentThatStartedMultiFunctionActivity != null) {
            if (intentThatStartedMultiFunctionActivity.hasExtra("identityAuthentication")) {
                identityInformation = intentThatStartedMultiFunctionActivity.getStringArrayExtra("identityAuthentication");
                Toast.makeText(MultiFunctionActivity.this,identityInformation[0] + identityInformation[1],Toast.LENGTH_SHORT).show();
                System.out.println(identityInformation[0] + identityInformation[1]);
            }
        }

        boundTextView();    //绑定疫情相关TextView控件
        //绑定个人中心相关控件
        mPersonCenterImageButton = findViewById(R.id.multi_image_button_person_center);
        mPersonCenterImageButton.setOnClickListener(this);
        mPersonCenterHouseNumberTextView = findViewById(R.id.multi_person_center_house_number);
        mPersonCenterResidentNameTextView = findViewById(R.id.multi_person_center_resident_name);

        //绑定体温上报相关控件
        mTemperatureReportImageButton = findViewById(R.id.multi_image_button_temperature_report);
        mTemperatureReportImageButton.setOnClickListener(this);

        //绑定我的家庭相关控件
        mMyFamilyImageButton = findViewById(R.id.multi_image_button_my_family);
        mMyFamilyImageButton.setOnClickListener(this);

        //绑定我的社区相关控件
        mMyCommunityImageButton = findViewById(R.id.multi_image_button_my_community);
        mMyCommunityImageButton.setOnClickListener(this);

        //显示个人信息
        mPersonCenterHouseNumberTextView.setText(identityInformation[0]);
        mPersonCenterResidentNameTextView.setText(identityInformation[1]);

        reminderReportTemperature();

        String cacheData = "??";
        try {
            cacheData = getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(this.getClass().getName(), cacheData);
        System.out.println(cacheData);

        getSupportLoaderManager().initLoader(EPIDEMIC_DATA_SEARCH_LOADER,null,this);
    }
 
    public void reminderReportTemperature() {

        ReminderUtilities.scheduleChargingReminder(MultiFunctionActivity.this, identityInformation);

        Uri uri = TemperatureRecordsContract.TemperatureRecordsEntry.CONTENT_URI;
        uri = uri.buildUpon()
                .appendPath(identityInformation[0])
                .build();
        if (uri != null) {
            System.out.println(uri.toString());
            Toast.makeText( MultiFunctionActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
        }

        String mSelection = TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER + " =? and "
                + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME + " =?";
        String[] mSelectionArgs = new String[]{identityInformation[0], identityInformation[1]};

        Cursor mFamiliesCursor = null;     //查找家庭成员

        mFamiliesCursor = getContentResolver().query(
                uri,null,
                mSelection, mSelectionArgs,
                TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME + " desc");

        if (!mFamiliesCursor.moveToFirst()) {
            return;
        }

        int dateIndex = mFamiliesCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME);
        String date = mFamiliesCursor.getString(dateIndex);

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Date today = new Date();// 获取当前时间
        String currentTime = sdf.format(today);

        if (!date.contains(currentTime)) {
            Toast.makeText( MultiFunctionActivity.this, "今日体温未上报", Toast.LENGTH_LONG).show();

            Resident resident = new Resident("","","", "");

            Intent remindReportTemperatureIntent = new Intent(
                    MultiFunctionActivity.this, TemperatureReportReminderIntentService.class);
            remindReportTemperatureIntent.putExtra(resident.identityAuthentication, identityInformation);
            remindReportTemperatureIntent.setAction(ReminderTasks.ACTION_REMIND_REPORT_TEMPERATURE);
            startService(remindReportTemperatureIntent);

            NotificationUtils.reminderUserBecauseCharging(MultiFunctionActivity.this, identityInformation);
        }


    }

//    @NonNull
    @Override
    public Loader<EpidemicData> onCreateLoader(int id,Bundle args) {
        return new AsyncTaskLoader<EpidemicData>(this) {

            EpidemicData mEpidemicData;

            @Override
            protected void onStartLoading() {
//                super.onStartLoading();
                if (mEpidemicData != null)
                    deliverResult(mEpidemicData);
                else
                    forceLoad();
            }


            @Override
            public EpidemicData loadInBackground() {
                URL epidemicDataRequestUrl = NetworkUtils.buildEpidemicUrl();

                try {
                    String jsonEpidemicDataResponse = NetworkUtils
                            .getResponseFromHttpUrl(epidemicDataRequestUrl);        //抓取API接口数据

                    EpidemicData epidemicData = NetworkUtils
                            .getSimpleEpidemicObjectFromJson(jsonEpidemicDataResponse);     //处理API接口数据

                    return epidemicData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }


            @Override
            public void deliverResult(EpidemicData data) {
                mEpidemicData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<EpidemicData> loader, EpidemicData data) {
        if (data != null) {
            Log.i(TAG, data.toString());
            Toast.makeText(this, "get text from api : " + data.toString(), Toast.LENGTH_SHORT).show();
            setEpidemicDataInTextView(data);
        }
        else {
            Log.i(TAG, "get null data");
            Toast.makeText(this, "get null from api : ", Toast.LENGTH_SHORT).show();
            hideTextView();
        }
    }

    @Override
    public void onLoaderReset(Loader<EpidemicData> loader) {

    }

    public void boundTextView() {
        for (int i = 0; i < 12; i++) {
            epidemicShowTextView[i] = findViewById(epidemicShowViewId[i]);
        }
    }

    public void hideTextView() {
        for (int i = 0; i < 12; i++) {
            epidemicShowTextView[i].setVisibility(View.INVISIBLE);
        }
    }

    public void setEpidemicDataInTextView(EpidemicData data) {

        String[] dataString = {"较昨日<font color='#a31d13'> +" + data.getTodayConfirm() + "</font>"
                , "较昨日<font color='#333333'> +" + data.getTodayDead() + "</font>"
                , "较昨日<font color='#34aa70'> +" + data.getTodayHeal() + "</font>"
                , "较昨日<font color='#ffa352'> +" + data.getTodayInput() + "</font>"
                , "较昨日<font color='#791618'> +" + data.getExtDataIncrNoSymptom() + "</font>"
                , "较昨日<font color='#e44a3d'> " + data.getTodayStoreConfirm() + "</font>"
                , "" + data.getTotalConfirm(), "" + data.getTotalDead()
                , "" + data.getTotalHeal(), "" + data.getTotalInput()
                , "" + data.getExtDataNoSymptom(), "" + data.getTotalStoreConfirm()};

        Spanned[] dataSpanned = new Spanned[12];
        for (int i = 0; i < 12; i++) {
            dataSpanned[i] = Html.fromHtml(dataString[i]);
        }

//        epidemicShowTextView[0].setText(data.getTodayConfirm());
//        epidemicShowTextView[1].setText(data.getTodayDead());
//        epidemicShowTextView[2].setText(data.getTodayHeal());
//        epidemicShowTextView[3].setText(data.getTodayInput());
//        epidemicShowTextView[4].setText(data.getExtDataIncrNoSymptom());
//        epidemicShowTextView[5].setText(data.getTodayStoreConfirm());
//        epidemicShowTextView[6].setText(data.getTotalConfirm());
//        epidemicShowTextView[7].setText(data.getTotalDead());
//        epidemicShowTextView[8].setText(data.getTotalHeal());
//        epidemicShowTextView[9].setText(data.getTotalInput());
//        epidemicShowTextView[10].setText(data.getExtDataNoSymptom());
//        epidemicShowTextView[11].setText(data.getTotalStoreConfirm());

        for (int i = 0; i < 12; i++) {
            epidemicShowTextView[i].setText(dataSpanned[i]);
        }

//        private int[] epidemicShowViewId = {R.id.multi_epi_today_storeConfirm_value, R.id.multi_epi_today_dead_value,
//                R.id.multi_epi_today_heal_value, R.id.multi_epi_today_input_value,
//                R.id.multi_epi_today_noSymptom_value, R.id.multi_epi_today_confirm_value,
//                R.id.multi_epi_total_storeConfirm_value,  R.id.multi_epi_total_dead_value,
//                R.id.multi_epi_total_heal_value, R.id.multi_epi_total_input_value,
//                R.id.multi_epi_total_noSymptom_value, R.id.multi_epi_total_confirm_value};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.multifunction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getSupportLoaderManager().restartLoader(EPIDEMIC_DATA_SEARCH_LOADER, null, this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.multi_image_button_person_center:
                Resident residentPC = new Resident("","","", "");
                Intent intentToPersonCenterActivity = new Intent(MultiFunctionActivity.this, PersonCenterActivity.class);
                intentToPersonCenterActivity.putExtra(residentPC.identityAuthentication, identityInformation);
                startActivity(intentToPersonCenterActivity);
                break;
            case R.id.multi_image_button_temperature_report:
                Resident residentTR = new Resident("","","", "");
                Intent intentToTemperatureReportActivity = new Intent(MultiFunctionActivity.this, TemperatureReportActivity.class);
                intentToTemperatureReportActivity.putExtra(residentTR.identityAuthentication, identityInformation);
                startActivity(intentToTemperatureReportActivity);
                break;
            case R.id.multi_image_button_my_family:
                Resident residentMF = new Resident("","","", "");
                Intent intentToMyFamilyActivity = new Intent(MultiFunctionActivity.this, MyFamilyActivity.class);
                intentToMyFamilyActivity.putExtra(residentMF.identityAuthentication, identityInformation);
                startActivity(intentToMyFamilyActivity);
                break;
            case R.id.multi_image_button_my_community:
                Resident residentMC = new Resident("","","", "");
                Intent intentToMyCommunityActivity = new Intent(MultiFunctionActivity.this, MyCommunityActivity.class);
                intentToMyCommunityActivity.putExtra(residentMC.identityAuthentication, identityInformation);
                startActivity(intentToMyCommunityActivity);
                break;
        }
    }
}
