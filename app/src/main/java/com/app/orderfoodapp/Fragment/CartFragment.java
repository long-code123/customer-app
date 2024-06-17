package com.app.orderfoodapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.orderfoodapp.Adapter.CartAdapter;
import com.app.orderfoodapp.Manager.CartManager;
import com.app.orderfoodapp.Model.CartItem;
import com.app.orderfoodapp.R;

import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnQuantityChangeListener {

    private RecyclerView recyclerViewCart;
    private TextView tvSubtotal, tvDelivery, tvTax, tvTotal;

    public CartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewCart = view.findViewById(R.id.viewCart);
        tvSubtotal = view.findViewById(R.id.tvSubtotal);
        tvDelivery = view.findViewById(R.id.tvDelivery);
        tvTax = view.findViewById(R.id.tvTax);
        tvTotal = view.findViewById(R.id.tvTotal);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        updateCart();
    }

    private void updateCart() {
        List<CartItem> cartItemList = CartManager.getInstance().getCartItems();
        CartAdapter adapter = new CartAdapter(cartItemList, getContext(), this, new CartAdapter.OnCartChangeListener() {
            @Override
            public void onCartChanged(List<CartItem> cartItems) {
                updateTotals(cartItems);
            }
        });
        recyclerViewCart.setAdapter(adapter);
        updateTotals(cartItemList);
    }

    private void updateTotals(List<CartItem> cartItemList) {
        double subtotal = 0.0;
        for (CartItem item : cartItemList) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        double delivery = 5.0; // Example delivery fee
        double tax = subtotal * 0.10;
        double total = subtotal + delivery + tax;

        tvSubtotal.setText(String.format("$%.2f", subtotal));
        tvDelivery.setText(String.format("$%.2f", delivery));
        tvTax.setText(String.format("$%.2f", tax));
        tvTotal.setText(String.format("$%.2f", total));
    }

    @Override
    public void onQuantityChanged() {
        updateCart();
    }
}
