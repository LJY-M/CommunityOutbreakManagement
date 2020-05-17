package com.example.communityoutbreakmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;

public class PersonCenterActivity extends AppCompatActivity implements View.OnClickListener {

    private ResidentDBHelper mResidentDBHelper = new ResidentDBHelper(this);

    private String[] identityInformation;

    private TextView mResidentNameTextView;
    private TextView mHouseNumberTextView;
    private TextView mResidentNowPasswordTextView;
    private EditText mResidentPasswordEditView;
    private Button mNewPasswordButton;
    private TextView mResidentNowPhoneTextView;
    private EditText mResidentPhoneEditView;
    private Button mNewPhoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);

        Intent intentThatStartedMultiFunctionActivity = getIntent();

        if (intentThatStartedMultiFunctionActivity != null) {
            if (intentThatStartedMultiFunctionActivity.hasExtra("identityAuthentication")) {
                identityInformation = intentThatStartedMultiFunctionActivity.getStringArrayExtra("identityAuthentication");
                Toast.makeText(PersonCenterActivity.this,identityInformation[0] + identityInformation[1],Toast.LENGTH_SHORT).show();
                System.out.println(identityInformation[0] + identityInformation[1]);
            }
        }

        mResidentNameTextView = findViewById(R.id.per_cen_resident_name);
        mHouseNumberTextView = findViewById(R.id.per_cen_house_number);
        mResidentNowPasswordTextView = findViewById(R.id.per_cen_resident_password_now);
        mResidentPasswordEditView = findViewById(R.id.per_cen_resident_password_edit);
        mNewPasswordButton = findViewById(R.id.per_cen_new_password_commit);
        mNewPasswordButton.setOnClickListener(this);

        mResidentNowPhoneTextView = findViewById(R.id.per_cen_resident_phone_now);
        mResidentPhoneEditView = findViewById(R.id.per_cen_resident_phone_edit);
        mNewPhoneButton = findViewById(R.id.per_cen_new_phone_commit);
        mNewPhoneButton.setOnClickListener(this);

        mResidentNameTextView.setText(identityInformation[0]);
        mHouseNumberTextView.setText(identityInformation[1]);

        queryUserPasswordAndPhoneByHNRNAndUpdateUI(mResidentDBHelper.
                getWritableDatabase(),identityInformation[0],identityInformation[1]);

        String cacheData = "??";
        try {
            cacheData = getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(this.getClass().getName(), cacheData);
        System.out.println(cacheData);
    }

    public void queryUserPasswordAndPhoneByHNRNAndUpdateUI(SQLiteDatabase sqLiteDatabase, String houseNumber, String residentName) {
        String userPassword;
        String userPhone;
        Cursor cursor =  sqLiteDatabase.rawQuery("select * from " +
                ResidentContract.ResidentEntry.TABLE_NAME +
                " where " + ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER +
                " = ? and " + ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME +
                " = ?", new String[]{houseNumber, residentName});
        if (cursor.moveToFirst()) {
            userPassword = cursor.getString(cursor.getColumnIndex(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD));
            userPhone = cursor.getString(cursor.getColumnIndex(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PHONE));
            mResidentNowPasswordTextView.setText(userPassword);
            mResidentNowPhoneTextView.setText(userPhone);
        }
    }

    public void updateUserPassword(SQLiteDatabase sqLiteDatabase, String houseNumber
            , String residentName, String residentNewPassword) {
        ContentValues values = new ContentValues();
        values.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD, residentNewPassword);
        String whereString = ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER + "=? AND "
                + ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME + "=?";
        sqLiteDatabase.update(ResidentContract.ResidentEntry.TABLE_NAME, values
                , whereString, new String[]{houseNumber, residentName});
    }

    public void updateUserPhone(SQLiteDatabase sqLiteDatabase, String houseNumber
            , String residentName, String residentNewPhone) {
        ContentValues values = new ContentValues();
        values.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PHONE, residentNewPhone);
        String whereString = ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER + "=? AND "
                + ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME + "=?";
        sqLiteDatabase.update(ResidentContract.ResidentEntry.TABLE_NAME, values
                , whereString, new String[]{houseNumber, residentName});
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.per_cen_new_password_commit:
                String newPassword = mResidentPasswordEditView.getText().toString();
                if (newPassword.equals("")) {
                    Toast.makeText(this, "新密码输入为空", Toast.LENGTH_SHORT).show();
                    Log.i(this.getClass().getName(), "新密码输入为空");
                    break;
                } else {
                    updateUserPassword(mResidentDBHelper.
                            getWritableDatabase(),identityInformation[0],
                            identityInformation[1], newPassword);
                    queryUserPasswordAndPhoneByHNRNAndUpdateUI(mResidentDBHelper.
                            getWritableDatabase(),identityInformation[0],identityInformation[1]);
                }
                break;
            case R.id.per_cen_new_phone_commit:
                String newPhone = mResidentPhoneEditView.getText().toString();
                if (newPhone.equals("")) {
                    Toast.makeText(this, "新号码输入为空", Toast.LENGTH_SHORT).show();
                    Log.i(this.getClass().getName(), "新号码输入为空");
                    break;
                } else {
                    updateUserPhone(mResidentDBHelper.
                                    getWritableDatabase(),identityInformation[0],
                            identityInformation[1], newPhone);
                    queryUserPasswordAndPhoneByHNRNAndUpdateUI(mResidentDBHelper.
                            getWritableDatabase(),identityInformation[0],identityInformation[1]);
                }
        }


    }

    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
//            return size + "Byte";
//        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "T";
    }





}
