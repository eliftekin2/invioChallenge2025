package com.eliftekin.inviochallenge.service;

import static com.eliftekin.inviochallenge.constants.AppConstants.BaseUrl;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getClient(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static CityApi getCityApiService() {
        return getClient().create(CityApi.class);
    }
}
