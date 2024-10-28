package com.app.orderfoodapp.Activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.orderfoodapp.API.OrderAPI;
import com.app.orderfoodapp.Model.Order;
import com.app.orderfoodapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView btnBackDelivery;
    private TextView tvPending;
    private TextView tvOrderReceived;
    private TextView tvInTransit;
    private TextView tvDone;
    private int userId;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_progress);

        userId = getIntent().getIntExtra("USER_ID", userId);
        Log.e("userId", String.valueOf(userId));

        progressBar = findViewById(R.id.progressBar);
        tvPending = findViewById(R.id.textView20);
        tvOrderReceived = findViewById(R.id.textView21);
        tvInTransit = findViewById(R.id.textView25);
        tvDone = findViewById(R.id.textView23);
        btnBackDelivery = findViewById(R.id.btnBackDelivery);

        btnBackDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getLastOrder();
    }

    private void getLastOrder() {
        Call<Order> call = OrderAPI.orderAPI.getLastOrderByUser(String.valueOf(userId));

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Order lastOrder = response.body();
                    updateProgress(lastOrder.getStatus());
                } else {
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
            }
        });
    }

    private void updateProgress(String status) {
        // Reset tất cả các TextView về kích thước và kiểu chữ mặc định
        resetStatusTextViews();

        switch (status.toLowerCase()) {
            case "pending":
                progressBar.setProgress(0);
                highlightStatus(tvPending, 24);
                break;
            case "order received":
                progressBar.setProgress(33);
                highlightStatus(tvOrderReceived, 24);
                break;
            case "in transit":
                progressBar.setProgress(66);
                highlightStatus(tvInTransit, 24);
                break;
            case "done":
                progressBar.setProgress(100);
                highlightStatus(tvDone, 24);
                break;
            default:
                progressBar.setProgress(0);
                break;
        }
    }

    private void resetStatusTextViews() {
        TextView[] statusTextViews = {
                tvPending,
                tvOrderReceived,
                tvInTransit,
                tvDone
        };

        for (TextView textView : statusTextViews) {
            textView.setTextSize(16); // Kích thước chữ mặc định
            textView.setTypeface(null, Typeface.NORMAL); // Đặt kiểu chữ về bình thường
        }
    }

    private void highlightStatus(TextView textView, int textSize) {
        // Thiết lập kích thước chữ và kiểu chữ cho trạng thái hiện tại
        textView.setTextSize(textSize);
        textView.setTypeface(null, Typeface.BOLD); // Đặt kiểu chữ thành in đậm
        textView.setTextColor(Color.RED); // Đặt màu chữ thành màu đỏ
    }
}
