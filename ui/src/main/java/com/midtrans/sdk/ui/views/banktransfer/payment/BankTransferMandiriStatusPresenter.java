package com.midtrans.sdk.ui.views.banktransfer.payment;

import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;

/**
 * Created by rakawm on 4/13/17.
 */

public class BankTransferMandiriStatusPresenter extends BasePaymentPresenter {
    private final MandiriBankTransferPaymentResponse response;

    public BankTransferMandiriStatusPresenter(MandiriBankTransferPaymentResponse response) {
        this.response = response;
    }

    public MandiriBankTransferPaymentResponse getResponse() {
        return response;
    }

    public String getCompanyCode() {
        return response.billerCode;
    }

    public String getPaymentCode() {
        return response.billKey;
    }

    public String getExpiration() {
        return response.billpaymentExpiration;
    }

    public String getDownloadUrl() {
        return response.pdfUrl;
    }
}
