package com.midtrans.sdk.corekit.core.grouppayment;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.creditcard.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.callback.CardRegistrationCallback;

public class CreditCardCharge extends PaymentsGroupBase {


    /**
     * It will run backgrond task to charge payment using Credit Card
     *
     * @param snapToken authentication token
     * @param callback  transaction callback
     */
    public void paymentUsingCard(String snapToken,
                                 final CreditCardPaymentParams creditCardPaymentParams,
                                 final CustomerDetailPayRequest customerDetailPayRequest,
                                 MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingCreditCard(snapToken, creditCardPaymentParams, customerDetailPayRequest, callback);
        }

    }

    /**
     * It will run backround task to register card PAPI(Payment API) Backend
     *
     * @param cardNumber   credit card number
     * @param cardCvv      credit card cvv
     * @param cardExpMonth credit card expired month
     * @param cardExpYear  credit card expired year
     * @param callback     Credit card registration callback
     */
    public void cardRegistration(@NonNull String cardNumber, @NonNull String clientKey,
                                 @NonNull String cardCvv, @NonNull String cardExpMonth,
                                 @NonNull String cardExpYear, @NonNull CardRegistrationCallback callback) {

        if (isNetworkAvailable(getSdkContext())) {
            getMidtransServiceManager().cardRegistration(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey, callback);
        }
    }
}
