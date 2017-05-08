package com.midtrans.demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.midtrans.demo.widgets.DemoTextView;

/**
 * Created by ziahaqi on 5/2/17.
 */

public class DemoProductListActivity extends AppCompatActivity {
    private CardView layoutProduct1;
    private CardView layoutProduct2;
    private DemoTextView labelPriceProduct1;
    private DemoTextView labelPriceProduct2;
    private ImageView imageSettingAccount;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_product_list);
        bindViews();
        initThemes();
        initToolbar();
        initLayout();
    }

    private void initThemes() {
        String theme = DemoPreferenceHelper.getStringPreference(this, DemoConfigActivity.COLOR_THEME);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back);
        if (theme != null && !TextUtils.isEmpty(theme)) {
            switch (theme) {
                case DemoThemeConstants.BLUE_THEME:
                    labelPriceProduct1.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_SECONDARY_HEX));
                    labelPriceProduct2.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_SECONDARY_HEX));
                    imageSettingAccount.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.RED_THEME:
                    labelPriceProduct1.setTextColor(Color.parseColor(DemoThemeConstants.RED_SECONDARY_HEX));
                    labelPriceProduct2.setTextColor(Color.parseColor(DemoThemeConstants.RED_SECONDARY_HEX));
                    imageSettingAccount.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    labelPriceProduct1.setTextColor(Color.parseColor(DemoThemeConstants.GREEN_SECONDARY_HEX));
                    labelPriceProduct2.setTextColor(Color.parseColor(DemoThemeConstants.GREEN_SECONDARY_HEX));
                    imageSettingAccount.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    labelPriceProduct1.setTextColor(Color.parseColor(DemoThemeConstants.ORANGE_SECONDARY_HEX));
                    labelPriceProduct2.setTextColor(Color.parseColor(DemoThemeConstants.ORANGE_SECONDARY_HEX));
                    imageSettingAccount.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    labelPriceProduct1.setTextColor(Color.parseColor(DemoThemeConstants.BLACK_SECONDARY_HEX));
                    labelPriceProduct2.setTextColor(Color.parseColor(DemoThemeConstants.BLACK_SECONDARY_HEX));
                    imageSettingAccount.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);

                    break;
                default:
                    labelPriceProduct1.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_SECONDARY_HEX));
                    labelPriceProduct2.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_SECONDARY_HEX));
                    imageSettingAccount.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
            }
        } else {
            labelPriceProduct1.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_SECONDARY_HEX));
            labelPriceProduct2.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_SECONDARY_HEX));
            imageSettingAccount.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
            drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
        }

        toolbar.setNavigationIcon(drawable);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageSettingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemoAccountActivity();
            }
        });
    }

    private void startDemoAccountActivity() {
        startActivity(new Intent(DemoProductListActivity.this, DemoAccountActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void initLayout() {

        layoutProduct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductPageActivity();
            }
        });
        layoutProduct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductPageActivity();
            }
        });
    }

    private void showProductPageActivity() {
        startActivity(new Intent(this, DemoProductPageActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        layoutProduct1 = (CardView) findViewById(R.id.layout_product_1);
        layoutProduct2 = (CardView) findViewById(R.id.layout_product_2);
        labelPriceProduct1 = (DemoTextView) findViewById(R.id.text_product_price_1);
        labelPriceProduct2 = (DemoTextView) findViewById(R.id.text_product_price_2);
        imageSettingAccount = (ImageView) findViewById(R.id.image_setting_account);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }
}
