package com.midtrans.sdk.corekit.core;

import android.app.Activity;

/**
 * @author ziahaqi
 */
public interface IScanner {
    /**
     * Abstract method of start scan.
     */
    void startScan(Activity activity, int requestCode);
}
