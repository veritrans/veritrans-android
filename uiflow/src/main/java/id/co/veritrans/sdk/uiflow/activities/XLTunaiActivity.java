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

import id.co.veritrans.sdk.coreflow.callback.TransactionCallback;
import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.utilities.Utils;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.fragments.InstructionXLTunaiFragment;
import id.co.veritrans.sdk.uiflow.fragments.XLTunaiPaymentFragment;
import id.co.veritrans.sdk.uiflow.fragments.XLTunaiPaymentStatusFragment;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;

/**
 * @author rakawm
 */
public class XLTunaiActivity extends BaseActivity implements View.OnClickListener{
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

    private InstructionXLTunaiFragment instructionXLTunaiFragment = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private int position = Constants.PAYMENT_METHOD_XL_TUNAI;

    private int RESULT_CODE = RESULT_CANCELED;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xl_tunai);

        veritransSDK = VeritransSDK.getInstance();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(getString(R.string.position), Constants.PAYMENT_METHOD_XL_TUNAI);
        } else {
            SdkUIFlowUtil.showSnackbar(XLTunaiActivity.this, getString(R.string.error_something_wrong));
            finish();
        }

        initializeView();
        bindDataToView();
        setUpHomeFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setUpHomeFragment() {
        // setup home fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        instructionXLTunaiFragment = new InstructionXLTunaiFragment();

        fragmentTransaction.add(R.id.instruction_container, instructionXLTunaiFragment, HOME_FRAGMENT);
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

        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindDataToView() {
        textViewTitle.setText(getString(R.string.xl_tunai));
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

                appBarLayout.setExpanded(true);

                if (transactionResponse != null) {
                    setUpTransactionStatusFragment(transactionResponse);
                } else {
                    RESULT_CODE = RESULT_OK;
                    SdkUIFlowUtil.showSnackbar(XLTunaiActivity.this, SOMETHING_WENT_WRONG);
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

        appBarLayout.setExpanded(false, false);

        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationIcon(closeIcon);
        setSupportActionBar(toolbar);

        XLTunaiPaymentStatusFragment indomaretPaymentStatusFragment =
                XLTunaiPaymentStatusFragment.newInstance(transactionResponse, false);

        // setup transaction status fragment
        fragmentTransaction.replace(R.id.instruction_container,
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
            XLTunaiPaymentFragment xlTunaiPaymentFragment =
                    XLTunaiPaymentFragment.newInstance(transactionResponse);
            fragmentTransaction.replace(R.id.instruction_container,
                    xlTunaiPaymentFragment, PAYMENT_FRAGMENT);
            fragmentTransaction.addToBackStack(PAYMENT_FRAGMENT);
            fragmentTransaction.commit();
            currentFragment = PAYMENT_FRAGMENT;
        } else {
            SdkUIFlowUtil.showSnackbar(XLTunaiActivity.this, getString(R.string.error_something_wrong));
            onBackPressed();
        }
    }

    private void performTransaction() {
        SdkUIFlowUtil.showProgressDialog(XLTunaiActivity.this, getString(R.string.processing_payment), false);
        //Execute transaction
        veritransSDK.snapPaymentUsingXLTunai(veritransSDK.readAuthenticationToken(), new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                SdkUIFlowUtil.hideProgressDialog();

                if (response != null) {
                    transactionResponse = response;
                    appBarLayout.setExpanded(true);
                    setUpTransactionFragment(response);
                } else {
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                SdkUIFlowUtil.hideProgressDialog();
                errorMessage = reason;
                transactionResponse = response;
                SdkUIFlowUtil.showSnackbar(XLTunaiActivity.this, "" + reason);
            }

            @Override
            public void onError(Throwable error) {
                SdkUIFlowUtil.hideProgressDialog();
                errorMessage = error.getMessage();
                SdkUIFlowUtil.showSnackbar(XLTunaiActivity.this, "" + error.getMessage());
            }
        });
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
        data.putExtra(getString(R.string.transaction_response), transactionResponse);
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }
}
