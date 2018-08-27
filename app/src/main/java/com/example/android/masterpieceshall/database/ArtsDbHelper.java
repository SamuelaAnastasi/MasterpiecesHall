package com.example.android.masterpieceshall.database;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.masterpieceshall.database.ArtsContract.ArtsEntry;

public class ArtsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "arts.db";

    private static final int DATABASE_VERSION = 3;


    ArtsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // SQLite CREATE TABLE command String
        final String SQL_CREATE_ARTS_TABLE =
                "CREATE TABLE IF NOT EXISTS " + ArtsEntry.TABLE_NAME + " (" +
                        ArtsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ArtsEntry.COLUMN_ART_OBJECT_NUMBER + " TEXT NOT NULL, " +
                        ArtsEntry.COLUMN_ART_AUTHOR + " TEXT NOT NULL, " +
                        ArtsEntry.COLUMN_ART_TITLE + " TEXT NOT NULL, " +
                        ArtsEntry.COLUMN_ART_LONG_TITLE + " TEXT NOT NULL, " +
                        ArtsEntry.COLUMN_ART_IMAGE_PATH + " TEXT NOT NULL, " +
                        ArtsEntry.COLUMN_ART_HEADER_PATH + " TEXT NOT NULL, " +
                        " UNIQUE (" + ArtsEntry.COLUMN_ART_OBJECT_NUMBER + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_ARTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ArtsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}