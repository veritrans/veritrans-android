package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.BankTransferFragment;
import id.co.veritrans.sdk.fragments.IndomaretPaymentFragment;
import id.co.veritrans.sdk.fragments.IndomaretPaymentStatusFragment;
import id.co.veritrans.sdk.fragments.InstructionBBMMoneyFragment;
import id.co.veritrans.sdk.fragments.InstructionIndomaretFragment;
import id.co.veritrans.sdk.models.IndomaretRequestModel;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by Ankit on 12/3/15.
 */
public class BBMMoneyActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private TextViewFont textViewOrderId = null;
    private TextViewFont textViewAmount = null;
    private Button buttonConfirmPayment = null;
    private AppBarLayout appBarLayout = null;
    private TextViewFont textViewTitle = null;

    private VeritransSDK veritransSDK = null;
    private Toolbar toolbar = null;

    private InstructionBBMMoneyFragment instructionBBMMoneyFragment = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private int position = Constants.PAYMENT_METHOD_BBM_MONEY;
    private int RESULT_CODE = RESULT_CANCELED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbm_money);

        veritransSDK = VeritransSDK.getVeritransSDK();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(Constants.POSITION, Constants
                    .PAYMENT_METHOD_BBM_MONEY);
        } else {
            SdkUtil.showSnackbar(BBMMoneyActivity.this, Constants.ERROR_SOMETHING_WENT_WRONG);
            finish();
        }

        initializeView();
        bindDataToView();
        setUpHomeFragment();
    }


    /**
     * set up {@link BankTransferFragment} to display payment instructions.
     */
    private void setUpHomeFragment() {
        // setup home fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        instructionBBMMoneyFragment = new InstructionBBMMoneyFragment();

        fragmentTransaction.add(R.id.bbm_money_container,
                instructionBBMMoneyFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        textViewOrderId = (TextViewFont) findViewById(R.id.text_order_id);
        textViewAmount = (TextViewFont) findViewById(R.id.text_amount);
        textViewTitle = (TextViewFont) findViewById(R.id.text_title);
        buttonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);

        //setup tool bar
        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindDataToView() {
        if (veritransSDK != null) {
            textViewAmount.setText(Constants.CURRENCY_PREFIX + " " + veritransSDK
                    .getTransactionRequest().getAmount());
            textViewOrderId.setText("" + veritransSDK.getTransactionRequest().getOrderId());
            buttonConfirmPayment.setTypeface(veritransSDK.getTypefaceOpenSansSemiBold());
            buttonConfirmPayment.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {

        }
    }
}
