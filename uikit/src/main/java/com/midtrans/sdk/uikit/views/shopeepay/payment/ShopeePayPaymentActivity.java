package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewStub;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

public class ShopeePayPaymentActivity extends BasePaymentActivity implements ShopeePayPaymentView {

    private ShopeePayPaymentPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressLayout();
        setContentView(R.layout.activity_shopeepay_payment);
        initPresenter();
        presenter.setTabletDevice(this);
        presenter.setShopeeInstalled(this);
        initLayout();
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

    @Override
    public void bindViews() {

    }

    @Override
    public void initTheme() {

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
