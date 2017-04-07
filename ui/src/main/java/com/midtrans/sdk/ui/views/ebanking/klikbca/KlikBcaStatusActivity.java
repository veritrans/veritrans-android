package com.midtrans.sdk.ui.views.ebanking.klikbca;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Picasso;

/**
 * Created by rakawm on 4/7/17.
 */

public class KlikBcaStatusActivity extends BaseActivity {
    public static final String EXTRA_VALIDITY = "klikbca.validity";

    private MidtransUi midtransUi;

    private RecyclerView itemDetails;
    private TextView titleText;
    private FancyButton finishButton;
    private TextView expireText;

    private FancyButton seeInstructionsButton;

    private String validity;

    private KlikBcaStatusPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klikbca_status);
        initMidtransUi();
        initPresenter();
        initExtras();
        initViews();
        initThemes();
        initToolbar();
        initItemDetails();
        initValues();
        initFinishButton();
        initSeeInstructionsButton();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initPresenter() {
        presenter = new KlikBcaStatusPresenter();
    }

    private void initExtras() {
        validity = getIntent().getStringExtra(EXTRA_VALIDITY);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        finishButton = (FancyButton) findViewById(R.id.btn_finish);
        titleText = (TextView) findViewById(R.id.page_title);
        seeInstructionsButton = (FancyButton) findViewById(R.id.btn_see_instruction);
        expireText = (TextView) findViewById(R.id.expire_text);
    }

    private void initThemes() {
        // Set color theme for item details bar
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        setBackgroundColor(finishButton, Theme.PRIMARY_COLOR);

        setBorderColor(seeInstructionsButton);
        setTextColor(seeInstructionsButton);

        // Set font into pay now button
        if (!TextUtils.isEmpty(midtransUi.getSemiBoldFontPath())) {
            finishButton.setCustomTextFont(midtransUi.getSemiBoldFontPath());
        }

        initThemeColor();
    }

    private void initToolbar() {
        // Set title
        setHeaderTitle(getString(R.string.klik_bca));

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

    private void initItemDetails() {
        ItemDetailsAdapter itemDetailsAdapter = new ItemDetailsAdapter(new ItemDetailsAdapter.ItemDetailListener() {
            @Override
            public void onItemShown() {
                // Do nothing
            }
        }, presenter.createItemDetails(this));
        itemDetails.setLayoutManager(new LinearLayoutManager(this));
        itemDetails.setAdapter(itemDetailsAdapter);
    }

    private void initValues() {
        expireText.setText(validity);
    }

    private void initFinishButton() {
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishKlikBcaPayment();
    }


    private void setHeaderTitle(String title) {
        titleText.setText(title);
    }

    private void initSeeInstructionsButton() {
        seeInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startKlikBcaInstructions();
            }
        });
    }

    private void finishKlikBcaPayment() {
        setResult(RESULT_OK);
        finish();
    }

    private void startKlikBcaInstructions() {
        Intent intent = new Intent(this, KlikBcaInstructionActivity.class);
        startActivity(intent);
    }
}
