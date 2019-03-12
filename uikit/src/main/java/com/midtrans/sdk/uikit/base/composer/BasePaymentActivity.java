package com.midtrans.sdk.uikit.base.composer;

import com.google.android.material.appbar.AppBarLayout;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.base.enums.Currency;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.mandatory.TransactionDetails;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AlfamartPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikPayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CreditCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.TelkomselCashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantPreferences;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.CurrencyHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.PaymentListActivity;
import com.midtrans.sdk.uikit.view.method.banktransfer.result.BankTransferResultPresenter;
import com.midtrans.sdk.uikit.view.method.status.PaymentStatusActivity;
import com.midtrans.sdk.uikit.view.method.webview.WebViewPaymentActivity;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

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
        initItemDetails(paymentInfoResponse);
        initTheme();
        initializeTheme();
        initMerchantPreferences();
    }

    protected abstract void initTheme();

    protected void initProperties() {
        paymentType = getIntent().getStringExtra(EXTRA_PAYMENT_TYPE);
        paymentInfoResponse = (PaymentInfoResponse) getIntent().getSerializableExtra(PaymentListActivity.EXTRA_PAYMENT_INFO);
    }

    protected void addNewItemDetails(final Item newItem) {
        if (detailAdapter != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    detailAdapter.addItemDetails(newItem);
                    changeTotalAmount();
                }
            }, 200);
        }
    }

    protected void removeItemDetails(final String itemId) {
        if (detailAdapter != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    detailAdapter.removeItemWithId(itemId);
                    changeTotalAmount();
                }
            }, 200);
        }
    }

    protected void changeTotalAmount() {
        if (textTotalAmount != null) {
            final double newTotalAmount = detailAdapter.getItemTotalAmount();
            String currency = Currency.IDR;

            TransactionDetails transactionDetails = paymentInfoResponse.getTransactionDetails();
            if (transactionDetails != null) {
                changeTotalAmountColor(transactionDetails.getGrossAmount(), newTotalAmount);
                currency = transactionDetails.getCurrency();
                PaymentInfoResponse paymentDetails = paymentInfoResponse;

                if (paymentDetails != null) {
                    paymentDetails.changePaymentDetails(detailAdapter.getAllData(), newTotalAmount);
                }
            }

            setTotalAmount(CurrencyHelper.formatAmount(this, newTotalAmount, currency));
        }
    }

    private void changeTotalAmountColor(double totalAmount, double newTotalAmount) {
        int primaryColor = getPrimaryColor() != 0 ? getPrimaryColor() : ContextCompat.getColor(BasePaymentActivity.this, R.color.dark_gray);
        int amountColor = newTotalAmount == totalAmount
                ? primaryColor : ContextCompat.getColor(BasePaymentActivity.this, R.color.promoAmount);

        textTotalAmount.setTextColor(amountColor);
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
            merchantNameInToolbar.setVisibility(View.GONE);
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
        intent.putExtra(Constants.INTENT_DATA_INFO, paymentInfoResponse);
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
        } else if (response instanceof AlfamartPaymentResponse) {
            AlfamartPaymentResponse alfamartPaymentResponse = (AlfamartPaymentResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, alfamartPaymentResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.ALFAMART);
        } else if (response instanceof BcaKlikPayResponse) {
            BcaKlikPayResponse bcaKlikpayResponse = (BcaKlikPayResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, bcaKlikpayResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.BCA_KLIKPAY);
        } else if (response instanceof TelkomselCashResponse) {
            TelkomselCashResponse telkomselCashResponse = (TelkomselCashResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, telkomselCashResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.TELKOMSEL_CASH);
        } else if (response instanceof MandiriClickpayResponse) {
            MandiriClickpayResponse mandiriClickpayResponse = (MandiriClickpayResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, mandiriClickpayResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.MANDIRI_CLICKPAY);
        } else if (response instanceof CreditCardResponse) {
            CreditCardResponse creditCardResponse = (CreditCardResponse) response;
            data.putExtra(Constants.INTENT_DATA_CALLBACK, creditCardResponse);
            data.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.CREDIT_CARD);
        }
        setResult(resultCode, data);
        super.onBackPressed();
    }

    protected void showWebViewPaymentPage(@PaymentType String paymentType, String redirectUrl, int intentCode) {
        Intent intent = new Intent(this, WebViewPaymentActivity.class);
        intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_TYPE, paymentType);
        intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_URL, redirectUrl);
        intent.putExtra(PaymentListActivity.EXTRA_PAYMENT_INFO, paymentInfoResponse);
        startActivityForResult(intent, intentCode);
    }

    protected void showWebViewPaymentPage(@PaymentType String paymentType, String redirectUrl) {
        showWebViewPaymentPage(paymentType, redirectUrl, Constants.INTENT_WEBVIEW_PAYMENT);
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