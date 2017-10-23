package com.midtrans.sdk.uikit.views.mandiri_clickpay;

import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.params.NewMandiriClickPaymentParams;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;

/**
 * Created by ziahaqi on 10/16/17.
 */

public class MandiriClickPayPresenter extends BasePaymentPresenter<MandiriClickPayView> {

    public MandiriClickPayPresenter(MandiriClickPayView view) {
        super();
        this.view = view;
    }


    public void getCardToken(String cardNumber) {
        final CardTokenRequest request = new CardTokenRequest(cardNumber, null, null, null, getMidtransSDK().getClientKey());

        MidtransSDK.getInstance()
                .getCardToken(request, new CardTokenCallback() {
                    @Override
                    public void onSuccess(TokenDetailsResponse response) {
                        view.onGetCardTokenSuccess(response);
                    }

                    @Override
                    public void onFailure(TokenDetailsResponse response, String reason) {
                        view.onGetCardTokenFailure(response);
                    }

                    @Override
                    public void onError(Throwable error) {
                        view.onGetCardTokenError(error);
                    }
                });
    }

    public void startPayment(String tokenId, String challengeToken, String input3) {
        String snapToken = getMidtransSDK().readAuthenticationToken();
        NewMandiriClickPaymentParams paymentParams = new NewMandiriClickPaymentParams(tokenId, challengeToken, input3);
        MidtransSDK.getInstance().paymentUsingMandiriClickPay(snapToken, paymentParams, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });

    }
}
