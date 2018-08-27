package com.example.android.masterpieceshall.async;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.os.AsyncTask;

import com.example.android.masterpieceshall.api.ApiClientGenerator;
import com.example.android.masterpieceshall.api.ApiInterface;
import com.example.android.masterpieceshall.model.EventsCalendar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.android.masterpieceshall.BuildConfig.API_KEY;
import static com.example.android.masterpieceshall.utilities.Constants.FORMAT_JSON;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_API_KEY;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_FORMAT;

public class EventsAsyncTask extends AsyncTask<String, Void, EventsCalendar> {

    private EventsTaskListener eventsTaskListener;

    public EventsAsyncTask(EventsTaskListener eventsTaskListener) {
        this.eventsTaskListener = eventsTaskListener;
    }

    @Override
    protected void onPreExecute() {
        eventsTaskListener.onEventsTaskPrepared();
    }

    @Override
    protected EventsCalendar doInBackground(String... queryDateArray) {

        Map<String, String> params = getQueryMap();
        String queryDate = queryDateArray[0];
        ApiInterface apiInterface = ApiClientGenerator.getClient().create(ApiInterface.class);
        Call<EventsCalendar> call = apiInterface.getCurrentEvents(queryDate, params);

        try {
            Response<EventsCalendar> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(EventsCalendar eventsCalendar) {
        eventsTaskListener.onEventsTaskExecuted(eventsCalendar);
    }

    private HashMap<String, String> getQueryMap() {
        HashMap<String, String> params = new HashMap<>();
        params.put(KEY_API_KEY, API_KEY);
        params.put(KEY_FORMAT, FORMAT_JSON);
        return params;
    }
}
