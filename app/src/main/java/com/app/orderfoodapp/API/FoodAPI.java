package com.app.orderfoodapp.API;

import com.app.orderfoodapp.Model.Food;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface FoodAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    FoodAPI foodAPI = new Retrofit.Builder()
//            .baseUrl("http://192.168.1.2:8000")
            .baseUrl("http://10.0.4.233:8000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FoodAPI.class);

    @GET("/api/v1/foods")
    Call<List<Food>> getAllFood();
}