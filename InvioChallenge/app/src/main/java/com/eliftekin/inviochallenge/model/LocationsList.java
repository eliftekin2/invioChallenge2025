package com.eliftekin.inviochallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationsList implements Parcelable {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;

    @SerializedName("image")
    @Expose
    private String imgUrl;

    //constructor
    public LocationsList(String name, String description, String imageUrl, Coordinates coordinates) {
        this.name = name;
        this.description = description;
        this.imgUrl = imageUrl;
        this.coordinates = coordinates;
    }

    //parcelable metodları
    public LocationsList(Parcel in) {
        name = in.readString();
        description = in.readString();
        imgUrl = in.readString();
        coordinates = in.readParcelable(Coordinates.class.getClassLoader());
    }

    public static final Creator<LocationsList> CREATOR = new Creator<LocationsList>() {
        @Override
        public LocationsList createFromParcel(Parcel in) {
            return new LocationsList(in);
        }

        @Override
        public LocationsList[] newArray(int size) {
            return new LocationsList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(imgUrl);
        parcel.writeParcelable(coordinates, i);
    }

    //getter ve setterlar
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
