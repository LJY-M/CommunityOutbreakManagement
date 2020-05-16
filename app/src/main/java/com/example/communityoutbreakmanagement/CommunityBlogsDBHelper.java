package com.example.communityoutbreakmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommunityBlogsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "blog1.db";
    private static final int DATABASE_VERSION = 1;

    public CommunityBlogsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RESIDENT_TABLE = "CREATE TABLE "
                + CommunityBlogsContract.CommunityBlogsEntry.TABLE_NAME + " ("
                + CommunityBlogsContract.CommunityBlogsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CommunityBlogsContract.CommunityBlogsEntry.COLUMN_HOUSE_NUMBER + " TEXT NOT NULL, "
                + CommunityBlogsContract.CommunityBlogsEntry.COLUMN_RESIDENT_NAME + " TEXT NOT NULL, "
                + CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_LABEL + " TEXT NOT NULL, "
                + CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_TITLE + " TEXT NOT NULL, "
                + CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_CONTENT + " TEXT NOT NULL, "
                + CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_TIME + " TEXT NOT NULL "
                + "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_RESIDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CommunityBlogsContract.CommunityBlogsEntry.TABLE_NAME);
        onCreate(db);
    }
}
