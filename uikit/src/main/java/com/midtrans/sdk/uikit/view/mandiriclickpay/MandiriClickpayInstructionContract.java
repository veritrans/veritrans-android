package com.midtrans.sdk.uikit.view.mandiriclickpay;

import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public interface MandiriClickpayInstructionContract extends BasePaymentContract {

    void onTokenizeSuccess(TokenDetailsResponse response);

    void onTokenizeError(Throwable error);
}
