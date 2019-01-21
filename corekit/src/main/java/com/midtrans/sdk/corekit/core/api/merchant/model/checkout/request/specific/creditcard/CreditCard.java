package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard;

import com.google.gson.annotations.SerializedName;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.enums.Authentication;
import com.midtrans.sdk.corekit.base.enums.BankType;
import com.midtrans.sdk.corekit.base.enums.CreditCardType;
import com.midtrans.sdk.corekit.utilities.Helper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.midtrans.sdk.corekit.utilities.Helper.mappingToBankType;
import static com.midtrans.sdk.corekit.utilities.Helper.mappingToCreditCardAuthentication;
import static com.midtrans.sdk.corekit.utilities.Helper.mappingToCreditCardType;

public class CreditCard implements Serializable {

    private static final String MIGS = "migs";
    private static final String AUTHENTICATION_TYPE_RBA = "rba";
    private static final String AUTHENTICATION_TYPE_3DS = "3ds";
    private static final String AUTHENTICATION_TYPE_NONE = "none";

    @Deprecated
    public static final String RBA = "rba";
    private static volatile CreditCard SINGLETON_INSTANCE = null;
    @SerializedName("save_card")
    private boolean saveCard;
    @SerializedName("token_id")
    private String tokenId;
    private boolean secure;
    private String channel;
    private String bank;
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
                       String bank,
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
        this.bank = bank;
        this.savedTokens = savedTokens;
        this.whitelistBins = whitelistBins;
        this.blacklistBins = blacklistBins;
        this.installment = installment;
        this.type = type;
        this.authentication = authentication;
    }

    public static Builder oneClickBuilder() {
        return new Builder();
    }

    public static Builder twoClickBuilder(boolean secure) {
        return new Builder(secure);
    }

    public static Builder normalClickBuilder(boolean secure,
                                             Authentication authentication) {
        return new Builder(secure, mappingToCreditCardAuthentication(authentication));
    }

    public static Builder builder(boolean secure,
                                  Authentication authentication) {
        return new Builder(secure, mappingToCreditCardAuthentication(authentication));
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

    public String getChannel() {
        return channel;
    }

    public BankType getBank() {
        return mappingToBankType(bank);
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

    public CreditCardType getType() {
        return mappingToCreditCardType(type);
    }

    public String getAuthentication() {
        return authentication;
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
        private String bank;
        private List<SavedToken> savedTokens;
        private List<String> whitelistBins;
        private List<String> blacklistBins;
        private Installment installment;
        private String type;
        private String authentication;

        private boolean installmentRequired;
        private Map<String, List<Integer>> installmentTerms;

        private boolean isSecureSet = false;
        private boolean isAuthenticationSet = false;

        Builder(boolean secure,
                String authentication) {
            setSecure(secure);
            setAuthentication(authentication);
        }

        Builder(boolean secure) {
            setSecure(secure);
        }

        Builder() {
            setAuthentication(AUTHENTICATION_TYPE_3DS);
            setSecure(true);
        }

        public Builder setTokenId(String tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        public Builder setChannel(String channel) {
            this.channel = channel;
            return this;
        }

        public Builder setBank(BankType bank) {
            this.bank = Helper.mappingToBankType(bank);
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

        public Builder setType(CreditCardType type) {
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

        public Builder setAuthentication(String authentication) {
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
                    bank,
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