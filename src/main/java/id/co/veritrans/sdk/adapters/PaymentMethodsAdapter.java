package id.co.veritrans.sdk.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/19/15.
 */
public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter
        .PaymentViewHolder> {


    private static final String TAG = PaymentMethodsAdapter.class.getSimpleName();

    private static Context mContext;
    private ArrayList<PaymentMethodsModel> data = null;


    public PaymentMethodsAdapter(Context mContext, ArrayList<PaymentMethodsModel> data) {
        this.mContext = mContext;
        this.data = data;
        Logger.d(TAG, "setting adapter");

    }


    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_payment_methods, parent, false);

        PaymentViewHolder paymentViewHolder = new PaymentViewHolder(view);
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
    public static class PaymentViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {


        TextViewFont name;
        ImageView mImageView;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            name = (TextViewFont) itemView.findViewById(R.id.text_payment_method_name);
            mImageView = (ImageView) itemView.findViewById(R.id.img_payment_method_icon);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "clicked on " + getAdapterPosition(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
