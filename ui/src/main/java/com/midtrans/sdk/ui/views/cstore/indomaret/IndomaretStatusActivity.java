package com.midtrans.sdk.ui.views.cstore.indomaret;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class IndomaretStatusActivity extends BaseActivity {

    public static final String EXTRA_PAYMENT_CODE = "indomaret.code";
    public static final String EXTRA_VALIDITY = "indomaret.validity";

    private static final String LABEL_PAYMENT_CODE = "Payment Code";

    private MidtransUi midtransUi;

    private RecyclerView itemDetails;
    private TextView titleText;
    private FancyButton finishButton;

    private TextView paymentCodeText;
    private FancyButton copyPaymentCodeButton;
    private TextView validityText;

    private String paymentCode;
    private String validity;

    private IndomaretStatusPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indomaret_status);
        initMidtransUi();
        initPresenter();
        initExtras();
        initViews();
        initThemes();
        initToolbar();
        initItemDetails();
        initValues();
        initFinishButton();
        initCopyPaymentCodeButton();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initPresenter() {
        presenter = new IndomaretStatusPresenter();
    }

    private void initExtras() {
        paymentCode = getIntent().getStringExtra(EXTRA_PAYMENT_CODE);
        validity = getIntent().getStringExtra(EXTRA_VALIDITY);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        finishButton = (FancyButton) findViewById(R.id.btn_finish);
        titleText = (TextView) findViewById(R.id.page_title);
        paymentCodeText = (TextView) findViewById(R.id.indomaret_payment_code);
        copyPaymentCodeButton = (FancyButton) findViewById(R.id.copy_payment_code_button);
        validityText = (TextView) findViewById(R.id.text_validity);
    }

    private void initThemes() {
        // Set color theme for item details bar
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        setBackgroundColor(finishButton, Theme.PRIMARY_COLOR);

        setBackgroundColor(paymentCodeText, Theme.SECONDARY_COLOR);
        paymentCodeText.getBackground().setAlpha(50);

        setBorderColor(copyPaymentCodeButton);
        setTextColor(copyPaymentCodeButton);

        // Set font into pay now button
        if (!TextUtils.isEmpty(midtransUi.getSemiBoldFontPath())) {
            finishButton.setCustomTextFont(midtransUi.getSemiBoldFontPath());
        }

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

    private void initValues() {
        paymentCodeText.setText(paymentCode);
        validityText.setText(validity);
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
        finishIndomaretPayment();
    }

    private void setHeaderTitle(String title) {
        titleText.setText(title);
    }

    private void initCopyPaymentCodeButton() {
        copyPaymentCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyPaymentCode();
            }
        });
    }

    /**
     * Copy payment code into clipboard.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyPaymentCode() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_PAYMENT_CODE, paymentCodeText.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    private void finishIndomaretPayment() {
        setResult(RESULT_OK);
        finish();
    }
}
