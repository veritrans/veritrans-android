package com.midtrans.sdk.corekit.core.snap;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.va.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_EMPTY_RESPONSE;

public class SnapApiManager extends BaseServiceManager {

    private static final String KEY_ERROR_MESSAGE = "error_messages";
    private static final String TAG = "SnapApiManager";

    private SnapApiService apiService;

    public SnapApiManager(SnapApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * This method will create a HTTP request to Snap to get transaction option.
     *
     * @param snapToken Snap token after creating Snap Token from Merchant Server.
     * @param callback  callback of Transaction Option.
     */
    public void getPaymentInfo(final String snapToken,
                               final MidtransCallback<PaymentInfoResponse> callback) {

        if (snapToken == null) {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        } else {
            if (apiService == null) {
                callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL));
            } else {
                Call<PaymentInfoResponse> call = apiService.getTransactionOptions(snapToken);
                call.enqueue(new Callback<PaymentInfoResponse>() {
                    @Override
                    public void onResponse(Call<PaymentInfoResponse> call, Response<PaymentInfoResponse> response) {
                        releaseResources();
                        handleServerResponse(response, callback, null);
                    }

                    @Override
                    public void onFailure(Call<PaymentInfoResponse> call, Throwable t) {
                        releaseResources();
                        handleServerResponse(null, callback, t);
                    }
                });
            }
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer BCA
     *
     * @param paymentRequest Payment Details.
     * @param callback       Transaction callback
     */
    public <T> void paymentUsingBankTransferVa(final String snapToken,
                                               final BankTransferPaymentRequest paymentRequest,
                                               final MidtransCallback<T> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        Call<T> call = apiService.paymentBankTransfer(snapToken, paymentRequest);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                releaseResources();
                handleServerResponse(null, callback, t);
            }
        });
    }

    private <T> void handleServerResponse(Response<T> response, MidtransCallback<T> callback, Throwable throwable) {
        if (response != null && response.isSuccessful()) {
            if (response.code() != 204) {
                T responseBody = response.body();
                callback.onSuccess(responseBody);
            } else {
                callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            }
        } else {
            if (throwable != null) {
                callback.onFailed(new Throwable(throwable.getMessage(), throwable.getCause()));
            } else {
                callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            }
        }
    }

}