package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.adapters.PaymentMethodsAdapter;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.CustomerDetails;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.models.UserDetail;
import id.co.veritrans.sdk.utilities.Utils;
import id.co.veritrans.sdk.widgets.HeaderView;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/16/15.
 */
public class PaymentMethodsActivity extends AppCompatActivity implements AppBarLayout
        .OnOffsetChangedListener {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.3f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.7f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private static final String TAG = PaymentMethodsActivity.class.getSimpleName();
    private ArrayList<PaymentMethodsModel> data = new ArrayList<>();


    //Views
    private Toolbar toolbar = null;
    private AppBarLayout mAppBarLayout = null;
    private RecyclerView mRecyclerView = null;
    private VeritransSDK veritransSDK = null;
    private StorageDataHandler storageDataHandler= null;

    private HeaderView toolbarHeaderView = null;
    private HeaderView floatHeaderView = null;
    private TextViewFont headerTextView = null;
    private boolean isHideToolbarView = false;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payments_method);

        storageDataHandler = new StorageDataHandler();

        UserDetail userDetail = null;
        try {
            userDetail = (UserDetail) storageDataHandler.readObject(this, Constants.USER_DETAILS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        veritransSDK = VeritransSDK.getVeritransSDK();
        TransactionRequest transactionRequest = veritransSDK.getTransactionRequest();
        CustomerDetails customerDetails = new CustomerDetails(userDetail.getUserFullName(), "",
                userDetail.getEmail(), userDetail.getPhoneNumber());
        transactionRequest.setCustomerDetails(customerDetails);
        setUpPaymentMethods();
    }

    private void setUpPaymentMethods() {

        //initialize views
        bindActivity();

        //setup tool bar
        mAppBarLayout.addOnOffsetChangedListener(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bindDataToView();

        // setUp recyclerView
        initialiseAdapterData();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        PaymentMethodsAdapter paymentMethodsAdapter = new
                PaymentMethodsAdapter(this, data);
        mRecyclerView.setAdapter(paymentMethodsAdapter);
    }

    private void bindDataToView() {

        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            String amount = Constants.CURRENCY_PREFIX + " "
                    + Utils.getFormattedAmount(veritransSDK.getTransactionRequest().getAmount());

            collapsingToolbarLayout.setTitle(" ");
            toolbarHeaderView.bindTo("Payable Amount", ""+amount);
            floatHeaderView.bindTo("Payable Amount", ""+amount);
            mAppBarLayout.addOnOffsetChangedListener(this);

        }

    }

    private void bindActivity() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_payment_methods);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        toolbarHeaderView = (HeaderView) findViewById(R.id.toolbar_header_view);
        floatHeaderView = (HeaderView) findViewById(R.id.float_header_view);
        headerTextView = (TextViewFont) findViewById(R.id.title_header);
    }




    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseAdapterData() {

        data.clear();
        for(PaymentMethodsModel paymentMethodsModel:veritransSDK.getSelectedPaymentMethods()){

            if(paymentMethodsModel.isSelected()){
                data.add(paymentMethodsModel);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            if( item.getItemId() ==  android.R.id.home){
                finish();
            }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        headerTextView.setAlpha(1 - percentage);

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            headerTextView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            headerTextView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        }


    }
}