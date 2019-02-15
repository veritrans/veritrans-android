package com.midtrans.sdk.uikit.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.enablepayment.EnabledPayment;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.adapter.BaseAdapter;
import com.midtrans.sdk.uikit.view.model.PaymentMethodsModel;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentMethodsAdapter extends BaseAdapter<PaymentMethodsModel> {

    private List<PaymentMethodsModel> paymentMethods;
    private PaymentMethodListener paymentMethodListener;

    public interface PaymentMethodListener {
        void onItemClick(int position);
    }

    public PaymentMethodsAdapter(PaymentMethodListener listener) {
        this.paymentMethodListener = listener;
        this.paymentMethods = new ArrayList<>();
        setHasStableIds(true);
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_payment_item_method, parent, false);

        PaymentViewHolder paymentViewHolder = new PaymentViewHolder(view, paymentMethodListener);
        return paymentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PaymentViewHolder viewHolder = (PaymentViewHolder) holder;
        viewHolder.mImageView.setImageResource(paymentMethods.get(position).getImageId());
        viewHolder.name.setText(paymentMethods.get(position).getName());
        viewHolder.description.setText(paymentMethods.get(position).getDescription());

        disablePaymentView(viewHolder, paymentMethods.get(position));
        showPromoBadge(viewHolder, paymentMethods.get(position));
    }

    @Override
    public void addAllData(List<PaymentMethodsModel> data) {
        this.paymentMethods.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void addData(PaymentMethodsModel data) {
        this.paymentMethods.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public PaymentMethodsModel getDataAt(int position) {
        return paymentMethods.get(position);
    }

    @Override
    public List<PaymentMethodsModel> getAllData() {
        return paymentMethods;
    }

    @Override
    public void setData(List<PaymentMethodsModel> data) {
        this.paymentMethods.clear();
        this.paymentMethods.addAll(data);
        this.notifyDataSetChanged();
    }

    private void showPromoBadge(PaymentViewHolder holder, PaymentMethodsModel paymentMethodsModel) {
        if (paymentMethodsModel != null && paymentMethodsModel.isHavePromo()) {
            holder.badgePromo.setVisibility(View.VISIBLE);
        } else {
            holder.badgePromo.setVisibility(View.GONE);
        }
    }

    private void disablePaymentView(PaymentViewHolder holder, PaymentMethodsModel paymentMethod) {
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

        PaymentViewHolder(View itemView, final PaymentMethodListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.text_payment_method_name);
            mImageView = itemView.findViewById(R.id.img_payment_method_icon);
            description = itemView.findViewById(R.id.text_payment_method_description);
            textUnavailable = itemView.findViewById(R.id.text_option_unavailable);
            layoutPaymentUnavailable = itemView.findViewById(R.id.layout_payment_unavailable);
            badgePromo = itemView.findViewById(R.id.badge_promo);

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