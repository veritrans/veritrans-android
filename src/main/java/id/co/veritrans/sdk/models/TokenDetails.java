package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/26/15.
 */
public class TokenDetails {


    /**
     * status_message : OK, success request new token
     * status_code : 200
     * token_id : 481111-1114-ec66e79a-b104-4ccb-b916-bb71247ce3b1
     */
    private String status_message;
    private String status_code;
    private String token_id;

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getStatus_message() {
        return status_message;
    }

    public String getStatus_code() {
        return status_code;
    }

    public String getToken_id() {
        return token_id;
    }
}
