package com.example.android.masterpieceshall.async;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import com.example.android.masterpieceshall.model.EventsCalendar;

public interface EventsTaskListener {
    void onEventsTaskPrepared();

    void onEventsTaskExecuted(EventsCalendar eventsCalendar);
}
