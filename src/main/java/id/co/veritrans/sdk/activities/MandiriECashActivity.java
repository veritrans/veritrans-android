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

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.InstructionCIMBFragment;
import id.co.veritrans.sdk.fragments.InstructionMandiriECashFragment;
import id.co.veritrans.sdk.fragments.MandiriClickPayFragment;
import id.co.veritrans.sdk.fragments.PaymentTransactionStatusFragment;
import id.co.veritrans.sdk.models.TransactionResponse;

/**
 * Created by Ankit on 11/30/15.
 */
public class MandiriECashActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PAYMENT_WEB_INTENT = 152;
    private InstructionMandiriECashFragment mandiriECashFragment = null;
    private Button buttonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private VeritransSDK mVeritransSDK = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private int RESULT_CODE = RESULT_CANCELED;
    private int position = Constants.PAYMENT_METHOD_MANDIRI_ECASH;

    private FragmentManager fragmentManager;
    private String currentFragmentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_mandiri_e_cash);
        mVeritransSDK = VeritransSDK.getVeritransSDK();

        if (mVeritransSDK == null) {
            SdkUtil.showSnackbar(MandiriECashActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
        initializeViews();
        setUpFragment();
    }

    private void initializeViews() {
        buttonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonConfirmPayment.setOnClickListener(this);
    }

    private void setUpFragment() {

        // setup  fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mandiriECashFragment = new InstructionMandiriECashFragment();

        fragmentTransaction.add(R.id.mandiri_e_cash_container,
                mandiriECashFragment);
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
        }
    }
}

