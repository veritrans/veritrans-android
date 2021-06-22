package com.midtrans.sdk.uikit.views.uob.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;

public class UobWebPaymentActivity extends BasePaymentActivity implements UobWebPaymentView {

    private UobWebPaymentPresenter presenter;
    private FancyButton buttonPrimary;
    private Boolean isAlreadyGotResponse = false;
    private int uobEzpayIntentCode, attempt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressLayout();
        setContentView(R.layout.uikit_activity_uob_web_payment);
        initPresenter();
        initLayout();
        initData();
        initActionButton();
        hideProgressLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
            finishPayment(RESULT_OK, presenter.getTransactionResponse());
        } else if (requestCode == UiKitConstants.INTENT_CODE_UOBEZPAY) {
            this.uobEzpayIntentCode = requestCode;
        }
    }

    @Override
    public void onBackPressed() {
        if (isDetailShown) {
            displayOrHideItemDetails();
        } else if (isAlreadyGotResponse) {
            finishPayment(RESULT_CANCELED, presenter.getTransactionResponse());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void bindViews() {
        buttonPrimary = findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPrimary);
    }

    @Override
    public void onGetTransactionStatusError(Throwable error) {
        //do nothing
    }

    @Override
    public void onGetTransactionStatusFailure(TransactionResponse transactionResponse) {
        //do nothing
    }

    @Override
    public void onGetTransactionStatusSuccess(TransactionResponse transactionResponse) {
        showPaymentStatusPage(transactionResponse, presenter.isShowPaymentStatusPage());
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        if (isActivityRunning()) {
            if (isResponseValid(response)) {
                isAlreadyGotResponse = true;
                openDeeplink(response.getUobWebUrl());
            } else {
                onPaymentFailure(response);
            }
        } else {
            finishPayment(RESULT_OK, presenter.getTransactionResponse());
        }
    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {
        hideProgressLayout();
        if (attempt < UiKitConstants.MAX_ATTEMPT) {
            attempt += 1;
            SdkUIFlowUtil.showToast(UobWebPaymentActivity.this, getString(R.string.uikit_error_uob_transaction));
        } else {
            if (response != null) {
                showPaymentStatusPage(response, presenter.isShowPaymentStatusPage());
            }
        }
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusmessage(error);
    }

    private void initPresenter() {
        presenter = new UobWebPaymentPresenter(this);
    }

    private void initLayout() {
        ViewStub stub = findViewById(R.id.uob_layout_stub);
        stub.setLayoutResource(R.layout.uikit_layout_uob_web_payment);
        stub.inflate();
    }

    private void initData() {
        setPageTitle(getString(R.string.page_title_uobweb));
    }

    private void initActionButton() {
        if (isProductionBuild()) {
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to prevent "payment has been paid"
                    if (isAlreadyGotResponse) {
                        openDeeplink(presenter.getTransactionResponse().getUobWebUrl());
                    } else {
                        startUobEzpayPayment();
                    }
                }
            });
            buttonPrimary.setTextBold();
        } else {
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to prevent "payment has been paid"
                    if (isAlreadyGotResponse) {
                        openDeeplink(presenter.getTransactionResponse().getUobWebUrl());
                    } else {
                        startUobEzpayPayment();
                    }
                }
            });
            buttonPrimary.setTextBold();
        }
    }

    private void openDeeplink(String deeplinkUrl) {
        if (deeplinkUrl == null) {
            Toast.makeText(this, R.string.UOB_payment_cant_open_deeplink, Toast.LENGTH_SHORT).show();
        } else {
            buttonPrimary.setEnabled(false);
            Toast.makeText(this, getString(R.string.uikit_redirecting_to_uob_web), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl));
            startActivityForResult(intent, UiKitConstants.INTENT_CODE_UOBEZPAY);
        }
        finishPayment(RESULT_CANCELED, presenter.getTransactionResponse());
    }

    private void startUobEzpayPayment() {
        showProgressLayout();
        presenter.startUobEzpayPayment();
    }

    private boolean isResponseValid(TransactionResponse response) {
        if (response == null) {
            return false;
        } else {
            return (!TextUtils.isEmpty(response.getUobWebUrl()));
        }
    }

    private boolean isProductionBuild() {
        return presenter.isProductionBuild();
    }
}
