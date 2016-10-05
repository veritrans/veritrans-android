package com.midtrans.sdk.uikit.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import com.squareup.picasso.Picasso;

/**
 * @author rakawm
 */
public class BaseActivity extends AppCompatActivity {
    protected String currentFragmentName;
    protected Fragment currentFragment = null;
    protected boolean saveCurrentFragment = false;

    public void initializeTheme() {
        MidtransSDK mMidtransSDK = MidtransSDK.getInstance();
        if (mMidtransSDK != null) {
            ImageView logo = (ImageView) findViewById(R.id.merchant_logo);
            TextView name = (TextView) findViewById(R.id.merchant_name);
            if (logo != null) {
                if (mMidtransSDK.getMerchantLogo() != null) {
                    name.setVisibility(View.GONE);
                    Picasso.with(this)
                            .load(mMidtransSDK.getMerchantLogo())
                            .into(logo);
                } else if (name != null && !TextUtils.isEmpty(mMidtransSDK.getMerchantName())) {
                    name.setVisibility(View.VISIBLE);
                    name.setText(mMidtransSDK.getMerchantName());
                }
            }
        }
    }

    public void replaceFragment(Fragment fragment, int fragmentContainer, boolean addToBackStack, boolean clearBackStack) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Logger.i("replace freagment");
            boolean fragmentPopped = false;
            String backStateName = fragment.getClass().getName();

            if (clearBackStack) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
            }

            if (!fragmentPopped) { //fragment not in back stack, create it.
                Logger.i("fragment not in back stack, create it");
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
}
