package id.co.veritrans.sdk.core;

import android.app.Activity;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import id.co.veritrans.sdk.BuildConfig;
import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.utilities.Utils;
import id.co.veritrans.sdk.widgets.VeritransDialog;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by chetan on 16/10/15.
 */
class VeritransRestAdapter {

    private static final String TAG = VeritransRestAdapter.class.getName();
    private static VeritranceApiInterface veritranceApiInterface;
    private static VeritranceApiInterface merchantVeritranceApiInterface;

    /**
     * It will return instance of VeritranceApiInterface using that we can execute api calls.
     *
     * @param activity                      reference of activity in which we want to show dialog.
     * @param showNetworkNotAvailableDialog boolean , whether to show network not available
     *                                      dialog or not.
     * @return
     */
    public static VeritranceApiInterface getApiClient(final Activity activity,
                                                      boolean showNetworkNotAvailableDialog) {
        if (Utils.isNetworkAvailable(activity)) {

            if (veritranceApiInterface == null) {
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


                if (BuildConfig.DEBUG) {
                    builder.setEndpoint(Constants.BASE_URL_FOR_DEBUG);
                    restAdapter = builder.build();
                    restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

                } else {
                    builder.setEndpoint(Constants.BASE_URL_FOR_RELEASE);
                    restAdapter = builder.build();
                }


                veritranceApiInterface = restAdapter.create(VeritranceApiInterface.class);
            }

            return veritranceApiInterface;

        } else {

            if (showNetworkNotAvailableDialog && activity != null) {
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VeritransDialog dialog = new VeritransDialog(activity,
                                    activity.getString(R.string.no_network),
                                    activity.getString(R.string.no_network_msg),
                                    activity.getString(R.string.ok), null);
                            dialog.show();
                        }
                    });

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        }

        return null;

    }

    public static VeritranceApiInterface getMerchantApiClient(final Activity activity,
                                                      boolean showNetworkNotAvailableDialog) {
        if (Utils.isNetworkAvailable(activity)) {

            if (merchantVeritranceApiInterface == null) {
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


                if (BuildConfig.DEBUG) {
                    builder.setEndpoint(Constants.BASE_URL_MERCHANT_FOR_DEBUG);
                    restAdapter = builder.build();
                    restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

                } else {
                    builder.setEndpoint(Constants.BASE_URL_MERCHANT_FOR_RELEASE);
                    restAdapter = builder.build();
                }


                merchantVeritranceApiInterface = restAdapter.create(VeritranceApiInterface.class);
            }

            return merchantVeritranceApiInterface;

        } else {

            if (showNetworkNotAvailableDialog && activity != null) {
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VeritransDialog dialog = new VeritransDialog(activity,
                                    activity.getString(R.string.no_network),
                                    activity.getString(R.string.no_network_msg),
                                    activity.getString(R.string.ok), null);
                            dialog.show();
                        }
                    });

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        }

        return null;

    }
}