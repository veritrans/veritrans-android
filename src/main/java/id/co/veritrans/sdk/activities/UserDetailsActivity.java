package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.fragments.UserAddressFragment;
import id.co.veritrans.sdk.fragments.UserDetailFragment;
import id.co.veritrans.sdk.model.UserAddress;
import id.co.veritrans.sdk.model.UserDetail;

public class UserDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserDetails();
    }

    public void checkUserDetails() {
        StorageDataHandler storageDataHandler = new StorageDataHandler();
        try {
            UserDetail userDetail = (UserDetail) storageDataHandler.readObject(this, Constants.USER_DETAILS);
            if(userDetail!=null && !TextUtils.isEmpty(userDetail.getUserFullName())){
                //TODO check user have address filled
                //if no take user to select address

                ArrayList<UserAddress>userAddresses = userDetail.getUserAddresses();
                if(userAddresses != null && !userAddresses.isEmpty()){
                    Log.i("UserdetailActivity", "userAddresses:" + userAddresses.size());
                    Intent paymentOptionIntent = new Intent(this,PaymentMethodsActivity.class);
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.user_detail_container, fragment);
            ft.commit();
        }
    }
}