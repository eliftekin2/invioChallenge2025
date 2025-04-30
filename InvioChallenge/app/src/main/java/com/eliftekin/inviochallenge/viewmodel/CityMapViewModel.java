package com.eliftekin.inviochallenge.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eliftekin.inviochallenge.model.DataList;
import com.eliftekin.inviochallenge.model.LocationsList;

import java.util.List;

public class CityMapViewModel extends ViewModel {
    private final MutableLiveData<DataList> _city = new MutableLiveData<>();
    public LiveData<DataList> city = _city;

    private final MutableLiveData<List<LocationsList>> _location = new MutableLiveData<>();
    public LiveData<List<LocationsList>> location = _location;

    public void setCity(DataList city){
        _city.setValue(city);
        setLocation();
    }

    public void setLocation(){
        DataList cityData = _city.getValue();
        if (cityData != null && cityData.getLocationsLists() != null) {
            _location.setValue(cityData.getLocationsLists());
        }
    }
}
