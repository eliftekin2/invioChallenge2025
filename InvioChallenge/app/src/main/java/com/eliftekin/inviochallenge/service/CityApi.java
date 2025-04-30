package com.eliftekin.inviochallenge.service;

import com.eliftekin.inviochallenge.model.CityListModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CityApi {
    @GET("page-{page}.json")
    Call<CityListModel> getCityList(@Path("page") int page);
}
