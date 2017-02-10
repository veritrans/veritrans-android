package com.midtrans.sdk.core.models.snap.bank.other;

import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;

/**
 * Created by rakawm on 1/24/17.
 */

public class OtherBankTransferPaymentResponse extends PermataBankTransferPaymentResponse {

    public OtherBankTransferPaymentResponse(String statusCode,
                                            String statusMessage,
                                            String transactionId,
                                            String orderId,
                                            String grossAmount,
                                            String paymentType,
                                            String transactionTime,
                                            String transactionStatus,
                                            String fraudStatus,
                                            String finishRedirectUrl,
                                            String pdfUrl,
                                            String permataVaNumber,
                                            String permataExpiration) {
        super(statusCode,
                statusMessage,
                transactionId,
                orderId,
                grossAmount,
                paymentType,
                transactionTime,
                transactionStatus,
                fraudStatus,
                finishRedirectUrl,
                pdfUrl,
                permataVaNumber,
                permataExpiration);
    }
}
