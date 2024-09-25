package com.app.orderfoodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.orderfoodapp.Model.Voucher;
import com.app.orderfoodapp.R;
import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private List<Voucher> voucherList;
    private VoucherSelectionListener listener;

    public VoucherAdapter(List<Voucher> voucherList, VoucherSelectionListener listener) {
        this.voucherList = voucherList;
        this.listener = listener;
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

        // Thiết lập sự kiện click cho nút
        holder.btnAddVoucher.setOnClickListener(v -> {
            listener.onVoucherSelected(voucher); // Gọi phương thức chọn voucher
        });
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView tvVoucherConditition, tvVoucherDescription, tvVoucherValue;
        Button btnAddVoucher;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVoucherDescription = itemView.findViewById(R.id.tvVoucherDescription);
            tvVoucherConditition = itemView.findViewById(R.id.tvVoucherConditition);
            tvVoucherValue = itemView.findViewById(R.id.tvVoucherValue);
            btnAddVoucher = itemView.findViewById(R.id.btnAddVoucher);
        }
    }

    // Giao diện để xử lý sự kiện chọn voucher
    public interface VoucherSelectionListener {
        void onVoucherSelected(Voucher voucher);
    }
}
