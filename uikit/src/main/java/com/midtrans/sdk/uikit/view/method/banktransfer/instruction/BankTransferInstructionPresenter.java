package com.midtrans.sdk.uikit.view.method.banktransfer.instruction;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BniBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriBillResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PermataBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.BankTransferCharge;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.Helper;

import okhttp3.ResponseBody;

class BankTransferInstructionPresenter extends BasePaymentPresenter<BankTransferInstructionContract> {

    private PaymentInfoResponse paymentInfoResponse;

    BankTransferInstructionPresenter(BankTransferInstructionContract view, PaymentInfoResponse paymentInfoResponse) {
        super();
        this.view = view;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    boolean isEmailValid(String email) {
        return !(!TextUtils.isEmpty(email) && !Helper.isEmailValid(email));
    }

    String getUserEmail() {
        String userEmail = "";
        CustomerDetails userDetail = paymentInfoResponse.getCustomerDetails();
        if (userDetail != null) {
            userEmail = userDetail.getEmail();
        }
        return userEmail;
    }

    void startPayment(String token, String bankType, String email) {
        if (!TextUtils.isEmpty(bankType)) {
            switch (bankType) {
                case PaymentType.BCA_VA:
                    startBcaBankTransferPayment(token, email);
                    break;

                case PaymentType.ECHANNEL:
                    startMandiriBillPayment(token, email);
                    break;

                case PaymentType.PERMATA_VA:
                    startPermataBankTransferPayment(token, email);
                    break;

                case PaymentType.BNI_VA:
                    startBniBankTransferPayment(token, email);
                    break;

                case PaymentType.OTHER_VA:
                    startOtherBanksTransferPayment(token, email);
                    break;

                default:
                    view.onBankTransferPaymentUnavailable(bankType);
                    break;
            }
        } else {
            view.onBankTransferPaymentUnavailable(bankType);
        }
    }

    private void startBcaBankTransferPayment(String snapToken, String email) {
        BankTransferCharge.paymentUsingBankTransferVaBca(
                snapToken,
                null,
                null,
                email,
                new MidtransCallback<BcaBankTransferReponse>() {
                    @Override
                    public void onSuccess(BcaBankTransferReponse data) {
                        view.onPaymentSuccess(data);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        view.onPaymentError(throwable);
                    }
                }
        );
    }

    private void startBniBankTransferPayment(String snapToken, String email) {
        BankTransferCharge.paymentUsingBankTransferVaBni(
                snapToken,
                null,
                null,
                email,
                new MidtransCallback<BniBankTransferResponse>() {
                    @Override
                    public void onSuccess(BniBankTransferResponse data) {
                        view.onPaymentSuccess(data);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        view.onPaymentError(throwable);
                    }
                }
        );
    }

    private void startPermataBankTransferPayment(String snapToken, String email) {
        BankTransferCharge.paymentUsingBankTransferVaPermata(
                snapToken,
                null,
                null,
                email,
                new MidtransCallback<PermataBankTransferResponse>() {
                    @Override
                    public void onSuccess(PermataBankTransferResponse data) {
                        view.onPaymentSuccess(data);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        view.onPaymentError(throwable);
                    }
                }
        );
    }

    private void startOtherBanksTransferPayment(String snapToken, String email) {
        BankTransferCharge.paymentUsingBankTransferVaOther(
                snapToken,
                null,
                null,
                email,
                new MidtransCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody data) {
                        view.onPaymentSuccess(data);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        view.onPaymentError(throwable);
                    }
                }
        );
    }

    private void startMandiriBillPayment(String snapToken, String email) {
        BankTransferCharge.paymentUsingBankTransferVaMandiriBill(
                snapToken,
                null,
                null,
                email,
                new MidtransCallback<MandiriBillResponse>() {
                    @Override
                    public void onSuccess(MandiriBillResponse data) {
                        view.onPaymentSuccess(data);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        view.onPaymentError(throwable);
                    }
                }
        );
    }
}