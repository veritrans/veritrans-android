package com.midtrans.sdk.core.models.snap.bank.bca;

import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.VaNumber;

import java.util.List;

/**
 * Created by rakawm on 1/24/17.
 */

public class BcaBankTransferPaymentResponse extends BankTransferPaymentResponse {

    public final String bcaVaNumber;
    public final String bcaExpiration;

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
                                          String pdfUrl,
                                          List<VaNumber> vaNumbers) {
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
        this.bcaVaNumber = bcaVaNumber;
        this.bcaExpiration = bcaExpiration;
    }
}
