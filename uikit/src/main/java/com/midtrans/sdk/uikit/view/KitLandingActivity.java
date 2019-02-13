package com.midtrans.sdk.uikit.view;

import android.os.Bundle;
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
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.uikit.MidtransKitFlow;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.BaseActivity;
import com.midtrans.sdk.uikit.base.callback.PaymentResult;
import com.midtrans.sdk.uikit.base.callback.Result;
import com.midtrans.sdk.uikit.base.enums.PaymentStatus;
import com.midtrans.sdk.uikit.utilities.ActivityHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class KitLandingActivity extends BaseActivity {

    private PaymentResult<PaymentResponse> callback;
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
    private String token = null;
    private CheckoutTransaction checkoutTransaction = null;
    @PaymentType
    private String paymentType;

    private LinearLayout progressContainer;
    private ImageView progressImage;
    private TextView progressMessage;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kit_landing);

        initToolbarAndView();
        initializeTheme();
        getIntentDataFromMidtransKitFlow();
        checkDataBeforeStartMidtransSdk();
    }

    private void initToolbarAndView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressContainer = findViewById(R.id.progress_container);
        progressImage = findViewById(R.id.progress_bar_image);
        progressMessage = findViewById(R.id.progress_bar_message);

        Ion.with(progressImage)
                .load(ActivityHelper.getImagePath(this) + R.drawable.midtrans_loader);
    }

    @SuppressWarnings("unchecked")
    private void getIntentDataFromMidtransKitFlow() {
        callback = (PaymentResult<PaymentResponse>) getIntent().getSerializableExtra(MidtransKitFlow.INTENT_EXTRA_CALLBACK);
        checkoutTransaction = (CheckoutTransaction) getIntent().getSerializableExtra(MidtransKitFlow.INTENT_EXTRA_TRANSACTION);
        paymentType = getIntent().getStringExtra(MidtransKitFlow.INTENT_EXTRA_DIRECT);
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
    }

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

    private void startCheckoutWithMidtransSdk(CheckoutTransaction checkoutTransaction) {
        MidtransSdk
                .getInstance()
                .checkoutWithTransaction(checkoutTransaction, new MidtransCallback<CheckoutWithTransactionResponse>() {
                    @Override
                    public void onSuccess(CheckoutWithTransactionResponse data) {
                        if (data.getToken() == null) {
                            if (data.getErrorMessages().get(0) != null) {
                                callback.onPaymentFinished(new Result(PaymentStatus.STATUS_FAILED, data.getErrorMessages().get(0), paymentType), null);
                            } else {
                                setOnFailedCallback(new Throwable(Constants.MESSAGE_ERROR_FAILURE_RESPONSE));
                            }
                        } else {
                            startGettingPaymentInfoWithMidtransSdk(data.getToken());
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        setOnFailedCallback(throwable);
                    }
                });
    }

    private void startGettingPaymentInfoWithMidtransSdk(String token) {
        MidtransSdk
                .getInstance()
                .getPaymentInfo(token, new MidtransCallback<PaymentInfoResponse>() {
                    @Override
                    public void onSuccess(PaymentInfoResponse data) {

                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        setOnFailedCallback(throwable);
                    }
                });
    }

    private void showProgress() {
        progressContainer.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressContainer.setVisibility(View.GONE);
    }

    private void setOnFailedCallback(Throwable throwable) {
        callback.onFailed(throwable);
        finish();
    }

}