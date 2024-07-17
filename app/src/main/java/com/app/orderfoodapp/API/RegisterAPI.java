package com.app.orderfoodapp.API;

import com.app.orderfoodapp.Model.RegisterRequest;
import com.app.orderfoodapp.Model.RegisterResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    RegisterAPI registerAPI = new Retrofit.Builder()
            .baseUrl("http://192.168.1.2:8000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RegisterAPI.class);
    @POST("/api/v1/users")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}
