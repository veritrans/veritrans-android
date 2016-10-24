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
import id.co.veritrans.sdk.coreflow.models.CstoreEntity;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.utilities.Utils;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.fragments.BankTransferFragment;
import id.co.veritrans.sdk.uiflow.fragments.IndomaretPaymentFragment;
import id.co.veritrans.sdk.uiflow.fragments.IndomaretPaymentStatusFragment;
import id.co.veritrans.sdk.uiflow.fragments.InstructionIndomaretFragment;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;

/**
 * Created by Ankit on 12/01/15.
 */
public class IndomaretActivity extends BaseActivity implements View.OnClickListener, TransactionBusCallback {

    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private TextView textViewOrderId = null;
    private TextView textViewAmount = null;
    private Button buttonConfirmPayment = null;
    private AppBarLayout appBarLayout = null;
    private TextView textViewTitle = null;
    private ImageView logo = null;

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
            position = data.getIntExtra(getString(R.string.position), Constants
                    .PAYMENT_METHOD_INDOMARET);
        } else {
            SdkUIFlowUtil.showSnackbar(IndomaretActivity.this, getString(R.string.error_something_wrong));
            finish();
        }

        initializeView();
        bindDataToView();
        setUpHomeFragment();
        if (!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
    }


    /**
     * set up {@link BankTransferFragment} to display payment instructions.
     */
    private void setUpHomeFragment() {
        // setup home fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        instructionIndomaretFragment = new InstructionIndomaretFragment();

        fragmentTransaction.add(R.id.indomaret_container, instructionIndomaretFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (currentFragment.equals(STATUS_FRAGMENT) || currentFragment.equals(PAYMENT_FRAGMENT)) {
                RESULT_CODE = RESULT_OK;
                setResultAndFinish();
            } else {
                onBackPressed();
            }
        }

        return false;
    }

    private void initializeView() {
        textViewOrderId = (TextView) findViewById(R.id.text_order_id);
        textViewAmount = (TextView) findViewById(R.id.text_amount);
        textViewTitle = (TextView) findViewById(R.id.text_title);
        buttonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        initializeTheme();
        //setup tool bar
        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindDataToView() {
        if (veritransSDK != null) {
            textViewAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(veritransSDK.getTransactionRequest().getAmount())));
            textViewOrderId.setText("" + veritransSDK.getTransactionRequest().getOrderId());
            if (veritransSDK.getSemiBoldText() != null) {
                buttonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), veritransSDK.getSemiBoldText()));
            }
            buttonConfirmPayment.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {
            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {

                performTransaction();

            } else if (currentFragment.equalsIgnoreCase(PAYMENT_FRAGMENT)) {

                //appBarLayout.setExpanded(true);

                if (transactionResponse != null) {
                    setUpTransactionStatusFragment(transactionResponse);
                } else {
                    RESULT_CODE = RESULT_OK;
                    SdkUIFlowUtil.showSnackbar(IndomaretActivity.this, SOMETHING_WENT_WRONG);
                    setResultAndFinish();
                }
            } else {
                RESULT_CODE = RESULT_OK;
                setResultAndFinish();
            }
        }
    }

    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = STATUS_FRAGMENT;
        buttonConfirmPayment.setText(R.string.done);

        //appBarLayout.setExpanded(false, false);

        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationIcon(closeIcon);
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
            SdkUIFlowUtil.showSnackbar(IndomaretActivity.this, SOMETHING_WENT_WRONG);
            onBackPressed();
        }
    }

    private void performTransaction() {

        SdkUIFlowUtil.showProgressDialog(IndomaretActivity.this, getString(R.string.processing_payment),
                false);

        CstoreEntity cstoreEntity = new CstoreEntity();
        cstoreEntity.setMessage("demo_message");
        cstoreEntity.setStore("indomaret");

        //Execute transaction
        veritransSDK.paymentUsingIndomaret(cstoreEntity);
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
        data.putExtra(getString(R.string.transaction_response), transactionResponse);
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent event) {
        SdkUIFlowUtil.hideProgressDialog();

        if (event.getResponse() != null) {
            IndomaretActivity.this.transactionResponse = event.getResponse();
            //appBarLayout.setExpanded(true);
            setUpTransactionFragment(event.getResponse());
        } else {
            onBackPressed();
        }
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        try {
            IndomaretActivity.this.errorMessage = event.getMessage();
            IndomaretActivity.this.transactionResponse = event.getResponse();

            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(IndomaretActivity.this, "" + event.getMessage());
        } catch (NullPointerException ex) {
            Logger.e("transaction error is " + event.getMessage());
        }
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        try {
            IndomaretActivity.this.errorMessage = getString(R.string.no_network_msg);

            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(IndomaretActivity.this, "" + getString(R.string.no_network_msg));
        } catch (NullPointerException ex) {
            Logger.e("transaction error is " + getString(R.string.no_network_msg));
        }
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        try {
            IndomaretActivity.this.errorMessage = event.getMessage();

            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(IndomaretActivity.this, "" + event.getMessage());
        } catch (NullPointerException ex) {
            Logger.e("transaction error is " + event.getMessage());
        }
    }
}