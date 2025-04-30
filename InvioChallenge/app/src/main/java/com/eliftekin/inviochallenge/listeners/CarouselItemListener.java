package com.eliftekin.inviochallenge.listeners;

import com.eliftekin.inviochallenge.model.LocationsList;

public interface CarouselItemListener {
    void onLocationSelected(LocationsList location);
    void onDetailClicked(LocationsList location);
}
