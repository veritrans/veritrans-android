package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/26/15.
 */
public class TokenDetailsResponse {


    /**
     * status_message : OK, success request new token
     * bank : bni
     * status_code : 200
     * token_id : 481111-1114-0452c0cb-3199-4081-82ba-2e05b378c0ca
     * redirect_url : https://api.sandbox.veritrans.co
     * .id/v2/token/redirect/481111-1114-0452c0cb-3199-4081-82ba-2e05b378c0ca
     */

    private String status_message;
    private String bank;
    private String status_code;
    private String token_id;
    private String redirect_url;

    public void setStatusMessage(String status_message) {
        this.status_message = status_message;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setStatusCode(String status_code) {
        this.status_code = status_code;
    }

    public void setTokenId(String token_id) {
        this.token_id = token_id;
    }

    public void setRedirectUrl(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getStatusMessage() {
        return status_message;
    }

    public String getBank() {
        return bank;
    }

    public String getStatusCode() {
        return status_code;
    }

    public String getTokenId() {
        return token_id;
    }

    public String getRedirectUrl() {
        return redirect_url;
    }
}