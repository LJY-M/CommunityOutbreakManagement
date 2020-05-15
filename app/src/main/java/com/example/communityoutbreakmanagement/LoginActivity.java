package com.example.communityoutbreakmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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

    private EditText mEditTextHouseNumber;  //门牌号
    private EditText mEditTextName;         //姓名
    private EditText mEditTextPassword;     //密码

    private Button mButtonLogin;            //登录按钮

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

        mNetworkConnectionIntentFilter = new IntentFilter();
        mNetworkConnectionBroadcastReceiver = new NetworkConnectionBroadcastReceiver();

        mNetworkConnectionIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        mNetworkConnectionIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mNetworkConnectionIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

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
                    Resident resident = new Resident("","","");
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
