package com.midtrans.sdk.core.api.papi;

import android.text.TextUtils;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.Channel;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.papi.CardTokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rakawm on 10/19/16.
 */

public class MidtransApiManager {

    private final MidtransApi midtransApi;

    public MidtransApiManager(MidtransApi midtransApi) {
        this.midtransApi = midtransApi;
    }

    /**
     * Get card token to Midtrans Payment API.
     */
    public void getCardToken(CardTokenRequest cardTokenRequest,
                             final MidtransCoreCallback<CardTokenResponse> callback) {
        String clientKey = MidtransCore.getInstance().getClientKey();
        Call<CardTokenResponse> cardTokenResponseCall;

        if (cardTokenRequest.isTwoClick()) {
            cardTokenResponseCall = midtransApi.getCardToken(
                    cardTokenRequest.getCardCvv(),
                    cardTokenRequest.getSavedTokenId(),
                    cardTokenRequest.isTwoClick(),
                    cardTokenRequest.getGrossAmount(),
                    cardTokenRequest.isSecure(),
                    clientKey,
                    cardTokenRequest.getType(),
                    cardTokenRequest.getBank(),
                    cardTokenRequest.isInstallment(),
                    cardTokenRequest.getInstallmentTerm(),
                    !TextUtils.isEmpty(cardTokenRequest.getChannel()) ? cardTokenRequest.getChannel() : Channel.DRAGON
            );
        } else {
            if (cardTokenRequest.isSecure()) {
                cardTokenResponseCall = midtransApi.getCardToken(
                        cardTokenRequest.getCardNumber(),
                        cardTokenRequest.getCardCvv(),
                        cardTokenRequest.getCardExpiryMonth(),
                        cardTokenRequest.getCardExpiryYear(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.getGrossAmount(),
                        clientKey,
                        cardTokenRequest.getType(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.isInstallment(),
                        cardTokenRequest.getInstallmentTerm(),
                        !TextUtils.isEmpty(cardTokenRequest.getChannel()) ? cardTokenRequest.getChannel() : Channel.DRAGON
                );
            } else {
                cardTokenResponseCall = midtransApi.getCardToken(
                        cardTokenRequest.getCardNumber(),
                        cardTokenRequest.getCardCvv(),
                        cardTokenRequest.getCardExpiryMonth(),
                        cardTokenRequest.getCardExpiryYear(),
                        clientKey,
                        cardTokenRequest.getType(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.isInstallment(),
                        cardTokenRequest.getInstallmentTerm(),
                        !TextUtils.isEmpty(cardTokenRequest.getChannel()) ? cardTokenRequest.getChannel() : Channel.DRAGON
                );
            }
        }

        // Do the API call
        cardTokenResponseCall.enqueue(new Callback<CardTokenResponse>() {
            @Override
            public void onResponse(Call<CardTokenResponse> call, Response<CardTokenResponse> response) {
                handleGetTokenCardResponse(response, callback);
            }

            @Override
            public void onFailure(Call<CardTokenResponse> call, Throwable throwable) {
                callback.onError(throwable);
            }
        });
    }

    private void handleGetTokenCardResponse(Response<CardTokenResponse> response, MidtransCoreCallback<CardTokenResponse> callback) {
        if (response.isSuccessful()
                && response.code() == 200
                && response.body().statusCode.equals("200")) {
            callback.onSuccess(response.body());
        } else {
            callback.onFailure(response.body());
        }
    }
}
