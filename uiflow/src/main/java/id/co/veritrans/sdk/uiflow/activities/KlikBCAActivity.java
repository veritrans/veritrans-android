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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.Events;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.KlikBCADescriptionModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.utilities.Utils;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.fragments.BankTransactionStatusFragment;
import id.co.veritrans.sdk.uiflow.fragments.KlikBCAFragment;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;

/**
 * @author rakawm
 */
public class KlikBCAActivity extends BaseActivity implements TransactionBusCallback {

    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = PAYMENT_FRAGMENT;
    private TransactionResponse transactionResponse;
    private String errorMessage;
    private TextView mTextViewOrderId;
    private TextView mTextViewAmount;
    private Button mButtonConfirmPayment;
    private AppBarLayout mAppBarLayout;
    private ImageView logo;
    private TextView mTextViewTitle;
    private LinearLayout containerCollapsing;

    private KlikBCAFragment klikBCAFragment;

    private VeritransSDK mVeritransSDK;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Subscribe to Event Bus
        if (!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
        setContentView(R.layout.activity_klik_bca);

        // Get Veritrans SDK instance
        mVeritransSDK = VeritransSDK.getVeritransSDK();

        // Initialize views
        mTextViewOrderId = (TextView) findViewById(R.id.text_order_id);
        mTextViewAmount = (TextView) findViewById(R.id.text_amount);
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        containerCollapsing = (LinearLayout) findViewById(R.id.container_collapsing);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        initializeTheme();
        // Setup toolbar
        mToolbar.setTitle(""); // disable default Text
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindData();
    }

    private void bindData() {
        // Set title
        mTextViewTitle.setText(R.string.klik_bca);
        // Set transaction details
        mTextViewAmount.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(mVeritransSDK.getTransactionRequest().getAmount())));
        mTextViewOrderId.setText(mVeritransSDK.getTransactionRequest().getOrderId());


        // Set custom font if available
        if (mVeritransSDK.getSemiBoldText() != null) {
            mButtonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mVeritransSDK.getSemiBoldText()));
        }

        // Initialize fragment
        klikBCAFragment = new KlikBCAFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.bank_transfer_container, klikBCAFragment).commit();

        mButtonConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment.equals(STATUS_FRAGMENT)) {
                    Intent data = new Intent();
                    if (transactionResponse != null) {
                        data.putExtra(getString(R.string.transaction_response), transactionResponse);
                    }
                    if (errorMessage != null && !errorMessage.equals("")) {
                        data.putExtra(getString(R.string.error_transaction), errorMessage);
                    }
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    // Check klik BCA user ID
                    if (klikBCAFragment.checkUserId()) {
                        // Do the payment
                        SdkUIFlowUtil.showProgressDialog(KlikBCAActivity.this, getString(R.string.processing_payment), false);
                        KlikBCADescriptionModel model = new KlikBCADescriptionModel("Any description", klikBCAFragment.getUserId());
                        mVeritransSDK.paymentUsingKlikBCA(model);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unsubscribe to Event Bus
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
    }

    /**
     * Displays status of transaction from {@link TransactionResponse} object.
     *
     * @param transactionResponse response of the transaction call
     */
    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = STATUS_FRAGMENT;
        mButtonConfirmPayment.setText(R.string.done);

        mAppBarLayout.setExpanded(false, false);
        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        mToolbar.setNavigationIcon(closeIcon);
        setSupportActionBar(mToolbar);

        BankTransactionStatusFragment bankTransactionStatusFragment =
                BankTransactionStatusFragment.newInstance(transactionResponse, Constants.PAYMENT_METHOD_KLIKBCA);

        // setup transaction status fragment
        fragmentTransaction.replace(R.id.bank_transfer_container,
                bankTransactionStatusFragment, STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();

        mButtonConfirmPayment.setText(R.string.complete_payment_at_klik_bca);
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent event) {
        if (event.getSource().equals(Events.PAYMENT)) {
            SdkUIFlowUtil.hideProgressDialog();
            this.transactionResponse = event.getResponse();
            this.errorMessage = event.getResponse().getStatusMessage();
            setUpTransactionStatusFragment(event.getResponse());
        }
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        if (event.getSource().equals(Events.PAYMENT)) {
            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(this, event.getMessage());
        }
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        if (event.getSource().equals(Events.PAYMENT)) {
            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(this, getString(R.string.error_unable_to_connect));
        }
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        if (event.getSource().equals(Events.PAYMENT)) {
            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(this, event.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (currentFragment.equals(STATUS_FRAGMENT)) {
                Intent data = new Intent();
                if (transactionResponse != null) {
                    data.putExtra(getString(R.string.transaction_response), transactionResponse);
                }
                if (errorMessage != null && !errorMessage.equals("")) {
                    data.putExtra(getString(R.string.error_transaction), errorMessage);
                }
                setResult(RESULT_OK, data);
                finish();
            } else {
                onBackPressed();
            }
        }

        return false;
    }
}
