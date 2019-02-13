package com.midtrans.sdk.corekit.base.network;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.merchant.model.savecard.SaveCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.SnapApiService;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_EMPTY_RESPONSE;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_FAILURE_RESPONSE;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_SNAP_TOKEN;
import static com.midtrans.sdk.corekit.utilities.NetworkHelper.isSuccess;

public abstract class BaseServiceManager {

    protected <T> Boolean isSnapTokenAvailable(MidtransCallback<T> callback,
                                               String snapToken,
                                               SnapApiService apiService) {
        if (snapToken == null) {
            doOnSnapTokenUnAvailable(callback);
            return false;
        }
        if (apiService == null) {
            doOnApiServiceUnAvailable(callback);
            return false;
        }
        if (apiService != null && snapToken != null) {
            return true;
        }
        return null;
    }

    protected <T> void handleCall(Call<T> basePaymentResponseCall,
                                  MidtransCallback<T> basePaymentResponseMidtransCallback) {
        basePaymentResponseCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                handleServerResponse(response, basePaymentResponseMidtransCallback, null);
            }

            @Override
            public void onFailure(Call<T> call, Throwable throwable) {
                handleServerResponse(null, basePaymentResponseMidtransCallback, throwable);
            }
        });
    }

    protected void handleCallForSaveCard(Call<List<SaveCardRequest>> basePaymentResponseCall,
                                         MidtransCallback<SaveCardResponse> basePaymentResponseMidtransCallback) {
        basePaymentResponseCall.enqueue(new Callback<List<SaveCardRequest>>() {
            @Override
            public void onResponse(Call<List<SaveCardRequest>> call, Response<List<SaveCardRequest>> response) {
                String statusCode = "";
                List<SaveCardRequest> data = response.body();

                if (data != null && !data.isEmpty()) {
                    SaveCardRequest cardResponse = data.get(0);
                    if (cardResponse != null) {
                        statusCode = cardResponse.getCode();
                    }
                }

                if (isSuccess(response.code(), statusCode)) {
                    SaveCardResponse saveCardResponse = new SaveCardResponse();
                    saveCardResponse.setCode(response.code());
                    saveCardResponse.setMessage(response.message());

                    basePaymentResponseMidtransCallback.onSuccess(saveCardResponse);
                } else {
                    basePaymentResponseMidtransCallback.onFailed(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<SaveCardRequest>> call, Throwable t) {
                handleServerResponse(null, basePaymentResponseMidtransCallback, t);
            }
        });
    }

    private <T> void handleServerResponse(Response<T> response,
                                          MidtransCallback<T> callback,
                                          Throwable throwable) {
        if (response != null && response.isSuccessful()) {
            if (response.code() != 204) {
                T responseBody = response.body();
                if (responseBody instanceof BasePaymentResponse) {
                    BasePaymentResponse basePaymentResponse = (BasePaymentResponse) responseBody;
                    if (isSuccess(response.code(), basePaymentResponse.getStatusCode())) {
                        callback.onSuccess(responseBody);
                    } else {
                        String statusMessage = MESSAGE_ERROR_FAILURE_RESPONSE;
                        ArrayList<String> validationMessages = basePaymentResponse.getValidationMessages();
                        if (!validationMessages.isEmpty()) {
                            statusMessage = validationMessages.get(0);
                        }
                        callback.onFailed(new Throwable(statusMessage));
                    }
                } else {
                    callback.onSuccess(responseBody);
                }
            } else {
                callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            }
        } else if (response != null && !response.isSuccessful()) {
            T responseBody = response.body();
            if (responseBody instanceof PaymentInfoResponse) {
                CheckoutWithTransactionResponse checkoutWithTransactionResponse = (CheckoutWithTransactionResponse) responseBody;
                if (checkoutWithTransactionResponse.getErrorMessages() != null) {
                    callback.onSuccess(responseBody);
                } else {
                    callback.onFailed(new Throwable(MESSAGE_ERROR_FAILURE_RESPONSE));
                }
            } else {
                callback.onSuccess(responseBody);
            }
        } else {
            if (throwable != null) {
                callback.onFailed(new Throwable(throwable.getMessage(), throwable.getCause()));
            } else {
                callback.onFailed(new Throwable(MESSAGE_ERROR_FAILURE_RESPONSE));
            }
        }
    }

    protected void doOnSnapTokenUnAvailable(MidtransCallback callback) {
        callback.onFailed(new Throwable(MESSAGE_ERROR_SNAP_TOKEN));
    }

    protected void doOnApiServiceUnAvailable(MidtransCallback callback) {
        callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_MERCHANT_URL));
    }

    protected void doOnInvalidDataSupplied(MidtransCallback callback) {
        callback.onFailed(new Throwable(MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
    }

}