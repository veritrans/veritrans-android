package com.midtrans.sdk.uikit.base.composer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.MidtransKitConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.model.MessageInfo;
import com.midtrans.sdk.uikit.base.theme.BaseColorTheme;
import com.midtrans.sdk.uikit.utilities.ActivityHelper;
import com.midtrans.sdk.uikit.utilities.CurrencyHelper;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.view.adapter.InstructionDetailAdapter;
import com.midtrans.sdk.uikit.widget.BoldTextView;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;
import com.midtrans.sdk.uikit.widget.SemiBoldTextView;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BaseActivity extends AppCompatActivity {

    public final static String USE_DEEP_LINK = "First Page";

    protected BoldTextView textTotalAmount;
    protected DefaultTextView textOrderId;

    private int primaryColor = 0;
    private int primaryDarkColor = 0;
    private int secondaryColor = 0;

    protected LinearLayout containerProgress;
    protected DefaultTextView textProgressMessage;
    protected ImageView imageProgressLogo;
    protected Toolbar toolbar;
    protected TextView merchantNameInToolbar;
    protected SemiBoldTextView paymentMethodTitleInToolbar;
    protected ImageView merchantLogoInToolbar;

    protected boolean isDetailShown = false;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        try {
            initView();
            initThemeProperties();
        } catch (Exception e) {
            Logger.error("appbar:" + e.getMessage());
        }
    }

    private void initThemeProperties() {
        BaseColorTheme baseColorTheme = MidtransKit.getInstance().getMidtransKitConfig().getColorTheme();
        if (baseColorTheme != null) {
            this.primaryColor = baseColorTheme.getPrimaryColor();
            this.primaryDarkColor = baseColorTheme.getPrimaryDarkColor();
            this.secondaryColor = baseColorTheme.getSecondaryColor();
        }
    }

    private void initView() {
        textTotalAmount = findViewById(R.id.text_view_amount);
        textOrderId = findViewById(R.id.text_view_order_id);

        toolbar = findViewById(R.id.toolbar_base);
        textProgressMessage = findViewById(R.id.progress_bar_message);
        containerProgress = findViewById(R.id.progress_container);
        imageProgressLogo = findViewById(R.id.progress_bar_image);
        merchantLogoInToolbar = findViewById(R.id.image_view_merchant_logo);
        merchantNameInToolbar = findViewById(R.id.text_view_merchant_name);
        paymentMethodTitleInToolbar = findViewById(R.id.text_view_page_title);

        if (imageProgressLogo != null) {
            Ion.with(imageProgressLogo).load(ActivityHelper.getImagePath(this) + R.drawable.midtrans_loader);
        }

        if (toolbar != null) {
            Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back);
            if (backIcon != null) {
                backIcon.setColorFilter(getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);
            }
            toolbar.setNavigationIcon(backIcon);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    protected void initializeTheme() {
        initBadgeTestView();

        MidtransKit midtransKit = MidtransKit.getInstance();
        if (midtransKit != null) {
            updateColorTheme(midtransKit);
        }
    }

    private void updateColorTheme(MidtransKit midtransKit) {
        try {
            MidtransKitConfig midtransKitConfig = midtransKit.getInstance().getMidtransKitConfig();
            if (midtransKitConfig.getColorTheme() != null) {
                int primaryColor = midtransKitConfig.getColorTheme().getPrimaryColor();
                int primaryDarkColor = midtransKitConfig.getColorTheme().getPrimaryDarkColor();
                if (primaryColor != 0) {
                    // Set primary button color
                }
                if (primaryDarkColor != 0) {
                    // Set amount text color
                    if (textTotalAmount != null) {
                        textTotalAmount.setTextColor(primaryDarkColor);
                    }
                }
            }
        } catch (Exception e) {
            Logger.error("themes", "init:" + e.getMessage());
        }
    }

    private void initBadgeTestView() {
        if (MidtransKit.getInstance().getEnvironment() == Environment.SANDBOX) {
            ImageView badgeView = findViewById(R.id.image_sandbox_badge);
            if (badgeView != null) {
                badgeView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void pendingSlideIn() {
        MidtransKitConfig midtransKitConfig = MidtransKit.getInstance().getMidtransKitConfig();
        if (midtransKitConfig != null && midtransKitConfig.isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    protected void setTotalAmount(String formattedAmount) {
        if (textTotalAmount != null) {
            textTotalAmount.setText(formattedAmount);
        }
    }

    protected void setOrderId(String orderId) {
        if (textOrderId != null) {
            textOrderId.setText(orderId);
        }
    }

    protected void initItemDetails(PaymentInfoResponse paymentInfo) {
        View itemDetailContainer = findViewById(R.id.container_item_details);
        if (itemDetailContainer != null) {
            if (paymentInfo.getTransactionDetails() != null) {
                // Add amount & order id
                double amount = paymentInfo.getTransactionDetails().getGrossAmount();
                String currency = paymentInfo.getTransactionDetails().getCurrency();
                String formattedAmount = CurrencyHelper.formatAmount(this, amount, currency);
                setTotalAmount(formattedAmount);
                setOrderId(paymentInfo.getTransactionDetails().getOrderId());
            }
            if (paymentInfo.getItemDetails() != null) {
                initTransactionDetail(paymentInfo.getItemDetails());
                findViewById(R.id.background_dim).setOnClickListener(v -> displayOrHideItemDetails());

                final LinearLayout amountContainer = findViewById(R.id.container_item_details);
                amountContainer.setOnClickListener(v -> displayOrHideItemDetails());
            }
        }
    }

    private void initTransactionDetail(List<Item> details) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_transaction_detail);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            InstructionDetailAdapter adapter = new InstructionDetailAdapter();
            recyclerView.setAdapter(adapter);
            adapter.setData(details);
        }
    }

    protected void displayOrHideItemDetails() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_transaction_detail);
        View dimView = findViewById(R.id.background_dim);
        if (recyclerView != null && dimView != null) {
            if (isDetailShown) {
                recyclerView.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.text_view_amount)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_amount_detail, 0);
                dimView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.text_view_amount)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                dimView.setVisibility(View.VISIBLE);
            }
            isDetailShown = !isDetailShown;
        }
    }

    public void setTextColor(View view) throws RuntimeException {
        if (primaryDarkColor != 0 && view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(primaryDarkColor);
            } else if (view instanceof FancyButton) {
                ((FancyButton) view).setTextColor(primaryDarkColor);
            }
        }
    }

    public void setSecondaryBackgroundColor(View view) throws RuntimeException {
        if (secondaryColor != 0 && view != null) {
            view.setBackgroundColor(secondaryColor);
        }
    }

    protected void setPrimaryBackgroundColor(View view) throws RuntimeException {
        if (primaryColor != 0 && view != null) {
            view.setBackgroundColor(primaryColor);
        }
    }

    public void setIconColorFilter(FancyButton fancyButton) throws RuntimeException {
        if (primaryDarkColor != 0) {
            fancyButton.setIconColorFilter(primaryDarkColor);
        }
    }

    public void setBorderColor(FancyButton fancyButton) {
        if (primaryDarkColor != 0) {
            fancyButton.setBorderColor(primaryDarkColor);
        }
    }

    public void setColorFilter(View view) throws RuntimeException {
        if (primaryDarkColor != 0 && view != null) {
            if (view instanceof ImageButton) {
                ((ImageButton) view).setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                view.getBackground().setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    protected void showProgressLayout(String message) {
        if (!TextUtils.isEmpty(message)) {
            textProgressMessage.setText(message);
        }

        if (containerProgress != null) {
            containerProgress.setVisibility(View.VISIBLE);
            containerProgress.setClickable(true);
        }
    }

    protected void showProgressLayout() {
        showProgressLayout("");
    }

    protected void hideProgressLayout() {
        new Handler().postDelayed(() -> {
            if (containerProgress != null) {
                containerProgress.setVisibility(View.GONE);
            }

            if (textProgressMessage != null) {
                textProgressMessage.setText(R.string.loading);
            }
        }, 500);
    }

    protected void showOnErrorPaymentStatusMessage(Throwable error) {
        showOnErrorPaymentStatusMessage(error, null);
    }

    protected void showOnErrorPaymentStatusMessage(Throwable error, String defaultmessage) {
        MessageInfo messageInfo = MessageHelper.createMessageOnError(error, this);
        MessageHelper.showToast(this, messageInfo.getDetailsMessage());
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        pendingSlideIn();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        pendingSlideIn();
    }

    @Override
    public void onBackPressed() {
        if (isDetailShown) {
            displayOrHideItemDetails();
        } else {
            super.onBackPressed();
            MidtransKitConfig midtransKitConfig = MidtransKit.getInstance().getMidtransKitConfig();
            if (midtransKitConfig != null && midtransKitConfig.isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
            }
        }
    }
}