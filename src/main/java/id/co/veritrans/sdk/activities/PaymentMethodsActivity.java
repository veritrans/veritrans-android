package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
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
    private static final float ALPHA = 0.6f;
    private static final String TAG = PaymentMethodsActivity.class.getSimpleName();
    private static final float PERCENTAGE_TOTAL = 1f;
    public static final String PAYABLE_AMOUNT = "Payable Amount";
    private ArrayList<PaymentMethodsModel> data = new ArrayList<>();


    //Views
    private Toolbar toolbar = null;
    private AppBarLayout mAppBarLayout = null;
    private RecyclerView mRecyclerView = null;
    private VeritransSDK veritransSDK = null;
    private StorageDataHandler storageDataHandler = null;

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
            toolbarHeaderView.bindTo(PAYABLE_AMOUNT, "" + amount);
            floatHeaderView.bindTo(PAYABLE_AMOUNT, "" + amount);

            floatHeaderView.getSubTitleTextView().setAlpha(PERCENTAGE_TOTAL);
            floatHeaderView.getTitleTextView().setAlpha(ALPHA);

            /*toolbarHeaderView.getSubTitleTextView().setAlpha(ALPHA);
            toolbarHeaderView.getTitleTextView().setAlpha(PERCENTAGE_TOTAL);
            */

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
        for (PaymentMethodsModel paymentMethodsModel : veritransSDK.getSelectedPaymentMethods()) {

            if (paymentMethodsModel.isSelected()) {
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

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        applyAlpha(percentage);

        if ( percentage == PERCENTAGE_TOTAL && isHideToolbarView ) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        } else if (percentage < PERCENTAGE_TOTAL && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }

        // manage visibility for title TextView
        /*if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            headerTextView.setVisibility(View.GONE);
        } else {
            headerTextView.setVisibility(View.VISIBLE);
        }*/

        if (percentage == PERCENTAGE_TOTAL) {
            headerTextView.setVisibility(View.GONE);
        } else {
            headerTextView.setVisibility(View.VISIBLE);
        }

        //applySlidingEffect(percentage);

    }

    private void applyAlpha(float percentage) {
        float constant = 0.5f;

        headerTextView.setAlpha( PERCENTAGE_TOTAL - percentage);

        if ( percentage >= constant) {

            float alpha = ( percentage * ALPHA ) / constant;

            if ( alpha > PERCENTAGE_TOTAL ) {
                floatHeaderView.getTitleTextView().setAlpha(PERCENTAGE_TOTAL);
                floatHeaderView.getTitleTextView().setPivotX(1);
                floatHeaderView.getTitleTextView().setScaleX(1 + alpha / 3.5f);
                floatHeaderView.getTitleTextView().setScaleY(1 + alpha / 3.5f);
                floatHeaderView.invalidate();
            } else {
                floatHeaderView.getTitleTextView().setAlpha(alpha);
                floatHeaderView.getTitleTextView().setPivotX(1);
                floatHeaderView.getTitleTextView().setScaleX(1 + alpha / 3.5f);
                floatHeaderView.getTitleTextView().setScaleY(1 + alpha / 3.5f);
                floatHeaderView.invalidate();
            }

            alpha = ALPHA  / percentage ;

            if ( alpha > PERCENTAGE_TOTAL ) {
                floatHeaderView.getSubTitleTextView().setAlpha( PERCENTAGE_TOTAL );
            } else if (alpha < ALPHA ){
                floatHeaderView.getSubTitleTextView().setAlpha( ALPHA );
            }else {
                floatHeaderView.getSubTitleTextView().setAlpha( alpha );
            }

            // for 0.5 it is 1 , for 1 it 0.6

        }else {

            floatHeaderView.getTitleTextView().setAlpha(ALPHA);
            floatHeaderView.getSubTitleTextView().setAlpha(PERCENTAGE_TOTAL);
            floatHeaderView.getTitleTextView().setScaleX(1);
            floatHeaderView.getTitleTextView().setScaleY(1);
            floatHeaderView.invalidate();
        }
    }

    private void applySlidingEffect(float percentage) {


        if (percentage > 5) {

            // manage title effect
            if (percentage >= 0.9f) {
                floatHeaderView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 19.5f);
                floatHeaderView.getSubTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        14.5f);

                floatHeaderView.getSubTitleTextView().setAlpha(0.6f);
                floatHeaderView.getTitleTextView().setAlpha(1f);
                floatHeaderView.invalidate();
            } else if (percentage >= 0.8f) {
                floatHeaderView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                floatHeaderView.getSubTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            } else if (percentage >= 0.7f) {
                floatHeaderView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.5f);
                floatHeaderView.getSubTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        15.5f);

                floatHeaderView.getSubTitleTextView().setAlpha(0.6f);
                floatHeaderView.getTitleTextView().setAlpha(1);

                floatHeaderView.invalidate();
            } else if (percentage >= 0.6f) {
                floatHeaderView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                floatHeaderView.getSubTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                floatHeaderView.getSubTitleTextView().setAlpha(0.7f);
                floatHeaderView.getTitleTextView().setAlpha(0.9f);

            } else if (percentage >= 0.5f) {
                floatHeaderView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 17.5f);
                floatHeaderView.getSubTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        16.5f);

                floatHeaderView.getSubTitleTextView().setAlpha(0.75f);
                floatHeaderView.getTitleTextView().setAlpha(0.85f);
                floatHeaderView.invalidate();
            }
        } else {

            //for reverse effect
            if (percentage <= 0.1f) {
                floatHeaderView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.5f);
                floatHeaderView.getSubTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        19.5f);

                floatHeaderView.getSubTitleTextView().setAlpha(0.9f);
                floatHeaderView.getTitleTextView().setAlpha(0.65f);
                floatHeaderView.invalidate();
            } else if (percentage <= 0.2f) {
                floatHeaderView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
                floatHeaderView.getSubTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f);

                floatHeaderView.getSubTitleTextView().setAlpha(0.85f);
                floatHeaderView.getTitleTextView().setAlpha(0.75f);

                floatHeaderView.invalidate();

            } else if (percentage <= 0.3f) {
                floatHeaderView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.5f);
                floatHeaderView.getSubTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);

                floatHeaderView.getSubTitleTextView().setAlpha(0.8f);
                floatHeaderView.getTitleTextView().setAlpha(0.8f);
                floatHeaderView.invalidate();

            } else if (percentage <= 0.4f) {
                floatHeaderView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                floatHeaderView.getSubTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f);

                floatHeaderView.getSubTitleTextView().setAlpha(0.75f);
                floatHeaderView.getTitleTextView().setAlpha(0.8f);

                floatHeaderView.invalidate();
            }
        }
    }
}