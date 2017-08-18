package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 8/15/17.
 */

public class BankTransferStatusPresenter {

    private final TransactionResponse response;
    private final String bankType;

    public BankTransferStatusPresenter(TransactionResponse response, String bankType) {
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
            }
        }

        return vaNumber;
    }

    public String getVaExpiration() {

        String expiration = "";
        if (!TextUtils.isEmpty(bankType)) {
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

    public void trackEvent(String eventName) {
        MidtransSDK.getInstance().trackEvent(eventName);
    }

    public String getBankType() {
        return bankType;
    }
}
