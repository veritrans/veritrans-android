package com.midtrans.sdk.ui.views.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.adapters.PaymentMethodsAdapter;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.presenters.TransactionPresenter;

import java.util.List;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class TransactionActivity extends BaseActivity implements TransactionView,
        PaymentMethodsAdapter.PaymentMethodListener, ItemDetailsAdapter.ItemDetailListener {

    private TransactionPresenter presenter;
    private PaymentMethodsAdapter paymentMethodsAdapter;
    private ItemDetailsAdapter itemDetailsAdapter;

    private LinearLayout layoutProgressContainer;
    private RecyclerView rvPaymentMethods;
    private RecyclerView rvItemDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initProperties();
        bindView();
        setupView();
        checkoutTransaction();
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
    public void showPaymentMethods(List<PaymentMethodModel> enabledPayments) {
        paymentMethodsAdapter.setData(enabledPayments);
    }

    @Override
    public void onItemClick(int position) {
        PaymentMethodModel paymentMethod = paymentMethodsAdapter.getItem(position);
        showPaymentActivity(paymentMethod);
    }

    private void showPaymentActivity(PaymentMethodModel paymentMethod) {

    }

    @Override
    public void onItemShown() {

    }

}
