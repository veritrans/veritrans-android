package com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;

/**
 * Created by rakawm on 1/25/17.
 */

public class MandiriClickpayPaymentResponse extends BaseTransactionResponse {
    public final String approvalCode;
    public final String maskedCard;

    public MandiriClickpayPaymentResponse(String statusCode,
                                          String statusMessage,
                                          String transactionId,
                                          String orderId,
                                          String grossAmount,
                                          String paymentType,
                                          String transactionTime,
                                          String transactionStatus,
                                          String fraudStatus,
                                          String finishRedirectUrl,
                                          String approvalCode,
                                          String maskedCard) {
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
        this.maskedCard = maskedCard;
    }
}
