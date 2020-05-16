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
import androidx.annotation.Nullable;

public class CommunityBlogsContentProvider extends ContentProvider {

    public static final int COMMUNITY_BLOGS = 200;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CommunityBlogsContract.AUTHORITY,
                CommunityBlogsContract.PATH_COMMUNITY_BLOGS, COMMUNITY_BLOGS);
        return uriMatcher;
    }

    private CommunityBlogsDBHelper mCommunityBlogsDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mCommunityBlogsDBHelper = new CommunityBlogsDBHelper(context);
        return false;
    }

    @Override
    public Uri insert(@NonNull  Uri uri,ContentValues contentValues) {

        final SQLiteDatabase database = mCommunityBlogsDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case COMMUNITY_BLOGS:
                long id = database.insert(
                        CommunityBlogsContract.CommunityBlogsEntry.TABLE_NAME,
                        null,
                        contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(
                            CommunityBlogsContract.CommunityBlogsEntry.CONTENT_URI,
                            id);
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
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase database = mCommunityBlogsDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case COMMUNITY_BLOGS:
                retCursor = database.query(
                        CommunityBlogsContract.CommunityBlogsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = mCommunityBlogsDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int communityBlogDeleted;

        switch (match) {
            case COMMUNITY_BLOGS:
                communityBlogDeleted = database.delete(
                        CommunityBlogsContract.CommunityBlogsEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        if (communityBlogDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return communityBlogDeleted;
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
