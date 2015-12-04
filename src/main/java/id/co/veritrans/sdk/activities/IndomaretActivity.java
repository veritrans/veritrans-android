package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import id.co.veritrans.sdk.fragments.BankTransferFragment;
import id.co.veritrans.sdk.fragments.IndomaretPaymentFragment;
import id.co.veritrans.sdk.fragments.IndomaretPaymentStatusFragment;
import id.co.veritrans.sdk.fragments.InstructionIndomaretFragment;
import id.co.veritrans.sdk.models.IndomaretRequestModel;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by Ankit on 12/01/15.
 */
public class IndomaretActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private TextViewFont textViewOrderId = null;
    private TextViewFont textViewAmount = null;
    private Button buttonConfirmPayment = null;
    private AppBarLayout appBarLayout = null;
    private TextViewFont textViewTitle = null;

    private VeritransSDK veritransSDK = null;
    private Toolbar toolbar = null;

    private InstructionIndomaretFragment instructionIndomaretFragment = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private int position = Constants.PAYMENT_METHOD_INDOMARET;
    private int RESULT_CODE = RESULT_CANCELED;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indomaret);

        veritransSDK = VeritransSDK.getVeritransSDK();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(Constants.POSITION, Constants
                    .PAYMENT_METHOD_INDOMARET);
        } else {
            SdkUtil.showSnackbar(IndomaretActivity.this, Constants.ERROR_SOMETHING_WENT_WRONG);
            finish();
        }

        initializeView();
        bindDataToView();
        setUpHomeFragment();
    }


    /**
     * set up {@link BankTransferFragment} to display payment instructions.
     */
    private void setUpHomeFragment() {
        // setup home fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        instructionIndomaretFragment = new InstructionIndomaretFragment();

        fragmentTransaction.add(R.id.indomaret_container,
                instructionIndomaretFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        setResultAndFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return false;
    }

    private void initializeView() {
        textViewOrderId = (TextViewFont) findViewById(R.id.text_order_id);
        textViewAmount = (TextViewFont) findViewById(R.id.text_amount);
        textViewTitle = (TextViewFont) findViewById(R.id.text_title);
        buttonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);

        //setup tool bar
        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindDataToView() {
        if (veritransSDK != null) {
            textViewAmount.setText(Constants.CURRENCY_PREFIX + " " + veritransSDK
                    .getTransactionRequest().getAmount());
            textViewOrderId.setText("" + veritransSDK.getTransactionRequest().getOrderId());
            buttonConfirmPayment.setTypeface(veritransSDK.getTypefaceOpenSansSemiBold());
            buttonConfirmPayment.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {
            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {

                performTrsansaction();

            } else if (currentFragment.equalsIgnoreCase(PAYMENT_FRAGMENT)) {

                appBarLayout.setExpanded(true);

                if (transactionResponse != null) {
                    setUpTransactionStatusFragment(transactionResponse);
                } else {
                    RESULT_CODE = RESULT_OK;
                    SdkUtil.showSnackbar(IndomaretActivity.this, SOMETHING_WENT_WRONG);
                    onBackPressed();
                }
            } else {
                RESULT_CODE = RESULT_OK;
                onBackPressed();
            }
        }
    }

    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = STATUS_FRAGMENT;
        buttonConfirmPayment.setText(R.string.done);

        collapsingToolbarLayout.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);

        IndomaretPaymentStatusFragment indomaretPaymentStatusFragment =
                IndomaretPaymentStatusFragment.newInstance(transactionResponse, false);


        // setup transaction status fragment
        fragmentTransaction.replace(R.id.indomaret_container,
                indomaretPaymentStatusFragment, STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void setUpTransactionFragment(final TransactionResponse
                                                  transactionResponse) {
        if (transactionResponse != null) {
            // setup transaction fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            IndomaretPaymentFragment indomaretPaymentFragment =
                    IndomaretPaymentFragment.newInstance(transactionResponse);
            fragmentTransaction.replace(R.id.indomaret_container,
                    indomaretPaymentFragment, PAYMENT_FRAGMENT);
            fragmentTransaction.addToBackStack(PAYMENT_FRAGMENT);
            fragmentTransaction.commit();
            currentFragment = PAYMENT_FRAGMENT;
        } else {
            SdkUtil.showSnackbar(IndomaretActivity.this, SOMETHING_WENT_WRONG);
            onBackPressed();
        }
    }

    private void performTrsansaction() {

        SdkUtil.showProgressDialog(IndomaretActivity.this, getString(R.string.processing_payment),
                false);

        IndomaretRequestModel.CstoreEntity cstoreEntity = new IndomaretRequestModel.CstoreEntity();
        cstoreEntity.setMessage("demo_message");
        cstoreEntity.setStore("indomaret");

        //Execute transaction
        veritransSDK.paymentUsingIndomaret(IndomaretActivity.this, new
                TransactionCallback() {

                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        Toast.makeText(IndomaretActivity.this, "Transaction success:  " +
                                transactionResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();

                        SdkUtil.hideProgressDialog();

                        if (transactionResponse != null) {
                            IndomaretActivity.this.transactionResponse = transactionResponse;
                            appBarLayout.setExpanded(true);
                            setUpTransactionFragment(transactionResponse);
                        } else {
                            onBackPressed();
                        }

                    }

                    @Override
                    public void onFailure(String errorMessage, TransactionResponse
                            transactionResponse) {

                        Toast.makeText(IndomaretActivity.this, "Transaction failed: " +
                                        errorMessage,
                                Toast.LENGTH_SHORT).show();
                        try {
                            IndomaretActivity.this.errorMessage = errorMessage;
                            IndomaretActivity.this.transactionResponse = transactionResponse;

                            SdkUtil.hideProgressDialog();
                            SdkUtil.showSnackbar(IndomaretActivity.this, "" + errorMessage);
                        } catch (NullPointerException ex) {
                            Logger.e("transaction error is " + errorMessage);
                        }

                    }
                }, cstoreEntity);
    }


    public int getPosition() {
        return position;
    }


    /**
     * in case of transaction failure it will change the text of confirm payment button to 'RETRY'
     */
    public void activateRetry() {

        if (buttonConfirmPayment != null) {
            buttonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }

    /**
     * send result back to  {@link PaymentMethodsActivity} and finish current activity.
     */
    private void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(Constants.TRANSACTION_RESPONSE, transactionResponse);
        data.putExtra(Constants.TRANSACTION_ERROR_MESSAGE, errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }
}