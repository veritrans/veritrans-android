package com.midtrans.sdk.uikit.view.banktransfer.instruction;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BniBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriBillResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.OtherBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PermataBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantPreferences;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.MidtransKitConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BaseActivity;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.Helper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.PaymentListActivity;
import com.midtrans.sdk.uikit.view.banktransfer.instruction.adapter.ListBankAdapter;
import com.midtrans.sdk.uikit.view.banktransfer.instruction.instruction.adapter.InstructionPagerAdapter;
import com.midtrans.sdk.uikit.view.banktransfer.instruction.instruction.fragment.InstructionVaOtherFragment;
import com.midtrans.sdk.uikit.view.banktransfer.result.BankPaymentResultActivity;
import com.midtrans.sdk.uikit.view.banktransfer.result.MandiriBillPaymentResultActivity;
import com.midtrans.sdk.uikit.view.banktransfer.result.OtherBankPaymentResultActivity;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import okhttp3.ResponseBody;

public class BankTransferInstructionActivity extends BaseActivity implements BankTransferInstructionContract {

    public static final String EXTRA_BANK_TYPE = "intent.extra.bank.type";
    public static final String EXTRA_PAYMENT_INFO = "intent.extra.payment.info";
    private BankTransferInstructionPresenter presenter;

    private ViewPager pagerInstruction;
    private TabLayout tabInstruction;
    private AppCompatEditText editEmail;
    private FancyButton buttonPay, bankToggle;

    private TextInputLayout containerEmail;

    private DefaultTextView textNotificationToken;
    private DefaultTextView textNotificationOtp;

    private Toolbar toolbar;
    private TextView merchantNameInToolbar;
    private TextView paymentMethodTitleInToolbar;
    private ImageView merchantLogoInToolbar;
    private PaymentInfoResponse paymentInfoResponse;

    private String paymentType;

