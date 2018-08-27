package com.example.android.masterpieceshall.widget;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.model.ArtObject;
import com.example.android.masterpieceshall.ui.info.WorkDetailsActivity;
import com.example.android.masterpieceshall.ui.main.WorksCollectionActivity;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class ArtAppWidget extends AppWidgetProvider {
    private static final String LOG_TAG = ArtAppWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                ArtObject artObject, int appWidgetId) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.art_app_widget);

        if (artObject != null) {
            Log.e(LOG_TAG, " " + artObject.getObjectNumber());
            final int[] widgetId = new int[] {appWidgetId};
            views.setTextViewText(R.id.appwidget_text, artObject.getLongTitle());
            final String imageUrl = artObject.getWebImage().getUrl();

            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.get()
                            .load(imageUrl)
                            .resize(460, 460)
                            .centerCrop()
                            .into(views, R.id.widgetIv, widgetId);
                }
            });

            Intent detailsIntent = new Intent(context, WorkDetailsActivity.class);
            Bundle bundle = new Bundle();
            Log.d(LOG_TAG, " Object Number: " + artObject.getObjectNumber());
            bundle.putParcelable(WorkDetailsActivity.CURRENT_ART_OBJECT, artObject);
            detailsIntent.putExtras(bundle);
            PendingIntent detailsPendingIntent = PendingIntent.getActivity(context, 0,
                    detailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.widgetIv, detailsPendingIntent);

        } else {

            CharSequence widgetArtTitle = context.getString(R.string.widgetNoFavorites);
            views.setImageViewResource(R.id.widgetIv, R.drawable.web_image_2);
            views.setTextViewText(R.id.appwidget_text, widgetArtTitle);


            Intent mainIntent = new Intent(context, WorksCollectionActivity.class);
            PendingIntent detailsPendingIntent = PendingIntent.getActivity(context, 0,
                    mainIntent, 0);

            views.setOnClickPendingIntent(R.id.widgetIv, detailsPendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ArtWidgetService.startActionLoadArtObject(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateArtObjectWidgets(Context context,
                                              AppWidgetManager appWidgetManager,
                                              ArtObject artObject, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
                 updateAppWidget(context, appWidgetManager, artObject, appWidgetId);
        }
    }
}


