package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.MerchantPreferences;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 8/15/17.
 */

public class BankTransferStatusPresenter extends BasePaymentPresenter {

    private static final String LABEL_BANK_CODE_BNI = "009 (Bank BNI)";
    private static final String LABEL_BANK_CODE_BRI = "002 (Bank BRI)";
    private static final String LABEL_BANK_CODE_PERMATA = "013 (Bank Permata)";

    private static final String BNI = "bni";
    private static final String BRI = "bri";

    private final String bankType;

    public BankTransferStatusPresenter(TransactionResponse response, String bankType) {
        super();
        this.bankType = bankType;
        this.transactionResponse = response;
    }

    public String getVaNumber() {

        String vaNumber = "";
        if (!TextUtils.isEmpty(bankType) && transactionResponse != null) {
            switch (bankType) {
                case PaymentType.BCA_VA:
                    vaNumber = transactionResponse.getBcaVaNumber();
                    break;
                case PaymentType.PERMATA_VA:
                    vaNumber = transactionResponse.getPermataVANumber();
                    break;
                case PaymentType.ALL_VA:
                    String bank = transactionResponse.getAccountNumbers().get(0).getBank();

                    if (bank.equals(BRI)) {
                        vaNumber = transactionResponse.getBriVaNumber();
                    } else if (bank.equals(BNI)) {
                        vaNumber = transactionResponse.getBniVaNumber();
                    } else {
                        vaNumber = transactionResponse.getPermataVANumber();
                    }
                    break;
                case PaymentType.BNI_VA:
                    vaNumber = transactionResponse.getBniVaNumber();
                    break;
                case PaymentType.BRI_VA:
                    vaNumber = transactionResponse.getBriVaNumber();
                    break;
                case PaymentType.E_CHANNEL:
                    vaNumber = transactionResponse.getPaymentCode();
                    break;
            }
        }

        return vaNumber;
    }

    public String getVaExpiration() {

        String expiration = "";
        if (!TextUtils.isEmpty(bankType) && transactionResponse != null) {
            switch (bankType) {
                case PaymentType.BCA_VA:
                    expiration = transactionResponse.getBcaExpiration();
                    break;
                case PaymentType.PERMATA_VA:
                    expiration = transactionResponse.getPermataExpiration();
                    break;
                case PaymentType.ALL_VA:
                    //expiration is based on other VA processor
                    expiration = TextUtils.isEmpty(transactionResponse.getBniExpiration()) ? transactionResponse.getPermataExpiration() : transactionResponse.getBniExpiration();
                    break;
                case PaymentType.BNI_VA:
                    expiration = transactionResponse.getBniExpiration();
                    break;
                case PaymentType.BRI_VA:
                    expiration = transactionResponse.getBriExpiration();
                    break;
            }
        }

        return expiration;
    }

    public String getInstructionUrl() {
        return transactionResponse == null ? "" : transactionResponse.getPdfUrl();
    }

    public boolean isPaymentFailed() {

        if (transactionResponse != null) {
            if (transactionResponse.getTransactionStatus().equals(UiKitConstants.STATUS_PENDING)
                    || transactionResponse.getStatusCode().equals(UiKitConstants.STATUS_CODE_201)) {
                return false;
            }
        }
        return true;
    }


    public String getBankType() {
        return bankType;
    }

    public String getCompanyCode() {
        return (transactionResponse == null || TextUtils.isEmpty(transactionResponse.getCompanyCode()) ? "" : transactionResponse.getCompanyCode());
    }

    public String getMandiriBillExpiration() {
        return (transactionResponse == null || TextUtils.isEmpty(transactionResponse.getMandiriBillExpiration()) ? "" : transactionResponse.getMandiriBillExpiration());
    }

    public String getBankCode() {
        String bankCode;
        String bank = transactionResponse.getAccountNumbers().get(0).getBank();

        if (bank.equals(BRI)) {
            bankCode = LABEL_BANK_CODE_BRI;
        } else if (bank.equals(BNI)) {
            bankCode = LABEL_BANK_CODE_BNI;
        } else {
            bankCode = LABEL_BANK_CODE_PERMATA;
        }

        return bankCode;
    }
}
