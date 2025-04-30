package com.eliftekin.inviochallenge.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritesDAO {

    //veritabanına ekle
    @Insert
    void insert(FavoritesEntity favorite);

    //veritabanından verileri çek
    @Query("SELECT * FROM favorites")
    LiveData<List<FavoritesEntity>> getFavorites();

    //veritabanından sil
    @Query("DELETE FROM favorites WHERE name = :name")
    void removeFromDB(String name);
}
