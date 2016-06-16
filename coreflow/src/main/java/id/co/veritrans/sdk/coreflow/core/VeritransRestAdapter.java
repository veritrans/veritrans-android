package id.co.veritrans.sdk.coreflow.core;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import id.co.veritrans.sdk.coreflow.BuildConfig;
import id.co.veritrans.sdk.coreflow.analytics.MixpanelApi;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.utilities.Utils;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by chetan on 16/10/15.
 */
class VeritransRestAdapter {
    private static final RestAdapter.LogLevel LOG_LEVEL = BuildConfig.FLAVOR.equalsIgnoreCase("development") ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;
    private static final String TAG = VeritransRestAdapter.class.getName();
    private static PaymentAPI paymentAPI;
    private static PaymentAPI merchantPaymentAPI;
    private static MixpanelApi mixpanelApi;

    /**
     * It will return instance of PaymentAPI using that we can execute api calls.
     *
     * @param showNetworkNotAvailableDialog boolean , whether to show network not available
     *                                      dialog or not.
     * @return Payment API implementation
     */
    public static PaymentAPI getApiClient(boolean showNetworkNotAvailableDialog) {
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null
                && veritransSDK.getContext() != null
                && Utils.isNetworkAvailable(veritransSDK.getContext())) {
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
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null
                && veritransSDK.getContext() != null
                && Utils.isNetworkAvailable(veritransSDK.getContext())) {

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

    public static MixpanelApi getMixpanelApi() {
        VeritransSDK paymentSdk = VeritransSDK.getVeritransSDK();
        if (paymentSdk != null
                && paymentSdk.getContext() != null
                && Utils.isNetworkAvailable(paymentSdk.getContext())) {
            if (mixpanelApi == null) {
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

                RestAdapter.Builder builder = new RestAdapter.Builder()
                        .setLogLevel(LOG_LEVEL)
                        .setClient(new OkClient(okHttpClient));
                RestAdapter restAdapter;

                builder.setEndpoint(BuildConfig.MIXPANEL_URL);
                restAdapter = builder.build();
                mixpanelApi = restAdapter.create(MixpanelApi.class);
            }
            return mixpanelApi;
        } else {
            VeritransBusProvider.getInstance().post(new NetworkUnavailableEvent());
        }
        return null;
    }

}