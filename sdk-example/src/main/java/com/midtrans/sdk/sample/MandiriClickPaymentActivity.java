package com.midtrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;

public class MandiriClickPaymentActivity extends AppCompatActivity {
    TextInputLayout cardNumberContainer, tokenContainer;
    EditText cardNumber, challengeToken;
    Button payBtn;
    ProgressDialog dialog;
    private String sampleMandiriCardNumber = "4111111111111111";
    private String sampleTokenResponse = "000000";
    private String input3 = "59478";
    private String sampleInput1 = "13123";
    private String sampleInput2 = "11231";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandiri_click_payment);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        //Initialize TextInputLayout
        tokenContainer = (TextInputLayout) findViewById(R.id.challenge_token_container);
        cardNumberContainer = (TextInputLayout) findViewById(R.id.card_number_container);
        //Initialize progress dialog
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");
        //Initialize EditText
        challengeToken = (EditText) findViewById(R.id.challenge_token);
        cardNumber = (EditText) findViewById(R.id.card_number);
        //Initialize Pay Button
        payBtn = (Button) findViewById(R.id.btn_payment);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh validator
                refreshView();
                if (inputValidator()) {
                    dialog.show();

                    MidtransSDK.getInstance().paymentUsingMandiriClickPay(
                            MidtransSDK.getInstance().readAuthenticationToken(),
                            sampleMandiriCardNumber, sampleTokenResponse, input3, new TransactionCallback() {
                                @Override
                                public void onSuccess(TransactionResponse response) {
                                    actionTransactionSuccess(response);
                                }

                                @Override
                                public void onFailure(TransactionResponse response, String reason) {
                                    actionTransactionFailure(response, reason);
                                }

                                @Override
                                public void onError(Throwable error) {
                                    actionTransactionError(error);
                                }
                            });
                }
            }
        });
    }

    private void refreshView() {
        // Check card number
        if (cardNumber.getText().toString().isEmpty()) {
            cardNumberContainer.setError("Must not be empty.");
        } else {
            if (cardNumber.getText().length() == 16) {
                cardNumberContainer.setError(null);
            } else {
                cardNumberContainer.setError("Must be 16 digits.");
            }
        }

        // Check cvv number
        if (challengeToken.getText().toString().isEmpty()) {
            challengeToken.setError("Must not be empty.");
        } else {
            if (challengeToken.getText().toString().length() == 6) {
                challengeToken.setError(null);
            } else {
                challengeToken.setError("Must be 6 digits.");
            }
        }
    }

    /**
     * @return if all input fields is not empty
     */
    private boolean inputValidator() {
        return !cardNumber.getText().toString().isEmpty()
                && cardNumber.getText().toString().length() == 16
                && !challengeToken.getText().toString().isEmpty()
                && challengeToken.getText().toString().length() == 6;
    }

    private void actionTransactionError(Throwable error) {
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Unknown error: " + error.getMessage())
                .create();
        dialog.show();
    }

    private void actionTransactionFailure(TransactionResponse response, String reason) {
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(reason)
                .create();
        dialog.show();
    }

    private void actionTransactionSuccess(TransactionResponse response) {
        // Handle success transaction
        dialog.dismiss();
        Toast.makeText(this, "transaction successfull (" + response.getStatusMessage() + ")", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }
}
