package com.midtrans.sdk.uikit.view.banktransfer.result;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BniBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriBillResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.OtherBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PermataBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantPreferences;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.Constants;

public class BankTransferResultPresenter extends BasePaymentPresenter {

    private static final String LABEL_BANK_CODE_BNI = "009 (Bank BNI)";
    private static final String LABEL_BANK_CODE_PERMATA = "013 (Bank Permata)";
    private static volatile BankTransferResultPresenter INSTANCE = null;
    private final String bankType;

    private BcaBankTransferReponse bcaResponse;
    private BniBankTransferResponse bniResponse;
    private PermataBankTransferResponse permataResponse;
    private MandiriBillResponse mandiriResponse;
    private OtherBankTransferResponse otherResponse;
    private PaymentInfoResponse paymentInfoResponse;

    BankTransferResultPresenter(
            String bankType,
            BcaBankTransferReponse bcaResponse,
            BniBankTransferResponse bniResponse,
            PermataBankTransferResponse permataResponse,
            MandiriBillResponse mandiriResponse,
            OtherBankTransferResponse otherResponse,
            PaymentInfoResponse paymentInfoResponse
    ) {
        this.bankType = bankType;
        this.bcaResponse = bcaResponse;
        this.bniResponse = bniResponse;
        this.permataResponse = permataResponse;
        this.mandiriResponse = mandiriResponse;
        this.otherResponse = otherResponse;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    public static Builder builder(@PaymentType String paymentType, PaymentInfoResponse paymentInfoResponse) {
        return new Builder(paymentType, paymentInfoResponse);
    }

    public static BankTransferResultPresenter getInstance() {
        return INSTANCE;
    }

    public BcaBankTransferReponse getBcaResponse() {
        return bcaResponse;
    }

    public BniBankTransferResponse getBniResponse() {
        return bniResponse;
    }

    public PermataBankTransferResponse getPermataResponse() {
        return permataResponse;
    }

    public MandiriBillResponse getMandiriResponse() {
        return mandiriResponse;
    }

    public OtherBankTransferResponse getOtherResponse() {
        return otherResponse;
    }

    public PaymentInfoResponse getPaymentInfoResponse() {
        return paymentInfoResponse;
    }

    public String getVaNumber() {
        String vaNumber = "";
        if (!TextUtils.isEmpty(bankType) && bcaResponse != null) {
            vaNumber = bcaResponse.getBcaVaNumber();
        } else if (!TextUtils.isEmpty(bankType) && permataResponse != null) {
            vaNumber = permataResponse.getPermataVaNumber();
        } else if (!TextUtils.isEmpty(bankType) && bniResponse != null) {
            vaNumber = bniResponse.getBniVaNumber();
        } else if (!TextUtils.isEmpty(bankType) && mandiriResponse != null) {
            vaNumber = mandiriResponse.getBillKey();
        } else {
            vaNumber = TextUtils.isEmpty(otherResponse.getBniVaNumber()) ? otherResponse.getPermataVaNumber() : otherResponse.getBniVaNumber();
        }
        return vaNumber;
    }

    public String getVaExpiration() {
        String expiration = "";
        if (!TextUtils.isEmpty(bankType) && bcaResponse != null) {
            expiration = bcaResponse.getBcaExpiration();
        } else if (!TextUtils.isEmpty(bankType) && permataResponse != null) {
            expiration = permataResponse.getPermataExpiration();
        } else if (!TextUtils.isEmpty(bankType) && bniResponse != null) {
            expiration = bniResponse.getBniExpiration();
        } else if (!TextUtils.isEmpty(bankType) && mandiriResponse != null) {
            expiration = mandiriResponse.getBillPaymentExpiration();
        } else {
            expiration = TextUtils.isEmpty(otherResponse.getBniExpiration()) ? otherResponse.getPermataExpiration() : otherResponse.getBniExpiration();
        }
        return expiration;
    }

    public String getInstructionUrl() {
        String instructionUrl;
        if (!TextUtils.isEmpty(bankType) && bcaResponse != null) {
            instructionUrl = bcaResponse.getPdfUrl();
        } else if (!TextUtils.isEmpty(bankType) && permataResponse != null) {
            instructionUrl = permataResponse.getPdfUrl();
        } else if (!TextUtils.isEmpty(bankType) && bniResponse != null) {
            instructionUrl = bniResponse.getPdfUrl();
        } else if (!TextUtils.isEmpty(bankType) && mandiriResponse != null) {
            instructionUrl = mandiriResponse.getPdfUrl();
        } else {
            instructionUrl = TextUtils.isEmpty(otherResponse.getPdfUrl()) ? otherResponse.getPdfUrl() : otherResponse.getPdfUrl();
        }
        return instructionUrl == null ? "" : instructionUrl;
    }

    public boolean isPaymentFailed() {
        boolean status;
        if (!TextUtils.isEmpty(bankType) && bcaResponse != null) {
            status = !bcaResponse.getTransactionStatus().equals(Constants.STATUS_PENDING)
                    && !bcaResponse.getStatusCode().equals(Constants.STATUS_CODE_201);
        } else if (!TextUtils.isEmpty(bankType) && permataResponse != null) {
            status = !permataResponse.getTransactionStatus().equals(Constants.STATUS_PENDING)
                    && !permataResponse.getStatusCode().equals(Constants.STATUS_CODE_201);
        } else if (!TextUtils.isEmpty(bankType) && bniResponse != null) {
            status = !bniResponse.getTransactionStatus().equals(Constants.STATUS_PENDING)
                    && !bniResponse.getStatusCode().equals(Constants.STATUS_CODE_201);
        } else if (!TextUtils.isEmpty(bankType) && mandiriResponse != null) {
            status = !mandiriResponse.getTransactionStatus().equals(Constants.STATUS_PENDING)
                    && !mandiriResponse.getStatusCode().equals(Constants.STATUS_CODE_201);
        } else {
            String transactionStatus = TextUtils.isEmpty(otherResponse.getTransactionStatus()) ?
                    otherResponse.getTransactionStatus() : otherResponse.getTransactionStatus();
            String transactionCode = TextUtils.isEmpty(otherResponse.getStatusCode()) ?
                    otherResponse.getStatusCode() : otherResponse.getStatusCode();
            status = !transactionStatus.equals(Constants.STATUS_PENDING)
                    && !transactionCode.equals(Constants.STATUS_CODE_201);
        }
        return status;
    }

    public String getBankType() {
        return bankType;
    }

    public String getCompanyCode() {
        return (mandiriResponse == null || TextUtils.isEmpty(mandiriResponse.getBillerCode()) ? "" : mandiriResponse.getBillerCode());
    }

    public String getBankCode() {
        String bankCode = LABEL_BANK_CODE_BNI;

        MerchantPreferences preferences = paymentInfoResponse.getMerchantData().getPreference();
        if (preferences != null && !TextUtils.isEmpty(preferences.getOtherVaProcessor())
                && preferences.getOtherVaProcessor().equals(Constants.OTHER_VA_PROCESSOR_PERMATA)) {
            bankCode = LABEL_BANK_CODE_PERMATA;
        }
        return bankCode;
    }

    public static final class Builder {
        private String bankType;
        private BcaBankTransferReponse bcaResponse;
        private BniBankTransferResponse bniResponse;
        private PermataBankTransferResponse permataResponse;
        private MandiriBillResponse mandiriResponse;
        private OtherBankTransferResponse otherResponse;
        private PaymentInfoResponse paymentInfoResponse;

        private Builder(@PaymentType String paymentType, PaymentInfoResponse paymentInfoResponse) {
            this.bankType = paymentType;
            this.paymentInfoResponse = paymentInfoResponse;
        }

        public Builder setBcaResponse(BcaBankTransferReponse bcaResponse) {
            this.bcaResponse = bcaResponse;
            return this;
        }

        public Builder setBniResponse(BniBankTransferResponse bniResponse) {
            this.bniResponse = bniResponse;
            return this;
        }

        public Builder setPermataResponse(PermataBankTransferResponse permataResponse) {
            this.permataResponse = permataResponse;
            return this;
        }

        public Builder setMandiriResponse(MandiriBillResponse mandiriResponse) {
            this.mandiriResponse = mandiriResponse;
            return this;
        }

        public Builder setOtherResponse(OtherBankTransferResponse otherResponse) {
            this.otherResponse = otherResponse;
            return this;
        }

        public BankTransferResultPresenter build() {
            INSTANCE = new BankTransferResultPresenter(
                    bankType,
                    bcaResponse,
                    bniResponse,
                    permataResponse,
                    mandiriResponse,
                    otherResponse,
                    paymentInfoResponse
            );
            return INSTANCE;
        }
    }
}