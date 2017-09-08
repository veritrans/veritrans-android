package com.midtrans.sdk.uikit.views.gopay.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 9/7/17.
 */

public class GoPayPaymentActivity extends BasePaymentActivity implements GoPayPaymentView {

    private static final String TAG = GoPayPaymentActivity.class.getSimpleName();

    private TextInputLayout containerCountryCode;
    private TextInputLayout containerPhoneNumber;

    private TextInputEditText fieldCountryCode;
    private TextInputEditText fieldPhoneNumber;

    private DefaultTextView textNotificationInfo;
    private FancyButton buttonContinue;


    private GopayPaymentPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopay_payment);
        initProperties();
        initActionButton();

    }

    private void initActionButton() {
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String countryCode = fieldCountryCode.getText().toString().trim();
                String phoneNumber = fieldPhoneNumber.getText().toString().trim();

            }
        });
    }

    @Override
    public void bindViews() {
        containerCountryCode = (TextInputLayout) findViewById(R.id.container_input_country_code);
        containerPhoneNumber = (TextInputLayout) findViewById(R.id.container_input_phone_number);

        fieldCountryCode = (TextInputEditText) findViewById(R.id.edit_country_code);
        fieldPhoneNumber = (TextInputEditText) findViewById(R.id.edit_phone_number);

        buttonContinue = (FancyButton)findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {

    }

    private void initProperties() {
        presenter = new GopayPaymentPresenter(this);
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
