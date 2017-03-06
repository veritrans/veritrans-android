package com.midtrans.sdk.ui.views.transaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.adapters.PaymentMethodsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Payment;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.views.creditcard.CreditCardActivity;

import java.util.List;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class TransactionActivity extends BaseActivity implements TransactionContract.View,
        PaymentMethodsAdapter.PaymentMethodListener, ItemDetailsAdapter.ItemDetailListener {

    private TransactionPresenter presenter;
    private PaymentMethodsAdapter paymentMethodsAdapter;
    private ItemDetailsAdapter itemDetailsAdapter;

    private LinearLayout layoutProgressContainer;
    private RecyclerView rvPaymentMethods;
    private RecyclerView rvItemDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initProperties();
        bindView();
        setupView();
        checkoutTransaction();
    }

    private void initProperties() {
        presenter = new TransactionPresenter(this, this);
        paymentMethodsAdapter = new PaymentMethodsAdapter(this);
        itemDetailsAdapter = new ItemDetailsAdapter(this, presenter.getItemDetails());
    }

    private void checkoutTransaction() {
        showProgressContainer(true);
        presenter.checkout();
    }

    private void setupView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvPaymentMethods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvPaymentMethods.setAdapter(paymentMethodsAdapter);

        rvItemDetails.setLayoutManager(new LinearLayoutManager(this));
        rvItemDetails.setAdapter(itemDetailsAdapter);

    }

    private void bindView() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        layoutProgressContainer = (LinearLayout) findViewById(R.id.progress_container);
        rvPaymentMethods = (RecyclerView) findViewById(R.id.rv_payment_methods);
        rvItemDetails = (RecyclerView) findViewById(R.id.rv_item_list);
    }


    @Override
    public void showProgressContainer(boolean show) {
        if (show) {
            layoutProgressContainer.setVisibility(View.VISIBLE);
        } else {
            layoutProgressContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showConfirmationDialog(String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.btn_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkoutTransaction();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create();
        alertDialog.show();
    }

    /**
     * @param enabledPayments Listof enabled payment method
     */
    @Override
    public void showPaymentMethods(List<PaymentMethodModel> enabledPayments) {
        paymentMethodsAdapter.setData(enabledPayments);
    }

    @Override
    public void onItemClick(int position) {
        PaymentMethodModel paymentMethod = paymentMethodsAdapter.getItem(position);
        Log.d(TAG, "method:" + paymentMethod.getName());
        showPaymentActivity(paymentMethod);
    }

    private void showPaymentActivity(PaymentMethodModel paymentMethod) {
        switch (paymentMethod.getPaymentType()){
            case Payment.Type.CREDIT_CARD:
                Intent intent = new Intent(this, CreditCardActivity.class);
                startActivityForResult(intent, Constants.IntentCode.PAYMENT);
                break;

            case Payment.Type.BANK_TRANSFER:

                break;
            default:

                break;
        }


//        String authenticationToken = MidtransSDK.getInstance().readAuthenticationToken();
//        if (name.equalsIgnoreCase(getString(R.string.payment_method_credit_card))) {
//            Intent intent = new Intent(this, CreditDebitCardFlowActivity.class);
//            startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bank_transfer))) {
//            Intent startBankPayment = new Intent(this, SelectBankTransferActivity.class);
//            startBankPayment.putStringArrayListExtra(SelectBankTransferActivity.EXTRA_BANK, getBankTransfers());
//            startActivityForResult(startBankPayment, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_mandiri_clickpay))) {
//            Intent startMandiriClickpay = new Intent(this, MandiriClickPayActivity.class);
//            startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bri_epay))) {
//            Intent startMandiriClickpay = new Intent(this, EpayBriActivity.class);
//            startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_cimb_clicks))) {
//            Intent startCIMBClickpay = new Intent(this, CIMBClickPayActivity.class);
//            startActivityForResult(startCIMBClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_mandiri_ecash))) {
//            Intent startMandiriECash = new Intent(this, MandiriECashActivity.class);
//            startActivityForResult(startMandiriECash, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_indosat_dompetku))) {
//            Intent startIndosatPaymentActivity = new Intent(this, IndosatDompetkuActivity.class);
//            startActivityForResult(startIndosatPaymentActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_indomaret))) {
//            Intent startIndomaret = new Intent(this, IndomaretActivity.class);
//            startActivityForResult(startIndomaret, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bca_klikpay))) {
//            Intent startBCAKlikPayActivity = new Intent(this, BCAKlikPayActivity.class);
//            startActivityForResult(startBCAKlikPayActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_klik_bca))) {
//            Intent startKlikBcaActivity = new Intent(this, KlikBCAActivity.class);
//            startKlikBcaActivity.putExtra(getString(R.string.position), Constants.PAYMENT_METHOD_KLIKBCA);
//            startActivityForResult(startKlikBcaActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_telkomsel_cash))) {
//            Intent telkomselCashActivity = new Intent(this, TelkomselCashActivity.class);
//            startActivityForResult(telkomselCashActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_xl_tunai))) {
//            Intent xlTunaiActivity = new Intent(this, XLTunaiActivity.class);
//            startActivityForResult(xlTunaiActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_kioson))) {
//            Intent kiosanActvity = new Intent(this, KiosonActivity.class);
//            startActivityForResult(kiosanActvity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_gci))) {
//            Intent gciActivity = new Intent(this, GCIActivity.class);
//            startActivityForResult(gciActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
//        } else {
//            Toast.makeText(this.getApplicationContext(),
//                    "This feature is not implemented yet.", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onItemShown() {

    }
    /**
     * Created by ziahaqi on 2/22/17.
     */

    public static class CardDetailsFragment extends Fragment {

    }
}