    //for other ATM network
    private ImageView bankPreview;
    private DefaultTextView bankDescription;
    private DefaultTextView cardDescription;
    private boolean[] flags;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_bank_transfer_instruction);
        getIntentData();
        initToolbarAndView();
        initMerchantPreferences();
        initializeTheme();
        initProperties();
        initTabPager();
        initData();
        initOtherAtmGuidanceView();
        initItemDetails(paymentInfoResponse);
        initPaymentButton();
    }

    /**
     * This method used for binding view and setup other view stuff like toolbar and progress image
     */
    private void initToolbarAndView() {
        toolbar = findViewById(R.id.toolbar_base);
        merchantLogoInToolbar = findViewById(R.id.image_view_merchant_logo);
        merchantNameInToolbar = findViewById(R.id.text_view_merchant_name);
        paymentMethodTitleInToolbar = findViewById(R.id.text_view_page_title);

        pagerInstruction = findViewById(R.id.tab_view_pager);
        tabInstruction = findViewById(R.id.tab_instructions);
        editEmail = findViewById(R.id.edit_email);
        buttonPay = findViewById(R.id.button_primary);

        textNotificationToken = findViewById(R.id.text_notificationToken);
        textNotificationOtp = findViewById(R.id.text_notificationOtp);

        containerEmail = findViewById(R.id.container_email);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * This method used for getting intent extra from MidtransKitFlow
     * later it can be used for making direct payment, callback, and deciding sdk process
     */
    @SuppressWarnings("unchecked")
    private void getIntentData() {
        paymentType = getIntent().getStringExtra(EXTRA_BANK_TYPE);
        paymentInfoResponse = (PaymentInfoResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_INFO);
    }

    private void initProperties() {
        presenter = new BankTransferInstructionPresenter(this, paymentInfoResponse);
    }

    /**
     * This method use for setup view stuff based on response and merchant preferences
     */
    private void initMerchantPreferences() {
        MerchantPreferences preferences = paymentInfoResponse.getMerchantData().getPreference();
        if (!TextUtils.isEmpty(preferences.getDisplayName())) {
            merchantNameInToolbar.setText(preferences.getDisplayName());
            merchantNameInToolbar.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(preferences.getLogoUrl())) {
            Ion.with(merchantLogoInToolbar)
                    .load(preferences.getLogoUrl());
            merchantLogoInToolbar.setVisibility(View.VISIBLE);
            merchantNameInToolbar.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(paymentType)) {
            paymentMethodTitleInToolbar.setText(PaymentListHelper.mappingPaymentTitle(this, paymentType));
            paymentMethodTitleInToolbar.setVisibility(View.VISIBLE);
        }
    }

    private void initPaymentButton() {
        buttonPay.setOnClickListener(v -> {
            Helper.hideKeyboard(BankTransferInstructionActivity.this);

            String email = editEmail.getText().toString().trim();
            if (checkEmailValidity(email)) {
                showProgressLayout();
                presenter.startPayment(paymentInfoResponse.getToken(), paymentType, email);
            } else {
                Toast.makeText(BankTransferInstructionActivity.this, getString(R.string.error_invalid_email_id), Toast.LENGTH_SHORT).show();
            }
        });
        buttonPay.setText(getString(R.string.pay_now));
        buttonPay.setBackgroundColor(getPrimaryColor());
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
            case PaymentType.ECHANNEL:
                title = getString(R.string.mandiri_bill_transfer);
                pageNumber = 2;
                break;
            case PaymentType.PERMATA_VA:
                title = getString(R.string.bank_permata_transfer);
                pageNumber = 2;
                flags = new boolean[pageNumber];
                break;
            case PaymentType.OTHER_VA:
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

        paymentMethodTitleInToolbar.setText(title);

        final InstructionPagerAdapter adapter = new InstructionPagerAdapter(
                this,
                bankType,
                getSupportFragmentManager(),
                pageNumber,
                paymentInfoResponse
        );
        pagerInstruction.setAdapter(adapter);
        final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getItem(position);
                if (fragment instanceof InstructionVaOtherFragment && flags != null) {
                    showOtherAtmGuidance(((InstructionVaOtherFragment) fragment).getFragmentCode());
                } else {
                    hideOtherAtmGuidance();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        pagerInstruction.addOnPageChangeListener(onPageChangeListener);
        pagerInstruction.post(() -> onPageChangeListener.onPageSelected(pagerInstruction.getCurrentItem()));
        setUpTabLayout();
    }

    private void initData() {
        editEmail.setText(presenter.getUserEmail());
        editEmail.clearFocus();
    }


    /**
     * Setup UI for additional information about other ATM network transfer
     */
    public void showOtherAtmGuidance(int fragmentCode) {
        findViewById(R.id.other_atm_guidance).setVisibility(View.VISIBLE);
        final int bankArrayId, dialogTitleId;
        switch (fragmentCode) {
            case Constants.ATM_BERSAMA:
                bankArrayId = R.array.atm_bersama_banks;
                dialogTitleId = R.string.bank_list_header_atm_bersama;
                bankPreview.setImageResource(R.drawable.bersama_preview);
                bankDescription.setText(R.string.preview_atm_bersama);
                bankToggle.setText(getString(R.string.expand_link_atm_bersama));
                cardDescription.setText(R.string.instruction_card_atm_bersama);
                cardDescription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bersama_atm, 0, 0, 0);
                break;
            case Constants.PRIMA:
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
        MidtransKitConfig midtransSDK = MidtransKit.getInstance().getMidtransKitConfig();
        if (midtransSDK != null) {
            bankToggle.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            bankToggle.setIconColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor());
        }

        bankToggle.setOnClickListener(v -> {
            if (v.getId() == R.id.bank_toggle) {
                //prepare bank name
                List<String> bankNames = Arrays.asList(getResources().getStringArray(bankArrayId));
                ListBankAdapter bankAdapter = new ListBankAdapter(v.getContext());

                final AppCompatDialog dialog = new AppCompatDialog(v.getContext());
                dialog.setContentView(R.layout.layout_dialog_bank_list);
                RecyclerView recyclerView = dialog.findViewById(R.id.bank_list_items);
                recyclerView.setAdapter(bankAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
                recyclerView.setHasFixedSize(true);
                bankAdapter.setData(bankNames);
                ((DefaultTextView) dialog.findViewById(R.id.bank_list_title)).setText(getString(dialogTitleId));
                (dialog.findViewById(R.id.bank_list_ok)).setOnClickListener(v1 -> dialog.dismiss());
                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }

    private void hideOtherAtmGuidance() {
        findViewById(R.id.other_atm_guidance).setVisibility(View.GONE);
    }

    private void initOtherAtmGuidanceView() {
        bankPreview = findViewById(R.id.bank_preview);
        bankDescription = findViewById(R.id.bank_description);
        bankToggle = findViewById(R.id.bank_toggle);
        cardDescription = findViewById(R.id.card_description);
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

    private void initTopNotification(int position) {
        if (!TextUtils.isEmpty(paymentType)) {
            switch (paymentType) {
                case PaymentType.BCA_VA:
                    if (position == 1) {
                        showTokenNotification(true);
                    } else {
                        showTokenNotification(false);
                    }
                    break;
                case PaymentType.BNI_VA:
                    if (position == 1) {
                        showOtpNotification(true);
                    } else {
                        showOtpNotification(false);
                    }
                    break;
                default:
                    showOtpNotification(false);
                    showTokenNotification(false);
                    break;
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
    public void onBankTransferPaymentUnavailable(String bankType) {
        hideProgressLayout();
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        Intent intentNormalBank = new Intent(this, BankPaymentResultActivity.class);
        intentNormalBank.putExtra(PaymentListActivity.EXTRA_PAYMENT_INFO, paymentInfoResponse);
        intentNormalBank.putExtra(BankPaymentResultActivity.EXTRA_BANK_TYPE, paymentType);
        if (response instanceof PermataBankTransferResponse) {
            PermataBankTransferResponse paymentResponse = (PermataBankTransferResponse) response;
            intentNormalBank.putExtra(BankPaymentResultActivity.EXTRA_PAYMENT_RESULT_PERMATA, paymentResponse);
            startActivityForResult(intentNormalBank, Constants.INTENT_CODE_PAYMENT_STATUS);
        } else if (response instanceof BniBankTransferResponse) {
            BniBankTransferResponse paymentResponse = (BniBankTransferResponse) response;
            intentNormalBank.putExtra(BankPaymentResultActivity.EXTRA_PAYMENT_RESULT_BNI, paymentResponse);
            startActivityForResult(intentNormalBank, Constants.INTENT_CODE_PAYMENT_STATUS);
        } else if (response instanceof BcaBankTransferReponse) {
            BcaBankTransferReponse paymentResponse = (BcaBankTransferReponse) response;
            intentNormalBank.putExtra(BankPaymentResultActivity.EXTRA_PAYMENT_RESULT_BCA, paymentResponse);
            startActivityForResult(intentNormalBank, Constants.INTENT_CODE_PAYMENT_STATUS);
        } else if (response instanceof MandiriBillResponse) {
            MandiriBillResponse paymentResponse = (MandiriBillResponse) response;
            Intent mandiriBill = new Intent(this, MandiriBillPaymentResultActivity.class);
            mandiriBill.putExtra(PaymentListActivity.EXTRA_PAYMENT_INFO, paymentInfoResponse);
            mandiriBill.putExtra(BankPaymentResultActivity.EXTRA_BANK_TYPE, paymentType);
            mandiriBill.putExtra(BankPaymentResultActivity.EXTRA_PAYMENT_RESULT_MANDIRI, paymentResponse);
            startActivityForResult(mandiriBill, Constants.INTENT_CODE_PAYMENT_STATUS);
        } else {
            ResponseBody responseFromServer = (ResponseBody) response;
            try {
                OtherBankTransferResponse paymentResponse = Helper.parseToModel(responseFromServer.string(), OtherBankTransferResponse.class);
                Intent otherBank = new Intent(this, OtherBankPaymentResultActivity.class);
                otherBank.putExtra(PaymentListActivity.EXTRA_PAYMENT_INFO, paymentInfoResponse);
                otherBank.putExtra(BankPaymentResultActivity.EXTRA_BANK_TYPE, paymentType);
                otherBank.putExtra(BankPaymentResultActivity.EXTRA_PAYMENT_RESULT_OTHER, paymentResponse);
                startActivityForResult(otherBank, Constants.INTENT_CODE_PAYMENT_STATUS);
            } catch (IOException exception) {
                Logger.error("Failed parsing other transfer >>> " + exception);
            } catch (Exception exception) {
                Logger.error("Failed other transfer response >>> " + exception);
            }
        }
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusMessage(error);
    }

    @Override
    public void onNullInstanceSdk() {
        setResult(Constants.RESULT_SDK_NOT_AVAILABLE);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_CODE_PAYMENT_STATUS && resultCode == RESULT_OK) {
            finishPayment(RESULT_OK, data);
        }
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        onBackPressed();
    }
}