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
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.adapters.PaymentMethodsAdapter;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.utilities.Utils;
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
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private ArrayList<PaymentMethodsModel> data = new ArrayList<>();
    //Views
    private Toolbar mToolbar = null;
    private TextViewFont mSubTitle = null;
    private TextViewFont mTextViewAmountExpanded = null;
    private TextViewFont mTitle = null;
    private LinearLayout mTitleContainer = null;
    private AppBarLayout mAppBarLayout = null;
    private FrameLayout mFrameParallax = null;
    private RecyclerView mRecyclerView = null;


    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_method);
        setUpPaymentMethods();
    }

    private void setUpPaymentMethods() {

        //initialize views
        bindActivity();

        //setup tool bar
        mToolbar.setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //hide subtitle
        startAlphaAnimation(mSubTitle, 0, View.INVISIBLE);

        initParallaxValues();

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

            mSubTitle.setText(amount);
            mTextViewAmountExpanded.setText(amount);
        }

    }

    private void bindActivity() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_payment_methods);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextViewFont) findViewById(R.id.main_textview_title);
        mSubTitle = (TextViewFont) findViewById(R.id.main_textview_subtitle);
        mTextViewAmountExpanded = (TextViewFont) findViewById(R.id.text_amount_expanded);

        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mFrameParallax = (FrameLayout) findViewById(R.id.main_framelayout_title);
       // mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
    }

    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFrameParallax.getLayoutParams();
        petBackgroundLp.setParallaxMultiplier(0.7f);
        mFrameParallax.setLayoutParams(petBackgroundLp);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mSubTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mSubTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
                //            startTranslateAnimation(false);
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
                //              startTranslateAnimation(true);
            }
        }
    }

   /* public void startTranslateAnimation(boolean makeViewLarge) {
        if (makeViewLarge) {
            Animation animation = new ScaleAnimation(0, 480, 0, 0);
            animation.setDuration(ALPHA_ANIMATIONS_DURATION);
            mTitle.startAnimation(animation);
        }
    }
*/

    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseAdapterData() {

        String[] names = getResources().getStringArray(R.array.payment_methods);
        Logger.d(TAG, "there are total " + names.length + " payment methods available.");

        int[] paymentImageList = getImageList();

        for (int i = 0; i < names.length; i++) {
            PaymentMethodsModel model = new PaymentMethodsModel(names[i], paymentImageList[i],
                    Constants.PAYMENT_METHOD_NOT_SELECTED);
            data.add(model);
        }

    }


    private int[] getImageList() {

        int[] paymentImageList = new int[11];

        paymentImageList[0] = R.drawable.ic_offers;
        paymentImageList[1] = R.drawable.ic_credit;
        paymentImageList[2] = R.drawable.ic_mandiri;
        paymentImageList[3] = R.drawable.ic_cimb;
        paymentImageList[4] = R.drawable.ic_epay;
        paymentImageList[5] = R.drawable.ic_bbm;
        paymentImageList[6] = R.drawable.ic_indosat;
        paymentImageList[7] = R.drawable.ic_mandiri_e_cash; // mandiri e-Cash
        paymentImageList[8] = R.drawable.ic_banktransfer;
        paymentImageList[9] = R.drawable.ic_mandiri_bill_payment;
        paymentImageList[10] = R.drawable.ic_indomaret;

        return paymentImageList;
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
}