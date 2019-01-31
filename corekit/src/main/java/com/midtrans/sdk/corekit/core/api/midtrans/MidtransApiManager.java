package com.midtrans.sdk.corekit.core.api.midtrans;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.api.midtrans.model.cardtoken.CardTokenRequest;
import com.midtrans.sdk.corekit.core.api.midtrans.model.registration.CreditCardTokenizeResponse;
import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;

import retrofit2.Call;

public class MidtransApiManager extends BaseServiceManager {

    private static final String TAG = "MidtransApiManager";

    private MidtransApiService apiService;

    public MidtransApiManager(MidtransApiService apiService) {
        this.apiService = apiService;
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
    public void tokenizeCard(final String cardNumber,
                             final String cardCvv,
                             final String cardExpMonth,
                             final String cardExpYear,
                             final String clientKey,
                             final MidtransCallback<CreditCardTokenizeResponse> callback) {
        if (apiService == null) {
            doOnApiServiceUnAvailable(callback);
        } else {
            Call<CreditCardTokenizeResponse> call = apiService.tokenizeCard(cardNumber,
                    cardCvv,
                    cardExpMonth,
                    cardExpYear,
                    clientKey);
            handleCall(call, callback);
        }
    }

    /**
     * It will execute an api call to get token from server, and after completion of request it
     *
     * @param cardTokenRequest information about credit card.
     * @param callback         get creditcard token callback
     */
    public void getCardToken(final CardTokenRequest cardTokenRequest,
                             final MidtransCallback<TokenDetailsResponse> callback) {

        if (apiService == null) {
            doOnApiServiceUnAvailable(callback);
        } else {

            Call<TokenDetailsResponse> call;
            if (cardTokenRequest.isTwoClick()) {

                if (cardTokenRequest.isInstallment()) {
                    call = apiService.getTokenInstalmentOfferTwoClick(
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
                            cardTokenRequest.getCurrency(),
                            cardTokenRequest.isPoint());
                } else {
                    call = apiService.getTokenTwoClick(
                            cardTokenRequest.getCardCVV(),
                            cardTokenRequest.getSavedTokenId(),
                            cardTokenRequest.isTwoClick(),
                            cardTokenRequest.isSecure(),
                            cardTokenRequest.getGrossAmount(),
                            cardTokenRequest.getBank(),
                            cardTokenRequest.getClientKey(),
                            cardTokenRequest.getChannel(),
                            cardTokenRequest.getType(),
                            cardTokenRequest.getCurrency(),
                            cardTokenRequest.isPoint());
                }

            } else {
                if (cardTokenRequest.isInstallment()) {

                    call = apiService.get3DSTokenInstalmentOffers(cardTokenRequest.getCardNumber(),
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
                            cardTokenRequest.getCurrency(),
                            cardTokenRequest.isPoint());

                } else {
                    //normal request
                    if (!cardTokenRequest.isSecure()) {

                        call = apiService.getToken(
                                cardTokenRequest.getCardNumber(),
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getCardExpiryMonth(),
                                cardTokenRequest.getCardExpiryYear(),
                                cardTokenRequest.getClientKey(),
                                cardTokenRequest.getGrossAmount(),
                                cardTokenRequest.getChannel(),
                                cardTokenRequest.getType(),
                                cardTokenRequest.getCurrency(),
                                cardTokenRequest.isPoint());
                    } else {
                        call = apiService.get3DSToken(cardTokenRequest.getCardNumber(),
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
                                cardTokenRequest.getCurrency(),
                                cardTokenRequest.isPoint());
                    }
                }

            }
            handleCall(call, callback);
        }
    }

}