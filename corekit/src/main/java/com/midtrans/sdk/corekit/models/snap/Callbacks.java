package com.midtrans.sdk.corekit.models.snap;

/**
 * Created by rakawm on 10/12/16.
 */

public class Callbacks {
    private String finish;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
}
