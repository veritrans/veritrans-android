package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 8/15/17.
 */

public class BankTransferStatusPresenter extends BasePaymentPresenter {

    private final TransactionResponse response;
    private final String bankType;

    public BankTransferStatusPresenter(TransactionResponse response, String bankType) {
        super();
        this.bankType = bankType;
        this.response = response;
    }

    public String getVaNumber() {

        String vaNumber = "";
        if (!TextUtils.isEmpty(bankType)) {
            switch (bankType) {
                case PaymentType.BCA_VA:
                    vaNumber = response.getBcaVaNumber();
                    break;
                case PaymentType.PERMATA_VA:
                    vaNumber = response.getPermataVANumber();
                    break;
                case PaymentType.ALL_VA:
                    vaNumber = response.getPermataVANumber();
                    break;
                case PaymentType.BNI_VA:
                    vaNumber = response.getBniVaNumber();
                    break;
                case PaymentType.E_CHANNEL:
                    vaNumber = response.getPaymentCode();
                    break;
            }
        }

        return vaNumber;
    }

    public String getVaExpiration() {

        String expiration = "";
        if (!TextUtils.isEmpty(bankType) && response != null) {
            switch (bankType) {
                case PaymentType.BCA_VA:
                    expiration = response.getBcaExpiration();
                    break;
                case PaymentType.PERMATA_VA:
                    expiration = response.getPermataExpiration();
                    break;
                case PaymentType.ALL_VA:
                    expiration = response.getPermataExpiration();
                    break;
                case PaymentType.BNI_VA:
                    expiration = response.getBniExpiration();
                    break;
            }
        }

        return expiration;
    }

    public String getInstructionUrl() {
        return response.getPdfUrl();
    }

    public boolean isPaymentFailed() {

        if (response != null) {
            if (response.getTransactionStatus().equals(UiKitConstants.STATUS_PENDING)
                    || response.getStatusCode().equals(UiKitConstants.STATUS_CODE_201)) {
                return false;
            }
        }
        return true;
    }


    public String getBankType() {
        return bankType;
    }

    public String getCompanyCode() {
        return (response == null || TextUtils.isEmpty(response.getCompanyCode()) ? "" : response.getCompanyCode());
    }

    public String getMandiriBillExpiration() {
        return (response == null || TextUtils.isEmpty(response.getMandiriBillExpiration()) ? "" : response.getMandiriBillExpiration());
    }
}
