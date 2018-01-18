package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseVaPaymentStatusActivity;

/**
 * Created by ziahaqi on 1/18/18.
 */

public class VaOtherBankPaymentStatusActivity extends BaseVaPaymentStatusActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bank_status);
    }

    @Override
    protected void initCompletePaymentButton() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
