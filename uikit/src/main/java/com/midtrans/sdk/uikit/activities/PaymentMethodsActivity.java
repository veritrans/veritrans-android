package com.midtrans.sdk.uikit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkUtil;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.ColorTheme;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
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
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.models.ItemViewDetails;
import com.midtrans.sdk.uikit.models.MessageInfo;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.banktransfer.list.BankTransferListActivity;
import com.midtrans.sdk.uikit.views.bca_klikbca.payment.KlikBcaPaymentActivity;
import com.midtrans.sdk.uikit.views.bca_klikpay.BcaKlikPayPaymentActivity;
import com.midtrans.sdk.uikit.views.creditcard.saved.SavedCreditCardActivity;
import com.midtrans.sdk.uikit.views.danamon_online.DanamonOnlineActivity;
import com.midtrans.sdk.uikit.views.gopay.payment.GoPayPaymentActivity;
import com.midtrans.sdk.uikit.widgets.BoldTextView;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays list of available payment methods.
 * <p/>
 * Created by shivam on 10/16/15.
 */
public class PaymentMethodsActivity extends BaseActivity implements PaymentMethodsAdapter.PaymentMethodListener, ItemDetailsAdapter.ItemDetailListener {
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
    private boolean isGopay = false;
    private boolean isDanamonOnline = false;
    private boolean backButtonEnabled;

    private MidtransSDK midtransSDK = null;

    //Views
    private Toolbar toolbar = null;
    private RecyclerView paymentMethodsView = null;
    private RecyclerView itemDetailsView = null;
    private TextView merchantName = null;
    private LinearLayout progressContainer = null;
    private LinearLayout maintenanceContainer;
    private ImageView logo = null;
    private ArrayList<EnabledPayment> bankTransfers = new ArrayList<>();
    private PaymentMethodsAdapter paymentMethodsAdapter;
    private AlertDialog alertDialog;
    private ImageView progressImage;
    private ImageView secureBadge;
    private TextView progressMessage;
    private BoldTextView maintenanceTitleMessage;
    private DefaultTextView maintenanceMessage;
    private FancyButton buttonRetry;
    private AppBarLayout appbar;
    private boolean alreadyUtilized = false;

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
        initRetryButton();

        setSupportActionBar(toolbar);

