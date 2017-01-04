package com.midtrans.sdk.uikit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.uikit.adapters.PaymentMethodsAdapter;
import com.midtrans.sdk.uikit.models.ItemViewDetails;
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
    // Payment Name
    protected static final String PAYMENT_TYPE_CIMB_CLICK = "cimb_click";
    protected static final String PAYMENT_TYPE_BCA_KLIKPAY = "bca_klikpay";
    protected static final String PAYMENT_TYPE_MANDIRI_CLICKPAY = "mandiri_clickpay";
    protected static final String PAYMENT_TYPE_MANDIRI_ECASH = "mandiri_ecash";
    protected static final String PAYMENT_TYPE_BANK_TRANSFER = "bank_transfer";
    protected static final String PAYMENT_TYPE_CREDIT_CARD = "cc";
    protected static final String PAYMENT_TYPE_BRI_EPAY = "bri_epay";
    protected static final String PAYMENT_TYPE_INDOSAT_DOMPETKU = "indosat_dompetku";
    protected static final String PAYMENT_TYPE_INDOMARET = "indomaret";
    protected static final String PAYMENT_TYPE_KLIK_BCA = "bca_klikbca";
    protected static final String PAYMENT_TYPE_TELKOMSEL_ECASH = "telkomsel_ecash";
    protected static final String PAYMENT_TYPE_XL_TUNAI = "xl_tunai";
    protected static final String PAYMENT_TYPE_KIOSON = "kiosan";
    protected static final String PAYMENT_TYPE_GCI = "gci";

    private static final String KEY_SELECT_PAYMENT = "Payment Select";
    private static final String KEY_CANCEL_TRANSACTION_EVENT = "Cancel Transaction";
    private static final String PAYMENT_SNAP = "snap";
    private static final String TAG = PaymentMethodsActivity.class.getSimpleName();
    private ArrayList<PaymentMethodsModel> data = new ArrayList<>();
    private boolean isCreditCardOnly = false;
    private boolean isBankTransferOnly = false;
    private boolean isBCAKlikpay = false;
    private boolean isKlikBCA = false;
    private boolean isMandiriClickPay = false;
    private boolean isMandiriECash = false;
    private boolean isCIMBClicks = false;
    private boolean isBRIEpay = false;
    private boolean isTelkomselCash = false;
    private boolean isIndosatDompetku = false;
    private boolean isXlTunai = false;
    private boolean isIndomaret = false;
    private boolean isKioson = false;
    private boolean isGci = false;

    private MidtransSDK midtransSDK = null;

    //Views
    private Toolbar toolbar = null;
    private RecyclerView paymentMethodsView = null;
    private RecyclerView itemDetailsView = null;
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
        isCreditCardOnly = getIntent().getBooleanExtra(UserDetailsActivity.CREDIT_CARD_ONLY, false);
        isBankTransferOnly = getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, false);
        isBCAKlikpay = getIntent().getBooleanExtra(UserDetailsActivity.BCA_KLIKPAY, false);
        isKlikBCA = getIntent().getBooleanExtra(UserDetailsActivity.KLIK_BCA, false);
        isMandiriClickPay = getIntent().getBooleanExtra(UserDetailsActivity.MANDIRI_CLICKPAY, false);
        isMandiriECash = getIntent().getBooleanExtra(UserDetailsActivity.MANDIRI_ECASH, false);
        isCIMBClicks = getIntent().getBooleanExtra(UserDetailsActivity.CIMB_CLICKS, false);
        isBRIEpay = getIntent().getBooleanExtra(UserDetailsActivity.BRI_EPAY, false);
        isTelkomselCash = getIntent().getBooleanExtra(UserDetailsActivity.TELKOMSEL_CASH, false);
        isIndosatDompetku = getIntent().getBooleanExtra(UserDetailsActivity.INDOSAT_DOMPETKU, false);
        isXlTunai = getIntent().getBooleanExtra(UserDetailsActivity.XL_TUNAI, false);
        isIndomaret = getIntent().getBooleanExtra(UserDetailsActivity.INDOMARET, false);
        isKioson = getIntent().getBooleanExtra(UserDetailsActivity.KIOSON, false);
        isGci = getIntent().getBooleanExtra(UserDetailsActivity.GIFT_CARD, false);

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
        paymentMethodsView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        paymentMethodsView.setAdapter(paymentMethodsAdapter);
    }

    /**
     * set data to view.
     */
    private void bindDataToView() {

        MidtransSDK midtransSDK = MidtransSDK.getInstance();

        if (midtransSDK != null) {
            List<ItemViewDetails> itemViewDetails = new ArrayList<>();
            // Add amount
            String amount = getString(R.string.prefix_money, Utils.getFormattedAmount(midtransSDK.getTransactionRequest().getAmount()));
            // Add header
            itemViewDetails.add(new ItemViewDetails(
                    null,
                    amount,
                    ItemViewDetails.TYPE_ITEM_HEADER,
                    midtransSDK.getTransactionRequest().getItemDetails().size() > 0));
            // Add item
            for (ItemDetails itemDetails : midtransSDK.getTransactionRequest().getItemDetails()) {
                String price = getString(R.string.prefix_money, Utils.getFormattedAmount(itemDetails.getPrice()));
                String itemName = itemDetails.getName();
                if (itemDetails.getQuantity() > 1) {
                    itemName = getString(R.string.text_item_name_format, itemDetails.getName(), itemDetails.getQuantity());
                }
                itemViewDetails.add(new ItemViewDetails(itemName, price, ItemViewDetails.TYPE_ITEM, true));
            }

            ItemDetailsAdapter adapter = new ItemDetailsAdapter(itemViewDetails);
            itemDetailsView.setLayoutManager(new LinearLayoutManager(this));
            itemDetailsView.setAdapter(adapter);
        }
    }

    /**
     * initialize views.
     */
    private void bindActivity() {
        paymentMethodsView = (RecyclerView) findViewById(R.id.rv_payment_methods);
        itemDetailsView = (RecyclerView) findViewById(R.id.rv_item_list);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        merchantName = (TextView) findViewById(R.id.merchant_name);
        progressContainer = (LinearLayout) findViewById(R.id.progress_container);
        if (isCreditCardOnly || isBankTransferOnly || isKlikBCA || isBCAKlikpay
                || isMandiriClickPay || isMandiriECash || isCIMBClicks || isBRIEpay
                || isTelkomselCash || isIndosatDompetku || isXlTunai
                || isIndomaret || isKioson || isGci) {
            TextView titleText = (TextView) findViewById(R.id.title_header);
            TextView loadingText = (TextView) findViewById(R.id.loading_text);
            titleText.setText(getString(R.string.txt_checkout));
            loadingText.setText(R.string.txt_checkout);
        }
    }

    private void getPaymentPages() {
        progressContainer.setVisibility(View.VISIBLE);
        enableButtonBack(false);
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        midtransSDK.checkout(userDetail.getUserId(), new CheckoutCallback() {
            @Override
            public void onSuccess(Token token) {
                Log.i(TAG, "checkout token:" + token.getTokenId());
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
                    String logoUrl = transaction.getMerchantData().getPreference().getLogoUrl();
                    String merchantName = transaction.getMerchantData().getPreference().getDisplayName();
                    midtransSDK.setCreditCard(transaction.getCreditCard());
                    midtransSDK.setMerchantLogo(logoUrl);
                    midtransSDK.setMerchantName(merchantName);
                    showLogo(logoUrl);
                    if (TextUtils.isEmpty(logoUrl)) {
                        showName(merchantName);
                    }
                    // Directly start credit card payment if using credit card mode only
                    initPaymentMethods(transaction.getEnabledPayments());
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

    private void initPaymentMethods(List<EnabledPayment> enabledPayments) {
        initialiseAdapterData(enabledPayments);
        if (MidtransSDK.getInstance().getTransactionRequest().isPaymentWithPromo()) {
            isCreditCardOnly = true;
        }
        if (isCreditCardOnly) {
            // track payment select credit card
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_CREDIT_CARD, null);
            Intent intent = new Intent(PaymentMethodsActivity.this, CreditDebitCardFlowActivity.class);
            startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isBankTransferOnly) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_BANK_TRANSFER, null);
            Intent startBankPayment = new Intent(PaymentMethodsActivity.this, SelectBankTransferActivity.class);
            if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, false)) {
                startBankPayment.putExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, true);
            } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, false)) {
                startBankPayment.putExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, true);
            } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_BCA, false)) {
                startBankPayment.putExtra(UserDetailsActivity.BANK_TRANSFER_BCA, true);
            } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, false)) {
                startBankPayment.putExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, true);
            }
            startBankPayment.putStringArrayListExtra(SelectBankTransferActivity.EXTRA_BANK, getBankTransfers());
            startActivityForResult(startBankPayment, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isBCAKlikpay) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_BCA_KLIKPAY, null);
            Intent startBCAKlikPayActivity = new Intent(this, BCAKlikPayActivity.class);
            startActivityForResult(startBCAKlikPayActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isKlikBCA) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_KLIK_BCA, null);
            Intent startKlikBcaActivity = new Intent(this, KlikBCAActivity.class);
            startKlikBcaActivity.putExtra(getString(R.string.position), Constants.PAYMENT_METHOD_KLIKBCA);
            startActivityForResult(startKlikBcaActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isMandiriClickPay) {
            // track payment select mandiri clickpay
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_MANDIRI_CLICKPAY, null);
            Intent startMandiriClickpay = new Intent(this, MandiriClickPayActivity.class);
            startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isMandiriECash) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_MANDIRI_ECASH, null);
            Intent startMandiriECash = new Intent(this, MandiriECashActivity.class);
            startActivityForResult(startMandiriECash, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isCIMBClicks) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_CIMB_CLICK, null);
            Intent startCIMBClickpay = new Intent(this, CIMBClickPayActivity.class);
            startActivityForResult(startCIMBClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isBRIEpay) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_BRI_EPAY, null);
            Intent startMandiriClickpay = new Intent(this, EpayBriActivity.class);
            startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isTelkomselCash) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_TELKOMSEL_ECASH, null);
            Intent telkomselCashActivity = new Intent(this, TelkomselCashActivity.class);
            startActivityForResult(telkomselCashActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isIndosatDompetku) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_INDOSAT_DOMPETKU, null);
            Intent startIndosatPaymentActivity = new Intent(this, IndosatDompetkuActivity.class);
            startActivityForResult(startIndosatPaymentActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isXlTunai) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_XL_TUNAI, null);
            Intent xlTunaiActivity = new Intent(this, XLTunaiActivity.class);
            startActivityForResult(xlTunaiActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isIndomaret) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_INDOMARET, null);
            Intent startIndomaret = new Intent(this, IndomaretActivity.class);
            startActivityForResult(startIndomaret, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isKioson) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_KIOSON, null);
            Intent kiosanActvity = new Intent(this, KiosonActivity.class);
            startActivityForResult(kiosanActvity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (isGci) {
            // track payment select Gift Card Indonesia
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_GCI, null);
            Intent gciActivity = new Intent(this, GCIActivity.class);
            startActivityForResult(gciActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else {
            if (data.isEmpty()) {
                showErrorAlertDialog(getString(R.string.message_payment_method_empty));
            } else if (data.size() == 1) {
                startPaymentMethod(data.get(0));
            } else {
                progressContainer.setVisibility(View.GONE);
                paymentMethodsAdapter.setData(data);
            }
        }
    }

    private void startPaymentMethod(PaymentMethodsModel paymentMethod) {
        String name = paymentMethod.getName();

        if (name.equalsIgnoreCase(getString(R.string.payment_method_credit_card))) {
            // track payment select credit card
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_CREDIT_CARD, null);
            Intent intent = new Intent(this, CreditDebitCardFlowActivity.class);
            startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bank_transfer))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_BANK_TRANSFER, null);
            Intent startBankPayment = new Intent(this, SelectBankTransferActivity.class);
            startBankPayment.putStringArrayListExtra(SelectBankTransferActivity.EXTRA_BANK, getBankTransfers());
            startActivityForResult(startBankPayment, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_mandiri_clickpay))) {
            // track payment select mandiri clickpay
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_MANDIRI_CLICKPAY, null);
            Intent startMandiriClickpay = new Intent(this, MandiriClickPayActivity.class);
            startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bri_epay))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_BRI_EPAY, null);
            Intent startMandiriClickpay = new Intent(this, EpayBriActivity.class);
            startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_cimb_clicks))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_CIMB_CLICK, null);
            Intent startCIMBClickpay = new Intent(this, CIMBClickPayActivity.class);
            startActivityForResult(startCIMBClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_mandiri_ecash))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_MANDIRI_ECASH, null);
            Intent startMandiriECash = new Intent(this, MandiriECashActivity.class);
            startActivityForResult(startMandiriECash, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_indosat_dompetku))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_INDOSAT_DOMPETKU, null);
            Intent startIndosatPaymentActivity = new Intent(this, IndosatDompetkuActivity.class);
            startActivityForResult(startIndosatPaymentActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_indomaret))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_INDOMARET, null);
            Intent startIndomaret = new Intent(this, IndomaretActivity.class);
            startActivityForResult(startIndomaret, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_offers))) {
            Intent startOffersActivity = new Intent(this, OffersActivity.class);
            startActivityForResult(startOffersActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bca_klikpay))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_BCA_KLIKPAY, null);
            Intent startBCAKlikPayActivity = new Intent(this, BCAKlikPayActivity.class);
            startActivityForResult(startBCAKlikPayActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_klik_bca))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_KLIK_BCA, null);
            Intent startKlikBcaActivity = new Intent(this, KlikBCAActivity.class);
            startKlikBcaActivity.putExtra(getString(R.string.position), Constants.PAYMENT_METHOD_KLIKBCA);
            startActivityForResult(startKlikBcaActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_telkomsel_cash))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_TELKOMSEL_ECASH, null);
            Intent telkomselCashActivity = new Intent(this, TelkomselCashActivity.class);
            startActivityForResult(telkomselCashActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_xl_tunai))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_XL_TUNAI, null);
            Intent xlTunaiActivity = new Intent(this, XLTunaiActivity.class);
            startActivityForResult(xlTunaiActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_kioson))) {
            // track payment select bank transfer
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_KIOSON, null);
            Intent kiosanActvity = new Intent(this, KiosonActivity.class);
            startActivityForResult(kiosanActvity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_gci))) {
            // track payment select Gift Card Indonesia
            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SELECT_PAYMENT, PAYMENT_TYPE_GCI, null);
            Intent gciActivity = new Intent(this, GCIActivity.class);
            startActivityForResult(gciActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else {
            Toast.makeText(this.getApplicationContext(),
                    "This feature is not implemented yet.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseAdapterData(List<EnabledPayment> enabledPayments) {
        data.clear();
        bankTrasfers.clear();

        for (EnabledPayment enabledPayment : enabledPayments) {
            if ((enabledPayment.getCategory() != null && enabledPayment.getCategory().equals(getString(R.string.enabled_payment_category_banktransfer)))
                    || enabledPayment.getType().equalsIgnoreCase(getString(R.string.payment_mandiri_bill_payment))) {
                bankTrasfers.add(enabledPayment.getType());
            } else {
                PaymentMethodsModel model = PaymentMethods.getMethods(this, enabledPayment.getType());
                if (model != null) {
                    data.add(model);
                }
            }
        }

        if (!bankTrasfers.isEmpty()) {
            data.add(PaymentMethods.getMethods(this, getString(R.string.payment_bank_transfer)));
        }
        SdkUtil.sortPaymentMethodsByPriority(data);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            SdkUIFlowUtil.hideKeyboard(this);
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * sends broadcast for transaction details.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(TAG, "in onActivity result : request code is " + requestCode + "," + resultCode);
        Logger.d(TAG, "in onActivity result : data:" + data);

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
                    midtransSDK.notifyTransactionFinished(new TransactionResult(null, null, TransactionResult.STATUS_INVALID));
                }
                finish();

            } else if (resultCode == RESULT_CANCELED) {
                if (data == null) {
                    if (this.data.size() == 1 || isCreditCardOnly || isBankTransferOnly || isBCAKlikpay || isKlikBCA
                            || isMandiriClickPay || isMandiriECash || isCIMBClicks || isBRIEpay
                            || isTelkomselCash || isIndosatDompetku || isXlTunai
                            || isIndomaret || isKioson || isGci) {
                        // track cancel transaction
                        midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_CANCEL_TRANSACTION_EVENT, PAYMENT_SNAP, null);

                        midtransSDK.notifyTransactionFinished(new TransactionResult(true));
                        finish();
                    }
                } else {
                    TransactionResponse response = (TransactionResponse) data.getSerializableExtra(getString(R.string.transaction_response));

                    if (response != null) {
                        if (response.getStatusCode().equals(getString(R.string.success_code_200))) {
                            midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_SUCCESS));
                        } else if (response.getStatusCode().equals(getString(R.string.success_code_201))) {
                            midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_PENDING));
                        } else {
                            midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_FAILED));
                        }
                        finish();
                    } else {
                        if (this.data.size() == 1 || isCreditCardOnly || isBankTransferOnly || isBCAKlikpay || isKlikBCA
                                || isMandiriClickPay || isMandiriECash || isCIMBClicks || isBRIEpay
                                || isTelkomselCash || isIndosatDompetku || isXlTunai
                                || isIndomaret || isKioson || isGci) {
                            // track cancel transaction
                            midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_CANCEL_TRANSACTION_EVENT, PAYMENT_SNAP, null);

                            midtransSDK.notifyTransactionFinished(new TransactionResult(true));
                            finish();
                        }
                    }
                }
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
        List<EnabledPayment> paymentMethods = PaymentMethods.getDefaultPaymentList(this);
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

    public ArrayList<String> getBankTransfers() {
        return bankTrasfers;
    }


    @Override
    public void onItemClick(int position) {
        if (paymentMethodsAdapter != null) {
            PaymentMethodsModel item = paymentMethodsAdapter.getItem(position);
            startPaymentMethod(item);
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
        // track cancel transaction
        midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_CANCEL_TRANSACTION_EVENT, PAYMENT_SNAP, null);

        midtransSDK.notifyTransactionFinished(new TransactionResult(true));
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