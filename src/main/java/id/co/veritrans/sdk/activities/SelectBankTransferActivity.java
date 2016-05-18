package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.adapters.BankTransferListAdapter;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.LocalDataHandler;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.BankTransferModel;
import id.co.veritrans.sdk.models.CustomerDetails;
import id.co.veritrans.sdk.models.UserDetail;
import id.co.veritrans.sdk.widgets.HeaderView;

/**
 * @author rakawm
 */
public class SelectBankTransferActivity extends BaseActivity {

    private static final String TAG = SelectBankTransferActivity.class.getSimpleName();

    VeritransSDK mVeritransSDK;

    private ArrayList<BankTransferModel> data = new ArrayList<>();

    //Views
    private Toolbar toolbar = null;
    private AppBarLayout mAppBarLayout = null;
    private RecyclerView mRecyclerView = null;
    private HeaderView toolbarHeaderView = null;
    private HeaderView floatHeaderView = null;
    private TextView headerTextView = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank_transfer);
        initializeTheme();
        mVeritransSDK = VeritransSDK.getVeritransSDK();
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
        //initialize views
        bindActivity();

        //setup tool bar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setUp recyclerView
        initialiseAdapterData();
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
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        toolbarHeaderView = (HeaderView) findViewById(R.id.toolbar_header_view);
        headerTextView = (TextView) findViewById(R.id.title_header);
    }

    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseAdapterData() {
        data.clear();
        for (BankTransferModel model : mVeritransSDK.getBankTransferList()) {

            if (model.isSelected()) {
                data.add(model);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            SdkUtil.hideKeyboard(this);
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
                data.setAction(getString(R.string.event_transaction_complete));
                sendBroadcast(data);
                setResult(RESULT_OK);
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
