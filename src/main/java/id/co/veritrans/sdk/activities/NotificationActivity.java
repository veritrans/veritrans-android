package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.fragments.PaymentTransactionStatusFragment;
import id.co.veritrans.sdk.models.TransactionResponse;

/**
 * Created by chetan on 01/12/15.
 */
public class NotificationActivity extends AppCompatActivity {
    private PaymentTransactionStatusFragment paymentStatusFragment;
    private FragmentManager fragmentManager;
    private String currentFragmentName;
    private TransactionResponse transactionResponse;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionResponse = (TransactionResponse) getIntent().getSerializableExtra(Constants.PAYMENT_STATUS);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_notification);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_payment_status));
        setUpFragment();
    }

    private void setUpFragment() {
        // setup  fragment
        if (transactionResponse != null) {
            paymentStatusFragment = PaymentTransactionStatusFragment.newInstance(transactionResponse);
            replaceFragment(paymentStatusFragment, true, false);
        } else {
            //show error
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, boolean clearBackStack) {
        if (fragment != null) {
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
                ft.add(R.id.notification_container_layout, fragment, backStateName);
                /*if (addToBackStack) {
                    ft.addToBackStack(backStateName);
                }*/
                ft.commit();
                currentFragmentName = backStateName;
            }
        }
    }
}
