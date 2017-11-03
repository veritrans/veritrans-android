package com.midtrans.sdk.uikit.views.banktransfer.payment;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 8/14/17.
 */

public class BankTransferPaymentPresenter extends BasePaymentPresenter<BankTransferPaymentView> {

    public BankTransferPaymentPresenter(BankTransferPaymentView view) {
        super();
        this.view = view;
    }


    public boolean isEmailValid(String email) {
        return !(!TextUtils.isEmpty(email) && !SdkUIFlowUtil.isEmailValid(email));
    }

    public String getUserEmail() {
        String userEmail = "";
        UserDetail userDetail = LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
        if (userDetail != null) {
            userEmail = userDetail.getEmail();
        }
        return userEmail;
    }

    public void startPayment(String bankType, String email) {
        if (!TextUtils.isEmpty(bankType)) {
            String snapToken = MidtransSDK.getInstance().readAuthenticationToken();
            switch (bankType) {
                case PaymentType.BCA_VA:
                    startBcaBankTransferpayment(snapToken, email);
                    break;

                case PaymentType.E_CHANNEL:
                    startMandiriBillPayment(snapToken, email);
                    break;

                case PaymentType.PERMATA_VA:
                    startPermataBankTransferpayment(snapToken, email);
                    break;

                case PaymentType.BNI_VA:
                    startBniBankTransferpayment(snapToken, email);
                    break;

                case PaymentType.ALL_VA:
                    startOtherBanksTransferpayment(snapToken, email);
                    break;

                default:
                    view.onBankTranferPaymentUnavailable(bankType);
                    break;
            }
        } else {
            view.onBankTranferPaymentUnavailable(bankType);
        }
    }

    private void startBcaBankTransferpayment(String snapToken, String email) {
        MidtransSDK.getInstance().paymentUsingBankTransferBCA(snapToken, email, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }

    private void startBniBankTransferpayment(String snapToken, String email) {
        MidtransSDK.getInstance().paymentUsingBankTransferBni(snapToken, email, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }

    private void startPermataBankTransferpayment(String snapToken, String email) {
        MidtransSDK.getInstance().paymentUsingBankTransferPermata(snapToken, email, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }

    private void startOtherBanksTransferpayment(String snapToken, String email) {
        MidtransSDK.getInstance().paymentUsingBankTransferAllBank(snapToken, email, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }

    private void startMandiriBillPayment(String snapToken, String email) {
        MidtransSDK.getInstance().paymentUsingMandiriBillPay(snapToken, email, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }
}
