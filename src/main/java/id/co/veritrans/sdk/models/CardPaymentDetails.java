package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 10/30/15.
 */
public class CardPaymentDetails{
    public CardPaymentDetails(String bank, String tokenId, boolean saveTokenId) {
        this.bank = bank;
        this.tokenId = tokenId;
        this.saveTokenId = saveTokenId;
    }

    /**
     * bank : bni
     * token_id : 481111-1114-7fd8c06e-a612-4f0b-a6d4-4fa2b8918c39
     * save_token_id : true
     */

    private String bank;

    @SerializedName("token_id")
    private String tokenId;

    @SerializedName("save_token_id")
    private boolean saveTokenId;

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setSaveTokenId(boolean saveTokenId) {
        this.saveTokenId = saveTokenId;
    }

    public String getBank() {
        return bank;
    }

    public String getTokenId() {
        return tokenId;
    }

    public boolean getSaveTokenId() {
        return saveTokenId;
    }
}