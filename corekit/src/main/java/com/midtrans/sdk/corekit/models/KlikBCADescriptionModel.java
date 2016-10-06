package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Klik BCA payment description model.
 *
 * @author rakawm
 */
public class KlikBCADescriptionModel {

    private String description;
    @SerializedName("user_id")
    private String userId;

    public KlikBCADescriptionModel() {

    }

    public KlikBCADescriptionModel(String description, String userId) {
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