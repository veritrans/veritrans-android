package com.midtrans.sdk.ui.views.banktransfer.payment;

import android.text.TextUtils;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.CustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.PaymentType;

/**
 * Created by ziahaqi on 4/3/17.
 */

public class BankTransferPresenter extends BasePaymentPresenter implements BankTransferPaymentContract.Presenter{

    private BankTransferPaymentContract.bankTransferPaymentView paymentView;

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
        CustomerDetails customerDetails;
        if(TextUtils.isEmpty(userEmail)){
            CustomerDetails currentCustomerDetail = midtransUiSdk.getTransaction().customerDetails;
            customerDetails = new CustomerDetails(currentCustomerDetail.firstName,
                    currentCustomerDetail.lastName, currentCustomerDetail.email, currentCustomerDetail.phone,
                    currentCustomerDetail.shippingAddress, currentCustomerDetail.billingAddress);
        }else{
            customerDetails = midtransUiSdk.getTransaction().customerDetails;
        }

        switch (bankType){
            case PaymentType.BCA_VA:
                performBcaBankTransferPayment(customerDetails);
                break;
            case PaymentType.E_CHANNEL:

                break;
            case PaymentType.PERMATA_VA:

                break;
            case PaymentType.BNI_VA:

                break;
            case PaymentType.OTHER_VA:

                break;
        }
        MidtransCore.getInstance().paymentUsingBcaBankTransfer(midtransUiSdk.getTransaction().token,
                midtransUiSdk.getTransaction().customerDetails.email =
                );
    }

    private void performBcaBankTransferPayment(CustomerDetails customerDetails) {
//        MidtransCore.getInstance().paymentUsingBcaBankTransfer(midtransUiSdk.getTransaction().token,
//                customerDetails, new MidtransCoreCallback<BcaBankTransferPaymentResponse>() {
//                    @Override
//                    public void onSuccess(BcaBankTransferPaymentResponse object) {
//
//                    }
//
//                    @Override
//                    public void onFailure(BcaBankTransferPaymentResponse object) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//
//                    }
//                });
    }
}
