package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/30/15.
 */
public class CardPaymentDetails{

    /**
     * bank : bni
     * token_id : 481111-1114-7fd8c06e-a612-4f0b-a6d4-4fa2b8918c39
     * save_token_id : true
     */
    private String bank;
    private String token_id;
    private String save_token_id;

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public void setSave_token_id(String save_token_id) {
        this.save_token_id = save_token_id;
    }

    public String getBank() {
        return bank;
    }

    public String getToken_id() {
        return token_id;
    }

    public String getSave_token_id() {
        return save_token_id;
    }
}