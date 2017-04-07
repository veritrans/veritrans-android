package com.midtrans.sdk.ui.views.ebanking.klikbca;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by rakawm on 4/7/17.
 */

public class KlikBcaInstructionActivity extends BaseActivity {

    private MidtransUi midtransUi;

    private TextView pageTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klikbca_instruction);
        initMidtransUi();
        initViews();
        initThemes();
        initToolbar();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        pageTitle = (TextView) findViewById(R.id.page_title);
    }

    private void initThemes() {
        initThemeColor();
    }

    private void initToolbar() {
        // Set title
        pageTitle.setText(R.string.payment_instruction);

        // Set merchant logo
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        Picasso.with(this)
                .load(midtransUi.getTransaction().merchant.preference.logoUrl)
                .into(merchantLogo);

        initToolbarBackButton();
        // Set back button click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
