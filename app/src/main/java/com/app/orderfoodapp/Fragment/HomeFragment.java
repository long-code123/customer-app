package com.app.orderfoodapp.Fragment;

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

import com.app.orderfoodapp.API.CategoryAPI;
import com.app.orderfoodapp.Adapter.CategoryAdapter;
import com.app.orderfoodapp.Model.Category;
import com.app.orderfoodapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;


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

        recyclerView = view.findViewById(R.id.viewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new CategoryAdapter(null, getContext());
        recyclerView.setAdapter(categoryAdapter);

        // Call API to get categories
        CategoryAPI.categoryAPI.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    if (categories != null && !categories.isEmpty()) {
                        categoryAdapter.setData(categories);
                        for (Category category : categories) {
                            Log.e("Category Info", "Category ID: " + category.getCategoryId() +
                                    ", Category Name: " + category.getCategoryName() +
                                    ", Category Image: " + category.getCategoryImage() +
                                    ", Created At: " + category.getCreated_at() +
                                    ", Updated At: " + category.getUpdated_at());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("HomeFragment", "Error fetching categories", t);
            }
        });
    }
}