package com.midtrans.sdk.core.models.snap.ewallet.tcash;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;

/**
 * Created by rakawm on 1/26/17.
 */

public class TelkomselCashPaymentResponse extends BaseTransactionResponse {

    public TelkomselCashPaymentResponse(String statusCode,
                                        String statusMessage,
                                        String transactionId,
                                        String orderId,
                                        String grossAmount,
                                        String paymentType,
                                        String transactionTime,
                                        String transactionStatus,
                                        String fraudStatus,
                                        String finishRedirectUrl) {
        super(statusCode,
                statusMessage,
                transactionId,
                orderId,
                grossAmount,
                paymentType,
                transactionTime,
                transactionStatus,
                fraudStatus,
                finishRedirectUrl);
    }
}
