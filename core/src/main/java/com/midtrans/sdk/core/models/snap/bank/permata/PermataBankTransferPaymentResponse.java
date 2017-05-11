package com.midtrans.sdk.core.models.snap.bank.permata;

import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.VaNumber;

import java.util.List;

/**
 * Created by rakawm on 1/24/17.
 */

public class PermataBankTransferPaymentResponse extends BankTransferPaymentResponse {
    public final String permataVaNumber;
    public final String permataExpiration;

    public PermataBankTransferPaymentResponse(String statusCode,
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
                                              List<VaNumber> vaNumbers,
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
                vaNumbers);
        this.permataVaNumber = permataVaNumber;
        this.permataExpiration = permataExpiration;
    }
}
