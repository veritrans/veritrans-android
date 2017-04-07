package com.midtrans.sdk.ui.views.webpayment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.utils.Logger;
import com.squareup.picasso.Picasso;


public class PaymentWebActivity extends BaseActivity {
    private String webUrl;
    private String type;
    private TextView titleText;
    private PaymentWebPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web);
        initPresenter();
        initDefaultProperties();
        initViews();
        initToolbar();

        MidtransWebviewFragment midtransWebviewFragment;
        if (type != null && !TextUtils.isEmpty(type)) {
            midtransWebviewFragment = MidtransWebviewFragment.newInstance(webUrl, type);
        } else {
            midtransWebviewFragment = MidtransWebviewFragment.newInstance(webUrl);
        }
        replaceFragment(midtransWebviewFragment, R.id.webview_container, true, false);
    }

    private void initPresenter() {
        presenter = new PaymentWebPresenter();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        titleText = (TextView) findViewById(R.id.page_title);
    }

    private void initToolbar() {
        // Set title
        setHeaderTitle(getString(R.string.processing_payment));

        // Set merchant logo
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        Picasso.with(this)
                .load(MidtransUi.getInstance().getTransaction().merchant.preference.logoUrl)
                .into(merchantLogo);

        initToolbarBackButton();
        // Set back button click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initDefaultProperties() {
        saveCurrentFragment = true;
        webUrl = getIntent().getStringExtra(Constants.WEB_VIEW_URL);
        type = getIntent().getStringExtra(Constants.WEB_VIEW_PARAM_TYPE);
        Logger.d(TAG, "weburl:" + webUrl);
        Logger.d(TAG, "type:" + type);
    }

    private void setHeaderTitle(String title) {
        titleText.setText(title);
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

        changeDialogButtonColor(dialog);
    }

    private void changeDialogButtonColor(AlertDialog alertDialog) {
        if (alertDialog.isShowing()
                && MidtransUi.getInstance() != null
                && MidtransUi.getInstance().getColorTheme() != null
                && MidtransUi.getInstance().getColorTheme().getPrimaryDarkColor() != 0) {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            positiveButton.setTextColor(MidtransUi.getInstance().getColorTheme().getPrimaryDarkColor());
            negativeButton.setTextColor(MidtransUi.getInstance().getColorTheme().getPrimaryDarkColor());
        }
    }
}
