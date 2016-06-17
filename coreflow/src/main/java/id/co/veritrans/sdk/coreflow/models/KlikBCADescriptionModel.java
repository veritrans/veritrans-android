package id.co.veritrans.sdk.coreflow.models;

import com.google.gson.annotations.SerializedName;

/**
 * Klik BCA payment description model.
 *
 * @author rakawm
 */
public class KlikBcaDescriptionModel {

    private String description;
    @SerializedName("user_id")
    private String userId;

    public KlikBcaDescriptionModel() {

    }

    public KlikBcaDescriptionModel(String description, String userId) {
        setDescription(description);
        setUserId(userId);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}