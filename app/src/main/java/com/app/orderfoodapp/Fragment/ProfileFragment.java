package com.app.orderfoodapp.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.orderfoodapp.API.LoginAPI;
import com.app.orderfoodapp.Activity.LoginActivity;
import com.app.orderfoodapp.Model.User;
import com.app.orderfoodapp.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    ImageView imageProfile;
    TextView textViewNameProf, textViewGmailProf, textViewDob, textViewPhone, textViewAddress;
    Button btnLogout;
    private LoginAPI apiService;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageProfile = view.findViewById(R.id.imageProfile);
        textViewNameProf = view.findViewById(R.id.textViewNameProf);
        textViewGmailProf = view.findViewById(R.id.textViewGmailProf);
        textViewDob = view.findViewById(R.id.textViewDob);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        btnLogout = view.findViewById(R.id.btnLogout);
        apiService = LoginAPI.loginAPI;

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý đăng xuất
                logoutUser();
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", "");
        Call<User> call = apiService.getCurrentUser("Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Log.e("userId", String.valueOf(user.getUserId()));
                    Picasso.get().load(user.getUserImage()).into(imageProfile);
                    textViewNameProf.setText(user.getUserName());
                    textViewGmailProf.setText(user.getEmail());
                    textViewDob.setText(user.getDateOfBirth());
                    textViewPhone.setText(user.getPhoneNumber());
                    textViewAddress.setText(user.getAddress());
                } else {
                    Toast.makeText(getActivity(), "Failed to get user information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        // Xóa token đã lưu
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwt_token");
        editor.apply();

        // Chuyển người dùng về màn hình đăng nhập
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
