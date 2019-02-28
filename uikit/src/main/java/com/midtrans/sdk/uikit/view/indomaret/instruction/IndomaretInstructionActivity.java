package com.midtrans.sdk.uikit.view.indomaret.instruction;

import android.content.Intent;
import android.os.Bundle;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.view.PaymentListActivity;
import com.midtrans.sdk.uikit.view.indomaret.result.IndomaretResultActivity;

import androidx.annotation.Nullable;

public class IndomaretInstructionActivity extends BasePaymentActivity implements BasePaymentContract {

    private IndomaretInstructionPresenter presenter;
    private IndomaretPaymentResponse indomaretPaymentResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_convenience_store_indomaret_instruction);
        initProperties();
        initActionButton();
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            showProgressLayout(getString(R.string.processing_payment));
            presenter.startIndomaretPayment(paymentInfoResponse.getToken());
        });
        buttonCompletePayment.setText(getString(R.string.confirm_payment));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.payment_method_indomaret));
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new IndomaretInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        indomaretPaymentResponse = (IndomaretPaymentResponse) response;
        if (indomaretPaymentResponse != null) {
            Intent intent = new Intent(this, IndomaretResultActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_PAYMENT_STATUS, indomaretPaymentResponse);
            intent.putExtra(PaymentListActivity.EXTRA_PAYMENT_INFO, paymentInfoResponse);
            startActivityForResult(intent, Constants.INTENT_CODE_PAYMENT_RESULT);
        } else {
            finishPayment(RESULT_OK, indomaretPaymentResponse);
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
            finishPayment(RESULT_OK, indomaretPaymentResponse);
        }
    }

}