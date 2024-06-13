package com.app.orderfoodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.orderfoodapp.API.CategoryAPI;
import com.app.orderfoodapp.API.StoreAPI;
import com.app.orderfoodapp.Adapter.FoodAdapter;
import com.app.orderfoodapp.Model.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodsByStoreActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFoods;
    private FoodAdapter foodAdapter;
    private TextView tvStoreTitle;
    private ImageView btnBack;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods_by_store);

        tvStoreTitle = findViewById(R.id.tvStoreTitle);
        recyclerViewFoods = findViewById(R.id.recyclerViewFoods);
        btnBack = findViewById(R.id.btnBack);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewFoods.setLayoutManager(layoutManager);

        String storeName = getIntent().getStringExtra("storeName");
        int storeId = getIntent().getIntExtra("storeId", -1);

        if (storeName != null) {
            tvStoreTitle.setText("List Food Of " + storeName);
        }

        // Gọi API để lấy danh sách món ăn theo danh mục
        if (storeId != -1) {
            fetchFoodsByStore(storeId);
        } else {
            Toast.makeText(this, "Invalid Store ID", Toast.LENGTH_SHORT).show();
        }

        btnBack.setOnClickListener(v -> {
            // Trở về MainActivity
            Intent intent = new Intent(FoodsByStoreActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void fetchFoodsByStore(int storeId) {
        StoreAPI.storeAPI.getFoodsByStore(storeId).enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    List<Food> foods = response.body();
                    if (foods != null && !foods.isEmpty()) {
                        foodAdapter = new FoodAdapter(foods, FoodsByStoreActivity.this);
                        recyclerViewFoods.setAdapter(foodAdapter);
                    } else {
                        Toast.makeText(FoodsByStoreActivity.this, "No foods found for this category.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("FoodsByStore", "Response code: " + response.code());
                    Log.e("FoodsByStore", "Response error body: " + response.errorBody());
                    Toast.makeText(FoodsByStoreActivity.this, "Failed to fetch foods. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.e("FoodsByCategory", "Error: " + t.getMessage(), t);
                Toast.makeText(FoodsByStoreActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}