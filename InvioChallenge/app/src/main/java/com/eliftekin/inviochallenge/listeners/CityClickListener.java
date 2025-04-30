package com.eliftekin.inviochallenge.listeners;

import com.eliftekin.inviochallenge.model.DataList;
import com.eliftekin.inviochallenge.model.LocationsList;

public interface CityClickListener {
    void onCityMapClick(DataList data);
    void onDetailsClick(LocationsList locationsList);
    void onIconClickListener(int position, LocationsList locationsList);
    void onExpandClickListener(int position);
}
