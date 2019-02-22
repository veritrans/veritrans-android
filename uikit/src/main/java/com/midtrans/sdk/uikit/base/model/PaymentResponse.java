package com.midtrans.sdk.uikit.base.model;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.bcaklikpay.BcaKlikPayData;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.va.VaNumber;

import java.util.ArrayList;
import java.util.List;

public class PaymentResponse extends BasePaymentResponse {

    private static volatile PaymentResponse INSTANCE = null;

    PaymentResponse(
            String statusCode,
            String statusMessage,
            String transactionId,
            String orderId,
            String grossAmount,
            String paymentType,
            String transactionTime,
            String transactionStatus
    ) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.grossAmount = grossAmount;
        this.paymentType = paymentType;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
    }

    public static Builder builder(
            String statusCode,
            String statusMessage,
            String transactionId,
            String orderId,
            String grossAmount,
            String paymentType,
            String transactionTime,
            String transactionStatus
    ) {
        return new Builder(
                statusCode,
                statusMessage,
                transactionId,
                orderId,
                grossAmount,
                paymentType,
                transactionTime,
                transactionStatus
        );
    }

    public synchronized static PaymentResponse getInstance() {
        return INSTANCE;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    private void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public List<VaNumber> getVaNumbersList() {
        return vaNumbersList;
    }

    private void setVaNumbersList(List<VaNumber> vaNumbersList) {
        this.vaNumbersList = vaNumbersList;
    }

    public String getBcaVaNumber() {
        return bcaVaNumber;
    }

    private void setBcaVaNumber(String bcaVaNumber) {
        this.bcaVaNumber = bcaVaNumber;
    }

    public String getBcaExpiration() {
        return bcaExpiration;
    }

    private void setBcaExpiration(String bcaExpiration) {
        this.bcaExpiration = bcaExpiration;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    private void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    private void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public BcaKlikPayData getBcaKlikPayData() {
        return dataResponse;
    }

    private void setBcaKlikPayData(BcaKlikPayData bcaKlikPayData) {
        this.dataResponse = bcaKlikPayData;
    }

    public String getBniVaNumber() {
        return bniVaNumber;
    }

    private void setBniVaNumber(String bniVaNumber) {
        this.bniVaNumber = bniVaNumber;
    }

    public String getBniExpiration() {
        return bniExpiration;
    }

    private void setBniExpiration(String bniExpiration) {
        this.bniExpiration = bniExpiration;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    private void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    private void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getBank() {
        return bank;
    }

    private void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardType() {
        return cardType;
    }

    private void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    private void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getDeeplinkUrl() {
        return deeplinkUrl;
    }

    private void setDeeplinkUrl(String deeplinkUrl) {
        this.deeplinkUrl = deeplinkUrl;
    }

    public String getGopayExpiration() {
        return gopayExpiration;
    }

    private void setGopayExpiration(String gopayExpiration) {
        this.gopayExpiration = gopayExpiration;
    }

    public String getGopayExpirationRaw() {
        return gopayExpirationRaw;
    }

    private void setGopayExpirationRaw(String gopayExpirationRaw) {
        this.gopayExpirationRaw = gopayExpirationRaw;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    private void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getStore() {
        return store;
    }

    private void setStore(String store) {
        this.store = store;
    }

    public String getIndomaretExpireTime() {
        return indomaretExpireTime;
    }

    private void setIndomaretExpireTime(String indomaretExpireTime) {
        this.indomaretExpireTime = indomaretExpireTime;
    }

    public String getKlikBcaExpireTime() {
        return klikBcaExpireTime;
    }

    private void setKlikBcaExpireTime(String klikBcaExpireTime) {
        this.klikBcaExpireTime = klikBcaExpireTime;
    }

    public String getSettlementTime() {
        return settlementTime;
    }

    private void setSettlementTime(String settlementTime) {
        this.settlementTime = settlementTime;
    }

    public String getPermataVaNumber() {
        return permataVaNumber;
    }

    private void setPermataVaNumber(String permataVaNumber) {
        this.permataVaNumber = permataVaNumber;
    }

    public String getPermataExpiration() {
        return permataExpiration;
    }

    private void setPermataExpiration(String permataExpiration) {
        this.permataExpiration = permataExpiration;
    }

    public String getBillKey() {
        return billKey;
    }

    public String getBillerCode() {
        return billerCode;
    }

    public String getBillPaymentExpiration() {
        return billPaymentExpiration;
    }

    public String getAtmChannel() {
        return atmChannel;
    }

    private void setAtmChannel(String atmChannel) {
        this.atmChannel = atmChannel;
    }

    public static final class Builder {
        private String statusCode;
        private String statusMessage;
        private String redirectUrl;
        private String transactionId;
        private String orderId;
        private String grossAmount;
        private String currency;
        private String paymentType;
        private String transactionTime;
        private String transactionStatus;
        private String finishRedirectUrl;
        private String settlementTime;
        private String approvalCode;
        private BcaKlikPayData dataResponse;
        private String fraudStatus;
        private String type;
        private String pdfUrl;
        private String atmChannel;
        private String bcaVaNumber;
        private String bcaExpiration;
        private List<VaNumber> vaNumbersList;
        private String bniVaNumber;
        private String bniExpiration;
        private String permataVaNumber;
        private String permataExpiration;
        private String userId;
        private String billKey;
        private String billerCode;
        private String billPaymentExpiration;
        private ArrayList<String> validationMessages;
        private Long pointBalance;
        private String pointBalanceAmount;
        private String qrCodeUrl;
        private String deeplinkUrl;
        private String gopayExpiration;
        private String gopayExpirationRaw;
        private String paymentCode;
        private String store;
        private String indomaretExpireTime;
        private String alfamartExpireTime;
        private String klikBcaExpireTime;
        private String maskedCard;
        private String bank;
        private String cardType;

        public Builder(
                String statusCode,
                String statusMessage,
                String transactionId,
                String orderId,
                String grossAmount,
                String paymentType,
                String transactionTime,
                String transactionStatus
        ) {
            this.statusCode = statusCode;
            this.statusMessage = statusMessage;
            this.transactionId = transactionId;
            this.orderId = orderId;
            this.grossAmount = grossAmount;
            this.paymentType = paymentType;
            this.transactionTime = transactionTime;
            this.transactionStatus = transactionStatus;
        }

        public Builder setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setFinishRedirectUrl(String finishRedirectUrl) {
            this.finishRedirectUrl = finishRedirectUrl;
            return this;
        }

        public Builder setSettlementTime(String settlementTime) {
            this.settlementTime = settlementTime;
            return this;
        }

        public Builder setApprovalCode(String approvalCode) {
            this.approvalCode = approvalCode;
            return this;
        }

        public Builder setDataResponse(BcaKlikPayData dataResponse) {
            this.dataResponse = dataResponse;
            return this;
        }

        public Builder setFraudStatus(String fraudStatus) {
            this.fraudStatus = fraudStatus;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setPdfUrl(String pdfUrl) {
            this.pdfUrl = pdfUrl;
            return this;
        }

        public Builder setAtmChannel(String atmChannel) {
            this.atmChannel = atmChannel;
            return this;
        }

        public Builder setBcaVaNumber(String bcaVaNumber) {
            this.bcaVaNumber = bcaVaNumber;
            return this;
        }

        public Builder setBcaExpiration(String bcaExpiration) {
            this.bcaExpiration = bcaExpiration;
            return this;
        }

        public Builder setVaNumbersList(List<VaNumber> vaNumbersList) {
            this.vaNumbersList = vaNumbersList;
            return this;
        }

        public Builder setBniVaNumber(String bniVaNumber) {
            this.bniVaNumber = bniVaNumber;
            return this;
        }

        public Builder setBniExpiration(String bniExpiration) {
            this.bniExpiration = bniExpiration;
            return this;
        }

        public Builder setPermataVaNumber(String permataVaNumber) {
            this.permataVaNumber = permataVaNumber;
            return this;
        }

        public Builder setPermataExpiration(String permataExpiration) {
            this.permataExpiration = permataExpiration;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setBillKey(String billKey) {
            this.billKey = billKey;
            return this;
        }

        public Builder setBillerCode(String billerCode) {
            this.billerCode = billerCode;
            return this;
        }

        public Builder setBillPaymentExpiration(String billPaymentExpiration) {
            this.billPaymentExpiration = billPaymentExpiration;
            return this;
        }

        public Builder setValidationMessages(ArrayList<String> validationMessages) {
            this.validationMessages = validationMessages;
            return this;
        }

        public Builder setPointBalance(Long pointBalance) {
            this.pointBalance = pointBalance;
            return this;
        }

        public Builder setPointBalanceAmount(String pointBalanceAmount) {
            this.pointBalanceAmount = pointBalanceAmount;
            return this;
        }

        public Builder setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
            return this;
        }

        public Builder setDeeplinkUrl(String deeplinkUrl) {
            this.deeplinkUrl = deeplinkUrl;
            return this;
        }

        public Builder setGopayExpiration(String gopayExpiration) {
            this.gopayExpiration = gopayExpiration;
            return this;
        }

        public Builder setGopayExpirationRaw(String gopayExpirationRaw) {
            this.gopayExpirationRaw = gopayExpirationRaw;
            return this;
        }

        public Builder setPaymentCode(String paymentCode) {
            this.paymentCode = paymentCode;
            return this;
        }

        public Builder setStore(String store) {
            this.store = store;
            return this;
        }

        public Builder setIndomaretExpireTime(String indomaretExpireTime) {
            this.indomaretExpireTime = indomaretExpireTime;
            return this;
        }

        public Builder setAlfamartExpireTime(String alfamartExpireTime) {
            this.alfamartExpireTime = alfamartExpireTime;
            return this;
        }

        public Builder setKlikBcaExpireTime(String klikBcaExpireTime) {
            this.klikBcaExpireTime = klikBcaExpireTime;
            return this;
        }

        public Builder setMaskedCard(String maskedCard) {
            this.maskedCard = maskedCard;
            return this;
        }

        public Builder setBank(String bank) {
            this.bank = bank;
            return this;
        }

        public Builder setCardType(String cardType) {
            this.cardType = cardType;
            return this;
        }

        public PaymentResponse build() {
            INSTANCE = new PaymentResponse(
                    statusCode,
                    statusMessage,
                    transactionId,
                    orderId,
                    grossAmount,
                    paymentType,
                    transactionTime,
                    transactionStatus
            );
            INSTANCE.setRedirectUrl(redirectUrl);
            INSTANCE.setCurrency(currency);
            INSTANCE.setFinishRedirectUrl(finishRedirectUrl);
            INSTANCE.setSettlementTime(settlementTime);
            INSTANCE.setApprovalCode(approvalCode);
            INSTANCE.setFraudStatus(fraudStatus);
            INSTANCE.setPdfUrl(pdfUrl);
            INSTANCE.setAtmChannel(atmChannel);
            INSTANCE.setBcaVaNumber(bcaVaNumber);
            INSTANCE.setBcaExpiration(bcaExpiration);
            INSTANCE.setVaNumbersList(vaNumbersList);
            INSTANCE.setBniVaNumber(bniVaNumber);
            INSTANCE.setBniExpiration(bniExpiration);
            INSTANCE.setPermataVaNumber(permataVaNumber);
            INSTANCE.setPermataExpiration(permataExpiration);
            INSTANCE.setValidationMessages(validationMessages);
            INSTANCE.setQrCodeUrl(qrCodeUrl);
            INSTANCE.setDeeplinkUrl(deeplinkUrl);
            INSTANCE.setGopayExpiration(gopayExpiration);
            INSTANCE.setGopayExpirationRaw(gopayExpirationRaw);
            INSTANCE.setPaymentCode(paymentCode);
            INSTANCE.setStore(store);
            INSTANCE.setIndomaretExpireTime(indomaretExpireTime);
            INSTANCE.setKlikBcaExpireTime(klikBcaExpireTime);
            INSTANCE.setMaskedCard(maskedCard);
            INSTANCE.setBank(bank);
            INSTANCE.setCardType(cardType);
            INSTANCE.billPaymentExpiration = this.billPaymentExpiration;
            INSTANCE.type = this.type;
            INSTANCE.billerCode = this.billerCode;
            INSTANCE.userId = this.userId;
            INSTANCE.pointBalanceAmount = this.pointBalanceAmount;
            INSTANCE.alfamartExpireTime = this.alfamartExpireTime;
            INSTANCE.pointBalance = this.pointBalance;
            INSTANCE.billKey = this.billKey;
            INSTANCE.dataResponse = this.dataResponse;
            return INSTANCE;
        }
    }
}