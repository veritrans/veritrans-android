package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.callbacks.PaymentStatusCallback;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.InstructionEpayBriFragment;
import id.co.veritrans.sdk.fragments.PaymentTransactionStatusFragment;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.models.TransactionStatusResponse;

public class EpayBriActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PAYMENT_WEB_INTENT = 150;
    private Button btConfirmPayment = null;
    private Toolbar toolbar = null;
    private VeritransSDK veritransSDK = null;
    private InstructionEpayBriFragment instructionEpayBriFragment;
    private TransactionResponse transactionResponse;
    private FragmentManager fragmentManager;
    private String currentFragmentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_epay_bri);
        veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK == null) {
            SdkUtil.showSnackbar(EpayBriActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }

        initializeViews();
        setUpFragment();
    }

    private void initializeViews() {
        btConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btConfirmPayment.setOnClickListener(this);
    }

    private void setUpFragment() {

        // setup  fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        instructionEpayBriFragment = new InstructionEpayBriFragment();
        fragmentTransaction.add(R.id.bri_container_layout,
                instructionEpayBriFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {
            makeTransaction();
        }
    }

    private void makeTransaction() {

        SdkUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        veritransSDK.paymentUsingEpayBri(EpayBriActivity.this,
                new TransactionCallback() {

                    @Override
                    public void onFailure(String errorMessage, TransactionResponse
                            transactionResponse) {
                        SdkUtil.hideKeyboard(EpayBriActivity.this);
                        Toast.makeText(getApplicationContext(), "failed : " + errorMessage, Toast
                                .LENGTH_SHORT).show();
                        //TODO show message
                    }

                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        /*Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT)
                                .show();*/
                        SdkUtil.hideKeyboard(EpayBriActivity.this);
                        if (transactionResponse != null &&
                                !TextUtils.isEmpty(transactionResponse.getRedirectUrl())) {
                            EpayBriActivity.this.transactionResponse = transactionResponse;
                            Intent intentPaymentWeb = new Intent(EpayBriActivity.this, PaymentWebActivity.class);
                            intentPaymentWeb.putExtra(Constants.WEBURL, transactionResponse.getRedirectUrl());
                            startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                        } else {
                            //TODO show message
                        }

                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("reqCode:" + requestCode + ",res:" + resultCode);
        if (resultCode == RESULT_OK && transactionResponse != null &&
                !TextUtils.isEmpty(transactionResponse.getTransactionId())) {
            veritransSDK.getPaymentStatus(this, transactionResponse.getTransactionId(), new PaymentStatusCallback() {
                @Override
                public void onFailure(String errorMessage, TransactionStatusResponse transactionStatusResponse) {
                    PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                            PaymentTransactionStatusFragment.newInstance(transactionResponse);
                    replaceFragment(paymentTransactionStatusFragment, true, false);
                }

                @Override
                public void onSuccess(TransactionStatusResponse transactionStatusResponse) {
                    PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                            PaymentTransactionStatusFragment.newInstance(transactionResponse);
                    replaceFragment(paymentTransactionStatusFragment, true, false);
                }
            });
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, boolean clearBackStack) {
        if (fragment != null) {
            Logger.i("replace freagment");
            boolean fragmentPopped = false;
            String backStateName = fragment.getClass().getName();

            if (clearBackStack) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
            }

            if (!fragmentPopped) { //fragment not in back stack, create it.
                Logger.i("fragment not in back stack, create it");
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.card_container, fragment, backStateName);
                if (addToBackStack) {
                    ft.addToBackStack(backStateName);
                }
                ft.commit();
                currentFragmentName = backStateName;
                //currentFragment = fragment;
            }
        }
    }
}