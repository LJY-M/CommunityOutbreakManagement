package com.example.communityoutbreakmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.communityoutbreakmanagement.ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER;
import static com.example.communityoutbreakmanagement.ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME;
import static com.example.communityoutbreakmanagement.ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ResidentDBHelper mResidentDBHelper = new ResidentDBHelper(this);
    private TemperatureRecordsDBHelper mTemperatureRecordsDBHelper = new TemperatureRecordsDBHelper(this);
    private CommunityBlogsDBHelper mCommunityBlogsDBHelper = new CommunityBlogsDBHelper(this);

    private EditText mEditTextHouseNumber;  //门牌号
    private EditText mEditTextName;         //姓名
    private EditText mEditTextPassword;     //密码

    private Button mButtonLogin;            //登录按钮

    MyLocationListener myLocationListener = new MyLocationListener();

    NetworkConnectionBroadcastReceiver mNetworkConnectionBroadcastReceiver;
    IntentFilter mNetworkConnectionIntentFilter;

//    public String identityAuthentication = "identityAuthentication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextHouseNumber = findViewById(R.id.login_house_number);
        mEditTextName = findViewById(R.id.login_name);
        mEditTextPassword = findViewById(R.id.login_password);

        mButtonLogin = findViewById(R.id.login_button);
        mButtonLogin.setOnClickListener(LoginActivity.this);

//        TestUtil.insertTemperatureRecordsFakeData(this);

        //插入测试数据
//        TestUtil.insertResidentFakeData(mResidentDBHelper.getWritableDatabase());

        //读取测试数据
        TestUtil.getAllResident(mResidentDBHelper.getWritableDatabase());

        TestUtil.getAllTemperatureRecords(mTemperatureRecordsDBHelper.getWritableDatabase());

        TestUtil.getAllCommunityBlog(mCommunityBlogsDBHelper.getWritableDatabase());

        mNetworkConnectionIntentFilter = new IntentFilter();
        mNetworkConnectionBroadcastReceiver = new NetworkConnectionBroadcastReceiver();

        mNetworkConnectionIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        mNetworkConnectionIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mNetworkConnectionIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        openGPSSettings();
        getLocation();

//        System.out.println("经度：" + myLocationListener.locations[0] + "，纬度：" + myLocationListener.locations[1]);
    }

    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText( this , " GPS模块正常 " , Toast.LENGTH_SHORT)
                    .show();
            return ;
        }

        Toast.makeText( this , " 请开启GPS！ " , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent, 0 ); // 此为设置完成后返回到获取界面
    }

    @SuppressLint("MissingPermission")
    private void getLocation()
    {
        // 获取位置管理服务
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this .getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired( false );
        criteria.setBearingRequired( false );
        criteria.setCostAllowed( true );
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

//        String provider = locationManager.getBestProvider(criteria, true ); // 获取GPS信息
//        @SuppressLint("MissingPermission")
//        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
////        updateToNewLocation(location);
//// 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
//        locationManager.requestLocationUpdates(provider, 100 * 1000 , 500 ,
//                myLocationListener);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
//        Location loc = LocationManager.getLastKnownLocation("gps");
        Location loc = locationManager.getLastKnownLocation("gps");
        double latitude = loc.getLatitude();  //维度
        double longitude = loc.getLongitude(); //经度
        System.out.println("经度：" + latitude + "，纬度：" + longitude);
        if (latitude > 29.58 && latitude < 31.22
                && longitude >113.41 && longitude < 115.05) {
            Toast.makeText(this, "授权区域允许登录", Toast.LENGTH_SHORT).show();
        }
        else {
//            mButtonLogin.setClickable(false);
            Toast.makeText(this, "未授权区域不允许登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        int select = v.getId();
        switch (select) {
            case R.id.login_button :
                //验证身份
                boolean bCheck =  checkIdentity( LoginActivity.this, mResidentDBHelper.getWritableDatabase());
                if (!bCheck)
                    return;
                else {
                    String houseNumber = mEditTextHouseNumber.getText().toString();
                    String residentName = mEditTextName.getText().toString();
                    String[] identityInformation  = {houseNumber, residentName};
                    Resident resident = new Resident("","","", "");
                    //跳转界面
                    Intent intentToMultiFunctionActivity = new Intent(LoginActivity.this, MultiFunctionActivity.class);
                    intentToMultiFunctionActivity.putExtra(resident.identityAuthentication,identityInformation);
                    startActivity(intentToMultiFunctionActivity);
                }
                break;
            default:
                break;
        }
    }

    public boolean checkIdentity(Context context, SQLiteDatabase db) {
        String mHouseNumber;
        String mName;
        String mPassword;

        mHouseNumber = mEditTextHouseNumber.getText().toString();
        mName = mEditTextName.getText().toString();
        mPassword = mEditTextPassword.getText().toString();

        if (mHouseNumber.equals("") || mName.equals("") || mPassword.equals("")) {
            Toast.makeText(context,"门牌号、姓名与密码不能为空",Toast.LENGTH_SHORT).show();
            Log.i(this.getClass().getName(),"门牌号、姓名与密码不能为空");
            return false;
        }

        //查询数据库检验
        Cursor userCursor = queryUserByHNRN(db,mHouseNumber,mName);
        if (!userCursor.moveToNext()) {
            Toast.makeText(context,"用户不存在",Toast.LENGTH_SHORT).show();
            Log.i(this.getClass().getName(),"用户不存在");
            return false;
        }

        if (userCursor.getString(userCursor.getColumnIndex(COLUMN_HOUSE_NUMBER)).equals(mHouseNumber) &&
            userCursor.getString(userCursor.getColumnIndex(COLUMN_RESIDENT_NAME)).equals(mName) &&
            userCursor.getString(userCursor.getColumnIndex(COLUMN_RESIDENT_PASSWORD)).equals(mPassword)) {
            Toast.makeText(context,"信息正确",Toast.LENGTH_SHORT).show();
            Log.i(this.getClass().getName(),"信息正确");
            return true;
        }
        userCursor.close();

        Toast.makeText(context,"未知错误",Toast.LENGTH_SHORT).show();
        Log.i(this.getClass().getName(),"未知错误");
        return false;
    }

    public Cursor queryUserByHNRN(SQLiteDatabase sqLiteDatabase,String houseNumber, String residentName) {
        Cursor cursor =  sqLiteDatabase.rawQuery("select * from " +
                ResidentContract.ResidentEntry.TABLE_NAME +
                " where " + ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER +
                " = ? and " + ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME +
                " = ?", new String[]{houseNumber, residentName});
        return cursor;
    }

}
