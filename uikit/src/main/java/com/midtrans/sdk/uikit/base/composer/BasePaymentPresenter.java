package com.midtrans.sdk.uikit.base.composer;

import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.base.enums.Currency;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.mandatory.TransactionDetails;
import com.midtrans.sdk.corekit.core.api.snap.model.payment.PaymentStatusResponse;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.base.contract.BaseContract;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;

public class BasePaymentPresenter<V extends BaseContract> extends BasePresenter<V> {

    private final String BACK_BUTTON_NAME = "Back";
    protected PaymentResponse paymentResponse;
    private String currency;

    public BasePaymentPresenter() {
        super();
    }

    /**
     * check for showing status page
     *
     * @return boolean
     */
    public boolean isShowPaymentStatusPage() {
        if (MidtransKit.getInstance() == null) {
            return false;
        } else {
            return MidtransKit.getInstance().getMidtransKitConfig().isShowPaymentStatus();
        }
    }

    public PaymentResponse getTransactionResponse() {
        return paymentResponse;
    }

    public String getCurrency() {
        String currency = Currency.IDR;
        TransactionDetails transactionDetails = getMidtransSdk().getCheckoutTransaction().getTransactionDetails();
        if (transactionDetails != null) {
            currency = transactionDetails.getCurrency();
        }

        return currency;
    }

    public MidtransSdk getMidtransSdk() {
        return MidtransSdk.getInstance();
    }

    public MidtransKit getMidtransKit() {
        return MidtransKit.getInstance();
    }

    protected PaymentResponse convertTransactionStatus(PaymentStatusResponse response) {
        return PaymentResponse
                .builder(
                        response.getStatusCode(),
                        response.getStatusMessage(),
                        response.getTransactionId(),
                        response.getOrderId(),
                        response.getGrossAmount(),
                        response.getPaymentType(),
                        response.getTransactionTime(),
                        response.getTransactionStatus()
                )
                .build();
    }
}