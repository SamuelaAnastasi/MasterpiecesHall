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

public class ArtObject implements Parcelable{
    @SerializedName("objectNumber")
    @Expose
    private String objectNumber;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("longTitle")
    @Expose
    private String longTitle;
    @SerializedName("principalOrFirstMaker")
    @Expose
    private String principalOrFirstMaker;
    @SerializedName("webImage")
    @Expose
    private WebImage webImage;
    @SerializedName("headerImage")
    @Expose
    private HeaderImage headerImage;

    public ArtObject() {
    }

    protected ArtObject(Parcel in) {
        objectNumber = in.readString();
        title = in.readString();
        longTitle = in.readString();
        principalOrFirstMaker = in.readString();
        webImage = in.readParcelable(WebImage.class.getClassLoader());
        headerImage = in.readParcelable(HeaderImage.class.getClassLoader());
    }

    public static final Creator<ArtObject> CREATOR = new Creator<ArtObject>() {
        @Override
        public ArtObject createFromParcel(Parcel in) {
            return new ArtObject(in);
        }

        @Override
        public ArtObject[] newArray(int size) {
            return new ArtObject[size];
        }
    };

    public String getObjectNumber() {
        return objectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        this.objectNumber = objectNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLongTitle() {
        return longTitle;
    }

    public void setLongTitle(String longTitle) {
        this.longTitle = longTitle;
    }

    public String getPrincipalOrFirstMaker() {
        return principalOrFirstMaker;
    }

    public void setPrincipalOrFirstMaker(String principalOrFirstMaker) {
        this.principalOrFirstMaker = principalOrFirstMaker;
    }

    public WebImage getWebImage() {
        return webImage;
    }

    public void setWebImage(WebImage webImage) {
        this.webImage = webImage;
    }

    public HeaderImage getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(HeaderImage headerImage) {
        this.headerImage = headerImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(objectNumber);
        parcel.writeString(title);
        parcel.writeString(longTitle);
        parcel.writeString(principalOrFirstMaker);
        parcel.writeParcelable(webImage, i);
        parcel.writeParcelable(headerImage, i);
    }
}
