package com.midtrans.sdk.core.models.snap.bank.bca;

import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentResponse;

import java.util.List;

/**
 * Created by rakawm on 1/24/17.
 */

public class BcaBankTransferPaymentResponse extends BankTransferPaymentResponse {

    public final String bcaVaNumber;
    public final String bcaExpiration;
    public final List<BcaVaNumber> vaNumbers;

    public BcaBankTransferPaymentResponse(String statusCode,
                                          String statusMessage,
                                          String transactionId,
                                          String orderId,
                                          String grossAmount,
                                          String paymentType,
                                          String transactionTime,
                                          String transactionStatus,
                                          String fraudStatus,
                                          String finishRedirectUrl,
                                          String bcaVaNumber,
                                          String bcaExpiration,
                                          List<BcaVaNumber> vaNumbers,
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
                finishRedirectUrl,
                pdfUrl);
        this.bcaVaNumber = bcaVaNumber;
        this.bcaExpiration = bcaExpiration;
        this.vaNumbers = vaNumbers;
    }
}
