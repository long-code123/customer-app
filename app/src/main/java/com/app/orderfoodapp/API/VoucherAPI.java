package com.app.orderfoodapp.API;

import com.app.orderfoodapp.Model.Category;
import com.app.orderfoodapp.Model.Voucher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface VoucherAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    VoucherAPI voucherAPI = new Retrofit.Builder()
            .baseUrl("http://192.168.1.2:8000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(VoucherAPI.class);

    @GET("/api/v1/vouchers")
    Call<List<Voucher>> getAllVouchers();
}
