package com.midtrans.sdk.ui.abtracts;

import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by rakawm on 4/18/17.
 */

public abstract class BasePaymentActivity extends BaseItemDetailsActivity {

    private FancyButton payButton;
    private FancyButton confirmPaymentButton;

    @Override
    protected void init() {
        super.init();
        initPaymentButtonViews();
        initPaymentButtonClick();
        initPaymentButtonThemeColor();
    }

    protected abstract void startPayment();

    protected abstract boolean validatePayment();

    protected void initPaymentButtonViews() {
        payButton = (FancyButton) findViewById(R.id.btn_pay_now);
        confirmPaymentButton = (FancyButton) findViewById(R.id.btn_confirm_payment);
    }

    private void initPaymentButtonClick() {
        View.OnClickListener buttonPaymentListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPaymentButton(view)
                        && validatePayment()) {
                    startPayment();
                }
            }
        };

        if (payButton != null) {
            payButton.setOnClickListener(buttonPaymentListener);
        }

        if (confirmPaymentButton != null) {
            confirmPaymentButton.setOnClickListener(buttonPaymentListener);
        }
    }

    private void initPaymentButtonThemeColor() {
        if (payButton != null) {
            setBackgroundColor(payButton, Theme.PRIMARY_COLOR);

            if (!TextUtils.isEmpty(MidtransUi.getInstance().getSemiBoldFontPath())) {
                payButton.setCustomTextFont(MidtransUi.getInstance().getSemiBoldFontPath());
            }
        }
        if (confirmPaymentButton != null) {
            setBackgroundColor(confirmPaymentButton, Theme.PRIMARY_COLOR);

            if (!TextUtils.isEmpty(MidtransUi.getInstance().getSemiBoldFontPath())) {
                confirmPaymentButton.setCustomTextFont(MidtransUi.getInstance().getSemiBoldFontPath());
            }
        }
    }

    private boolean isPaymentButton(View view) {
        return view.getId() == R.id.btn_pay_now
                || view.getId() == R.id.btn_confirm_payment;
    }
}
