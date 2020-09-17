package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressLayout();
        setContentView(R.layout.activity_shopeepay_payment);
        initPresenter();
        initLayout();
        initData();
        initActionButton();
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
                stub.setLayoutResource(presenter.getShopeeInstalled() ? R.layout.layout_shopeepay_payment : R.layout.layout_install_shopeepay);
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
                        //todo
                        //to prevent "payment has been paid"
//                        if (isAlreadyGotResponse) {
//                            openDeeplink(presenter.getTransactionResponse().getDeeplinkUrl());
//                        } else {
//                            startGoPayPayment();
//                        }
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
                RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                    //TODO
                    //to prevent "payment has been paid"
//                    if (isAlreadyGotResponse) {
//                        openDeeplink(presenter.getTransactionResponse().getDeeplinkUrl());
//                    } else {
//                        startGoPayPayment();
//                    }
                }
            });
            buttonPrimary.setTextBold();
            buttonPrimary.setText(getString(R.string.gopay_confirm_button));

            buttonPrimary.setIconResource(R.drawable.ic_gopay_white);
            buttonPrimary.setIconPosition(FancyButton.POSITION_RIGHT);
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

    }

    @Override
    public void onGetTransactionStatusFailure(TransactionResponse transactionResponse) {

    }

    @Override
    public void onGetTransactionStatusSuccess(TransactionResponse transactionResponse) {

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
