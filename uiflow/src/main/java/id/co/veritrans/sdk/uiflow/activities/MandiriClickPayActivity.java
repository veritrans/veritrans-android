package id.co.veritrans.sdk.uiflow.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.fragments.BankTransactionStatusFragment;
import id.co.veritrans.sdk.uiflow.fragments.MandiriClickPayFragment;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;

/**
 * Created by shivam on 11/3/15.
 */
public class MandiriClickPayActivity extends BaseActivity implements View.OnClickListener, TransactionBusCallback {

    public static final String DENY = "202";
    public static final String HOME_FRAGMENT = "home";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";
    private MandiriClickPayFragment mMandiriClickPayFragment = null;
    private Button mButtonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private TextView mTextViewAppli = null;
    private TextView mTextViewInput1 = null;
    private TextView mTextViewInput2 = null;
    private TextView mTextViewInput3 = null;
    private AppBarLayout appBar = null;
    private ImageView logo = null;
    private CollapsingToolbarLayout mCollapsingToolbarLayout = null;
    private VeritransSDK mVeritransSDK = null;
    // for result
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private int RESULT_CODE = RESULT_CANCELED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mandiri_clickpay);

        mVeritransSDK = VeritransSDK.getVeritransSDK();

        if (mVeritransSDK == null) {
            SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }


        initializeViews();
        bindDataToView();
        setUpHomeFragment();
        if (!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
        super.onDestroy();
    }


    private void initializeViews() {

        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        mTextViewInput1 = (TextView) findViewById(R.id.text_input_1);
        mTextViewInput2 = (TextView) findViewById(R.id.text_input_2);
        mTextViewInput3 = (TextView) findViewById(R.id.text_input_3);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        appBar = (AppBarLayout) findViewById(R.id.main_appbar);
        initializeTheme();

        if (mVeritransSDK != null) {
            if (mVeritransSDK.getSemiBoldText() != null) {
                mButtonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mVeritransSDK.getSemiBoldText()));
            }
        }

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mButtonConfirmPayment.setOnClickListener(this);
    }

    private void bindDataToView() {
        mTextViewInput1.setText("");
        mTextViewInput2.setText("" + mVeritransSDK.getTransactionRequest().getAmount());
        mTextViewInput3.setText("" + SdkUIFlowUtil.generateRandomNumber());
    }

    private void setUpHomeFragment() {

        // setup  fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mMandiriClickPayFragment = new MandiriClickPayFragment();

        fragmentTransaction.add(R.id.mandiri_clickpay_container,
                mMandiriClickPayFragment);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (currentFragment.equals(STATUS_FRAGMENT)) {
                if (mButtonConfirmPayment.getText().toString().equalsIgnoreCase(getString(R.string.retry))) {
                    //finish();
                    Logger.i("on retry pressed");
                    setResultAndFinish();
                } else {
                    RESULT_CODE = RESULT_OK;
                    setResultAndFinish();
                }
            } else {
                onBackPressed();
            }
        }
        return true;
    }

    public void onChangeOfDebitCardNumer(CharSequence text) {

        if (text != null && text.length() > 0) {
            String cardNumber = text.toString().trim().replace(" ", "");

            if (cardNumber.length() > 10) {
                mTextViewInput1.setText(cardNumber.substring(cardNumber.length() - 10,
                        cardNumber.length()));
            } else {
                mTextViewInput1.setText(cardNumber);
            }
        } else {
            mTextViewInput1.setText("");
        }
    }

    /**
     * Handles the click of confirm payment button based on following 2 conditions.
     * <p/>
     * 1) if current fragment is home fragment then it will start payment execution.
     * 2) if current fragment is status fragment  then it will send result back to {@link
     * PaymentMethodsActivity}.
     *
     * @param view  clicked view
     */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {

            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {

                validateInformation();

            } else {
                if(mButtonConfirmPayment.getText().toString().equalsIgnoreCase(getString(R.string.retry))) {
                    //finish();
                    Logger.i("on retry pressed");
                    setResultAndFinish();
                }else {
                    RESULT_CODE = RESULT_OK;
                    setResultAndFinish();
                }
            }
        }
    }


    private void validateInformation() {

        if (mMandiriClickPayFragment != null && !mMandiriClickPayFragment.isDetached()) {

            String challengeToken = mMandiriClickPayFragment.getChallengeToken();
            String debitCardNumber = mMandiriClickPayFragment.getDebitCardNumber();

            if (!TextUtils.isEmpty(challengeToken) && !TextUtils.isEmpty(debitCardNumber)) {

                debitCardNumber = debitCardNumber.replace(" ", "");

                if (debitCardNumber.length() < 16 || !SdkUIFlowUtil.isValidCardNumber(debitCardNumber)) {
                    SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_card_no));
                } else if (challengeToken.trim().length() != 6) {
                    SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_token_no));
                } else {

                    MandiriClickPayModel mandiriClickPayModel = new MandiriClickPayModel();
                    mandiriClickPayModel.setCardNumber(debitCardNumber);
                    mandiriClickPayModel.setInput1(mTextViewInput1.getText().toString());
                    mandiriClickPayModel.setInput2(mTextViewInput2.getText().toString());
                    mandiriClickPayModel.setInput3(mTextViewInput3.getText().toString());
                    mandiriClickPayModel.setToken(challengeToken);

                    SdkUIFlowUtil.showProgressDialog(MandiriClickPayActivity.this, getString(R.string.processing_payment), false);
                    makeTransaction(mandiriClickPayModel);

                }

            } else {
                SdkUIFlowUtil.showSnackbar(
                        MandiriClickPayActivity.this,
                        "Please fill up information properly."
                );
            }

        } else {
            SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this, getString(R.string.error_something_wrong));
            finish();
        }

    }

    /**
     * execute payment code and on success set status fragment to show payment information.
     * and in onFailure displays error message.
     *
     * @param mandiriClickPayModel  Mandiri click pay request object
     */
    private void makeTransaction(MandiriClickPayModel mandiriClickPayModel) {
        mVeritransSDK.snapPaymentUsingMandiriClickPay(mVeritransSDK.readAuthenticationToken(), mandiriClickPayModel.getCardNumber(), mandiriClickPayModel.getToken(), mandiriClickPayModel.getInput3());
    }


    /**
     * Displays status of transaction from {@link TransactionResponse} object.
     *
     * @param transactionResponse   response of transaction call
     */
    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = STATUS_FRAGMENT;
        mButtonConfirmPayment.setText(R.string.done);

        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        mToolbar.setNavigationIcon(closeIcon);
        appBar.setExpanded(false, false);
        setSupportActionBar(mToolbar);

        BankTransactionStatusFragment bankTransactionStatusFragment =
                BankTransactionStatusFragment.newInstance(transactionResponse,
                        Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY);

        // setup transaction status fragment
        fragmentTransaction.replace(R.id.mandiri_clickpay_container,
                bankTransactionStatusFragment, STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();
    }


    /**
     * send result back to  {@link PaymentMethodsActivity} and finish current activity.
     */
    private void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(getString(R.string.transaction_response), transactionResponse);
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_OK, data);
        finish();
    }


    /**
     * in case of transaction failure it will change the text of confirm payment button to 'RETRY'
     */
    public void activateRetry() {

        if (mButtonConfirmPayment != null) {
            mButtonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent event) {
        SdkUIFlowUtil.hideProgressDialog();

        MandiriClickPayActivity.this.transactionResponse = event.getResponse();

        if (transactionResponse != null) {
            setUpTransactionStatusFragment(transactionResponse);
        } else {
            SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this,
                    SOMETHING_WENT_WRONG);
        }
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        MandiriClickPayActivity.this.transactionResponse = event.getResponse();
        MandiriClickPayActivity.this.errorMessage = event.getMessage();


        Logger.e("Error is ", "" + errorMessage);

        if (transactionResponse != null
                && transactionResponse.getStatusCode().contains(DENY)) {
            setUpTransactionStatusFragment(transactionResponse);
        }


        SdkUIFlowUtil.hideProgressDialog();
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        MandiriClickPayActivity.this.errorMessage = getString(R.string.no_network_msg);


        Logger.e("Error is ", "" + errorMessage);
        SdkUIFlowUtil.hideProgressDialog();
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        MandiriClickPayActivity.this.errorMessage = event.getMessage();


        Logger.e("Error is ", "" + errorMessage);
        SdkUIFlowUtil.hideProgressDialog();
    }
}