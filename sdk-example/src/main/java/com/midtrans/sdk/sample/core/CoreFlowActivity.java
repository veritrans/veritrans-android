package com.midtrans.sdk.sample.core;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.sample.BCAKlikPayActivity;
import com.midtrans.sdk.sample.BankTransferPaymentActivity;
import com.midtrans.sdk.sample.CIMBClickPayPaymentActivity;
import com.midtrans.sdk.sample.CreditCardPaymentActivity;
import com.midtrans.sdk.sample.EpayBRIPaymentActivity;
import com.midtrans.sdk.sample.IndomaretPaymentActivity;
import com.midtrans.sdk.sample.IndosatDompetkuPaymentActivity;
import com.midtrans.sdk.sample.KlikBcaPaymentActivity;
import com.midtrans.sdk.sample.MainActivity;
import com.midtrans.sdk.sample.MandiriBillPaymentActivity;
import com.midtrans.sdk.sample.MandiriClickPaymentActivity;
import com.midtrans.sdk.sample.MandiriECashActivity;
import com.midtrans.sdk.sample.R;
import com.midtrans.sdk.sample.TelkomselEcashPaymentActivity;
import com.midtrans.sdk.sample.XlTunaiPaymentActivity;
import com.midtrans.sdk.sample.utils.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * @author rakawm
 */
