package id.co.veritrans.sdk.scancard;

import android.app.Activity;
import android.content.Intent;

import id.co.veritrans.sdk.core.ExternalScanner;


/**
 * @author rakawm
 */
public class ScanCard extends ExternalScanner {

    /**
     * Start scanning using activity instance and request code.
     *
     * @param activity    activity instance
     * @param requestCode request code
     */
    @Override
    public void startScan(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ScanCardActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
