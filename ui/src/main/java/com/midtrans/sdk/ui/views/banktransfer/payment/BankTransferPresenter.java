package com.midtrans.sdk.ui.views.banktransfer.payment;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.bni.BniBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.Utils;

/**
 * Created by ziahaqi on 4/3/17.
 */

public class BankTransferPresenter extends BasePaymentPresenter {

    private final BankTransferPaymentView paymentView;
    private PaymentResult paymentResult;

    public BankTransferPresenter(BankTransferPaymentView paymentView) {
        this.paymentView = paymentView;
    }

    /**
     * Track Mixpanel Event
     *
     * @param eventName
     */
    public void trackEvent(String eventName) {
        Utils.trackEvent(eventName);
    }

    public void performPayment(String userEmail, String bankType) {
        SnapCustomerDetails customerDetails = Utils.createSnapCustomerDetails(userEmail);

        switch (bankType) {
            case PaymentType.BCA_VA:
                performBcaBankTransferPayment(customerDetails);
                break;
            case PaymentType.E_CHANNEL:
                performMandiriBillPayment(customerDetails);
                break;
            case PaymentType.PERMATA_VA:
                performPermataBankTransferPayment(customerDetails);
                break;
            case PaymentType.OTHER_VA:
                performOtherBankPayment(customerDetails);
                break;
            case PaymentType.BNI_VA:
                performBniBankTransferPayment(customerDetails);
                break;
        }
    }

    public void performPayment(String bankType) {
        switch (bankType) {
            case PaymentType.BCA_VA:
                performBcaBankTransferPayment(null);
                break;
            case PaymentType.E_CHANNEL:
                performMandiriBillPayment(null);
                break;
            case PaymentType.PERMATA_VA:
                performPermataBankTransferPayment(null);
                break;
            case PaymentType.OTHER_VA:
                performOtherBankPayment(null);
                break;
            case PaymentType.BNI_VA:
                performBniBankTransferPayment(null);
                break;
        }
    }

    private void performOtherBankPayment(SnapCustomerDetails customerDetails) {
        MidtransCore.getInstance().paymentUsingOtherBankTransfer(midtransUi.getTransaction().token,
                customerDetails, new MidtransCoreCallback<OtherBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(OtherBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentSuccess(paymentResult);

                        trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);
                    }

                    @Override
                    public void onFailure(OtherBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentFailure(paymentResult);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        paymentResult = new PaymentResult(throwable.getMessage());
                        paymentView.onPaymentError(throwable.getMessage());

                    }
                });
    }

    private void performPermataBankTransferPayment(SnapCustomerDetails customerDetails) {
        MidtransCore.getInstance().paymentUsingPermataBankTransfer(midtransUi.getTransaction().token,
                customerDetails, new MidtransCoreCallback<PermataBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(PermataBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentSuccess(paymentResult);

                        trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);
                    }

                    @Override
                    public void onFailure(PermataBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentFailure(paymentResult);

                        trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        paymentResult = new PaymentResult<>(throwable.getMessage());
                        paymentView.onPaymentError(throwable.getMessage());

                        trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
                    }
                });
    }

    private void performMandiriBillPayment(SnapCustomerDetails customerDetails) {
        MidtransCore.getInstance().paymentUsingMandiriBankTransfer(midtransUi.getTransaction().token,
                customerDetails, new MidtransCoreCallback<MandiriBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(MandiriBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentSuccess(paymentResult);

                        trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);
                    }

                    @Override
                    public void onFailure(MandiriBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentFailure(paymentResult);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        paymentResult = new PaymentResult<>(throwable.getMessage());
                        paymentView.onPaymentError(throwable.getMessage());
                    }
                });
    }

    private void performBcaBankTransferPayment(SnapCustomerDetails customerDetails) {
        MidtransCore.getInstance().paymentUsingBcaBankTransfer(midtransUi.getTransaction().token,
                customerDetails, new MidtransCoreCallback<BcaBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(BcaBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentSuccess(paymentResult);
                    }

                    @Override
                    public void onFailure(BcaBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentFailure(paymentResult);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        paymentResult = new PaymentResult<>(throwable.getMessage());
                        paymentView.onPaymentError(throwable.getMessage());
                    }
                });
    }

    private void performBniBankTransferPayment(SnapCustomerDetails customerDetails) {
        MidtransCore.getInstance().paymentUsingBniBankTransfer(
                MidtransUi.getInstance().getTransaction().token,
                customerDetails,
                new MidtransCoreCallback<BniBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(BniBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentSuccess(paymentResult);
                    }

                    @Override
                    public void onFailure(BniBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentFailure(paymentResult);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        paymentResult = new PaymentResult<>(throwable.getMessage());
                        paymentView.onPaymentError(throwable.getMessage());
                    }
                }
        );
    }
    public PaymentResult getPaymentResult() {
        return paymentResult;
    }
}
