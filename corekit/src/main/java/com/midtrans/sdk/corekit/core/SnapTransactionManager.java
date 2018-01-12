package com.midtrans.sdk.corekit.core;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.BanksPointCallback;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.GoPayResendAuthorizationCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.GoPayResendAuthorizationResponse;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.DanamonOnlinePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayAuthorizationRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.MandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.NewMandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;

import javax.net.ssl.SSLHandshakeException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.ConversionException;

/**
 * Created by ziahaqi on 7/18/16.
 */
public class SnapTransactionManager extends BaseTransactionManager {
    private static final String TAG = "TransactionManager";
    private static final String KEY_ERROR_MESSAGE = "error_messages";

    private SnapRestAPI snapRestAPI;

    SnapTransactionManager(SnapRestAPI snapRestAPI, MerchantRestAPI merchantApiClient, MidtransRestAPI midtransRestAPI) {
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
        if (merchantPaymentAPI == null) {
            String errorMessage = Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL;
            callback.onError(new Throwable(errorMessage));
            Logger.e(TAG, "checkout():" + errorMessage);
            return;
        }

        merchantPaymentAPI.checkout(model, new Callback<Token>() {
            @Override
            public void success(Token snapTokenDetailResponse, Response response) {
                releaseResources();
                if (snapTokenDetailResponse != null) {
                    if (snapTokenDetailResponse.getTokenId() != null && !snapTokenDetailResponse.getTokenId().equals("")) {
                        callback.onSuccess(snapTokenDetailResponse);
                    } else {
                        callback.onFailure(snapTokenDetailResponse, Constants.MESSAGE_ERROR_EMPTY_RESPONSE);
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void failure(RetrofitError e) {
                try {
                    releaseResources();

                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                        Logger.i(TAG, "Error in SSL Certificate. " + e.getMessage());
                    }

                    callback.onError(new Throwable(e.getMessage(), e.getCause()));

                } catch (Exception ex) {
                    callback.onError(new Throwable(ex.getMessage(), ex.getCause()));
                }
            }
        });
    }


    /**
     * This will create a HTTP request to Snap to get transaction option.
     *
     * @param snapToken Snap Token.
     * @param callback  callback of payment option
     */
    public void getTransactionOptions(@NonNull final String snapToken, final TransactionOptionsCallback callback) {
        snapRestAPI.getPaymentOption(snapToken, new Callback<Transaction>() {
            @Override
            public void success(Transaction transaction, Response response) {
                releaseResources();

                if (transaction != null) {
                    if (response.getStatus() == 200 && !transaction.getTransactionDetails().getOrderId().equals("")) {
                        callback.onSuccess(transaction);
                    } else {
                        callback.onFailure(transaction, response.getReason());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                    Logger.e(TAG, Constants.MESSAGE_ERROR_EMPTY_RESPONSE);
                }
            }

            @Override
            public void failure(RetrofitError e) {
                try {
                    releaseResources();

                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + e.getMessage());
                    }

                    JsonObject jsonObject = (JsonObject) e.getBodyAs(JsonObject.class);
                    if (jsonObject != null && jsonObject.getAsJsonArray(KEY_ERROR_MESSAGE) != null) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray(KEY_ERROR_MESSAGE);
                        String errorMessage = e.getMessage();
                        if (jsonArray.get(0) != null) {
                            errorMessage = jsonArray.get(0).toString();
                        }
                        callback.onError(new Throwable(errorMessage, e.getCause()));

                    } else {
                        callback.onError(new Throwable(e.getMessage(), e.getCause()));
                    }
                } catch (Exception ex) {
                    callback.onError(new Throwable(ex.getMessage(), ex.getCause()));
                }
            }
        });
    }

    /**
     * This method is used for card payment using snap backend.
     *
     * @param requestModel Payment details.
     * @param callback     Transaction callback
     */

