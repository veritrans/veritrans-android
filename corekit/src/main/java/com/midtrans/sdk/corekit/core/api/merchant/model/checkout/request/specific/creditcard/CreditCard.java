package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.base.enums.AcquiringBankType;
import com.midtrans.sdk.corekit.base.enums.AcquiringChannel;
import com.midtrans.sdk.corekit.base.enums.Authentication;
import com.midtrans.sdk.corekit.base.enums.CreditCardTransactionType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.midtrans.sdk.corekit.utilities.EnumHelper.mappingToAcquiringChannel;
import static com.midtrans.sdk.corekit.utilities.EnumHelper.mappingToBankType;
import static com.midtrans.sdk.corekit.utilities.EnumHelper.mappingToCreditCardAuthentication;
import static com.midtrans.sdk.corekit.utilities.EnumHelper.mappingToCreditCardType;

public class CreditCard implements Serializable {

    private static volatile CreditCard SINGLETON_INSTANCE = null;

    @SerializedName("save_card")
    private boolean saveCard;
    @SerializedName("token_id")
    private String tokenId;
    private boolean secure;
    @SerializedName("saved_tokens")
    private List<SavedToken> savedTokens;
    @SerializedName("whitelist_bins")
    private List<String> whitelistBins;
    @SerializedName("blacklist_bins")
    private List<String> blacklistBins;
    @SerializedName("installment")
    private Installment installment;
    @SerializedName("type")
    private @CreditCardTransactionType
    String type;
    @SerializedName("authentication")
    private @Authentication
    String authentication;
    @SerializedName("channel")
    @AcquiringChannel
    private String channel;
    @SerializedName("bank")
    @AcquiringBankType
    private String acquiringBank;

    public CreditCard() {
    }

    private CreditCard(boolean saveCard,
                       String tokenId,
                       boolean secure,
                       @AcquiringChannel String channel,
                       @AcquiringBankType String acquiringBank,
                       List<SavedToken> savedTokens,
                       List<String> whitelistBins,
                       List<String> blacklistBins,
                       Installment installment,
                       @CreditCardTransactionType String type,
                       @Authentication String authentication) {
        this.saveCard = saveCard;
        this.tokenId = tokenId;
        this.secure = secure;
        this.channel = channel;
        this.acquiringBank = acquiringBank;
        this.savedTokens = savedTokens;
        this.whitelistBins = whitelistBins;
        this.blacklistBins = blacklistBins;
        this.installment = installment;
        this.type = type;
        this.authentication = authentication;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isSaveCard() {
        return saveCard;
    }

    public String getTokenId() {
        return tokenId;
    }

    public @AcquiringChannel
    String getAcquiringChannel() {
        return mappingToAcquiringChannel(channel);
    }

    public @AcquiringBankType
    String getAcquiringBank() {
        return mappingToBankType(acquiringBank);
    }

    public List<SavedToken> getSavedTokens() {
        return savedTokens;
    }

    public List<String> getWhitelistBins() {
        return whitelistBins;
    }

    public List<String> getBlacklistBins() {
        return blacklistBins;
    }

    public Installment getInstallment() {
        return installment;
    }

    public @CreditCardTransactionType
    String getType() {
        return mappingToCreditCardType(type);
    }

    public boolean isSecure() {
        return secure;
    }

    public @Authentication
    String getAuthentication() {
        return mappingToCreditCardAuthentication(authentication, secure);
    }

    public static class Builder {
        private boolean saveCard;
        private String tokenId;
        private boolean secure;
        private @AcquiringChannel
        String channel;
        private String acquiringBank;
        private List<SavedToken> savedTokens;
        private List<String> whitelistBins;
        private List<String> blacklistBins;
        private Installment installment;
        private @CreditCardTransactionType
        String type;
        private String authentication;

        public Builder setTokenId(String tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        public Builder setAcquiringChannel(@AcquiringChannel String channel) {
            this.channel = mappingToAcquiringChannel(channel);
            return this;
        }

        public Builder setAcquiringBank(@AcquiringBankType String acquiringBank) {
            this.acquiringBank = mappingToBankType(acquiringBank);
            return this;
        }

        public Builder setSaveCard(boolean saveCard) {
            this.saveCard = saveCard;
            return this;
        }

        public Builder setAuthentication(@Authentication String authentication) {
            this.secure = authentication != null && authentication.equalsIgnoreCase(Authentication.AUTH_3DS);
            this.authentication = mappingToCreditCardAuthentication(authentication, secure);
            return this;
        }

        public Builder setSavedTokens(List<SavedToken> savedTokens) {
            this.savedTokens = savedTokens;
            return this;
        }

        public Builder setType(@CreditCardTransactionType String type) {
            this.type = mappingToCreditCardType(type);
            return this;
        }

        public Builder setInstallment(boolean installmentRequired,
                                      Map<String, List<Integer>> installmentTerms) {
            if (installmentTerms != null) {
                this.installment = new Installment(installmentRequired, installmentTerms);
            }
            return this;
        }

        public Builder setBlackListBins(List<String> blacklistBins) {
            this.blacklistBins = blacklistBins;
            return this;
        }

        public Builder setWhiteListBins(List<String> whiteListBins) {
            this.whitelistBins = whiteListBins;
            return this;
        }

        public Builder setNewCreditCardTokens(CreditCard creditCard, List<SavedToken> tokens) {
            this.savedTokens = tokens;
            this.saveCard = creditCard.saveCard;
            this.tokenId = creditCard.tokenId;
            this.secure = creditCard.secure;
            this.channel = creditCard.channel;
            this.acquiringBank = creditCard.acquiringBank;
            this.whitelistBins = creditCard.whitelistBins;
            this.blacklistBins = creditCard.blacklistBins;
            this.installment = creditCard.installment;
            this.type = creditCard.type;
            this.authentication = creditCard.authentication;
            return this;
        }

        public CreditCard build() {
            SINGLETON_INSTANCE = new CreditCard(
                    saveCard,
                    tokenId,
                    secure,
                    channel,
                    acquiringBank,
                    savedTokens,
                    whitelistBins,
                    blacklistBins,
                    installment,
                    type,
                    authentication
            );
            return SINGLETON_INSTANCE;
        }
    }
}