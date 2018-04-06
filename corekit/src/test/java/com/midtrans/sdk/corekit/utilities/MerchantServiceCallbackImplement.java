package com.midtrans.sdk.corekit.utilities;

import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.core.CallbackCollaborator;
import com.midtrans.sdk.corekit.core.MerchantServiceManager;
import com.midtrans.sdk.corekit.models.snap.Token;

/**
 * Created by ziahaqi on 4/6/18.
 */

public class MerchantServiceCallbackImplement implements CheckoutCallback {

    private final MerchantServiceManager manager;
    private final CallbackCollaborator collaborator;

    public MerchantServiceCallbackImplement(MerchantServiceManager manager, CallbackCollaborator collaborator) {
        this.collaborator = collaborator;
        this.manager = manager;
    }

    @Override
    public void onError(Throwable error) {

    }

    @Override
    public void onSuccess(Token token) {

    }

    @Override
    public void onFailure(Token token, String reason) {

    }
}
