package com.midtrans.sdk.corekit.core.api.merchant;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.merchant.model.savecard.SaveCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;

import java.util.List;

import retrofit2.Call;

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
    public void saveCards(String userId, final List<SaveCardRequest> requests,
                          final MidtransCallback<SaveCardResponse> callback) {
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
}