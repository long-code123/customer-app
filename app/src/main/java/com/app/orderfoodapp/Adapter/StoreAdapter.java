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
import com.app.orderfoodapp.FoodsByStoreActivity;
import com.app.orderfoodapp.Model.Category;
import com.app.orderfoodapp.Model.Store;
import com.app.orderfoodapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    public interface OnStoreClickListener {
        void onStoreClick(Store store);
    }
    List<Store> stores;
    Context context;
    private StoreAdapter.OnStoreClickListener onStoreClickListener;


    public StoreAdapter(List<Store> stores, Context context, OnStoreClickListener onStoreClickListener) {
        this.stores = stores;
        this.context = context;
        this.onStoreClickListener = onStoreClickListener;
    }

    @NonNull
    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_store, parent, false);
        StoreAdapter.ViewHolder viewHolder = new StoreAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.textStoreName.setText(store.getStoreName());
        Picasso.get().load(store.getStoreImage()).into(holder.storeImage);
        Log.d("StoreAdapter", "Image URL: " + store.getStoreImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStoreClickListener != null) {
                    onStoreClickListener.onStoreClick(store);
                }

                // Chuyển sang FoodsByCategoryActivity và truyền dữ liệu danh mục
                Intent intent = new Intent(context, FoodsByStoreActivity.class);
                intent.putExtra("storeName", store.getStoreName());
                intent.putExtra("storeId", store.getStoreId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stores == null ? 0 : stores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textStoreName;
        private ImageView storeImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textStoreName = itemView.findViewById(R.id.textStoreName);
            storeImage = itemView.findViewById(R.id.imageStore);
        }
    }
    public void setData(List<Category> categories) {
        this.stores = stores;
        notifyDataSetChanged();
    }
}
