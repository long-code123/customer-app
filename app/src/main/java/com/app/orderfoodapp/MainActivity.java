package com.app.orderfoodapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.orderfoodapp.Fragment.CartFragment;
import com.app.orderfoodapp.Fragment.HomeFragment;
import com.app.orderfoodapp.Fragment.ProfileFragment;
import com.app.orderfoodapp.Fragment.VoucherFragment;
import com.app.orderfoodapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.app.orderfoodapp.R;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment currentFragment = null;
    private static final String PREF_NAME = "CartPreferences";
    private static final String KEY_DATA_CLEARED = "dataCleared";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        clearSharedPreferencesIfFirstLaunch(); // Gọi hàm để xóa dữ liệu từ SharedPreferences nếu là lần đầu khởi động

        // Ban đầu thay thế bằng HomeFragment
        currentFragment = new HomeFragment();
        replaceFragment(currentFragment);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    if (!(currentFragment instanceof HomeFragment)) {
                        currentFragment = new HomeFragment();
                        replaceFragment(currentFragment);
                    }
                    break;
                case R.id.voucher:
                    if (!(currentFragment instanceof VoucherFragment)) {
                        currentFragment = new VoucherFragment();
                        replaceFragment(currentFragment);
                    }
                    break;
                case R.id.cart:
                    if (!(currentFragment instanceof CartFragment)) {
                        currentFragment = new CartFragment();
                        replaceFragment(currentFragment);
                    }
                    break;
                case R.id.profile:
                    if (!(currentFragment instanceof ProfileFragment)) {
                        currentFragment = new ProfileFragment();
                        replaceFragment(currentFragment);
                    }
                    break;
            }
            return true;
        });
    }

    private void clearSharedPreferencesIfFirstLaunch() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean dataCleared = sharedPreferences.getBoolean(KEY_DATA_CLEARED, false);

        if (!dataCleared) {
            // Xóa dữ liệu từ SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Xóa hết dữ liệu
            editor.putBoolean(KEY_DATA_CLEARED, true); // Đặt biến đã xóa dữ liệu
            editor.apply();
            Log.d("MainActivity", "SharedPreferences cleared successfully.");
        } else {
            Log.d("MainActivity", "SharedPreferences already cleared.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Phương thức để thay thế fragment
    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.addToBackStack(null); // Thêm Fragment vào back stack để hỗ trợ back navigation
            fragmentTransaction.commitAllowingStateLoss(); // Thực hiện FragmentTransaction an toàn
        }
    }
}

