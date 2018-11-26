package com.midtrans.sdk.corekit.core.snap;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.model.PaymentType;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.PaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.mandiriecash.MandiriEcashResponse;
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
                    public void onResponse(@NonNull Call<PaymentInfoResponse> call, @NonNull Response<PaymentInfoResponse> response) {
                        releaseResources();
                        handleServerResponse(response, callback, null);
                    }

                    @Override
                    public void onFailure(@NonNull Call<PaymentInfoResponse> call, @NonNull Throwable throwable) {
                        releaseResources();
                        handleServerResponse(null, callback, throwable);
                    }
                });
            }
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer BCA
     *
     * @param snapToken       snapToken after get payment info.
     * @param customerDetails Payment Details.
     * @param callback        Transaction callback.
     */
    public void paymentUsingBankTransferVaBca(final String snapToken,
                                              final CustomerDetailPayRequest customerDetails,
                                              final MidtransCallback<BcaPaymentResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        PaymentRequest paymentRequest = new PaymentRequest(PaymentType.BCA_VA, customerDetails);
        Call<BcaPaymentResponse> call = apiService.paymentBankTransferBca(snapToken, paymentRequest);
        call.enqueue(new Callback<BcaPaymentResponse>() {
            @Override
            public void onResponse(@NonNull Call<BcaPaymentResponse> call, @NonNull Response<BcaPaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(@NonNull Call<BcaPaymentResponse> call, @NonNull Throwable throwable) {
                releaseResources();
                handleServerResponse(null, callback, throwable);
            }
        });
    }

    /**
     * This method is used for Payment Using Bank Transfer BNI
     *
     * @param snapToken       snapToken after get payment info.
     * @param customerDetails Payment Details.
     * @param callback        Transaction callback.
     */
    public void paymentUsingBankTransferVaBni(final String snapToken,
                                              final CustomerDetailPayRequest customerDetails,
                                              final MidtransCallback<BniPaymentResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        PaymentRequest paymentRequest = new PaymentRequest(PaymentType.BNI_VA, customerDetails);
        Call<BniPaymentResponse> call = apiService.paymentBankTransferBni(snapToken, paymentRequest);
        call.enqueue(new Callback<BniPaymentResponse>() {
            @Override
            public void onResponse(@NonNull Call<BniPaymentResponse> call, @NonNull Response<BniPaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(@NonNull Call<BniPaymentResponse> call, @NonNull Throwable throwable) {
                releaseResources();
                handleServerResponse(null, callback, throwable);
            }
        });
    }

    /**
     * This method is used for Payment Using Bank Transfer Permata
     *
     * @param snapToken       snapToken after get payment info.
     * @param customerDetails Payment Details.
     * @param callback        Transaction callback.
     */
    public void paymentUsingBankTransferVaPermata(final String snapToken,
                                                  final CustomerDetailPayRequest customerDetails,
                                                  final MidtransCallback<PermataPaymentResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        PaymentRequest paymentRequest = new PaymentRequest(PaymentType.PERMATA_VA, customerDetails);
        Call<PermataPaymentResponse> call = apiService.paymentBankTransferPermata(snapToken, paymentRequest);
        call.enqueue(new Callback<PermataPaymentResponse>() {
            @Override
            public void onResponse(@NonNull Call<PermataPaymentResponse> call, @NonNull Response<PermataPaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(@NonNull Call<PermataPaymentResponse> call, @NonNull Throwable throwable) {
                releaseResources();
                handleServerResponse(null, callback, throwable);
            }
        });
    }

    /**
     * This method is used for Payment Using Bank Transfer Other Bank
     *
     * @param snapToken       snapToken after get payment info.
     * @param customerDetails Payment Details.
     * @param callback        Transaction callback.
     */
    public void paymentUsingBankTransferVaOther(final String snapToken,
                                                final CustomerDetailPayRequest customerDetails,
                                                final MidtransCallback<OtherPaymentResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        PaymentRequest paymentRequest = new PaymentRequest(PaymentType.OTHER_VA, customerDetails);
        Call<OtherPaymentResponse> call = apiService.paymentBankTransferOther(snapToken, paymentRequest);
        call.enqueue(new Callback<OtherPaymentResponse>() {
            @Override
            public void onResponse(@NonNull Call<OtherPaymentResponse> call, @NonNull Response<OtherPaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(@NonNull Call<OtherPaymentResponse> call, @NonNull Throwable throwable) {
                releaseResources();
                handleServerResponse(null, callback, throwable);
            }
        });
    }

    /**
     * This method is used for Payment Using Mandiri Echannel
     *
     * @param snapToken             snapToken after get payment info.
     * @param customerDetailPayRequest Payment Details.zz
     * @param callback              Transaction callback.
     */
    public void paymentUsingMandiriEcash(final String snapToken,
                                         final CustomerDetailPayRequest customerDetailPayRequest,
                                         final MidtransCallback<MandiriEcashResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        PaymentRequest paymentRequest = new PaymentRequest(PaymentType.MANDIRI_ECASH, customerDetailPayRequest);
        Call<MandiriEcashResponse> call = apiService.paymentMandiriEcash(snapToken, paymentRequest);
        call.enqueue(new Callback<MandiriEcashResponse>() {
            @Override
            public void onResponse(@NonNull Call<MandiriEcashResponse> call, @NonNull Response<MandiriEcashResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(@NonNull Call<MandiriEcashResponse> call, @NonNull Throwable throwable) {
                releaseResources();
                handleServerResponse(null, callback, throwable);
            }
        });
    }

    private <T> void handleServerResponse(Response<T> response,
                                          MidtransCallback<T> callback,
                                          Throwable throwable) {
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