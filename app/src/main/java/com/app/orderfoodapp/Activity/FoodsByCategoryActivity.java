package com.app.orderfoodapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.orderfoodapp.API.CategoryAPI;
import com.app.orderfoodapp.Adapter.FoodAdapter;
import com.app.orderfoodapp.MainActivity;
import com.app.orderfoodapp.Model.Food;
import com.app.orderfoodapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodsByCategoryActivity extends AppCompatActivity {
    private RelativeLayout cartBar;
    private TextView tvCartItems, tvCartTotal;
    private Button btnViewCart;
    private RecyclerView recyclerViewFoods;
    private FoodAdapter foodAdapter;
    private TextView tvCategoryTitle;
    private ImageView btnBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods_by_category);


        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        recyclerViewFoods = findViewById(R.id.recyclerViewFoods);
        btnBack = findViewById(R.id.btnBack);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewFoods.setLayoutManager(layoutManager);

        // Nhận dữ liệu từ Intent
        String categoryName = getIntent().getStringExtra("categoryName");
        int categoryId = getIntent().getIntExtra("categoryId", -1);

        if (categoryName != null) {
            tvCategoryTitle.setText("List Food Of " + categoryName);
        }

        // Gọi API để lấy danh sách món ăn theo danh mục
        if (categoryId != -1) {
            fetchFoodsByCategory(categoryId);
        } else {
            Toast.makeText(this, "Invalid Category ID", Toast.LENGTH_SHORT).show();
        }

        btnBack.setOnClickListener(v -> {
            // Trở về MainActivity
            Intent intent = new Intent(FoodsByCategoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchFoodsByCategory(int categoryId) {
        CategoryAPI.categoryAPI.getFoodsByCategory(categoryId).enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    List<Food> foods = response.body();
                    if (foods != null && !foods.isEmpty()) {
                        foodAdapter = new FoodAdapter(foods, FoodsByCategoryActivity.this, new FoodAdapter.OnFoodClickListener() {
                            @Override
                            public void onFoodClick(Food food) {
                                Intent intent = new Intent(FoodsByCategoryActivity.this, FoodActivity.class);
                                intent.putExtra("foodName", food.getFoodName());
                                intent.putExtra("foodImage", food.getFoodImage());
                                intent.putExtra("foodPrice", food.getPrice());
                                intent.putExtra("foodDescription", food.getDescription());
                                intent.putExtra("foodId", food.getFoodId());
                                intent.putExtra("storeId", food.getStoreId());
                                startActivity(intent);
                            }
                        });
                        recyclerViewFoods.setAdapter(foodAdapter);
                    } else {
                        Toast.makeText(FoodsByCategoryActivity.this, "No foods found for this category.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("FoodsByCategory", "Response code: " + response.code());
                    Log.e("FoodsByCategory", "Response error body: " + response.errorBody());
                    Toast.makeText(FoodsByCategoryActivity.this, "Failed to fetch foods. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.e("FoodsByCategory", "Error: " + t.getMessage(), t);
                Toast.makeText(FoodsByCategoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

