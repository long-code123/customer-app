package com.app.orderfoodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.orderfoodapp.FoodsByCategoryActivity;
import com.app.orderfoodapp.Model.Category;
import com.app.orderfoodapp.Model.Food;
import com.app.orderfoodapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }
    List<Category> categories;
    Context context;
    private OnCategoryClickListener onCategoryClickListener;


    public CategoryAdapter(List<Category> categories, Context context, OnCategoryClickListener onCategoryClickListener) {
        this.categories = categories;
        this.context = context;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        CategoryAdapter.ViewHolder viewHolder = new CategoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.textCategoryName.setText(category.getCategoryName());
        Picasso
                .get()
                .load(category.getCategoryImage())
                .into(holder.categoryImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCategoryClickListener != null) {
                    onCategoryClickListener.onCategoryClick(category);
                }

                // Chuyển sang FoodsByCategoryActivity và truyền dữ liệu danh mục
                Intent intent = new Intent(context, FoodsByCategoryActivity.class);
                intent.putExtra("categoryName", category.getCategoryName());
                intent.putExtra("categoryId", category.getCategoryId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textCategoryName;
        private ImageView categoryImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoryName = itemView.findViewById(R.id.textCategoryName);
            categoryImage = itemView.findViewById(R.id.imageCategory);
        }
    }
    public void setData(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }
}
