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

public abstract class BaseStatusActivity extends BaseItemDetailsActivity {
    private FancyButton finishButton;

    @Override
    protected void init() {
        super.init();
        initFinishButtonViews();
        initFinishButtonClick();
        initFinishButtonColorTheme();
    }

    protected abstract void finishPayment();

    private void initFinishButtonViews() {
        finishButton = (FancyButton) findViewById(R.id.btn_finish);
    }

    private void initFinishButtonClick() {
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishPayment();
            }
        });
    }

    private void initFinishButtonColorTheme() {
        setBackgroundColor(finishButton, Theme.PRIMARY_COLOR);

        if (!TextUtils.isEmpty(MidtransUi.getInstance().getSemiBoldFontPath())) {
            finishButton.setCustomTextFont(MidtransUi.getInstance().getSemiBoldFontPath());
        }
    }
}
