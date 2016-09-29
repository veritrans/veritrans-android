package com.midtrans.sdk.corekit;

import com.google.gson.Gson;

import com.midtrans.sdk.corekit.core.MerchantRestAPI;
import com.midtrans.sdk.corekit.core.MidtransRestAPI;
import com.midtrans.sdk.corekit.restapi.RestAPIMocUtilites;
import com.midtrans.sdk.corekit.restapi.RetrofitMockClient;

import java.util.Collections;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

/**
 * Created by ziahaqi on 23/06/2016.
 */

public abstract class APIClientMain {

    public static final String TAG = "APIClientMain";
    public static final String BASE_API_URL = "https://vt-merchant.coralshop.top/api/auth";
    public static final String CARD_NUMBER = "4811111111111114";
    public static final String CARD_CVV = "123";
    public static final String CARD_EXP_MONTH = "123";
    public static final String CARD_EXP_YEAR = "123";

    public static final String VT_CARD_REG_RESPONSE_SUCCESS = "vt_register_card_success.json";
    public static final String VT_GET_TOKEN_SUCCESS = "vt_get_token.json";
    public static final String MERCHANT_PAYMENT_SUCCESS = "merchant_pay_card.json";
    public static final String MERCHANT_PAYMENT_FAILED_EXPIRED = "merchant_pay_card_expired.json";
    public static final String MERCHANT_PAYMENT_FAILED_ORDERID_EXIST = "merchant_pay_card_orderid_exist.json";


    public static final String X_AUTH = "ab6e6714fec84419ce2149db863c8ef9";
    public static final String TRANS_TOKEN_VALID = "481111-1114-7b85086a-3ab9-435f-bbd5-9b0a6d75d22f";
    public static final String TRANS_TOKEN_EXP = "481111-1114-7b85086a-3ab9-435f-bbd5-9b0a6d75d22g";
    public static final String TRANS_ORDER_ID_VALID = "d0721a25-8f19-47f8-be8b-0ebe1adee873";
    public static final String TRANS_ORDER_ALREADY_EXIST = "d0721a25-8f19-47f8-be8b-0ebe1adee875";

    protected String sampleJsonResponse = "{\"a\":\"a\"}";
    protected Response retrofitResponse = new Response("URL", 200, "success", Collections.EMPTY_LIST,
            new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

    protected MidtransRestAPI createVeritransPaymentAPIMock(String jsonResponseName, int responseCode, String reason) throws Exception {

        RetrofitMockClient client = RestAPIMocUtilites.getClient(this.getClass().getClassLoader(),
                responseCode, reason, jsonResponseName);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.BASE_URL)
                .setClient(client)
                .setConverter(new GsonConverter(new Gson()))
                .build();
        return restAdapter.create(MidtransRestAPI.class);
    }

    protected MerchantRestAPI createMerchantPaymentAPIMock(String jsonResponseName, int responseCode, String reason) throws Exception {

        RetrofitMockClient client = RestAPIMocUtilites.getClient(this.getClass().getClassLoader(),
                responseCode, reason, jsonResponseName);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.BASE_URL)
                .setClient(client)
                .setConverter(new GsonConverter(new Gson()))
                .build();
        return restAdapter.create(MerchantRestAPI.class);
    }


}
