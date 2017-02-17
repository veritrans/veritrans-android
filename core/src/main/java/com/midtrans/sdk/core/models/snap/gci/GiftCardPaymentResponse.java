package com.midtrans.sdk.core.models.snap.gci;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;

/**
 * Created by rakawm on 1/26/17.
 */

public class GiftCardPaymentResponse extends BaseTransactionResponse {
    public final String approvalCode;

    public GiftCardPaymentResponse(String statusCode,
                                   String statusMessage,
                                   String transactionId,
                                   String orderId,
                                   String grossAmount,
                                   String paymentType,
                                   String transactionTime,
                                   String transactionStatus,
                                   String fraudStatus,
                                   String finishRedirectUrl,
                                   String approvalCode) {
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
        this.approvalCode = approvalCode;
    }
}
