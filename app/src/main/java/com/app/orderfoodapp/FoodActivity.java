package com.app.orderfoodapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.orderfoodapp.API.FoodAPI;
import com.app.orderfoodapp.Adapter.FoodAdapter;
import com.app.orderfoodapp.Model.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        recyclerView = findViewById(R.id.recyclerViewFoods);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter(null,this);
        recyclerView.setAdapter(foodAdapter);

        callAPI();

    }

    private void callAPI() {
        FoodAPI.foodAPI.getAllFood().enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FoodActivity.this, "Call API success", Toast.LENGTH_SHORT).show();
                    List<Food> foodList = response.body();
                    if (foodList != null && !foodList.isEmpty()) {
                        foodAdapter.setData(foodList);
                    } else {
                        recyclerView.setVisibility(View.GONE); // Ẩn RecyclerView nếu danh sách trống
                        Toast.makeText(FoodActivity.this, "Empty food list", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FoodActivity.this, "Call API failed", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "HTTP error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.e("API Error", "Call API failed: " + t.getMessage()); // Log lỗi vào Logcat
                Toast.makeText(FoodActivity.this, "Call API failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
