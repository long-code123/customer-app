package com.app.orderfoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.orderfoodapp.Fragment.CartFragment;
import com.app.orderfoodapp.Manager.CartManager;
import com.app.orderfoodapp.Model.CartItem;
import com.app.orderfoodapp.Model.Store;
import com.app.orderfoodapp.Model.StoreRepository;
import com.squareup.picasso.Picasso;

public class FoodActivity extends AppCompatActivity {
    private TextView tvFoodName, tvFoodDescription, tvFoodPrice, tvstoreName, tvQuantityDetail;
    private ImageView ivFoodImage, btnbackDetail, ivStoreFood;
    private Button btnIncrease, btnDecrease, btnAdd;
    private Store store; // Khai báo biến store ở cấp lớp
    private int quantity = 1; // Biến để lưu trữ số lượng hiện tại

    // Biến để quản lý giỏ hàng
    private RelativeLayout cartBar;
    private TextView tvCartItems, tvCartTotal;
    private Button btnViewCart;
    private int totalItems = 0;
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        tvFoodName = findViewById(R.id.foodName);
        tvFoodDescription = findViewById(R.id.textViewDescription);
        tvFoodPrice = findViewById(R.id.foodPrice);
        ivFoodImage = findViewById(R.id.imageDetailFood);
        btnbackDetail = findViewById(R.id.btnBackDetail);
        tvstoreName = findViewById(R.id.storeName);
        ivStoreFood = findViewById(R.id.imageStoreFood);
        btnIncrease = findViewById(R.id.btnPlus);
        btnDecrease = findViewById(R.id.btnSubstract);
        tvQuantityDetail = findViewById(R.id.tvQuantityDetail);
        btnAdd = findViewById(R.id.btnAdd);

        // Khởi tạo thanh bar giỏ hàng
        cartBar = findViewById(R.id.cartBar);
        tvCartItems = findViewById(R.id.tvCartItems);
        tvCartTotal = findViewById(R.id.tvCartTotal);
        btnViewCart = findViewById(R.id.btnViewCart);

        // Nhận dữ liệu từ Intent
        int foodId = getIntent().getIntExtra("foodId", -1);
        String foodName = getIntent().getStringExtra("foodName");
        String foodImage = getIntent().getStringExtra("foodImage");
        double foodPrice = getIntent().getDoubleExtra("foodPrice", 0.0);
        String foodDescription = getIntent().getStringExtra("foodDescription");
        int storeId = getIntent().getIntExtra("storeId", -1);

        // Gán giá trị cho biến store ở cấp lớp
        if (storeId != -1) {
            store = StoreRepository.getInstance().getStoreById(storeId);
            if (store != null) {
                tvstoreName.setText(store.getStoreName());
                Picasso.get().load(store.getStoreImage()).into(ivStoreFood);
                Log.e("FoodActivity", "Store Url: " + store.getStoreImage());
            } else {
                Toast.makeText(this, "Store not found", Toast.LENGTH_SHORT).show();
            }
        }

        // Hiển thị dữ liệu món ăn
        tvFoodName.setText(foodName);
        tvFoodPrice.setText(String.format("$%s", foodPrice));
        tvFoodDescription.setText(foodDescription);
        Picasso.get().load(foodImage).into(ivFoodImage);
        tvQuantityDetail.setText(String.valueOf(quantity));

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantityDetail.setText(String.valueOf(quantity));
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantityDetail.setText(String.valueOf(quantity));
            }
        });

        btnAdd.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(foodId, foodName, foodPrice, quantity, foodImage);
            CartManager.getInstance().addToCart(cartItem);
            totalItems += quantity;
            totalPrice += quantity * foodPrice;
            updateCartBar();
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        });

        btnbackDetail.setOnClickListener(v -> {
            if (store != null) { // Kiểm tra nếu store không null
                Intent intent = new Intent(FoodActivity.this, FoodsByStoreActivity.class);
                intent.putExtra("storeId", store.getStoreId());
                intent.putExtra("storeName", store.getStoreName());
                intent.putExtra("storeImage", store.getStoreImage());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Store not found", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewCart.setOnClickListener(v -> {
            // Xử lý sự kiện khi nhấn vào nút View Cart
            Intent intent = new Intent(FoodActivity.this, CartFragment.class);
            startActivity(intent);
        });
    }


    private void updateCartBar() {
        if (totalItems > 0) {
            cartBar.setVisibility(View.VISIBLE);
            tvCartItems.setText("Items: " + totalItems);
            tvCartTotal.setText("Total: $" + String.format("%.2f", totalPrice));
        } else {
            cartBar.setVisibility(View.GONE);
        }
    }
}
