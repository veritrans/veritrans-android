package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;

/**
 * Created by ziahaqi on 8/15/17.
 */

public class BankTransferStatusPresenter {

    private final TransactionResponse response;

    public BankTransferStatusPresenter(TransactionResponse response) {
        this.response = response;
    }

    public String getVaNumber(String bankType) {

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

    public String getVaExpiration(String bankType) {

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
}