public class CoreFlowActivity extends AppCompatActivity {
    private static final int REQ_PAYMENT = 12;
    private RecyclerView coreMethods;
    private CoreFlowListAdapter adapter;
    private ProgressDialog dialog;
    private ArrayList<CoreViewModel> paymentMethodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_flow);
        dialog = new ProgressDialog(this);
        init();
        getpaymentPages();
    }

    private void getpaymentPages() {
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");
        dialog.show();
        MidtransSDK.getInstance().checkout(MainActivity.SAMPLE_USER_ID, new CheckoutCallback() {
            @Override
            public void onSuccess(Token token) {
                Logger.i("snaptoken>success");
                String tokenId = token.getTokenId();
                LocalDataHandler.saveString(Constants.AUTH_TOKEN, tokenId);
                getPaymentOption(tokenId);
            }

            @Override
            public void onFailure(Token token, String reason) {
                Logger.i("snaptoken>failure");
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                adapter.clear();
                showAlertDialog(reason);
            }

            @Override
            public void onError(Throwable error) {
                Logger.i("snaptoken>error");
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                showAlertDialog(error.getMessage());
            }
        });
    }

    private void getPaymentOption(String tokenId) {
        MidtransSDK.getInstance().getTransactionOptions(tokenId, new TransactionOptionsCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                MidtransSDK.getInstance().setCreditCard(transaction.getCreditCard());
                paymentMethodList.clear();
                for (EnabledPayment method : transaction.getEnabledPayments()) {
                    if (method.getCategory() != null && method.getType().equals(R.string.enabled_payment_category_banktransfer)) {

                        CoreViewModel viewModel = generateBankViewModels(method.getType());
                        if (viewModel != null) {
                            paymentMethodList.add(viewModel);
                        }
                    }else{
                        CoreViewModel viewModel = generateCoreViewModels(method.getType());
                        if(viewModel != null){
                            paymentMethodList.add(viewModel);
                        }
                    }
                }
                adapter.setData(paymentMethodList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Transaction transaction, String reason) {
                Logger.i("snaptransaction>failure");
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable error) {
                Logger.i("snaptransaction>error");
            }
        });
    }

    private void init() {
        coreMethods = (RecyclerView) findViewById(R.id.core_flow_list);

        // Init recycler view
        coreMethods.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CoreFlowListAdapter();
        coreMethods.setAdapter(adapter);
        coreMethods.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CoreViewModel coreViewModel = adapter.getItem(position);
                if(coreViewModel.getTitle().equalsIgnoreCase(getString(R.string.name_credit_card))){
                    goToActivity(new Intent(getApplicationContext(), CreditCardPaymentActivity.class));
                }else if(coreViewModel.getTitle().equalsIgnoreCase(getString(R.string.name_bca_klikpay))){
                    goToActivity(new Intent(getApplicationContext(), BCAKlikPayActivity.class));
                }else if(coreViewModel.getTitle().equalsIgnoreCase(getString(R.string.name_klik_bca))){
                    goToActivity(new Intent(getApplicationContext(), KlikBcaPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_epay_bri))){
                    goToActivity(new Intent(getApplicationContext(), EpayBRIPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_cimb_clicks))){
                    goToActivity(new Intent(getApplicationContext(), CIMBClickPayPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_mandiri_click_pay))){
                    goToActivity(new Intent(getApplicationContext(), MandiriClickPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_mandiri_bill_payment))){
                    goToActivity(new Intent(getApplicationContext(), MandiriBillPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_indomaret))){
                    goToActivity(new Intent(getApplicationContext(), IndomaretPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_telkomsel_ecash))){
                    goToActivity(new Intent(getApplicationContext(), TelkomselEcashPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_payment_method_mandiri_ecash))){
                    goToActivity(new Intent(getApplicationContext(), MandiriECashActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_indosat_dompetku))){
                    goToActivity(new Intent(getApplicationContext(), IndosatDompetkuPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_xl_tunai))){
                    goToActivity(new Intent(getApplicationContext(), XlTunaiPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_bank_transfer_bca))){
                    Intent intent = new Intent(getApplicationContext(), BankTransferPaymentActivity.class);
                    intent.putExtra(BankTransferPaymentActivity.TRANSFER_TYPE, getString(R.string.label_bank_transfer_bca));
                    goToActivity(intent);
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_bank_transfer_permata))){
                    Intent intent = new Intent(getApplicationContext(), BankTransferPaymentActivity.class);
                    intent.putExtra(BankTransferPaymentActivity.TRANSFER_TYPE, getString(R.string.label_bank_transfer_permata));
                    goToActivity(intent);
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_bank_transfer_mandiri))){
                    Intent intent = new Intent(getApplicationContext(), BankTransferPaymentActivity.class);
                    intent.putExtra(BankTransferPaymentActivity.TRANSFER_TYPE, getString(R.string.label_bank_transfer_mandiri));
                    goToActivity(intent);
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_bank_transfer_others))){
                    Intent intent = new Intent(getApplicationContext(), BankTransferPaymentActivity.class);
                    intent.putExtra(BankTransferPaymentActivity.TRANSFER_TYPE, getString(R.string.label_bank_transfer_other));
                    goToActivity(intent);
                }
            }
        }));
    }

    private CoreViewModel generateCoreViewModels(String name) {
        if (name.equals(getString(R.string.label_credit_card))) {
           return (new CoreViewModel(getString(R.string.name_credit_card), R.mipmap.ic_credit_sample));
        } else if (name.equals(getString(R.string.label_bca_klikpay))) {
            return (new CoreViewModel(getString(R.string.name_bca_klikpay),R.mipmap.ic_klikbca_sample));
        }  else if (name.equals(getString(R.string.label_bca_klik_bca))) {
           return (new CoreViewModel(getString(R.string.name_klik_bca),R.mipmap.ic_klikbca_sample));
        } else if (name.equals(getString(R.string.label_bri_epay))) {
            return (new CoreViewModel(getString(R.string.name_epay_bri),R.mipmap.bri_epay_sample));
        }else if (name.equals(getString(R.string.label_cimbclick))) {
            return (new CoreViewModel(getString(R.string.name_cimb_clicks),R.mipmap.ic_cimb_sample));
        } else if (name.equals(getString(R.string.label_mandiri_clickpay))) {
           return (new CoreViewModel(getString(R.string.name_mandiri_click_pay),R.mipmap.ic_mandiri_clickpay_sample));
        } else if (name.equals(getString(R.string.label_mandiri_bill))) {
            return (new CoreViewModel(getString(R.string.name_mandiri_bill_payment),R.mipmap.ic_mandiri_bill_payment2_sample));
        } else if (name.equals(getString(R.string.label_payment_indomaret))) {
           return (new CoreViewModel(getString(R.string.name_indomaret),R.mipmap.ic_indomaret_sample));
        } else if (name.equals(getString(R.string.label_telkomsel_ecash))) {
           return (new CoreViewModel(getString(R.string.name_telkomsel_ecash),R.mipmap.ic_telkomsel_ecash_sample));
        }  else if (name.equals(getString(R.string.label_payment_mandiri_ecash))) {
           return (new CoreViewModel(getString(R.string.name_payment_method_mandiri_ecash),R.mipmap.ic_mandiri_e_cash_sample));
        } else if (name.equals(getString(R.string.label_indosat_dompetku))) {
           return (new CoreViewModel(getString(R.string.name_indosat_dompetku),R.mipmap.ic_indosat_sample));
        } else if (name.equals(getString(R.string.label_indosat_dompetku))) {
            return (new CoreViewModel(getString(R.string.name_indosat_dompetku),R.mipmap.ic_indosat_sample));
        }else if (name.equals(getString(R.string.label_xl_tunai))) {
            return (new CoreViewModel(getString(R.string.name_xl_tunai),R.mipmap.ic_xl_tunai_sample));
        }
        else{
            return null;
        }
    }

    private void goToActivity(Intent intent) {
        startActivityForResult(intent, REQ_PAYMENT);
    }

    private CoreViewModel generateBankViewModels(String bank) {
        if(bank.equals(getString(R.string.label_bank_transfer_bca))){
            return new CoreViewModel(getString(R.string.name_bank_transfer_bca), R.drawable.ic_atm);
        }else if(bank.equals(getString(R.string.label_bank_transfer_permata))){
            return new CoreViewModel(getString(R.string.name_bank_transfer_permata), R.drawable.ic_atm);
        }else if(bank.equals(getString(R.string.label_bank_transfer_mandiri))){
            return new CoreViewModel(getString(R.string.name_bank_transfer_mandiri), R.drawable.ic_atm);
        }else if(bank.equals(getString(R.string.label_bank_transfer_other))){
            return new CoreViewModel(getString(R.string.name_bank_transfer_others), R.drawable.ic_atm);
        }else{
            return null;
        }
    }

    private void showAlertDialog(String message){
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setPositiveButton(com.midtrans.sdk.uikit.R.string.btn_retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getpaymentPages();
                        }
                    })
                    .setNegativeButton(com.midtrans.sdk.uikit.R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create();
            alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_PAYMENT && resultCode == RESULT_OK){
            finish();
        }
    }
}
