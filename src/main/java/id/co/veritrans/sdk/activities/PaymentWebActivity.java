package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.WebviewFragment;
import id.co.veritrans.sdk.widgets.VeritransDialog;

public class PaymentWebActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String currentFragmentName;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private VeritransSDK veritransSDK;
    private RelativeLayout webviewContainer;
    private String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web);
        webUrl = getIntent().getStringExtra(Constants.WEBURL);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragmentManager = getSupportFragmentManager();
        toolbar.setTitle(R.string.processing_payment);

        setSupportActionBar(toolbar);
        WebviewFragment webviewFragment = WebviewFragment.newInstance(webUrl);
        replaceFragment(webviewFragment, true, false);
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, boolean clearBackStack) {
        if (fragment != null) {
            boolean fragmentPopped = false;
            String backStateName = fragment.getClass().getName();

            if (clearBackStack) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
            }

            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.webview_container, fragment, backStateName);
                if (addToBackStack) ft.addToBackStack(backStateName);
                ft.commit();
                currentFragmentName = backStateName;
                currentFragment = fragment;
            }
        }
    }

    @Override
    public void onBackPressed() {
        VeritransDialog veritransDialog = new VeritransDialog(this,getString(R.string.cancel_transaction),
                getString(R.string.cancel_transaction_message),getString(android.R.string.yes),getString(android.R.string.no));
        veritransDialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.getBackStackEntryCount() == 1) {
                    finish();
                } else {
                    if (currentFragmentName.equalsIgnoreCase(WebviewFragment.class.getName())) {
                        if (((WebviewFragment) currentFragment).webView.canGoBack()) {
                            ((WebviewFragment) currentFragment).webviewBackPressed();
                        } else {
                            PaymentWebActivity.super.onBackPressed();
                        }
                    } else {
                        PaymentWebActivity.super.onBackPressed();
                    }
                }
            }
        });
        veritransDialog.show();

    }

}
