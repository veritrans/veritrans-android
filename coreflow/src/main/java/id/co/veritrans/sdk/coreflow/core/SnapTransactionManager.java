package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;
import android.support.annotation.NonNull;

import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.events.Events;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SSLErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.SnapTokenRequestModel;
import id.co.veritrans.sdk.coreflow.models.snap.Token;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ziahaqi on 7/18/16.
 */
public class SnapTransactionManager extends BaseTransactionManager{
    // Snap
    private static final String GET_SNAP_TRANSACTION_FAILED = "Failed Getting Snap Transaction";
    private static final String GET_SNAP_TRANSACTION_SUCCESS = "Success Getting Snap Transaction";
    private static final String PAYMENT_TYPE_SNAP = "snap";
    private SnapRestAPI snapRestAPI;


    public SnapTransactionManager(Context context, SnapRestAPI snapRestAPI, MerchantRestAPI merchantApiClient) {
        this.context = context;
        this.snapRestAPI = snapRestAPI;
        this.merchantPaymentAPI = merchantApiClient;

    }

    /*
     * this method will get snap token via merchant server
     */
    public void getSnapToken(SnapTokenRequestModel model) {
        final long start = System.currentTimeMillis();
        merchantPaymentAPI.getSnapToken(model, new Callback<Token>() {
            @Override
            public void success(Token snapTokenDetailResponse, Response response) {
                releaseResources();
                if(snapTokenDetailResponse != null){
                    if (snapTokenDetailResponse.getTokenId() != null && !snapTokenDetailResponse.getTokenId().equals("")) {
                        VeritransBusProvider.getInstance().post(new GetSnapTokenSuccessEvent(snapTokenDetailResponse, Events.GET_SNAP_TOKEN));
                    }else{
                        VeritransBusProvider.getInstance().post(new GetSnapTokenFailedEvent(context.getString(R.string.error_empty_response), snapTokenDetailResponse, Events.GET_SNAP_TOKEN));
                    }
                }else{
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.empty_transaction_response), Events.GET_SNAP_TOKEN));
                }

            }

            @Override
            public void failure(RetrofitError e) {
                releaseResources();
                long end = System.currentTimeMillis();

                if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                    VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.GET_SNAP_TOKEN));
                    Logger.i("Error in SSL Certificate. " + e.getMessage());
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.GET_SNAP_TOKEN));
                    Logger.i("General error occurred " + e.getMessage());
                }

                // Track Mixpanel event
                analyticsManager.trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, e.getMessage());
            }
        });
    }


    /**
     * This will create a HTTP request to merchant server to
     *
     * @param snapToken
     */
    public void getSnapTransaction(@NonNull String snapToken) {
        final long start = System.currentTimeMillis();
        if(snapToken != null){
            snapRestAPI.getSnapTransaction(snapToken, new Callback<Transaction>() {
                @Override
                public void success(Transaction transaction, Response response) {
                    releaseResources();

                    long end = System.currentTimeMillis();

                    if (transaction != null) {
                        if (response.getStatus() == 200 && !transaction.getTransactionData().getTransactionId().equals("")) {
                            VeritransBusProvider.getInstance().post(new GetSnapTransactionSuccessEvent(transaction, Events.GET_SNAP_TRANSACTION));
                            // Track Mixpanel event
                            analyticsManager.trackMixpanel(GET_SNAP_TRANSACTION_SUCCESS, PAYMENT_TYPE_SNAP, end - start);
                        } else {
                            VeritransBusProvider.getInstance().post(new GetSnapTransactionFailedEvent(response.getReason(), transaction, Events.GET_SNAP_TRANSACTION));
                            // Track Mixpanel event
                            analyticsManager.trackMixpanel(GET_SNAP_TRANSACTION_FAILED, PAYMENT_TYPE_SNAP, end - start);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.GET_SNAP_TRANSACTION));
                        Logger.e(context.getString(R.string.error_empty_response));

                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(GET_SNAP_TRANSACTION_FAILED, PAYMENT_TYPE_SNAP, end - start);
                    }
                }

                @Override
                public void failure(RetrofitError e) {
                    releaseResources();

                    long end = System.currentTimeMillis();

                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.GET_SNAP_TRANSACTION));
                        Logger.i("General error occurred " + e.getMessage());
                    }

                    // Track Mixpanel event
                    analyticsManager.trackMixpanel(GET_SNAP_TRANSACTION_FAILED, PAYMENT_TYPE_SNAP, end - start);
                }
            });
        } else {
            releaseResources();
            Logger.e(context.getString(R.string.error_invalid_data_supplied));
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.GET_SNAP_TRANSACTION));
        }
    }


    protected void setRestApi(SnapRestAPI restApi) {
        this.snapRestAPI = restApi;
    }
}
