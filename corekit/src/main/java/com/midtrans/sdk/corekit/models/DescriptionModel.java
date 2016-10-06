package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 11/27/15.
 */
public class DescriptionModel {

    @SerializedName("description")
    private String description;

    public DescriptionModel(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

}
