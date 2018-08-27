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

public class WorkDetails implements Parcelable {
    @SerializedName("artObject")
    @Expose
    private ArtObjectDetails artObject;
    @SerializedName("artObjectPage")
    @Expose
    private ArtObjectPage artObjectPage;

    public WorkDetails() {
    }

    protected WorkDetails(Parcel in) {
        artObject = in.readParcelable(ArtObjectDetails.class.getClassLoader());
    }

    public static final Creator<WorkDetails> CREATOR = new Creator<WorkDetails>() {
        @Override
        public WorkDetails createFromParcel(Parcel in) {
            return new WorkDetails(in);
        }

        @Override
        public WorkDetails[] newArray(int size) {
            return new WorkDetails[size];
        }
    };

    public ArtObjectDetails getArtObject() {
        return artObject;
    }

    public void setArtObject(ArtObjectDetails artObject) {
        this.artObject = artObject;
    }

    public ArtObjectPage getArtObjectPage() {
        return artObjectPage;
    }

    public void setArtObjectPage(ArtObjectPage artObjectPage) {
        this.artObjectPage = artObjectPage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(artObject, i);
    }
}
