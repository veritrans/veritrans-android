package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.UserAddressFragment;
import com.midtrans.sdk.uikit.fragments.UserDetailFragment;

import java.util.ArrayList;

public class UserDetailsActivity extends BaseActivity {
    public static final String CREDIT_CARD_ONLY = "cconly";
    public static final String BANK_TRANSFER_ONLY = "btonly";
    public static final String BANK_TRANSFER_MANDIRI = "bt_mandiri";
    public static final String BANK_TRANSFER_BCA = "bt_bca";
    public static final String BANK_TRANSFER_PERMATA = "bt_permata";
    public static final String BANK_TRANSFER_BNI = "bt_bni";
    public static final String BANK_TRANSFER_OTHER = "bt_other";
    public static final String BCA_KLIKPAY = "bcaklikpay";
    public static final String KLIK_BCA = "klikbca";
    public static final String MANDIRI_CLICKPAY = "mandiriclickpay";
    public static final String MANDIRI_ECASH = "mandiriecash";
    public static final String CIMB_CLICKS = "cimbclicks";
    public static final String BRI_EPAY = "briepay";
    public static final String TELKOMSEL_CASH = "tcash";
    public static final String INDOSAT_DOMPETKU = "indosatdompetku";
    public static final String XL_TUNAI = "xltunai";
    public static final String INDOMARET = "indomaret";
    public static final String KIOSON = "kioson";
    public static final String GIFT_CARD = "gci";
    private static final String TAG = "UserDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserDetails();
    }

    public void checkUserDetails() {
        try {
            UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);

            if (userDetail != null && !TextUtils.isEmpty(userDetail.getUserFullName())) {
                //TODO check user have address filled
                //if no take user to select address

                ArrayList<UserAddress> userAddresses = userDetail.getUserAddresses();
                if (userAddresses != null && !userAddresses.isEmpty()) {
                    Intent paymentOptionIntent = new Intent(this, PaymentMethodsActivity.class);
                    if (getIntent().getBooleanExtra(CREDIT_CARD_ONLY, false)) {
                        paymentOptionIntent.putExtra(CREDIT_CARD_ONLY, true);
                    } else if (getIntent().getBooleanExtra(BANK_TRANSFER_ONLY, false)) {
                        paymentOptionIntent.putExtra(BANK_TRANSFER_ONLY, true);
                        if (getIntent().getBooleanExtra(BANK_TRANSFER_PERMATA, false)) {
                            paymentOptionIntent.putExtra(BANK_TRANSFER_PERMATA, true);
                        } else if (getIntent().getBooleanExtra(BANK_TRANSFER_MANDIRI, false)) {
                            paymentOptionIntent.putExtra(BANK_TRANSFER_MANDIRI, true);
                        } else if (getIntent().getBooleanExtra(BANK_TRANSFER_BCA, false)) {
                            paymentOptionIntent.putExtra(BANK_TRANSFER_BCA, true);
                        } else if (getIntent().getBooleanExtra(BANK_TRANSFER_OTHER, false)) {
                            paymentOptionIntent.putExtra(BANK_TRANSFER_OTHER, true);
                        } else if (getIntent().getBooleanExtra(BANK_TRANSFER_BNI, false)) {
                            paymentOptionIntent.putExtra(BANK_TRANSFER_BNI, true);
                        }
                    } else if (getIntent().getBooleanExtra(BCA_KLIKPAY, false)) {
                        paymentOptionIntent.putExtra(BCA_KLIKPAY, true);
                    } else if (getIntent().getBooleanExtra(KLIK_BCA, false)) {
                        paymentOptionIntent.putExtra(KLIK_BCA, true);
                    } else if (getIntent().getBooleanExtra(MANDIRI_CLICKPAY, false)) {
                        paymentOptionIntent.putExtra(MANDIRI_CLICKPAY, true);
                    } else if (getIntent().getBooleanExtra(MANDIRI_ECASH, false)) {
                        paymentOptionIntent.putExtra(MANDIRI_ECASH, true);
                    } else if (getIntent().getBooleanExtra(CIMB_CLICKS, false)) {
                        paymentOptionIntent.putExtra(CIMB_CLICKS, true);
                    } else if (getIntent().getBooleanExtra(BRI_EPAY, false)) {
                        paymentOptionIntent.putExtra(BRI_EPAY, true);
                    } else if (getIntent().getBooleanExtra(TELKOMSEL_CASH, false)) {
                        paymentOptionIntent.putExtra(TELKOMSEL_CASH, true);
                    } else if (getIntent().getBooleanExtra(INDOSAT_DOMPETKU, false)) {
                        paymentOptionIntent.putExtra(INDOSAT_DOMPETKU, true);
                    } else if (getIntent().getBooleanExtra(XL_TUNAI, false)) {
                        paymentOptionIntent.putExtra(XL_TUNAI, true);
                    } else if (getIntent().getBooleanExtra(INDOMARET, false)) {
                        paymentOptionIntent.putExtra(INDOMARET, true);
                    } else if (getIntent().getBooleanExtra(KIOSON, false)) {
                        paymentOptionIntent.putExtra(KIOSON, true);
                    } else if (getIntent().getBooleanExtra(GIFT_CARD, false)) {
                        paymentOptionIntent.putExtra(GIFT_CARD, true);
                    }
                    startActivity(paymentOptionIntent);
                    if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                            && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    }
                    finish();
                } else {
                    setView();
                    UserAddressFragment userAddressFragment = UserAddressFragment.newInstance();
                    replaceFragment(userAddressFragment, false);
                    return;
                }
            } else {
                setView();
                UserDetailFragment userDetailFragment = UserDetailFragment.newInstance();
                replaceFragment(userDetailFragment, false);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setView();
        UserDetailFragment userDetailFragment = UserDetailFragment.newInstance();
        replaceFragment(userDetailFragment, false);
    }

    private void setView() {
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prepareToolbar(toolbar);
        initializeTheme();

    }

    private void prepareToolbar(Toolbar toolbar) {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);

        try {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();

            if (midtransSDK != null && midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                drawable.setColorFilter(
                        midtransSDK.getColorTheme().getPrimaryDarkColor(),
                        PorterDuff.Mode.SRC_ATOP);
            }
        } catch (Exception e) {
            Log.d(TAG, "render toolbar:" + e.getMessage());
        }

        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_back, R.anim.slide_out_back);
            }
            ft.replace(R.id.user_detail_container, fragment);
            ft.commit();
        }
    }

    public void replaceFragment(Fragment fragment, boolean needAnimation) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (needAnimation) {
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_back, R.anim.slide_out_back);
                }
            }
            ft.replace(R.id.user_detail_container, fragment);
            ft.commit();
        }
    }
}