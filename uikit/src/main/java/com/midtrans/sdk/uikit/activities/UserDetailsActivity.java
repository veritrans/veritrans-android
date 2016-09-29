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