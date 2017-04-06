package com.midtrans.sdk.ui.views.cstore.indomaret;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
 * Created by rakawm on 4/6/17.
 */

public class IndomaretActivity extends BaseActivity {
    private MidtransUi midtransUi;

    private RecyclerView itemDetails;
    private TextView titleText;
    private FancyButton payNowButton;

    private IndomaretPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indomaret);
        initMidtransUi();
        initPresenter();
        initViews();
        initThemes();
        initToolbar();
        initItemDetails();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initPresenter() {
        presenter = new IndomaretPresenter();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        payNowButton = (FancyButton) findViewById(R.id.btn_pay_now);
        titleText = (TextView) findViewById(R.id.page_title);
    }

    private void initThemes() {
        // Set color theme for item details bar
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        setBackgroundColor(payNowButton, Theme.PRIMARY_COLOR);

        initThemeColor();
    }

    private void initToolbar() {
        // Set title
        setHeaderTitle(getString(R.string.indomaret));

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

    private void setHeaderTitle(String title) {
        titleText.setText(title);
    }
}
