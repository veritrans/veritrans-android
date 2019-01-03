package com.midtrans.sdk.corekit.core.merchant;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.midtrans.response.SaveCardResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantApiManager extends BaseServiceManager {

    private static final String TAG = "MerchantApiManager";

    private MerchantApiService apiService;

    public MerchantApiManager(MerchantApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * This method will create a HTTP request to Merchant Server for making Snap Token.
     *
     * @param request  CheckoutTransaction model that construct by user.
     * @param callback callback of making Snap Token.
     */
    public void checkout(final CheckoutTransaction request,
                         final MidtransCallback<CheckoutWithTransactionResponse> callback) {
        if (request == null) {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        } else {
            if (apiService == null) {
                callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL));
            } else {
                Call<CheckoutWithTransactionResponse> call = apiService.checkout(request);
                call.enqueue(new retrofit2.Callback<CheckoutWithTransactionResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CheckoutWithTransactionResponse> call, @NonNull retrofit2.Response<CheckoutWithTransactionResponse> response) {
                        releaseResources();
                        handleServerResponse(response, callback, null);
                    }

                    @Override
                    public void onFailure(@NonNull Call<CheckoutWithTransactionResponse> call, @NonNull Throwable throwable) {
                        releaseResources();
                        handleServerResponse(null, callback, throwable);
                    }
                });
            }
        }
    }

    /**
     * this method is used to get saved card list on merchant server
     *
     * @param userId   unique id to each user
     * @param callback Transaction callback
     */
    public void getCards(final String userId, final MidtransCallback<ArrayList<SaveCardRequest>> callback) {

        if (apiService == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<List<SaveCardRequest>> call = apiService.getCards(userId);
        call.enqueue(new Callback<List<SaveCardRequest>>() {
            @Override
            public void onResponse(Call<List<SaveCardRequest>> call, Response<List<SaveCardRequest>> response) {
                releaseResources();

                List<SaveCardRequest> cardsResponses = response.body();
                if (cardsResponses != null && cardsResponses.size() > 0) {
                    if (response.code() == 200 || response.code() == 201) {
                        callback.onSuccess(new ArrayList<>(cardsResponses));
                    } else {
                        callback.onFailed(new Throwable(response.message()));
                    }
                } else {
                    callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void onFailure(Call<List<SaveCardRequest>> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * This method is used to save credit cards to merchant server
     *
     * @param cardRequests credit card Request model
     * @param userId       unique id for every user
     * @param callback     save card callback
     */
    public void saveCards(String userId, final List<SaveCardRequest> cardRequests, final MidtransCallback<SaveCardResponse> callback) {
        if (apiService == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        if (cardRequests != null) {
            Call<List<SaveCardRequest>> call = apiService.saveCards(userId, cardRequests);

            call.enqueue(new Callback<List<SaveCardRequest>>() {
                @Override
                public void onResponse(Call<List<SaveCardRequest>> call, Response<List<SaveCardRequest>> response) {
                    releaseResources();

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

                        callback.onSuccess(saveCardResponse);
                    } else {
                        callback.onFailed(new Throwable(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<List<SaveCardRequest>> call, Throwable t) {
                    doOnResponseFailure(t, callback);
                }
            });

        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    private boolean isSuccess(int httpStatusCode, String responseStatusCode) {

        if (httpStatusCode == 200
                || httpStatusCode == 201
                || isResponseStatusCodeSuccess(responseStatusCode)) {
            return true;
        }

        return false;
    }

    private boolean isResponseStatusCodeSuccess(String responseStatusCode) {

        if (!TextUtils.isEmpty(responseStatusCode)
                && (responseStatusCode.equals(Constants.STATUS_CODE_200)
                || responseStatusCode.equals(Constants.STATUS_CODE_201))) {
            return true;
        }

        return false;
    }
}