package com.midtrans.sdk.corekit.utilities;

import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.MerchantServiceManager;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.snap.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 4/6/18.
 */

public class MerchantServiceCallbackImplement implements CheckoutCallback, SaveCardCallback, GetCardCallback{

    private final MerchantServiceManager manager;
    private final CallbackCollaborator collaborator;

    public MerchantServiceCallbackImplement(MerchantServiceManager manager, CallbackCollaborator collaborator) {
        this.collaborator = collaborator;
        this.manager = manager;
    }

    @Override
    public void onSuccess(SaveCardResponse response) {
        collaborator.onSaveAndGetCardsSuccess();
    }

    @Override
    public void onSuccess(ArrayList<SaveCardRequest> response) {
        collaborator.onSaveAndGetCardsSuccess();
    }

    @Override
    public void onFailure(String reason) {
        collaborator.onSaveAndGetCardsFailure();
    }

    @Override
    public void onError(Throwable error) {
        collaborator.onError();
    }

    @Override
    public void onSuccess(Token token) {
        collaborator.onCheckoutSuccess();
    }

    @Override
    public void onFailure(Token token, String reason) {
        collaborator.onCheckoutFailure();
    }

    public void checkout(TokenRequestModel model) {
        this.manager.checkout(model, this);
    }

    public void saveCards(String userId, List<SaveCardRequest> cards) {
        this.manager.saveCards(userId, cards, this);
    }

    public void getCards(String userId) {
        this.manager.getCards(userId, this);
    }

    public CheckoutCallback getCheckoutCallback() {
        return this;
    }

    public SaveCardCallback getSaveCardCallback() {
        return this;
    }
}
