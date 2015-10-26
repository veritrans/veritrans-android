package id.co.veritrans.sdk.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.BankTransferActivity;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/19/15.
 */
public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter
        .PaymentViewHolder> {


    private static final String TAG = PaymentMethodsAdapter.class.getSimpleName();

    private static Activity sActivity;
    private ArrayList<PaymentMethodsModel> data = null;


    public PaymentMethodsAdapter(Activity activity, ArrayList<PaymentMethodsModel> data) {
        this.sActivity = activity;
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
            /*Toast.makeText(sActivity, "clicked on " + getAdapterPosition(), Toast.LENGTH_SHORT)
                    .show();*/
            switch (getAdapterPosition()){
                case Constants.PAYMENT_METHOD_OFFERS:
                    break;
                case Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT:
                    Intent intent = new Intent(sActivity, CreditDebitCardFlowActivity.class);
                    sActivity.startActivity(intent);
                    break;
                case Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY:
                    break;
                case Constants.PAYMENT_METHOD_CIMB_CLICKS:
                    break;
                case Constants.PAYMENT_METHOD_EPAY_BRI:
                    break;
                case Constants.PAYMENT_METHOD_BBM_MONEY:
                    break;
                case Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU:
                    break;
                case Constants.PAYMENT_METHOD_MANDIRI_ECASH:
                    break;
                case Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT:
                    break;
                case Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER:
                    Intent startBankPayment = new Intent(sActivity, BankTransferActivity.class);
                    sActivity.startActivity(startBankPayment);
                    break;
            }
        }
    }

}
