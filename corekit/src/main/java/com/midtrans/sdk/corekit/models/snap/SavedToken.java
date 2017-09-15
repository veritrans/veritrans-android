package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */

public class SavedToken {
    public static final String ONE_CLICK = "one_click";
    public static final String TWO_CLICKS = "two_clicks";

    private String token;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("masked_card")
    private String maskedCard;
    @SerializedName("expires_at")
    private String expiresAt;

    private boolean fromHostApp;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
