package com.example.android.masterpieceshall.api;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.content.Context;

import com.example.android.masterpieceshall.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Retrofit;

import static com.example.android.masterpieceshall.utilities.Utils.isConnected;

public class ApiClientGenerator {
    private static final String BASE_URL = "https://www.rijksmuseum.nl/api/";
    private  static Retrofit retrofit;

    // Create GsonConverter factory instance;
    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation().create();

    private static GsonConverterFactory gsonFactory = GsonConverterFactory.create(gson);

    // OkHttpClient builder and Interceptor
    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
            .connectTimeout(30,TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS);

    private static final HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    // OkHttpClient and interceptors
    private static OkHttpClient getCacheClient(final Context context) {

        final Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                String cacheControl = originalResponse.header("Cache-Control");
                if (cacheControl == null || cacheControl.contains("no-store") ||
                        cacheControl.contains("no-cache") ||
                        cacheControl.contains("must-revalidate") ||
                        cacheControl.contains("max-age=0")) {
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + 5000)
                            .build();
                } else {
                    return originalResponse;
                }
            }
        };

        final int maxStale = 60 * 60 * 24 * 7;

        final Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isConnected(context)) {
                    request = request.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
                return chain.proceed(request);
            }
        };

        // create cache
        File httpCacheDirectory = new File(context.getCacheDir(), "cacheretrofit");
        int size = 10 * 1024 * 1024;
        Cache cache = new Cache(httpCacheDirectory, size);

        if(BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
        }

        return okHttpClientBuilder
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE)
                .cache(cache)
                .build();
    }

    public static Retrofit getClient(final Context context) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getCacheClient(context))
                    .baseUrl(BASE_URL)
                    .addConverterFactory(gsonFactory)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient() {
        OkHttpClient okHttpClient = okHttpClientBuilder
                .addInterceptor(loggingInterceptor)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(gsonFactory)
                    .build();
        }
        return retrofit;
    }
}
