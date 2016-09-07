package com.midtrans.sdk.uiflow.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import com.midtrans.sdk.coreflow.core.Logger;
import com.midtrans.sdk.coreflow.core.VeritransSDK;
import com.midtrans.sdk.uiflow.R;

/**
 * @author rakawm
 */
public class BaseActivity extends AppCompatActivity {
    protected String currentFragmentName;
    protected Fragment currentFragment = null;
    protected  boolean saveCurrentFragment = false;

    public void initializeTheme() {
        VeritransSDK mVeritransSDK = VeritransSDK.getInstance();
        if (mVeritransSDK != null) {
            ImageView logo = (ImageView) findViewById(R.id.merchant_logo);
            if (logo != null) {
                if (mVeritransSDK.getMerchantLogo() != null) {
                    Picasso.with(this)
                            .load(mVeritransSDK.getMerchantLogo())
                            .into(logo);
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
                if(saveCurrentFragment){
                    currentFragment = fragment;
                }
            }
        }
    }

    protected Fragment getCurrentFagment(Class fragmentClass){
        if(!TextUtils.isEmpty(currentFragmentName) && currentFragmentName.equals(fragmentClass.getName())){
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(currentFragmentName);
            return currentFragment;
        }
        return null;
    }
}
