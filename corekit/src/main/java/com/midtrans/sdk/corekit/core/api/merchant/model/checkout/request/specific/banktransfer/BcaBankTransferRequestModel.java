package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BcaBankTransferRequestModel extends BankTransferRequestModel implements Serializable {

    @SerializedName("free_text")
    private BcaBankFreeText freeText;
    @SerializedName("sub_company_code")
    private String subCompanyCode;

    public BcaBankTransferRequestModel(String vaNumber, BcaBankFreeText freeText, String subCompanyCode) {
        super(vaNumber);
        this.freeText = freeText;
        this.subCompanyCode = subCompanyCode;
    }

    public BcaBankTransferRequestModel(String vaNumber) {
        super(vaNumber);
    }

    public BcaBankTransferRequestModel(String vaNumber, BcaBankFreeText freeText) {
        super(vaNumber);
        this.freeText = freeText;
    }

    public String getSubCompanyCode() {
        return subCompanyCode;
    }

    public void setSubCompanyCode(String subCompanyCode) {
        this.subCompanyCode = subCompanyCode;
    }
}