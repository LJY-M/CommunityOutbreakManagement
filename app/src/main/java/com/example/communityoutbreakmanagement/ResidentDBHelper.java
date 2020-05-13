package com.example.communityoutbreakmanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ResidentDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "resident2.db";
    private static final int DATABASE_VERSION = 1;

    public ResidentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RESIDENT_TABLE = "CREATE TABLE "
                + ResidentContract.ResidentEntry.TABLE_NAME + " ("
                + ResidentContract.ResidentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER + " TEXT NOT NULL, "
                + ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME + " TEXT NOT NULL, "
                + ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD + " TEXT NOT NULL "
                + "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_RESIDENT_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ResidentContract.ResidentEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
