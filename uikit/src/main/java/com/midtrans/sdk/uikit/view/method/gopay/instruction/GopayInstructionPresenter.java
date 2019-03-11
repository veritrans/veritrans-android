package com.midtrans.sdk.uikit.view.method.gopay.instruction;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.payment.PaymentStatusResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.EWalletCharge;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.Constants;

public class GopayInstructionPresenter extends BasePaymentPresenter<GopayInstructionContract> {

    private PaymentInfoResponse paymentInfoResponse;

    GopayInstructionPresenter(GopayInstructionContract view, PaymentInfoResponse paymentInfoResponse) {
        super();
        this.view = view;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    void startGopayEwalletPayment(String token) {
        EWalletCharge.paymentUsingGopay(token, new MidtransCallback<GopayResponse>() {
            @Override
            public void onSuccess(GopayResponse data) {
                view.onPaymentSuccess(data);
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onPaymentError(throwable);
            }
        });
    }

    void getPaymentStatus(String token) {
        MidtransSdk.getInstance().getPaymentStatus(token, new MidtransCallback<PaymentStatusResponse>() {
            @Override
            public void onSuccess(PaymentStatusResponse data) {
                if (data != null && !isPaymentPending(data)) {
                    view.onGetPaymentStatusSuccess(data);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onGetPaymentStatusError(throwable);
            }
        });
    }

    private boolean isPaymentPending(PaymentStatusResponse response) {
        String statusCode = response.getStatusCode();
        String transactionStatus = response.getTransactionStatus();

        return (!TextUtils.isEmpty(statusCode) && statusCode.equals(Constants.STATUS_CODE_201)
                || !TextUtils.isEmpty(transactionStatus) && transactionStatus.equalsIgnoreCase(Constants.STATUS_PENDING));
    }
}