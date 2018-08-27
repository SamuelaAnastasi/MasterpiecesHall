package com.example.android.masterpieceshall.model;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Period implements Parcelable {

    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("text")
    @Expose
    private String text;
    public final static Parcelable.Creator<Period> CREATOR = new Creator<Period>() {

        @SuppressWarnings({"unchecked"})
        public Period createFromParcel(Parcel in) {
            return new Period(in);
        }

        public Period[] newArray(int size) {
            return (new Period[size]);
        }
    };

    protected Period(Parcel in) {
        this.startDate = ((String) in.readValue((String.class.getClassLoader())));
        this.text = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Period() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(startDate);
        dest.writeValue(text);
    }

    public int describeContents() {
        return 0;
    }

}