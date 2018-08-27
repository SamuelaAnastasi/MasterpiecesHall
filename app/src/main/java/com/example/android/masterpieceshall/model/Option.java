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

public class Option implements Parcelable {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("period")
    @Expose
    private Period period;
    @SerializedName("exposition")
    @Expose
    private Exposition exposition;
    @SerializedName("pageRef")
    @Expose
    private PageRef pageRef;

    public final static Parcelable.Creator<Option> CREATOR = new Creator<Option>() {

        @SuppressWarnings({"unchecked"})
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        public Option[] newArray(int size) {
            return (new Option[size]);
        }
    };

    protected Option(Parcel in) {
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.period = ((Period) in.readValue((Period.class.getClassLoader())));
        this.exposition = ((Exposition) in.readValue((Exposition.class.getClassLoader())));
        this.pageRef = ((PageRef) in.readValue((PageRef.class.getClassLoader())));
    }

    public Option() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Exposition getExposition() {
        return exposition;
    }

    public void setExposition(Exposition exposition) {
        this.exposition = exposition;
    }

    public PageRef getPageRef() {
        return pageRef;
    }

    public void setPageRef(PageRef pageRef) {
        this.pageRef = pageRef;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(date);
        dest.writeValue(period);
        dest.writeValue(exposition);
        dest.writeValue(pageRef);
    }

    public int describeContents() {
        return 0;
    }

}