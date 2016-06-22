package id.co.veritrans.sdk.coreflow.core;

import android.app.Activity;

/**
 *
 * @author ziahaqi
 */
public interface IScanner {
    /**
     * Abstract method of start scan.
     */
     void startScan(Activity activity, int requestCode);
}
