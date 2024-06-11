package com.app.orderfoodapp.API;

import com.app.orderfoodapp.Model.Category;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface CategoryAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    CategoryAPI categoryAPI = new Retrofit.Builder()
            .baseUrl("http://10.0.4.233:8000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CategoryAPI.class);

    @GET("/api/v1/categories")
    Call<List<Category>> getAllCategories();
}
