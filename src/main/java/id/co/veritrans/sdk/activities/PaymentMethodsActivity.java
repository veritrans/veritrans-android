package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
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
 * Displays list of available payment methods.
 * <p/>
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

    private VeritransSDK veritransSDK = null;
    private StorageDataHandler storageDataHandler = null;
    private boolean isHideToolbarView = false;

    //Views
    private Toolbar toolbar = null;
    private AppBarLayout mAppBarLayout = null;
    private RecyclerView mRecyclerView = null;
    private HeaderView toolbarHeaderView = null;
    private HeaderView floatHeaderView = null;
    private TextViewFont headerTextView = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private TextViewFont textViewMeasureHeight = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_method);

        storageDataHandler = new StorageDataHandler();

        UserDetail userDetail = null;
        try {
            userDetail = (UserDetail) storageDataHandler.readObject(this, Constants.USER_DETAILS);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        veritransSDK = VeritransSDK.getVeritransSDK();
        TransactionRequest transactionRequest = veritransSDK.getTransactionRequest();
        CustomerDetails customerDetails = new CustomerDetails(userDetail.getUserFullName(), "",
                userDetail.getEmail(), userDetail.getPhoneNumber());
        transactionRequest.setCustomerDetails(customerDetails);
        setUpPaymentMethods();
    }

    /**
     * if recycler view fits within screen then it will disable the scrolling of it.
     */
    private void handleScrollingOfRecyclerView() {
        headerTextView.setAlpha(1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView
                        .getLayoutManager();

                int hiddenTextViewHeight = textViewMeasureHeight.getHeight();
                int recyclerViewHieght = mRecyclerView.getMeasuredHeight();
                int appBarHeight = mAppBarLayout.getHeight();
                int exactHeightForCompare = hiddenTextViewHeight - appBarHeight;

                int oneRowHeight = dp2px(60);
                int totalHeight = (mRecyclerView.getAdapter().getItemCount() - 1) * oneRowHeight;

                if (totalHeight < exactHeightForCompare) {
                    disableScrolling();
                    headerTextView.setAlpha(1);
                }
            }
        }, 200);
    }

    /**
     * Disable scrolling of recycler view.
     */
    private void disableScrolling() {
        //turn off scrolling
        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams)
                mAppBarLayout.getLayoutParams();
        appBarLayoutParams.setBehavior(null);
        mAppBarLayout.setLayoutParams(appBarLayoutParams);
    }

    /**
     * bind views , initializes adapter and set it to recycler view.
     */
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
        PaymentMethodsAdapter paymentMethodsAdapter = new
                PaymentMethodsAdapter(this, data);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(paymentMethodsAdapter);

        // disable scrolling of recycler view if there is no need of it.
        handleScrollingOfRecyclerView();

    }

    /**
     * set data to view.
     */
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
            mAppBarLayout.addOnOffsetChangedListener(this);
        }

    }

    /**
     * initialize views.
     */
    private void bindActivity() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_payment_methods);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        toolbarHeaderView = (HeaderView) findViewById(R.id.toolbar_header_view);
        floatHeaderView = (HeaderView) findViewById(R.id.float_header_view);
        headerTextView = (TextViewFont) findViewById(R.id.title_header);
        textViewMeasureHeight = (TextViewFont) findViewById(R.id.textview_to_compare);
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
            SdkUtil.hideKeyboard(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        applyAlpha(percentage);

        if (percentage == PERCENTAGE_TOTAL && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        } else if (percentage < PERCENTAGE_TOTAL && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }

        if (percentage == PERCENTAGE_TOTAL) {
            headerTextView.setVisibility(View.GONE);
        } else {
            headerTextView.setVisibility(View.VISIBLE);
        }

    }

    private void applyAlpha(float percentage) {

        float constant = 0.2f;

        headerTextView.setAlpha(PERCENTAGE_TOTAL - (percentage + constant));

        if (percentage > constant) {

            float alpha = (percentage * ALPHA) / 0.5f;

            if (percentage >= 0.95) {

                floatHeaderView.getTitleTextView().setAlpha(1);
                floatHeaderView.getTitleTextView().setPivotX(1);

                floatHeaderView.getTitleTextView().setScaleX(1 + alpha / 2.95f);
                floatHeaderView.getTitleTextView().setScaleY(1 + alpha / 3.1f);
                floatHeaderView.invalidate();

            } else {

                if (alpha > ALPHA) {
                    floatHeaderView.getTitleTextView().setAlpha(alpha);
                }

                floatHeaderView.getTitleTextView().setPivotX(1);
                floatHeaderView.getTitleTextView().setScaleX(1 + alpha / 2.95f);
                floatHeaderView.getTitleTextView().setScaleY(1 + alpha / 3.05f);

                floatHeaderView.getSubTitleTextView().setPivotX(1);
                floatHeaderView.getSubTitleTextView().setScaleX(1 - alpha / 4f);
                floatHeaderView.getSubTitleTextView().setScaleY(1 - alpha / 4f);

                floatHeaderView.invalidate();
            }

            alpha = ALPHA / percentage;

            if (alpha > PERCENTAGE_TOTAL) {
                floatHeaderView.getSubTitleTextView().setAlpha(PERCENTAGE_TOTAL);
            } else if (alpha < ALPHA) {
                floatHeaderView.getSubTitleTextView().setAlpha(ALPHA);
            } else {
                floatHeaderView.getSubTitleTextView().setAlpha(alpha);
            }

        } else {

            floatHeaderView.getTitleTextView().setAlpha(ALPHA);
            floatHeaderView.getSubTitleTextView().setAlpha(PERCENTAGE_TOTAL);
            floatHeaderView.getTitleTextView().setScaleX(1);
            floatHeaderView.getTitleTextView().setScaleY(1);
            floatHeaderView.invalidate();
        }
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
                data.setAction(Constants.EVENT_TRANSACTION_COMPLETE);
                sendBroadcast(data);
                finish();
            } else {
                //transaction failed.
            }

        } else {
            Logger.d(TAG, "failed to send result back " + requestCode);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}