package com.midtrans.sdk.uikit.views.gopay.payment;

import com.google.gson.Gson;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;

/**
 * Created by ziahaqi on 9/7/17.
 */

public class GopayPaymentPresenter extends BasePaymentPresenter<GoPayPaymentView> {

    public GopayPaymentPresenter(GoPayPaymentView view) {
        super();
        this.view = view;
    }

    public void startGoPayPayment() {
        String snapToken = getMidtransSDK().readAuthenticationToken();
        //mock up response until it's deployed in back-end
        TransactionResponse response = getGoPayMockResponse();
        transactionResponse = response;
        view.onPaymentSuccess(response);

//        getMidtransSDK().paymentUsingGoPay(snapToken, new TransactionCallback() {
//            @Override
//            public void onSuccess(TransactionResponse response) {
//                transactionResponse = response;
//                view.onPaymentSuccess(response);
//            }
//
//            @Override
//            public void onFailure(TransactionResponse response, String reason) {
//                transactionResponse = response;
//                view.onPaymentFailure(response);
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                view.onPaymentError(error);
//            }
//        });
    }

    private TransactionResponse getGoPayMockResponse() {
        String fakeResponse = "{\n"
            + "    \"status_code\": \"201\",\n"
            + "    \"status_message\": \"Transaksi sedang diproses\",\n"
            + "    \"transaction_id\": \"dfef2b35-a588-4f58-9773-78eb268bfe0e\",\n"
            + "    \"order_id\": \"test-1511328179\",\n"
            + "    \"gross_amount\": \"10000.00\",\n"
            + "    \"payment_type\": \"gopay\",\n"
            + "    \"transaction_time\": \"2017-11-22 12:23:06\",\n"
            + "    \"transaction_status\": \"pending\",\n"
            + "    \"fraud_status\": \"accept\",\n"
            + "    \"qr_code_url\": \"https://i.imgur.com/UIHjjU0.png\",\n"
            + "    \"deeplink_url\": \"gojek://gopay/merchanttransfer?tref=i3VwApFnnG&amount=10000&activity=GP:RR\",\n"
            + "    \"finish_redirect_url\": \"https://google.com?order_id=test-1511328179&status_code=201&transaction_status=pending\"\n"
            + "}";
        Gson gson = new Gson();
        TransactionResponse response = gson.fromJson(fakeResponse, TransactionResponse.class);
        return response;
    }
}
