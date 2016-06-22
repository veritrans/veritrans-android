package id.co.veritrans.sdk.coreflow.core;

import android.util.Base64;

import com.google.gson.Gson;

import id.co.veritrans.sdk.coreflow.analytics.MixpanelApi;
import id.co.veritrans.sdk.coreflow.analytics.MixpanelEvent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author rakawm
 */
public class MixpanelAnalyticsManager {

    /**
     * Track event for mixpanel.
     *
     * @param event Mixpanel parameter.
     */
    public static void trackEvent(MixpanelEvent event) {
        MixpanelApi api = VeritransRestAdapter.getMixpanelApi();

        if (api != null) {
            Gson gson = new Gson();
            String eventObject = gson.toJson(event);
            String data = Base64.encodeToString(eventObject.getBytes(), Base64.DEFAULT);
            api.trackEvent(data, new Callback<Integer>() {
                @Override
                public void success(Integer integer, Response response) {
                    Logger.i("Response: " + Integer.toString(integer));

                }

                @Override
                public void failure(RetrofitError error) {
                    Logger.i("Response>error: " + error.getMessage());

                }
            });
        } else {
            Logger.e("No network connection");
        }
    }
}
