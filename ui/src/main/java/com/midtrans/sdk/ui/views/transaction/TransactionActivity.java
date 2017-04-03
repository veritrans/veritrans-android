package com.midtrans.sdk.ui.views.transaction;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.adapters.PaymentMethodsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.views.banktransfer.list.BankTransferListActivity;
import com.midtrans.sdk.ui.views.creditcard.details.CreditCardDetailsActivity;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class TransactionActivity
        extends BaseActivity
        implements TransactionContract.View,
        PaymentMethodsAdapter.PaymentMethodListener,
        ItemDetailsAdapter.ItemDetailListener {

    private TransactionPresenter presenter;
    private PaymentMethodsAdapter paymentMethodsAdapter;
    private ItemDetailsAdapter itemDetailsAdapter;

    private LinearLayout layoutProgressContainer;
    private RecyclerView paymentMethodsContainer;
    private RecyclerView itemDetailsContainer;
    private DefaultTextView merchantNameTitle;
    private DefaultTextView errorMessageText;
    private FancyButton retryButton;
    private FancyButton cancelButton;
    private RelativeLayout containerError;
    private ImageView merchantLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initProperties();
        initViews();
        initThemeColor();
        bindViews();
        initTransaction();
        initRetryAndCancel();
    }

    private void initProperties() {
        presenter = new TransactionPresenter(this, this);
        paymentMethodsAdapter = new PaymentMethodsAdapter(this);
        itemDetailsAdapter = new ItemDetailsAdapter(this, presenter.getItemDetails());
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        layoutProgressContainer = (LinearLayout) findViewById(R.id.progress_container);
        paymentMethodsContainer = (RecyclerView) findViewById(R.id.container_payment_methods);
        itemDetailsContainer = (RecyclerView) findViewById(R.id.container_item_details);
        merchantNameTitle = (DefaultTextView) findViewById(R.id.merchant_name);
        merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        retryButton = (FancyButton) findViewById(R.id.checkout_retry_button);
        cancelButton = (FancyButton) findViewById(R.id.cancel_button);
        containerError = (RelativeLayout) findViewById(R.id.container_error);
        errorMessageText = (DefaultTextView) findViewById(R.id.error_text);
    }

    private void initTransaction() {
        showProgressContainer(true);
        presenter.init();
    }

    private void bindViews() {
        setSupportActionBar(toolbar);

        paymentMethodsContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        paymentMethodsContainer.setAdapter(paymentMethodsAdapter);

        itemDetailsContainer.setLayoutManager(new LinearLayoutManager(this));
        itemDetailsContainer.setAdapter(itemDetailsAdapter);
    }


    @Override
    public void showMerchantNameOrLogo(final String merchantName, String merchantLogoUrl) {
        if (merchantLogoUrl != null && !TextUtils.isEmpty(merchantLogoUrl)) {
            merchantNameTitle.setVisibility(View.GONE);
            merchantLogo.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(merchantLogoUrl)
                    .into(merchantLogo, new Callback() {
                        @Override
                        public void onSuccess() {
                            Logger.d(TAG, "Success to load merchant logo.");
                        }

                        @Override
                        public void onError() {
                            Logger.d(TAG, "Failed to load merchant logo.");
                            merchantLogo.setVisibility(View.GONE);
                            merchantNameTitle.setVisibility(View.VISIBLE);
                            merchantNameTitle.setText(merchantName);
                        }
                    });
        } else {
            merchantLogo.setVisibility(View.GONE);
            merchantNameTitle.setVisibility(View.VISIBLE);
            merchantNameTitle.setText(merchantName);
        }
    }

    @Override
    public void showProgressContainer(boolean shown) {
        if (shown) {
            layoutProgressContainer.setVisibility(View.VISIBLE);
        } else {
            layoutProgressContainer.setVisibility(View.GONE);
        }
    }

    /**
     * @param enabledPayments List of enabled payment method
     */
    @Override
    public void showPaymentMethods(List<PaymentMethodModel> enabledPayments) {
        initTheme();
        itemDetailsContainer.setBackgroundColor(presenter.getPrimaryColor());
        paymentMethodsAdapter.setData(enabledPayments);
    }

    @Override
    public void updateColorTheme() {
        Drawable backButton = ContextCompat.getDrawable(this, R.drawable.ic_back);
        backButton.setColorFilter(MidtransUi.getInstance().getColorTheme().getPrimaryDarkColor(), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(backButton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendPaymentResult(new PaymentResult(true));
                finish();
            }
        });
    }

    @Override
    public void showErrorContainer() {
        errorMessageText.setText(getString(R.string.error_message_failed_checkout));
        containerError.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorContainer(String message, boolean needRetry) {
        errorMessageText.setText(message);
        containerError.setVisibility(View.VISIBLE);
        if (needRetry) {
            retryButton.setVisibility(View.VISIBLE);
        } else {
            retryButton.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(int position) {
        PaymentMethodModel paymentMethod = paymentMethodsAdapter.getItem(position);
        Logger.d(TAG, "method:" + paymentMethod.getName());
        showPaymentActivity(paymentMethod);
    }

    private void showPaymentActivity(PaymentMethodModel paymentMethod) {
        switch (paymentMethod.getPaymentType()) {
            case PaymentType.CREDIT_CARD:
                Intent intent = new Intent(this, CreditCardDetailsActivity.class);
                startActivityForResult(intent, Constants.INTENT_CODE_PAYMENT);
                break;
            case PaymentType.BANK_TRANSFER:
                Intent bankTransferIntent = new Intent(this, BankTransferListActivity.class);
                bankTransferIntent.putStringArrayListExtra(BankTransferListActivity.ARGS_BANK_LIST, new ArrayList<>(presenter.getBankList()));
                startActivityForResult(bankTransferIntent, Constants.INTENT_CODE_PAYMENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemShown() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(TAG, "onActivityResult(): request code is " + requestCode + "," + resultCode);

        if (requestCode == Constants.INTENT_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                PaymentResult result = (PaymentResult) data.getSerializableExtra(Constants.PAYMENT_RESULT);
                Logger.d(TAG, "onActivityResult(): response:" + result);

                if (result != null) {
                    MidtransUi.getInstance().clearTransaction();
                    presenter.sendPaymentResult(result);
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getSerializableExtra(Constants.PAYMENT_RESULT) != null) {
                    PaymentResult result = (PaymentResult) data.getSerializableExtra(Constants.PAYMENT_RESULT);
                    Logger.d(TAG, "onActivityResult():response:" + result);

                    if (result != null) {
                        presenter.sendPaymentResult(result);
                        finish();
                    }
                }
            }
        } else {
            Logger.d(TAG, "failed to send result back " + requestCode);
        }
    }

    private void initRetryAndCancel() {
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerError.setVisibility(View.GONE);
                showProgressContainer(true);
                initTransaction();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendPaymentResult(new PaymentResult(true));
                finish();
            }
        });
    }
}
