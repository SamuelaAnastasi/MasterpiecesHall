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

import java.util.List;

public class WorkData implements Parcelable {
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("artObjects")
    @Expose
    private List<ArtObject> artObjects = null;

    public WorkData() {
    }

    protected WorkData(Parcel in) {
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readInt();
        }
        artObjects = in.createTypedArrayList(ArtObject.CREATOR);
    }

    public static final Creator<WorkData> CREATOR = new Creator<WorkData>() {
        @Override
        public WorkData createFromParcel(Parcel in) {
            return new WorkData(in);
        }

        @Override
        public WorkData[] newArray(int size) {
            return new WorkData[size];
        }
    };

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ArtObject> getArtObjects() {
        return artObjects;
    }

    public void setArtObjects(List<ArtObject> artObjects) {
        this.artObjects = artObjects;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (count == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(count);
        }
        parcel.writeTypedList(artObjects);
    }
}
