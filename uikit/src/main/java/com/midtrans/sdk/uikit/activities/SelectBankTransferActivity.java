package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.adapters.BankTransferListAdapter;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rakawm
 * Deprecated, please use {@link com.midtrans.sdk.uikit.views.banktransfer.list.BankTransferListActivity} instead
 */
@Deprecated
public class SelectBankTransferActivity extends BaseActivity implements BankTransferListAdapter.BankTransferAdapterListener {
    public static final String EXTRA_BANK = "extra.bank";
    private static final String TAG = SelectBankTransferActivity.class.getSimpleName();
    MidtransSDK mMidtransSDK;

    private ArrayList<BankTransferModel> data = new ArrayList<>();

    //Views
    private Toolbar toolbar = null;
    private RecyclerView mRecyclerView = null;
    private BankTransferListAdapter adapter;
    private DefaultTextView textTotalAmount;

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
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
        }

        setUpBankList();
    }

    private void prepareToolbar() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);

        try {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();

            if (midtransSDK != null && midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                drawable.setColorFilter(
                        midtransSDK.getColorTheme().getPrimaryDarkColor(),
                        PorterDuff.Mode.SRC_ATOP);
            }
        } catch (Exception e) {
            Log.d(TAG, "render toolbar:" + e.getMessage());
        }

        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SdkUIFlowUtil.hideKeyboard(SelectBankTransferActivity.this);
                finish();
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
                }
            }
        });
    }

    /**
     * Set up bank list.
     */
    private void setUpBankList() {
        //setup tool bar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        prepareToolbar();
        // setUp recyclerView

        EnabledPayments enabledPayments = (EnabledPayments) getIntent().getSerializableExtra(EXTRA_BANK);
        List<EnabledPayment> banks = new ArrayList<>();
        if (enabledPayments != null) {
            banks.addAll(enabledPayments.getEnabledPayments());
        }

        if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, false)) {
            Intent startBankPayment = new Intent(this, BankTransferActivity.class);
            startBankPayment.putExtra(
                    getString(R.string.position),
                    Constants.BANK_TRANSFER_PERMATA);

            startActivityForResult(
                    startBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }

        } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, false)) {
            Intent startMandiriBankPayment = new Intent(this, BankTransferActivity.class);
            startMandiriBankPayment.putExtra(getString(R.string.position), Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT);
            startActivityForResult(
                    startMandiriBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_BCA, false)) {
            Intent startBankPayment = new Intent(this, BankTransferActivity.class);
            startBankPayment.putExtra(
                    this.getString(R.string.position),
                    Constants.BANK_TRANSFER_BCA
            );

            startActivityForResult(
                    startBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_BNI, false)) {
            Intent startBankPayment = new Intent(this, BankTransferActivity.class);
            startBankPayment.putExtra(
                    this.getString(R.string.position),
                    Constants.BANK_TRANSFER_BNI
            );

            startActivityForResult(
                    startBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, false)) {

            Intent startOtherBankPayment = new Intent(this, BankTransferActivity.class);
            startOtherBankPayment.putExtra(
                    getString(R.string.position),
                    Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK
            );
            startActivityForResult(
                    startOtherBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else {
            if (banks != null && banks.size() > 0) {
                initialiseBankTransfersModel(banks);
                if (data.size() == 1) {
                    startBankTransferPayment(data.get(0));
                } else {
                    adapter.setData(this.data);
                }
            } else {
                initialiseBankTransfersModel();
                adapter.setData(this.data);
            }
        }
    }

    /**
     * initialize views.
     */
    private void bindActivity() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_bank_list);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new BankTransferListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);

    }

    /**
     * initialize adapter data model by snap values.
     */
    private void initialiseBankTransfersModel(List<EnabledPayment> banks) {
        data.clear();
        if (banks.size() > 0) {
            for (EnabledPayment bank : banks) {
                BankTransferModel model = PaymentMethods.getBankTransferModel(this, bank.getType(), bank.getStatus());
                if (model != null) {
                    data.add(model);
                }
            }
        }
        SdkUIFlowUtil.sortBankPaymentMethodsByPriority(data);
    }

    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseBankTransfersModel() {
        data.clear();
        for (BankTransferModel model : PaymentMethods.getBankTransferList(this)) {
            if (model != null) {
                data.add(model);
            }
        }
        SdkUIFlowUtil.sortBankPaymentMethodsByPriority(data);
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
            } else if (resultCode == RESULT_CANCELED) {
                if (data == null) {
                    if (this.data.size() == 1) {
                        finish();
                    }

                    if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, false)
                            || getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, false)
                            || getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_BCA, false)
                            || getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, false)) {
                        finish();
                    }
                } else {
                    setResult(RESULT_OK, data);
                    finish();
                }
            }

        } else {
            Logger.d(TAG, "failed to send result back " + requestCode);
        }
    }

    @Override
    public void onItemClick(int position) {
        BankTransferModel item = adapter.getItem(position);
        startBankTransferPayment(item);
    }

    private void startBankTransferPayment(BankTransferModel item) {
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
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equals(getString(R.string.permata_bank_transfer))) {
            Intent startBankPayment = new Intent(this, BankTransferActivity.class);
            startBankPayment.putExtra(
                    getString(R.string.position),
                    Constants.BANK_TRANSFER_PERMATA);

            startActivityForResult(
                    startBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equals(getString(R.string.bni_bank_transfer))) {
            Intent startBankPayment = new Intent(this, BankTransferActivity.class);
            startBankPayment.putExtra(
                    getString(R.string.position),
                    Constants.BANK_TRANSFER_BNI);

            startActivityForResult(
                    startBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else if (name.equals(getString(R.string.mandiri_bank_transfer))) {
            Intent startMandiriBankPayment = new Intent(this, BankTransferActivity.class);
            startMandiriBankPayment.putExtra(getString(R.string.position), Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT);
            startActivityForResult(
                    startMandiriBankPayment,
                    Constants.RESULT_CODE_PAYMENT_TRANSFER
            );
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
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
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        } else {
            Toast.makeText(this, "This feature is not implemented yet.", Toast.LENGTH_SHORT).show();
        }
    }
}
