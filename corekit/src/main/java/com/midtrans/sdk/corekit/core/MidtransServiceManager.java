package com.midtrans.sdk.corekit.core;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ziahaqi on 3/27/18.
 */

public class MidtransServiceManager extends BaseServiceManager {
    private static final String TAG = MerchantServiceManager.class.getSimpleName();
    private MidtransApiService service;

    public MidtransServiceManager(MidtransApiService service) {
        this.service = service;
    }

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
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<CardRegistrationResponse> call = service.registerCard(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey);
        call.enqueue(new Callback<CardRegistrationResponse>() {
            @Override
            public void onResponse(Call<CardRegistrationResponse> call, Response<CardRegistrationResponse> response) {
                releaseResources();
                CardRegistrationResponse cardRegistrationResponse = response.body();
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
            public void onFailure(Call<CardRegistrationResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * It will execute an api call to get token from server, and after completion of request it
     *
     * @param cardTokenRequest information about credit card.
     * @param callback         get creditcard token callback
     */
    public void getToken(CardTokenRequest cardTokenRequest, final CardTokenCallback callback) {

        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TokenDetailsResponse> call;
        if (cardTokenRequest.isTwoClick()) {

            if (cardTokenRequest.isInstallment()) {
                call = service.getTokenInstalmentOfferTwoClick(
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
                        cardTokenRequest.isPoint());
            } else {
                call = service.getTokenTwoClick(
                        cardTokenRequest.getCardCVV(),
                        cardTokenRequest.getSavedTokenId(),
                        cardTokenRequest.isTwoClick(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.getGrossAmount(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.getClientKey(),
                        cardTokenRequest.getChannel(),
                        cardTokenRequest.getType(),
                        cardTokenRequest.isPoint());
            }

        } else {
            if (cardTokenRequest.isInstallment()) {

                call = service.get3DSTokenInstalmentOffers(cardTokenRequest.getCardNumber(),
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
                        cardTokenRequest.isPoint());

            } else {
                //normal request
                if (!cardTokenRequest.isSecure()) {

                    call = service.getToken(
                            cardTokenRequest.getCardNumber(),
                            cardTokenRequest.getCardCVV(),
                            cardTokenRequest.getCardExpiryMonth(),
                            cardTokenRequest.getCardExpiryYear(),
                            cardTokenRequest.getClientKey(),
                            cardTokenRequest.getGrossAmount(),
                            cardTokenRequest.getChannel(),
                            cardTokenRequest.getType(),
                            cardTokenRequest.isPoint());
                } else {
                    call = service.get3DSToken(cardTokenRequest.getCardNumber(),
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
                            cardTokenRequest.isPoint());
                }
            }

        }

        call.enqueue(new Callback<TokenDetailsResponse>() {
            @Override
            public void onResponse(Call<TokenDetailsResponse> call, Response<TokenDetailsResponse> response) {
                doOnGetCardTokenSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TokenDetailsResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }


    private void doOnGetCardTokenSuccess(Response<TokenDetailsResponse> response, CardTokenCallback callback) {
        releaseResources();

        TokenDetailsResponse tokenDetailsResponse = response.body();

        if (tokenDetailsResponse != null) {
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

    public void setService(MidtransApiService service) {
        this.service = service;
    }
}
