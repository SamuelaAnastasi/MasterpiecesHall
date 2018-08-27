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

public class ArtObjectPage implements Parcelable {
    @SerializedName("plaqueDescription")
    @Expose
    private String plaqueDescription;

    public ArtObjectPage() {
    }

    protected ArtObjectPage(Parcel in) {
        plaqueDescription = in.readString();
    }

    public static final Creator<ArtObjectPage> CREATOR = new Creator<ArtObjectPage>() {
        @Override
        public ArtObjectPage createFromParcel(Parcel in) {
            return new ArtObjectPage(in);
        }

        @Override
        public ArtObjectPage[] newArray(int size) {
            return new ArtObjectPage[size];
        }
    };

    public String getPlaqueDescription() {
        return plaqueDescription;
    }

    public void setPlaqueDescription(String plaqueDescription) {
        this.plaqueDescription = plaqueDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(plaqueDescription);
    }
}
