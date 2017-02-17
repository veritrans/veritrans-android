package com.midtrans.sdk.core.models.snap.bank;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;

/**
 * Created by rakawm on 1/24/17.
 */

public class BankTransferPaymentResponse extends BaseTransactionResponse {
    public final String pdfUrl;

    public BankTransferPaymentResponse(String statusCode,
                                       String statusMessage,
                                       String transactionId,
                                       String orderId,
                                       String grossAmount,
                                       String paymentType,
                                       String transactionTime,
                                       String transactionStatus,
                                       String fraudStatus,
                                       String finishRedirectUrl,
                                       String pdfUrl) {
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
        this.pdfUrl = pdfUrl;
    }
}
