package com.midtrans.sdk.corekit.core.grouppayment;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.midtrans.CardTokenRequest;
import com.midtrans.sdk.corekit.core.midtrans.response.CardRegistrationResponse;
import com.midtrans.sdk.corekit.core.midtrans.response.SaveCardResponse;
import com.midtrans.sdk.corekit.core.midtrans.response.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.creditcard.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.corekit.utilities.Validation;

import java.util.ArrayList;

public class CreditCardCharge extends PaymentsGroupBase {

    /**
     * It will run backgrond task to charge payment using Credit Card
     *
     * @param snapToken authentication token
     * @param callback  transaction callback
     */
    public void paymentUsingCard(@NonNull String snapToken,
                                 @NonNull final CreditCardPaymentParams creditCardPaymentParams,
                                 @NonNull final CustomerDetailPayRequest customerDetailPayRequest,
                                 MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingCreditCard(snapToken, creditCardPaymentParams, customerDetailPayRequest, callback);
        }

    }

    /**
     * It will run backround task to register card PAPI(Payment API) Backend
     *
     * @param cardNumber   credit card number
     * @param cardCvv      credit card cvv
     * @param cardExpMonth credit card expired month
     * @param cardExpYear  credit card expired year
     * @param callback     Credit card registration callback
     */
    public void cardRegistration(@NonNull String cardNumber, @NonNull String clientKey,
                                 @NonNull String cardCvv, @NonNull String cardExpMonth,
                                 @NonNull String cardExpYear, @NonNull MidtransCallback<CardRegistrationResponse> callback) {

        if (isNetworkAvailable(getSdkContext())) {
            getMidtransServiceManager().cardRegistration(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey, callback);
        }
    }

    /**
     * It will execute an api request to retrieve a authentication token.
     *
     * @param cardTokenRequest get card token  request object
     * @param callback         get card token callback
     */
    public void getCardToken(@NonNull CardTokenRequest cardTokenRequest,
                             @NonNull MidtransCallback<TokenDetailsResponse> callback) {

        if (Validation.isNotEmpty(cardTokenRequest)) {
            if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
                getMidtransServiceManager().getToken(cardTokenRequest, callback);
            } else {
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
        }
    }

    /**
     * It will run backround task to get card from merchant server
     *
     * @param userId   id user
     * @param callback Get credit card callback
     */
    public void getCards(@NonNull String userId,
                         @NonNull MidtransCallback<ArrayList<SaveCardRequest>> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
            if (getMerchantApiManager() != null) {
                getMerchantApiManager().getCards(userId, callback);
            } else {
                callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL));
            }
        } else {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to save card to merchant server
     *
     * @param userId   id user
     * @param requests save card request model
     * @param callback save card callback
     */
    public void saveCards(@NonNull String userId, @NonNull ArrayList<SaveCardRequest> requests,
                          @NonNull MidtransCallback<SaveCardResponse> callback) {
        if (requests != null) {
            if (Validation.isNetworkAvailable(getSdkContext())) {
                if (getMerchantApiManager() != null) {
                    getMerchantApiManager().saveCards(userId, requests, callback);
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL));
                }
            } else {
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }
}
