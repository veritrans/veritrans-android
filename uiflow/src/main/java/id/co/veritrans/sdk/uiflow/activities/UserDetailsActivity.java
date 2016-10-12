package id.co.veritrans.sdk.uiflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.LocalDataHandler;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFinishedEvent;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.UserAddress;
import id.co.veritrans.sdk.coreflow.models.UserDetail;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.fragments.UserAddressFragment;
import id.co.veritrans.sdk.uiflow.fragments.UserDetailFragment;

public class UserDetailsActivity extends BaseActivity {

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
                    Logger.i("UserDetailActivity", "userAddresses:" + userAddresses.size());
                    runPaymentActivity();
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

    public void runPaymentActivity() {
        String paymentMethod = getIntent().getStringExtra(Constants.PARAM_PAYMENT_METHOD);
        if(!TextUtils.isEmpty(paymentMethod) && paymentMethod.equals(getString(R.string.payment_method_credit_card))){
            Intent intent = new Intent(this, CreditDebitCardFlowActivity.class);
            startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        }else{
            Intent paymentOptionIntent = new Intent(this, PaymentMethodsActivity.class);
            startActivity(paymentOptionIntent);
            finish();
        }
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

        if(item.getItemId() ==  android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "in onActivity result : request code is " + requestCode + "," + resultCode);

        if (requestCode == Constants.RESULT_CODE_PAYMENT_TRANSFER) {
            Logger.d(TAG, "sending result back with code " + requestCode);

            if (resultCode == RESULT_OK) {
                TransactionResponse response = (TransactionResponse) data.getSerializableExtra(getString(R.string.transaction_response));
                if (response != null) {
                    VeritransBusProvider.getInstance().post(new TransactionFinishedEvent(response));
                } else {
                    VeritransBusProvider.getInstance().post(new TransactionFinishedEvent());
                }
            }
            finish();

        } else {
            Logger.d(TAG, "failed to send result back " + requestCode);
        }
    }

}