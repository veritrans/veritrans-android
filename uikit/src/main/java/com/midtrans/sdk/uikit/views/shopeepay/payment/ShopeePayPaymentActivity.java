package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.shopeepay.status.ShopeePayStatusActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

public class ShopeePayPaymentActivity extends BasePaymentActivity implements ShopeePayPaymentView {

    private ShopeePayPaymentPresenter presenter;
    private FancyButton buttonPrimary;
    private Boolean isAlreadyGotResponse = false;
    private int shopeePayIntentCode, attempt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressLayout();
        setContentView(R.layout.uikit_activity_shopeepay_payment);
        initPresenter();
        initLayout();
        initData();
        initActionButton();
        hideProgressLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (shopeePayIntentCode == UiKitConstants.INTENT_CODE_SHOPEEPAY && presenter != null) {
            presenter.getPaymentStatus();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
            finishPayment(RESULT_OK, presenter.getTransactionResponse());
        } else if (requestCode == UiKitConstants.INTENT_CODE_SHOPEEPAY) {
            this.shopeePayIntentCode = requestCode;
        }
    }

    @Override
    public void onBackPressed() {
        if (isDetailShown) {
            displayOrHideItemDetails();
        } else if (isAlreadyGotResponse) {
            showConfirmationDialog(getString(R.string.uikit_confirm_shopeepay_deeplink));
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
        showPaymentStatusPage(transactionResponse,presenter.isShowPaymentStatusPage() );
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        if (isActivityRunning()) {
            if (isResponseValid(response)) {
                if (isTablet()) {
                    Intent intent = new Intent(this, ShopeePayStatusActivity.class);
                    intent.putExtra(ShopeePayStatusActivity.EXTRA_PAYMENT_STATUS, response);
                    startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
                } else {
                    isAlreadyGotResponse = true;
                    openDeeplink(response.getDeeplinkUrl());
                }
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
            SdkUIFlowUtil.showToast(ShopeePayPaymentActivity.this, getString(R.string.uikit_error_shopeepay_transaction));
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
        presenter = new ShopeePayPaymentPresenter(this);
        presenter.setTabletDevice(this);
    }

    private void initLayout() {
        ViewStub stub = findViewById(R.id.shopee_layout_stub);
        if (isTablet()) {
            stub.setLayoutResource(R.layout.uikit_layout_shopeepay_payment_tablet);
        } else {
            stub.setLayoutResource(R.layout.uikit_layout_shopeepay_payment);
        }
        stub.inflate();
    }

    private void initData() {
        setPageTitle(getString(R.string.uikit_shopeepay));
    }

    private void initActionButton() {
        if (isProductionBuild()) {
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to prevent "payment has been paid"
                    if (isAlreadyGotResponse) {
                        openDeeplink(presenter.getTransactionResponse().getDeeplinkUrl());
                    } else {
                        startShopeePayPayment();
                    }
                }
            });
            buttonPrimary.setTextBold();
            buttonPrimary.setText(getString(R.string.uikit_shopeepay_confirm_button));

            buttonPrimary.setIconResource(R.drawable.uikit_ic_shopeepay_white);
            buttonPrimary.setIconPosition(FancyButton.POSITION_RIGHT);
        } else {
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to prevent "payment has been paid"
                    if (isAlreadyGotResponse) {
                        openDeeplink(presenter.getTransactionResponse().getDeeplinkUrl());
                    } else {
                        startShopeePayPayment();
                    }
                }
            });
            buttonPrimary.setTextBold();
            buttonPrimary.setText(getString(R.string.uikit_shopeepay_confirm_button));

            buttonPrimary.setIconResource(R.drawable.uikit_ic_shopeepay_white);
            buttonPrimary.setIconPosition(FancyButton.POSITION_RIGHT);
        }
    }

    private void openDeeplink(String deeplinkUrl) {
        if (deeplinkUrl == null) {
            Toast.makeText(this, R.string.shopeepay_payment_cant_open_deeplink, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.uikit_redirecting_to_shopee), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl));
            startActivityForResult(intent, UiKitConstants.INTENT_CODE_SHOPEEPAY);
        }
    }

    private void startShopeePayPayment() {
        showProgressLayout();
        presenter.startShopeePayPayment();
    }

    private boolean isResponseValid(TransactionResponse response) {
        if (response == null) {
            return false;
        } else {
            return (!TextUtils.isEmpty(response.getDeeplinkUrl()) || isTablet())
                && (!TextUtils.isEmpty(response.getQrisUrl()) || !isTablet());
        }
    }

    private void showConfirmationDialog(String message) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(ShopeePayPaymentActivity.this, R.style.AlertDialogCustom)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!ShopeePayPaymentActivity.this.isFinishing()) {
                            dialog.dismiss();
                            finishPayment(RESULT_CANCELED, presenter.getTransactionResponse());
                        }
                    }
                })
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!ShopeePayPaymentActivity.this.isFinishing()) {
                            dialog.dismiss();
                        }
                    }
                })
                .setTitle(R.string.cancel_transaction)
                .setMessage(message)
                .create();
            dialog.show();
        } catch (Exception e) {
            Logger.e(ShopeePayPaymentActivity.class.getSimpleName(), "showDialog:" + e.getMessage());
        }
    }

    private boolean isProductionBuild() {
        return presenter.isProductionBuild();
    }

    private boolean isTablet() {
        return presenter.isTablet();
    }

}
