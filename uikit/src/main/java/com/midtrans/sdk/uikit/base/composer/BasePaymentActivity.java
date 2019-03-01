package com.midtrans.sdk.uikit.base.composer;

import com.google.android.material.appbar.AppBarLayout;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantPreferences;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.PaymentListActivity;
import com.midtrans.sdk.uikit.view.banktransfer.result.BankTransferResultPresenter;
import com.midtrans.sdk.uikit.view.status.PaymentStatusActivity;
import com.midtrans.sdk.uikit.view.webview.WebViewPaymentActivity;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.LayoutRes;

public abstract class BasePaymentActivity extends BaseActivity {

    public static final String EXTRA_PAYMENT_TYPE = "intent.extra.payment.type";

    private static final int PAGE_MARGIN = 20;
    private static final int CURRENT_POSITION = -1;

    protected FancyButton buttonCompletePayment;
    protected BankTransferResultPresenter presenter;
    protected PaymentInfoResponse paymentInfoResponse;

    protected String paymentType;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initProperties();
        initToolbarAndView();
        initMerchantPreferences();
        initItemDetails(paymentInfoResponse);
        initTheme();
        initializeTheme();
    }

    protected abstract void initTheme();

    protected void initProperties() {
        paymentType = getIntent().getStringExtra(EXTRA_PAYMENT_TYPE);
        paymentInfoResponse = (PaymentInfoResponse) getIntent().getSerializableExtra(PaymentListActivity.EXTRA_PAYMENT_INFO);
    }

    /**
     * This method use for setup view stuff based on response and merchant preferences
     */
    private void initMerchantPreferences() {
        MerchantPreferences preferences = paymentInfoResponse.getMerchantData().getPreference();
        if (!TextUtils.isEmpty(preferences.getDisplayName())) {
            merchantNameInToolbar.setText(preferences.getDisplayName());
            merchantNameInToolbar.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(preferences.getLogoUrl())) {
            Ion.with(merchantLogoInToolbar)
                    .load(preferences.getLogoUrl());
            merchantLogoInToolbar.setVisibility(View.VISIBLE);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
            params.height = params.height + (int) getResources().getDimension(R.dimen.toolbar_expansion_size);
            toolbar.setLayoutParams(params);
        }
        if (!TextUtils.isEmpty(paymentType)) {
            paymentMethodTitleInToolbar.setText(PaymentListHelper.mappingPaymentTitle(this, paymentType));
            paymentMethodTitleInToolbar.setVisibility(View.VISIBLE);
        }
    }

    protected void startResultActivity(int code, PaymentResponse response) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.INTENT_DATA_CALLBACK, response);
        startActivityForResult(intent, code);
    }

    protected <T> void finishPayment(int resultCode, T response) {
        Intent data = new Intent();
        if (response instanceof GopayResponse) {
            GopayResponse gopayResponse = (GopayResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, gopayResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.GOPAY);
        } else if (response instanceof IndomaretPaymentResponse) {
            IndomaretPaymentResponse indomaretResponse = (IndomaretPaymentResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, indomaretResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.INDOMARET);
        } else if (response instanceof KlikBcaResponse) {
            KlikBcaResponse klikBcaResponse = (KlikBcaResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, klikBcaResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.KLIK_BCA);
        } else if (response instanceof CimbClicksResponse) {
            CimbClicksResponse cimbClicksResponse = (CimbClicksResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, cimbClicksResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.CIMB_CLICKS);
        } else if (response instanceof DanamonOnlineResponse) {
            DanamonOnlineResponse danamonOnlineResponse = (DanamonOnlineResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, danamonOnlineResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.DANAMON_ONLINE);
        } else if (response instanceof AkulakuResponse) {
            AkulakuResponse akulakuResponse = (AkulakuResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, akulakuResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.AKULAKU);
        } else if (response instanceof BriEpayPaymentResponse) {
            BriEpayPaymentResponse briEpayPaymentResponse = (BriEpayPaymentResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, briEpayPaymentResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.BRI_EPAY);
        } else if (response instanceof MandiriEcashResponse) {
            MandiriEcashResponse mandiriEcashResponse = (MandiriEcashResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, mandiriEcashResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.MANDIRI_ECASH);
        }
        setResult(resultCode, data);
        super.onBackPressed();
    }

    protected void showWebViewPaymentPage(@PaymentType String paymentType, String redirectUrl) {
        Intent intent = new Intent(this, WebViewPaymentActivity.class);
        intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_TYPE, paymentType);
        intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_URL, redirectUrl);
        intent.putExtra(PaymentListActivity.EXTRA_PAYMENT_INFO, paymentInfoResponse);
        startActivityForResult(intent, Constants.INTENT_WEBVIEW_PAYMENT);
    }

    protected void finishWebViewPayment(WebViewPaymentActivity activity, int resultCode) {
        Intent returnIntent = new Intent();
        activity.setResult(resultCode, returnIntent);
        activity.finish();
    }

    /**
     * This method used for binding view and setup other view stuff like toolbar and progress image
     */
    protected void initToolbarAndView() {
        toolbar = findViewById(R.id.toolbar_base);
        buttonCompletePayment = findViewById(R.id.button_primary);
    }

    protected void setTitle(String title) {
        paymentMethodTitleInToolbar.setText(title);
    }
}