package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.adapters.BankTransferListAdapter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rakawm
 */
public class SelectBankTransferActivity extends BaseActivity implements BankTransferListAdapter.BankTransferAdapterListener {

    public static final String EXTRA_BANK = "extra.bank";
    private static final String TAG = SelectBankTransferActivity.class.getSimpleName();
    MidtransSDK mMidtransSDK;

    private ArrayList<BankTransferModel> data = new ArrayList<>();

    //Views
    private Toolbar toolbar = null;
    private RecyclerView mRecyclerView = null;
    private ImageView logo = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private BankTransferListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank_transfer);
        //initialize views
        initializeTheme();
        bindActivity();
        mMidtransSDK = MidtransSDK.getInstance();
        TransactionRequest transactionRequest = null;
        if (mMidtransSDK != null) {
            transactionRequest = mMidtransSDK.getTransactionRequest();
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
        adapter = new BankTransferListAdapter(this, data);
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

    @Override
    public void onItemClick(int position) {
        BankTransferModel item = adapter.getItem(position);
        String name = item.getBankName();

        if (name.equals(getString(R.string.bca_bank_transfer))) {

            Intent startBankPayment = new Intent(this, BankTransferActivity.class);
            startBankPayment.putExtra(
                    this.getString(R.string.position),
                    Constants.BANK_TRANSFER_BCA
            );

            startActivityForResult(
                    startBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );

        } else if (name.equals(getString(R.string.permata_bank_transfer))) {

            Intent startBankPayment = new Intent(this, BankTransferActivity.class);
            startBankPayment.putExtra(
                    getString(R.string.position),
                    Constants.BANK_TRANSFER_PERMATA);

            startActivityForResult(
                    startBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );

        } else if (name.equals(getString(R.string.mandiri_bank_transfer))) {

            Intent startMandiriBankPayment = new Intent(this, BankTransferActivity.class);
            startMandiriBankPayment.putExtra(getString(R.string.position), Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT);
            startActivityForResult(
                    startMandiriBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );

        } else if (name.equals(getString(R.string.all_bank_transfer))) {

            Intent startOtherBankPayment = new Intent(this, BankTransferActivity.class);
            startOtherBankPayment.putExtra(
                    getString(R.string.position),
                    Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK
            );
            startActivityForResult(
                    startOtherBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );

        } else {
            Toast.makeText(this, "This feature is not implemented yet.", Toast.LENGTH_SHORT).show();
        }
    }
}
