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

import com.app.orderfoodapp.Activity.FoodActivity;
import com.app.orderfoodapp.Model.Food;
import com.app.orderfoodapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    public interface OnFoodClickListener {
        void onFoodClick(Food food);
    }

    private List<Food> foods;
    private Context context;
    private OnFoodClickListener onFoodClickListener;

    public FoodAdapter(List<Food> foods, Context context, OnFoodClickListener onFoodClickListener) {
        this.foods = foods;
        this.context = context;
        this.onFoodClickListener = onFoodClickListener;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.textFoodName.setText(food.getFoodName());
        String priceWithCurrency = String.format("$%s", food.getPrice());
        holder.textFoodPrice.setText(priceWithCurrency);
        Picasso.get().load(food.getFoodImage()).into(holder.foodImage);
        Log.d("FoodAdapter", "Image URL: " + food.getFoodImage());

        holder.itemView.setOnClickListener(v -> {
            if (onFoodClickListener != null) {
                onFoodClickListener.onFoodClick(food);
            }
            Intent intent = new Intent(context, FoodActivity.class);
            intent.putExtra("foodName", food.getFoodName());
            intent.putExtra("foodImage", food.getFoodImage());
            intent.putExtra("foodDescription", food.getDescription());
            intent.putExtra("foodPrice", food.getPrice());
            intent.putExtra("foodId", food.getFoodId());
            intent.putExtra("storeId", food.getStoreId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foods == null ? 0 : foods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textFoodName;
        private final TextView textFoodPrice;
        private final ImageView foodImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.textFoodName);
            textFoodPrice = itemView.findViewById(R.id.textFoodPrice);
            foodImage = itemView.findViewById(R.id.imageFood);
        }
    }

    public void setData(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }
}
