package com.midtrans.sdk.corekit.core.payment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.merchant.model.savecard.SaveCardResponse;
import com.midtrans.sdk.corekit.core.api.midtrans.model.cardtoken.CardTokenRequest;
import com.midtrans.sdk.corekit.core.api.midtrans.model.registration.CreditCardTokenizeResponse;
import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CreditCardResponse;
import com.midtrans.sdk.corekit.utilities.Constants;

import java.util.List;

import static com.midtrans.sdk.corekit.utilities.ValidationHelper.isNotEmpty;

public class CreditCardCharge extends BaseGroupPayment {

    /**
     * It will run backgrond task to charge payment using Credit Card
     *
     * @param snapToken authentication token
     * @param callback  transaction callback
     */
    public static void paymentUsingCard(final String snapToken,
                                        final CreditCardPaymentParams creditCardPaymentParams,
                                        final CustomerDetailPayRequest customerDetailPayRequest,
                                        final MidtransCallback<CreditCardResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingCreditCard(snapToken,
                    creditCardPaymentParams,
                    customerDetailPayRequest,
                    callback);
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
    public static void tokenizeCard(final String cardNumber,
                                    final String cardCvv,
                                    final String cardExpMonth,
                                    final String cardExpYear,
                                    final MidtransCallback<CreditCardTokenizeResponse> callback) {

        if (isValidForNetworkCall(callback)) {
            getMidtransServiceManager().tokenizeCard(cardNumber,
                    cardCvv,
                    cardExpMonth,
                    cardExpYear,
                    getClientKey(),
                    callback);
        }
    }

    /**
     * It will execute an api request to retrieve a authentication token.
     *
     * @param cardTokenRequest get card token  request object
     * @param callback         get card token callback
     */
    public static void getCardToken(final CardTokenRequest cardTokenRequest,
                                    final MidtransCallback<TokenDetailsResponse> callback) {

        if (isNotEmpty(cardTokenRequest)) {
            if (isValidForNetworkCall(callback)) {
                getMidtransServiceManager().getCardToken(cardTokenRequest, callback);
            }
        } else {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
        }
    }

    /**
     * It will run backround task to save card to merchant server
     *
     * @param userId   id user
     * @param requests save card request model
     * @param callback save card callback
     */
    public static void saveCards(final String userId,
                                 final List<SaveCardRequest> requests,
                                 final MidtransCallback<SaveCardResponse> callback) {
        if (isNotEmpty(requests)) {
            if (isValidForNetworkCall(callback)) {
                getMerchantApiManager().saveCards(userId, requests, callback);
            }
        } else {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
        }
    }
}