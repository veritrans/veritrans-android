package com.midtrans.sdk.uikit.views.creditcard.register;

import android.app.Activity;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.uikit.abstracts.BasePresenter;

/**
 * Created by ziahaqi on 8/18/17.
 */

public class CardRegistrationPresenter extends BasePresenter<CardRegistrationView> {

    private final CardRegistrationCallback callback;

    public CardRegistrationPresenter(CardRegistrationView view, CardRegistrationCallback callback) {
        this.callback = callback;
        this.view = view;
    }

    public void startScan(Activity activity, int intentScanCard) {
        MidtransSDK.getInstance().getExternalScanner().startScan(activity, intentScanCard);
    }

    public void register(String cardNumber, String cvv, String expiryMonth, String expiryYear) {
        MidtransSDK.getInstance().cardRegistration(cardNumber, cvv, expiryMonth, expiryYear, new CardRegistrationCallback() {
            @Override
            public void onSuccess(CardRegistrationResponse response) {
                callback.onSuccess(response);
                view.onRegisterCardSuccess(response);
            }

            @Override
            public void onFailure(CardRegistrationResponse response, String reason) {
                callback.onFailure(response, reason);
                view.onRegisterFailure(response, reason);
            }

            @Override
            public void onError(Throwable error) {
                callback.onError(error);
                view.onRegisterError(error);
            }
        });
    }

    public String getBankByCardBin(String cleanCardNumber) {
        return null;
    }

    public boolean isMandiriDebitCard(String cleanCardNumber) {
        return false;
    }
}
