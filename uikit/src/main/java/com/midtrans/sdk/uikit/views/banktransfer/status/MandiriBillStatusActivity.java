package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;

/**
 * Created by ziahaqi on 8/15/17.
 */

public class MandiriBillStatusActivity extends BasePaymentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandiri_bill_status);
    }

    @Override
    public void bindViews() {

    }

    @Override
    public void initTheme() {

    }
}
