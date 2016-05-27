package id.co.veritrans.sdk.scancard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import id.co.veritrans.sdk.core.ExternalScanner;
import id.co.veritrans.sdk.models.ScannerModel;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * @author rakawm
 */
public class ScanCardActivity extends AppCompatActivity {

    public static final int SCAN_REQUEST_CODE = 10004;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startScan();
    }

    private void startScan() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);

        // SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                Log.d("ScanCardActivity", "Card Number: " + scanResult.cardNumber + "\n");
                ScannerModel model = new ScannerModel(scanResult.cardNumber, scanResult.cvv, scanResult.expiryMonth, scanResult.expiryYear);
                data.putExtra(ExternalScanner.EXTRA_SCAN_DATA, model);
                finishWithResult(data);
            }
        }
    }

    private void finishWithResult(Intent data) {
        setResult(RESULT_OK, data);
        finish();
    }
}
