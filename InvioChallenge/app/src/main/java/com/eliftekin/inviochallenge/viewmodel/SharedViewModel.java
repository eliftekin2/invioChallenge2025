package com.eliftekin.inviochallenge.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eliftekin.inviochallenge.database.FavoritesEntity;
import com.eliftekin.inviochallenge.model.CityListModel;
import com.eliftekin.inviochallenge.model.DataList;
import com.eliftekin.inviochallenge.repo.CityRepo;
import com.eliftekin.inviochallenge.repo.FavoritesRepo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedViewModel extends AndroidViewModel {
    private final CityRepo cityRepo = new CityRepo();

    //sayfa bilgileri ve şehirler listesi
    private final MutableLiveData<List<DataList>> _cityList = new MutableLiveData<>();
    public LiveData<List<DataList>> dataList = _cityList;

    List<DataList> dataListHolder = new ArrayList<>();

    //hata mesajı
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    private int currentPage = 1;
    private boolean isLoading;

    //kartların açık/kapalı durumları
    private final MutableLiveData<Set<Integer>> _expandedPositions = new MutableLiveData<>(new HashSet<>());
    public LiveData<Set<Integer>> expandedPositions = _expandedPositions;

    //room işlemleri için
    private final FavoritesRepo favoritesRepo;
    private final LiveData<List<FavoritesEntity>> favorites;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        favoritesRepo = new FavoritesRepo(application);
        favorites = favoritesRepo.getFavorites();
    }

    public void fetchFirstPage(){
        fetchData(currentPage);
    }

    public void fetchNextPage() {
        fetchData(currentPage);
    }

    private void fetchData(int page_no){
        if (!isLoading){
            isLoading = true;

            cityRepo.getCities(page_no, new CityRepo.CityListCallBack() {
                @Override
                public void onSuccess(CityListModel cityListModel) {
                    if(cityListModel != null){
                        int totalPage = cityListModel.getTotalPage();

                        if(currentPage <= totalPage){
                            dataListHolder.addAll(cityListModel.getDataList());
                            _cityList.setValue(dataListHolder);
                            currentPage++;
                        }
                    }

                    isLoading = false;
                }

                @Override
                public void onFailed(String errorMessage) {
                    _errorMessage.setValue(errorMessage);

                    isLoading = false;

                }
            });
        }
    }

    //açık tüm listelerin kapanması
    public void collapseAll(){
        _expandedPositions.setValue(new HashSet<>());
    }

    //favorilere ekleme
    public void insertFavorite(FavoritesEntity favorite) {
        favoritesRepo.insertFavorite(favorite);
    }

    //favorilerden çıkarma
    public void removeFavorite(String name) {
        favoritesRepo.removeFavorite(name);
    }

    //favoriler listesini çeker
    public LiveData<List<FavoritesEntity>> getFavorites() {
        return favorites;
    }

    public void togglePosition(int position) {
        Set<Integer> value = _expandedPositions.getValue();
        if (value == null) value = new HashSet<>();

        if (value.contains(position))
            value.remove(position);
        else
            value.add(position);

        _expandedPositions.setValue(value);
    }
}
