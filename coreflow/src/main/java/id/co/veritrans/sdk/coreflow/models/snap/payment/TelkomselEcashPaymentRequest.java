package id.co.veritrans.sdk.coreflow.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 8/1/16.
 */
public class TelkomselEcashPaymentRequest extends BasePaymentRequest {

    //customer phone number
    @SerializedName("customer")
    private String customer;
}
