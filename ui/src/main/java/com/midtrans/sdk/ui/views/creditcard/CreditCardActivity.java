package com.midtrans.sdk.ui.views.creditcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Payment;
import com.midtrans.sdk.ui.models.CreditCardDetails;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.thirdparty.ExternalScanner;
import com.midtrans.sdk.ui.thirdparty.ScannerModel;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.utils.Utils;
import com.midtrans.sdk.ui.views.PaymentStatusFragment;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by ziahaqi on 2/22/17.
 */

public class CreditCardActivity extends BaseActivity {

    private CreditCardPresenter presenter;

    private TextView tvHeaderTitle;
    private DefaultTextView tvTotalAmount;
    private FancyButton buttonBack;
    private ImageView ivDeleteSavedCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        initProperties();
        bindView();
        initThemeColor();
        setupView();
        initDefaultState();
    }

    private void initDefaultState() {
        setViewTotalAmount(MidtransUi.getInstance().getCheckoutTokenRequest().transactionDetails.grossAmount);

        if (presenter.isNormalMode()) {
            showCreditCardDetailFragment(null);
        } else {
            presenter.getSavedCards();
        }
    }


    private void setupView() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void bindView() {
        layoutTotalAmount = (RelativeLayout) findViewById(R.id.layout_total_amount);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        tvHeaderTitle = (TextView) findViewById(R.id.text_title);
        tvTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
        buttonBack = (FancyButton) findViewById(R.id.btn_back);
        ivDeleteSavedCard = (ImageView) findViewById(R.id.image_saved_card_delete);
    }

    private void initProperties() {
        presenter = new CreditCardPresenter(this);
    }

    private void showCreditCardDetailFragment(CreditCardDetails model) {
        if (model != null && model.hasSavedToken()) {
            showDeleteCardIcon(true);
        } else {
            showDeleteCardIcon(false);
        }

        CreditCardDetailsFragment addCreditCardDetailsFragment = CreditCardDetailsFragment.newInstance(model);
        replaceFragment(addCreditCardDetailsFragment, R.id.layout_container, true, false);

        presenter.setCardDetailView(addCreditCardDetailsFragment);
        setHeaderTitle(getString(R.string.card_details));
    }

    public void showDeleteCardIcon(boolean show) {
        if (show) {
            ivDeleteSavedCard.setVisibility(View.VISIBLE);
        } else {
            ivDeleteSavedCard.setVisibility(View.INVISIBLE);
        }
    }


    private void showSavedCreditCardFragment() {
        SavedCreditCardsFragment savedCreditCardsFragment = SavedCreditCardsFragment.newInstance();
        replaceFragment(savedCreditCardsFragment, R.id.layout_container, true, false);

        presenter.setSavedCreditCardsView(savedCreditCardsFragment);
        setHeaderTitle(getString(R.string.saved_card));
    }

    public void setHeaderTitle(String headerTitle) {
        this.tvHeaderTitle.setText(headerTitle);
    }


    public void setViewTotalAmount(double amount) {
        tvTotalAmount.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(amount)));
    }


    public CreditCardPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(TAG, "reqCode:" + requestCode + " | res:" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.IntentCode.PAYMENT_WEB_INTENT) {
                UiUtils.showProgressDialog(this, getString(R.string.processing_payment), false);
                presenter.payUsingCard();
            } else if (requestCode == Constants.IntentCode.SCAN_REQUEST_CODE) {
                if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {

                    ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
                    Logger.d(TAG, String.format("Card Number: %s, Card Expire: %s/%d", scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                    updateCreditCardData(Utils.getFormattedCreditCardNumber(scanData.getCardNumber()), scanData.getCvv(), String.format("%s/%d", scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                } else {
                    Logger.d(TAG, "No result");
                }
            } else {
                Logger.d(TAG, "scancard:Not available");
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == Constants.IntentCode.PAYMENT_WEB_INTENT) {
                UiUtils.hideProgressDialog();
                String errorMessage = getString(R.string.payment_canceled);
                initPaymentResult(new PaymentResult(errorMessage), Payment.Type.CREDIT_CARD);
            } else if (requestCode == Constants.IntentCode.SCAN_REQUEST_CODE) {
                Logger.d(TAG, "scancard:canceled");
            }
        }
    }

    private void updateCreditCardData(String cardNumber, String cvv, String expired) {
        // Update credit card data in AddCardDetailsFragment
        presenter.setCardDetailFromScanner(cardNumber, cvv, expired);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment != null) {
            Logger.d(TAG, "currentFragment:" + currentFragment);
            if (currentFragment.equals(PaymentStatusFragment.class)) {
                setResultCode(RESULT_OK);
                completePayment(presenter.getPaymentResult);
            } else if (currentFragment.equals(CreditCardDetailsFragment.class)) {
                super.onBackPressed();
            }
        } else {
            setResultCode(RESULT_CANCELED);
            completePayment(null);
        }
    }
}
