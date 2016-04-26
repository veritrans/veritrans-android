package id.co.veritrans.sdk.analytics;


import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author rakawm
 */
public interface MixpanelApi {
    @GET("/track")
    Observable<Integer> trackEvent(
            @Query("data") String data
    );
}
