package com.midtrans.sdk.uikit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

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
        holder.description.setText(data.get(position).getDescription());

        disablePaymentView(holder, data.get(position));
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
        return data.size();
    }

    public interface PaymentMethodListener {
        void onItemClick(int position);
    }

    /**
     * public static view holder class.
     */
    class PaymentViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        ImageView mImageView;
        DefaultTextView textUnavailable;
        LinearLayout layoutPaymentUnavailable;

        PaymentViewHolder(View itemView, final PaymentMethodListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_payment_method_name);
            mImageView = (ImageView) itemView.findViewById(R.id.img_payment_method_icon);
            description = (TextView) itemView.findViewById(R.id.text_payment_method_description);
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
