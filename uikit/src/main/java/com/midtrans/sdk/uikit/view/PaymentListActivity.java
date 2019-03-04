package com.midtrans.sdk.uikit.view;

import com.google.android.material.appbar.AppBarLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.enablepayment.EnabledPayment;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantPreferences;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.MidtransKitFlow;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.callback.PaymentResult;
import com.midtrans.sdk.uikit.base.callback.Result;
import com.midtrans.sdk.uikit.base.composer.BaseActivity;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.enums.PaymentStatus;
import com.midtrans.sdk.uikit.base.model.MessageInfo;
import com.midtrans.sdk.uikit.utilities.ActivityHelper;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.CurrencyHelper;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.adapter.PaymentItemDetailsAdapter;
import com.midtrans.sdk.uikit.view.adapter.PaymentMethodsAdapter;
import com.midtrans.sdk.uikit.view.akulaku.AkulakuInstructionActivity;
import com.midtrans.sdk.uikit.view.alfamart.instruction.AlfamartInstructionActivity;
import com.midtrans.sdk.uikit.view.banktransfer.list.BankTransferListActivity;
import com.midtrans.sdk.uikit.view.banktransfer.list.model.EnabledBankTransfer;
import com.midtrans.sdk.uikit.view.bcaklikpay.BcaKlikpayInstructionActivity;
import com.midtrans.sdk.uikit.view.briepay.BriEpayInstructionActivity;
import com.midtrans.sdk.uikit.view.cimbclicks.CimbClicksInstructionActivity;
import com.midtrans.sdk.uikit.view.danamononline.DanamonOnlineInstructionActivity;
import com.midtrans.sdk.uikit.view.gopay.instruction.GopayInstructionActivity;
import com.midtrans.sdk.uikit.view.indomaret.instruction.IndomaretInstructionActivity;
import com.midtrans.sdk.uikit.view.klikbca.instruction.KlikBcaInstructionActivity;
import com.midtrans.sdk.uikit.view.mandiriecash.MandiriEcashInstructionActivity;
import com.midtrans.sdk.uikit.view.model.ItemViewDetails;
import com.midtrans.sdk.uikit.view.model.PaymentMethodsModel;
import com.midtrans.sdk.uikit.widget.BoldTextView;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_FAILURE_RESPONSE;
import static com.midtrans.sdk.uikit.utilities.PaymentListHelper.mappingBankTransfer;
import static com.midtrans.sdk.uikit.utilities.PaymentListHelper.mappingEnabledPayment;

public class PaymentListActivity extends BaseActivity {

    public static final String EXTRA_PAYMENT_INFO = "extra.payment.info";

    /**
     * This property used for direct payment
     */
    private boolean isCreditCardOnly = false;
    private boolean isBankTransferOnly = false;
    private boolean isBCAKlikpay = false;
    private boolean isKlikBca = false;
    private boolean isMandiriClickPay = false;
    private boolean isMandiriEcash = false;
    private boolean isCimbClicks = false;
    private boolean isBriEpay = false;
    private boolean isTelkomselCash = false;
    private boolean isIndomaret = false;
    private boolean isGopay = false;
    private boolean isDanamonOnline = false;
    private boolean isAkulaku = false;
    private boolean isAlfamart = false;
    private boolean isBcaVa = false;
    private boolean isBniVa = false;
    private boolean isPermataVa = false;
    private boolean isOtherVa = false;
    private boolean isMandiriBill = false;
    private boolean isDeepLink = false;

    /**
     * This property used for token, transaction object, and payment callback
     * Later, this used for deciding process inside payment method
     */
    private String token = null;
    private CheckoutTransaction checkoutTransaction = null;
    private PaymentResult callback = null;

    /**
     * This property used for layout related stuff
     */
    private Toolbar toolbar;
    private AppBarLayout appbar;
    private TextView merchantNameInToolbar;
    private ImageView merchantLogoInToolbar;
    private ImageView secureBadge;

