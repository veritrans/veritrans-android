package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;

/**
 * Created by ziahaqi on 4/2/18.
 */

public class SnapServiceCallbackImplement implements TransactionCallback, CheckoutCallback, TransactionOptionsCallback {
    CallbackCollaborator callbackCollaborator;
    SnapServiceManager serviceManager;

    public SnapServiceCallbackImplement(SnapServiceManager serviceManager, CallbackCollaborator callbackCollaborator) {
        this.callbackCollaborator = callbackCollaborator;
        this.serviceManager = serviceManager;
    }


    @Override
    public void onError(Throwable error) {
        callbackCollaborator.onError();
    }

    @Override
    public void onSuccess(Token token) {

    }

    @Override
    public void onFailure(Token token, String reason) {

    }

    @Override
    public void onSuccess(TransactionResponse response) {

    }

    @Override
    public void onFailure(TransactionResponse response, String reason) {

    }

    @Override
    public void onSuccess(Transaction transaction) {
        callbackCollaborator.onGetPaymentOptionSuccess();
    }

    @Override
    public void onFailure(Transaction transaction, String reason) {
        callbackCollaborator.onGetPaymentOptionFailure();
    }

    public void checkout(TokenRequestModel snapTokenRequestModelMock) {
    }


    public void getTransactionOptions(String tokenId) {
        this.serviceManager.getTransactionOptions(tokenId, this);
    }
}
