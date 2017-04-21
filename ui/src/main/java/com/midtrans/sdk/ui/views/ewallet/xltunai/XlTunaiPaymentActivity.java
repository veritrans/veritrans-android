package com.midtrans.sdk.ui.views.ewallet.xltunai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BasePaymentActivity;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;

/**
 * Created by ziahaqi on 4/7/17.
 */

public class XlTunaiPaymentActivity extends BasePaymentActivity implements XLTunaiPaymentView {
    private XlTunaiPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xl_tunai);
        initPresenter();
        trackPage();
    }

    private void initPresenter() {
        presenter = new XlTunaiPresenter(this);
    }

    private void trackPage() {
        presenter.trackEvent(AnalyticsEventName.PAGE_XL_TUNAI);
    }

    private void showProgressDialog(String message) {
        UiUtils.showProgressDialog(this, message, false);
    }

    private void dismissProgressDialog() {
        UiUtils.hideProgressDialog();
    }

    @Override
    public void onPaymentError(String message) {
        dismissProgressDialog();
    }

    @Override
    public void onPaymentFailure(XlTunaiPaymentResponse response) {
        dismissProgressDialog();
        showPaymentStatus(response);
    }

    @Override
    public void onPaymentSuccess(XlTunaiPaymentResponse response) {
        dismissProgressDialog();
        showPaymentStatus(response);
    }

    private void showPaymentStatus(XlTunaiPaymentResponse response) {
        Intent intent = new Intent(this, XlTunaiStatusActivity.class);
        intent.putExtra(XlTunaiStatusActivity.EXTRA_ORDER_ID, response.xlTunaiOrderId);
        intent.putExtra(XlTunaiStatusActivity.EXTRA_MERCHANT_CODE, response.xlTunaiMerchantId);
        intent.putExtra(XlTunaiStatusActivity.EXTRA_EXPIRATION, response.xlExpiration);
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == STATUS_REQUEST_CODE) {
                finishPayment(resultCode, presenter.getPaymentResult());
            }
        }
    }

    private void finishPayment(int resultCode, PaymentResult<XlTunaiPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void startPayment() {
        showProgressDialog(getString(R.string.processing_payment));
        presenter.startPayment();
    }

    @Override
    protected boolean validatePayment() {
        return true;
    }
}