    /**
     * This property used for making list payment and list item
     */
    private RecyclerView containerPaymentMethod = null;
    private RecyclerView containerItemDetails = null;
    private List<EnabledPayment> bankTransferList = new ArrayList<>();
    private List<PaymentMethodsModel> data = new ArrayList<>();

    /**
     * This property used for making loading when performing network request
     */
    private ImageView progressImage;
    private TextView progressMessage;

    /**
     * This property used for showing error dialog or maintenance view
     */
    private AlertDialog alertDialog = null;
    private LinearLayout progressContainer;
    private LinearLayout maintenanceContainer;
    private BoldTextView maintenanceTitle;
    private DefaultTextView maintenanceMessage;
    private FancyButton maintenanceButton;

    /**
     * This property used for set onFailed callback to host-app
     * This happen when getting exception in network request or user press back when performing network request
     */
    private boolean isThrowableFromNetworkRequest = false;
    private boolean isPaymentListAlreadyShow = false;
    private Throwable throwableFromNetworkRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);

        initToolbarAndView();
        initializeTheme();
        getIntentDataFromMidtransKitFlow();
        checkDataBeforeStartMidtransSdk();
    }

    /**
     * This method used for binding view and setup other view stuff like toolbar and progress image
     */
    private void initToolbarAndView() {
        toolbar = findViewById(R.id.toolbar);
        appbar = findViewById(R.id.main_appbar);
        progressContainer = findViewById(R.id.progress_container);
        progressImage = findViewById(R.id.progress_bar_image);
        progressMessage = findViewById(R.id.progress_bar_message);
        maintenanceContainer = findViewById(R.id.maintenance_container);
        maintenanceTitle = findViewById(R.id.text_maintenance_title);
        maintenanceMessage = findViewById(R.id.text_maintenance_message);
        maintenanceButton = findViewById(R.id.button_maintenance_retry);
        containerItemDetails = findViewById(R.id.recycler_view_item_list);
        containerPaymentMethod = findViewById(R.id.recycler_view_payment_methods);
        merchantLogoInToolbar = findViewById(R.id.image_view_merchant_logo);
        merchantNameInToolbar = findViewById(R.id.text_view_merchant_name);
        secureBadge = findViewById(R.id.image_view_secure_badge);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Ion.with(progressImage)
                .load(ActivityHelper.getImagePath(this) + R.drawable.midtrans_loader);
    }

    /**
     * This method used for getting intent extra from MidtransKitFlow
     * later it can be used for making direct payment, callback, and deciding sdk process
     */
    @SuppressWarnings("unchecked")
    private void getIntentDataFromMidtransKitFlow() {
        callback = (PaymentResult) getIntent().getSerializableExtra(MidtransKitFlow.INTENT_EXTRA_CALLBACK);
        checkoutTransaction = (CheckoutTransaction) getIntent().getSerializableExtra(MidtransKitFlow.INTENT_EXTRA_TRANSACTION);
        token = getIntent().getStringExtra(MidtransKitFlow.INTENT_EXTRA_TOKEN);

        isCreditCardOnly = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_CREDIT_CARD_ONLY, false);
        isBankTransferOnly = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_ONLY, false);
        isGopay = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_GOPAY, false);
        isBCAKlikpay = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BCA_KLIKPAY, false);
        isKlikBca = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_KLIK_BCA, false);
        isMandiriClickPay = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_MANDIRI_CLICKPAY, false);
        isMandiriEcash = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_MANDIRI_ECASH, false);
        isCimbClicks = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_CIMB_CLICKS, false);
        isBriEpay = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BRI_EPAY, false);
        isTelkomselCash = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_TELKOMSEL_CASH, false);
        isIndomaret = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_INDOMARET, false);
        isDanamonOnline = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_DANAMON_ONLINE, false);
        isAkulaku = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_AKULAKU, false);
        isAlfamart = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_ALFAMART, false);
        isBcaVa = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_BCA, false);
        isBniVa = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_BNI, false);
        isPermataVa = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_PERMATA, false);
        isOtherVa = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_OTHER, false);
        isMandiriBill = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_MANDIRI, false);
    }

    /**
     * This method is checker for checking the data before starting sdk flow
     * If host-app not have token, start checkout with object
     * If host-app have token, start getting payment info with token
     */
    private void checkDataBeforeStartMidtransSdk() {
        showProgress();
        if (checkoutTransaction == null && token != null) {
            startGettingPaymentInfoWithMidtransSdk(token);
        } else if (checkoutTransaction != null && token == null) {
            startCheckoutWithMidtransSdk(checkoutTransaction);
        } else {
            setOnFailedCallback(new Throwable("Please check your input data, MidtransKit is not started."));
        }
    }

    /**
     * This method use CheckoutTransaction object as parameter and used for creating token
     */
    private void startCheckoutWithMidtransSdk(CheckoutTransaction checkoutTransaction) {
        progressMessage.setText(R.string.txt_checkout);
        MidtransSdk
                .getInstance()
                .checkoutWithTransaction(checkoutTransaction, new MidtransCallback<CheckoutWithTransactionResponse>() {
                    @Override
                    public void onSuccess(CheckoutWithTransactionResponse data) {
                        doOnCheckoutWithMidtransSdkSucceeded(data);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        doOnCheckoutWithMidtransSdkFailed(throwable);
                    }
                });
    }

    /**
     * This method will handle startCheckoutWithMidtransSdk() if success making network call
     */
    private void doOnCheckoutWithMidtransSdkSucceeded(CheckoutWithTransactionResponse response) {
        if (response != null) {
            if (response.getToken() != null) {
                startGettingPaymentInfoWithMidtransSdk(response.getToken());
            } else {
                if (response.getErrorMessages().get(0) != null) {
                    String errorMessage = MessageHelper.createMessageWhenCheckoutFailed(PaymentListActivity.this, response.getErrorMessages());
                    showErrorMessage(errorMessage, true);
                } else {
                    showErrorMessage(MESSAGE_ERROR_FAILURE_RESPONSE, true);
                }
            }
        } else {
            showErrorMessage(MESSAGE_ERROR_FAILURE_RESPONSE, true);
        }
    }

    /**
     * This method will handle startCheckoutWithMidtransSdk() if failed when making network call
     */
    private void doOnCheckoutWithMidtransSdkFailed(Throwable throwable) {
        showFallbackErrorPage(throwable, true);
    }

    /**
     * This method use token for getting payment info
     */
    private void startGettingPaymentInfoWithMidtransSdk(String token) {
        this.token = token;
        progressMessage.setText(getString(R.string.txt_loading_payment));
        MidtransSdk
                .getInstance()
                .getPaymentInfo(this.token, new MidtransCallback<PaymentInfoResponse>() {
                    @Override
                    public void onSuccess(PaymentInfoResponse data) {
                        doOnGettingPaymentInfoWithMidtransSdkSucceeded(data);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        doOnGettingPaymentInfoWithMidtransSdkFailed(throwable);
                    }
                });
    }

    /**
     * This method will handle startGettingPaymentInfoWithMidtransSdk() if success making network call
     */
    private void doOnGettingPaymentInfoWithMidtransSdkSucceeded(PaymentInfoResponse data) {
        if (data != null) {
            initItemDetailsList(data);
            initPaymentMethodList(data);
            initMerchantPreferences(data);
            isPaymentListAlreadyShow = true;
        } else {
            showErrorMessage(null, false);
        }
    }

    /**
     * This method will handle startGettingPaymentInfoWithMidtransSdk() if failed when making network call
     */
    private void doOnGettingPaymentInfoWithMidtransSdkFailed(Throwable throwable) {
        showFallbackErrorPage(throwable, false);
    }

    /**
     * This method use for setup view stuff based on response and merchant preferences
     */
    private void initMerchantPreferences(PaymentInfoResponse response) {
        MerchantPreferences preferences = response.getMerchantData().getPreference();
        if (!TextUtils.isEmpty(preferences.getDisplayName())) {
            merchantNameInToolbar.setText(preferences.getDisplayName());
            merchantNameInToolbar.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(preferences.getLogoUrl())) {
            Ion.with(merchantLogoInToolbar)
                    .load(preferences.getLogoUrl());
            merchantLogoInToolbar.setVisibility(View.VISIBLE);
        }
        containerItemDetails.setBackgroundColor(MidtransKit.getInstance().getMidtransKitConfig().getColorTheme().getPrimaryColor());
        int secureBadgeType = PaymentListHelper.getCreditCardIconType(response);
        switch (secureBadgeType) {
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
    }

    /**
     * This method used for making list of itemlist
     */
    private void initItemDetailsList(PaymentInfoResponse response) {
        if (response != null) {
            List<ItemViewDetails> itemViewDetails = new ArrayList<>();
            int itemDetailsSize = response.getItemDetails() != null ? response.getItemDetails().size() : 0;

            // Add amount
            double amount = response.getTransactionDetails().getGrossAmount();
            String currency = response.getTransactionDetails().getCurrency();
            String formattedAmount = CurrencyHelper.formatAmount(this, amount, currency);

            // Add header
            itemViewDetails.add(new ItemViewDetails(
                    null,
                    formattedAmount,
                    ItemViewDetails.TYPE_ITEM_HEADER,
                    itemDetailsSize > 0));
            itemViewDetails.addAll(PaymentListHelper.mappingItemDetails(this, response));
            PaymentItemDetailsAdapter paymentItemDetailsAdapter = new PaymentItemDetailsAdapter(
                    itemViewDetails,
                    response.getTransactionDetails().getOrderId(),
                    () -> {

                    });

            containerItemDetails.setLayoutManager(new LinearLayoutManager(this));
            containerItemDetails.setAdapter(paymentItemDetailsAdapter);
        }
    }

    /**
     * This method used for making list of Payment Method
     */
    private void initPaymentMethodList(PaymentInfoResponse response) {
        data = mappingEnabledPayment(this, response);
        bankTransferList = mappingBankTransfer(response);
        PaymentMethodsAdapter paymentMethodsAdapter = new PaymentMethodsAdapter(position -> startPaymentMethodScreen(data.get(position), response));

        containerPaymentMethod.setLayoutManager(new LinearLayoutManager(this));
        containerPaymentMethod.setAdapter(paymentMethodsAdapter);

        if (data.isEmpty()) {
            showErrorMessage(getString(R.string.message_payment_method_empty), false);
        } else if (data.size() == 1) {
            hideProgress();
            isDeepLink = true;
            startPaymentMethodScreen(data.get(0), response);
        } else {
            hideProgress();
            paymentMethodsAdapter.setData(data);
        }
    }

    private void startPaymentMethodScreen(PaymentMethodsModel paymentMethodsModel, PaymentInfoResponse response) {
        String method = paymentMethodsModel.getPaymentType();
        Logger.debug("Payment Method clicked : " + method);
        switch (method) {
            case PaymentType.CREDIT_CARD:
                break;
            case PaymentListHelper.BANK_TRANSFER: {
                Intent intent = new Intent(this, BankTransferListActivity.class);
                intent.putExtra(BankTransferListActivity.EXTRA_BANK_LIST, new EnabledBankTransfer(bankTransferList));
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BankTransferListActivity.USE_DEEP_LINK, isDeepLink);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.BCA_KLIKPAY: {
                Intent intent = new Intent(this, CimbClicksInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.CIMB_CLICKS);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.KLIK_BCA: {
                Intent intent = new Intent(this, KlikBcaInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.KLIK_BCA);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.BRI_EPAY: {
                Intent intent = new Intent(this, BriEpayInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.BRI_EPAY);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.CIMB_CLICKS: {
                Intent intent = new Intent(this, CimbClicksInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.CIMB_CLICKS);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.MANDIRI_CLICKPAY:
                break;
            case PaymentType.INDOMARET: {
                Intent intent = new Intent(this, IndomaretInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.INDOMARET);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.TELKOMSEL_CASH:
                break;
            case PaymentType.MANDIRI_ECASH: {
                Intent intent = new Intent(this, MandiriEcashInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.MANDIRI_ECASH);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.GOPAY: {
                Intent intent = new Intent(this, GopayInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.GOPAY);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.DANAMON_ONLINE: {
                Intent intent = new Intent(this, DanamonOnlineInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.DANAMON_ONLINE);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.AKULAKU: {
                Intent intent = new Intent(this, AkulakuInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.AKULAKU);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
            case PaymentType.ALFAMART: {
                Intent intent = new Intent(this, AlfamartInstructionActivity.class);
                intent.putExtra(EXTRA_PAYMENT_INFO, response);
                intent.putExtra(BasePaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.ALFAMART);
                startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
                break;
            }
        }
    }

    private void showErrorMessage(String message, boolean isCheckout) {
        if (!isFinishing()) {
            if (!isCheckout) {
                message = getString(R.string.error_snap_transaction_details);
            }
            String finalMessage = message;
            if (alertDialog == null) {
                alertDialog = new AlertDialog
                        .Builder(this)
                        .setMessage(finalMessage)
                        .setCancelable(false)
                        .setPositiveButton(R.string.btn_retry, (dialog, which) -> {
                            if (isCheckout) {
                                startCheckoutWithMidtransSdk(checkoutTransaction);
                            } else {
                                startGettingPaymentInfoWithMidtransSdk(token);
                            }
                            dialog.dismiss();
                            alertDialog = null;
                        })
                        .setNegativeButton(R.string.btn_cancel, (dialog, which) -> {
                            isThrowableFromNetworkRequest = true;
                            throwableFromNetworkRequest = new Throwable(finalMessage);
                            dialog.dismiss();
                            onBackPressed();
                            alertDialog = null;
                        })
                        .create();
                alertDialog.show();
            }
        }
    }

    private void showFallbackErrorPage(Throwable error, boolean isCheckout) {
        throwableFromNetworkRequest = error;
        isThrowableFromNetworkRequest = true;
        MessageInfo messageInfo = MessageHelper.createMessageOnError(error, this);

        maintenanceTitle.setText(messageInfo.getTitleMessage());
        maintenanceMessage.setText(messageInfo.getDetailsMessage());
        maintenanceButton.setText(getString(R.string.try_again));
        maintenanceButton.setOnClickListener(v -> {
            if (isCheckout) {
                checkDataBeforeStartMidtransSdk();
            } else {
                startGettingPaymentInfoWithMidtransSdk(token);
            }
            showMaintenance(false);
        });
        showMaintenance(true);
    }

    private void showMaintenance(boolean show) {
        if (show) {
            maintenanceContainer.setVisibility(View.VISIBLE);
        } else {
            maintenanceContainer.setVisibility(View.GONE);
        }
    }

    private void showProgress() {
        progressContainer.setVisibility(View.VISIBLE);
        appbar.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressContainer.setVisibility(View.GONE);
        appbar.setVisibility(View.VISIBLE);
    }

    private void setOnFailedCallback(Throwable throwable) {
        callback.onFailed(throwable);
        if (isThrowableFromNetworkRequest) {
            isThrowableFromNetworkRequest = false;
            onBackPressed();
        }
    }

    /**
     * sends broadcast for transaction details.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.debug("in onActivity result : request code is " + requestCode + "," + resultCode);
        Logger.debug("in onActivity result : data:" + data);
        if (resultCode == com.midtrans.sdk.uikit.utilities.Constants.RESULT_SDK_NOT_AVAILABLE) {
            onBackPressed();
            return;
        }
        if (requestCode == Constants.RESULT_CODE_PAYMENT_TRANSFER) {
            Logger.debug("sending result back with code " + requestCode);
            if (resultCode == RESULT_OK) {
                PaymentListHelper.setActivityResult(resultCode, data, callback);
                super.onBackPressed();
            }
        } else {
            Logger.debug("failed to send result back " + requestCode);
        }
    }

    @Override
    public void onBackPressed() {
        if (isThrowableFromNetworkRequest) {
            setOnFailedCallback(throwableFromNetworkRequest);
        } else if (isPaymentListAlreadyShow) {
            callback.onPaymentFinished(new Result(PaymentStatus.STATUS_CANCEL, null), null);
        }
        super.onBackPressed();
    }
}