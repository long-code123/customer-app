package com.app.orderfoodapp.API;

import com.app.orderfoodapp.Model.LoginRequest;
import com.app.orderfoodapp.Model.LoginResponse;
import com.app.orderfoodapp.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    LoginAPI loginAPI = new Retrofit.Builder()
            .baseUrl("http://192.168.1.2:8000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LoginAPI.class);

    @POST("/api/v1/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("/api/v1/login/me")
    Call<User> getCurrentUser(@Header("Authorization") String token);
}

