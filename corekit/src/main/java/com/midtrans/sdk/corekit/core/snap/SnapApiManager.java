package com.midtrans.sdk.corekit.core.snap;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.snap.model.transaction.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.TransactionOptionsResponse;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.corekit.utilities.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void getTransactionOptions(@NonNull final String snapToken,
                                      final TransactionOptionsCallback callback) {

        if (apiService == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionOptionsResponse> call = apiService.getTransactionOptions(snapToken);
        call.enqueue(new Callback<TransactionOptionsResponse>() {
            @Override
            public void onResponse(Call<TransactionOptionsResponse> call, Response<TransactionOptionsResponse> response) {
                releaseResources();
                TransactionOptionsResponse transaction = response.body();
                if (transaction != null) {
                    if (response.code() == 200 && !TextUtils.isEmpty(transaction.getToken())) {
                        callback.onSuccess(transaction);
                    } else {
                        callback.onFailure(transaction, response.message());
                    }
                    return;
                }

                try {
                    if (response.errorBody() != null) {

                        String strErrorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(strErrorBody);
                        String errorMessage = response.message();

                        if (jsonObject != null && jsonObject.getJSONArray(KEY_ERROR_MESSAGE) != null) {
                            JSONArray jsonArray = jsonObject.getJSONArray(KEY_ERROR_MESSAGE);
                            if (jsonArray.get(0) != null) {
                                errorMessage = jsonArray.get(0).toString();
                            }
                        }
                        callback.onError(new Throwable(errorMessage));

                    } else {
                        callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                        Logger.error(TAG, Constants.MESSAGE_ERROR_EMPTY_RESPONSE);
                    }

                } catch (Exception e) {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                    Logger.error(TAG, "error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<TransactionOptionsResponse> call, Throwable t) {
                releaseResources();
                callback.onError(new Throwable(t.getMessage(), t.getCause()));
            }
        });
    }

}