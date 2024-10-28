package com.app.orderfoodapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
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

public class VoucherFragment extends Fragment implements VoucherAdapter.VoucherSelectionListener {

    private RecyclerView recyclerView;
    private VoucherAdapter voucherAdapter;
    private List<Voucher> voucherList;

    public VoucherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewVouchers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        voucherList = new ArrayList<>();
        voucherAdapter = new VoucherAdapter(voucherList, this);
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
                    Toast.makeText(getContext(), "Fetch voucher failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {
                Log.e("VoucherFragment", "Lỗi: " + t.getMessage());
                Toast.makeText(getContext(), "Error voucher", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveVoucherToPreferences(Voucher voucher) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("voucherId", voucher.getVoucherId());
        editor.putString("voucherDescription", voucher.getDescription());
        editor.putFloat("voucherValue", (float) voucher.getValue());
        editor.apply();
    }

    @Override
    public void onVoucherSelected(Voucher voucher) {
        saveVoucherToPreferences(voucher);
        Toast.makeText(getContext(), "Voucher is chose: " + voucher.getDescription(), Toast.LENGTH_SHORT).show();

        // Gọi API xóa voucher
        deleteVoucherFromAPI(voucher.getVoucherId());
    }

    private void deleteVoucherFromAPI(int voucherId) {
        VoucherAPI.voucherAPI.deleteVoucher(voucherId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Delete voucher succeed", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < voucherList.size(); i++) {
                        if (voucherList.get(i).getVoucherId() == voucherId) {
                            voucherList.remove(i);
                            voucherAdapter.notifyItemRemoved(i);
                            break;
                        }
                    }                } else {
                    Toast.makeText(getContext(), "Delete voucher fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
