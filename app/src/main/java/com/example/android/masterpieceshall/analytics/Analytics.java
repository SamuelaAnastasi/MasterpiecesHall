package com.example.android.masterpieceshall.analytics;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.content.Context;
import android.os.Bundle;

import com.example.android.masterpieceshall.model.ArtObject;
import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics {

    public static void logEventViewItem(Context context, ArtObject artObject) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, artObject.getTitle());
        FirebaseAnalytics.getInstance(context)
                .logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
    }

    public static void logEventShare(Context context, String contentType) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        FirebaseAnalytics.getInstance(context)
                .logEvent(FirebaseAnalytics.Event.SHARE, params);
    }
}
