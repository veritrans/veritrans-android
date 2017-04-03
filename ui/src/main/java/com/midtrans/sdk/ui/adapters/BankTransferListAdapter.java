package com.midtrans.sdk.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ziahaqi
 */
public class BankTransferListAdapter extends RecyclerView.Adapter<BankTransferListAdapter.BankTransferViewHolder> {
    private static final String TAG = BankTransferListAdapter.class.getSimpleName();

    private List<PaymentMethodModel> bankList = new ArrayList<>();
    private BankTransferListener listener;

    public BankTransferListAdapter(BankTransferListener listener) {
        this.listener = listener;
    }

    public void setData(List<PaymentMethodModel> banktransfers) {
        if (banktransfers != null) {
            this.bankList.clear();
            this.bankList.addAll(banktransfers);
            this.notifyDataSetChanged();
        }
    }

    public PaymentMethodModel getItem(int position) {
        return bankList.get(position);
    }

    @Override
    public BankTransferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_banktransfer_row, parent, false);
        return new BankTransferViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(BankTransferViewHolder holder, int position) {
        holder.tvBankName.setText(bankList.get(position).getName());
        holder.ivBankIcon.setImageResource(bankList.get(position).getImageId());
        holder.tvBankDescription.setText(bankList.get(position).getDescription());
        Logger.d(TAG, "Bank Item: " + bankList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    public interface BankTransferListener {
        void onItemClick(int position);
    }

    class BankTransferViewHolder extends RecyclerView.ViewHolder {
        TextView tvBankName;
        TextView tvBankDescription;
        ImageView ivBankIcon;

        BankTransferViewHolder(View itemView, final BankTransferListener listener) {
            super(itemView);
            tvBankName = (TextView) itemView.findViewById(R.id.text_bank_name);
            ivBankIcon = (ImageView) itemView.findViewById(R.id.iv_bank_icon);
            tvBankDescription = (TextView) itemView.findViewById(R.id.text_bank_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

    }
}
