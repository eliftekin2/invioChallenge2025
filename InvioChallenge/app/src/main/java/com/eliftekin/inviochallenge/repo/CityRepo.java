package com.eliftekin.inviochallenge.repo;

import com.eliftekin.inviochallenge.model.CityListModel;
import com.eliftekin.inviochallenge.service.CityApi;
import com.eliftekin.inviochallenge.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityRepo {

    private final CityApi cityApi;

    public CityRepo() {
        cityApi = RetrofitClient.getCityApiService();
    }

    public void getCities(int page, CityListCallBack callBack){
        cityApi.getCityList(page).enqueue(new Callback<CityListModel>() {
            @Override
            public void onResponse(Call<CityListModel> call, Response<CityListModel> response) {
                if(response.isSuccessful()){
                    if(callBack != null)
                        callBack.onSuccess(response.body());
                }
                else{
                    if(callBack != null)
                        callBack.onFailed("Hata!");
                }
            }

            @Override
            public void onFailure(Call<CityListModel> call, Throwable t) {
                if(callBack != null)
                    callBack.onFailed("Hata mesajı: " + t);
            }
        });
    }

    public interface CityListCallBack{
        void onSuccess(CityListModel cityListModel);
        void onFailed(String errorMessage);
    }
}
