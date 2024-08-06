package com.app.orderfoodapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DeliveryProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvProgress;
    private ImageView imgDelivery;
    private int progressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_progress);

        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tvProgress);
        imgDelivery = findViewById(R.id.imgDelivery);

        // Giả lập tiến trình giao hàng trong một luồng khác
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Cập nhật UI từ luồng giao diện chính
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            tvProgress.setText(progressStatus + "%");
                            if (progressStatus == 100) {
                                imgDelivery.setVisibility(ImageView.VISIBLE);
                                tvProgress.setText("Delivered");
                            }
                        }
                    });
                    try {
                        // Giả lập thời gian chờ của tiến trình
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
