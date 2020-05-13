package com.example.communityoutbreakmanagement;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MyFamilyInformationAdapter extends RecyclerView.Adapter<MyFamilyInformationAdapter.MyFamilyInformationViewHolder> {

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

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String name = "成员姓名：" + mCursor.getString(nameIndex);
        String temperature = "今日体温：" + mCursor.getString(temperatureIndex);
        String date = mCursor.getString(dateIndex);

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

        public MyFamilyInformationViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.family_info_layout_name);
            temperatureTextView = view.findViewById(R.id.family_info_layout_temperature);
            remindButton = view.findViewById(R.id.family_info_layout_remind);
            remindButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
