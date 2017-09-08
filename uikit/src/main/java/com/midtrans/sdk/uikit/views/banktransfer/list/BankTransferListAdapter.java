package com.midtrans.sdk.uikit.views.banktransfer.list;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.BankTransfer;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 8/11/17.
 */

public class BankTransferListAdapter extends RecyclerView.Adapter<BankTransferListAdapter.BankTransferViewHolder> {

    private static final String TAG = BankTransferListAdapter.class.getSimpleName();

    private List<BankTransfer> mData = new ArrayList<>();
    private BankTransferAdapterListener listener;

    public BankTransferListAdapter(BankTransferAdapterListener listener) {
        this.listener = listener;
    }

    public void setData(List<BankTransfer> banktransfers) {
        this.mData.clear();
        if (banktransfers != null) {
            this.mData.addAll(banktransfers);
        }
        this.notifyDataSetChanged();
    }

    public BankTransfer getItem(int position) {
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
        BankTransfer bankTransfer = mData.get(position);
        if (bankTransfer != null) {
            holder.bankName.setText(mData.get(position).getBankName());
            holder.bankIcon.setImageResource(mData.get(position).getImage());
            holder.bankDescription.setText(mData.get(position).getDescription());
            Logger.d(TAG, "Bank Item: " + mData.get(position).getBankName());

            disablePaymentView(holder, bankTransfer);
        }
    }

    private void disablePaymentView(BankTransferViewHolder holder, BankTransfer paymentMethod) {
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

    public interface BankTransferAdapterListener {
        void onItemClick(int position);
    }

    class BankTransferViewHolder extends RecyclerView.ViewHolder {
        TextView bankName;
        TextView bankDescription;
        ImageView bankIcon;
        DefaultTextView textUnavailable;
        LinearLayout layoutPaymentUnavailable;

        BankTransferViewHolder(View itemView, final BankTransferAdapterListener listener) {
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
