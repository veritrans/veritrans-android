package com.midtrans.sdk.corekit.core.snap;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.va.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BcaPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BniPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.OtherPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.PermataPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_EMPTY_RESPONSE;

public class SnapApiManager extends BaseServiceManager {

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
     * @param snapToken      snapToken after get payment info.
     * @param paymentRequest Payment Details.
     * @param callback       Transaction callback.
     */
    public void paymentUsingBankTransferVaBca(final String snapToken,
                                              final BankTransferPaymentRequest paymentRequest,
                                              final MidtransCallback<BcaPaymentResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        Call<BcaPaymentResponse> call = apiService.paymentBankTransferBca(snapToken, paymentRequest);
        call.enqueue(new Callback<BcaPaymentResponse>() {
            @Override
            public void onResponse(Call<BcaPaymentResponse> call, Response<BcaPaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(Call<BcaPaymentResponse> call, Throwable t) {
                releaseResources();
                handleServerResponse(null, callback, t);
            }
        });
    }

    /**
     * This method is used for Payment Using Bank Transfer BNI
     *
     * @param snapToken      snapToken after get payment info.
     * @param paymentRequest Payment Details.
     * @param callback       Transaction callback.
     */
    public void paymentUsingBankTransferVaBni(final String snapToken,
                                              final BankTransferPaymentRequest paymentRequest,
                                              final MidtransCallback<BniPaymentResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        Call<BniPaymentResponse> call = apiService.paymentBankTransferBni(snapToken, paymentRequest);
        call.enqueue(new Callback<BniPaymentResponse>() {
            @Override
            public void onResponse(Call<BniPaymentResponse> call, Response<BniPaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(Call<BniPaymentResponse> call, Throwable t) {
                releaseResources();
                handleServerResponse(null, callback, t);
            }
        });
    }

    /**
     * This method is used for Payment Using Bank Transfer Permata
     *
     * @param snapToken      snapToken after get payment info.
     * @param paymentRequest Payment Details.
     * @param callback       Transaction callback.
     */
    public void paymentUsingBankTransferVaPermata(final String snapToken,
                                                  final BankTransferPaymentRequest paymentRequest,
                                                  final MidtransCallback<PermataPaymentResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        Call<PermataPaymentResponse> call = apiService.paymentBankTransferPermata(snapToken, paymentRequest);
        call.enqueue(new Callback<PermataPaymentResponse>() {
            @Override
            public void onResponse(Call<PermataPaymentResponse> call, Response<PermataPaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(Call<PermataPaymentResponse> call, Throwable t) {
                releaseResources();
                handleServerResponse(null, callback, t);
            }
        });
    }

    /**
     * This method is used for Payment Using Bank Transfer Other Bank
     *
     * @param snapToken      snapToken after get payment info.
     * @param paymentRequest Payment Details.
     * @param callback       Transaction callback.
     */
    public void paymentUsingBankTransferVaOther(final String snapToken,
                                                final BankTransferPaymentRequest paymentRequest,
                                                final MidtransCallback<OtherPaymentResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        Call<OtherPaymentResponse> call = apiService.paymentBankTransferOther(snapToken, paymentRequest);
        call.enqueue(new Callback<OtherPaymentResponse>() {
            @Override
            public void onResponse(Call<OtherPaymentResponse> call, Response<OtherPaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(Call<OtherPaymentResponse> call, Throwable t) {
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