package com.midtrans.sdk.uikit.views.creditcard.tnc;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 2/19/18.
 */

public class TermsAndConditionsActivity extends BaseActivity {

    public static final String EXTRA_POINT_TYPE = "point.type";
    public static final int INTENT_TNC = 909;
    private static final String TAG = TermsAndConditionsActivity.class.getSimpleName();

    private FancyButton buttonPrimary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tnc);
        initToolbar();
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
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            }
        } catch (RuntimeException e) {
            Log.e(TAG, "initToolbar():" + e.getMessage());
        }
    }


    private void initActionButton() {
        buttonPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                onBackPressed();
            }
        });
    }

    @Override
    public void bindViews() {
        buttonPrimary = findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {
        setTextColor(buttonPrimary);
    }
}
