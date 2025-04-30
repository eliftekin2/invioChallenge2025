package com.eliftekin.inviochallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataList implements Parcelable {
    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("locations")
    @Expose
    private List<LocationsList> locationsLists;

    //parcelable metodları
    protected DataList(Parcel in) {
        city = in.readString();
        locationsLists = in.createTypedArrayList(LocationsList.CREATOR);
    }

    public static final Creator<DataList> CREATOR = new Creator<DataList>() {
        @Override
        public DataList createFromParcel(Parcel in) {
            return new DataList(in);
        }

        @Override
        public DataList[] newArray(int size) {
            return new DataList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(city);
        parcel.writeTypedList(locationsLists);
    }

    //getter ve setterlar
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<LocationsList> getLocationsLists() {
        return locationsLists;
    }

    public void setLocationsLists(List<LocationsList> locationsLists) {
        this.locationsLists = locationsLists;
    }

}


