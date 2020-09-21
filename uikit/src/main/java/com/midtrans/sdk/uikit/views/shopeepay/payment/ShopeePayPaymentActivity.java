package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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
    private View buttonPrimaryLayout;
    private Boolean isAlreadyGotResponse, isShopeeInstalledWhenPaused;
    private int shopeePayIntentCode, attempt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressLayout();
        setContentView(R.layout.activity_shopeepay_payment);
        initPresenter();
        initLayout();
        initData();
        initActionButton();
        hideProgressLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isProductionBuild()){
            presenter.setShopeeInstalled(this);
            if (isShopeeInstalledWhenPaused != null && isShopeeInstalledWhenPaused != isShopeeInstalled()) {
                recreate();
            }
        }

        if (shopeePayIntentCode == UiKitConstants.INTENT_CODE_GOPAY && presenter != null) {
            presenter.getPaymentStatus();
        }
    }

    @Override
    protected void onPause() {
        if(isProductionBuild()){
            isShopeeInstalledWhenPaused = isShopeeInstalled();
        }
        super.onPause();
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
            showConfirmationDialog(getString(R.string.confirm_gopay_deeplink));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void bindViews() {
        buttonPrimary = findViewById(R.id.button_primary);
        buttonPrimaryLayout = findViewById(R.id.layout_primary_button);
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
            SdkUIFlowUtil.showToast(ShopeePayPaymentActivity.this, getString(R.string.error_shopeepay_transaction));
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
        presenter.setShopeeInstalled(this);
    }

    private void initLayout() {
        ViewStub stub = findViewById(R.id.shopee_layout_stub);
        if (isTablet()) {
            stub.setLayoutResource(R.layout.layout_shopeepay_payment_tablet);
        } else {
            if (isProductionBuild()) {
                stub.setLayoutResource(isShopeeInstalled() ? R.layout.layout_shopeepay_payment : R.layout.layout_install_shopeepay);
            } else {
                stub.setLayoutResource(R.layout.layout_shopeepay_payment);
            }
        }
        stub.inflate();
    }

    private void initData() {
        setPageTitle(getString(R.string.shopeepay));
    }

    private void initActionButton() {
        if (isProductionBuild()) {
            if (isShopeeInstalled() || isTablet()) {
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
                buttonPrimary.setText(getString(R.string.shopeepay_confirm_button));

                buttonPrimary.setIconResource(R.drawable.ic_gopay_white);//TODO check icon
                buttonPrimary.setIconPosition(FancyButton.POSITION_RIGHT);
            } else {
                //hide confirm button and adjust item details to bottom of screen
                buttonPrimaryLayout.setVisibility(View.GONE);
                findViewById(R.id.primary_button_separator).setVisibility(View.GONE);
                View itemDetail = findViewById(R.id.container_item_details);
                RelativeLayout.LayoutParams layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                itemDetail.setLayoutParams(layoutParams);

                FancyButton buttonDownload = findViewById(R.id.button_download_shopee);
                setTextColor(buttonDownload);
                setIconColorFilter(buttonDownload);
                buttonDownload.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.openShopeeInPlayStore(ShopeePayPaymentActivity.this);
                    }
                });
            }
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
            buttonPrimary.setText(getString(R.string.shopeepay_confirm_button));

            buttonPrimary.setIconResource(R.drawable.ic_gopay_white);
            buttonPrimary.setIconPosition(FancyButton.POSITION_RIGHT);
        }
    }

    private void openDeeplink(String deeplinkUrl) {
        if (deeplinkUrl == null) {
            Toast.makeText(this, R.string.gopay_payment_cant_open_deeplink, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.redirecting_to_shopee), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl));
            startActivityForResult(intent, UiKitConstants.INTENT_CODE_GOPAY);
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
                && (!TextUtils.isEmpty(response.getQrCodeUrl()) || !isTablet());
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

    private Boolean isProductionBuild() {
        return presenter.isProductionBuild();
    }

    private Boolean isTablet() {
        return presenter.isTablet();
    }

    private Boolean isShopeeInstalled() {
        return presenter.isShopeeInstalled();
    }
}
