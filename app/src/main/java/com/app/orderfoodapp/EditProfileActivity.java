package com.app.orderfoodapp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.orderfoodapp.API.EditUserAPI;
import com.app.orderfoodapp.API.LoginAPI;
import com.app.orderfoodapp.Model.RegisterRequest;
import com.app.orderfoodapp.Model.RegisterResponse;
import com.app.orderfoodapp.Model.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileActivity extends AppCompatActivity {

    private EditText edtName, edtImage, edtPhoneNumber, edtEmail, edtAddress, edtPassword;
    private TextView tvSelectedDob;
    private Button btnEditProfile, btnDob;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        edtName = findViewById(R.id.edtName);
        edtImage = findViewById(R.id.edtImage);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtPassword = findViewById(R.id.edtPassword);
        tvSelectedDob = findViewById(R.id.tvSelectedDob);
        btnEditProfile = findViewById(R.id.btnEditAccount);
        btnDob = findViewById(R.id.btnDob);

        // Load user profile data
        loadUserProfile();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        // Initialize calendar for date picker
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void showDatePickerDialog(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    tvSelectedDob.setText("Date of birth: " + selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = EditProfileActivity.this.getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", "");
        Call<User> call = LoginAPI.loginAPI.getCurrentUser("Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
//                    Picasso.get().load(user.getUserImage()).into((Target) edtImage);
                    edtImage.setText(user.getUserImage());
                    edtName.setText(user.getUserName());
                    edtEmail.setText(user.getEmail());
                    tvSelectedDob.setText("Date of birth: " + user.getDateOfBirth());
                    edtPhoneNumber.setText(user.getPhoneNumber());
                    edtAddress.setText(user.getAddress());
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to get user information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to edit user profile
    private void editProfile() {
        // Retrieve user input from EditText fields
        String name = edtName.getText().toString().trim();
        String image = edtImage.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String dateOfBirth = tvSelectedDob.getText().toString().replace("Date of birth: ", "");

        // Validate input fields
        if (name.isEmpty() || image.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty() || password.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");

        RegisterRequest registerRequest = new RegisterRequest(name, image, phoneNumber, email, address, password, dateOfBirth);

        EditUserAPI.editUserAPI.editUser(userId, registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorResponse = null;
                    try {
                        errorResponse = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.e("EditProfileActivity", "Update profile failed. Response: " + errorResponse);
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("EditProfileActivity", "Error when updating profile: " + t.getMessage());
                Toast.makeText(EditProfileActivity.this, "Failed to update profile. Please check your network connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
