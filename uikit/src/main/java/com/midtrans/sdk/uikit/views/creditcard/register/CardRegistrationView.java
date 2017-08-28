package com.midtrans.sdk.uikit.views.creditcard.register;

import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.uikit.abstracts.BaseView;

/**
 * Created by ziahaqi on 8/18/17.
 */

public interface CardRegistrationView extends BaseView {

    void onRegisterFailure(CardRegistrationResponse response, String reason);

    void onRegisterCardSuccess(CardRegistrationResponse response);

    void onRegisterError(Throwable error);

    void onCallbackUnImplemented();
}
