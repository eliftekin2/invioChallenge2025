package com.eliftekin.inviochallenge.listeners;

import com.eliftekin.inviochallenge.database.FavoritesEntity;

public interface FavoritesListener {
    void onItemClick(FavoritesEntity favorites);
    void onFavRemoveClick(FavoritesEntity favorites);
}
