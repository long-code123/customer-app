package com.app.orderfoodapp.API;

import com.app.orderfoodapp.Config.Constants;
import com.app.orderfoodapp.Model.Order; // Thay đổi tùy theo lớp Order của bạn
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    OrderAPI orderAPI = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OrderAPI.class);

    @GET("/api/v1/customer/users/{id}/last-order")
    Call<Order> getLastOrderByUser(@Path("id") String userId);
}