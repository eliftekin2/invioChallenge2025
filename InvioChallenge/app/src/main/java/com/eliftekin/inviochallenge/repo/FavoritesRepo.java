package com.eliftekin.inviochallenge.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.eliftekin.inviochallenge.database.FavoritesDAO;
import com.eliftekin.inviochallenge.database.FavoritesDB;
import com.eliftekin.inviochallenge.database.FavoritesEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoritesRepo {
    private FavoritesDAO favoritesDAO;
    private Executor executor;

    public FavoritesRepo(Context context) {
        FavoritesDB favoritesDB = FavoritesDB.getInstance(context);
        favoritesDAO = favoritesDB.favoritesDAO();

        executor = Executors.newSingleThreadExecutor();
    }

    public void insertFavorite(FavoritesEntity favorites){
        executor.execute(() -> favoritesDAO.insert(favorites));
    }

    public LiveData<List<FavoritesEntity>> getFavorites(){
        return favoritesDAO.getFavorites();
    }

    public void removeFavorite(String name){
        executor.execute(() -> favoritesDAO.removeFromDB(name));
    }
}
