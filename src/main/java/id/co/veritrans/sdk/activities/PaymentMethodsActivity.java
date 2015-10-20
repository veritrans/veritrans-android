package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.adapters.PaymentMethodsAdapter;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.models.PaymentMethodsModel;

/**
 * Created by shivam on 10/16/15.
 */
public class PaymentMethodsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView = null;
    private ArrayList<PaymentMethodsModel> data = new ArrayList<>();
    private static final String TAG = PaymentMethodsActivity.class.getSimpleName();

    private CollapsingToolbarLayout mCollapsingToolbarLayout = null;
    private Toolbar mToolbar = null;

    private TextView mTextViewPayableAmount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payments_method);
        setUpPaymentMethods();
    }

    private void setUpPaymentMethods() {
        initialiseAdapterData();

        mCollapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.select_payment_method));

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_payment_methods);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        PaymentMethodsAdapter paymentMethodsAdapter = new
                PaymentMethodsAdapter(getApplicationContext(), data);
        mRecyclerView.setAdapter(paymentMethodsAdapter);


    }

    private void initialiseAdapterData() {

        String[] names = getResources().getStringArray(R.array.payment_methods);
        int[] icons = new int[names.length];

        Logger.d(TAG, "there are total " + names.length + " payment methods available.");

        for (int i = 0; i < names.length; i++) {
            icons[i] = R.drawable.ic_launcher;
        }

        for (int i = 0; i < names.length; i++) {
            PaymentMethodsModel model = new PaymentMethodsModel(names[i], icons[i]);
            data.add(model);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
