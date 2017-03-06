package com.midtrans.sdk.core.models.snap;

import java.io.Serializable;

/**
 * @author rakawm
 */

public class SavedToken implements Serializable{
    public static final String ONE_CLICK = "one_click";
    public static final String TWO_CLICKS = "two_clicks";

    public final String token;
    public final String tokenType;
    public final String maskedCard;
    public final String expiresAt;

    public SavedToken(String token, String tokenType, String maskedCard, String expiresAt) {
        this.token = token;
        this.tokenType = tokenType;
        this.maskedCard = maskedCard;
        this.expiresAt = expiresAt;
    }
}
