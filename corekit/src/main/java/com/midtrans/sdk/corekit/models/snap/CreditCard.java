package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.corekit.utilities.Utils.mappingToCreditCardAuthentication;

/**
 * Created by ziahaqi on 8/5/16.
 */
public class CreditCard {

    public static final String MIGS = "migs";

    @Deprecated
    public static final String RBA = "rba";

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
    private ArrayList<String> whitelistBins;

    @SerializedName("blacklist_bins")
    private List<String> blacklistBins;

    @SerializedName("installment")
    private Installment installment;
    private String type;
    private String authentication;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public boolean isSaveCard() {
        return saveCard;
    }

    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public List<SavedToken> getSavedTokens() {
        return savedTokens;
    }

    public void setSavedTokens(List<SavedToken> savedTokens) {
        this.savedTokens = savedTokens;
    }

    public ArrayList<String> getWhitelistBins() {
        return whitelistBins;
    }

    public Installment getInstallment() {
        return installment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInstallment(Installment installment) {
        this.installment = installment;
    }

    public void setWhiteListBins(ArrayList<String> whiteListBins) {
        this.whitelistBins = whiteListBins;
    }

    public Authentication getAuthentication() {
        return mappingToCreditCardAuthentication(authentication, secure);
    }

    public void setAuthentication(Authentication authentication) {
        this.secure = authentication != null && authentication == Authentication.AUTH_3DS;
        this.authentication = mappingToCreditCardAuthentication(authentication);
    }

    public List<String> getBlacklistBins() {
        return blacklistBins;
    }

    public void setBlacklistBins(List<String> blacklistBins) {
        this.blacklistBins = blacklistBins;
    }
}
