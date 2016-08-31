package id.co.veritrans.sdk.uiflow.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.fragments.WebviewFragment;

public class PaymentWebActivity extends BaseActivity {
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private VeritransSDK veritransSDK;
    private RelativeLayout webviewContainer;
    private ImageView logo;
    private String webUrl;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web);
        veritransSDK = VeritransSDK.getVeritransSDK();

        saveCurrentFragment = true;
        webUrl = getIntent().getStringExtra(Constants.WEBURL);
        if (getIntent().getStringExtra(Constants.TYPE) != null && !getIntent().getStringExtra(Constants.TYPE).equals("")) {
            type = getIntent().getStringExtra(Constants.TYPE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        initializeTheme();

        fragmentManager = getSupportFragmentManager();
        toolbar.setTitle(R.string.processing_payment);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebviewFragment webviewFragment;
        if (!type.equals("")) {
            webviewFragment = WebviewFragment.newInstance(webUrl, type);
        } else {
            webviewFragment = WebviewFragment.newInstance(webUrl);
        }
        replaceFragment(webviewFragment, R.id.webview_container, true, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == android.R.id.home) {
            showCancelConfirmationDialog();
            return true;
        } else if (item.getItemId() == R.id.action_close) {
            showCancelConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showCancelConfirmationDialog();
    }

    private void showCancelConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_CANCELED, returnIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle(R.string.cancel_transaction)
                .setMessage(R.string.cancel_transaction_message)
                .create();
        dialog.show();
    }

}
