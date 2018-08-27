package com.example.android.masterpieceshall.database;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.masterpieceshall.database.ArtsContract.ArtsEntry;

public class ArtsProvider extends ContentProvider {

    public static final String LOG_TAG = ArtsProvider.class.getSimpleName();

    public static final int CODE_ARTS = 100;
    public static final int CODE_ARTS_WITH_ID = 101;

    private ArtsDbHelper artsDbHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ArtsContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ArtsContract.PATH_ARTS, CODE_ARTS);
        matcher.addURI(authority, ArtsContract.PATH_ARTS + "/*", CODE_ARTS_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        artsDbHelper = new ArtsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase database = artsDbHelper.getReadableDatabase();

        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case CODE_ARTS:
                cursor = database.query(ArtsEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case CODE_ARTS_WITH_ID:
                cursor = database.query(ArtsEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase database = artsDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case CODE_ARTS:
                return insertArt(database, uri, contentValues);
            default:
                database.close();
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        SQLiteDatabase database = artsDbHelper.getWritableDatabase();
        int rowsDeleted;
        int match = uriMatcher.match(uri);
        switch (match) {
            case CODE_ARTS:
                rowsDeleted = database.delete(ArtsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_ARTS_WITH_ID:
                rowsDeleted = database.delete(ArtsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                database.close();
                throw new IllegalArgumentException("Deletion not supported for uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    // Helper method for insert()
    private Uri insertArt(SQLiteDatabase database, Uri uri, ContentValues values) {
        String imagePath = values.getAsString(ArtsEntry.COLUMN_ART_IMAGE_PATH);
        if(imagePath == null) {
            throw new IllegalArgumentException("Image path should not be null");
        }
        String headerPath = values.getAsString(ArtsEntry.COLUMN_ART_HEADER_PATH);
        if(headerPath == null) {
            throw new IllegalArgumentException("Header path should not be null");
        }

        database = artsDbHelper.getWritableDatabase();
        long rowId = database.insert(ArtsEntry.TABLE_NAME, null, values);
        if (rowId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, rowId);
    }
}
