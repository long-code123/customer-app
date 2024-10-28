package com.app.orderfoodapp.API;

import com.app.orderfoodapp.Config.Constants;
import com.app.orderfoodapp.Model.Food;
import com.app.orderfoodapp.Model.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.List;

public class StoreOKAPI {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    // Phương thức lấy tất cả các cửa hàng
    public void getAllStores(final StoresCallback callback) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/api/v1/customer/stores")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    List<Store> stores = gson.fromJson(jsonData, new com.google.gson.reflect.TypeToken<List<Store>>() {}.getType());
                    callback.onSuccess(stores);
                } else {
                    callback.onError("Failed to get stores");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Phương thức lấy địa chỉ cửa hàng
    public void getStoreAddress(int storeId, final AddressCallback callback) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/api/v1/customer/stores/" + storeId + "/address")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    Store store = gson.fromJson(jsonData, Store.class);
                    callback.onSuccess(store.getAddress());
                } else {
                    callback.onError("Failed to get store address");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Giao diện callback cho danh sách cửa hàng
    public interface StoresCallback {
        void onSuccess(List<Store> stores);
        void onError(String errorMessage);
    }

    // Giao diện callback cho địa chỉ cửa hàng
    public interface AddressCallback {
        void onSuccess(String address);
        void onError(String errorMessage);
    }
}
