package id.co.veritrans.sdk.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.BBMMoneyActivity;
import id.co.veritrans.sdk.activities.BankTransferActivity;
import id.co.veritrans.sdk.activities.CIMBClickPayActivity;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.activities.EpayBriActivity;
import id.co.veritrans.sdk.activities.IndomaretActivity;
import id.co.veritrans.sdk.activities.IndosatDompetkuActivity;
import id.co.veritrans.sdk.activities.MandiriClickPayActivity;
import id.co.veritrans.sdk.activities.MandiriECashActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * adapter for payment methods recycler view.
 * holds data of payment method's name and icon.
 * <p/>
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


        /**
         * starts payment flow, it compares name of that view to payment method to start
         * particular payment flow.
         *
         * @param view
         */
        @Override
        public void onClick(View view) {

            TextViewFont nameText = (TextViewFont) view.findViewById(R.id.text_payment_method_name);
            String name = nameText.getText().toString().trim();

            if (name.equalsIgnoreCase(sActivity.getResources()
                    .getString(R.string.credit_card))) {

                Intent intent = new Intent(sActivity, CreditDebitCardFlowActivity.class);
                sActivity.startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(sActivity
                    .getResources().getString(R.string.mandiri_bill_payment))) {

                Intent startMandiriBillpay = new Intent(sActivity, BankTransferActivity.class);
                startMandiriBillpay.putExtra(Constants.POSITION,
                        Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT);

                sActivity.startActivityForResult(startMandiriBillpay, Constants
                        .RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(sActivity
                    .getResources().getString(R.string.bank_transfer))) {

                Intent startBankPayment = new Intent(sActivity, BankTransferActivity.class);
                startBankPayment.putExtra(Constants.POSITION,
                        Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER);

                sActivity.startActivityForResult(startBankPayment,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(sActivity
                    .getResources().getString(R.string.mandiri_click_pay))) {

                Intent startMandiriClickpay = new Intent(sActivity, MandiriClickPayActivity
                        .class);
                sActivity.startActivityForResult(startMandiriClickpay,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (nameText.getText().toString().trim().equalsIgnoreCase(sActivity
                    .getResources().getString(R.string.epay_bri))) {
                Intent startMandiriClickpay = new Intent(sActivity, EpayBriActivity
                        .class);
                sActivity.startActivityForResult(startMandiriClickpay,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (nameText.getText().toString().trim().equalsIgnoreCase(sActivity
                    .getResources().getString(R.string.cimb_clicks))) {
                Intent startCIMBClickpay = new Intent(sActivity, CIMBClickPayActivity
                        .class);
                sActivity.startActivityForResult(startCIMBClickpay,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);


            } else if (nameText.getText().toString().trim().equalsIgnoreCase(sActivity
                    .getResources().getString(R.string.mandiri_e_cash))) {
                Intent startMandiriECash = new Intent(sActivity, MandiriECashActivity.class);
                sActivity.startActivityForResult(startMandiriECash,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity
                    .getResources().getString(R.string.indosat_dompetku))) {
                Intent startIndosatPaymentActivity = new Intent(sActivity, IndosatDompetkuActivity.class);
                sActivity.startActivityForResult(startIndosatPaymentActivity,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (nameText.getText().toString().trim().equalsIgnoreCase(sActivity
                    .getResources().getString(R.string.indomaret))) {
                Intent startIndomaret = new Intent(sActivity, IndomaretActivity.class);
                sActivity.startActivityForResult(startIndomaret,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (nameText.getText().toString().trim().equalsIgnoreCase(sActivity
                    .getResources().getString(R.string.bbm_money))) {
                Intent startBBMMoney = new Intent(sActivity, BBMMoneyActivity.class);
                sActivity.startActivityForResult(startBBMMoney,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else {
                showMessage();
            }
        }

        public void showMessage() {
            Toast.makeText(sActivity.getApplicationContext(),
                    "This feature is not implemented yet.", Toast.LENGTH_SHORT).show();
        }

    }

}
