package com.app.orderfoodapp.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.orderfoodapp.API.LoginAPI;
import com.app.orderfoodapp.Adapter.CartAdapter;
import com.app.orderfoodapp.Config.Constants;
import com.app.orderfoodapp.Manager.CartManager;
import com.app.orderfoodapp.Model.CartItem;
import com.app.orderfoodapp.Model.OrderItem;
import com.app.orderfoodapp.Model.OrderRequest;
import com.app.orderfoodapp.Model.User;
import com.app.orderfoodapp.OSMActivity;
import com.app.orderfoodapp.R;
import com.app.orderfoodapp.Zalopay.Api.CreateOrder;
import com.google.gson.Gson;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CartFragment extends Fragment implements CartAdapter.OnQuantityChangeListener {
    private static final int ZALO_PAY_REQUEST_CODE = 1001;
    private static final int REQUEST_CODE_OSM_ACTIVITY = 1;

    private RecyclerView recyclerViewCart;
    private TextView tvSubtotal, tvDelivery, tvTax, tvTotal, tvAddress, tvPaymentMethod;
    private Button btnOrderNow;
    private ImageView btnAddress, btnPaymentMethod;
    private LoginAPI apiService;
    private String paymentMethod = "Cash"; // Default payment method
    private double distanceKm = 0.0; // Distance in kilometers for delivery calculation
    private int userIdCart; // Get Id user.

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
        tvAddress = view.findViewById(R.id.tvAddress);
        btnOrderNow = view.findViewById(R.id.btnOrderNow);
        btnAddress = view.findViewById(R.id.btnAddress);
        btnPaymentMethod = view.findViewById(R.id.btnPaymentMethod);
        tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod);
        apiService = LoginAPI.loginAPI;

        //Get UserId


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        updateCart();

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentMethod.equals("ZaloPay")) {
                    initiateZaloPayPayment();
//                    createOrder();
                } else {
                    createOrder();
                }
            }
        });

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get address from TextView
                String address = tvAddress.getText().toString();

                Intent intent = new Intent(getActivity(), OSMActivity.class);
                intent.putExtra("ADDRESS", address);
                startActivityForResult(intent, REQUEST_CODE_OSM_ACTIVITY);
            }
        });

        btnPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentMethodDialog();
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", "");
        retrofit2.Call<User> call = apiService.getCurrentUser("Bearer " + token);
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, retrofit2.Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    tvAddress.setText(user.getAddress());
                    Log.e("userId", String.valueOf(user.getUserId()));
                    userIdCart = user.getUserId();
                } else {
                    Toast.makeText(getActivity(), "Failed to get user information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OSM_ACTIVITY && resultCode == getActivity().RESULT_OK) {
            if (data != null && data.hasExtra("searchedAddress")) {
                String searchedAddress = data.getStringExtra("searchedAddress");
                tvAddress.setText(searchedAddress);
            }
            if (data != null && data.hasExtra("distance")) {
                distanceKm = data.getDoubleExtra("distance", 0);
                String deliveryTime = calculateDeliveryTime(distanceKm);
                tvDelivery.setText(deliveryTime);
                updateTotals(CartManager.getInstance().getCartItems());
            }
        }

        // Xử lý kết quả từ ZaloPay
        if (requestCode == ZALO_PAY_REQUEST_CODE) {
            ZaloPaySDK.getInstance().onResult(data);
        }
    }


    private String calculateDeliveryTime(double distanceKm) {
        // Calculate delivery time based on distance
        int deliveryTimeMinutes = (int) (distanceKm * 3); // 1 km = 3 minutes
        int hours = deliveryTimeMinutes / 60;
        int minutes = deliveryTimeMinutes % 60;

        if (hours > 0) {
            return hours + " hours " + minutes + " min";
        } else {
            return minutes + " min";
        }
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

        double tax = subtotal * 0.1;
        double deliveryFeeUSD = distanceKm * 3000 / 24000; // Shipping cost (1 km = 3000 VND, exchange rate 24000 VND = 1 USD)
        double total = subtotal + tax + deliveryFeeUSD;

        tvSubtotal.setText(String.format("$%.2f", subtotal));
        tvTax.setText(String.format("$%.2f", tax));
        tvDelivery.setText(String.format("$%.2f", deliveryFeeUSD));
        tvTotal.setText(String.format("$%.2f", total));
    }

    @Override
    public void onQuantityChanged() {
        updateCart();
    }

    private void createOrder() {
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            orderItems.add(new OrderItem(cartItem.getId(), cartItem.getQuantity()));
        }
        OrderRequest orderRequest = new OrderRequest(calculateDeliveryTime(distanceKm), userIdCart, 1, orderItems);

        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(orderRequest);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/api/v1/orders/orders")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Failed to create order", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Order created successfully", Toast.LENGTH_SHORT).show();
                            // Clear the cart after order is created
                            CartManager.getInstance().clearCart();
                            updateCart();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Failed to create order", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void showPaymentMethodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Payment Method")
                .setItems(new String[]{"Cash", "ZaloPay"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        if (which == 0) {
                            paymentMethod = "Cash";
                            tvPaymentMethod.setText("Cash");
                        } else {
                            paymentMethod = "ZaloPay";
                            tvPaymentMethod.setText("ZaloPay");
                        }
                        updateTotals(CartManager.getInstance().getCartItems()); // Update totals including delivery fee
                    }
                });
        builder.create().show();
    }

    private void initiateZaloPayPayment() {
        // Get total value from tvTotal TextView and remove currency symbol
        String totalStr = tvTotal.getText().toString().replace("$", "");
        try {
            // Convert total to double
            double total = Double.parseDouble(totalStr);

            double totalInVND = total * 24000;

            CreateOrder orderApi = new CreateOrder();
            JSONObject data = orderApi.createOrder(String.valueOf((long) totalInVND));
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(getActivity(), token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Log.d("Long", "Payment Succeeded");
                        createOrder();
                        Toast.makeText(getActivity(), "Payment succeeded", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Log.d("Long", "Payment canceled");
                        Toast.makeText(getActivity(), "Payment canceled", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Log.d("Long", "Payment Error");
                        Toast.makeText(getActivity(), "Payment error: ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

