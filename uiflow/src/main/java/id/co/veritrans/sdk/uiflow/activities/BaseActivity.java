package id.co.veritrans.sdk.uiflow.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.uiflow.R;

/**
 * @author rakawm
 */
public class BaseActivity extends AppCompatActivity {
    protected String currentFragmentName;
    protected Fragment currentFragment = null;
    protected  boolean saveCurrentFragment = false;

    public void initializeTheme() {
        VeritransSDK mVeritransSDK = VeritransSDK.getVeritransSDK();
        if (mVeritransSDK != null) {
            ImageView logo = (ImageView) findViewById(R.id.merchant_logo);
            if (logo != null) {
                if (mVeritransSDK.getMerchantLogoDrawable() != null) {
                    logo.setImageDrawable(mVeritransSDK.getMerchantLogoDrawable());
                } else {
                    logo.setVisibility(View.GONE);
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
            Logger.d("Fragment Name: " + backStateName);

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
}
