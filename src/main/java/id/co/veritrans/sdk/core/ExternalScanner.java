package id.co.veritrans.sdk.core;

import android.app.Activity;

import java.io.Serializable;

/**
 * Abstract class of external scanner implementation.
 *
 * @author rakawm
 */
public abstract class ExternalScanner {

    public static final String EXTRA_SCAN_DATA = "scan.data";

    /**
     * Abstract method of start scan.
     */
    public abstract void startScan(Activity activity, int requestCode);
}
