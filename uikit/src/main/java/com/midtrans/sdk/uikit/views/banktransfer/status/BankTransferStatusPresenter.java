package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.VaNumber;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

import java.util.Map;

/**
 * Created by ziahaqi on 8/15/17.
 */

public class BankTransferStatusPresenter extends BasePaymentPresenter {

    private static final String LABEL_BANK_CODE_BNI = "009 (Bank BNI)";
    private static final String LABEL_BANK_CODE_BRI = "002 (Bank BRI)";
    private static final String LABEL_BANK_CODE_PERMATA = "013 (Bank Permata)";

    private static final String BNI = "bni";
    private static final String BRI = "bri";
    private static final String PERMATA = "permata";

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
                    vaNumber = getOtherBankVANumber();
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
                    expiration = getOtherBankVAExpiration();
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

        String bankCode = "";
        String bank = getOtherVaProcessor();

        switch (bank) {
            case BRI:
                bankCode = LABEL_BANK_CODE_BRI;
                break;
            case BNI:
                bankCode = LABEL_BANK_CODE_BNI;
                break;
            case PERMATA:
                bankCode = LABEL_BANK_CODE_PERMATA;
                break;
        }

        return bankCode;
    }

    private String getOtherBankVAExpiration() {

        String expiration = "";
        String otherVaProcessor = getOtherVaProcessor();

        switch (otherVaProcessor) {
            case BRI:
                expiration = transactionResponse.getBriExpiration();
                break;
            case BNI:
                expiration = transactionResponse.getBniExpiration();
                break;
            case PERMATA:
                expiration = transactionResponse.getPermataExpiration();
                break;
        }

        return expiration;
    }

    private String getOtherBankVANumber() {

        String vaNumber = "";
        String otherVaProcessor = getOtherVaProcessor();

        switch (otherVaProcessor) {
            case BRI:
                vaNumber = transactionResponse.getBriVaNumber();
                break;
            case BNI:
                vaNumber = transactionResponse.getBniVaNumber();
                break;
            case PERMATA:
                vaNumber = transactionResponse.getPermataVANumber();
                break;
        }

        return vaNumber;
    }

    private String getOtherVaProcessor() {
        return transactionResponse.getAccountNumbers().get(0).getBank();
    }
}
