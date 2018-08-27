package com.example.android.masterpieceshall.api;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import com.example.android.masterpieceshall.model.EventsCalendar;
import com.example.android.masterpieceshall.model.WorkData;
import com.example.android.masterpieceshall.model.WorkDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiInterface {
    @GET("en/collection")
    Call<WorkData> getWorkData(
            @QueryMap Map<String, String> params);

    @GET("en/collection/{objectNumber}")
    Call<WorkDetails> getWorkDetails(@Path("objectNumber") String objectNumber,
                                     @QueryMap Map<String, String> params);

    @GET("en/agenda/{currentDate}")
    Call<EventsCalendar> getCurrentEvents(@Path("currentDate") String currentDate,
                                          @QueryMap Map<String, String> params);
}

