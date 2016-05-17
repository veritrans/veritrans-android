package id.co.veritrans.sdk.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.VeritransSDK;

/**
 * @author rakawm
 */
public class BaseActivity extends AppCompatActivity {

    public void initializeTheme() {
        VeritransSDK mVeritransSDK = VeritransSDK.getVeritransSDK();
        if (mVeritransSDK != null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setBackgroundColor(mVeritransSDK.getThemeColor());
            }

            Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_toolbar);
            if (toolbar1 != null) {
                toolbar1.setBackgroundColor(mVeritransSDK.getThemeColor());
            }
        }
    }
}
