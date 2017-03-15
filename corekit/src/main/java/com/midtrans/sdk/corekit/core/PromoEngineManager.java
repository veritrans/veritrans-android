package com.midtrans.sdk.corekit.core;

import android.content.Context;

import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.callback.ObtainPromoCallback;
import com.midtrans.sdk.corekit.models.promo.ObtainPromoResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rakawm on 2/3/17.
 */

public class PromoEngineManager {

    private Context context;
    private PromoEngineRestAPI promoEngineRestAPI;

    public PromoEngineManager(Context context, PromoEngineRestAPI promoEngineRestAPI) {
        this.context = context;
        this.promoEngineRestAPI = promoEngineRestAPI;
    }

    public void obtainPromo(String promoId, double amount, final ObtainPromoCallback callback) {
        promoEngineRestAPI.obtainPromo(promoId, amount, MidtransSDK.getInstance().getClientKey(), new Callback<ObtainPromoResponse>() {
            @Override
            public void success(ObtainPromoResponse obtainPromoResponse, Response response) {
                if (obtainPromoResponse.isSuccess()) {
                    callback.onSuccess(obtainPromoResponse);
                } else {
                    callback.onFailure(obtainPromoResponse.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(new Throwable(context.getString(R.string.error_empty_response)));
            }
        });
    }
}
