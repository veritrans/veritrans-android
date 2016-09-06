package id.co.veritrans.sdk.uiflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.LocalDataHandler;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.TransactionRequest;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.models.BankTransferModel;
import id.co.veritrans.sdk.coreflow.models.CustomerDetails;
import id.co.veritrans.sdk.coreflow.models.UserDetail;
import id.co.veritrans.sdk.uiflow.PaymentMethods;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.adapters.BankTransferListAdapter;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;

/**
 * @author rakawm
 */
public class SelectBankTransferActivity extends BaseActivity {

    public static final String EXTRA_BANK = "extra.bank";
    private static final String TAG = SelectBankTransferActivity.class.getSimpleName();
    VeritransSDK mVeritransSDK;

    private ArrayList<BankTransferModel> data = new ArrayList<>();

    //Views
    private Toolbar toolbar = null;
    private RecyclerView mRecyclerView = null;
    private ImageView logo = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank_transfer);
        //initialize views
        initializeTheme();
        bindActivity();
        mVeritransSDK = VeritransSDK.getInstance();
        TransactionRequest transactionRequest = null;
        if (mVeritransSDK != null) {
            transactionRequest = mVeritransSDK.getTransactionRequest();
            UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
            CustomerDetails customerDetails = new CustomerDetails(userDetail.getUserFullName(), "", userDetail.getEmail(), userDetail.getPhoneNumber());
            transactionRequest.setCustomerDetails(customerDetails);
        }

        setUpBankList();
    }

    /**
     * Set up bank list.
     */
    private void setUpBankList() {
        //setup tool bar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setUp recyclerView
        ArrayList<String> banks = getIntent().getStringArrayListExtra(EXTRA_BANK);
        if (banks != null && banks.size() > 0) {
            initialiseAdapterData(banks);
        } else {
            initialiseAdapterData();
        }
        BankTransferListAdapter adapter = new
                BankTransferListAdapter(this, data);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * initialize views.
     */
    private void bindActivity() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_bank_list);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);
    }

    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseAdapterData(List<String> banks) {
        data.clear();
        if (banks.size() > 0) {

            for (String bank : banks) {

                BankTransferModel model = PaymentMethods.getBankTransferModel(this, bank);
                if (model != null) {
                    data.add(model);
                }
            }
        }
    }

    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseAdapterData() {
        data.clear();
        for (BankTransferModel model : PaymentMethods.getBankTransferList(this)) {
            if (model != null) {
                data.add(model);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            SdkUIFlowUtil.hideKeyboard(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * sends broadcast for transaction details.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Logger.d(TAG, "in onActivity result : request code is " + requestCode + "," + resultCode);

        if (requestCode == Constants.RESULT_CODE_PAYMENT_TRANSFER) {
            Logger.d(TAG, "sending result back with code " + requestCode);

            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            }

        } else {
            Logger.d(TAG, "failed to send result back " + requestCode);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
