package com.eliftekin.inviochallenge.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.eliftekin.inviochallenge.database.FavoritesEntity;
import com.eliftekin.inviochallenge.repo.FavoritesRepo;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private final FavoritesRepo favoritesRepo;
    private final LiveData<List<FavoritesEntity>> favoritesList;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        favoritesRepo = new FavoritesRepo(application);
        favoritesList = favoritesRepo.getFavorites();
    }

    public LiveData<List<FavoritesEntity>> getFavorites() {
        return favoritesList;
    }

    public void removeFavorite(String name) {
        favoritesRepo.removeFavorite(name);
    }
}
