package com.eliftekin.inviochallenge.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eliftekin.inviochallenge.model.LocationsList;

public class LocationMapViewModel extends ViewModel {

    private final MutableLiveData<LocationsList> _location = new MutableLiveData<>();
    public LiveData<LocationsList> location = _location;

    public void setLocation(LocationsList location) {
        _location.setValue(location);
    }
}
