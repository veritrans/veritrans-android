package id.co.veritrans.sdk.activities;

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
import id.co.veritrans.sdk.model.UserAddress;
import id.co.veritrans.sdk.model.UserDetail;

public class UserDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StorageDataHandler storageDataHandler = new StorageDataHandler();
        try {
            UserDetail userDetail = (UserDetail) storageDataHandler.readObject(this, Constants.USER_DETAILS);
            if(userDetail!=null && !TextUtils.isEmpty(userDetail.getUserFullName())){
                //TODO check user have address filled
                //if no take user to select address
                ArrayList<UserAddress>userAddresses = (ArrayList<UserAddress>) storageDataHandler.readObject(this, Constants.USER_ADDRESS_DETAILS);
                if(userAddresses != null && !userAddresses.isEmpty()){
                    Log.i("UserdetailActivity","userAddresses:"+userAddresses.size());
                    //open sele
                }
                //if yes take user to payment option screen
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*UserDetailFragment userDetailFragment = UserDetailFragment.newInstance();
        replaceFragment(userDetailFragment);*/
        UserAddressFragment userAddressFragment = UserAddressFragment.newInstance();
        replaceFragment(userAddressFragment);
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