        bindDataToView();
        getPaymentPages();
    }

    private void initRetryButton() {
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                buttonRetry.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                buttonRetry.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }
        }
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
                String price = getString(R.string.prefix_money, Utils.getFormattedAmount(itemDetails.getQuantity() * itemDetails.getPrice()));
                String itemName = itemDetails.getName();
                if (itemDetails.getQuantity() > 1) {
                    itemName = getString(R.string.text_item_name_format, itemDetails.getName(), itemDetails.getQuantity());
                }
                itemViewDetails.add(new ItemViewDetails(itemName, price, ItemViewDetails.TYPE_ITEM, true));
            }

            ItemDetailsAdapter adapter = new ItemDetailsAdapter(itemViewDetails, this);
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
        appbar = (AppBarLayout) findViewById(R.id.main_appbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        merchantName = (TextView) findViewById(R.id.merchant_name);
        progressContainer = (LinearLayout) findViewById(R.id.progress_container);
        maintenanceContainer = (LinearLayout) findViewById(R.id.maintenance_container);
        progressImage = (ImageView) findViewById(R.id.progress_bar_image);
        secureBadge = (ImageView) findViewById(R.id.secure_badge);
        progressMessage = (TextView) findViewById(R.id.progress_bar_message);
        maintenanceTitleMessage = (BoldTextView) findViewById(R.id.text_maintenance_title);
        maintenanceMessage = (DefaultTextView) findViewById(R.id.text_maintenance_message);
        buttonRetry = (FancyButton) findViewById(R.id.button_maintenance_retry);

        if (isCreditCardOnly || isBankTransferOnly || isKlikBCA || isBCAKlikpay
                || isMandiriClickPay || isMandiriECash || isCIMBClicks || isBRIEpay
                || isTelkomselCash || isIndosatDompetku || isXlTunai
                || isIndomaret || isKioson || isGci) {
            progressMessage.setText(R.string.txt_checkout);
        }

        // Init progress
        Glide.with(this)
                .load(R.drawable.midtrans_loader)
                .asGif()
                .into(progressImage);
        progressMessage.setText(R.string.txt_loading_payment);
    }

    private void getPaymentPages() {
        progressContainer.setVisibility(View.VISIBLE);
        enableButtonBack(false);
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);

        if (!isAlreadyUtilized()) {
            midtransSDK.checkout(userDetail.getUserId(), new CheckoutCallback() {
                @Override
                public void onSuccess(Token token) {
                    Log.i(TAG, "checkout token:" + token.getTokenId());
                    LocalDataHandler.saveString(Constants.AUTH_TOKEN, token.getTokenId());
                    getPaymentOptions(token.getTokenId());
                }

                @Override
                public void onFailure(Token token, String reason) {
                    Log.d(TAG, "Failed to registering transaction: " + reason);
                    enableButtonBack(true);
                    String errorMessage = MessageUtil.createMessageWhenCheckoutFailed(PaymentMethodsActivity.this, token.getErrorMessage());
                    showErrorMessage(errorMessage);
                }

                @Override
                public void onError(Throwable error) {
                    Logger.e(TAG, "checkout>error:" + error.getMessage());
                    showFallbackErrorPage(error, getString(R.string.maintenance_message));
                }
            });
        } else {
            SdkUIFlowUtil.showToast(this, getString(R.string.error_utilized_orderid));
        }
    }

    private void showFallbackErrorPage(Throwable error, String defaultMessage) {
        MessageInfo messageInfo = MessageUtil.createMessageOnError(this, error, defaultMessage);

        if (messageInfo.statusMessage.equalsIgnoreCase(MessageUtil.TIMEOUT) || messageInfo.statusMessage.equalsIgnoreCase(MessageUtil.RETROFIT_TIMEOUT)) {
            maintenanceTitleMessage.setText(getString(R.string.failed_title));
            maintenanceMessage.setText(getString(R.string.timeout_message));
            buttonRetry.setText(getString(R.string.try_again));
            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMaintenanceContainer(false);
                    if (isAlreadyUtilized()) {
                        SdkUIFlowUtil.showToast(PaymentMethodsActivity.this, getString(R.string.error_utilized_orderid));
                    } else {
                        getPaymentPages();
                    }
                }
            });
        } else {
            maintenanceTitleMessage.setText(getString(R.string.failed_title));
            maintenanceMessage.setText(messageInfo.detailsMessage);
            buttonRetry.setText(getString(R.string.maintenance_back));

            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMaintenanceContainer(false);
                    finish();
                }
            });
        }
        showMaintenanceContainer(true);
    }

    private void showMaintenanceContainer(boolean show) {
        if (show) {
            appbar.setVisibility(View.GONE);
            maintenanceContainer.setVisibility(View.VISIBLE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            maintenanceContainer.setVisibility(View.GONE);
        }
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
                    midtransSDK.setTransaction(transaction);
                    midtransSDK.setPromoResponses(transaction.getPromos());
                    midtransSDK.setMerchantLogo(logoUrl);
                    midtransSDK.setMerchantName(merchantName);
                    // Prioritize custom color themes over Snap preferences
                    if (midtransSDK.getColorTheme() == null
                            || !(midtransSDK.getColorTheme() instanceof CustomColorTheme)) {
                        midtransSDK.setColorTheme(
                                new ColorTheme(
                                        PaymentMethodsActivity.this,
                                        transaction.getMerchantData().getPreference().getColorScheme()));
                    }
                    // Set color themes on item details
                    itemDetailsView.setBackgroundColor(midtransSDK.getColorTheme().getPrimaryColor());

                    midtransSDK.getmMixpanelAnalyticsManager().setDeviceType(SdkUIFlowUtil.getDeviceType(PaymentMethodsActivity.this));
                    showLogo(logoUrl);
                    if (TextUtils.isEmpty(logoUrl)) {
                        showName(merchantName);
                    }

                    //merchant data is already retrieved, set secure badge here
                    int secureBadgeCode = SdkUIFlowUtil.getCreditCardIconType();
                    switch (secureBadgeCode) {
                        case 1:
                            secureBadge.setImageResource(R.drawable.badge_full);
                            break;
                        case 3:
                            secureBadge.setImageResource(R.drawable.badge_jcb);
                            break;
                        case 4:
                            secureBadge.setImageResource(R.drawable.badge_amex);
                            break;
                        default:
                            secureBadge.setImageResource(R.drawable.badge_default);
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
                showTransactionDetailsErrorMessage();
            }

            @Override
            public void onError(Throwable error) {
                Logger.e(TAG, "error:" + error.getMessage());
                enableButtonBack(true);
                progressContainer.setVisibility(View.GONE);
                showFallbackErrorPage(error, getString(R.string.maintenance_message));
            }
        });
    }

    private void initPaymentMethods(List<EnabledPayment> enabledPayments) {
        initialiseAdapterData(enabledPayments);

        if (isCreditCardOnly) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_credit_debit))) {
                Intent intent = new Intent(PaymentMethodsActivity.this, SavedCreditCardActivity.class);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isBankTransferOnly) {
            if (SdkUIFlowUtil.isBankTransferMethodEnabled(getApplicationContext(), enabledPayments)) {
                Intent startBankPayment = new Intent(PaymentMethodsActivity.this, SelectBankTransferActivity.class);
                if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, false)) {
                    if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_permata_va))) {
                        startBankPayment.putExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, true);
                    } else {
                        showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
                        return;
                    }
                } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, false)) {
                    if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_mandiri_bill_payment))) {
                        startBankPayment.putExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, true);
                    } else {
                        showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
                        return;
                    }
                } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_BCA, false)) {
                    if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_bca_va))) {
                        startBankPayment.putExtra(UserDetailsActivity.BANK_TRANSFER_BCA, true);
                    } else {
                        showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
                        return;
                    }
                } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_BNI, false)) {
                    if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_bni_va))) {
                        startBankPayment.putExtra(UserDetailsActivity.BANK_TRANSFER_BNI, true);
                    } else {
                        showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
                        return;
                    }
                } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, false)) {
                    if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_all_va))) {
                        startBankPayment.putExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, true);
                    } else {
                        showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
                        return;
                    }
                }
                startBankPayment.putExtra(BankTransferListActivity.EXTRA_BANK_LIST, getBankTransfers());
                startActivityForResult(startBankPayment, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isBCAKlikpay) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_bca_click))) {
                Intent startBCAKlikPayActivity = new Intent(this, BcaKlikPayPaymentActivity.class);
                startActivityForResult(startBCAKlikPayActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isKlikBCA) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_klik_bca))) {
                Intent startKlikBcaActivity = new Intent(this, KlikBcaPaymentActivity.class);
                startKlikBcaActivity.putExtra(getString(R.string.position), Constants.PAYMENT_METHOD_KLIKBCA);
                startActivityForResult(startKlikBcaActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isMandiriClickPay) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_mandiri_clickpay))) {
                Intent startMandiriClickpay = new Intent(this, MandiriClickPayActivity.class);
                startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isMandiriECash) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_mandiri_ecash))) {
                Intent startMandiriECash = new Intent(this, MandiriECashActivity.class);
                startActivityForResult(startMandiriECash, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isCIMBClicks) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_mandiri_clickpay))) {
                Intent startCIMBClickpay = new Intent(this, CIMBClickPayActivity.class);
                startActivityForResult(startCIMBClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isBRIEpay) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_epay_bri))) {
                Intent startMandiriClickpay = new Intent(this, EpayBriActivity.class);
                startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isTelkomselCash) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_telkomsel_cash))) {
                Intent telkomselCashActivity = new Intent(this, TelkomselCashActivity.class);
                startActivityForResult(telkomselCashActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isIndosatDompetku) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_indosat_dompetku))) {
                Intent startIndosatPaymentActivity = new Intent(this, IndosatDompetkuActivity.class);
                startActivityForResult(startIndosatPaymentActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isXlTunai) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_xl_tunai))) {
                Intent xlTunaiActivity = new Intent(this, XLTunaiActivity.class);
                startActivityForResult(xlTunaiActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isIndomaret) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_indomaret))) {
                Intent startIndomaret = new Intent(this, IndomaretActivity.class);
                startActivityForResult(startIndomaret, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isKioson) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_kioson))) {
                Intent kiosanActvity = new Intent(this, KiosonActivity.class);
                startActivityForResult(kiosanActvity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isGci) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_gci))) {
                Intent gciActivity = new Intent(this, GCIActivity.class);
                startActivityForResult(gciActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isGopay) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_gopay))) {
                Intent gopayActivity = new Intent(this, GoPayPaymentActivity.class);
                startActivityForResult(gopayActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else if (isDanamonOnline) {
            if (SdkUIFlowUtil.isPaymentMethodEnabled(enabledPayments, getString(R.string.payment_danamon_online))) {
                Intent danamonOnlineIntent = new Intent(this, DanamonOnlineActivity.class);
                startActivityForResult(danamonOnlineIntent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } else {
                showErrorAlertDialog(getString(R.string.payment_not_enabled_message));
            }
        } else {
            if (data.isEmpty()) {
                showErrorAlertDialog(getString(R.string.message_payment_method_empty));
            } else if (data.size() == 1) {
                startPaymentMethod(data.get(0));
            } else {
                progressContainer.setVisibility(View.GONE);
                paymentMethodsAdapter.setData(data);

                //track page select paymentpage
                midtransSDK.trackEvent(AnalyticsEventName.PAGE_SELECT_PAYMENT);
            }
        }
    }

    private void startPaymentMethod(PaymentMethodsModel paymentMethod) {
        String name = paymentMethod.getName();
        if (name.equalsIgnoreCase(getString(R.string.payment_method_credit_card))) {
            Intent intent = new Intent(this, SavedCreditCardActivity.class);
            startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bank_transfer))) {
            Intent startBankPayment = new Intent(this, BankTransferListActivity.class);
            startBankPayment.putExtra(BankTransferListActivity.EXTRA_BANK_LIST, getBankTransfers());
            startActivityForResult(startBankPayment, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_mandiri_clickpay))) {
            Intent startMandiriClickpay = new Intent(this, MandiriClickPayActivity.class);
            startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bri_epay))) {
            Intent startMandiriClickpay = new Intent(this, EpayBriActivity.class);
            startActivityForResult(startMandiriClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_cimb_clicks))) {
            Intent startCIMBClickpay = new Intent(this, CIMBClickPayActivity.class);
            startActivityForResult(startCIMBClickpay, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_mandiri_ecash))) {
            Intent startMandiriECash = new Intent(this, MandiriECashActivity.class);
            startActivityForResult(startMandiriECash, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_indosat_dompetku))) {
            Intent startIndosatPaymentActivity = new Intent(this, IndosatDompetkuActivity.class);
            startActivityForResult(startIndosatPaymentActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_indomaret))) {
            Intent startIndomaret = new Intent(this, IndomaretActivity.class);
            startActivityForResult(startIndomaret, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_bca_klikpay))) {
            Intent startBCAKlikPayActivity = new Intent(this, BcaKlikPayPaymentActivity.class);
            startActivityForResult(startBCAKlikPayActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_klik_bca))) {
            Intent startKlikBcaActivity = new Intent(this, KlikBcaPaymentActivity.class);
            startKlikBcaActivity.putExtra(getString(R.string.position), Constants.PAYMENT_METHOD_KLIKBCA);
            startActivityForResult(startKlikBcaActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_telkomsel_cash))) {
            Intent telkomselCashActivity = new Intent(this, TelkomselCashActivity.class);
            startActivityForResult(telkomselCashActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_xl_tunai))) {
            Intent xlTunaiActivity = new Intent(this, XLTunaiActivity.class);
            startActivityForResult(xlTunaiActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_kioson))) {
            Intent kiosanActvity = new Intent(this, KiosonActivity.class);
            startActivityForResult(kiosanActvity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_gci))) {
            Intent gciActivity = new Intent(this, GCIActivity.class);
            startActivityForResult(gciActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_gopay))) {
            Intent gopayIntent = new Intent(this, GoPayPaymentActivity.class);
            startActivityForResult(gopayIntent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equalsIgnoreCase(getString(R.string.payment_method_danamon_online))) {
            Intent danamonOnlineIntent = new Intent(this, DanamonOnlineActivity.class);
            startActivityForResult(danamonOnlineIntent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
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
        bankTransfers.clear();

        for (EnabledPayment enabledPayment : enabledPayments) {
            if ((enabledPayment.getCategory() != null && enabledPayment.getCategory().equals(getString(R.string.enabled_payment_category_banktransfer)))
                    || enabledPayment.getType().equalsIgnoreCase(getString(R.string.payment_mandiri_bill_payment))) {
                bankTransfers.add(enabledPayment);
            } else {
                PaymentMethodsModel model = PaymentMethods.getMethods(this, enabledPayment.getType(), enabledPayment.getStatus());
                if (model != null) {
                    data.add(model);
                }
            }
        }


        if (!bankTransfers.isEmpty()) {
            data.add(PaymentMethods.getMethods(this, getString(R.string.payment_bank_transfer), EnabledPayment.STATUS_UP));
        }
        SdkUtil.sortPaymentMethodsByPriority(data);
    }

    /**
     * sends broadcast for transaction details.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.d(TAG, "in onActivity result : request code is " + requestCode + "," + resultCode);
        Logger.d(TAG, "in onActivity result : data:" + data);

        if (resultCode == UiKitConstants.RESULT_SDK_NOT_AVAILABLE) {
            finish();
            return;
        }

        if (requestCode == Constants.RESULT_CODE_PAYMENT_TRANSFER) {
            Logger.d(TAG, "sending result back with code " + requestCode);

            if (resultCode == RESULT_OK) {
                TransactionResponse response = (TransactionResponse) data.getSerializableExtra(getString(R.string.transaction_response));

                if (response != null) {
                    if (response.getStatusCode().equals(getString(R.string.success_code_200))) {
                        midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_SUCCESS));
                        setAlreadyUtilized(true);
                    } else if (response.getStatusCode().equals(getString(R.string.success_code_201))) {
                        midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_PENDING));
                        setAlreadyUtilized(true);
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

                        midtransSDK.notifyTransactionFinished(new TransactionResult(true));
                        finish();
                    }
                } else {
                    TransactionResponse response = (TransactionResponse) data.getSerializableExtra(getString(R.string.transaction_response));

                    if (response != null) {
                        if (response.getStatusCode().equals(getString(R.string.success_code_200))) {
                            midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_SUCCESS));
                            setAlreadyUtilized(true);
                        } else if (response.getStatusCode().equals(getString(R.string.success_code_201))) {
                            midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_PENDING));
                            setAlreadyUtilized(true);
                        } else {
                            midtransSDK.notifyTransactionFinished(new TransactionResult(response, null, TransactionResult.STATUS_FAILED));
                        }
                        finish();
                    } else {
                        if (this.data.size() == 1 || isCreditCardOnly || isBankTransferOnly || isBCAKlikpay || isKlikBCA
                                || isMandiriClickPay || isMandiriECash || isCIMBClicks || isBRIEpay
                                || isTelkomselCash || isIndosatDompetku || isXlTunai
                                || isIndomaret || isKioson || isGci) {

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
            Glide.with(this)
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

    private void showErrorMessage(String message) {
        if (!isFinishing()) {
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setPositiveButton(R.string.btn_retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (isAlreadyUtilized()) {
                                SdkUIFlowUtil.showToast(PaymentMethodsActivity.this, getString(R.string.error_utilized_orderid));
                            } else {
                                getPaymentPages();
                            }
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

    private void showTransactionDetailsErrorMessage() {
        if (!isFinishing()) {
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.error_snap_transaction_details))
                    .setPositiveButton(R.string.btn_retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (isAlreadyUtilized()) {
                                SdkUIFlowUtil.showToast(PaymentMethodsActivity.this, getString(R.string.error_utilized_orderid));
                            } else {
                                getPaymentPages();
                            }
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

    public EnabledPayments getBankTransfers() {
        return new EnabledPayments(this.bankTransfers);
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

        midtransSDK.notifyTransactionFinished(new TransactionResult(true));
        super.onBackPressed();
    }

    private void showBackButton() {
        if (backButtonEnabled && getSupportActionBar() != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = ContextCompat.getDrawable(PaymentMethodsActivity.this, R.drawable.ic_back);
                    if (midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                        drawable.setColorFilter(
                                midtransSDK.getColorTheme().getPrimaryDarkColor(),
                                PorterDuff.Mode.SRC_ATOP);
                    }
                    toolbar.setNavigationIcon(drawable);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SdkUIFlowUtil.hideKeyboard(PaymentMethodsActivity.this);
                            onBackPressed();
                        }
                    });
                }
            }, 50);
        }
    }

    @Override
    public void onItemShown() {
        midtransSDK.trackEvent(AnalyticsEventName.PAGE_ORDER_SUMMARY);
    }


    public void setAlreadyUtilized(boolean alreadyUtilized) {
        this.alreadyUtilized = alreadyUtilized;
    }

    public boolean isAlreadyUtilized() {
        return alreadyUtilized;
    }
}