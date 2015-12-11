package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 12/10/15.
 */
public class GetOffersResponseModel {

    @SerializedName("message")
    private String message;
    @SerializedName("offers")
    private OffersResponseModel offers;

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The offers
     */
    public OffersResponseModel getOffers() {
        return offers;
    }

    /**
     *
     * @param offers
     * The offers
     */
    public void setOffers(OffersResponseModel offers) {
        this.offers = offers;
    }
}
