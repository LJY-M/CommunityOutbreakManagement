package com.example.communityoutbreakmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

//import static com.example.communityoutbreakmanagement.ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER;
//import static com.example.communityoutbreakmanagement.ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME;
//import static com.example.communityoutbreakmanagement.ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD;

public class TestUtil {

    public static void insertResidentFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER, "1-1-101");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME, "小乔");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD, "111111");
        list.add(cv);

        cv = new ContentValues();
        cv.put(ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER, "1-1-101");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME, "周瑜");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD, "111111");
        list.add(cv);

        cv = new ContentValues();
        cv.put(ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER, "1-1-101");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME, "周循");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD, "111111");
        list.add(cv);

        cv = new ContentValues();
        cv.put(ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER, "1-2-501");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME, "张良");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD, "111111");
        list.add(cv);

        cv = new ContentValues();
        cv.put(ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER, "2-3-602");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME, "东方耀");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD, "111111");
        list.add(cv);

        cv = new ContentValues();
        cv.put(ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER, "2-2-702");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME, "亚瑟");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD, "111111");
        list.add(cv);

        cv = new ContentValues();
        cv.put(ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER, "1-3-801");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME, "李白");
        cv.put(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD, "111111");
        list.add(cv);

        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (ResidentContract.ResidentEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(ResidentContract.ResidentEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }

    public static void getAllResident(SQLiteDatabase db) {

        Cursor cursor = db.query(
                ResidentContract.ResidentEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<Resident> residentList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                residentList.add(new Resident(
                        cursor.getString(cursor.getColumnIndex(ResidentContract.ResidentEntry.COLUMN_HOUSE_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(ResidentContract.ResidentEntry.COLUMN_RESIDENT_NAME)),
                        cursor.getString(cursor.getColumnIndex(ResidentContract.ResidentEntry.COLUMN_RESIDENT_PASSWORD))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (Resident resident : residentList) {
            System.out.println(resident.toString());
        }

    }

    public static void insertTemperatureRecordsFakeData(Context context) {

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Date date = new Date();// 获取当前时间
        String currentTime = sdf.format(date);
        System.out.println("现在时间：" + currentTime); // 输出已经格式化的现在时间（24小时制）

        ContentValues contentValues = new ContentValues();
        contentValues.put(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER, "1-1-101");
        contentValues.put(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME, "小乔");
        contentValues.put(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_TEMPERATURE, "36.5℃");
        contentValues.put(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME, currentTime);
        Uri uri = context.getContentResolver().insert(TemperatureRecordsContract.
                TemperatureRecordsEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            System.out.println(uri.toString());
            Toast.makeText( context, uri.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public static void getAllTemperatureRecords(SQLiteDatabase db) {

        Cursor cursor = db.query(
                TemperatureRecordsContract.TemperatureRecordsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<TemperatureRecords> TemperatureRecordsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                TemperatureRecordsList.add(new TemperatureRecords(
                        cursor.getString(cursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME)),
                        cursor.getString(cursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_TEMPERATURE)),
                        cursor.getString(cursor.getColumnIndex(TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (TemperatureRecords temperatureRecords : TemperatureRecordsList) {
            System.out.println(temperatureRecords.toString());
        }

    }

    public static void getAllCommunityBlog(SQLiteDatabase db) {

        Cursor cursor = db.query(
                CommunityBlogsContract.CommunityBlogsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<CommunityBlogs> CommunityBlogsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                CommunityBlogsList.add(new CommunityBlogs(
                        cursor.getString(cursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_HOUSE_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_RESIDENT_NAME)),
                        cursor.getString(cursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_LABEL)),
                        cursor.getString(cursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_TITLE)),
                        cursor.getString(cursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_TIME))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (CommunityBlogs communityBlogs : CommunityBlogsList) {
            System.out.println(communityBlogs.toString());
        }

    }
}
