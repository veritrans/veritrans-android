package com.midtrans.sdk.uikit.views.banktransfer.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.abstracts.VaInstructionFragment.OnInstructionShownListener;
import com.midtrans.sdk.uikit.adapters.InstructionPagerAdapter;
import com.midtrans.sdk.uikit.adapters.ListBankAdapter;
import com.midtrans.sdk.uikit.models.MessageInfo;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionOtherBankFragment;
import com.midtrans.sdk.uikit.views.banktransfer.status.MandiriBillStatusActivity;
import com.midtrans.sdk.uikit.views.banktransfer.status.VaOtherBankPaymentStatusActivity;
import com.midtrans.sdk.uikit.views.banktransfer.status.VaPaymentStatusActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 8/9/17.
 */

public class BankTransferPaymentActivity extends BasePaymentActivity implements BankTransferPaymentView,
        OnInstructionShownListener {

    public static final String EXTRA_BANK_TYPE = "bank.type";
    private BankTransferPaymentPresenter presenter;

    private ViewPager pagerInstruction;
    private TabLayout tabInstruction;
    private AppCompatEditText editEmail;
    private FancyButton buttonPay, bankToggle;

    private TextInputLayout containerEmail;

    private DefaultTextView textNotificationToken;
    private DefaultTextView textNotificationOtp;

    private String paymentType;
    private String pageName, buttonName;

    //for other ATM network
    private ImageView bankPreview;
    private DefaultTextView bankDescription;
    private DefaultTextView cardDescription;
    private boolean[] flags;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_payment);
        initProperties();
        initTabPager();
        trackPage();
        initPaymentButton();
        initData();
        bindOtherAtmGuidanceView();
    }

    private void initPaymentButton() {
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdkUIFlowUtil.hideKeyboard(BankTransferPaymentActivity.this);

                String email = editEmail.getText().toString().trim();
                if (checkEmailValidity(email)) {
                    showProgressLayout();
                    if (!TextUtils.isEmpty(buttonName)) {
                        presenter.trackButtonClick(buttonName, pageName);
                    }
                    presenter.startPayment(paymentType, email);
                } else {
                    Toast.makeText(BankTransferPaymentActivity.this, getString(R.string.error_invalid_email_id), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonPay.setText(getString(R.string.pay_now));
    }

    private boolean checkEmailValidity(String email) {
        boolean valid = true;

        if (presenter.isEmailValid(email)) {
            containerEmail.setError("");
        } else {
            containerEmail.setError(getString(R.string.error_invalid_email_id));
            valid = false;
        }

        return valid;
    }

    private void initProperties() {
        presenter = new BankTransferPaymentPresenter(this);

        paymentType = getIntent().getStringExtra(EXTRA_BANK_TYPE);
    }

    private void trackPage() {
        switch (paymentType) {
            case PaymentType.BCA_VA:
                buttonName = "Confirm Payment Bank Transfer BCA";
                pageName = "Bank Transfer BCA Overview";
                presenter.trackPageView(pageName, false);
                break;
            case PaymentType.BNI_VA:
                pageName = "Bank Transfer BNI Overview";
                presenter.trackPageView(pageName, false);
                break;
            case PaymentType.E_CHANNEL:
                buttonName = "Confirm Payment Mandiri Bill";
                pageName = "Bank Transfer Mandiri Overview";
                presenter.trackPageView(pageName, false);
                break;
            case PaymentType.PERMATA_VA:
                buttonName = "Confirm Payment Bank Transfer Permata";
                pageName = "Bank Transfer Permata Overview";
                presenter.trackPageView(pageName, false);
                break;
            case PaymentType.ALL_VA:
                buttonName = "Confirm Payment Bank Transfer All Bank";
                pageName = "Bank Transfer Other Overview";
                presenter.trackPageView(pageName, false);
        }
    }

    @Override
    public void bindViews() {
        pagerInstruction = (ViewPager) findViewById(R.id.tab_view_pager);
        tabInstruction = (TabLayout) findViewById(R.id.tab_instructions);
        editEmail = (AppCompatEditText) findViewById(R.id.edit_email);
        buttonPay = (FancyButton) findViewById(R.id.button_primary);

        textNotificationToken = (DefaultTextView) findViewById(R.id.text_notificationToken);
        textNotificationOtp = (DefaultTextView) findViewById(R.id.text_notificationOtp);

        containerEmail = (TextInputLayout) findViewById(R.id.container_email);
    }

    @Override
    public void initTheme() {
        tabInstruction.setSelectedTabIndicatorColor(getPrimaryColor());
        setPrimaryBackgroundColor(buttonPay);
        setBackgroundTintList(editEmail);
        setTextInputlayoutFilter(containerEmail);
    }


    private void initTabPager() {

        tabInstruction.setSelectedTabIndicatorColor(getPrimaryColor());
        pagerInstruction.setPageMargin(getResources().getDimensionPixelSize(R.dimen.twenty_dp));

        int pageNumber;
        String title;
        String bankType = paymentType;
        switch (bankType) {
            case PaymentType.BCA_VA:
                title = getString(R.string.bank_bca_transfer);
                pageNumber = 3;
                break;
            case PaymentType.E_CHANNEL:
                title = getString(R.string.mandiri_bill_transfer);
                pageNumber = 2;
                break;
            case PaymentType.PERMATA_VA:
                title = getString(R.string.bank_permata_transfer);
                pageNumber = 2;
                flags = new boolean[pageNumber];
                break;
            case PaymentType.ALL_VA:
                title = getString(R.string.other_bank_transfer);
                pageNumber = 3;
                flags = new boolean[pageNumber];
                break;
            case PaymentType.BNI_VA:
                title = getString(R.string.bank_bni_transfer);
                pageNumber = 3;
                break;
            default:
                title = getString(R.string.bank_transfer);
                pageNumber = 0;
                break;
        }

        setPageTitle(title);
        final InstructionPagerAdapter adapter = new InstructionPagerAdapter(this, bankType, getSupportFragmentManager(), pageNumber);
        pagerInstruction.setAdapter(adapter);
        final OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getItem(position);
                if (fragment instanceof InstructionOtherBankFragment && flags != null) {
                    showOtherAtmGuidance(((InstructionOtherBankFragment) fragment).getFragmentCode());
                } else {
                    hideOtherAtmGuidance();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        pagerInstruction.addOnPageChangeListener(onPageChangeListener);
        pagerInstruction.post(new Runnable() {
            @Override
            public void run() {
                onPageChangeListener.onPageSelected(pagerInstruction.getCurrentItem());
            }
        });
        setUpTabLayout();
    }

    private void initData() {
        editEmail.setText(presenter.getUserEmail());
        editEmail.clearFocus();
    }


    private void setUpTabLayout() {

        tabInstruction.setupWithViewPager(pagerInstruction);
        tabInstruction.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagerInstruction.setCurrentItem(tab.getPosition());
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

    private void initPaymentStatus(TransactionResponse response) {
        if (!TextUtils.isEmpty(paymentType)) {
            if (paymentType.equals(PaymentType.E_CHANNEL)) {
                showEchannelStatusPage(response);
            } else if (paymentType.equals(PaymentType.ALL_VA)) {
                showOtherVaBankTransferStatusPage(response);
            } else {
                showBankTransferStatusPage(response);
            }
        } else {
            showBankTransferStatusPage(response);
        }

    }

    private void showEchannelStatusPage(TransactionResponse response) {
        Intent intent = new Intent(this, MandiriBillStatusActivity.class);
        intent.putExtra(MandiriBillStatusActivity.EXTRA_PAYMENT_RESULT, response);
        intent.putExtra(MandiriBillStatusActivity.EXTRA_BANK_TYPE, paymentType);
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
    }

    private void showBankTransferStatusPage(TransactionResponse response) {
        Intent intent = new Intent(this, VaPaymentStatusActivity.class);
        intent.putExtra(VaPaymentStatusActivity.EXTRA_PAYMENT_RESULT, response);
        intent.putExtra(VaPaymentStatusActivity.EXTRA_BANK_TYPE, paymentType);
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
    }


    private void showOtherVaBankTransferStatusPage(TransactionResponse response) {
        Intent intent = new Intent(this, VaOtherBankPaymentStatusActivity.class);
        intent.putExtra(VaPaymentStatusActivity.EXTRA_PAYMENT_RESULT, response);
        intent.putExtra(VaPaymentStatusActivity.EXTRA_BANK_TYPE, paymentType);
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
    }

    private void finishPayment(int resultCode) {
        Intent data = new Intent();
        data.putExtra(UiKitConstants.KEY_TRANSACTION_RESPONSE, presenter.getTransactionResponse());
        setResult(resultCode, data);
        finish();
    }

    private void initTopNotification(int position) {
        if (!TextUtils.isEmpty(paymentType)) {
            if (paymentType.equals(PaymentType.BCA_VA)) {
                if (position == 1) {
                    showTokenNotification(true);
                } else {
                    showTokenNotification(false);
                }
            } else if (paymentType.equals(PaymentType.BNI_VA)) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS && resultCode == RESULT_OK) {
            finishPayment(RESULT_OK);
        }
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {

        hideProgressLayout();
        if (!isFinishing()) {
            initPaymentStatus(response);
        } else {
            finishPayment(RESULT_OK);
        }
    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {
        hideProgressLayout();
        if (!isFinishing()) {
            MessageInfo messageInfo = MessageUtil.createPaymentFailedMessage(this, response);
            SdkUIFlowUtil.showToast(this, messageInfo.detailsMessage);

            initPaymentStatus(response);
        }
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusmessage(error);
    }

    @Override
    public void onBankTranferPaymentUnavailable(String bankType) {
        hideProgressLayout();
    }

    /**
     * Setup UI for additional information about other ATM network transfer
     *
     * @param fragmentCode
     */
    public void showOtherAtmGuidance(int fragmentCode) {
        findViewById(R.id.other_atm_guidance).setVisibility(View.VISIBLE);
        final int bankArrayId, dialogTitleId;
        switch (fragmentCode) {
            case UiKitConstants.ATM_BERSAMA:
                bankArrayId = R.array.atm_bersama_banks;
                dialogTitleId = R.string.bank_list_header_atm_bersama;
                bankPreview.setImageResource(R.drawable.bersama_preview);
                bankDescription.setText(R.string.preview_atm_bersama);
                bankToggle.setText(getString(R.string.expand_link_atm_bersama));
                cardDescription.setText(R.string.instruction_card_atm_bersama);
                cardDescription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bersama_atm, 0, 0, 0);
                break;
            case UiKitConstants.PRIMA:
                bankArrayId = R.array.prima_banks;
                dialogTitleId = R.string.bank_list_header_prima;
                bankPreview.setImageResource(R.drawable.prima_preview);
                bankDescription.setText(R.string.preview_prima);
                bankToggle.setText(getString(R.string.expand_link_prima));
                cardDescription.setText(R.string.instruction_card_prima);
                cardDescription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.prima_atm, 0, 0, 0);
                break;
            default:
                bankArrayId = R.array.alto_banks;
                dialogTitleId = R.string.bank_list_header_alto;
                bankPreview.setImageResource(R.drawable.alto_preview);
                bankDescription.setText(R.string.preview_alto);
                bankToggle.setText(getString(R.string.expand_link_alto));
                cardDescription.setText(R.string.instruction_card_alto);
                cardDescription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.alto_atm, 0, 0, 0);
                break;
        }
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null) {
            bankToggle.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            bankToggle.setIconColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor());
        }

        bankToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bank_toggle) {
                    //prepare bank name
                    String[] bankNames = getResources().getStringArray(bankArrayId);
                    ListBankAdapter bankAdapter = new ListBankAdapter(bankNames, v.getContext());

                    final AppCompatDialog dialog = new AppCompatDialog(v.getContext());
                    dialog.setContentView(R.layout.dialog_bank_list);
                    RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.bank_list_items);
                    recyclerView.setAdapter(bankAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
                    recyclerView.setHasFixedSize(true);
                    ((DefaultTextView) dialog.findViewById(R.id.bank_list_title))
                            .setText(getString(dialogTitleId));
                    (dialog.findViewById(R.id.bank_list_ok)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.show();
                }
            }
        });
    }

    private void hideOtherAtmGuidance() {
        findViewById(R.id.other_atm_guidance).setVisibility(View.GONE);
    }

    private void bindOtherAtmGuidanceView() {
        bankPreview = (ImageView) findViewById(R.id.bank_preview);
        bankDescription = (DefaultTextView) findViewById(R.id.bank_description);
        bankToggle = (FancyButton) findViewById(R.id.bank_toggle);
        cardDescription = (DefaultTextView) findViewById(R.id.card_description);
    }

    private void setButtonPayText(String text) {
        buttonPay.setText(text);
        buttonPay.setTextBold();
    }

    @Override
    public void onInstructionShown(boolean isShown, int fragmentCode) {
        //do nothing
    }

    @Override
    public void onBackPressed() {
        if (presenter != null && !TextUtils.isEmpty(pageName)) {
            presenter.trackBackButtonClick(pageName);
        }
        super.onBackPressed();
    }
}
