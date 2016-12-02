package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.midtrans.sdk.corekit.core.LocalDataHandler;
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
    public static final String BANK_TRANSFER_OTHER = "bt_other";
    public static final String BCA_KLIKPAY = "bcaklikpay";
    public static final String KLIK_BCA = "klikbca";
    public static final String MANDIRI_CLICKPAY = "mandiriclickpay";
    public static final String MANDIRI_ECASH = "mandiriecash";
    public static final String CIMB_CLICKS = "cimbclicks";
    public static final String BRI_EPAY = "briepay";

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
                    }
                    startActivity(paymentOptionIntent);
                    finish();
                } else {
                    setView();
                    UserAddressFragment userAddressFragment = UserAddressFragment.newInstance();
                    replaceFragment(userAddressFragment);
                    return;
                }
            } else {
                setView();
                UserDetailFragment userDetailFragment = UserDetailFragment.newInstance();
                replaceFragment(userDetailFragment);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setView();
        UserDetailFragment userDetailFragment = UserDetailFragment.newInstance();
        replaceFragment(userDetailFragment);
    }

    private void setView() {
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeTheme();

    }

    public void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.user_detail_container, fragment);
            ft.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}