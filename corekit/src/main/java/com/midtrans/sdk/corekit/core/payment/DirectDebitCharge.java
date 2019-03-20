package com.midtrans.sdk.corekit.core.payment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.midtrans.model.cardtoken.CardTokenRequest;
import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;
import com.midtrans.sdk.corekit.utilities.Constants;

public class DirectDebitCharge extends BaseGroupPayment {

    /**
     * Start payment using bank transfer and va with Klik BCA.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingKlikBca(final String token,
                                           final String klikBcaUserId,
                                           final MidtransCallback<KlikBcaResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingKlikBca(token, klikBcaUserId, callback);
        }
    }


    /**
     * Start payment using bank transfer and va with Mandiri Ecash.
     *
     * @param token        token after making checkoutWithTransaction.
     * @param cardNumber generated tokenId
     * @param clickpayToken   token from mandiri token
     * @param input3           input3 from mandiri token
     * @param callback         for receiving callback from request.
     */
    public static void paymentUsingMandiriClickPay(final String token,
                                                   final String cardNumber,
                                                   final String clickpayToken,
                                                   final String input3,
                                                   final MidtransCallback<MandiriClickpayResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            CardTokenRequest request = new CardTokenRequest(cardNumber,null,null,null,getClientKey());
            CreditCardCharge.getCardToken(request, new MidtransCallback<TokenDetailsResponse>() {
                @Override
                public void onSuccess(TokenDetailsResponse data) {
                    if (data.getTokenId() != null) {
                        getSnapApiManager().paymentUsingMandiriClickPay(token, data.getTokenId(), clickpayToken, input3, callback);
                    } else {
                        callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILURE_RESPONSE));
                    }
                }

                @Override
                public void onFailed(Throwable throwable) {
                    callback.onFailed(throwable);
                }
            });
        }
    }

}