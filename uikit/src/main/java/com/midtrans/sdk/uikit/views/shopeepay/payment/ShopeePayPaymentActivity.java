package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;

public class ShopeePayPaymentActivity extends BasePaymentActivity implements ShopeePayPaymentView {

    private Boolean isTablet;
    private ShopeePayPaymentPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressLayout();
        isTablet = presenter.checkTabletDevice(this);

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
