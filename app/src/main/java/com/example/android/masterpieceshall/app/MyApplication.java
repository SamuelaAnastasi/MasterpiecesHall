package com.example.android.masterpieceshall.app;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.app.Application;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.masterpieceshall.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {
    private static final String LOG_TAG = MyApplication.class.getSimpleName();

    AdView adView;

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this, getString(R.string.admob_app_id));

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.banner_home_footer));

        // Request for Ads
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the Logcat to get your device ID to test ad on real device
//                .addTestDevice("YOUR_TEST_DEVICE_ID")
                .build();

        adView.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                Log.d(LOG_TAG, " Ad is closed!");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                if (i == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.d(LOG_TAG, "No ads available.");
                } else if (i == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                    Log.d(LOG_TAG, "Internet required to display ad.");
                } else {
                    Log.d(LOG_TAG, "Ad failed to load! Error code: " + i);
                }
            }

            @Override
            public void onAdLoaded() {
                Log.d(LOG_TAG, " Test Ad loaded!");
            }
        });

        // Load ads into Banner Ads
        adView.loadAd(adRequest);
    }

    public void loadAd(LinearLayout layAd) {

        // Locate the Banner Ad in activity xml
        if (adView.getParent() != null) {
            ViewGroup tempVg = (ViewGroup) adView.getParent();
            tempVg.removeView(adView);
        }

        layAd.addView(adView);
    }
}
