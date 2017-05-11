package com.midtrans.sdk.core.models.snap.bank.bni;

import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.VaNumber;

import java.util.List;

/**
 * Created by rakawm on 5/11/17.
 */

public class BniBankTransferPaymentResponse extends BankTransferPaymentResponse {
    public final String bniVaNumber;
    public final String bniExpiration;

    public BniBankTransferPaymentResponse(String statusCode,
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
                                          List<VaNumber> vaNumbers, String bniVaNumber, String bniExpiration) {
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
                vaNumbers);
        this.bniVaNumber = bniVaNumber;
        this.bniExpiration = bniExpiration;
    }
}
