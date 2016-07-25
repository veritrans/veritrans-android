package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.style.TtsSpan;

import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.events.Events;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SSLErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionStatusSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.SnapTokenRequestModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.snap.Token;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BankTransferPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.CreditCardPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.KlikBCAPaymentRequest;
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
    private static final String SNAP_PAYMENT_CARD_SUCCESS = "Payment Using Credit Card Success";
    private static final String SNAP_PAYMENT_CARD_FAILED = "Payment Using Credit Card Failed";
    private static final String SNAP_PAYMENT_BANK_TRANSFER_SUCCESS = "Payment Using Bank Transfer Success";
    private static final String SNAP_PAYMENT_BANK_TRANSFER_FAILED = "Payment Using Bank Transfer Success";
    private static final String SNAP_PAYMENT_KLIK_BCA_SUCCESS = "Payment Using KLIK BCA Success";
    private static final String SNAP_PAYMENT_KLIK_BCA_FAILED= "Payment Using KLIK BCA Failed";
    private SnapRestAPI snapRestAPI;


    public SnapTransactionManager(Context context, SnapRestAPI snapRestAPI, MerchantRestAPI merchantApiClient) {
        this.context = context;
        this.snapRestAPI = snapRestAPI;
        this.merchantPaymentAPI = merchantApiClient;

    }

    protected void setRestApi(SnapRestAPI restApi) {
        this.snapRestAPI = restApi;
    }


    /**
     * this method will get snap token via merchant server
     * @param model
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

    /**
     * this method is used for card payment using snap backend
     * @param requestModel
     */

    public void paymentUsingCreditCard(CreditCardPaymentRequest requestModel){
        final long start = System.currentTimeMillis();
        if(requestModel != null){
            snapRestAPI.paymentUsingCreditCard(requestModel, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if(isSDKLogEnabled){
                        displayResponse(transactionResponse);
                    }
                    if(transactionResponse != null){
                        if(transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))){
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(SNAP_PAYMENT_CARD_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
                        }else{
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(SNAP_PAYMENT_CARD_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, transactionResponse.getStatusMessage());
                        }
                    }else{
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.empty_transaction_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(SNAP_PAYMENT_CARD_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if(error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException){
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    }else{
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(SNAP_PAYMENT_CARD_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, error.getMessage());

                }
            });
        }else{
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.SNAP_PAYMENT));
        }
    }

    /**
     * this method is used for Payment Using Bank Transfer
     *
     */
    public void paymentUsingBankTransfer(BankTransferPaymentRequest request){
        final long start = System.currentTimeMillis();
        if(request != null){
            snapRestAPI.paymentUsingBankTransfer(request, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if(isSDKLogEnabled){
                        displayResponse(transactionResponse);
                    }
                    if(transactionResponse != null){
                        if(transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))){
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(SNAP_PAYMENT_BANK_TRANSFER_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, end - start, Events.SNAP_PAYMENT);
                        }else{
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(SNAP_PAYMENT_BANK_TRANSFER_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, transactionResponse.getStatusMessage());
                        }
                    }else{
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(SNAP_PAYMENT_BANK_TRANSFER_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if(error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof  CertPathValidatorException){
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    }else{
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(SNAP_PAYMENT_BANK_TRANSFER_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, error.getMessage());
                }
            });
        }else{
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.SNAP_PAYMENT));
        }

    }

    /**
     * this method is used for payment using Klik BCA
     * @param
     */
    public void paymentUsingKlikBCA(KlikBCAPaymentRequest request){
        final long start = System.currentTimeMillis();
        if(request != null){
            snapRestAPI.paymentUsingKlikBCA(request, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if(isSDKLogEnabled){
                        displayResponse(transactionResponse);
                    }
                    if(transactionResponse != null){
                        if(transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))){
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(SNAP_PAYMENT_KLIK_BCA_SUCCESS, PAYMENT_TYPE_KLIK_BCA, end - start, Events.SNAP_PAYMENT);
                        }else{
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(SNAP_PAYMENT_KLIK_BCA_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, Events.SNAP_PAYMENT);
                        }
                    }else{
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(SNAP_PAYMENT_KLIK_BCA_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if(error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException){
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    }else{
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(SNAP_PAYMENT_KLIK_BCA_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, error.getMessage());
                }
            });

        }else{
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }




}
