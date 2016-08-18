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
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by chetan on 16/10/15.
 */
public class VeritransRestAdapter {
    private static final RestAdapter.LogLevel LOG_LEVEL = BuildConfig.FLAVOR.equalsIgnoreCase("development") ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;
    private static final String TAG = VeritransRestAdapter.class.getName();

    /**
     * It will return instance of PaymentAPI using that we can execute api calls.
     *
     * @return Payment API implementation
     */
    public static VeritransRestAPI getVeritransApiClient() {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                    .create();
            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setEndpoint(BuildConfig.BASE_URL)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(LOG_LEVEL)
                    .setClient(new OkClient(okHttpClient));
            RestAdapter restAdapter = builder.build();
            return restAdapter.create(VeritransRestAPI.class);
    }

    /**
     * Return Merchant API implementation
     *
     * @param merchantBaseURL Merchant base URL
     * @return Merchant API implementation
     */
    public static MerchantRestAPI getMerchantApiClient(String merchantBaseURL) {

            OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                    .create();
            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(LOG_LEVEL)
                    .setClient(new OkClient(okHttpClient))
                    .setEndpoint(merchantBaseURL);
            RestAdapter restAdapter = builder.build();
            return restAdapter.create(MerchantRestAPI.class);

    }


    /**
     * Return Mixpanel API implementation
     * @return
     */
    public static MixpanelApi getMixpanelApi() {
            OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setLogLevel(LOG_LEVEL)
                    .setClient(new OkClient(okHttpClient))
                    .setEndpoint(BuildConfig.MIXPANEL_URL);
            RestAdapter restAdapter = builder.build();
            return restAdapter.create(MixpanelApi.class);
    }

    /**
     * Return Snap API implementation
     * @return
     */
    public static SnapRestAPI getSnapRestAPI() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setLogLevel(LOG_LEVEL)
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(BuildConfig.SNAP_BASE_URL);
        return builder.build().create(SnapRestAPI.class);
    }
}