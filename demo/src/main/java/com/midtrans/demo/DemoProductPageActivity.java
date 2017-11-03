package com.midtrans.demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.midtrans.demo.indicator.CirclePageIndicator;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by rakawm on 3/15/17.
 */

public class DemoProductPageActivity extends AppCompatActivity {

    private TextView productPrice;
    private FancyButton buyBtn;
    private ViewPager viewPager;
    private CirclePageIndicator indicator;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_shopper_page);
        bindViews();
        bindThemes();
        initViewPager();
        initBuyButton();
    }

    private void bindViews() {
        productPrice = (TextView) findViewById(R.id.product_price);
        buyBtn = (FancyButton) findViewById(R.id.button_primary);
        viewPager = (ViewPager) findViewById(R.id.image_view_pager);
        indicator = (CirclePageIndicator) findViewById(R.id.image_indicator);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void bindThemes() {
        String theme = DemoPreferenceHelper.getStringPreference(this, DemoConfigActivity.COLOR_THEME);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back);
        if (theme != null && !TextUtils.isEmpty(theme)) {
            switch (theme) {
                case DemoThemeConstants.BLUE_THEME:
                    productPrice.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_SECONDARY_HEX));
                    buyBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    indicator.setFillColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    indicator.setStrokeColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.RED_THEME:
                    productPrice.setTextColor(Color.parseColor(DemoThemeConstants.RED_SECONDARY_HEX));
                    buyBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX));
                    indicator.setFillColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX));
                    indicator.setStrokeColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    indicator.setFillColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    indicator.setStrokeColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    productPrice.setTextColor(Color.parseColor(DemoThemeConstants.GREEN_SECONDARY_HEX));
                    buyBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    indicator.setFillColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    indicator.setStrokeColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    productPrice.setTextColor(Color.parseColor(DemoThemeConstants.ORANGE_SECONDARY_HEX));
                    buyBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    indicator.setFillColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    indicator.setStrokeColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    productPrice.setTextColor(Color.parseColor(DemoThemeConstants.BLACK_SECONDARY_HEX));
                    buyBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                default:
                    indicator.setFillColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    indicator.setStrokeColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    productPrice.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_SECONDARY_HEX));
                    buyBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
            }
        } else {
            productPrice.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_SECONDARY_HEX));
            buyBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
            indicator.setFillColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
            indicator.setStrokeColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
            drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
        }

        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }

    private void initViewPager() {
        viewPager.setAdapter(new DemoImageAdapter(getSupportFragmentManager()));
        indicator.setViewPager(viewPager);
        indicator.setSnap(true);
    }

    private void initBuyButton() {
        buyBtn.setText(getString(R.string.btn_buy));
        buyBtn.setTextBold();
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DemoProductPageActivity.this, DemoOrderReviewActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }
}
