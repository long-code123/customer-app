package com.app.orderfoodapp.API;

import com.app.orderfoodapp.Model.Food;
import com.app.orderfoodapp.Model.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StoreAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    StoreAPI storeAPI = new Retrofit.Builder()
//            .baseUrl("http://192.168.1.2:8000")
            .baseUrl("http://10.0.4.233:8000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StoreAPI.class);

    @GET("/api/v1/stores")
    Call<List<Store>> getAllStores();

    @GET("/api/v1/stores/{id}/foods")
    Call<List<Food>> getFoodsByStore(@Path("id") int storeId);
}
