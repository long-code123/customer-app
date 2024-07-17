package com.app.orderfoodapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.orderfoodapp.Model.CartItem;
import com.app.orderfoodapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;
    private OnQuantityChangeListener onQuantityChangeListener;
    private OnCartChangeListener onCartChangeListener;

    public CartAdapter(List<CartItem> cartItemList, Context context, OnQuantityChangeListener onQuantityChangeListener, OnCartChangeListener onCartChangeListener) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.onQuantityChangeListener = onQuantityChangeListener;
        this.onCartChangeListener = onCartChangeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.foodName.setText(cartItem.getFoodName());
        holder.foodPrice.setText(String.format("$%.2f", cartItem.getPrice()));
        holder.foodQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.totalItem.setText(String.format("$%.2f", cartItem.getPrice() * cartItem.getQuantity()));
        Picasso.get().load(cartItem.getFoodImage()).into(holder.foodImage);

        holder.btnIncrease.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            holder.foodQuantity.setText(String.valueOf(cartItem.getQuantity()));
            holder.totalItem.setText(String.format("$%.2f", cartItem.getPrice() * cartItem.getQuantity()));
            onQuantityChangeListener.onQuantityChanged();
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                holder.foodQuantity.setText(String.valueOf(cartItem.getQuantity()));
                holder.totalItem.setText(String.format("$%.2f", cartItem.getPrice() * cartItem.getQuantity()));
                onQuantityChangeListener.onQuantityChanged();
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            showDeleteConfirmation(position);
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice, foodQuantity, totalItem;
        ImageView foodImage, btnDelete;
        androidx.appcompat.widget.AppCompatButton btnIncrease, btnDecrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.tvFoodName);
            foodPrice = itemView.findViewById(R.id.tvFoodPrice);
            foodQuantity = itemView.findViewById(R.id.tvQuantity);
            totalItem = itemView.findViewById(R.id.tvTotalItem);
            foodImage = itemView.findViewById(R.id.ivFoodImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }

    private void showDeleteConfirmation(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Food");
        builder.setMessage("Are you sure you want to delete this food?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform delete operation here
                cartItemList.remove(position);
                notifyDataSetChanged();

                // Notify listener about cart change
                onCartChangeListener.onCartChanged(cartItemList);

                Toast.makeText(context, "Food deleted", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dismiss the dialog
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public interface OnCartChangeListener {
        void onCartChanged(List<CartItem> cartItemList);
    }
}
