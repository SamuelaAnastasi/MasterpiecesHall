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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.masterpieceshall.R;

public class Utils {

    private Utils() {}

    public static boolean isTabletPortrait(Context context) {
        return context.getResources().getBoolean(R.bool.isTabletPortrait);
    }

    public static boolean isPhonePortrait(Context context) {
        return context.getResources().getBoolean(R.bool.isPhonePortrait);
    }

    public static boolean isPhoneLand(Context context) {
        return context.getResources().getBoolean(R.bool.isPhoneLand);
    }

    public static boolean isTabletLand(Context context) {
        return context.getResources().getBoolean(R.bool.isTabletLand);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
        return false;
    }
}
