package com.example.communityoutbreakmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TemperatureRecordsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "temperature1.db";
    private static final int DATABASE_VERSION = 1;

    public TemperatureRecordsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RESIDENT_TABLE = "CREATE TABLE "
                + TemperatureRecordsContract.TemperatureRecordsEntry.TABLE_NAME + " ("
                +TemperatureRecordsContract.TemperatureRecordsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER + " TEXT NOT NULL, "
                + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME + " TEXT NOT NULL, "
                + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_TEMPERATURE + " TEXT NOT NULL, "
                + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME + " TEXT NOT NULL "
                + "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_RESIDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ResidentContract.ResidentEntry.TABLE_NAME);
        onCreate(db);
    }
}
