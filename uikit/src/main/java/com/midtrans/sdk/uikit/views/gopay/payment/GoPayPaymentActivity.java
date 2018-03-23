package com.midtrans.sdk.uikit.views.gopay.payment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.gopay.status.GoPayStatusActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.Utils;

/**
 * Created by ziahaqi on 9/7/17.
 */

public class GoPayPaymentActivity extends BasePaymentActivity implements GoPayPaymentView {

    private static final String TAG = GoPayPaymentActivity.class.getSimpleName();
    private final String GOJEK_PACKAGE_NAME = "com.gojek.app";

    private FancyButton buttonPrimary;
    private FancyButton buttonDownload;
    private View buttonPrimaryLayout;

    private GopayPaymentPresenter presenter;
    private boolean isTablet, isGojekInstalled, isAlreadyGotResponse;
    private Boolean isGojekInstalledWhenPaused;
    private int attempt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressLayout();
        isTablet = SdkUIFlowUtil.getDeviceType(this).equals(SdkUIFlowUtil.TYPE_TABLET);
        isGojekInstalled = Utils.isAppInstalled(this, GOJEK_PACKAGE_NAME);
        setContentView(R.layout.activity_gopay_payment);
        initProperties();
        initLayout();
        initData();
        initActionButton();
        hideProgressLayout();
    }

    private void initLayout() {
        ViewStub stub = (ViewStub) findViewById(R.id.gopay_layout_stub);
        if (isTablet) {
            stub.setLayoutResource(R.layout.layout_gopay_payment_tablet);
        } else {
            stub.setLayoutResource(
                    isGojekInstalled ? R.layout.layout_gopay_payment : R.layout.layout_install_gopay);
        }
        stub.inflate();
    }

    private void initData() {
        setPageTitle(getString(R.string.gopay));
    }

    private void initActionButton() {
        if (isGojekInstalled || isTablet) {
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to prevent "payment has been paid"
                    if (isAlreadyGotResponse) {
                        openDeeplink(presenter.getTransactionResponse().getDeeplinkUrl());
                    } else {
                        startGoPayPayment();
                    }
                }
            });
            buttonPrimary.setTextBold();
            buttonPrimary.setText(getString(R.string.gopay_confirm_button));

            buttonPrimary.setIconResource(R.drawable.ic_gopay_white);
            buttonPrimary.setIconPosition(FancyButton.POSITION_RIGHT);
        } else {
            //hide confirm button and adjust item details to bottom of screen
            buttonPrimaryLayout.setVisibility(View.GONE);
            findViewById(R.id.primary_button_separator).setVisibility(View.GONE);
            View itemDetail = findViewById(R.id.container_item_details);
            RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            itemDetail.setLayoutParams(layoutParams);

            buttonDownload = (FancyButton) findViewById(R.id.button_download_gojek);
            setTextColor(buttonDownload);
            setIconColorFilter(buttonDownload);
            buttonDownload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.openAppInPlayStore(GoPayPaymentActivity.this, GOJEK_PACKAGE_NAME);
                }
            });
        }
    }

    private void startGoPayPayment() {
        showProgressLayout();
        presenter.startGoPayPayment();
    }

    @Override
    public void bindViews() {
        buttonPrimary = (FancyButton) findViewById(R.id.button_primary);
        buttonPrimaryLayout = findViewById(R.id.layout_primary_button);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPrimary);
    }

    private void initProperties() {
        presenter = new GopayPaymentPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGojekInstalled = Utils.isAppInstalled(this, GOJEK_PACKAGE_NAME);
        if (isGojekInstalledWhenPaused != null && isGojekInstalledWhenPaused != isGojekInstalled) {
            recreate();
        }
    }

    @Override
    protected void onPause() {
        isGojekInstalledWhenPaused = isGojekInstalled;
        super.onPause();
    }

    @Override
    public void onPaymentSuccess(final TransactionResponse response) {
        hideProgressLayout();
        if (isActivityRunning()) {
            if (isResponseValid(response)) {
                if (isTablet) {
                    Intent intent = new Intent(this, GoPayStatusActivity.class);
                    intent.putExtra(GoPayStatusActivity.EXTRA_PAYMENT_STATUS, response);
                    startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
                } else {
                    isAlreadyGotResponse = true;

                    openDeeplink(response.getDeeplinkUrl());
                }
            } else {
                onPaymentFailure(response);
            }
        } else {
            finishPayment(RESULT_OK, presenter.getTransactionResponse());
        }
    }

    /**
     * Check whether the transaction response from server is valid or not
     * Valid if both deeplink URL and qr code URL aren't empty, or at least one of them is not,
     * depending on which one that will be used
     *
     * @param response transaction response
     * @return validity of response
     */
    private boolean isResponseValid(TransactionResponse response) {
        if (response == null) {
            return false;
        } else {
            if (TextUtils.isEmpty(response.getDeeplinkUrl()) && !isTablet) {
                return false;
            }
            if (TextUtils.isEmpty(response.getQrCodeUrl()) && isTablet) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {
        hideProgressLayout();
        if (attempt < UiKitConstants.MAX_ATTEMPT) {
            attempt += 1;
            SdkUIFlowUtil.showToast(GoPayPaymentActivity.this, getString(R.string.error_gopay_transaction));
        } else {
            if (response != null) {
                showPaymentStatusPage(response, presenter.isShowPaymentStatusPage());
            }
        }
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusmessage(error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
            finishPayment(RESULT_OK, presenter.getTransactionResponse());
        }
    }

    private void openDeeplink(String deeplinkUrl) {
        Toast.makeText(this, getString(R.string.redirecting_to_gopay), Toast.LENGTH_SHORT)
            .show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl));
        startActivity(intent);
    }

    private void showConfirmationDialog(String message) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(GoPayPaymentActivity.this, R.style.AlertDialogCustom)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!GoPayPaymentActivity.this.isFinishing()) {
                            dialog.dismiss();
                            finishPayment(RESULT_CANCELED, presenter.getTransactionResponse());
                        }
                    }
                })
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!GoPayPaymentActivity.this.isFinishing()) {
                            dialog.dismiss();
                        }
                    }
                })
                .setTitle(R.string.cancel_transaction)
                .setMessage(message)
                .create();
            dialog.show();
        } catch (Exception e) {
            Logger.e(TAG, "showDialog:" + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        if (isDetailShown) {
            displayOrHideItemDetails();
        } else if (isAlreadyGotResponse) {
            showConfirmationDialog(getString(R.string.confirm_gopay_deeplink));
        } else {
            super.onBackPressed();
        }
    }
}
