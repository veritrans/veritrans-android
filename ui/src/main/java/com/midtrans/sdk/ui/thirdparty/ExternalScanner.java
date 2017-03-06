package com.midtrans.sdk.ui.thirdparty;

import android.app.Activity;

/**
 * Abstract class of external scanner implementation.
 *
 * @author rakawm
 */
public abstract class ExternalScanner {

    public static final String EXTRA_SCAN_DATA = "scan.data";

    public abstract void startScan(Activity activity, int requestCode);

}
