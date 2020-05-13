package com.example.communityoutbreakmanagement;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TemperatureReportAdapter extends RecyclerView.Adapter<TemperatureReportAdapter.TemperatureReportViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public TemperatureReportAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public TemperatureReportViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.temperature_records_layout, parent, false);
        return new TemperatureReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder( TemperatureReportViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position, payloads);
        int idIndex = mCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry._ID);
        int dateIndex = mCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME);
        int temperatureIndex = mCursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_TEMPERATURE);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String date = mCursor.getString(dateIndex);
        String temperature = mCursor.getString(temperatureIndex);

        holder.itemView.setTag(id);
        holder.dateRecordTextView.setText(date);
        holder.temperatureRecordTextView.setText(temperature);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    class TemperatureReportViewHolder extends RecyclerView.ViewHolder {
        TextView dateRecordTextView;
        TextView temperatureRecordTextView;

        public TemperatureReportViewHolder(View view) {
            super(view);

            dateRecordTextView = view.findViewById(R.id.temperature_records_date);
            temperatureRecordTextView = view.findViewById(R.id.temperature_records_temperature);
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
