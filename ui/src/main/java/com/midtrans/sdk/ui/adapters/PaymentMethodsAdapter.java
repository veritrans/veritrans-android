package com.midtrans.sdk.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.models.PaymentMethodModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 2/19/17.
 */
public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.PaymentViewHolder> {

    private ArrayList<PaymentMethodModel> paymentMethodModelList = new ArrayList<>();
    private PaymentMethodListener paymentMethodListener;

    public PaymentMethodsAdapter(PaymentMethodListener listener) {
        this.paymentMethodListener = listener;
    }

    public PaymentMethodModel getItem(int position) {
        return paymentMethodModelList.get(position);
    }

    public void setData(List<PaymentMethodModel> models) {
        this.paymentMethodModelList.clear();
        this.paymentMethodModelList.addAll(models);
        this.notifyDataSetChanged();
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_payment_methods, parent, false);

        return new PaymentViewHolder(view, paymentMethodListener);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            PaymentMethodModel paymentMethodModel = paymentMethodModelList.get(position);
            holder.logo.setImageResource(paymentMethodModel.getImageId());
            holder.name.setText(paymentMethodModel.getName());
            holder.description.setText(paymentMethodModel.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return paymentMethodModelList.size();
    }

    public interface PaymentMethodListener {
        void onItemClick(int position);
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView description;
        private ImageView logo;

        PaymentViewHolder(View itemView, final PaymentMethodListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_payment_method_name);
            logo = (ImageView) itemView.findViewById(R.id.img_payment_method_icon);
            description = (TextView) itemView.findViewById(R.id.text_payment_method_description);
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
