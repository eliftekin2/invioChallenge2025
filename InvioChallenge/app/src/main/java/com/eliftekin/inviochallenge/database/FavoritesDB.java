package com.eliftekin.inviochallenge.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoritesEntity.class}, version = 1)
public abstract class FavoritesDB extends RoomDatabase {

    public abstract FavoritesDAO favoritesDAO();
    private static FavoritesDB instance;

    public static FavoritesDB getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FavoritesDB.class,
                    "favoritesDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
