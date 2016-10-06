package com.midtrans.sdk.corekit.models;


import com.google.gson.annotations.SerializedName;

/**
 * model class to hold information bout Bank Transfer api call
 *
 * Created by shivam on 10/26/15.
 */
public class IndosatDompetku {


    @SerializedName("msisdn")
    private String msisdn;

    public IndosatDompetku(String msisdn) {
        this.msisdn = msisdn;
    }

}