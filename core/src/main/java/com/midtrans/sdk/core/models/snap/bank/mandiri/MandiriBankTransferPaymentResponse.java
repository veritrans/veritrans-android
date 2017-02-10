package com.midtrans.sdk.core.models.snap.bank.mandiri;

import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentResponse;

/**
 * Created by rakawm on 1/24/17.
 */

public class MandiriBankTransferPaymentResponse extends BankTransferPaymentResponse {
    public final String billKey;
    public final String billerCode;
    public final String billpaymentExpiration;

    public MandiriBankTransferPaymentResponse(String statusCode,
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
                                              String billKey,
                                              String billerCode,
                                              String billpaymentExpiration) {
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
                pdfUrl);
        this.billKey = billKey;
        this.billerCode = billerCode;
        this.billpaymentExpiration = billpaymentExpiration;
    }
}
