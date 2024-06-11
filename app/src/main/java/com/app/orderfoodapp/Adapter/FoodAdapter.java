package com.app.orderfoodapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.orderfoodapp.Model.Food;
import com.app.orderfoodapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    List<Food> foods;
    Context context;

    public FoodAdapter(List<Food> foods, Context context) {
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_food, parent, false);
        FoodAdapter.ViewHolder viewHolder = new FoodAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.textFoodName.setText(food.getFoodName());
        holder.textFoodPrice.setText(String.valueOf(food.getPrice()));
        holder.textFoodDescription.setText(food.getDescription());
        holder.textFoodCategory.setText(String.valueOf(food.getCategoryId()));
        holder.textFoodStore.setText(String.valueOf(food.getStoreId()));
        Picasso.get().load(food.getFoodImage()).into(holder.foodImage);
        Log.d("FoodAdapter", "Image URL: " + food.getFoodImage());

    }

    @Override
    public int getItemCount() {
        return foods == null ? 0 : foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textFoodName, textFoodPrice, textFoodDescription, textFoodCategory, textFoodStore;
        private ImageView foodImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.textFoodName);
            textFoodPrice = itemView.findViewById(R.id.textFoodPrice);
            textFoodDescription = itemView.findViewById(R.id.textFoodDescription);
            textFoodCategory = itemView.findViewById(R.id.textFoodCategory);
            textFoodStore = itemView.findViewById(R.id.textFoodStore);
            foodImage = itemView.findViewById(R.id.imageFood);
        }
    }
    public void setData(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }
}
