package id.co.veritrans.sdk.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.BBMMoneyPaymentFragment;
import id.co.veritrans.sdk.fragments.BBMMoneyPaymentStatusFragment;
import id.co.veritrans.sdk.fragments.BankTransferFragment;
import id.co.veritrans.sdk.fragments.InstructionBBMMoneyFragment;
import id.co.veritrans.sdk.models.BBMCallBackUrl;
import id.co.veritrans.sdk.models.BBMUrlEncodeJson;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;
import id.co.veritrans.sdk.widgets.VeritransDialog;

/**
 * Created by Ankit on 12/3/15.
 */
public class BBMMoneyActivity extends AppCompatActivity implements View.OnClickListener {

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
    private LinearLayout layoutPayWithBBM = null;

    private VeritransSDK veritransSDK = null;
    private Toolbar toolbar = null;

    private InstructionBBMMoneyFragment instructionBBMMoneyFragment = null;
    private BBMMoneyPaymentFragment bbmMoneyPaymentFragment = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private int position = Constants.PAYMENT_METHOD_BBM_MONEY;
    private int RESULT_CODE = RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbm_money);

        veritransSDK = VeritransSDK.getVeritransSDK();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(Constants.POSITION, Constants
                    .PAYMENT_METHOD_BBM_MONEY);
        } else {
            SdkUtil.showSnackbar(BBMMoneyActivity.this, Constants.ERROR_SOMETHING_WENT_WRONG);
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
        instructionBBMMoneyFragment = new InstructionBBMMoneyFragment();

        fragmentTransaction.add(R.id.bbm_money_container,
                instructionBBMMoneyFragment, HOME_FRAGMENT);
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
        layoutPayWithBBM = (LinearLayout) findViewById(R.id.layout_pay_with_bbm);

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
            layoutPayWithBBM.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_confirm_payment) {
            if (instructionBBMMoneyFragment.isBBMMoneyInstalled(Constants.BBM_MONEY_PACKAGE)) {

                if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {

                    performTransaction();

                } else if (currentFragment.equalsIgnoreCase(PAYMENT_FRAGMENT)) {

                    appBarLayout.setExpanded(true);

                    if (transactionResponse != null) {
//                        setUpTransactionStatusFragment(transactionResponse);

                    } else {
                        RESULT_CODE = RESULT_OK;
                        SdkUtil.showSnackbar(BBMMoneyActivity.this, SOMETHING_WENT_WRONG);
                        onBackPressed();
                    }
                } else {
                    RESULT_CODE = RESULT_OK;
                    onBackPressed();
                }

            } else {
                Logger.i("BBM Not present");
                VeritransDialog veritransDialog = new VeritransDialog(BBMMoneyActivity.this,
                        getString(R.string.title_bbm_not_found),
                        getString(R.string.message_bbm_not_found), getString(R.string.got_it), "");
                veritransDialog.show();
            }
        }

        if (view.getId() == R.id.layout_pay_with_bbm) {
            String encodedUrl = createEncodedUrl();
            String feedUrl = Constants.BBM_PREFIX_URL + encodedUrl;

            if (instructionBBMMoneyFragment.isBBMMoneyInstalled(Constants.BBM_MONEY_PACKAGE)) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(feedUrl)));
            } else {
                instructionBBMMoneyFragment.openPlayStore();
            }
        }
    }

    private void performTransaction() {

        SdkUtil.showProgressDialog(BBMMoneyActivity.this, false);

        //Execute transaction
        veritransSDK.paymentUsingBBMMoney(BBMMoneyActivity.this, new
                TransactionCallback() {

                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        Toast.makeText(BBMMoneyActivity.this, "Transaction success:  " +
                                transactionResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        SdkUtil.hideProgressDialog();

                        if (transactionResponse != null) {
                            BBMMoneyActivity.this.transactionResponse = transactionResponse;
                            appBarLayout.setExpanded(true);
                            setUpTransactionFragment(transactionResponse);
                        } else {
                            onBackPressed();
                        }

                    }

                    @Override
                    public void onFailure(String errorMessage, TransactionResponse
                            transactionResponse) {

                        Toast.makeText(BBMMoneyActivity.this, "Transaction failed: " + errorMessage,
                                Toast.LENGTH_SHORT).show();
                        try {
                            BBMMoneyActivity.this.errorMessage = errorMessage;
                            BBMMoneyActivity.this.transactionResponse = transactionResponse;

                            SdkUtil.hideProgressDialog();
                            SdkUtil.showSnackbar(BBMMoneyActivity.this, "" + errorMessage);
                        } catch (NullPointerException ex) {
                            Logger.e("transaction error is " + errorMessage);
                        }
                    }
                });
    }

    private void setUpTransactionStatusFragment(final TransactionResponse transactionResponse) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = STATUS_FRAGMENT;
        buttonConfirmPayment.setVisibility(View.VISIBLE);
        layoutPayWithBBM.setVisibility(View.GONE);
        buttonConfirmPayment.setText(R.string.done);

        collapsingToolbarLayout.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        BBMMoneyPaymentStatusFragment bbmMoneyPaymentStatusFragment =
                BBMMoneyPaymentStatusFragment.newInstance(transactionResponse, false);

        // setup transaction status fragment
        fragmentTransaction.replace(R.id.bbm_money_container,
                bbmMoneyPaymentStatusFragment, STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void setUpTransactionFragment(final TransactionResponse transactionResponse) {
        if (transactionResponse != null) {
            // setup transaction fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            bbmMoneyPaymentFragment =
                    BBMMoneyPaymentFragment.newInstance(transactionResponse);
            fragmentTransaction.replace(R.id.bbm_money_container,
                    bbmMoneyPaymentFragment, PAYMENT_FRAGMENT);
            fragmentTransaction.addToBackStack(PAYMENT_FRAGMENT);
            fragmentTransaction.commit();
            buttonConfirmPayment.setVisibility(View.GONE);
            layoutPayWithBBM.setVisibility(View.VISIBLE);
            currentFragment = PAYMENT_FRAGMENT;
        } else {
            SdkUtil.showSnackbar(BBMMoneyActivity.this, SOMETHING_WENT_WRONG);
            onBackPressed();
        }
    }

    public int getPosition() {
        return position;
    }

    public void activateRetry() {

        if (buttonConfirmPayment != null) {
            buttonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }

    private void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(Constants.TRANSACTION_RESPONSE, transactionResponse);
        data.putExtra(Constants.TRANSACTION_ERROR_MESSAGE, errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }

    private String createEncodedUrl() {
        String encodedUrl = null;
        BBMCallBackUrl bbmCallBackUrl = new BBMCallBackUrl();
        bbmCallBackUrl.setCheckStatus(Constants.CHECK_STATUS);
        bbmCallBackUrl.setBeforePaymentError(Constants.BEFORE_PAYMENT_ERROR);
        bbmCallBackUrl.setUserCancel(Constants.USER_CANCEL);

        BBMUrlEncodeJson bbmUrlEncodeJson = new BBMUrlEncodeJson();
        if (bbmMoneyPaymentFragment.PERMATA_VA != null) {
            bbmUrlEncodeJson.setReference(bbmMoneyPaymentFragment.PERMATA_VA);
        }
        bbmUrlEncodeJson.setCallbackUrl(bbmCallBackUrl);
        String jsonString = bbmUrlEncodeJson.getString();
        Logger.i("JSON String: " + jsonString);

        try {
            encodedUrl = URLEncoder.encode(jsonString, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedUrl;
    }
}
