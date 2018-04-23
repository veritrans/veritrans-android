package com.midtrans.sdk.uikit.abstracts;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.MerchantPreferences;
import com.midtrans.sdk.corekit.models.PaymentDetails;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.MerchantData;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionDetails;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.adapters.TransactionDetailsAdapter;
import com.midtrans.sdk.uikit.models.MessageInfo;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.status.PaymentStatusActivity;
import com.midtrans.sdk.uikit.views.webview.WebViewPaymentActivity;
import com.midtrans.sdk.uikit.widgets.BoldTextView;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

import java.util.List;

/**
 * Created by ziahaqi on 7/20/17.
 */

public abstract class BasePaymentActivity extends BaseActivity {

    //for tracking first page
    public final static String USE_DEEP_LINK = "First Page";
    private static final String TAG = BasePaymentActivity.class.getSimpleName();
    protected boolean isDetailShown = false;
    private boolean hasMerchantLogo = false;

    protected BoldTextView textTotalAmount;
    protected TransactionDetailsAdapter transactionDetailAdapter;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        try {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    initMerchantLogo();
                    initToolbarBackButton();
                }
            });

            initItemDetails();
        } catch (Exception e) {
            Logger.e(TAG, "appbar:" + e.getMessage());
        }
    }


    private void initItemDetails() {
        View itemDetailContainer = findViewById(R.id.container_item_details);
        if (itemDetailContainer != null) {
            initTotalAmount();
        }
    }

    private void initTotalAmount() {
        final Transaction transaction = getMidtransSdk().getTransaction();
        if (transaction.getTransactionDetails() != null) {
            textTotalAmount = (BoldTextView) findViewById(R.id.text_amount);
            if (textTotalAmount != null) {
                PaymentDetails paymentDetails = getMidtransSdk().getPaymentDetails();
                if (paymentDetails != null) {
                    long totalAmount = paymentDetails.getTotalAmount();
                    long defaultTotalAmount = transaction.getTransactionDetails().getAmount();

                    String formattedTotalAmount = getString(R.string.prefix_money, Utils.getFormattedAmount(totalAmount));
                    textTotalAmount.setText(formattedTotalAmount);

                    changeTotalAmountColor(defaultTotalAmount, totalAmount);
                }

            }
        }
        initTransactionDetail(getMidtransSdk().getPaymentDetails().getItemDetailsList());
        //init dim
        findViewById(R.id.background_dim).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOrHideItemDetails();
            }
        });

        final LinearLayout amountContainer = (LinearLayout) findViewById(R.id.container_item_details);
        amountContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOrHideItemDetails();
            }
        });
    }

    private void initTransactionDetail(List<ItemDetails> details) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_transaction_detail);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            transactionDetailAdapter = new TransactionDetailsAdapter(details);
            recyclerView.setAdapter(transactionDetailAdapter);
        }
    }

    protected void displayOrHideItemDetails() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_transaction_detail);
        View dimView = findViewById(R.id.background_dim);
        if (recyclerView != null && dimView != null) {
            if (isDetailShown) {
                recyclerView.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.text_amount)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_amount_detail, 0);
                dimView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.text_amount)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                dimView.setVisibility(View.VISIBLE);
            }
            isDetailShown = !isDetailShown;
        }
    }

    protected void addNewItemDetails(final ItemDetails newItem) {
        if (transactionDetailAdapter != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    transactionDetailAdapter.addItemDetails(newItem);
                    changeTotalAmount();
                }
            }, 200);
        }
    }

    protected void removeItemDetails(final String itemId) {
        if (transactionDetailAdapter != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    transactionDetailAdapter.removeItemDetails(itemId);
                    changeTotalAmount();
                }
            }, 200);
        }
    }

    protected void changeTotalAmount() {
        if (textTotalAmount != null) {
            final long newTotalAmount = transactionDetailAdapter.getItemTotalAmount();

            String formattedTotalAmount = getString(R.string.prefix_money, Utils.getFormattedAmount(newTotalAmount));
            textTotalAmount.setText(formattedTotalAmount);

            TransactionDetails transactionDetails = getMidtransSdk().getTransaction().getTransactionDetails();
            if (transactionDetails != null) {
                changeTotalAmountColor(transactionDetails.getAmount(), newTotalAmount);
                PaymentDetails paymentDetails = getMidtransSdk().getPaymentDetails();

                if (paymentDetails != null) {
                    paymentDetails.changePaymentDetails(transactionDetailAdapter.getItemDetails(), newTotalAmount);
                }
            }
        }
    }

    private void changeTotalAmountColor(long totalAmount, long newTotalAmount) {
        int primaryColor = getPrimaryColor() != 0 ? getPrimaryColor() : ContextCompat.getColor(BasePaymentActivity.this, R.color.dark_gray);
        int amountColor = newTotalAmount == totalAmount
                ? primaryColor : ContextCompat.getColor(BasePaymentActivity.this, R.color.promoAmount);

        textTotalAmount.setTextColor(amountColor);
    }

    protected void initMerchantLogo() {
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        DefaultTextView merchantNameText = (DefaultTextView) findViewById(R.id.text_page_merchant_name);

        MerchantData merchantData = getMidtransSdk().getMerchantData();

        if (merchantData != null) {
            MerchantPreferences preferences = merchantData.getPreference();
            if (preferences != null) {
                String merchantName = preferences.getDisplayName();
                String merchantLogoUrl = preferences.getLogoUrl();
                if (!TextUtils.isEmpty(merchantLogoUrl)) {
                    if (merchantLogo != null) {
                        hasMerchantLogo = true;
                        Ion.with(merchantLogo).load(merchantLogoUrl);
                        merchantLogo.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (merchantName != null) {
                        if (merchantNameText != null && !TextUtils.isEmpty(merchantName)) {
                            hasMerchantLogo = true;
                            merchantNameText.setVisibility(View.VISIBLE);
                            merchantNameText.setText(merchantName);
                            if (merchantLogo != null) {
                                merchantLogo.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void initToolbarBackButton() {
        // Set toolbar back icon
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back);
            if (backIcon != null) {
                backIcon.setColorFilter(getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);
            }
            toolbar.setNavigationIcon(backIcon);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            adjustToolbarSize(toolbar);
        }
    }

    protected void adjustToolbarSize(Toolbar toolbar) {
        if (hasMerchantLogo) {
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
            params.height = params.height + (int) getResources().getDimension(R.dimen.toolbar_expansion_size);
            toolbar.setLayoutParams(params);
        }
    }

    protected void setPageTitle(@NonNull String title) {
        SemiBoldTextView textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
        if (textTitle != null) {
            textTitle.setText(title);
        }
    }

    protected void showPaymentStatusPage(TransactionResponse response, boolean showPaymentStatus) {
        if (isActivityRunning() && showPaymentStatus) {
            Intent intent = new Intent(this, PaymentStatusActivity.class);
            intent.putExtra(PaymentStatusActivity.EXTRA_PAYMENT_RESULT, response);
            startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
        } else {
            finishPayment(RESULT_OK, response);
        }
    }

    protected void showWebViewPaymentPage(TransactionResponse response, String paymentType) {
        if (isActivityRunning()) {
            Intent intent = new Intent(this, WebViewPaymentActivity.class);
            intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_TYPE, paymentType);
            intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_URL, response.getRedirectUrl());
            startActivityForResult(intent, UiKitConstants.INTENT_WEBVIEW_PAYMENT);
        } else {
            finishPayment(RESULT_OK, response);
        }
    }

    protected void finishPayment(int resultCode, TransactionResponse response) {
        Intent data = new Intent();
        data.putExtra(UiKitConstants.KEY_TRANSACTION_RESPONSE, response);
        setResult(resultCode, data);
        finish();
    }

    protected void showOnErrorPaymentStatusmessage(Throwable error) {
        showOnErrorPaymentStatusmessage(error, null);
    }

    protected void showOnErrorPaymentStatusmessage(Throwable error, String defaultmessage) {
        if (isActivityRunning()) {
            MessageInfo messageInfo = MessageUtil.createMessageOnError(this, error, defaultmessage);
            SdkUIFlowUtil.showToast(this, messageInfo.detailsMessage);
        }
    }

    protected boolean copyToClipboard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (isDetailShown) {
            displayOrHideItemDetails();
        } else {
            super.onBackPressed();
        }
    }
}
