package com.midtrans.sdk.uikit.views.banktransfer.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.activities.BankTransferInstructionActivity;
import com.midtrans.sdk.uikit.adapters.InstructionFragmentPagerAdapter;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.fragments.BankTransferFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Created by ziahaqi on 8/9/17.
 */

public class BankTransferPaymentActivity extends BasePaymentActivity implements BankTransferView {


    private static final String EXTRA_BANK_TYPE = "bank.type";

    public static final String DOWNLOAD_URL = "url";
    public static final String BANK = "bank";
    public static final String TYPE_BCA = "bank.bca";
    public static final String TYPE_BNI = "bank.bni";
    public static final String TYPE_PERMATA = "bank.permata";
    public static final String TYPE_MANDIRI = "bank.mandiri";
    public static final String TYPE_MANDIRI_BILL = "bank.mandiri.bill";
    public static final String TYPE_ALL_BANK = "bank.others";
    public static final String PAGE = "page";
    public static final int KLIKBCA_PAGE = 1;
    private static final int PAGE_MARGIN = 20;
    private static final String TAG = "BankTransferFragment";
    private int POSITION = -1;

    private ViewPager instructionViewPager = null;
    private TabLayout instructionTab = null;
    private TextInputLayout mTextInputEmailId = null;
    private AppCompatEditText mEditTextEmailId = null;
    private DefaultTextView textNotificationToken;
    private DefaultTextView textNotificationOtp;

    private UserDetail userDetail;
    private int bankType = 9;
    private int instructionPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_payment);
        initProperties();
    }

    private void initProperties() {
        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            bankType = data.getIntExtra(EXTRA_BANK_TYPE, Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT);
        } else {
            SdkUIFlowUtil.showToast(BankTransferPaymentActivity.this, getString(R.string.error_something_wrong));
            finish();
        }
    }

    @Override
    public void bindViews() {
        instructionViewPager = (ViewPager) findViewById(R.id.pager_bank_instruction);
        instructionTab = (TabLayout) findViewById(R.id.tab_instructions);
        mTextInputEmailId = (TextInputLayout) findViewById(R.id.container_email);
        mEditTextEmailId = (AppCompatEditText) findViewById(R.id.et_email);
        mTextInputEmailId = (TextInputLayout) findViewById(R.id.email_til);
        textNotificationToken = (DefaultTextView) findViewById(R.id.text_notificationToken);
        textNotificationOtp = (DefaultTextView) findViewById(R.id.text_notificationOtp);
    }

    @Override
    public void initTheme() {

    }


    private void setUpViewPager() {
        instructionViewPager.setPageMargin(PAGE_MARGIN);
        int pageNumber;
        String bankInstruction = getArguments().getString(BANK);
        switch (bankInstruction) {
            case TYPE_BCA:
                pageNumber = 3;
                POSITION = getArguments().getInt(PAGE, -1);

                if (POSITION == KLIKBCA_PAGE) {
                    //track page bca va overview
                    MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_BCA_KLIKBCA_OVERVIEW);
                } else {
                    //track page bca va overview
                    MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_BCA_VA_OVERVIEW);
                }
                break;
            case TYPE_PERMATA:
                pageNumber = 2;

                //track page permata va overview
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_PERMATA_VA_OVERVIEW);
                break;
            case TYPE_MANDIRI:
                pageNumber = 2;
                break;
            case TYPE_BNI:
                pageNumber = 3;

                //track page BNI va overview
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_BNI_VA_OVERVIEW);
                break;
            case TYPE_MANDIRI_BILL:
                pageNumber = 2;

                //track page mandiri bill overview
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_MANDIRI_BILL_OVERVIEW);
                break;
            case TYPE_ALL_BANK:
                pageNumber = 3;

                //track page other bank va overview
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_OTHER_BANK_VA_OVERVIEW);
                break;
            default:
                pageNumber = 0;
                break;
        }
        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(getContext(), bankInstruction, getChildFragmentManager(), pageNumber);
        instructionViewPager.setAdapter(adapter);
        if (POSITION > -1) {
            instructionViewPager.setCurrentItem(POSITION);
        }
    }

    private void setUpTabLayout() {
        instructionTab.setupWithViewPager(instructionViewPager);
        instructionTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                instructionViewPager.setCurrentItem(tab.getPosition());
                initTopNotification(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initTopNotification(int position) {
        if (getArguments() != null) {
            String bank = getArguments().getString(BankTransferInstructionActivity.BANK);
            if (!TextUtils.isEmpty(bank) && bank.equals(BankTransferFragment.TYPE_BCA)) {
                if (position == 1) {
                    showTokenNotification(true);
                } else {
                    showTokenNotification(false);
                }
            } else if (!TextUtils.isEmpty(bank) && bank.equals(BankTransferFragment.TYPE_BNI)) {
                if (position == 1) {
                    showOtpNotification(true);
                } else {
                    showOtpNotification(false);
                }
            } else {
                showOtpNotification(false);
                showTokenNotification(false);
            }
        }
    }

    private void showOtpNotification(boolean show) {
        if (show) {
            textNotificationOtp.setVisibility(View.VISIBLE);
            final Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
            textNotificationOtp.startAnimation(animation);

        } else {
            textNotificationOtp.setVisibility(View.GONE);
            textNotificationOtp.setAnimation(null);
        }
    }

    private void showTokenNotification(boolean show) {
        if (show) {
            textNotificationToken.setVisibility(View.VISIBLE);
            final Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
            textNotificationToken.startAnimation(animation);

        } else {
            textNotificationToken.setVisibility(View.GONE);
            textNotificationToken.setAnimation(null);
        }
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {

    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {

    }

    @Override
    public void onPaymentError(Throwable error) {

    }
}
