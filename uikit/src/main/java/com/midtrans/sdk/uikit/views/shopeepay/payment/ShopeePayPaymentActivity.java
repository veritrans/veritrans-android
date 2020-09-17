package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;

public class ShopeePayPaymentActivity extends BasePaymentActivity implements ShopeePayPaymentView {

    private ShopeePayPaymentPresenter presenter;
    private FancyButton buttonPrimary;
    private FancyButton buttonDownload;
    private View buttonPrimaryLayout;
    private Boolean isAlreadyGotResponse, isShopeeInstalledWhenPaused;
    private int shopeePayIntentCode;

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
        if (BuildConfig.FLAVOR.equals(UiKitConstants.ENVIRONMENT_PRODUCTION)){
            presenter.setShopeeInstalled(this);
            if (isShopeeInstalledWhenPaused != null && isShopeeInstalledWhenPaused != presenter.getShopeeInstalled()) {
                recreate();
            }
        }

        if (shopeePayIntentCode == UiKitConstants.INTENT_CODE_GOPAY && presenter != null) {
            presenter.getPaymentStatus();
        }
    }

    @Override
    protected void onPause() {
        if(BuildConfig.FLAVOR.equals(UiKitConstants.ENVIRONMENT_PRODUCTION)){
            isShopeeInstalledWhenPaused = presenter.getShopeeInstalled();
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

    private void initPresenter() {
        presenter = new ShopeePayPaymentPresenter(this);
        presenter.setTabletDevice(this);
        presenter.setShopeeInstalled(this);
    }

    private void initLayout() {
        ViewStub stub = findViewById(R.id.shopee_layout_stub);
        if (presenter.getTabletDevice()) {
            stub.setLayoutResource(R.layout.layout_shopeepay_payment_tablet);
        } else {
            if (BuildConfig.FLAVOR.equals(UiKitConstants.ENVIRONMENT_PRODUCTION)) {
                stub.setLayoutResource(
                    presenter.getShopeeInstalled() ? R.layout.layout_shopeepay_payment
                        : R.layout.layout_install_shopeepay);
            } else {
                stub.setLayoutResource(R.layout.layout_shopeepay_payment);
            }
        }
    }

    private void initData() {
        setPageTitle(getString(R.string.shopeepay));
    }

    private void initActionButton() {
        if (BuildConfig.FLAVOR.equals(UiKitConstants.ENVIRONMENT_PRODUCTION)) {
            if (presenter.getShopeeInstalled() || presenter.getTabletDevice()) {
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
                buttonPrimary.setText(getString(R.string.gopay_confirm_button));

                buttonPrimary.setIconResource(R.drawable.ic_gopay_white);
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

                buttonDownload = findViewById(R.id.button_download_shopee);
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
            buttonPrimary.setText(getString(R.string.gopay_confirm_button));

            buttonPrimary.setIconResource(R.drawable.ic_gopay_white);
            buttonPrimary.setIconPosition(FancyButton.POSITION_RIGHT);
        }
    }

    private void openDeeplink(String deeplinkUrl) {
        if (deeplinkUrl == null) {
            Toast.makeText(this, R.string.gopay_payment_cant_open_deeplink, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.redirecting_to_gopay), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl));
            startActivityForResult(intent, UiKitConstants.INTENT_CODE_GOPAY);
        }
    }

    private void startShopeePayPayment() {
        showProgressLayout();
        presenter.startShopeePayPayment();
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

    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {

    }

    @Override
    public void onPaymentError(Throwable error) {

    }
}
