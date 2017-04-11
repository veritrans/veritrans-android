package com.midtrans.sdk.ui.views.ewallet.xltunai;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Picasso;

/**
 * Created by ziahaqi on 4/10/17.
 */

public class XlTunaiStatusActivity extends BaseActivity {
    public static final String EXTRA_MERCHANT_CODE = "merchant.code";
    public static final String EXTRA_ORDER_ID = "order.id";
    public static final String EXTRA_EXPIRATION = "expiration";
    private static final String LABEL_ORDER_ID = "Order ID";
    private static final String LABEL_MERCHANT_CODE = "Merchant Code";

    private DefaultTextView textOrderId;
    private DefaultTextView textMerchantCode;
    private DefaultTextView textValidUntil;
    private FancyButton buttonCopyOrderId;
    private FancyButton buttonCopyMerchantCode;
    private DefaultTextView textTitle;
    private RecyclerView recyclerItemDetails;
    private FancyButton buttonFinish;

    private BasePaymentPresenter presenter;
    private ItemDetailsAdapter itemDetailsAdapter;

    private String orderId;
    private String expirationTime;
    private String merchantCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xltunai_status);
        initPresenter();
        initViews();
        initItemDetails();
        initProperties();
        initToolbar();
        initThemes();
        initCopyButton();
        initFinishButton();
        bindValues();
    }

    private void initToolbar() {
        // Set title
        textTitle.setText(getString(R.string.xl_tunai));

        // Set merchant logo
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        Picasso.with(this)
                .load(presenter.getMerchantLogo())
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
        itemDetailsAdapter = new ItemDetailsAdapter(new ItemDetailsAdapter.ItemDetailListener() {
            @Override
            public void onItemShown() {
                // do nothing
            }
        }, presenter.createItemDetails(this));
    }

    private void initPresenter() {
        presenter = new BasePaymentPresenter();
    }

    private void initThemes() {
        initThemeColor();
        setBackgroundColor(buttonFinish, Theme.PRIMARY_COLOR);
        setBackgroundColor(recyclerItemDetails, Theme.PRIMARY_COLOR);
    }

    private void initProperties() {
        orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        merchantCode = getIntent().getStringExtra(EXTRA_MERCHANT_CODE);
        expirationTime = getIntent().getStringExtra(EXTRA_EXPIRATION);
    }


    private void bindValues() {
        //set merchant data
        textMerchantCode.setText(merchantCode);
        textOrderId.setText(orderId);
        textValidUntil.setText(expirationTime);

        // set item details
        recyclerItemDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerItemDetails.setAdapter(itemDetailsAdapter);

    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        textTitle = (DefaultTextView) findViewById(R.id.page_title);
        textOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        textMerchantCode = (DefaultTextView) findViewById(R.id.text_merchant_code);
        textValidUntil = (DefaultTextView) findViewById(R.id.text_validaty);
        buttonCopyOrderId = (FancyButton) findViewById(R.id.btn_copy_order_id);
        buttonCopyMerchantCode = (FancyButton) findViewById(R.id.btn_copy_merchant_code);
        buttonFinish = (FancyButton) findViewById(R.id.btn_finish);
        recyclerItemDetails = (RecyclerView) findViewById(R.id.container_item_details);
    }

    @Override
    public void onBackPressed() {
        finishXlTunaiPayment();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyOrderId() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_ORDER_ID, textOrderId.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(this, R.string.copied_order_id, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyMerchantCode() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_MERCHANT_CODE, textMerchantCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(this, R.string.copied_merchant_code, Toast.LENGTH_SHORT).show();
    }

    private void finishXlTunaiPayment() {
        setResult(RESULT_OK);
        finish();
    }

    private void initCopyButton() {
        buttonCopyOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyOrderId();
            }
        });

        buttonCopyMerchantCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyMerchantCode();
            }
        });
    }

    private void initFinishButton() {
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
