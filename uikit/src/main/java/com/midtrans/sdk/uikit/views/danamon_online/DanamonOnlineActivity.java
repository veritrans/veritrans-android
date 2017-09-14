package com.midtrans.sdk.uikit.views.danamon_online;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;
import com.midtrans.sdk.uikit.models.MessageInfo;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.status.PaymentStatusActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 9/13/17.
 */

public class DanamonOnlineActivity extends BasePaymentActivity implements BasePaymentView {

    private DanamonOnlinePresenter presenter;
    private FancyButton buttonPrimary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danamon_online);
        initProperties();
        initActionButton();
    }

    private void initActionButton() {
        buttonPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startPayment();
            }
        });
    }

    private void initProperties() {
        this.presenter = new DanamonOnlinePresenter(this);
    }

    private void showStatusPage(TransactionResponse response) {
        if (isActivityRunning()) {
            if (presenter.isShowPaymentStatusPage()) {
                Intent intent = new Intent(this, PaymentStatusActivity.class);
                intent.putExtra(PaymentStatusActivity.EXTRA_PAYMENT_RESULT, response);
                startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
            } else {
                finishPayment(RESULT_OK);
            }
        } else {
            finish();
        }
    }

    private void finishPayment(int resultCode) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.transaction_response), presenter.getTransactionResponse());
        setResult(resultCode, intent);
    }

    @Override
    public void bindViews() {
        buttonPrimary = (FancyButton) findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPrimary);
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        showStatusPage(response);
    }


    @Override
    public void onPaymentFailure(TransactionResponse response) {
        showStatusPage(response);
    }

    @Override
    public void onPaymentError(Throwable error) {
        if (isActivityRunning()) {
            MessageInfo messageInfo = MessageUtil.createMessageOnError(this, error, null);
            SdkUIFlowUtil.showToast(this, messageInfo.detailsMessage);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
            finishPayment(RESULT_OK);
        }
    }
}
