package com.midtrans.sdk.corekit.core.snap.model.transaction.response.callback;

import java.io.Serializable;

public class Callbacks implements Serializable {

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