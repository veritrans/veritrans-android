package com.midtrans.sdk.ui.views.ebanking.mandiri_clickpay;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentParams;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;

/**
 * Created by rakawm on 4/10/17.
 */

public class MandiriClickpayPresenter extends BasePaymentPresenter {

    private final MandiriClickpayView view;
    private String input3;
    private PaymentResult<MandiriClickpayPaymentResponse> paymentResult;

    public MandiriClickpayPresenter(MandiriClickpayView view) {
        this.view = view;
        initInput3();
    }

    private void initInput3() {
        input3 = String.valueOf(UiUtils.generateRandomNumber());
    }

    public void startPayment(String mandiriCardNumber, String mandiriTokenResponse) {
        String checkoutToken = MidtransUi.getInstance().getTransaction().token;
        final MandiriClickpayPaymentParams paymentParams = new MandiriClickpayPaymentParams(mandiriCardNumber, input3, mandiriTokenResponse);
        MidtransCore.getInstance().paymentUsingMandiriClickpay(checkoutToken, paymentParams, new MidtransCoreCallback<MandiriClickpayPaymentResponse>() {
            @Override
            public void onSuccess(MandiriClickpayPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onMandiriClickpaySuccess(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_SUCCESS);
            }

            @Override
            public void onFailure(MandiriClickpayPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onMandiriClickpayFailure(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onMandiriClickpayError(throwable.getMessage());

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }
        });
    }

    public PaymentResult<MandiriClickpayPaymentResponse> getPaymentResult() {
        return paymentResult;
    }

    public String getInput3() {
        return input3;
    }

    public void setInput3(String input3) {
        this.input3 = input3;
    }

    public String getInput2() {
        SnapTransaction transaction = MidtransUi.getInstance().getTransaction();
        return String.valueOf(transaction.transactionDetails.grossAmount);
    }
}
