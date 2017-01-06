package com.midtrans.sdk.uikit.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BankTransactionStatusFragment;
import com.midtrans.sdk.uikit.fragments.PaymentTransactionStatusFragment;
import com.squareup.picasso.Picasso;

/**
 * @author rakawm
 */
public class BaseActivity extends AppCompatActivity {
    protected String currentFragmentName;
    protected Fragment currentFragment = null;
    protected boolean saveCurrentFragment = false;
    protected int RESULT_CODE = RESULT_CANCELED;

    public void initializeTheme() {
        MidtransSDK mMidtransSDK = MidtransSDK.getInstance();
        if (mMidtransSDK != null) {
            ImageView logo = (ImageView) findViewById(R.id.merchant_logo);
            TextView name = (TextView) findViewById(R.id.merchant_name);
            if (logo != null) {
                if (mMidtransSDK.getMerchantLogo() != null) {
                    if (name != null) {
                        name.setVisibility(View.GONE);
                    }
                    Picasso.with(this)
                            .load(mMidtransSDK.getMerchantLogo())
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

    protected void initPaymentStatus(TransactionResponse transactionResponse, String errorMessage, int paymentMethod, boolean addToBackStack) {
        if (MidtransSDK.getInstance().getUIKitCustomSetting().isShowPaymentStatus()) {
            PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                    PaymentTransactionStatusFragment.newInstance(transactionResponse, paymentMethod);
            replaceFragment(paymentTransactionStatusFragment, R.id.main_layout, addToBackStack, false);
        } else {
            setResultCode(RESULT_OK);
            setResultAndFinish(transactionResponse, errorMessage);
        }
    }


    protected void setResultAndFinish(TransactionResponse transactionResponse, String errorMessage) {
        Intent data = new Intent();
        data.putExtra(getString(R.string.transaction_response), transactionResponse);
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(this.RESULT_CODE, data);
        finish();
    }

    public void setResultCode(int resultCode) {
        this.RESULT_CODE = resultCode;
    }

    protected void initBankTransferPaymentStatus(TransactionResponse transactionResponse, String errorMessage, int paymentMethod, String statusFragment) {
        if (MidtransSDK.getInstance().getUIKitCustomSetting().isShowPaymentStatus()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            BankTransactionStatusFragment bankTransactionStatusFragment =
                    BankTransactionStatusFragment.newInstance(transactionResponse,
                            paymentMethod);

            // setup transaction status fragment
            fragmentTransaction.replace(R.id.instruction_container,
                    bankTransactionStatusFragment, statusFragment);
            fragmentTransaction.addToBackStack(statusFragment);
            fragmentTransaction.commit();
        } else {
            setResultCode(RESULT_OK);
            setResultAndFinish(transactionResponse, errorMessage);
        }
    }

}
