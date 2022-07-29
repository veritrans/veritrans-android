package com.midtrans.sdk.corekit.models.snap;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class BankSingleBinResponse {
    @SerializedName("data")
    public BankBin data;

    @Keep
    public class BankBin {
        @SerializedName("country_name")
        public String countryName;
        @SerializedName("country_code")
        public String countryCode;
        @SerializedName("brand")
        public String brand;
        @SerializedName("bin_type")
        public String binType;
        @SerializedName("bin_class")
        public String binClass;
        @SerializedName("bin")
        public String bin;
        @SerializedName("bank")
        public String bank;
        @SerializedName("bank_code")
        public String bankCode;
    }
}
