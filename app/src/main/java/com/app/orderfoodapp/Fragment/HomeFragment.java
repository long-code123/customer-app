package com.app.orderfoodapp.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.orderfoodapp.API.CategoryAPI;
import com.app.orderfoodapp.API.LoginAPI;
import com.app.orderfoodapp.API.StoreAPI;
import com.app.orderfoodapp.Adapter.CategoryAdapter;
import com.app.orderfoodapp.Adapter.StoreAdapter;
import com.app.orderfoodapp.FoodsByCategoryActivity;
import com.app.orderfoodapp.FoodsByStoreActivity;
import com.app.orderfoodapp.Model.Category;
import com.app.orderfoodapp.Model.Store;
import com.app.orderfoodapp.Model.User;
import com.app.orderfoodapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewStores;
    private CategoryAdapter categoryAdapter;
    private StoreAdapter storeAdapter; // Adapter cho cửa hàng
    private TextView dateTextView, textViewUserName; // Thêm TextView cho ngày
    private LoginAPI apiService;
    private ImageView imageProf;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewCategories = view.findViewById(R.id.viewCategories);
        recyclerViewStores = view.findViewById(R.id.viewStores);
        dateTextView = view.findViewById(R.id.textViewDate);
        textViewUserName = view.findViewById(R.id.textViewGetMe);
        imageProf = view.findViewById(R.id.imagePro);
        apiService = LoginAPI.loginAPI;

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        dateTextView.setText(currentDate);

        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(categoryLayoutManager);

        CategoryAPI.categoryAPI.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    if (categories != null && !categories.isEmpty()) {
                        categoryAdapter = new CategoryAdapter(categories, getActivity(), new CategoryAdapter.OnCategoryClickListener() {
                            @Override
                            public void onCategoryClick(Category category) {
                                Intent intent = new Intent(getActivity(), FoodsByCategoryActivity.class);
                                intent.putExtra("categoryName", category.getCategoryName());
                                intent.putExtra("categoryId", category.getCategoryId());
                                startActivity(intent);
                            }
                        });
                        recyclerViewCategories.setAdapter(categoryAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("HomeFragment", "Error fetching categories", t);
            }
        });

        LinearLayoutManager storeLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewStores.setLayoutManager(storeLayoutManager);

        StoreAPI.storeAPI.getAllStores().enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.isSuccessful()) {
                    List<Store> stores = response.body();
                    if (stores != null && !stores.isEmpty()) {
                        storeAdapter = new StoreAdapter(stores, getActivity(), new StoreAdapter.OnStoreClickListener() {
                            @Override
                            public void onStoreClick(Store store) {
                                Intent intent = new Intent(getActivity(), FoodsByStoreActivity.class);
                                intent.putExtra("storeName", store.getStoreName());
                                intent.putExtra("storeId", store.getStoreId());
                                startActivity(intent);
                            }
                        });
                        recyclerViewStores.setAdapter(storeAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.e("HomeFragment", "Error fetching stores", t);
            }
        });
        getCurrentUser();
    }

    private void getCurrentUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", "");
        Call<User> call = apiService.getCurrentUser("Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    textViewUserName.setText("Hihaho " + user.getUserName());
                    Picasso.get().load(user.getUserImage()).into(imageProf);
                } else {
                    Toast.makeText(getActivity(), "Failed to get user information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}