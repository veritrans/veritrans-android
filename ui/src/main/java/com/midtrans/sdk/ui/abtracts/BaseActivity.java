package com.midtrans.sdk.ui.abtracts;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.ui.CustomSetting;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.themes.BaseColorTheme;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.views.PaymentStatusFragment;
import com.midtrans.sdk.ui.widgets.FancyButton;

import java.lang.reflect.Field;

/**
 * Created by ziahaqi on 2/19/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String DEFAULT_TEXT_COLOR = "mDefaultTextColor";
    private static final String FOCUSED_TEXT_COLOR = "mFocusedTextColor";
    protected String currentFragmentName;
    protected Fragment currentFragment = null;
    protected boolean saveCurrentFragment = false;
    protected Toolbar toolbar;
    protected String TAG = getClass().getSimpleName();
    protected RelativeLayout layoutTotalAmount;
    private int resultCode = RESULT_CANCELED;
    private int primaryColor = 0;
    private int secondaryColor = 0;
    private int primaryDarkColor = 0;

    private String fontDefault;
    private String fontBold;
    private String fontSemiBold;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
    }

    public void replaceFragment(Fragment fragment, int fragmentContainer, boolean addToBackStack, boolean clearBackStack) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Logger.d(TAG, "replace freagment");
            boolean fragmentPopped = false;
            String backStateName = fragment.getClass().getName();

            if (clearBackStack) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
            }

            if (!fragmentPopped) { //fragment not in back stack, create it.
                Logger.d(TAG, "fragment not in back stack, create it");
                FragmentTransaction ft = fragmentManager.beginTransaction();
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

    protected Fragment getCurrentFagment(Class fragmentClass) {
        if (!TextUtils.isEmpty(currentFragmentName) && currentFragmentName.equals(fragmentClass.getName())) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(currentFragmentName);
            return currentFragment;
        }
        return null;
    }


    public void initPaymentResult(PaymentResult result, String paymentType) {
        CustomSetting settings = MidtransUi.getInstance().getCustomSetting();
        Log.d(TAG, "show:" + settings.isShowPaymentStatus());
        if (settings.isShowPaymentStatus()) {
            PaymentStatusFragment fragment = PaymentStatusFragment.newInstance(result, paymentType);
            replaceFragment(fragment, R.id.main_layout, true, false);
        } else {
            setResultCode(RESULT_OK);
            completePayment(result);
        }
    }

    public void completePayment(PaymentResult result) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, result);
        setResult(resultCode, data);
        finish();
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void initTheme() {
        MidtransUi midtransUi = MidtransUi.getInstance();
        if (midtransUi != null) {
            BaseColorTheme baseColorTheme = MidtransUi.getInstance().getColorTheme();
            if (baseColorTheme != null) {
                this.primaryColor = baseColorTheme.getPrimaryColor();
                this.primaryDarkColor = baseColorTheme.getPrimaryDarkColor();
                this.secondaryColor = baseColorTheme.getSecondaryColor();
                Logger.d(TAG, "prim:" + primaryColor);
                Logger.d(TAG, "secon:" + secondaryColor);
                Logger.d(TAG, "dar:" + primaryDarkColor);

            }

            this.fontDefault = midtransUi.getDefaultFontPath();
            this.fontSemiBold = midtransUi.getSemiBoldFontPath();
            this.fontBold = midtransUi.getBoldFontPath();

        }
        setStatusBarColor();
    }

    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && primaryDarkColor != 0) {
            getWindow().setStatusBarColor(primaryDarkColor);
        }
    }

    public void filterBackgroundColor(View view) {
        if (secondaryColor != 0) {
            try {
                view.getBackground().setColorFilter(secondaryColor, PorterDuff.Mode.SRC_ATOP);
            } catch (RuntimeException exception) {
                Logger.e(TAG, exception.getMessage());
            }
        }
    }

    public void filterEditTextColor(EditText editText) {
        if (secondaryColor != 0) {
            editText.getBackground().setColorFilter(secondaryColor, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void setBackgroundColor(View view, String colorType) {
        try {
            if (colorType.equals(Theme.PRIMARY_COLOR) && primaryColor != 0) {
                view.setBackgroundColor(primaryColor);

            } else if (colorType.equals(Theme.SECONDARY_COLOR) && secondaryColor != 0) {
                view.setBackgroundColor(secondaryColor);

            } else if (colorType.equals(Theme.PRIMARY_DARK_COLOR) && primaryDarkColor != 0) {
                view.setBackgroundColor(primaryDarkColor);
            }
        } catch (RuntimeException exception) {
            Logger.e(TAG, exception.getMessage());
        }
    }

    public void filterColor(View view) {
        if (secondaryColor != 0) {
            try {
                if (view instanceof ImageView)
                    ((ImageView) view).setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_ATOP);
            } catch (RuntimeException exception) {
                Logger.e(TAG, exception.getMessage());
            }
        }
    }

    public void setIconColorFilter(FancyButton fancyButton) {
        if (primaryDarkColor != 0) {
            fancyButton.setIconColorFilter(primaryDarkColor);
        }
    }

    public void setHintColor(View view) {
        if (secondaryColor != 0) {
            try {
                if (view instanceof TextView) {
                    ((TextView) view).setHintTextColor(secondaryColor);
                } else if (view instanceof Button) {
                    ((Button) view).setHintTextColor(secondaryColor);

                } else if (view instanceof EditText) {
                    ((EditText) view).setHintTextColor(secondaryColor);
                }
            } catch (RuntimeException exception) {
                Logger.e(TAG, exception.getMessage());
            }
        }
    }

    public void setTextColor(View view) {
        if (primaryDarkColor != 0) {
            try {
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(primaryDarkColor);
                } else if (view instanceof FancyButton) {
                    ((FancyButton) view).setTextColor(primaryDarkColor);
                }
            } catch (RuntimeException exception) {
                Logger.e(TAG, exception.getMessage());
            }
        }
    }

    public void setBorderColor(FancyButton fancyButton) {
        if (primaryDarkColor != 0) {
            try {
                fancyButton.setBorderColor(primaryDarkColor);
            } catch (RuntimeException exception) {
                Logger.e(TAG, exception.getMessage());
            }
        }
    }

    public void setCheckoxStateColor(AppCompatCheckBox checkBox) {
        if (secondaryColor != 0) {
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked},
            };

            int[] trackColors = new int[]{
                    Color.GRAY,
                    secondaryColor
            };
            checkBox.setSupportButtonTintList(new ColorStateList(states, trackColors));
        }
    }

    public void setTextInputLayoutColorFilter(TextInputLayout textInputLayout) {
        try {
            Field fDefaultTextColor = TextInputLayout.class.getDeclaredField(DEFAULT_TEXT_COLOR);
            fDefaultTextColor.setAccessible(true);
            fDefaultTextColor.set(textInputLayout, new ColorStateList(new int[][]{{0}}, new int[]{getSecondaryColor()}));

            Field fFocusedTextColor = TextInputLayout.class.getDeclaredField(FOCUSED_TEXT_COLOR);
            fFocusedTextColor.setAccessible(true);
            fFocusedTextColor.set(textInputLayout, new ColorStateList(new int[][]{{0}}, new int[]{getSecondaryColor()}));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEditTextCompatBackgroundTintColor(AppCompatEditText editText) {
        try {
            editText.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{getSecondaryColor()}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initToolbarBackButton() {
        // Set toolbar back icon
        if (primaryDarkColor != 0) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
            if (toolbar != null) {
                Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back);
                backIcon.setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_ATOP);
                toolbar.setNavigationIcon(backIcon);
            }
        }
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    protected void initThemeColor() {

        // Set amount panel background

        if (layoutTotalAmount != null && primaryColor != 0) {
            layoutTotalAmount.setBackgroundColor(primaryColor);
        }

        if (primaryColor != 0) {

            // Set button pay now color
            FancyButton payNowButton = (FancyButton) findViewById(R.id.btn_pay_now);

            if (payNowButton != null) {
                payNowButton.setBackgroundColor(primaryColor);
            }

            // Set amount panel background
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout_total_amount);
            if (relativeLayout != null) {
                relativeLayout.setBackgroundColor(primaryColor);
            }

            // Set indicator color
            View indicator = findViewById(R.id.title_underscore);
            if (indicator != null) {
                indicator.setBackgroundColor(primaryColor);
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (MidtransUi.getInstance().getCustomSetting().isAnimationEnabled()) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (MidtransUi.getInstance().getCustomSetting().isAnimationEnabled()) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (MidtransUi.getInstance().getCustomSetting().isAnimationEnabled()) {
            overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
        }
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        Drawable backButton = ContextCompat.getDrawable(this, R.drawable.ic_back);
        backButton.setColorFilter(getPrimaryDarkColor(), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(backButton);
        super.setSupportActionBar(toolbar);
    }
}