package com.midtrans.sdk.uikit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkUtil;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.adapters.PaymentMethodsAdapter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays list of available payment methods.
 * <p/>
 * Created by shivam on 10/16/15.
 */
public class PaymentMethodsActivity extends BaseActivity implements PaymentMethodsAdapter.PaymentMethodListener {

    public static final String PAYABLE_AMOUNT = "Payable Amount";
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.3f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.7f;
    private static final float ALPHA = 0.6f;
    private static final String TAG = PaymentMethodsActivity.class.getSimpleName();
    private static final float PERCENTAGE_TOTAL = 1f;
    private ArrayList<PaymentMethodsModel> data = new ArrayList<>();

    private MidtransSDK midtransSDK = null;
    private boolean isHideToolbarView = false;

    //Views
    private Toolbar toolbar = null;
    private AppBarLayout mAppBarLayout = null;
    private RecyclerView mRecyclerView = null;
    private TextView headerTextView = null;
    private TextView textViewMeasureHeight = null;
    private TextView amountText = null;
    private TextView merchantName = null;
    private LinearLayout progressContainer = null;
    private ImageView logo = null;
    private ArrayList<String> bankTrasfers = new ArrayList<>();
    private PaymentMethodsAdapter paymentMethodsAdapter;
    private AlertDialog alertDialog;
    private boolean backButtonEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_method);
        midtransSDK = MidtransSDK.getInstance();
        initializeTheme();
        UserDetail userDetail = null;
        try {
            userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        TransactionRequest transactionRequest = null;
        if (midtransSDK != null) {
            transactionRequest = midtransSDK.getTransactionRequest();
            if (transactionRequest != null) {
                CustomerDetails customerDetails = null;
                if (userDetail != null) {
                    customerDetails = new CustomerDetails(userDetail.getUserFullName(), null,
                            userDetail.getEmail(), userDetail.getPhoneNumber());
                    transactionRequest.setCustomerDetails(customerDetails);
                    Logger.d(String.format("Customer name: %s, Customer email: %s, Customer phone: %s", userDetail.getUserFullName(), userDetail.getEmail(), userDetail.getPhoneNumber()));
                }
                setUpPaymentMethods();
                setupRecyclerView();

            } else {
                showErrorAlertDialog(getString(R.string.error_transaction_empty));
            }
        } else {
            Logger.e("Veritrans SDK is not started.");
            finish();
        }
    }

    /**
     * if recycler view fits within screen then it will disable the scrolling of it.
     */
    private void handleScrollingOfRecyclerView() {
        headerTextView.setAlpha(1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView
                        .getLayoutManager();

                int hiddenTextViewHeight = textViewMeasureHeight.getHeight();
                int recyclerViewHieght = mRecyclerView.getMeasuredHeight();
                int appBarHeight = mAppBarLayout.getHeight();
                int exactHeightForCompare = hiddenTextViewHeight - appBarHeight;

                int oneRowHeight = dp2px(60);
                int totalHeight = (mRecyclerView.getAdapter().getItemCount() - 1) * oneRowHeight;

                if (totalHeight < exactHeightForCompare) {
                    disableScrolling();
                    headerTextView.setAlpha(1);
                }
            }
        }, 200);
    }

    /**
     * Disable scrolling of recycler view.
     */
    private void disableScrolling() {
        //turn off scrolling
        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams)
                mAppBarLayout.getLayoutParams();
        appBarLayoutParams.setBehavior(null);
        mAppBarLayout.setLayoutParams(appBarLayoutParams);
    }

    /**
     * bind views , initializes adapter and set it to recycler view.
     */
    private void setUpPaymentMethods() {

        //initialize views
        bindActivity();

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        bindDataToView();
        getPaymentPages();
    }

    private void setupRecyclerView() {
        paymentMethodsAdapter = new PaymentMethodsAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(paymentMethodsAdapter);
        // disable scrolling of recycler view if there is no need of it.
        handleScrollingOfRecyclerView();
    }

    /**
     * set data to view.
     */
    private void bindDataToView() {

        MidtransSDK midtransSDK = MidtransSDK.getInstance();

        if (midtransSDK != null) {
            String amount = getString(R.string.prefix_money, Utils.getFormattedAmount(midtransSDK.getTransactionRequest().getAmount()));

            amountText.setText(amount);
        }

    }

    /**
     * initialize views.
     */
    private void bindActivity() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_payment_methods);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        headerTextView = (TextView) findViewById(R.id.title_header);
        amountText = (TextView) findViewById(R.id.header_view_sub_title);
        textViewMeasureHeight = (TextView) findViewById(R.id.textview_to_compare);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        merchantName = (TextView) findViewById(R.id.merchant_name);
        progressContainer = (LinearLayout) findViewById(R.id.progress_container);
    }

    private void getPaymentPages() {
        progressContainer.setVisibility(View.VISIBLE);
        enableButtonBack(false);
        midtransSDK.checkout(new CheckoutCallback() {
            @Override
            public void onSuccess(Token token) {
                LocalDataHandler.saveString(Constants.AUTH_TOKEN, token.getTokenId());
                getPaymentOptions(token.getTokenId());
            }

            @Override
            public void onFailure(Token token, String reason) {
                enableButtonBack(true);
                showErrorMessage();
            }

            @Override
            public void onError(Throwable error) {
                enableButtonBack(true);
                showErrorMessage();
            }
        });
    }

    private void enableButtonBack(boolean enable) {
        backButtonEnabled = enable;
    }

    private void getPaymentOptions(String tokenId) {
        midtransSDK.getTransactionOptions(tokenId, new TransactionOptionsCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                enableButtonBack(true);
                showBackButton();
                try {
                    progressContainer.setVisibility(View.GONE);
                    String logoUrl = transaction.getMerchantData().getLogoUrl();
                    String merchantName = transaction.getMerchantData().getDisplayName();
                    midtransSDK.setMerchantLogo(logoUrl);
                    midtransSDK.setMerchantName(merchantName);
                    showLogo(logoUrl);
                    if (TextUtils.isEmpty(logoUrl)) {
                        showName(merchantName);
                    }

                    List<String> paymentMethods = transaction.getTransactionData().getEnabledPayments();
                    initialiseAdapterData(paymentMethods);
                    paymentMethodsAdapter.setData(data);
                } catch (NullPointerException e) {
                    Logger.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Transaction transaction, String reason) {
                enableButtonBack(true);
                progressContainer.setVisibility(View.GONE);
                showDefaultPaymentMethods();
            }

            @Override
            public void onError(Throwable error) {
                enableButtonBack(true);
                progressContainer.setVisibility(View.GONE);
                showDefaultPaymentMethods();
            }
        });
    }

    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseAdapterData(List<String> enabledPayments) {
        data.clear();
        bankTrasfers.clear();
        for (String paymentType : enabledPayments) {
            PaymentMethodsModel model = PaymentMethods.getMethods(this, paymentType);
            if (model != null) {
                data.add(model);
            }
            if(paymentType.equals(getString(R.string.payment_permata_va)) ||
                    paymentType.equals(getString(R.string.payment_bca_va)) ||
                    paymentType.equals(getString(R.string.payment_mandiri_bill_payment)) ||
                    paymentType.equals(getString(R.string.payment_all_va))){
                bankTrasfers.add(paymentType);
            }
        }
        if(!bankTrasfers.isEmpty()){
            data.add(PaymentMethods.getMethods(this,getString(R.string.payment_bank_transfer)));
        }
        SdkUtil.sortPaymentMethodsByPriority(data);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            SdkUIFlowUtil.hideKeyboard(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * sends broadcast for transaction details.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Logger.d(TAG, "in onActivity result : request code is " + requestCode + "," + resultCode);

        if (requestCode == Constants.RESULT_CODE_PAYMENT_TRANSFER) {
            Logger.d(TAG, "sending result back with code " + requestCode);

            if (resultCode == RESULT_OK) {
                TransactionResponse response = (TransactionResponse) data.getSerializableExtra(getString(R.string.transaction_response));
                if (response != null) {
                    if (response.getStatusCode().equals(getString(R.string.success_code_200))) {
                        midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_SUCCESS));
                    } else if (response.getStatusCode().equals(getString(R.string.success_code_201))) {
                        midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_PENDING));
                    } else {
                        midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_FAILED));
                    }
                } else {
                    midtransSDK.notifyTransactionFinished(new TransactionResult());
                }
                finish();
            }

        } else {
            Logger.d(TAG, "failed to send result back " + requestCode);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void showLogo(String url) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(this)
                    .load(url)
                    .into(logo);
            merchantName.setVisibility(View.GONE);
        } else {
            logo.setVisibility(View.INVISIBLE);
        }
    }

    private void showName(String name) {
        if (!TextUtils.isEmpty(name)) {
            merchantName.setVisibility(View.VISIBLE);
            merchantName.setText(name);
        }
    }

    private void showDefaultPaymentMethods() {
        progressContainer.setVisibility(View.GONE);
        List<String> paymentMethods = PaymentMethods.getDefaultPaymentList(this);
        initialiseAdapterData(paymentMethods);
        paymentMethodsAdapter.setData(data);
    }

    private void showErrorMessage() {
        if (!isFinishing()) {
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.txt_error_snap_token))
                    .setPositiveButton(R.string.btn_retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getPaymentPages();
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }

    private void showErrorAlertDialog(String message) {
        if (!isFinishing()) {
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }

    public ArrayList<String> getBankTrasfers() {
        return bankTrasfers;
    }


    @Override
    public void onItemClick(int position) {
        if (paymentMethodsAdapter != null) {
            PaymentMethodsModel item = paymentMethodsAdapter.getItem(position);
            String name = item.getName();

            if (name.equalsIgnoreCase(getString(R.string.payment_method_credit_card))) {

                Intent intent = new Intent(this, CreditDebitCardFlowActivity.class);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bank_transfer))) {

                Intent startBankPayment = new Intent(this, SelectBankTransferActivity.class);
                startBankPayment.putStringArrayListExtra(SelectBankTransferActivity.EXTRA_BANK, getBankTrasfers());
                startActivityForResult(startBankPayment, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_mandiri_clickpay))) {

                Intent startMandiriClickpay = new Intent(this, MandiriClickPayActivity.class);
                startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bri_epay))) {

                Intent startMandiriClickpay = new Intent(this, EpayBriActivity.class);
                startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_cimb_clicks))) {

                Intent startCIMBClickpay = new Intent(this, CIMBClickPayActivity.class);
                startActivityForResult(startCIMBClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_mandiri_ecash))) {
                Intent startMandiriECash = new Intent(this, MandiriECashActivity.class);
                startActivityForResult(startMandiriECash, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_indosat_dompetku))) {

                Intent startIndosatPaymentActivity = new Intent(this, IndosatDompetkuActivity.class);
                startActivityForResult(startIndosatPaymentActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_indomaret))) {

                Intent startIndomaret = new Intent(this, IndomaretActivity.class);
                startActivityForResult(startIndomaret, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bbm_money))) {

                Intent startBBMMoney = new Intent(this, BBMMoneyActivity.class);
                startActivityForResult(startBBMMoney, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_offers))) {

                Intent startOffersActivity = new Intent(this, OffersActivity.class);
                startActivityForResult(startOffersActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bca_klikpay))) {

                Intent startBCAKlikPayActivity = new Intent(this, BCAKlikPayActivity.class);
                startActivityForResult(startBCAKlikPayActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_klik_bca))) {

                Intent startKlikBcaActivity = new Intent(this, KlikBCAActivity.class);
                startKlikBcaActivity.putExtra(getString(R.string.position), Constants.PAYMENT_METHOD_KLIKBCA);
                startActivityForResult(startKlikBcaActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_telkomsel_cash))) {

                Intent telkomselCashActivity = new Intent(this, TelkomselCashActivity.class);
                startActivityForResult(telkomselCashActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_xl_tunai))) {

                Intent xlTunaiActivity = new Intent(this, XLTunaiActivity.class);
                startActivityForResult(xlTunaiActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else if (name.equalsIgnoreCase(getString(R.string.payment_method_kioson))) {

                Intent kiosanActvity = new Intent(this, KiosonActivity.class);
                startActivityForResult(kiosanActvity, Constants.RESULT_CODE_PAYMENT_TRANSFER);

            } else {

                Toast.makeText(this.getApplicationContext(),
                        "This feature is not implemented yet.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (alertDialog != null) {
            alertDialog.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        if (!backButtonEnabled) {
            return;
        }
        super.onBackPressed();
    }

    private void showBackButton() {
        if (backButtonEnabled && getSupportActionBar() != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }, 50);
        }
    }
}