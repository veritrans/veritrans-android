package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 12/10/15.
 */
public class GetOffersResponseModel {

    @SerializedName("status_code")
    private int code;
    @SerializedName("status_message")
    private String message;
    @SerializedName("data")
    private OffersResponseModel offers;

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The offers
     */
    public OffersResponseModel getOffers() {
        return offers;
    }

    /**
     * @param offers The offers
     */
    public void setOffers(OffersResponseModel offers) {
        this.offers = offers;
    }

    /**
     * @return status code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code status code
     */
    public void setCode(int code) {
        this.code = code;
    }
}
