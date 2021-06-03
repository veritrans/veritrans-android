package com.midtrans.sdk.uikit.views.uob;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.BankTransfer;
import com.midtrans.sdk.uikit.models.UobPayment;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 8/11/17.
 */

public class UobListAdapter extends RecyclerView.Adapter<UobListAdapter.BankTransferViewHolder> {

    private static final String TAG = UobListAdapter.class.getSimpleName();

    private List<UobPayment> mData = new ArrayList<>();
    private UobAdapterListener listener;

    public UobListAdapter(UobAdapterListener listener) {
        this.listener = listener;
    }

    public void setData(List<UobPayment> payments) {
        this.mData.clear();
        if (payments != null) {
            this.mData.addAll(payments);
        }
        this.notifyDataSetChanged();
    }

    public UobPayment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public BankTransferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_payment_methods, parent, false);
        return new BankTransferViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(BankTransferViewHolder holder, int position) {
        UobPayment payment = mData.get(position);
        if (payment != null) {
            holder.bankName.setText(mData.get(position).getPaymentName());
            holder.bankIcon.setImageResource(mData.get(position).getImage());
            holder.bankDescription.setText(mData.get(position).getDescription());
            Logger.d(TAG, "Bank Item: " + mData.get(position).getPaymentName());

            disablePaymentView(holder, payment);
        }
    }

    private void disablePaymentView(BankTransferViewHolder holder, UobPayment paymentMethod) {
        if (!TextUtils.isEmpty(paymentMethod.getStatus()) && paymentMethod.getStatus().equals(EnabledPayment.STATUS_DOWN)) {
            holder.layoutPaymentUnavailable.setVisibility(View.VISIBLE);
            holder.itemView.setClickable(false);
            holder.textUnavailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface UobAdapterListener {
        void onItemClick(int position);
    }

    class BankTransferViewHolder extends RecyclerView.ViewHolder {
        TextView bankName;
        TextView bankDescription;
        ImageView bankIcon;
        DefaultTextView textUnavailable;
        LinearLayout layoutPaymentUnavailable;

        BankTransferViewHolder(View itemView, final UobAdapterListener listener) {
            super(itemView);
            bankName = (TextView) itemView.findViewById(R.id.text_payment_method_name);
            bankIcon = (ImageView) itemView.findViewById(R.id.img_payment_method_icon);
            bankDescription = (TextView) itemView.findViewById(R.id.text_payment_method_description);
            textUnavailable = (DefaultTextView) itemView.findViewById(R.id.text_option_unavailable);
            layoutPaymentUnavailable = (LinearLayout) itemView.findViewById(R.id.layout_payment_unavailable);

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