    public void paymentUsingCreditCard(final String authenticationToken, CreditCardPaymentRequest requestModel, final TransactionCallback callback) {
        if (requestModel != null) {
            snapRestAPI.paymentUsingCreditCard(authenticationToken, requestModel, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }


    /**
     * This method is used for Payment Using Bank Transfer BCA
     *
     * @param paymentRequest Payment Details.
     * @param callback       Transaction callback
     */
    public void paymentUsingBankTransferBCA(final String authenticationToken, BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBankTransfer(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer BNI
     *
     * @param paymentRequest Payment Details.
     * @param callback       Transaction callback
     */
    public void paymentUsingBankTransferBni(final String authenticationToken, BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBankTransfer(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }


    /**
     * This method is used for Payment Using Bank Transfer Permata
     *
     * @param authenticationToken authentication token
     * @param paymentRequest      payment Details
     * @param callback            transaction callback
     */
    public void paymentUsingBankTransferPermata(final String authenticationToken, final BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBankTransfer(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using Klik BCA.
     *
     * @param request  Payment details
     * @param callback transaction callback
     */
    public void paymentUsingKlikBCA(final String authenticationToken, KlikBCAPaymentRequest request, final TransactionCallback callback) {
        if (request != null) {
            snapRestAPI.paymentUsingKlikBCA(authenticationToken, request, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });

        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using BCA Klik Pay.
     *
     * @param paymentRequest payment request for BCA Klik pay
     * @param callback       transaction callback
     */
    public void paymentUsingBCAKlikpay(final String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBCAKlikPay(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using Mandiri Bill Pay.
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for Mandiri Bill pay
     * @param callback            transaction callback
     */
    public void paymentUsingMandiriBillPay(final String authenticationToken, BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriBillPay(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using Mandiri Click Pay.
     * <p>
     * Deprecated, please see {@link com.midtrans.sdk.corekit.core.SnapTransactionManager#paymentUsingMandiriClickPay(String, NewMandiriClickPayPaymentRequest, TransactionCallback)}
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for Mandiri Click Pay
     * @param callback            transaction callback
     */
    @Deprecated
    public void paymentUsingMandiriClickPay(final String authenticationToken, MandiriClickPayPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriClickPay(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using new flow of Mandiri Click Pay.
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for Mandiri Click Pay
     * @param callback            transaction callback
     */
    public void paymentUsingMandiriClickPay(final String authenticationToken, NewMandiriClickPayPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriClickPay(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using CIMB Click.
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for CIMB Click
     * @param callback            transaction callback
     */
    public void paymentUsingCIMBClick(final String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingCIMBClick(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using BRI Epay.
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for BRI Epay.
     * @param callback            transaction callback
     */
    public void paymentUsingBRIEpay(final String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBRIEpay(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using Mandiri E-Cash
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for Mandiri E-Cash
     * @param callback            transaction callbaack
     */
    public void paymentUsingMandiriEcash(final String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingMandiriEcash(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using Telkomsel E-Cash
     *
     * @param paymentRequest payment request for Telkomsel E-Cash
     * @param callback       transaction callback
     */
    public void paymentUsingTelkomselCash(final String authenticationToken, TelkomselEcashPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingTelkomselEcash(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using XL Tunai
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for XL Tunai
     * @param callback            transaction callback
     */
    public void paymentUsingXLTunai(final String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingXlTunai(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using Indomaret
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for Indomaret
     * @param callback            Transaction callback
     */
    public void paymentUsingIndomaret(final String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingIndomaret(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using Indosat Dompetku
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for Indosat Dompetku
     * @param callback            transaction callback
     */
    public void paymentUsingIndosatDompetku(final String authenticationToken, IndosatDompetkuPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingIndosatDompetku(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using Kiosan
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for Kiosan
     * @param callback            transaction callback
     */
    public void paymentUsingKiosan(final String authenticationToken, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingKiosan(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }


    public void paymentUsingGCI(final String authenticationToken, GCIPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingGCI(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using Bank Transfer
     *
     * @param authenticationToken
     * @param paymentRequest      payment request for Bank Transfer
     * @param callback            transaction callback
     */
    public void paymentUsingBankTransferAllBank(final String authenticationToken, BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingBankTransfer(authenticationToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using GoPay
     *
     * @param paymentRequest
     * @param callback
     */
    public void paymentUsingGoPay(String snapToken, GoPayPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingGoPay(snapToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for payment using GoPay
     *
     * @param request  GoPayAuthorizationRequest
     * @param callback
     */
    public void authorizeGoPayPayment(String snapToken, GoPayAuthorizationRequest request, final TransactionCallback callback) {
        if (request != null) {
            snapRestAPI.authorizeGoPayPayment(snapToken, request, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * This method is used for resend  GoPay otp authorization
     *
     * @param snapToken Snap Token
     * @param callback
     */
    public void resendGoPayAuthorization(String snapToken, final GoPayResendAuthorizationCallback callback) {
        if (snapToken != null) {
            snapRestAPI.resendGoPayAuthorization(snapToken, new Callback<GoPayResendAuthorizationResponse>() {
                @Override
                public void success(GoPayResendAuthorizationResponse response, Response retrofitResponse) {
                    releaseResources();

                    if (response != null) {
                        if (response.getStatusCode() != null && response.getStatusCode().equals(Constants.STATUS_CODE_200)) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure(response, retrofitResponse.getReason());
                        }
                    } else {
                        callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
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
        } else {
            releaseResources();
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
        }
    }


    /**
     * This method is used for payment using Danamon Online
     *
     * @param snapToken      SnapToken
     * @param paymentRequest DanamonOnlinePaymentRequest
     * @param callback       TransactionCallback
     */
    public void paymentUsingDanamonOnline(String snapToken, DanamonOnlinePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (paymentRequest != null) {
            snapRestAPI.paymentUsingDanamonOnline(snapToken, paymentRequest, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse transactionResponse, Response response) {
                    doOnPaymentResponseSuccess(transactionResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnPaymentResponseFailure(error, callback);
                }
            });
        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    private void doOnPaymentResponseFailure(RetrofitError error, TransactionCallback callback) {
        releaseResources();

        try {
            if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
            }

            callback.onError(new Throwable(error.getMessage(), error.getCause()));
        } catch (Exception e) {
            callback.onError(new Throwable(e.getMessage(), e.getCause()));
        }
    }

    private void doOnPaymentResponseSuccess(TransactionResponse transactionResponse, Response response, TransactionCallback callback) {
        releaseResources();

        if (isSDKLogEnabled) {
            displayResponse(transactionResponse);
        }

        if (transactionResponse != null) {
            String statusCode = transactionResponse.getStatusCode();
            if (!TextUtils.isEmpty(statusCode)
                    && (transactionResponse.getStatusCode().equals(Constants.STATUS_CODE_200)
                    || transactionResponse.getStatusCode().equals(Constants.STATUS_CODE_201))) {

                callback.onSuccess(transactionResponse);

            } else {

                if (statusCode.equals(Constants.STATUS_CODE_400)) {
                    String message;
                    if (transactionResponse.getValidationMessages() != null && !transactionResponse.getValidationMessages().isEmpty()) {
                        message = transactionResponse.getValidationMessages().get(0);
                    } else {
                        message = transactionResponse.getStatusMessage();
                    }
                    callback.onFailure(transactionResponse, message);
                } else {
                    callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                }

            }
        } else {
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
        }
    }

    private void doOnInvalidDataSupplied(TransactionCallback callback) {
        releaseResources();
        callback.onError(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
    }


    /**
     * This method is used to save credit cards to merchant server
     *
     * @param cardRequests credit card Request model
     * @param userId       unique id for every user
     * @param callback     save card callback
     */
    public void saveCards(String userId, ArrayList<SaveCardRequest> cardRequests, final SaveCardCallback callback) {
        if (merchantPaymentAPI == null) {
            String errorMessage = Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL;
            callback.onError(new Throwable(errorMessage));
            Logger.e(TAG, "saveCards():" + errorMessage);
            return;
        }

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
                    try {
                        releaseResources();

                        if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                            Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                        }

                        if (error.getCause() instanceof ConversionException) {
                            SaveCardResponse saveCardResponse = new SaveCardResponse();
                            saveCardResponse.setCode(200);
                            saveCardResponse.setMessage(error.getMessage());
                            callback.onSuccess(saveCardResponse);
                        } else {
                            callback.onError(new Throwable(error.getMessage(), error.getCause()));
                        }

                    } catch (Exception e) {
                        callback.onError(new Throwable(e.getMessage(), e.getCause()));
                    }
                }
            });
        } else {
            releaseResources();
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
        }
    }

    /**
     * this method is used to get saved card list on merchant server
     *
     * @param userId   unique id to each user
     * @param callback Transaction callback
     */
    public void getCards(String userId, final GetCardCallback callback) {
        if (merchantPaymentAPI == null) {
            String errorMessage = Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL;
            callback.onError(new Throwable(errorMessage));
            Logger.e(TAG, "getCards():" + errorMessage);
            return;
        }

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
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    releaseResources();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }

                    callback.onError(new Throwable(error.getMessage(), error.getCause()));

                } catch (Exception e) {
                    callback.onError(new Throwable(e.getMessage(), e.getCause()));
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
                    String statusCode = cardRegistrationResponse.getStatusCode();
                    if (!TextUtils.isEmpty(statusCode) && statusCode.equals(Constants.STATUS_CODE_200)) {
                        callback.onSuccess(cardRegistrationResponse);
                    } else {
                        callback.onFailure(cardRegistrationResponse, cardRegistrationResponse.getStatusMessage());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void failure(RetrofitError e) {
                try {
                    releaseResources();
                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + e.getMessage());
                    }
                    callback.onError(new Throwable(e.getMessage(), e.getCause()));

                } catch (Exception exception) {
                    callback.onError(new Throwable(exception.getMessage(), exception.getCause()));
                }
            }
        });
    }

    /**
     * It will execute an api call to get token from server, and after completion of request it
     *
     * @param cardTokenRequest information about credit card.
     * @param callback         get creditcard token callback
     */
    public void getToken(final String authenticationToken, @NonNull CardTokenRequest cardTokenRequest, @NonNull final CardTokenCallback callback) {
        final long start = System.currentTimeMillis();
        if (cardTokenRequest.isTwoClick()) {
            if (cardTokenRequest.isInstallment()) {
                midtransPaymentAPI.getTokenInstalmentOfferTwoClick(
                        cardTokenRequest.getCardCVV(),
                        cardTokenRequest.getSavedTokenId(),
                        cardTokenRequest.isTwoClick(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.getGrossAmount(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.getClientKey(),
                        cardTokenRequest.isInstallment(),
                        cardTokenRequest.getFormattedInstalmentTerm(),
                        cardTokenRequest.getChannel(),
                        cardTokenRequest.getType(),
                        cardTokenRequest.isPoint(),
                        new Callback<TokenDetailsResponse>() {
                            @Override
                            public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                consumeTokenSuccesResponse(authenticationToken, start, tokenDetailsResponse, callback);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                consumeTokenErrorResponse(authenticationToken, start, error, callback);
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
                        cardTokenRequest.getClientKey(),
                        cardTokenRequest.getChannel(),
                        cardTokenRequest.getType(),
                        cardTokenRequest.isPoint(),
                        new Callback<TokenDetailsResponse>() {
                            @Override
                            public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                consumeTokenSuccesResponse(authenticationToken, start, tokenDetailsResponse, callback);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                consumeTokenErrorResponse(authenticationToken, start, error, callback);
                            }
                        });
            }

        } else {
            if (cardTokenRequest.isInstallment()) {
                midtransPaymentAPI.get3DSTokenInstalmentOffers(cardTokenRequest.getCardNumber(),
                        cardTokenRequest.getCardCVV(),
                        cardTokenRequest.getCardExpiryMonth(), cardTokenRequest
                                .getCardExpiryYear(),
                        cardTokenRequest.getClientKey(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.isTwoClick(),
                        cardTokenRequest.getGrossAmount(),
                        cardTokenRequest.isInstallment(),
                        cardTokenRequest.getChannel(),
                        cardTokenRequest.getFormattedInstalmentTerm(),
                        cardTokenRequest.getType(),
                        cardTokenRequest.isPoint(),
                        new Callback<TokenDetailsResponse>() {
                            @Override
                            public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                consumeTokenSuccesResponse(authenticationToken, start, tokenDetailsResponse, callback);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                consumeTokenErrorResponse(authenticationToken, start, error, callback);
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
                            cardTokenRequest.getChannel(),
                            cardTokenRequest.getType(),
                            cardTokenRequest.isPoint(),
                            new Callback<TokenDetailsResponse>() {
                                @Override
                                public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                    consumeTokenSuccesResponse(authenticationToken, start, tokenDetailsResponse, callback);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    consumeTokenErrorResponse(authenticationToken, start, error, callback);
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
                            cardTokenRequest.getGrossAmount(),
                            cardTokenRequest.getChannel(),
                            cardTokenRequest.getType(),
                            cardTokenRequest.isPoint(),
                            new Callback<TokenDetailsResponse>() {
                                @Override
                                public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                    consumeTokenSuccesResponse(authenticationToken, start, tokenDetailsResponse, callback);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    consumeTokenErrorResponse(authenticationToken, start, error, callback);
                                }
                            });
                }
            }

        }
    }

    public void deleteCard(String authenticationToken, String maskedCard, final DeleteCardCallback deleteCardCallback) {
        snapRestAPI.deleteCard(authenticationToken, maskedCard, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                releaseResources();
                if (response.getStatus() == 200 || response.getStatus() == 201) {
                    deleteCardCallback.onSuccess(aVoid);
                } else {
                    deleteCardCallback.onFailure(aVoid);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                releaseResources();
                if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                    Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                }
                deleteCardCallback.onError(new Throwable(error.getMessage(), error.getCause()));
            }
        });
    }

    private void consumeTokenSuccesResponse(final String authenticationToken, long start, TokenDetailsResponse tokenDetailsResponse, CardTokenCallback callback) {
        releaseResources();

        if (tokenDetailsResponse != null) {
            if (isSDKLogEnabled) {
                displayTokenResponse(tokenDetailsResponse);
            }
            if (tokenDetailsResponse.getStatusCode().trim().equalsIgnoreCase(Constants.STATUS_CODE_200)) {
                callback.onSuccess(tokenDetailsResponse);
            } else {
                if (!TextUtils.isEmpty(tokenDetailsResponse.getStatusMessage())) {
                    callback.onFailure(tokenDetailsResponse, tokenDetailsResponse.getStatusMessage());
                } else {
                    callback.onFailure(tokenDetailsResponse,
                            Constants.MESSAGE_ERROR_EMPTY_RESPONSE);
                }
            }
        } else {
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
            Logger.e(TAG, Constants.MESSAGE_ERROR_EMPTY_RESPONSE);
        }
    }

    private void consumeTokenErrorResponse(String authenticationToken, long start, RetrofitError e, CardTokenCallback callback) {
        try {
            releaseResources();

            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                Logger.e(TAG, "Error in SSL Certificate. " + e.getMessage());
            }

            callback.onError(new Throwable(e.getMessage(), e.getCause()));

        } catch (Exception ex) {
            callback.onError(new Throwable(ex.getMessage(), ex.getCause()));
        }
    }


    /**
     * it will be used to get bank bins on the snap API
     *
     * @param callback BankbinsCallback instance
     */
    public void getBankBins(final BankBinsCallback callback) {
        snapRestAPI.getBankBins(new Callback<ArrayList<BankBinsResponse>>() {
            @Override
            public void success(ArrayList<BankBinsResponse> bankBinsResponses, Response response) {
                releaseResources();

                if (bankBinsResponses != null && !bankBinsResponses.isEmpty()) {
                    if (response.getStatus() == 200 || response.getStatus() == 201) {
                        callback.onSuccess(bankBinsResponses);
                    } else {
                        callback.onFailure(response.getReason());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    releaseResources();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }

                    callback.onError(new Throwable(error.getMessage(), error.getCause()));

                } catch (Exception ex) {
                    callback.onError(new Throwable(ex.getMessage(), ex.getCause()));
                }
            }
        });
    }

    /**
     * @param authenticationToken snap token
     * @param cardToken           credit card token
     * @param callback            BNI points callback instance
     */
    public void getBanksPoint(String authenticationToken, String cardToken, final BanksPointCallback callback) {

        snapRestAPI.getBanksPoint(authenticationToken, cardToken, new Callback<BanksPointResponse>() {
            @Override
            public void success(BanksPointResponse bankPointResponse, Response response) {
                releaseResources();
                if (bankPointResponse != null) {
                    if (bankPointResponse.getStatusCode() != null && bankPointResponse.getStatusCode().equals(Constants.STATUS_CODE_200)) {
                        callback.onSuccess(bankPointResponse);
                    } else {
                        callback.onFailure(response.getReason());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    releaseResources();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));

                } catch (Exception ex) {
                    callback.onError(new Throwable(ex.getMessage(), ex.getCause()));
                }
            }
        });

    }

    public void getTransactionStatus(String snapToken, final GetTransactionStatusCallback callback) {
        snapRestAPI.getTransactionStatus(snapToken, new Callback<TransactionStatusResponse>() {
            @Override
            public void success(TransactionStatusResponse transactionStatusResponse, Response response) {
                releaseResources();
                if (transactionStatusResponse != null) {
                    if (transactionStatusResponse.getStatusCode() != null && transactionStatusResponse.getStatusCode().equals(Constants.STATUS_CODE_200)) {
                        callback.onSuccess(transactionStatusResponse);
                    } else {
                        callback.onFailure(transactionStatusResponse, response.getReason());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    releaseResources();
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }
                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                } catch (Exception ex) {
                    callback.onError(new Throwable(ex.getMessage(), ex.getCause()));
                }
            }
        });
    }
}
