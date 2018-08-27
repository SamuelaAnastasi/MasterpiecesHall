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

public class ArtObjectDetails implements Parcelable{
    @SerializedName("objectNumber")
    @Expose
    private String objectNumber;
    @SerializedName("subTitle")
    @Expose
    private String subTitle;
    @SerializedName("plaqueDescriptionEnglish")
    @Expose
    private String plaqueDescriptionEnglish;
    @SerializedName("physicalMedium")
    @Expose
    private String physicalMedium;
    @SerializedName("webImage")
    @Expose
    private WebImage webImage;
    @SerializedName("label")
    @Expose
    private Label label;

    public ArtObjectDetails() {
    }

    protected ArtObjectDetails(Parcel in) {
        objectNumber = in.readString();
        subTitle = in.readString();
        plaqueDescriptionEnglish = in.readString();
        physicalMedium = in.readString();
        webImage = in.readParcelable(WebImage.class.getClassLoader());
        label = in.readParcelable(Label.class.getClassLoader());
    }

    public static final Creator<ArtObjectDetails> CREATOR = new Creator<ArtObjectDetails>() {
        @Override
        public ArtObjectDetails createFromParcel(Parcel in) {
            return new ArtObjectDetails(in);
        }

        @Override
        public ArtObjectDetails[] newArray(int size) {
            return new ArtObjectDetails[size];
        }
    };

    public ArtObjectDetails(String objectNumber, String subTitle, String plaqueDescriptionEnglish, String physicalMedium, WebImage webImage, Label label) {
        this.objectNumber = objectNumber;
        this.subTitle = subTitle;
        this.plaqueDescriptionEnglish = plaqueDescriptionEnglish;
        this.physicalMedium = physicalMedium;
        this.webImage = webImage;
        this.label = label;
    }

    public String getObjectNumber() {
        return objectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        this.objectNumber = objectNumber;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPlaqueDescriptionEnglish() {
        return plaqueDescriptionEnglish;
    }

    public void setPlaqueDescriptionEnglish(String plaqueDescriptionEnglish) {
        this.plaqueDescriptionEnglish = plaqueDescriptionEnglish;
    }

    public String getPhysicalMedium() {
        return physicalMedium;
    }

    public void setPhysicalMedium(String physicalMedium) {
        this.physicalMedium = physicalMedium;
    }

    public WebImage getWebImage() {
        return webImage;
    }

    public void setWebImage(WebImage webImage) {
        this.webImage = webImage;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(objectNumber);
        parcel.writeString(subTitle);
        parcel.writeString(plaqueDescriptionEnglish);
        parcel.writeString(physicalMedium);
        parcel.writeParcelable(webImage, i);
        parcel.writeParcelable(label, i);
    }
}
