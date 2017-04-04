package com.midtrans.sdk.ui.views.banktransfer.payment;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.Utils;

/**
 * Created by ziahaqi on 4/3/17.
 */

public class BankTransferPresenter extends BasePaymentPresenter implements BankTransferPaymentContract.Presenter {

    private BankTransferPaymentContract.BankTransferPaymentView paymentView;
    private PaymentResult paymentResult;
    private BankTransferPaymentContract.bankTransferStatusView statusView;

    public BankTransferPresenter() {
        midtransUiSdk = MidtransUi.getInstance();
    }

    /**
     * Track Mixpanel Event
     *
     * @param eventName
     */
    @Override
    public void trackEvent(String eventName) {
        midtransUiSdk.trackEvent(eventName);
    }

    /**
     * Set presenter to payment fragment
     *
     * @param paymentView
     */
    public void setPaymentView(BankTransferPaymentFragment paymentView) {
        this.paymentView = paymentView;
        this.paymentView.setPresenter(this);
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
        }
    }

    private void performOtherBankPayment(SnapCustomerDetails customerDetails) {
        MidtransCore.getInstance().paymentUsingOtherBankTransfer(midtransUiSdk.getTransaction().token,
                customerDetails, new MidtransCoreCallback<OtherBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(OtherBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentSuccess(paymentResult);
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
        MidtransCore.getInstance().paymentUsingPermataBankTransfer(midtransUiSdk.getTransaction().token,
                customerDetails, new MidtransCoreCallback<PermataBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(PermataBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentSuccess(paymentResult);
                    }

                    @Override
                    public void onFailure(PermataBankTransferPaymentResponse response) {
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

    private void performMandiriBillPayment(SnapCustomerDetails customerDetails) {
        MidtransCore.getInstance().paymentUsingMandiriBankTransfer(midtransUiSdk.getTransaction().token,
                customerDetails, new MidtransCoreCallback<MandiriBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(MandiriBankTransferPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        paymentView.onPaymentSuccess(paymentResult);
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
        MidtransCore.getInstance().paymentUsingBcaBankTransfer(midtransUiSdk.getTransaction().token,
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

    public void setStatusView(BankTransferPaymentContract.bankTransferStatusView statusView) {
        this.statusView = statusView;
        this.paymentView.setPresenter(this);
    }

    public PaymentResult getPaymentResult() {
        return paymentResult;
    }
}
