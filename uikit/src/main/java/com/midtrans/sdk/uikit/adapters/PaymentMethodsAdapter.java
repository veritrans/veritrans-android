package com.midtrans.sdk.uikit.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.SectionedPaymentMethod;

import java.util.ArrayList;

/**
 * adapter for payment methods recycler view. holds data of payment method's name and icon.
 * <p/>
 * Created by shivam on 10/19/15.
 */
public class PaymentMethodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PaymentMethodsAdapter.class.getSimpleName();

    private ArrayList<SectionedPaymentMethod> data = null;
    private PaymentMethodListener paymentMethodListener;

    public PaymentMethodsAdapter(PaymentMethodListener listener) {
        this.paymentMethodListener = listener;
        this.data = new ArrayList<>();
    }

    public SectionedPaymentMethod getItem(int position) {
        return data.get(position);
    }

    public void setData(ArrayList<SectionedPaymentMethod> models) {
        this.data.clear();
        this.data.addAll(models);
        this.notifyDataSetChanged();
        Log.d("searchx" , "setdata>size:" + data.size());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Logger.d("searchx", "viewtype:" + viewType);

        View view;

        switch (viewType) {
            case PaymentMethods.ITEM:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_row_payment_methods, parent, false);

                PaymentViewHolder paymentViewHolder = new PaymentViewHolder(view, paymentMethodListener);
                return paymentViewHolder;

            case PaymentMethods.SECTION:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_row_payment_sections, parent, false);
                SectionViewHolder sectionViewHolder = new SectionViewHolder(view);
                return sectionViewHolder;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SectionedPaymentMethod sectionedPaymentMethod = data.get(position);
        Logger.d("searchx", "onbind>name : | type :" + sectionedPaymentMethod.getModel().getName() + " | " + sectionedPaymentMethod.getType());

        switch (sectionedPaymentMethod.getType()) {
            case PaymentMethods.ITEM:
                ((PaymentViewHolder) holder).mImageView.setImageResource(data.get(position).getModel().getImageId());
                ((PaymentViewHolder) holder).name.setText(data.get(position).getModel().getName());
                break;

            case PaymentMethods.SECTION:
                ((SectionViewHolder) holder).sectionName.setText(data.get(position).getModel().getName());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("searchx", "searchgetItemViewType");
        if (!data.isEmpty()) {
            SectionedPaymentMethod sectionedPaymentMethod = data.get(position);
            if (sectionedPaymentMethod != null) {
                return sectionedPaymentMethod.getType();
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface PaymentMethodListener {
        void onItemClick(int position);
    }

    /**
     * view holder class for payment methods.
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

    /**
     * view holder class for sections
     */
    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionName = (TextView) itemView.findViewById(R.id.text_row_section);
        }
    }

}
