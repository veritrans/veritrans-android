package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
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
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.BankTransferPaymentFragment;
import id.co.veritrans.sdk.fragments.InstructionCIMBFragment;
import id.co.veritrans.sdk.fragments.MandiriBillPayFragment;
import id.co.veritrans.sdk.fragments.MandiriClickPayFragment;
import id.co.veritrans.sdk.fragments.PaymentTransactionStatusFragment;
import id.co.veritrans.sdk.models.CIMBClickPayModel;
import id.co.veritrans.sdk.models.MandiriClickPayModel;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.models.TransactionStatusResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by Ankit on 11/26/15.
 */
public class CIMBClickPayActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PAYMENT_WEB_INTENT = 151;
    private InstructionCIMBFragment mCIMBClickPayFragment = null;
    private Button mButtonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private VeritransSDK mVeritransSDK = null;
    private TransactionResponse mTransactionResponse = null;
    private String errorMessage = null;
    private int RESULT_CODE = RESULT_CANCELED;
    private int position = Constants.PAYMENT_METHOD_CIMB_CLICKS;

    private FragmentManager fragmentManager;
    private String currentFragmentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_cimb_clickpay);
        mVeritransSDK = VeritransSDK.getVeritransSDK();

        if (mVeritransSDK == null) {
            SdkUtil.showSnackbar(CIMBClickPayActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
        initializeViews();
        setUpFragment();
    }

    private void initializeViews() {
        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mButtonConfirmPayment.setOnClickListener(this);
    }

    private void setUpFragment() {

        // setup  fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mCIMBClickPayFragment = new InstructionCIMBFragment();
        fragmentTransaction.add(R.id.cimb_clickpay_container,
                mCIMBClickPayFragment);
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

    private void makeTransaction(){
        SdkUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        mVeritransSDK.paymentUsingCIMBClickPay(CIMBClickPayActivity.this, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse cimbClickPayTransferResponse) {
                SdkUtil.hideProgressDialog();
                SdkUtil.hideKeyboard(CIMBClickPayActivity.this);
//                if (cimbClickPayTransferResponse != null) {
//                    mTransactionResponse = cimbClickPayTransferResponse;
//                } else {
//                    onBackPressed();
//                }

                if (cimbClickPayTransferResponse != null &&
                        !TextUtils.isEmpty(cimbClickPayTransferResponse.getRedirectUrl())) {
                    CIMBClickPayActivity.this.mTransactionResponse = cimbClickPayTransferResponse;
                    Intent intentPaymentWeb = new Intent(CIMBClickPayActivity.this, PaymentWebActivity.class);
                    intentPaymentWeb.putExtra(Constants.WEBURL, cimbClickPayTransferResponse.getRedirectUrl());
                    startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                } else {
                    onBackPressed();
                }

            }

            @Override
            public void onFailure(String errorMessage, TransactionResponse transactionResponse) {

                try {
                    CIMBClickPayActivity.this.errorMessage = errorMessage;
                    mTransactionResponse = transactionResponse;
                    SdkUtil.hideProgressDialog();
                    SdkUtil.hideKeyboard(CIMBClickPayActivity.this);
                    SdkUtil.showSnackbar(CIMBClickPayActivity.this, "" + errorMessage);
                } catch (NullPointerException ex) {
                    Logger.e("transaction error is " + errorMessage);
                }
            }
        });
    }
}

