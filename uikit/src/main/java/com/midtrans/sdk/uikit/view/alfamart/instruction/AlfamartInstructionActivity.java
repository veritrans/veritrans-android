package com.midtrans.sdk.uikit.view.alfamart.instruction;

import android.content.Intent;
import android.os.Bundle;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AlfamartPaymentResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.view.PaymentListActivity;
import com.midtrans.sdk.uikit.view.alfamart.result.AlfamartResultActivity;

import androidx.annotation.Nullable;

public class AlfamartInstructionActivity extends BasePaymentActivity implements BasePaymentContract {

    private AlfamartInstructionPresenter presenter;
    private AlfamartPaymentResponse alfamartPaymentResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_convenience_store_alfamart_instruction);
        initProperties();
        initActionButton();
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            showProgressLayout(getString(R.string.processing_payment));
            presenter.startAlfamartPayment(paymentInfoResponse.getToken());
        });
        buttonCompletePayment.setText(getString(R.string.confirm_payment));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.alfamart));
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new AlfamartInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        alfamartPaymentResponse = (AlfamartPaymentResponse) response;
        if (alfamartPaymentResponse != null) {
            Intent intent = new Intent(this, AlfamartResultActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_PAYMENT_STATUS, alfamartPaymentResponse);
            intent.putExtra(PaymentListActivity.EXTRA_PAYMENT_INFO, paymentInfoResponse);
            startActivityForResult(intent, Constants.INTENT_CODE_PAYMENT_RESULT);
        } else {
            finishPayment(RESULT_OK, alfamartPaymentResponse);
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
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_CODE_PAYMENT_RESULT) {
            finishPayment(RESULT_OK, alfamartPaymentResponse);
        }
    }

}