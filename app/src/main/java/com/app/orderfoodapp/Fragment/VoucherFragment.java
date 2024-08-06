package com.app.orderfoodapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.orderfoodapp.R;
import com.app.orderfoodapp.API.VoucherAPI;
import com.app.orderfoodapp.Adapter.VoucherAdapter;
import com.app.orderfoodapp.Model.Voucher;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherFragment extends Fragment {

    private RecyclerView recyclerView;
    private VoucherAdapter voucherAdapter;
    private List<Voucher> voucherList;

    public VoucherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewVouchers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        voucherList = new ArrayList<>();
        voucherAdapter = new VoucherAdapter(voucherList);
        recyclerView.setAdapter(voucherAdapter);

        loadVouchers();

        return view;
    }

    private void loadVouchers() {
        VoucherAPI.voucherAPI.getAllVouchers().enqueue(new Callback<List<Voucher>>() {
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    voucherList.addAll(response.body());
                    voucherAdapter.notifyDataSetChanged();
                } else {
                    Log.e("VoucherFragment", "Response unsuccessful or body is null");
                    Toast.makeText(getContext(), "Failed to load vouchers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {
                Log.e("VoucherFragment", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Error loading vouchers", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
