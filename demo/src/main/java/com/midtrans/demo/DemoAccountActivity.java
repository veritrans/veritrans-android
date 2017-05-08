package com.midtrans.demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by ziahaqi on 5/3/17.
 */

public class DemoAccountActivity extends AppCompatActivity {
    private RelativeLayout layoutSavedCards;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_account);
        bindViews();
        initToolbar();
        initLayout();
        initThemes();
    }

    private void initThemes() {
        String theme = DemoPreferenceHelper.getStringPreference(this, DemoConfigActivity.COLOR_THEME);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back);
        if (theme != null && !TextUtils.isEmpty(theme)) {
            switch (theme) {
                case DemoThemeConstants.BLUE_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.RED_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                default:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
            }
        } else {
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
    }

    private void initLayout() {
        layoutSavedCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSavedCardActivity();
            }
        });
    }

    private void startSavedCardActivity() {
        Intent intent = new Intent(this, DemoSavedCardsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void bindViews() {
        layoutSavedCards = (RelativeLayout) findViewById(R.id.layout_saved_cards);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }
}
