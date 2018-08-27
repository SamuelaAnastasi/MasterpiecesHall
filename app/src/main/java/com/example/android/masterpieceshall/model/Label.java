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

public class Label implements Parcelable{
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("makerLine")
    @Expose
    private String makerLine;
    @SerializedName("description")
    @Expose
    private String description;

    public Label() {
    }

    protected Label(Parcel in) {
        title = in.readString();
        makerLine = in.readString();
        description = in.readString();
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel in) {
            return new Label(in);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };

    public Label(String title, String makerLine, String description) {
        this.title = title;
        this.makerLine = makerLine;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMakerLine() {
        return makerLine;
    }

    public void setMakerLine(String makerLine) {
        this.makerLine = makerLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(makerLine);
        parcel.writeString(description);
    }
}
