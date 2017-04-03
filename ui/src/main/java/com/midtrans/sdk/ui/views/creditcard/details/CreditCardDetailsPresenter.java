package com.midtrans.sdk.ui.views.creditcard.details;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.CreditCard;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;

/**
 * Created by rakawm on 3/27/17.
 */

public class CreditCardDetailsPresenter extends BasePaymentPresenter {

    private MidtransCoreCallback<CardTokenResponse> getCardTokenCallback;
    private MidtransCoreCallback<CreditCardPaymentResponse> paymentCallback;

    private CreditCardDetailsView cardDetailsView;
    private String cardToken;
    private PaymentResult<CreditCardPaymentResponse> paymentResult;

    public void init(CreditCardDetailsView view) {
        this.cardDetailsView = view;
        initCallbacks();
    }

    void startTokenize(String cardNumber, String expiryMonth, String expiryYear, String cvv) {
        SnapTransaction transaction = MidtransUi.getInstance().getTransaction();
        CreditCard creditCard = transaction.creditCard;
        if (creditCard.secure) {
            CardTokenRequest cardTokenRequest = CardTokenRequest.newNormalSecureCard(
                    cardNumber,
                    cvv,
                    expiryMonth,
                    expiryYear,
                    true,
                    MidtransUi.getInstance().getTransaction().transactionDetails.grossAmount);
            MidtransCore.getInstance().getCardToken(cardTokenRequest, getCardTokenCallback);
        } else {
            CardTokenRequest cardTokenRequest = CardTokenRequest.newNormalCard(
                    cardNumber,
                    cvv,
                    expiryMonth,
                    expiryYear
            );
            MidtransCore.getInstance().getCardToken(cardTokenRequest, getCardTokenCallback);
        }
    }

    void startPayment() {
        MidtransCore.getInstance().paymentUsingCreditCard(
                MidtransUi.getInstance().getTransaction().token,
                CreditCardPaymentParams.newBasicPaymentParams(cardToken),
                UiUtils.buildCustomerDetails(),
                paymentCallback);
    }

    private void initCallbacks() {
        getCardTokenCallback = new MidtransCoreCallback<CardTokenResponse>() {
            @Override
            public void onSuccess(CardTokenResponse object) {
                setCardToken(object.tokenId);
                cardDetailsView.onGetCardTokenSuccess(object);
            }

            @Override
            public void onFailure(CardTokenResponse object) {
                cardDetailsView.onGetCardTokenFailure(object);
            }

            @Override
            public void onError(Throwable throwable) {
                cardDetailsView.onGetCardTokenError(throwable.getMessage());
            }
        };

        paymentCallback = new MidtransCoreCallback<CreditCardPaymentResponse>() {
            @Override
            public void onSuccess(CreditCardPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                cardDetailsView.onCreditCardPaymentSuccess(object);
            }

            @Override
            public void onFailure(CreditCardPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                cardDetailsView.onCreditCardPaymentFailure(object);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                cardDetailsView.onCreditCardPaymentError(throwable);
            }
        };
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public PaymentResult<CreditCardPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
