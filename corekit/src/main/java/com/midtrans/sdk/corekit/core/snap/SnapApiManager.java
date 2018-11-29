package com.midtrans.sdk.corekit.core.snap;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.model.PaymentType;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.BasePaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.PaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.gopay.GopayPaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.mandiriclick.MandiriClickpayParams;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.mandiriclick.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.telkomsel.TelkomselCashPaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.epaybri.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BcaPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BniPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.OtherPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.PermataPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.PaymentInfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_EMPTY_RESPONSE;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_FAILURE_RESPONSE;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_SNAP_TOKEN;

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

        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
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
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
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
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
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
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
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
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
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
    }

    /**
     * This method is used for Payment Using Mandiri Echannel
     *
     * @param snapToken                snapToken after get payment info.
     * @param customerDetailPayRequest Payment Details.zz
     * @param callback                 Transaction callback.
     */
    public void paymentUsingMandiriEcash(final String snapToken,
                                         final CustomerDetailPayRequest customerDetailPayRequest,
                                         final MidtransCallback<BasePaymentResponse> callback) {
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.MANDIRI_ECASH, customerDetailPayRequest);
            Call<BasePaymentResponse> call = apiService.paymentMandiriEcash(snapToken, paymentRequest);
            call.enqueue(new Callback<BasePaymentResponse>() {
                @Override
                public void onResponse(@NonNull Call<BasePaymentResponse> call, @NonNull Response<BasePaymentResponse> response) {
                    releaseResources();
                    handleServerResponse(response, callback, null);
                }

                @Override
                public void onFailure(@NonNull Call<BasePaymentResponse> call, @NonNull Throwable throwable) {
                    releaseResources();
                    handleServerResponse(null, callback, throwable);
                }
            });
        }
    }

    /**
     * This method is used for Payment Using CIMB Clicks
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingCimbClick(final String snapToken,
                                      final MidtransCallback<BasePaymentResponse> callback) {
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.CIMB_CLICKS);
            Call<BasePaymentResponse> call = apiService.paymentCimbClicks(snapToken, basePaymentRequest);
            call.enqueue(new Callback<BasePaymentResponse>() {
                @Override
                public void onResponse(@NonNull Call<BasePaymentResponse> call, @NonNull Response<BasePaymentResponse> response) {
                    releaseResources();
                    handleServerResponse(response, callback, null);
                }

                @Override
                public void onFailure(@NonNull Call<BasePaymentResponse> call, @NonNull Throwable throwable) {
                    releaseResources();
                    handleServerResponse(null, callback, throwable);
                }
            });
        }
    }

    /**
     * This method is used for Payment Using Akulaku
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingAkulaku(final String snapToken,
                                    final MidtransCallback<BasePaymentResponse> callback) {
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.AKULAKU);
            Call<BasePaymentResponse> call = apiService.paymentAkulaku(snapToken, basePaymentRequest);
            call.enqueue(new Callback<BasePaymentResponse>() {
                @Override
                public void onResponse(@NonNull Call<BasePaymentResponse> call, @NonNull Response<BasePaymentResponse> response) {
                    releaseResources();
                    handleServerResponse(response, callback, null);
                }

                @Override
                public void onFailure(@NonNull Call<BasePaymentResponse> call, @NonNull Throwable throwable) {
                    releaseResources();
                    handleServerResponse(null, callback, throwable);
                }
            });
        }
    }

    /**
     * This method is used for Payment Using Gopay
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingGopay(final String snapToken,
                                  final String gopayAccountNumber,
                                  final MidtransCallback<BasePaymentResponse> callback) {
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
            GopayPaymentRequest gopayPaymentRequest = new GopayPaymentRequest(PaymentType.GOPAY, gopayAccountNumber);
            Call<BasePaymentResponse> call = apiService.paymentUsingGoPay(snapToken, gopayPaymentRequest);
            call.enqueue(new Callback<BasePaymentResponse>() {
                @Override
                public void onResponse(@NonNull Call<BasePaymentResponse> call, @NonNull Response<BasePaymentResponse> response) {
                    releaseResources();
                    handleServerResponse(response, callback, null);
                }

                @Override
                public void onFailure(@NonNull Call<BasePaymentResponse> call, @NonNull Throwable throwable) {
                    releaseResources();
                    handleServerResponse(null, callback, throwable);
                }
            });
        }
    }

    /**
     * This method is used for Payment Using Telkomsel Cash
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingTelkomselCash(final String snapToken,
                                          final String customerNumber,
                                          final MidtransCallback<BasePaymentResponse> callback) {
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
            TelkomselCashPaymentRequest telkomselCashPaymentRequest = new TelkomselCashPaymentRequest(PaymentType.TELKOMSEL_CASH, customerNumber);
            Call<BasePaymentResponse> call = apiService.paymentUsingTelkomselCash(snapToken, telkomselCashPaymentRequest);
            call.enqueue(new Callback<BasePaymentResponse>() {
                @Override
                public void onResponse(@NonNull Call<BasePaymentResponse> call, @NonNull Response<BasePaymentResponse> response) {
                    releaseResources();
                    handleServerResponse(response, callback, null);
                }

                @Override
                public void onFailure(@NonNull Call<BasePaymentResponse> call, @NonNull Throwable throwable) {
                    releaseResources();
                    handleServerResponse(null, callback, throwable);
                }
            });
        }
    }

    /**
     * This method is used for Payment Using Indomaret
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingIndomaret(final String snapToken,
                                      final MidtransCallback<BasePaymentResponse> callback) {
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.INDOMARET);
            Call<BasePaymentResponse> call = apiService.paymentIndomaret(snapToken, basePaymentRequest);
            call.enqueue(new Callback<BasePaymentResponse>() {
                @Override
                public void onResponse(@NonNull Call<BasePaymentResponse> call, @NonNull Response<BasePaymentResponse> response) {
                    releaseResources();
                    handleServerResponse(response, callback, null);
                }

                @Override
                public void onFailure(@NonNull Call<BasePaymentResponse> call, @NonNull Throwable throwable) {
                    releaseResources();
                    handleServerResponse(null, callback, throwable);
                }
            });
        }
    }

    /**
     * This method is used for Payment Using BRI Epay
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingBriEpay(final String snapToken,
                                    final MidtransCallback<BriEpayPaymentResponse> callback) {
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.BRI_EPAY);
            Call<BriEpayPaymentResponse> call = apiService.paymentBriEpay(snapToken, basePaymentRequest);
            call.enqueue(new Callback<BriEpayPaymentResponse>() {
                @Override
                public void onResponse(@NonNull Call<BriEpayPaymentResponse> call, @NonNull Response<BriEpayPaymentResponse> response) {
                    releaseResources();
                    handleServerResponse(response, callback, null);
                }

                @Override
                public void onFailure(@NonNull Call<BriEpayPaymentResponse> call, @NonNull Throwable throwable) {
                    releaseResources();
                    handleServerResponse(null, callback, throwable);
                }
            });
        }
    }

    /**
     * This method is used for Payment Using Klik Bca
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingKlikBca(final String snapToken,
                                    final String klikBcaUserId,
                                    final MidtransCallback<KlikBcaPaymentResponse> callback) {
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            return;
        }
        KlikBcaPaymentRequest paymentRequest = new KlikBcaPaymentRequest(PaymentType.KLIK_BCA, klikBcaUserId);
        Call<KlikBcaPaymentResponse> call = apiService.paymentKlikBca(snapToken, paymentRequest);
        call.enqueue(new Callback<KlikBcaPaymentResponse>() {
            @Override
            public void onResponse(@NonNull Call<KlikBcaPaymentResponse> call, @NonNull Response<KlikBcaPaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, callback, null);
            }

            public void onFailure(@NonNull Call<KlikBcaPaymentResponse> call, @NonNull Throwable throwable) {
                releaseResources();
                handleServerResponse(null, callback, throwable);
            }
        });
    }

    /**
     * This method is used for Payment Using Mandiri ClickPay
     *
     * @param snapToken             snapToken after get payment info.
     * @param mandiriClickpayParams parameter for Mandiri Clickpay
     * @param callback              Transaction callback.
     */
    public void paymentUsingMandiriClickPay(final String snapToken,
                                            final MandiriClickpayParams mandiriClickpayParams,
                                            final MidtransCallback<BasePaymentResponse> callback) {
        if (isSnapTokenAvailable(callback,
                snapToken,
                apiService)) {
            MandiriClickpayPaymentRequest paymentRequest = new MandiriClickpayPaymentRequest(PaymentType.KLIK_BCA, mandiriClickpayParams);
            Call<BasePaymentResponse> call = apiService.paymentMandiriClickpay(snapToken, paymentRequest);
            call.enqueue(new Callback<BasePaymentResponse>() {
                @Override
                public void onResponse(@NonNull Call<BasePaymentResponse> call, @NonNull Response<BasePaymentResponse> response) {
                    releaseResources();
                    handleServerResponse(response, callback, null);
                }

                @Override
                public void onFailure(@NonNull Call<BasePaymentResponse> call, @NonNull Throwable throwable) {
                    releaseResources();
                    handleServerResponse(null, callback, throwable);
                }
            });
        }
    }

    private <T> Boolean isSnapTokenAvailable(MidtransCallback<T> callback,
                                             String snapToken,
                                             SnapApiService apiService) {
        if (snapToken == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_SNAP_TOKEN));
            return false;
        }
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_MERCHANT_URL));
            return false;
        }
        if (apiService != null && snapToken != null) {
            return true;
        }
        return null;
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
                callback.onFailed(new Throwable(MESSAGE_ERROR_FAILURE_RESPONSE));
            }
        }
    }

}