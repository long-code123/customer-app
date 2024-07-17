package com.app.orderfoodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.orderfoodapp.Model.Voucher;
import com.app.orderfoodapp.R;
import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private List<Voucher> voucherList;

    public VoucherAdapter(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);
        holder.tvVoucherDescription.setText(voucher.getDescription());
        holder.tvVoucherConditition.setText(voucher.getDescription());
        holder.tvVoucherValue.setText("Expiry Date: " + voucher.getValue());
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView tvVoucherConditition, tvVoucherDescription, tvVoucherValue;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVoucherDescription = itemView.findViewById(R.id.tvVoucherDescription);
            tvVoucherConditition = itemView.findViewById(R.id.tvVoucherConditition);
            tvVoucherValue = itemView.findViewById(R.id.tvVoucherValue);
        }
    }
}
