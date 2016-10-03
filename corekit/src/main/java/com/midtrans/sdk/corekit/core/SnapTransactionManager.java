package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.MandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;

import javax.net.ssl.SSLHandshakeException;

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
    private static final String TAG = "TransactionManager";

    private SnapRestAPI snapRestAPI;

    SnapTransactionManager(Context context, SnapRestAPI snapRestAPI, MerchantRestAPI merchantApiClient, MidtransRestAPI midtransRestAPI) {
        this.context = context;
        this.snapRestAPI = snapRestAPI;
        this.merchantPaymentAPI = merchantApiClient;
        this.midtransPaymentAPI = midtransRestAPI;
    }

    protected void setRestApi(SnapRestAPI restApi) {
        this.snapRestAPI = restApi;
    }

    /**
     * This method will get snap token via merchant server.
     *
     * @param model    Transaction details.
     * @param callback Checkout Callback
     */
    public void checkout(TokenRequestModel model, final CheckoutCallback callback) {
        merchantPaymentAPI.checkout(model, new Callback<Token>() {
            @Override
            public void success(Token snapTokenDetailResponse, Response response) {
                releaseResources();
                if (snapTokenDetailResponse != null) {
                    if (snapTokenDetailResponse.getTokenId() != null && !snapTokenDetailResponse.getTokenId().equals("")) {
                        callback.onSuccess(snapTokenDetailResponse);
                    } else {
                        callback.onFailure(snapTokenDetailResponse, context.getString(R.string.error_empty_response));
                    }
                } else {
                    callback.onError(new Throwable(context.getString(R.string.error_empty_response)));
                }
            }

            @Override
            public void failure(RetrofitError e) {
                releaseResources();

                if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                    Logger.i(TAG, "Error in SSL Certificate. " + e.getMessage());
                }
                callback.onError(new Throwable(e.getMessage(), e.getCause()));
            }
        });
    }


    /**
     * This will create a HTTP request to Snap to get transaction option.
     *
     * @param snapToken Snap Token.
     * @param callback  callback of payment option
     */
    public void getTransactionOptions(@NonNull String snapToken, final TransactionOptionsCallback callback) {
        final long start = System.currentTimeMillis();
        snapRestAPI.getPaymentOption(snapToken, new Callback<Transaction>() {
            @Override
            public void success(Transaction transaction, Response response) {
                releaseResources();
                long end = System.currentTimeMillis();

                if (transaction != null) {
                    if (response.getStatus() == 200 && !transaction.getTransactionData().getTransactionId().equals("")) {
                        callback.onSuccess(transaction);
                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(GET_SNAP_TRANSACTION_SUCCESS, PAYMENT_TYPE_SNAP, end - start);
                    } else {
                        callback.onFailure(transaction, response.getReason());
                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(GET_SNAP_TRANSACTION_FAILED, PAYMENT_TYPE_SNAP, end - start);
                    }
                } else {
                    callback.onError(new Throwable(context.getString(R.string.error_empty_response)));
                    Logger.e(TAG, context.getString(R.string.error_empty_response));

                    // Track Mixpanel event
                    analyticsManager.trackMixpanel(GET_SNAP_TRANSACTION_FAILED, PAYMENT_TYPE_SNAP, end - start);
                }
            }

            @Override
            public void failure(RetrofitError e) {
                releaseResources();
                long end = System.currentTimeMillis();

                if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                    Logger.e(TAG, "Error in SSL Certificate. " + e.getMessage());
                }
                callback.onError(new Throwable(e.getMessage(), e.getCause()));
                // Track Mixpanel event
                analyticsManager.trackMixpanel(GET_SNAP_TRANSACTION_FAILED, PAYMENT_TYPE_SNAP, end - start);
            }
        });
    }

    /**
     * This method is used for card payment using snap backend.
     *
     * @param requestModel Payment details.
     * @param callback     Transaction callback
     */

    public void paymentUsingCreditCard(String authenticationToken, CreditCardPaymentRequest requestModel, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (requestModel != null) {
            snapRestAPI.paymentUsingCreditCard(authenticationToken, requestModel, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, transactionResponse.getStatusMessage());
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();

                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer BCA
     *
     * @param paymentRequest Payment Details.
     * @param callback       Transaction callback
     */
    public void paymentUsingBankTransferBCA(String authenticationToken, BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBankTransfer(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start, transactionResponse.getStatusMessage());
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer Permata
     *
     * @param authenticationToken authentication token
     * @param paymentRequest payment Details
     * @param callback       transaction callback
     */
    public void paymentUsingBankTransferPermata(String authenticationToken, final BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBankTransfer(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start, transactionResponse.getStatusMessage());
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using Klik BCA.
     *
     * @param request  Payment details
     * @param callback transaction callback
     */
    public void paymentUsingKlikBCA(String authenticationToken, KlikBCAPaymentRequest request, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (request != null) {
            snapRestAPI.paymentUsingKlikBCA(authenticationToken, request, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_KLIK_BCA, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();

                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, error.getMessage());
                }
            });

        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using BCA Klik Pay.
     *
     * @param paymentRequest payment request for BCA Klik pay
     * @param callback       transaction callback
     */
    public void paymentUsingBCAKlikpay(String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBCAKlikPay(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BCA_KLIKPAY, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using Mandiri Bill Pay.
     *
     * @param authenticationToken
     * @param paymentRequest payment request for Mandiri Bill pay
     * @param callback       transaction callback
     */
    public void paymentUsingMandiriBillPay(String authenticationToken, BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriBillPay(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_BILL_PAY, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_BILL_PAY, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_BILL_PAY, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_BILL_PAY, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using Mandiri Click Pay.
     *
     * @param authenticationToken
     * @param paymentRequest payment request for Mandiri Click Pay
     * @param callback       transaction callback
     */
    public void paymentUsingMandiriClickPay(String authenticationToken, MandiriClickPayPaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriClickPay(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using CIMB Click.
     *
     * @param authenticationToken
     * @param paymentRequest payment request for CIMB Click
     * @param callback       transaction callback
     */
    public void paymentUsingCIMBClick(String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingCIMBClick(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CIMB_CLICK, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using BRI Epay.
     *
     * @param authenticationToken
     * @param paymentRequest payment request for BRI Epay.
     * @param callback       transaction callback
     */
    public void paymentUsingBRIEpay(String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBRIEpay(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BRI_EPAY, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using Mandiri E-Cash
     *
     * @param authenticationToken
     * @param paymentRequest payment request for Mandiri E-Cash
     * @param callback       transaction callbaack
     */
    public void paymentUsingMandiriEcash(String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriEcash(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_ECASH, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using Telkomsel E-Cash
     *
     * @param paymentRequest payment request for Telkomsel E-Cash
     * @param callback       transaction callback
     */
    public void paymentUsingTelkomselCash(String authenticationToken, TelkomselEcashPaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingTelkomselEcash(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_TELKOMSEL_ECASH, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_TELKOMSEL_ECASH, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_TELKOMSEL_ECASH, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_TELKOMSEL_ECASH, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using XL Tunai
     *
     * @param authenticationToken
     * @param paymentRequest payment request for XL Tunai
     * @param callback       transaction callback
     */
    public void paymentUsingXLTunai(String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingXlTunai(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_XL_TUNAI, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_XL_TUNAI, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_XL_TUNAI, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_XL_TUNAI, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using Indomaret
     *
     * @param authenticationToken
     * @param paymentRequest payment request for Indomaret
     * @param callback       Transaction callback
     */
    public void paymentUsingIndomaret(String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingIndomaret(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOMARET, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using Indosat Dompetku
     *
     * @param authenticationToken
     * @param paymentRequest payment request for Indosat Dompetku
     * @param callback       transaction callback
     */
    public void paymentUsingIndosatDompetku(String authenticationToken, IndosatDompetkuPaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingIndosatDompetku(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using Kiosan
     *
     * @param authenticationToken
     * @param paymentRequest payment request for Kiosan
     * @param callback       transaction callback
     */
    public void paymentUsingKiosan(String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingKiosan(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_KIOSAN, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KIOSAN, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KIOSAN, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KIOSAN, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used for payment using Bank Transfer
     *
     * @param authenticationToken
     * @param paymentRequest payment request for Bank Transfer
     * @param callback       transaction callback
     */
    public void paymentUsingBankTransferAllBank(String authenticationToken, BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        final long start = System.currentTimeMillis();
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBankTransfer(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
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
                            callback.onSuccess(transactionResponse);
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, ALL_BANK, end - start, Events.SNAP_PAYMENT);
                        } else {
                            callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, Events.SNAP_PAYMENT);
                        }
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, end - start, context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    long end = System.currentTimeMillis();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, ALL_BANK, end - start, error.getMessage());
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * This method is used to save credit cards to merchant server
     *
     * @param cardRequests credit card Request model
     * @param userId       unique id for every user
     * @param callback     save card callback
     */
    public void saveCards(String userId, ArrayList<SaveCardRequest> cardRequests, final SaveCardCallback callback) {
        if (cardRequests != null) {
            merchantPaymentAPI.saveCards(userId, cardRequests, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    releaseResources();
                    SaveCardResponse saveCardResponse = new SaveCardResponse();
                    saveCardResponse.setCode(response.getStatus());
                    saveCardResponse.setMessage(response.getReason());
                    if (response.getStatus() == 200 || response.getStatus() == 201) {
                        callback.onSuccess(saveCardResponse);
                    } else {
                        callback.onFailure(response.getReason());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    releaseResources();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * this method is used to get saved card list on merchant server
     *
     * @param userId   unique id to each user
     * @param callback Transaction callback
     */
    public void getCards(String userId, final GetCardCallback callback) {
        merchantPaymentAPI.getCards(userId, new Callback<ArrayList<SaveCardRequest>>() {
            @Override
            public void success(ArrayList<SaveCardRequest> cardsResponses, Response response) {
                releaseResources();
                if (cardsResponses != null && cardsResponses.size() > 0) {
                    if (response.getStatus() == 200 || response.getStatus() == 201) {
                        callback.onSuccess(cardsResponses);
                    } else {
                        callback.onFailure(response.getReason());
                    }
                } else {
                    callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                releaseResources();
                if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                    Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                }
                callback.onError(new Throwable(error.getMessage(), error.getCause()));
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
     * @param callback     card transaction callback
     */
    public void cardRegistration(String cardNumber,
                                 String cardCvv,
                                 String cardExpMonth,
                                 String cardExpYear, String clientKey, final CardRegistrationCallback callback) {
        midtransPaymentAPI.registerCard(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey, new Callback<CardRegistrationResponse>() {
            @Override
            public void success(CardRegistrationResponse cardRegistrationResponse, Response response) {
                releaseResources();

                if (cardRegistrationResponse != null) {
                    if (cardRegistrationResponse.getStatusCode().equals(context.getString(R.string.success_code_200))) {
                        callback.onSuccess(cardRegistrationResponse);
                    } else {
                        callback.onFailure(cardRegistrationResponse, cardRegistrationResponse.getStatusMessage());
                    }
                } else {
                    callback.onError(new Throwable(context.getString(R.string.empty_transaction_response)));
                }
            }

            @Override
            public void failure(RetrofitError e) {
                releaseResources();
                if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                    Logger.e(TAG, "Error in SSL Certificate. " + e.getMessage());
                }
                callback.onError(new Throwable(e.getMessage(), e.getCause()));
            }
        });
    }

    /**
     * It will execute an api call to get token from server, and after completion of request it
     *
     * @param cardTokenRequest information about credit card.
     * @param callback         get creditcard token callback
     */
    public void getToken(@NonNull CardTokenRequest cardTokenRequest, @NonNull final CardTokenCallback callback) {
        final long start = System.currentTimeMillis();
        if (cardTokenRequest.isTwoClick()) {
            if (cardTokenRequest.isInstalment()) {
                midtransPaymentAPI.getTokenInstalmentOfferTwoClick(
                        cardTokenRequest.getCardCVV(),
                        cardTokenRequest.getSavedTokenId(),
                        cardTokenRequest.isTwoClick(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.getGrossAmount(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.getClientKey(),
                        cardTokenRequest.isInstalment(),
                        cardTokenRequest.getFormattedInstalmentTerm(), new Callback<TokenDetailsResponse>() {
                            @Override
                            public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                consumeTokenSuccesResponse(start, tokenDetailsResponse, callback);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                consumeTokenErrorResponse(start, error, callback);
                            }
                        });
            } else {
                midtransPaymentAPI.getTokenTwoClick(
                        cardTokenRequest.getCardCVV(),
                        cardTokenRequest.getSavedTokenId(),
                        cardTokenRequest.isTwoClick(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.getGrossAmount(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.getClientKey(), new Callback<TokenDetailsResponse>() {
                            @Override
                            public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                consumeTokenSuccesResponse(start, tokenDetailsResponse, callback);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                consumeTokenErrorResponse(start, error, callback);
                            }
                        });
            }

        } else {
            if (cardTokenRequest.isInstalment()) {
                midtransPaymentAPI.get3DSTokenInstalmentOffers(cardTokenRequest.getCardNumber(),
                        cardTokenRequest.getCardCVV(),
                        cardTokenRequest.getCardExpiryMonth(), cardTokenRequest
                                .getCardExpiryYear(),
                        cardTokenRequest.getClientKey(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.isTwoClick(),
                        cardTokenRequest.getGrossAmount(),
                        cardTokenRequest.isInstalment(),
                        cardTokenRequest.getFormattedInstalmentTerm(), new Callback<TokenDetailsResponse>() {
                            @Override
                            public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                consumeTokenSuccesResponse(start, tokenDetailsResponse, callback);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                consumeTokenErrorResponse(start, error, callback);
                            }
                        });
            } else {
                //normal request
                if (!cardTokenRequest.isSecure()) {
                    midtransPaymentAPI.getToken(
                            cardTokenRequest.getCardNumber(),
                            cardTokenRequest.getCardCVV(),
                            cardTokenRequest.getCardExpiryMonth(),
                            cardTokenRequest.getCardExpiryYear(),
                            cardTokenRequest.getClientKey(),
                            new Callback<TokenDetailsResponse>() {
                                @Override
                                public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                    consumeTokenSuccesResponse(start, tokenDetailsResponse, callback);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    consumeTokenErrorResponse(start, error, callback);
                                }
                            }
                    );
                } else {
                    midtransPaymentAPI.get3DSToken(cardTokenRequest.getCardNumber(),
                            cardTokenRequest.getCardCVV(),
                            cardTokenRequest.getCardExpiryMonth(),
                            cardTokenRequest.getCardExpiryYear(),
                            cardTokenRequest.getClientKey(),
                            cardTokenRequest.getBank(),
                            cardTokenRequest.isSecure(),
                            cardTokenRequest.isTwoClick(),
                            cardTokenRequest.getGrossAmount(), new Callback<TokenDetailsResponse>() {
                                @Override
                                public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                    consumeTokenSuccesResponse(start, tokenDetailsResponse, callback);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    consumeTokenErrorResponse(start, error, callback);
                                }
                            });
                }
            }

        }
    }

    private void consumeTokenSuccesResponse(long start, TokenDetailsResponse tokenDetailsResponse, CardTokenCallback callback) {
        releaseResources();

        long end = System.currentTimeMillis();

        if (tokenDetailsResponse != null) {
            if (isSDKLogEnabled) {
                displayTokenResponse(tokenDetailsResponse);
            }
            if (tokenDetailsResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))) {
                callback.onSuccess(tokenDetailsResponse);

                // Track Mixpanel event
                analyticsManager.trackMixpanel(KEY_TOKENIZE_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
            } else {
                if (!TextUtils.isEmpty(tokenDetailsResponse.getStatusMessage())) {
                    callback.onFailure(tokenDetailsResponse, tokenDetailsResponse.getStatusMessage());

                    // Track Mixpanel event
                    analyticsManager.trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, tokenDetailsResponse.getStatusMessage());
                } else {
                    callback.onFailure(tokenDetailsResponse,
                            context.getString(R.string.error_empty_response));

                    analyticsManager.trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start,
                            context.getString(R.string.error_empty_response));
                }
            }
        } else {
            callback.onError(new Throwable(context.getString(R.string.error_empty_response)));

            analyticsManager.trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start,
                    context.getString(R.string.error_empty_response));

            Logger.e(TAG, context.getString(R.string.error_empty_response));
        }
    }

    private void consumeTokenErrorResponse(long start, RetrofitError e, CardTokenCallback callback) {
        releaseResources();
        long end = System.currentTimeMillis();

        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
            Logger.e(TAG, "Error in SSL Certificate. " + e.getMessage());
        }
        callback.onError(new Throwable(e.getMessage(), e.getCause()));
        // Track Mixpanel event
        analyticsManager.trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, e.getMessage());
    }
}
