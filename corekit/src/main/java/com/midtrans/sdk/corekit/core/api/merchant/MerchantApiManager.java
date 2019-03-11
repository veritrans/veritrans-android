package com.midtrans.sdk.corekit.core.api.merchant;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.merchant.model.savecard.SaveCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantApiManager extends BaseServiceManager {

    private final static String TAG = "MerchantApiManager";

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
    public void checkout(CheckoutTransaction request,
                         MidtransCallback<CheckoutWithTransactionResponse> callback) {
        if (request == null) {
            doOnInvalidDataSupplied(callback);
        } else {
            if (apiService == null) {
                doOnApiServiceUnAvailable(callback);
            } else {
                Call<CheckoutWithTransactionResponse> call = apiService.checkout(request);
                handleCall(call, callback);
            }
        }
    }

    /**
     * This method is used to save credit cards to merchant server
     *
     * @param requests credit card Request model
     * @param userId   unique id for every user
     * @param callback save card callback
     */
    public void saveCards(String userId,
                          List<SaveCardRequest> requests,
                          MidtransCallback<SaveCardResponse> callback) {
        if (requests == null) {
            doOnInvalidDataSupplied(callback);
        } else {
            if (apiService == null) {
                doOnApiServiceUnAvailable(callback);
            } else {
                Call<List<SaveCardRequest>> call = apiService.saveCards(userId, requests);
                handleCallForSaveCard(call, callback);
            }
        }
    }


    /**
     * This method is used to save credit cards to merchant server
     *
     * @param userId   unique id for every user
     * @param callback save card callback
     */
    public void getCards(String userId,
                         MidtransCallback<List<SaveCardRequest>> callback) {
        if (apiService == null) {
            doOnApiServiceUnAvailable(callback);
        } else {
            Call<List<SaveCardRequest>> call = apiService.getCards(userId);
            call.enqueue(new Callback<List<SaveCardRequest>>() {
                @Override
                public void onResponse(Call<List<SaveCardRequest>> call, Response<List<SaveCardRequest>> response) {
                    List<SaveCardRequest> cardsResponses = response.body();
                    callback.onSuccess(cardsResponses);
                }

                @Override
                public void onFailure(Call<List<SaveCardRequest>> call, Throwable t) {
                    callback.onFailed(new Throwable(t.getMessage(), t.getCause()));
                }
            });
        }
    }

}