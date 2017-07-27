package com.midtrans.sdk.uikit.abstracts;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.snap.MerchantData;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Created by ziahaqi on 7/20/17.
 */

public class BasePaymentActivity extends BaseActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initMerchantLogo();
        initToolbarBackButton();
        initItemDetails();
    }


    private void initItemDetails() {
        View itemDetailContainer = findViewById(R.id.container_item_details);
        setPrimaryBackgroundColor(itemDetailContainer);
        setTotalAmount();
    }

    private void setTotalAmount() {
        Transaction transaction = MidtransSDK.getInstance().getTransaction();
        if (transaction != null && transaction.getTransactionDetails() != null) {
            DefaultTextView textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
            String totalAmount = getString(R.string.prefix_money, Utils.getFormattedAmount(transaction.getTransactionDetails().getAmount()));
            textTotalAmount.setText(totalAmount);
        }
    }

    protected void initMerchantLogo() {
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        DefaultTextView merchantNameText = (DefaultTextView) findViewById(R.id.text_page_merchant_name);

        MerchantData merchantData = MidtransSDK.getInstance().getMerchantData();

        if (merchantData != null) {
            String merchantName = merchantData.getPreference().getDisplayName();
            String merchantLogoUrl = merchantData.getPreference().getLogoUrl();
            if (!TextUtils.isEmpty(merchantLogoUrl)) {
                if (merchantLogo != null) {
                    Glide.with(this)
                            .load(merchantLogoUrl)
                            .into(merchantLogo);
                }
            } else {
                if (merchantName != null) {
                    if (!TextUtils.isEmpty(merchantName)) {
                        merchantLogo.setVisibility(View.GONE);
                        merchantNameText.setVisibility(View.VISIBLE);
                        merchantNameText.setText(merchantName);
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
        }
    }

}
