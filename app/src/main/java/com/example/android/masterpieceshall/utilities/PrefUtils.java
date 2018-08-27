package com.example.android.masterpieceshall.utilities;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.masterpieceshall.R;

import static com.example.android.masterpieceshall.utilities.Constants.ART_TYPE_PAINTING;

public class PrefUtils {
    private PrefUtils() {}

    private static final String LOG_TAG = PrefUtils.class.getSimpleName();

    private static final String NAVIGATION_OPTION = "lastNavOption";
    private static final String TOOLBAR_TITLE = "lastToolbarTitle";
    private static final String ART_TYPE = "lastArtType";


    public static String getLastNavOption(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultNavOption = context.getString(R.string.pref_option_paintings);
        Log.v(LOG_TAG, "get last selected navigation option: " +
                sharedPreferences.getString(NAVIGATION_OPTION, defaultNavOption));
        return sharedPreferences.getString(NAVIGATION_OPTION, defaultNavOption);
    }

    public static void setLastNavOption(Context context, String navOption) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAVIGATION_OPTION, navOption);
        Log.v(LOG_TAG,"set last selected navigation option" + navOption);
        editor.apply();
    }

    public static String getLastToolbarTitle(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultToolbarTitle = context.getString(R.string.pref_title_paintings);
        Log.v(LOG_TAG, "get last selected navigation option: " +
                sharedPreferences.getString(TOOLBAR_TITLE, defaultToolbarTitle));
        return sharedPreferences.getString(TOOLBAR_TITLE, defaultToolbarTitle);
    }

    public static void setLastToolbarTitle(Context context, String toolbarTitle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOOLBAR_TITLE, toolbarTitle);
        Log.v(LOG_TAG,"set last selected navigation option" + toolbarTitle);
        editor.apply();
    }

    public static String getLastArtType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultArtType = ART_TYPE_PAINTING;
        Log.v(LOG_TAG, "get last selected art type: " +
                sharedPreferences.getString(ART_TYPE, defaultArtType));
        return sharedPreferences.getString(ART_TYPE, defaultArtType);
    }

    public static void setLastArtType(Context context, String artType) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ART_TYPE, artType);
        Log.v(LOG_TAG,"set last selected art Type" + artType);
        editor.apply();
    }
}
