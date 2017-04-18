package com.midtrans.sdk.ui.abtracts;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.squareup.picasso.Picasso;

/**
 * Created by rakawm on 4/18/17.
 */

public abstract class BaseTitleActivity extends BaseActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }

    protected void init() {
        initMerchantLogo();
        initDefaultHeaderTitle();
        initToolbarBackButton();
    }

    protected void initDefaultHeaderTitle() {
        String title = getTitle().toString();
        TextView headerTitleText = (TextView) findViewById(R.id.page_title);
        if (headerTitleText != null
                && !TextUtils.isEmpty(title)) {
            headerTitleText.setText(title);
        }
    }

    protected void initToolbarBackButton() {
        // Set toolbar back icon
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back);
            backIcon.setColorFilter(getPrimaryDarkColor(), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(backIcon);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }


    protected void initMerchantLogo() {
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        TextView merchantNameText = (TextView) findViewById(R.id.merchant_name);
        String merchantLogoUrl = MidtransUi.getInstance().getTransaction().merchant.preference.logoUrl;
        String merchantName = MidtransUi.getInstance().getTransaction().merchant.preference.displayName;
        if (!TextUtils.isEmpty(merchantLogoUrl)) {
            if (merchantLogo != null) {
                Picasso.with(this)
                        .load(merchantLogoUrl)
                        .into(merchantLogo);
            }
        } else {
            if (merchantName != null) {
                if (!TextUtils.isEmpty(merchantName)) {
                    merchantNameText.setText(merchantName);
                }
            }
        }
    }

}
