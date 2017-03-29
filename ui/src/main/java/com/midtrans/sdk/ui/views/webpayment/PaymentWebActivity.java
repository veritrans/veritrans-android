package com.midtrans.sdk.ui.views.webpayment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.utils.Logger;


public class PaymentWebActivity extends BaseActivity {
    private String webUrl;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web);
        initDefaultProperties();
        initViews();
        setupViews();

        MidtransWebviewFragment midtransWebviewFragment;
        if (type != null && !TextUtils.isEmpty(type)) {
            midtransWebviewFragment = MidtransWebviewFragment.newInstance(webUrl, type);
        } else {
            midtransWebviewFragment = MidtransWebviewFragment.newInstance(webUrl);
        }
        replaceFragment(midtransWebviewFragment, R.id.webview_container, true, false);
    }

    private void setupViews() {
        toolbar.setTitle(R.string.processing_payment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void initDefaultProperties() {
        saveCurrentFragment = true;
        webUrl = getIntent().getStringExtra(Constants.WEB_VIEW_URL);
        type = getIntent().getStringExtra(Constants.WEB_VIEW_PARAM_TYPE);
        Logger.d(TAG, "weburl:" + webUrl);
        Logger.d(TAG, "type:" + type);
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
