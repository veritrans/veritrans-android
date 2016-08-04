package id.co.veritrans.sdk.coreflow.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class CreditCardPaymentRequest extends BasePaymentRequest {
    @SerializedName("token_id")
    private String tokenId;
    @SerializedName("payment_detail")
    private PaymentDetails paymentDetails;
    @SerializedName("save_card")
    private boolean saveCard;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public boolean isSaveCard() {
        return saveCard;
    }

    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }
}
