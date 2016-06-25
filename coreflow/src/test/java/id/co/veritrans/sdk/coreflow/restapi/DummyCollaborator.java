package id.co.veritrans.sdk.coreflow.restapi;

import java.util.Collections;

import id.co.veritrans.sdk.coreflow.models.AuthModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ziahaqi on 24/06/2016.
 */
public class DummyCollaborator {
    public static int ERROR_CODE = 1;

    public DummyCollaborator() {
        // empty
    }

    public void doSomethingAsynchronously (final Callback<AuthModel> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    callback.success(new AuthModel(), null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
