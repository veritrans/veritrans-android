package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 11/27/15.
 */
public class CIMBDescription {

    @SerializedName("description")
    private String description;

    public CIMBDescription() {

    }

    public CIMBDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
