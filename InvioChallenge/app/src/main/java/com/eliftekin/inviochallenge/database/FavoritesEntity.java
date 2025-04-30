package com.eliftekin.inviochallenge.database;

import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.eliftekin.inviochallenge.model.Coordinates;
import com.eliftekin.inviochallenge.model.LocationsList;

@Entity(tableName = "favorites")
public class FavoritesEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private String imageUrl;
    private double latitude;
    private double longitude;

    //constructor
    public FavoritesEntity(String name, String description, String imageUrl, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //getter ve setterlar
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Parcelable toLocationsList() {
        return new LocationsList(name, description, imageUrl, new Coordinates(latitude, longitude));
    }
}
