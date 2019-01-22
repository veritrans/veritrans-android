package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard;

import com.google.gson.annotations.SerializedName;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.enums.AcquiringBankType;
import com.midtrans.sdk.corekit.base.enums.AcquiringChannel;
import com.midtrans.sdk.corekit.base.enums.Authentication;
import com.midtrans.sdk.corekit.base.enums.CreditCardTransactionType;
import com.midtrans.sdk.corekit.utilities.Helper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.AuthenticationType.AUTHENTICATION_TYPE_3DS;
import static com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.AuthenticationType.AUTHENTICATION_TYPE_NONE;
import static com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.AuthenticationType.AUTHENTICATION_TYPE_RBA;
import static com.midtrans.sdk.corekit.utilities.Helper.mappingToAcquiringChannel;
import static com.midtrans.sdk.corekit.utilities.Helper.mappingToBankType;
import static com.midtrans.sdk.corekit.utilities.Helper.mappingToCreditCardAuthentication;
import static com.midtrans.sdk.corekit.utilities.Helper.mappingToCreditCardType;

public class CreditCard implements Serializable {


    @Deprecated
    public static final String RBA = "rba";
    private static volatile CreditCard SINGLETON_INSTANCE = null;
    @SerializedName("save_card")
    private boolean saveCard;
    @SerializedName("token_id")
    private String tokenId;
    private boolean secure;
    private String channel;
    private String acquiringBank;
    @SerializedName("saved_tokens")
    private List<SavedToken> savedTokens;
    @SerializedName("whitelist_bins")
    private List<String> whitelistBins;
    @SerializedName("blacklist_bins")
    private List<String> blacklistBins;
    @SerializedName("installment")
    private Installment installment;
    private String type;
    private String authentication;

    private boolean isSecureSet = false;
    private boolean isAuthenticationSet = false;

    private CreditCard(boolean saveCard,
                       String tokenId,
                       boolean secure,
                       String channel,
                       String acquiringBank,
                       List<SavedToken> savedTokens,
                       List<String> whitelistBins,
                       List<String> blacklistBins,
                       Installment installment,
                       String type,
                       String authentication) {
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

    public boolean isSecure() {
        return secure;
    }

    public AcquiringChannel getAcquiringChannel() {
        return mappingToAcquiringChannel(channel);
    }

    public AcquiringBankType getAcquiringBank() {
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

    public CreditCardTransactionType getType() {
        return mappingToCreditCardType(type);
    }

    public Authentication getAuthentication() {
        return mappingToCreditCardAuthentication(authentication);
    }

    public boolean isSecureSet() {
        return isSecureSet;
    }

    public boolean isAuthenticationSet() {
        return isAuthenticationSet;
    }

    public static class Builder {
        private boolean saveCard;
        private String tokenId;
        private boolean secure;
        private String channel;
        private String acquiringBank;
        private List<SavedToken> savedTokens;
        private List<String> whitelistBins;
        private List<String> blacklistBins;
        private Installment installment;
        private String type;
        private String authentication;

        private boolean isSecureSet = false;
        private boolean isAuthenticationSet = false;

        Builder() {
        }

        public Builder setTokenId(String tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        public Builder setAcquiringChannel(AcquiringChannel channel) {
            this.channel = mappingToAcquiringChannel(channel);
            return this;
        }

        public Builder setAcquiringBank(AcquiringBankType acquiringBank) {
            this.acquiringBank = Helper.mappingToBankType(acquiringBank);
            return this;
        }

        public Builder setSaveCard(boolean saveCard) {
            this.saveCard = saveCard;
            return this;
        }

        public Builder setSecure(boolean secure) {
            if (authentication == null ||
                    !(authentication.equals(AUTHENTICATION_TYPE_RBA)
                            || authentication.equals(AUTHENTICATION_TYPE_3DS)
                            || authentication.equals(AUTHENTICATION_TYPE_NONE))) {
                if (!isSecureSet) {
                    this.secure = secure;
                    isSecureSet = true;
                }
            }
            return this;
        }

        public Builder setSavedTokens(List<SavedToken> savedTokens) {
            this.savedTokens = savedTokens;
            return this;
        }

        public Builder setType(CreditCardTransactionType type) {
            this.type = mappingToCreditCardType(type);
            return this;
        }

        public Builder setInstallment(boolean installmentRequired,
                                      Map<AcquiringBankType, List<Integer>> installmentTerms) {
            if (installmentTerms != null) {
                this.installment = new Installment(installmentRequired, installmentTerms);
            }
            return this;
        }

        public Builder setAuthentication(Authentication auth) {
            String authentication = mappingToCreditCardAuthentication(auth);
            if (!TextUtils.isEmpty(authentication)) {
                if (authentication.equals(AUTHENTICATION_TYPE_RBA)
                        || authentication.equals(AUTHENTICATION_TYPE_NONE)) {
                    this.secure = false;
                } else if (authentication.equals(AUTHENTICATION_TYPE_3DS)) {
                    this.secure = true;
                }
            }
            if (!isAuthenticationSet) {
                this.authentication = authentication;
                isAuthenticationSet = true;
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

        public CreditCard build() {
            SINGLETON_INSTANCE = new CreditCard(saveCard,
                    tokenId,
                    secure,
                    channel,
                    acquiringBank,
                    savedTokens,
                    whitelistBins,
                    blacklistBins,
                    installment,
                    type,
                    authentication);
            return SINGLETON_INSTANCE;
        }
    }
}