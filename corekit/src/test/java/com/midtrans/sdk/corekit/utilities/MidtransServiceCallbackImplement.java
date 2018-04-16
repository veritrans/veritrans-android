package com.midtrans.sdk.corekit.utilities;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.core.MidtransServiceManager;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;

/**
 * Created by ziahaqi on 4/6/18.
 */

public class MidtransServiceCallbackImplement implements CardTokenCallback, CardRegistrationCallback {
    private final CallbackCollaborator collaborator;
    private final MidtransServiceManager manager;

    public MidtransServiceCallbackImplement(MidtransServiceManager midtransServiceManager, CallbackCollaborator callbackCollaboratorMock) {
        this.manager = midtransServiceManager;
        this.collaborator = callbackCollaboratorMock;
    }

    @Override
    public void onError(Throwable error) {
        this.collaborator.onError();
    }

    @Override
    public void onSuccess(TokenDetailsResponse response) {
        this.collaborator.onGetCardTokenSuccess();
    }

    @Override
    public void onFailure(TokenDetailsResponse response, String reason) {
        this.collaborator.onGetCardTokenFailed();
    }

    @Override
    public void onSuccess(CardRegistrationResponse response) {
        this.collaborator.onCardRegistrationSuccess();
    }

    @Override
    public void onFailure(CardRegistrationResponse response, String reason) {
        this.collaborator.onCardRegistrationFailed();
    }

    public void registerCard(String cardNumber, String cardCvv, String cardExpiryMonth, String cardExpiryYear, String clientKey) {
        this.manager.cardRegistration(cardNumber, cardCvv, cardExpiryMonth, cardExpiryYear, clientKey, this);
    }

    public void getCardToken(CardTokenRequest request) {
        this.manager.getToken(request, this);
    }

    public CardTokenCallback getCardTokenCallback() {
        return this;
    }
}
