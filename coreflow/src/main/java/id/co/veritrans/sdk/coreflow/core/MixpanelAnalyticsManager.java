package id.co.veritrans.sdk.coreflow.core;

import android.util.Base64;

import com.google.gson.Gson;

import id.co.veritrans.sdk.coreflow.analytics.MixpanelApi;
import id.co.veritrans.sdk.coreflow.analytics.MixpanelEvent;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
            Observable<Integer> observable = api.trackEvent(data);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.getMessage());
                        }

                        @Override
                        public void onNext(Integer responseBody) {
                            Logger.i("Response: " + Integer.toString(responseBody));
                        }
                    });
        } else {
            Logger.e("No network connection");
        }
    }
}
