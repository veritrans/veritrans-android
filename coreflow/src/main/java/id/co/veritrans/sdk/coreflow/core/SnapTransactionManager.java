package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;
import android.support.annotation.NonNull;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.Events;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SSLErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetCardsFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetCardsSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;
import id.co.veritrans.sdk.coreflow.models.SnapTokenRequestModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.snap.Token;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BankTransferPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BasePaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.CreditCardPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.IndosatDompetkuPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.KlikBCAPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.MandiriClickPayPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.TelkomselEcashPaymentRequest;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ziahaqi on 7/18/16.
 */
public class SnapTransactionManager extends BaseTransactionManager {
    // Snap
    private static final String GET_SNAP_TRANSACTION_FAILED = "Failed Getting Snap Transaction";
    private static final String GET_SNAP_TRANSACTION_SUCCESS = "Success Getting Snap Transaction";
    private static final String PAYMENT_TYPE_SNAP = "snap";

    private SnapRestAPI snapRestAPI;

    SnapTransactionManager(Context context, SnapRestAPI snapRestAPI, MerchantRestAPI merchantApiClient, VeritransRestAPI veritransRestAPI) {
        this.context = context;
        this.snapRestAPI = snapRestAPI;
        this.merchantPaymentAPI = merchantApiClient;
        this.veritransPaymentAPI = veritransRestAPI;
    }

    protected void setRestApi(SnapRestAPI restApi) {
        this.snapRestAPI = restApi;
    }

