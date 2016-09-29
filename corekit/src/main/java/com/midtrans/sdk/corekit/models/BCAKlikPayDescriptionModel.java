package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Hold information about BCA KlikPay specific transaction.
 *
 * @author rakawm
 */
public class BCAKlikPayDescriptionModel {

    public static final int NORMAL_TYPE = 1;
    public static final int DEFAULT_MISC_FEE = 0;

    private int type;
    @SerializedName("misc_fee")
    private int miscFee;
    private String description;

    /**
     * Default constructor for creating this class' object.
     *
     * @param type        BCA KlikPay payment type. For normal transaction set to 1.
     * @param miscFee     Payment additional fee.
     * @param description Payment description.
     */
    public BCAKlikPayDescriptionModel(int type, int miscFee, String description) {
        setType(type);
        setMiscFee(miscFee);
        setDescription(description);
    }

    /**
     * Constructor for creating this class' object using default type.
     *
     * @param miscFee     Payment additional fee.
     * @param description Payment description.
     */
    public BCAKlikPayDescriptionModel(int miscFee, String description) {
        setType(NORMAL_TYPE);
        setMiscFee(miscFee);
        setDescription(description);
    }

    /**
     * Constructor for creating this class' object using default type and default misc fee.
     *
     * @param description Payment description.
     */
    public BCAKlikPayDescriptionModel(String description) {
        setType(NORMAL_TYPE);
        setMiscFee(DEFAULT_MISC_FEE);
        setDescription(description);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMiscFee() {
        return miscFee;
    }

    public void setMiscFee(int miscFee) {
        this.miscFee = miscFee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
