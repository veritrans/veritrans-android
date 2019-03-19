package com.midtrans.sdk.uikit.view.method.creditcard.tnc;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toolbar;

import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BaseActivity;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class TermsAndConditionsActivity extends BaseActivity {

    public static final String EXTRA_POINT_TYPE = "point.type";
    public static final int INTENT_TNC = 909;
    private static final String TAG = TermsAndConditionsActivity.class.getSimpleName();

    private FancyButton buttonPrimary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_credit_card_tnc);
        initToolbar();
        bindViews();
        initActionButton();
    }

    private void initToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.main_toolbar);
            if (toolbar != null) {
                Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back);
                if (backIcon != null) {
                    backIcon.setColorFilter(getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);
                }
                toolbar.setNavigationIcon(backIcon);
                toolbar.setNavigationOnClickListener(view -> onBackPressed());
            }
        } catch (RuntimeException e) {
            Logger.error(TAG, "initToolbar():" + e.getMessage());
        }
    }


    private void initActionButton() {
        buttonPrimary.setOnClickListener(v -> {
            setResult(RESULT_OK);
            onBackPressed();
        });
    }

    public void bindViews() {
        buttonPrimary = findViewById(R.id.button_primary);
    }

    @Override
    protected void initializeTheme() {
        super.initializeTheme();
        setTextColor(buttonPrimary);
    }

}