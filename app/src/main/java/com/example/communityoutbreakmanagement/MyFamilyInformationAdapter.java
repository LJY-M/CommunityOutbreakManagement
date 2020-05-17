package com.example.communityoutbreakmanagement;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MyFamilyInformationAdapter
        extends RecyclerView.Adapter<MyFamilyInformationAdapter.MyFamilyInformationViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public MyFamilyInformationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public MyFamilyInformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.family_information_layout, parent, false);
        return new MyFamilyInformationViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyFamilyInformationViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry._ID);
        int nameIndex = mCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME);
        int temperatureIndex = mCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_TEMPERATURE);
        int dateIndex = mCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME);
        int numberIndex = mCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String name = "成员姓名：" + mCursor.getString(nameIndex);
        String temperature = "今日体温：" + mCursor.getString(temperatureIndex);
        String date = mCursor.getString(dateIndex);
        String number = mCursor.getString(numberIndex);

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Date today = new Date();// 获取当前时间
        String currentTime = sdf.format(today);

        holder.itemView.setTag(id);
        holder.nameTextView.setText(name);

        if (date.contains(currentTime)) {
            holder.temperatureTextView.setText(temperature);
        } else {
            holder.temperatureTextView.setText("今日体温未上报!");
        }

        String phoneNumber = "";
        ResidentDBHelper residentDBHelper = new ResidentDBHelper(mContext);
        SQLiteDatabase sqLiteDatabase = residentDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " +
                ResidentContract.ResidentEntry.TABLE_NAME +
                " where " + ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER +
                " = ? and " + ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME +
                " = ? ", new String[]{number, mCursor.getString(nameIndex)});
        if (cursor.moveToFirst()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PHONE));
            holder.phoneNumber = phoneNumber;
        }

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    class MyFamilyInformationViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView temperatureTextView;
        Button remindButton;
        String phoneNumber = "";

        public MyFamilyInformationViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.family_info_layout_name);
            temperatureTextView = view.findViewById(R.id.family_info_layout_temperature);

            remindButton = view.findViewById(R.id.family_info_layout_remind);
            remindButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(phoneNumber);

                    Uri uri = Uri.parse("smsto:" + phoneNumber);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra("sms_body",
                            nameTextView.getText().toString()
                                    + ",请尽快上报今日体温吧！");
                    mContext.startActivity(intent);
//                    SmsManager smsManager = SmsManager.getDefault();
//                    String content = nameTextView.getText().toString()
//                            + ",请尽快上报今日体温吧！" ;
//                    ArrayList<String> list = smsManager.divideMessage(content);
//                    for (int i = 0; i < list.size(); i++) {
//                        smsManager.sendTextMessage(
//                                phoneNumber, null,
//                                list.get(i), null, null);
//                    }

                    System.out.println("remind already");
                }
            });
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
