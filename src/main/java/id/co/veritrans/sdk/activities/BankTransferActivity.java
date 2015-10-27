package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.BankTransactionStatusFragment;
import id.co.veritrans.sdk.fragments.BankTransferFragment;
import id.co.veritrans.sdk.fragments.BankTransferPaymentFragment;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/26/15.
 */
public class BankTransferActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public String currentFragment = "home";


    private TextViewFont mTextViewOrderId = null;
    private TextViewFont mTextViewAmount = null;
    private Button mButtonConfirmPayment = null;

    private VeritransSDK mVeritransSDK = null;
    private Toolbar mToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);

        mVeritransSDK = VeritransSDK.getVeritransSDK();

        initializeView();
        bindDataToView();

        setUpHomeFragment();
    }

    private void setUpHomeFragment() {
        // setup home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.bank_transfer_container,
                new BankTransferFragment(), HOME_FRAGMENT);
        fragmentTransaction.addToBackStack(HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }

    @Override
    public void onBackPressed() {

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 1) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }*/

        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return false;
    }


    private void initializeView() {

        mTextViewOrderId = (TextViewFont) findViewById(R.id.text_order_id);
        mTextViewAmount = (TextViewFont) findViewById(R.id.text_amount);
        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);

        //setup tool bar
        mToolbar.setTitle(""); // disable default Text
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void bindDataToView() {

        if (mVeritransSDK != null) {
            mTextViewAmount.setText(Constants.CURRENCY_PREFIX + " " + mVeritransSDK.getAmount());
            mTextViewOrderId.setText(" " + mVeritransSDK.getOrderId());
            mButtonConfirmPayment.setTypeface(mVeritransSDK.getTypefaceOpenSansSemiBold());
            mButtonConfirmPayment.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_confirm_payment) {

            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {
                setUpTransactionFragment();

            } else if (currentFragment.equalsIgnoreCase(PAYMENT_FRAGMENT)) {
                setUpTransactionStatusFragment();
            } else {
                onBackPressed();
            }
        }
    }


    private void setUpTransactionStatusFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // setup transaction status fragment
        fragmentTransaction.replace(R.id.bank_transfer_container,
                new BankTransactionStatusFragment(), STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();


        currentFragment = STATUS_FRAGMENT;
        mButtonConfirmPayment.setText(R.string.done);
    }


    private void setUpTransactionFragment() {
        // setup transaction fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bank_transfer_container,
                new BankTransferPaymentFragment(), PAYMENT_FRAGMENT);
        fragmentTransaction.addToBackStack(PAYMENT_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = PAYMENT_FRAGMENT;
        mButtonConfirmPayment.setText(R.string.complete_payment_at_atm);
    }

}