package com.example.android.masterpieceshall.database;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.net.Uri;
import android.provider.BaseColumns;

public class ArtsContract {
    // Private empty constructor to avoid instantiation.
    private ArtsContract() {}

    // Using package name as authority
    static final String CONTENT_AUTHORITY = "com.example.android.masterpieceshall";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Provider's path for favorite Art works
    static final String PATH_ARTS = "arts";

    // BaseColumns Entry class for (favorite) arts table
    public static final class ArtsEntry implements BaseColumns {

        // Content Uri to query the (favorite) arts table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ARTS).build();

        // Used internally in the SQLite CREATE TABLE command
        static final String TABLE_NAME = "arts";

        // Column names (_ID column inherited by BaseColumns so it is not defined here)
        public static final String COLUMN_ART_OBJECT_NUMBER = "objectNumber";
        public static final String COLUMN_ART_AUTHOR = "author";
        public static final String COLUMN_ART_TITLE = "title";
        public static final String COLUMN_ART_LONG_TITLE = "longTitle";
        public static final String COLUMN_ART_IMAGE_PATH = "imagePath";
        public static final String COLUMN_ART_HEADER_PATH = "headerPath";
    }
}
