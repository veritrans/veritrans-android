package com.midtrans.sdk.uikit.views.creditcard.bankpoints;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.Locale;

/**
 * Created by ziahaqi on 7/25/17.
 */

public class BankPointsActivity extends BasePaymentActivity implements BankPointsView {
    public static final String EXTRA_POINT = "point.balance";
    public static final String EXTRA_BANK = "point.bank";

    public static final String EXTRA_DATA_POINT = "point.redeemed";

    private EditText fieldRedeemedPoint;

    private TextView textTotalPoints;
    private TextView textAmountToPay;
    private DefaultTextView textTitle;

    private ImageView imageBankPointLogo;

    private FancyButton buttonRedeemPoint;
    private FancyButton containerAmount;
    private FancyButton containerTotalPoint;

    private BankPointsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        setContentView(R.layout.activity_bank_points);
        initRedeemedPointsField();
        bindValues();
        updateAmountToPayText();
        initRedeemPointButton();
        initBankPointLogoAndTitle();
    }

    private void initPresenter() {
        String pointBank = getIntent().getStringExtra(EXTRA_BANK);
        float pointBalance = getIntent().getFloatExtra(EXTRA_POINT, 0f);
        presenter = new BankPointsPresenter(this, pointBalance, pointBank);
    }


    @Override
    public void bindViews() {
        fieldRedeemedPoint = (AppCompatEditText) findViewById(R.id.redeemed_point_field);
        textTotalPoints = (TextView) findViewById(R.id.text_total_point);
        textAmountToPay = (TextView) findViewById(R.id.text_amount_to_pay);
        textTitle = (DefaultTextView) findViewById(R.id.text_page_title);

        imageBankPointLogo = (ImageView) findViewById(R.id.bank_point_logo);
        buttonRedeemPoint = (FancyButton) findViewById(R.id.btn_redeem_point);
        containerAmount = (FancyButton) findViewById(R.id.container_amount);
        containerTotalPoint = (FancyButton) findViewById(R.id.container_total_point);

    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonRedeemPoint);
        setPrimaryBackgroundColor(buttonRedeemPoint);
        setSecondaryBackgroundColor(containerAmount);
        containerAmount.setAlpha(0.5f);
        setSecondaryBackgroundColor(containerTotalPoint);
        containerTotalPoint.setAlpha(0.5f);

        // Set font into pay now button
        String fonthPath = presenter.getSemiBoldFontPath();
        if (!TextUtils.isEmpty(fonthPath)) {
            buttonRedeemPoint.setCustomTextFont(fonthPath);
        }
    }

    private void initRedeemedPointsField() {
        fieldRedeemedPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputString = editable.toString();
                if (editable.length() == 0) {
                    editable.insert(0, "0");
                } else if (inputString.length() > 1 && inputString.charAt(0) == '0') {
                    editable.delete(0, 1);
                }

                if (presenter.isValidInputPoint(inputString)) {
                    presenter.setLatestValidPoint(inputString);
                } else {
                    fieldRedeemedPoint.setText(presenter.getLatestValidPoint());
                    fieldRedeemedPoint.setSelection(fieldRedeemedPoint.getText().length());
                }
                updateAmountToPayText();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Request focus for edit text
                fieldRedeemedPoint.requestFocus();
                fieldRedeemedPoint.setSelection(fieldRedeemedPoint.getText().toString().length());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(fieldRedeemedPoint, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);
    }

    private void bindValues() {
        String formattedBalance;
        if (presenter.getPointBalance() == (long) presenter.getPointBalance()) {
            formattedBalance = String.format(Locale.getDefault(), "%d", (long) presenter.getPointBalance());
        } else {
            formattedBalance = String.format("%s", presenter.getPointBalance());
        }
        presenter.setLatestValidPoint(formattedBalance);
        presenter.calculateAmount(presenter.getPointBalance());
        fieldRedeemedPoint.setText(formattedBalance);
        textTotalPoints.setText(formattedBalance);
        textAmountToPay.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(presenter.getAmountToPay())));
    }

    private void initBankPointLogoAndTitle() {
        String bank = presenter.getPointBank();
        switch (bank) {
            case BankType.BNI:
                setHeaderTitle(getString(R.string.redeem_bank_point_title, getString(R.string.bank_bni)));
                imageBankPointLogo.setImageResource(R.drawable.bni_badge);
                break;
            default:
                break;
        }
    }

    private void setHeaderTitle(String title) {
        textTitle.setText(title);
    }

    private void initRedeemPointButton() {
        buttonRedeemPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redeemPoint();
            }
        });
    }

    private void updateAmountToPayText() {
        textAmountToPay.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(presenter.getAmountToPay())));
    }

    private void redeemPoint() {
        SdkUIFlowUtil.hideKeyboard(this);
        String strPoint = fieldRedeemedPoint.getText().toString().trim();
        float redeemedPoint = Float.valueOf(strPoint);
        finishBankPoint(redeemedPoint);
    }

    private void finishBankPoint(float redeemedPoint) {
        Intent data = new Intent();
        data.putExtra(EXTRA_DATA_POINT, redeemedPoint);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
