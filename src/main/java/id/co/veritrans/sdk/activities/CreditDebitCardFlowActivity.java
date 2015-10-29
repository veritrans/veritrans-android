package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.fragments.SavedCardFragment;
import id.co.veritrans.sdk.fragments.WebviewFragment;

public class CreditDebitCardFlowActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private float cardWidth;
    private String currentFragmentName;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_card_flow);
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
        //replaceFragment(addCardDetailsFragment);
        calculateScreenWidth();
        SavedCardFragment savedCardFragment = SavedCardFragment.newInstance();
        replaceFragment(savedCardFragment, true, false);
    }

    private void calculateScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        cardWidth = outMetrics.widthPixels;
        cardWidth = cardWidth - ((2 * getResources().getDimension(R.dimen.sixteen_dp)) / density);
    }

    public float getScreenWidth() {
        return cardWidth;
    }

    /*public void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.card_container, fragment);
            ft.a
            ft.commit();
        }
    }*/
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
                ft.replace(R.id.card_container, fragment, backStateName);
                if (addToBackStack) ft.addToBackStack(backStateName);
                ft.commit();
                currentFragmentName = backStateName;
                currentFragment = fragment;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }



    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            if(currentFragmentName.equalsIgnoreCase(WebviewFragment.class.getName())){
                if(((WebviewFragment)currentFragment).webView.canGoBack()){
                    ((WebviewFragment)currentFragment).webviewBackPressed();
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }
}



/*

    //card_number=4811111111111114
    // &card_cvv=123
    // &card_exp_month=12
    // &card_exp_year=2020
    // &client_key=VT-client-Lre_JFh5klhfGefF
    // &secure=true
    // &gross_amount=10000
    // &bank=bni
    // &two_click=false

    TokenRequestModel tokenRequestModel =
            new TokenRequestModel("4811111111111114", 123, 12, 2020, veritransSDK
                    .getClientKey());


                // to enable 3ds transaction
                tokenRequestModel.setBank("bni");
                tokenRequestModel.setGrossAmount(1000);
                tokenRequestModel.setSecure(true);
                tokenRequestModel.setTwoClick(false);


    veritransSDK.getToken(BankTransferActivity.this, tokenRequestModel, new
    TokenCallBack() {

        @Override
        public void onSuccess(TokenDetailsResponse tokenDetailsResponse) {

            Toast.makeText(getApplicationContext(), "onSuccess of get Token",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailure(String errorMessage) {

            Toast.makeText(getApplicationContext(), "onFailure of get Token",
                    Toast.LENGTH_SHORT).show();

        }
    });

*/

