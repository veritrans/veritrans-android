package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;

import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.events.AuthenticationEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.Events;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SSLErrorEvent;
import id.co.veritrans.sdk.coreflow.models.AuthModel;
import id.co.veritrans.sdk.coreflow.models.SnapTokenDetailResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ziahaqi on 7/18/16.
 */
public class SnapTransactionManager extends BaseTransactionManager{

    public SnapTransactionManager(Context context, VeritransRestAPI veritransRestAPI, MerchantRestAPI merchantApiClient) {
        this.context = context;
        this.veritransPaymentAPI = veritransRestAPI ;
        this.merchantPaymentAPI = merchantApiClient;

    }

    public void getSnapToken() {
        veritransPaymentAPI.getSnapToken(new Callback<SnapTokenDetailResponse>() {
            @Override
            public void success(SnapTokenDetailResponse snapTokenDetailResponse, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

}
