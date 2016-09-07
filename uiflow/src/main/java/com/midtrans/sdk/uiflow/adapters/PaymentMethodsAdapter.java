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

    private static Activity sActivity;
    private ArrayList<PaymentMethodsModel> data = null;

    public PaymentMethodsAdapter(Activity activity, ArrayList<PaymentMethodsModel> data) {
        sActivity = activity;
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

        TextView name;
        ImageView mImageView;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_payment_method_name);
            mImageView = (ImageView) itemView.findViewById(R.id.img_payment_method_icon);
            itemView.setOnClickListener(this);
        }


        /**
         * starts payment flow, it compares name of that view to payment method to start
         * particular payment flow.
         *
         * @param view  clicked view
         */
        @Override
        public void onClick(View view) {

            TextView nameText = (TextView) view.findViewById(R.id.text_payment_method_name);
            String name = nameText.getText().toString().trim();

            if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_credit_card))) {
                Intent intent = new Intent(sActivity, CreditDebitCardFlowActivity.class);
                sActivity.startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_bank_transfer))) {
                Intent startBankPayment = new Intent(sActivity, SelectBankTransferActivity.class);
                if (sActivity instanceof PaymentMethodsActivity) {
                    startBankPayment.putStringArrayListExtra(SelectBankTransferActivity.EXTRA_BANK, ((PaymentMethodsActivity) sActivity).getBankTrasfers());
                }
                sActivity.startActivityForResult(startBankPayment, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_mandiri_clickpay))) {
                Intent startMandiriClickpay = new Intent(sActivity, MandiriClickPayActivity.class);
                sActivity.startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_bri_epay))) {
                Intent startMandiriClickpay = new Intent(sActivity, EpayBriActivity.class);
                sActivity.startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_cimb_clicks))) {
                Intent startCIMBClickpay = new Intent(sActivity, CIMBClickPayActivity.class);
                sActivity.startActivityForResult(startCIMBClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_mandiri_ecash))) {
                Intent startMandiriECash = new Intent(sActivity, MandiriECashActivity.class);
                sActivity.startActivityForResult(startMandiriECash, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_indosat_dompetku))) {
                Intent startIndosatPaymentActivity = new Intent(sActivity, IndosatDompetkuActivity.class);
                sActivity.startActivityForResult(startIndosatPaymentActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_indomaret))) {
                Intent startIndomaret = new Intent(sActivity, IndomaretActivity.class);
                sActivity.startActivityForResult(startIndomaret, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_bbm_money))) {
                Intent startBBMMoney = new Intent(sActivity, BBMMoneyActivity.class);
                sActivity.startActivityForResult(startBBMMoney, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_offers))) {
                Intent startOffersActivity = new Intent(sActivity, OffersActivity.class);
                sActivity.startActivityForResult(startOffersActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_bca_klikpay))) {
                Intent startBCAKlikPayActivity = new Intent(sActivity, BCAKlikPayActivity.class);
                sActivity.startActivityForResult(startBCAKlikPayActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_klik_bca))) {
                Intent startKlikBcaActivity = new Intent(sActivity, KlikBCAActivity.class);
                startKlikBcaActivity.putExtra(sActivity.getString(R.string.position), Constants.PAYMENT_METHOD_KLIKBCA);
                sActivity.startActivityForResult(startKlikBcaActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_telkomsel_cash))) {
                Intent telkomselCashActivity = new Intent(sActivity, TelkomselCashActivity.class);
                sActivity.startActivityForResult(telkomselCashActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_xl_tunai))) {
                Intent xlTunaiActivity = new Intent(sActivity, XLTunaiActivity.class);
                sActivity.startActivityForResult(xlTunaiActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            }else if (name.equalsIgnoreCase(sActivity.getString(R.string.payment_method_kioson))){
                Intent kiosanActvity = new Intent(sActivity, KiosonActivity.class);
                sActivity.startActivityForResult(kiosanActvity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            }else {
                showMessage();
            }
        }

        public void showMessage() {
            Toast.makeText(sActivity.getApplicationContext(),
                    "This feature is not implemented yet.", Toast.LENGTH_SHORT).show();
        }

    }

}
