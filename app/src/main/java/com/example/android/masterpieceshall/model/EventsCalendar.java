package com.example.android.masterpieceshall.model;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventsCalendar implements Parcelable {
    @SerializedName("options")
    @Expose
    private List<Option> options = null;

    public EventsCalendar() {
    }

    protected EventsCalendar(Parcel in) {
        options = in.createTypedArrayList(Option.CREATOR);
    }

    public static final Creator<EventsCalendar> CREATOR = new Creator<EventsCalendar>() {
        @Override
        public EventsCalendar createFromParcel(Parcel in) {
            return new EventsCalendar(in);
        }

        @Override
        public EventsCalendar[] newArray(int size) {
            return new EventsCalendar[size];
        }
    };

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(options);
    }
}