    /**
     * This method will get snap token via merchant server.
     *
     * @param model Transaction details.
     */
    public void getSnapToken(SnapTokenRequestModel model) {
        final long start = System.currentTimeMillis();
        merchantPaymentAPI.getSnapToken(model, new Callback<Token>() {
            @Override
            public void success(Token snapTokenDetailResponse, Response response) {
                releaseResources();
                if (snapTokenDetailResponse != null) {
                    if (snapTokenDetailResponse.getTokenId() != null && !snapTokenDetailResponse.getTokenId().equals("")) {
                        VeritransBusProvider.getInstance().post(new GetSnapTokenSuccessEvent(snapTokenDetailResponse, Events.GET_SNAP_TOKEN));
                    } else {
                        VeritransBusProvider.getInstance().post(new GetSnapTokenFailedEvent(context.getString(R.string.error_empty_response), snapTokenDetailResponse, Events.GET_SNAP_TOKEN));
                    }
                } else {
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
     * This will create a HTTP request to Snap to get transaction details.
     *
     * @param snapToken Snap Token.
     */
    public void getSnapTransaction(@NonNull String snapToken) {
        final long start = System.currentTimeMillis();
        if (snapToken != null) {
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
     * This method is used for card payment using snap backend.
     *
     * @param requestModel Payment details.
     */

    public void paymentUsingCreditCard(CreditCardPaymentRequest requestModel) {
        final long start = System.currentTimeMillis();
        if (requestModel != null) {
            snapRestAPI.paymentUsingCreditCard(requestModel, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, transactionResponse.getStatusMessage());
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.empty_transaction_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, error.getMessage());

                }
            });
        }else{
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.SNAP_PAYMENT));
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer.
     *
     * @param request Payment Details.
     */
    public void paymentUsingBankTransferBCA(BankTransferPaymentRequest request) {
        final long start = System.currentTimeMillis();
        if (request != null) {
            snapRestAPI.paymentUsingBankTransferBCA(request, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, transactionResponse.getStatusMessage());
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.SNAP_PAYMENT));
        }
    }

    public void paymentUsingBankTransferPermata(BankTransferPaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBankTransferPermata(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }

                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, transactionResponse.getStatusMessage());
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.SNAP_PAYMENT));
        }
    }

    /**
     * This method is used for payment using Klik BCA.
     *
     * @param request Payment details
     */
    public void paymentUsingKlikBCA(KlikBCAPaymentRequest request) {
        final long start = System.currentTimeMillis();
        if (request != null) {
            snapRestAPI.paymentUsingKlikBCA(request, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_KLIK_BCA, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, error.getMessage());
                }
            });

        }else{
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    public void paymentUsingBCAKlikpay(BasePaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBCAKlikPay(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BCA_KLIKPAY, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    public void paymentUsingMandiriBillPay(BankTransferPaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriBillPay(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_BILL_PAY, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_BILL_PAY, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_BILL_PAY, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_BILL_PAY, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    public void paymentUsingMandiriClickPay(MandiriClickPayPaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriClickPay(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }


    public void paymentUsingCIMBClick(BasePaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingCIMBClick(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CIMB_CLICK, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }


    public void paymentUsingBRIEpay(BasePaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBRIEpay(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BRI_EPAY, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }


    public void paymentUsingMandiriEcash(BasePaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriEcash(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_ECASH, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }


    public void paymentUsingTelkomselCash(TelkomselEcashPaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingTelkomselEcash(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_TELKOMSEL_ECASH, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_TELKOMSEL_ECASH, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_TELKOMSEL_ECASH, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_TELKOMSEL_ECASH, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    public void paymentUsingXLTunai(BasePaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingXlTunai(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_XL_TUNAI, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_XL_TUNAI, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_XL_TUNAI, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_XL_TUNAI, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    public void paymentUsingIndomaret(BasePaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingIndomaret(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOMARET, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    public void paymentUsingIndosatDompetku(IndosatDompetkuPaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingIndosatDompetku(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }


    public void paymentUsingKiosan(BasePaymentRequest paymentRequest) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingKiosan(paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_KIOSAN, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KIOSAN, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KIOSAN, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KIOSAN, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    public void paymentUsingBankTransferAllBank(BankTransferPaymentRequest bankTransferPaymentRequest) {
        final long start = System.currentTimeMillis();
        if (bankTransferPaymentRequest != null) {
            snapRestAPI.paymentUsingBankTransferAllBank(bankTransferPaymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (isSDKLogEnabled) {
                        displayResponse(transactionResponse);
                    }
                    if (transactionResponse != null) {
                        if (transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_200))
                                || transactionResponse.getStatusCode().equals(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, ALL_BANK, end - start, Events.SNAP_PAYMENT);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.SNAP_PAYMENT));
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.SNAP_PAYMENT));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_PAYMENT));
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_PAYMENT));
                    }
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, ALL_BANK, end - start, error.getMessage());
                }
            });
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.SNAP_PAYMENT));
        }
    }

    /*
     * save cards  to merchant server
     */

    public void saveCards(String userId, ArrayList<SaveCardRequest> cardRequests){
        if(cardRequests != null){
            merchantPaymentAPI.saveCards(userId, cardRequests, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    releaseResources();
                    SaveCardResponse saveCardResponse = new SaveCardResponse();
                    saveCardResponse.setCode(response.getStatus());
                    saveCardResponse.setMessage(response.getReason());
                    if(response.getStatus() == 200 || response.getStatus() == 201){
                        VeritransBusProvider.getInstance().post(new SaveCardSuccessEvent(saveCardResponse, Events.REGISTER_CARD));
                    }else{
                        VeritransBusProvider.getInstance().post(new SaveCardFailedEvent(response.getReason(), saveCardResponse, Events.REGISTER_CARD));
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    if(error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException){
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_CARD_REGISTRATION));
                    }else{
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_CARD_REGISTRATION));
                    }
                }
            });
        }else{
            releaseResources();
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * get saved card list on merchant server
     * @param userId unique id to each user
     */

    public void getCards(String userId){
            merchantPaymentAPI.getCards(userId, new Callback<ArrayList<SaveCardRequest>>() {
                @Override
                public void success(ArrayList<SaveCardRequest> cardsResponses, Response response) {
                    releaseResources();
                    if(cardsResponses != null && cardsResponses.size() > 0){
                        if(response.getStatus() == 200 || response.getStatus() == 201){
                            VeritransBusProvider.getInstance().post(new GetCardsSuccessEvent(cardsResponses, Events.SNAP_GET_CARD));
                        }else{
                            VeritransBusProvider.getInstance().post(new GetCardsFailedEvent(response.getReason(), cardsResponses, Events.SNAP_GET_CARD));
                        }
                    }else{
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.empty_transaction_response), Events.SNAP_GET_CARD));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    if(error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException){
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.SNAP_CARD_REGISTRATION));
                    }else{
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(error.getMessage(), Events.SNAP_CARD_REGISTRATION));
                    }
                }
            });
    }


    /*
     * PAPI STUFF
     */

    /**
     * It will execute API call to get token from Veritrans that can be used later.
     *
     * @param cardNumber   credit card number
     * @param cardCvv      credit card CVV number
     * @param cardExpMonth credit card expired month
     * @param cardExpYear  credit card expired year
     */
    public void cardRegistration(String cardNumber,
                                 String cardCvv,
                                 String cardExpMonth,
                                 String cardExpYear, String clientKey) {
        veritransPaymentAPI.registerCard(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey, new Callback<CardRegistrationResponse>() {
            @Override
            public void success(CardRegistrationResponse cardRegistrationResponse, Response response) {
                releaseResources();

                if (cardRegistrationResponse != null) {
                    if (cardRegistrationResponse.getStatusCode().equals(context.getString(R.string.success_code_200))) {
                        VeritransBusProvider.getInstance().post(new CardRegistrationSuccessEvent(cardRegistrationResponse, Events.CARD_REGISTRATION));
                    } else {
                        VeritransBusProvider.getInstance().post(new CardRegistrationFailedEvent(cardRegistrationResponse.getStatusMessage(), cardRegistrationResponse, Events.CARD_REGISTRATION));
                    }
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.CARD_REGISTRATION));
                    Logger.e(context.getString(R.string.error_empty_response));
                }
            }

            @Override
            public void failure(RetrofitError e) {
                releaseResources();
                if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                    VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.CARD_REGISTRATION));
                    Logger.i("Error in SSL Certificate. " + e.getMessage());
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.CARD_REGISTRATION));
                    Logger.i("General error occurred " + e.getMessage());
                }
            }
        });
    }

}
