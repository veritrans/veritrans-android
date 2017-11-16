package com.midtrans.sdk.uikit.views.gopay.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.midtrans.sdk.uikit.views.gopay.authorization.GoPayAuthorizationActivitiy;
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
    private String fullPhoneNumber = "";
    private boolean isGojekInstalled;
    private Boolean isGojekInstalledWhenPaused;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressLayout();
        isGojekInstalled = Utils.isAppInstalled(this, GOJEK_PACKAGE_NAME);
        setContentView(R.layout.activity_gopay_payment);
        initProperties();
        initLayout();
        initActionButton();
        initData();
        hideProgressLayout();
    }

    private void initLayout() {
        ViewStub stub = (ViewStub) findViewById(R.id.gopay_layout_stub);
        stub.setLayoutResource(isGojekInstalled ? R.layout.layout_gopay_payment : R.layout.layout_install_gopay);
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
        if (isGojekInstalled) {
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGoPayPayment();
                }
            });
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
        SdkUIFlowUtil.hideKeyboard(this);
        showProgressLayout();
        // TODO: 16/11/17 use startActivityForResult 
        startActivity(new Intent(this, GoPayStatusActivity.class));
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
        // TODO: 15/11/17 need to check this
        hideProgressLayout();
        if (isActivityRunning()) {
            Intent intent = new Intent(this, GoPayAuthorizationActivitiy.class);
            intent.putExtra(GoPayAuthorizationActivitiy.EXTRA_PHONE_NUMBER, fullPhoneNumber);
            startActivityForResult(intent, UiKitConstants.INTENT_VERIFICATION);
        } else {
            finishPayment(RESULT_OK, presenter.getTransactionResponse());
        }
    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {
        hideProgressLayout();
        showPaymentStatusPage(response, presenter.isShowPaymentStatusPage());
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusmessage(error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
                finishPayment(RESULT_OK, presenter.getTransactionResponse());
            } else if (requestCode == UiKitConstants.INTENT_VERIFICATION) {
                finishPayment(RESULT_OK, data);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
                finishPayment(RESULT_OK, presenter.getTransactionResponse());
            }
        }
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }

}
