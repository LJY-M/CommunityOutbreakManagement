package com.example.communityoutbreakmanagement;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;

public class TemperatureRecordsContentProvider extends ContentProvider {

    public static final int TEMPERATURE_RECORDS = 100;
    public static final int TEMPERATURE_RECORD_WITH_NUMBER = 101;
    public static final int TEMPERATURE_RECORD_WITH_NUMBER_AND_NAME = 102;
    public static final int TEMPERATURE_RECORD_WITH_NUMBER_AND_NAME_AND_TEM_AND_DATE = 103;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TemperatureRecordsContract.AUTHORITY,
                TemperatureRecordsContract.PATH_TEMPERATURE_RECORDS, TEMPERATURE_RECORDS);
        uriMatcher.addURI(TemperatureRecordsContract.AUTHORITY,
                TemperatureRecordsContract.PATH_TEMPERATURE_RECORDS + "/*", TEMPERATURE_RECORD_WITH_NUMBER);
        uriMatcher.addURI(TemperatureRecordsContract.AUTHORITY,
                TemperatureRecordsContract.PATH_TEMPERATURE_RECORDS + "/*/*", TEMPERATURE_RECORD_WITH_NUMBER_AND_NAME);
        uriMatcher.addURI(TemperatureRecordsContract.AUTHORITY,
                TemperatureRecordsContract.PATH_TEMPERATURE_RECORDS + "/*/*/*/*", TEMPERATURE_RECORD_WITH_NUMBER_AND_NAME_AND_TEM_AND_DATE);
        return uriMatcher;
    }

    private TemperatureRecordsDBHelper mTemperatureRecordsDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mTemperatureRecordsDBHelper = new TemperatureRecordsDBHelper(context);
        return false;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase database = mTemperatureRecordsDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TEMPERATURE_RECORDS:
                long id = database.insert(TemperatureRecordsContract.TemperatureRecordsEntry.
                        TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TemperatureRecordsContract.
                            TemperatureRecordsEntry.CONTENT_URI, id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;

//        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase database = mTemperatureRecordsDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case TEMPERATURE_RECORDS:
                retCursor = database.query(
                        TemperatureRecordsContract.TemperatureRecordsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TEMPERATURE_RECORD_WITH_NUMBER:
//                String houseNumber = uri.getPathSegments().get(1);
                String mGroupByN = TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME;
//                String mSelectionN = TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER + "=?";
////                String[] mSelectionArgsN = new String[]{houseNumber};
                retCursor = database.query(
                        TemperatureRecordsContract.TemperatureRecordsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        mGroupByN,
                        null,
                        sortOrder);
                break;
            case TEMPERATURE_RECORD_WITH_NUMBER_AND_NAME:
                String number = uri.getPathSegments().get(1);
                String name = uri.getPathSegments().get(2);
                String mSelection = TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER + "=?"
                         + " and " + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME + "=?";
                String[] mSelectionArgs = new String[]{number, name};
                retCursor = database.query(
                        TemperatureRecordsContract.TemperatureRecordsEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

//        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase database = mTemperatureRecordsDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int temperatureRecordDeleted;

        switch (match) {
            case TEMPERATURE_RECORD_WITH_NUMBER_AND_NAME_AND_TEM_AND_DATE:
                String number = uri.getPathSegments().get(1);
                String name = uri.getPathSegments().get(2);
                String temperature = uri.getPathSegments().get(3);
                String date = uri.getPathSegments().get(4);
                String mWhereClause = TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_HOUSE_NUMBER + "=?"
                        + " and " + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_NAME + "=?"
                        + " and " + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RESIDENT_TEMPERATURE + "=?"
                        + " and " + TemperatureRecordsContract.TemperatureRecordsEntry.COLUMN_RECORDS_TIME + "=?";
                String[] mWhereArgs = {number, name, temperature, date};
                temperatureRecordDeleted = database.delete(
                        TemperatureRecordsContract.TemperatureRecordsEntry.TABLE_NAME,
                        mWhereClause, mWhereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        if (temperatureRecordDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return temperatureRecordDeleted;

//        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

}
