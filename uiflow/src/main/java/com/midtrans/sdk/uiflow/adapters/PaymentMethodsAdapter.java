package com.midtrans.sdk.uiflow.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.midtrans.sdk.coreflow.core.Constants;
import com.midtrans.sdk.coreflow.core.Logger;
import com.midtrans.sdk.coreflow.models.PaymentMethodsModel;
import com.midtrans.sdk.uiflow.activities.EpayBriActivity;
import com.midtrans.sdk.uiflow.activities.OffersActivity;
import com.midtrans.sdk.uiflow.activities.SelectBankTransferActivity;

import com.midtrans.sdk.uiflow.R;
import com.midtrans.sdk.uiflow.activities.BBMMoneyActivity;
import com.midtrans.sdk.uiflow.activities.BCAKlikPayActivity;
import com.midtrans.sdk.uiflow.activities.CIMBClickPayActivity;
import com.midtrans.sdk.uiflow.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uiflow.activities.IndomaretActivity;
import com.midtrans.sdk.uiflow.activities.IndosatDompetkuActivity;
import com.midtrans.sdk.uiflow.activities.KiosonActivity;
import com.midtrans.sdk.uiflow.activities.KlikBCAActivity;
import com.midtrans.sdk.uiflow.activities.MandiriClickPayActivity;
import com.midtrans.sdk.uiflow.activities.MandiriECashActivity;
import com.midtrans.sdk.uiflow.activities.PaymentMethodsActivity;
import com.midtrans.sdk.uiflow.activities.TelkomselCashActivity;
import com.midtrans.sdk.uiflow.activities.XLTunaiActivity;

/**
 * adapter for payment methods recycler view.
 * holds data of payment method's name and icon.
 * <p/>
 * Created by shivam on 10/19/15.
 */
public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.PaymentViewHolder> {

    private static final String TAG = PaymentMethodsAdapter.class.getSimpleName();

    private ArrayList<PaymentMethodsModel> data = null;
    private PaymentMethodListener paymentMethodListener;

    public PaymentMethodsModel getItem(int position) {
        return data.get(position);
    }

    public interface PaymentMethodListener {
        void onItemClick(int position);
    }

    public PaymentMethodsAdapter(PaymentMethodListener listener) {
        this.paymentMethodListener = listener;
        this.data = new ArrayList<>();
    }

    public void setData(ArrayList<PaymentMethodsModel> models){
        this.data.clear();
        this.data.addAll(models);
        this.notifyDataSetChanged();
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_payment_methods, parent, false);

        PaymentViewHolder paymentViewHolder = new PaymentViewHolder(view, paymentMethodListener);
        return paymentViewHolder;
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder holder, int position) {
        holder.mImageView.setImageResource(data.get(position).getImageId());
        holder.name.setText(data.get(position).getName());
        Logger.d(TAG, "name is " + data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * public static view holder class.
     */
    public static class PaymentViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView mImageView;

        public PaymentViewHolder(View itemView, final PaymentMethodListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_payment_method_name);
            mImageView = (ImageView) itemView.findViewById(R.id.img_payment_method_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

}
