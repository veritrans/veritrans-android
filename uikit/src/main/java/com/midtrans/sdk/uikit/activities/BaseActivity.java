package com.midtrans.sdk.uikit.activities;

import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.core.Currency;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.MerchantPreferences;
import com.midtrans.sdk.corekit.models.snap.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.MerchantData;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.adapters.TransactionDetailsAdapter;
import com.midtrans.sdk.uikit.widgets.BoldTextView;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.List;

/**
 * @author rakawm
 */
public class BaseActivity extends AppCompatActivity {
    public static final String ENVIRONMENT_DEVELOPMENT = "development";
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected String currentFragmentName;
    protected Fragment currentFragment = null;
    protected BoldTextView textTotalAmount;

    protected boolean saveCurrentFragment = false;
    protected boolean hasMerchantLogo;
    protected int RESULT_CODE = RESULT_CANCELED;
    protected boolean isDetailShown = false;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        try {
            initView();
            initMerchantLogo();
            initItemDetails();
        } catch (Exception e) {
            Logger.e(TAG, "appbar:" + e.getMessage());
        }
    }

    private void initView() {
        textTotalAmount = findViewById(R.id.text_amount);
    }

    public void initializeTheme() {
        initBadgeTestView();

        MidtransSDK mMidtransSDK = MidtransSDK.getInstance();
        if (mMidtransSDK != null) {
            updateColorTheme(mMidtransSDK);
        }
    }

    private void initBadgeTestView() {
        if (BuildConfig.FLAVOR.equalsIgnoreCase(ENVIRONMENT_DEVELOPMENT)) {
            ImageView badgeView = (ImageView) findViewById(R.id.image_sandbox_badge);
            if (badgeView != null) {
                badgeView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void initMerchantLogo() {
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        DefaultTextView merchantNameText = (DefaultTextView) findViewById(R.id.text_page_merchant_name);

        MerchantData merchantData = MidtransSDK.getInstance().getMerchantData();

        if (merchantData != null) {
            MerchantPreferences preferences = merchantData.getPreference();
            if (preferences != null) {
                String merchantName = preferences.getDisplayName();
                String merchantLogoUrl = preferences.getLogoUrl();
                if (!TextUtils.isEmpty(merchantLogoUrl)) {
                    if (merchantLogo != null) {
                        hasMerchantLogo = true;
                        Ion.with(merchantLogo).load(merchantLogoUrl);
                        merchantLogo.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (merchantName != null) {
                        if (merchantNameText != null && !TextUtils.isEmpty(merchantName)) {
                            hasMerchantLogo = true;
                            merchantNameText.setVisibility(View.VISIBLE);
                            merchantNameText.setText(merchantName);
                            if (merchantLogo != null) {
                                merchantLogo.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void adjustToolbarSize() {
        if (hasMerchantLogo) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
            if (toolbar != null) {
                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                params.height = params.height + (int) getResources().getDimension(R.dimen.toolbar_expansion_size);
                toolbar.setLayoutParams(params);
            }
        }
    }

    private void initItemDetails() {
        View itemDetailContainer = findViewById(R.id.container_item_details);
        if (itemDetailContainer != null) {
            initTotalAmount();
        }
    }

    private void initTotalAmount() {

        final Transaction transaction = MidtransSDK.getInstance().getTransaction();
        if (transaction.getTransactionDetails() != null) {
            String currency = transaction.getTransactionDetails().getCurrency();
            String formattedAmount = formatAmount(transaction.getTransactionDetails().getAmount(), currency);
            setTotalAmount(formattedAmount);
        }

        initTransactionDetail(MidtransSDK.getInstance().getTransaction().getItemDetails());
        //init dim
        findViewById(R.id.background_dim).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOrHideItemDetails();
            }
        });

        final LinearLayout amountContainer = (LinearLayout) findViewById(R.id.container_item_details);
        amountContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOrHideItemDetails();
            }
        });
    }

    private void initTransactionDetail(List<ItemDetails> details) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_transaction_detail);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            TransactionDetailsAdapter adapter = new TransactionDetailsAdapter(details);
            recyclerView.setAdapter(adapter);
        }
    }

    protected void displayOrHideItemDetails() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_transaction_detail);
        View dimView = findViewById(R.id.background_dim);
        if (recyclerView != null && dimView != null) {
            if (isDetailShown) {
                recyclerView.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.text_amount)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_amount_detail, 0);
                dimView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.text_amount)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                dimView.setVisibility(View.VISIBLE);
            }
            isDetailShown = !isDetailShown;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
        }
    }


    public void updateColorTheme(MidtransSDK mMidtransSDK) {
        try {
            if (mMidtransSDK.getColorTheme() != null) {
                int primaryColor = mMidtransSDK.getColorTheme().getPrimaryColor();
                int primaryDarkColor = mMidtransSDK.getColorTheme().getPrimaryDarkColor();
                if (primaryColor != 0) {
                    // Set primary button color
                    FancyButton primaryButton = (FancyButton) findViewById(R.id.button_primary);
                    if (primaryButton != null) {
                        primaryButton.setBackgroundColor(primaryColor);
                    }

                }

                if (primaryDarkColor != 0) {
                    // Set amount text color
                    if (textTotalAmount != null) {
                        textTotalAmount.setTextColor(primaryDarkColor);
                    }
                }
            }
        } catch (Exception e) {
            Logger.e("themes", "init:" + e.getMessage());
        }
    }

    public void replaceFragment(Fragment fragment, int fragmentContainer, boolean addToBackStack, boolean clearBackStack) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Logger.i("replace fragment");
            boolean fragmentPopped = false;
            String backStateName = fragment.getClass().getName();

            if (clearBackStack) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            if (!fragmentPopped) {
                Logger.i("fragment not in back stack, create it");
                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_back, R.anim.slide_out_back);
                }
                ft.replace(fragmentContainer, fragment, backStateName);
                if (addToBackStack) {
                    ft.addToBackStack(backStateName);
                }
                ft.commit();
                currentFragmentName = backStateName;
                if (saveCurrentFragment) {
                    currentFragment = fragment;
                }
            }
        }
    }

    public void setResultCode(int resultCode) {
        this.RESULT_CODE = resultCode;
    }

    protected String formatAmount(double totalAmount, String currency) {
        String formattedAmount;

        if (TextUtils.isEmpty(currency)) {
            formattedAmount = getString(R.string.prefix_money, Utils.getFormattedAmount(totalAmount));
        } else {
            switch (currency) {
                case Currency.SGD:
                    formattedAmount = getString(R.string.prefix_money_sgd, Utils.getFormattedAmount(totalAmount));
                    break;

                default:
                    formattedAmount = getString(R.string.prefix_money, Utils.getFormattedAmount(totalAmount));
                    break;
            }
        }

        return formattedAmount;
    }


    protected void setTotalAmount(String formattedAmount) {
        if (textTotalAmount != null) {
            textTotalAmount.setText(formattedAmount);
        }
    }
}
