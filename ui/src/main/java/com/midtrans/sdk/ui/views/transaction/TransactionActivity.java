package com.midtrans.sdk.ui.views.transaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.adapters.PaymentMethodsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Payment;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.views.creditcard.CreditCardActivity;
import com.midtrans.sdk.ui.widgets.DefaultTextView;

import java.util.List;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class TransactionActivity extends BaseActivity implements TransactionContract.View,
        PaymentMethodsAdapter.PaymentMethodListener, ItemDetailsAdapter.ItemDetailListener {

    private TransactionPresenter presenter;
    private PaymentMethodsAdapter paymentMethodsAdapter;
    private ItemDetailsAdapter itemDetailsAdapter;

    private LinearLayout layoutProgressContainer;
    private RecyclerView rvPaymentMethods;
    private RecyclerView rvItemDetails;
    private DefaultTextView tvHeaderTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initProperties();
        bindView();
        initThemeColor();

        setupView();
        checkoutTransaction();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initProperties() {
        presenter = new TransactionPresenter(this, this);
        paymentMethodsAdapter = new PaymentMethodsAdapter(this);
        itemDetailsAdapter = new ItemDetailsAdapter(this, presenter.getItemDetails());
    }

    private void checkoutTransaction() {
        showProgressContainer(true);
        presenter.checkout();
    }

    private void setupView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvPaymentMethods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvPaymentMethods.setAdapter(paymentMethodsAdapter);

        rvItemDetails.setLayoutManager(new LinearLayoutManager(this));
        rvItemDetails.setAdapter(itemDetailsAdapter);

    }

    private void bindView() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        layoutProgressContainer = (LinearLayout) findViewById(R.id.progress_container);
        rvPaymentMethods = (RecyclerView) findViewById(R.id.rv_payment_methods);
        rvItemDetails = (RecyclerView) findViewById(R.id.rv_item_list);
        tvHeaderTitle = (DefaultTextView) findViewById(R.id.title_header);
    }


    @Override
    public void showProgressContainer(boolean show) {
        if (show) {
            layoutProgressContainer.setVisibility(View.VISIBLE);
        } else {
            layoutProgressContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showConfirmationDialog(String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.btn_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkoutTransaction();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create();
        alertDialog.show();
    }

    /**
     * @param enabledPayments Listof enabled payment method
     */
    @Override
    public void showPaymentMethods(List<PaymentMethodModel> enabledPayments, String merchantName) {
        initTheme();
        rvItemDetails.setBackgroundColor(presenter.getPrimaryColor());
        tvHeaderTitle.setText(merchantName);
        paymentMethodsAdapter.setData(enabledPayments);
    }

    @Override
    public void onItemClick(int position) {
        PaymentMethodModel paymentMethod = paymentMethodsAdapter.getItem(position);
        Log.d(TAG, "method:" + paymentMethod.getName());
        showPaymentActivity(paymentMethod);
    }

    private void showPaymentActivity(PaymentMethodModel paymentMethod) {
        switch (paymentMethod.getPaymentType()) {
            case Payment.Type.CREDIT_CARD:
                Intent intent = new Intent(this, CreditCardActivity.class);
                startActivityForResult(intent, Constants.IntentCode.PAYMENT);
                break;

            case Payment.Type.BANK_TRANSFER:

                break;
            default:

                break;
        }
    }

    @Override
    public void onItemShown() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(TAG, "onActivityResult(): request code is " + requestCode + "," + resultCode);

        if (requestCode == Constants.IntentCode.PAYMENT) {
            if (resultCode == RESULT_OK) {
                PaymentResult result = (PaymentResult) data.getSerializableExtra(Payment.Param.PAYMENT_RESULT);
                Logger.d(TAG, "onActivityResult():response:" + result);
                if (result != null) {
                    presenter.sendPaymentResult(result);
                    finish();
                }

            } else if (resultCode == RESULT_CANCELED) {
                PaymentResult result = (PaymentResult) data.getSerializableExtra(Payment.Param.PAYMENT_RESULT);
                Logger.d(TAG, "onActivityResult():response:" + result);

                if (result != null) {
                    presenter.sendPaymentResult(result);
                    finish();
                }
            }

        } else {
            Logger.d(TAG, "failed to send result back " + requestCode);
        }
    }
}
