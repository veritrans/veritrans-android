package id.co.veritrans.sdk.coreflow.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 8/1/16.
 */
public class MandiriClickPayPaymentRequest extends BasePaymentRequest {

    @SerializedName("mandiri_card_no")
    private String mandiriCardNumber;

    private long input3;

    @SerializedName("token_response")
    private String tokenResponse;
}
