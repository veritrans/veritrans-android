package com.midtrans.sdk.ui.views.banktransfer.payment;

import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.PaymentType;

/**
 * Created by rakawm on 4/13/17.
 */

public class BankTransferPaymentStatusPresenter extends BasePaymentPresenter {

    private final BankTransferPaymentResponse response;
    private final String bank;

    public BankTransferPaymentStatusPresenter(BankTransferPaymentResponse response, String bank) {
        this.response = response;
        this.bank = bank;
    }

    public BankTransferPaymentResponse getResponse() {
        return response;
    }

    public String getBank() {
        return bank;
    }

    public String getVirtualAccountNumber() {
        switch (bank) {
            case PaymentType.BCA_VA:
                BcaBankTransferPaymentResponse bcaBankTransferPaymentResponse = (BcaBankTransferPaymentResponse) response;
                return bcaBankTransferPaymentResponse.bcaVaNumber;
            case PaymentType.PERMATA_VA:
                PermataBankTransferPaymentResponse permataBankTransferPaymentResponse = (PermataBankTransferPaymentResponse) response;
                return permataBankTransferPaymentResponse.permataVaNumber;
            case PaymentType.OTHER_VA:
                OtherBankTransferPaymentResponse otherBankTransferPaymentResponse = (OtherBankTransferPaymentResponse) response;
                return otherBankTransferPaymentResponse.permataVaNumber;
            case PaymentType.BNI_VA:
                return null;
            default:
                return null;
        }
    }

    public String getExpirationText() {
        switch (bank) {
            case PaymentType.BCA_VA:
                BcaBankTransferPaymentResponse bcaBankTransferPaymentResponse = (BcaBankTransferPaymentResponse) response;
                return bcaBankTransferPaymentResponse.bcaExpiration;
            case PaymentType.PERMATA_VA:
                PermataBankTransferPaymentResponse permataBankTransferPaymentResponse = (PermataBankTransferPaymentResponse) response;
                return permataBankTransferPaymentResponse.permataExpiration;
            case PaymentType.OTHER_VA:
                OtherBankTransferPaymentResponse otherBankTransferPaymentResponse = (OtherBankTransferPaymentResponse) response;
                return otherBankTransferPaymentResponse.permataExpiration;
            case PaymentType.BNI_VA:
                return null;
            default:
                return null;
        }
    }
}
