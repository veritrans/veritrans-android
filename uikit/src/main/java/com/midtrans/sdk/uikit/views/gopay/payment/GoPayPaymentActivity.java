package com.midtrans.sdk.uikit.views.gopay.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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
    private boolean isTablet;
    private boolean isGojekInstalled;
    private Boolean isGojekInstalledWhenPaused;
    private int attempt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressLayout();
        isTablet = SdkUIFlowUtil.getDeviceType(this).equals("TABLET");
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
        if (isGojekInstalled) {
            buttonPrimary.setText(getString(R.string.confirm_payment));
            buttonPrimary.setTextBold();
        }
    }

    private void initActionButton() {
        if (isGojekInstalled || isTablet) {
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGoPayPayment();
                }
            });
            buttonPrimary.setTextBold();
            if (isTablet) {
                buttonPrimary.setText(getString(R.string.payment_method_description_gopay));
            }
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
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        if (isActivityRunning()) {
            if (isResponseValid(response)) {
                Intent intent = new Intent(this, GoPayStatusActivity.class);
                intent.putExtra(GoPayStatusActivity.EXTRA_PAYMENT_STATUS, response);
                startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
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
}
