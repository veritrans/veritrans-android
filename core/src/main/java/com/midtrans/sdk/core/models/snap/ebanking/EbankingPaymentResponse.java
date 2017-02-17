package com.midtrans.sdk.core.models.snap.ebanking;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;

/**
 * Created by rakawm on 1/24/17.
 */

public class EbankingPaymentResponse extends BaseTransactionResponse {
    public final String redirectUrl;

    public EbankingPaymentResponse(String statusCode,
                                   String statusMessage,
                                   String transactionId,
                                   String orderId,
                                   String grossAmount,
                                   String paymentType,
                                   String transactionTime,
                                   String transactionStatus,
                                   String fraudStatus,
                                   String finishRedirectUrl,
                                   String redirectUrl) {
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
        this.redirectUrl = redirectUrl;
    }
}
