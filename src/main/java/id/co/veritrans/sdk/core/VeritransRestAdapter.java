package id.co.veritrans.sdk.core;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import id.co.veritrans.sdk.BuildConfig;
import id.co.veritrans.sdk.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.utilities.Utils;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by chetan on 16/10/15.
 */
class VeritransRestAdapter {
    private static final String TAG = VeritransRestAdapter.class.getName();
    private static PaymentAPI paymentAPI;
    private static PaymentAPI merchantPaymentAPI;

    /**
     * It will return instance of PaymentAPI using that we can execute api calls.
     *
     * @param showNetworkNotAvailableDialog boolean , whether to show network not available
     *                                      dialog or not.
     * @return Payment API implementation
     */
    public static PaymentAPI getApiClient(boolean showNetworkNotAvailableDialog) {
        if (VeritransSDK.getVeritransSDK() != null
                && VeritransSDK.getVeritransSDK().getContext() != null
                && Utils.isNetworkAvailable(VeritransSDK.getVeritransSDK().getContext())) {
            if (paymentAPI == null) {
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                        .registerTypeAdapter(Date.class, new DateTypeAdapter())
                        .create();
                RestAdapter.Builder builder = new RestAdapter.Builder()
                        .setEndpoint(BuildConfig.BASE_URL)
                        .setConverter(new GsonConverter(gson))
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setClient(new OkClient(okHttpClient));
                RestAdapter restAdapter;
                if (BuildConfig.DEBUG) {
                    restAdapter = builder.build();
                    restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
                } else {
                    restAdapter = builder.build();
                }
                paymentAPI = restAdapter.create(PaymentAPI.class);
            }
            return paymentAPI;
        } else {
            if (showNetworkNotAvailableDialog) {
                VeritransBusProvider.getInstance().post(new NetworkUnavailableEvent());
            }
        }
        return null;
    }

    public static PaymentAPI getMerchantApiClient(boolean showNetworkNotAvailableDialog) {
        if (VeritransSDK.getVeritransSDK() != null
                && VeritransSDK.getVeritransSDK().getContext() != null
                && Utils.isNetworkAvailable(VeritransSDK.getVeritransSDK().getContext())) {

            if (merchantPaymentAPI == null && VeritransSDK.getVeritransSDK() != null) {

                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                        .registerTypeAdapter(Date.class, new DateTypeAdapter())
                        .create();
                RestAdapter.Builder builder = new RestAdapter.Builder()
                        .setConverter(new GsonConverter(gson))
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setClient(new OkClient(okHttpClient));
                RestAdapter restAdapter;

                builder.setEndpoint(VeritransSDK.getVeritransSDK().getMerchantServerUrl());
                restAdapter = builder.build();

                if (BuildConfig.DEBUG) {
                    restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
                }

                merchantPaymentAPI = restAdapter.create(PaymentAPI.class);

            } else if (VeritransSDK.getVeritransSDK() == null) {
                Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            }

            return merchantPaymentAPI;

        } else {
            if (showNetworkNotAvailableDialog) {
                VeritransBusProvider.getInstance().post(new NetworkUnavailableEvent());
            }
        }
        return null;
    }
}