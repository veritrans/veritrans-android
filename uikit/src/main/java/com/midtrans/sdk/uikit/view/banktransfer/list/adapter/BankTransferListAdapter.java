package com.midtrans.sdk.uikit.view.banktransfer.list.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.enablepayment.EnabledPayment;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.adapter.BaseAdapter;
import com.midtrans.sdk.uikit.base.model.BankTransfer;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BankTransferListAdapter extends BaseAdapter<BankTransfer> {

    private List<BankTransfer> paymentMethods;
    private BankTransferListener paymentMethodListener;

    public interface BankTransferListener {
        void onItemClick(int position);
    }

    public BankTransferListAdapter(BankTransferListener listener) {
        this.paymentMethodListener = listener;
        this.paymentMethods = new ArrayList<>();
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_item_method, parent, false);
        return new PaymentViewHolder(view, paymentMethodListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PaymentViewHolder viewHolder = (PaymentViewHolder) holder;
        viewHolder.mImageView.setImageResource(paymentMethods.get(position).getImage());
        viewHolder.name.setText(paymentMethods.get(position).getBankName());
        viewHolder.description.setText(paymentMethods.get(position).getDescription());

        disablePaymentView(viewHolder, paymentMethods.get(position));
    }

    @Override
    public void addAllData(List<BankTransfer> data) {
        this.paymentMethods.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void addData(BankTransfer data) {
        this.paymentMethods.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public BankTransfer getDataAt(int position) {
        return paymentMethods.get(position);
    }

    @Override
    public List<BankTransfer> getAllData() {
        return paymentMethods;
    }

    @Override
    public void setData(List<BankTransfer> data) {
        this.paymentMethods.clear();
        this.paymentMethods.addAll(data);
        this.notifyDataSetChanged();
    }

    private void disablePaymentView(PaymentViewHolder holder, BankTransfer paymentMethod) {
        if (paymentMethod.getStatus().equals(EnabledPayment.STATUS_DOWN)) {
            holder.layoutPaymentUnavailable.setVisibility(View.VISIBLE);
            holder.itemView.setClickable(false);
            holder.textUnavailable.setVisibility(View.VISIBLE);
        } else {
            holder.layoutPaymentUnavailable.setVisibility(View.GONE);
            holder.itemView.setClickable(true);
            holder.textUnavailable.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return paymentMethods.size();
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        ImageView mImageView;
        DefaultTextView textUnavailable;
        LinearLayout layoutPaymentUnavailable;
        FancyButton badgePromo;

        PaymentViewHolder(View itemView, final BankTransferListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.text_payment_method_name);
            mImageView = itemView.findViewById(R.id.img_payment_method_icon);
            description = itemView.findViewById(R.id.text_payment_method_description);
            textUnavailable = itemView.findViewById(R.id.text_option_unavailable);
            layoutPaymentUnavailable = itemView.findViewById(R.id.layout_payment_unavailable);
            badgePromo = itemView.findViewById(R.id.badge_promo);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}