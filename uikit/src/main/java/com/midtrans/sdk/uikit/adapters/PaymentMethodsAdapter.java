package com.midtrans.sdk.uikit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.uikit.R;

import java.util.ArrayList;

/**
 * adapter for payment methods recycler view. holds data of payment method's name and icon.
 * <p/>
 * Created by shivam on 10/19/15.
 */
public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.PaymentViewHolder> {

    private static final String TAG = PaymentMethodsAdapter.class.getSimpleName();

    private ArrayList<PaymentMethodsModel> data = null;
    private PaymentMethodListener paymentMethodListener;

    public PaymentMethodsAdapter(PaymentMethodListener listener) {
        this.paymentMethodListener = listener;
        this.data = new ArrayList<>();
    }

    public PaymentMethodsModel getItem(int position) {
        return data.get(position);
    }

    public void setData(ArrayList<PaymentMethodsModel> models) {
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

    public interface PaymentMethodListener {
        void onItemClick(int position);
    }

    /**
     * public static view holder class.
     */
    public static class PaymentViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView mImageView;

        public PaymentViewHolder(View itemView, final PaymentMethodListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_payment_method_name);
            mImageView = (ImageView) itemView.findViewById(R.id.img_payment_method_icon);
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
