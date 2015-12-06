package id.co.veritrans.sdk.models;

/**
 * Created by chetan on 04/12/15.
 */
public class DeleteCardResponse {
    private String message;
    private String error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
