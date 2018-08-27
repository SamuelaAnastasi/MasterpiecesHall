package com.example.android.masterpieceshall.utilities;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import java.util.concurrent.TimeUnit;

public class Constants {
    private Constants() {}

    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static final String EVENTS_AGENDA_URL = "https://www.rijksmuseum.nl/en/agenda/";

    public static final String KEY_API_KEY = "key";
    public static final String KEY_FORMAT = "format";
    public static final String FORMAT_JSON = "json";
    public static final String KEY_IMG_ONLY = "imgonly";
    public static final String IMG_ONLY_TRUE = "true";
    public static final String KEY_PAGE_NUMBER = "p";
    public static final String PAGE_NUMBER_VALUE = "1";
    public static final String KEY_OBJECTS_NUMBER = "ps";
    public static final String OBJECTS_NUMBER_VALUE = "100";
    public static final String KEY_ART_TYPE = "type";
    public static final String ART_TYPE_PAINTING = "painting";
    public static final String ART_TYPE_FIGURE = "figure";
}
