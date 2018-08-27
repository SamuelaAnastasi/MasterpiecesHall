package com.example.android.masterpieceshall.widget;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.android.masterpieceshall.database.ArtsContract;
import com.example.android.masterpieceshall.model.ArtObject;
import com.example.android.masterpieceshall.model.HeaderImage;
import com.example.android.masterpieceshall.model.WebImage;

public class ArtWidgetService extends IntentService {

    public static final String ACTION_LOAD_ART_OBJECT
            = "com.example.android.masterpieceshall.action.load_art_object";

    public ArtWidgetService() {
        super("ArtWidgetService");
    }

    public static void startActionLoadArtObject(Context context) {
        Intent intent = new Intent(context, ArtWidgetService.class);
        intent.setAction(ACTION_LOAD_ART_OBJECT);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_ART_OBJECT.equals(action)) {
                handleActionLoadArtObject();
            }
        }
    }

    private void handleActionLoadArtObject() {

        ArtObject artObject = null;

        Cursor cursor = getContentResolver().query(
                ArtsContract.ArtsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            artObject = new ArtObject();
            artObject.setObjectNumber(cursor.getString(
                    cursor.getColumnIndex(ArtsContract.ArtsEntry.COLUMN_ART_OBJECT_NUMBER)));
            artObject.setPrincipalOrFirstMaker(cursor.getString(
                    cursor.getColumnIndex(ArtsContract.ArtsEntry.COLUMN_ART_AUTHOR)));
            artObject.setTitle(cursor.getString(
                    cursor.getColumnIndex(ArtsContract.ArtsEntry.COLUMN_ART_TITLE)));
            artObject.setLongTitle(cursor.getString(
                    cursor.getColumnIndex(ArtsContract.ArtsEntry.COLUMN_ART_LONG_TITLE)));
            WebImage webImage = new WebImage();
            webImage.setUrl(cursor.getString(
                    cursor.getColumnIndex(ArtsContract.ArtsEntry.COLUMN_ART_IMAGE_PATH)));
            artObject.setWebImage(webImage);
            HeaderImage headerImage = new HeaderImage();
            headerImage.setUrl(cursor.getString(
                    cursor.getColumnIndex(ArtsContract.ArtsEntry.COLUMN_ART_HEADER_PATH)));
            artObject.setHeaderImage(headerImage);
        }
        if (cursor != null) {
            cursor.close();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ArtAppWidget.class));

        ArtAppWidget.updateArtObjectWidgets(this, appWidgetManager, artObject, appWidgetIds);
    }
}
