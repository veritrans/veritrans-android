package com.midtrans.sdk.corekit.models.snap;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rakawm on 4/25/17.
 */

public class BankTransferRequestModel implements Serializable {
    @SerializedName("va_number")
    private String vaNumber;
    @SerializedName("sub_company_code")
    private String subCompanyCode;

    public BankTransferRequestModel(String vaNumber) {
        setVaNumber(vaNumber);
    }

    public String getVaNumber() {
        return vaNumber;
    }

    public void setVaNumber(String vaNumber) {
        this.vaNumber = vaNumber;
    }

    public String getSubCompanyCode() {
        return subCompanyCode;
    }

    public void setSubCompanyCode(String subCompanyCode) {
        if (!TextUtils.isEmpty(vaNumber)) {
            this.subCompanyCode = subCompanyCode;
        }
    }
}
