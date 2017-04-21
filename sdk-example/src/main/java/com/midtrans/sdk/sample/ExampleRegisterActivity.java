package com.midtrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;

import java.util.Locale;

/**
 * Created by rakawm on 4/11/17.
 */

public class ExampleRegisterActivity extends AppCompatActivity {

    private EditText cardNumberField;
    private EditText cardExpiryField;
    private EditText cardCvvField;
    private Button registerButton;
    private TextView savedTokenIdText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_register);
        initViews();
        initProgressDialog();
        initRegisterButton();
    }

    private void initViews() {
        cardNumberField = (EditText) findViewById(R.id.card_number_field);
        cardExpiryField = (EditText) findViewById(R.id.card_expiry_field);
        cardCvvField = (EditText) findViewById(R.id.card_cvv_field);
        savedTokenIdText = (TextView) findViewById(R.id.saved_token_id);
        registerButton = (Button) findViewById(R.id.register_card_button);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));
    }

    private void initRegisterButton() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start register card
                String cardNumber = cardNumberField.getText().toString();
                String expiryMonth = cardExpiryField.getText().toString().split("/")[0];
                String expiryYear = cardExpiryField.getText().toString().split("/")[1];
                String cvv = cardCvvField.getText().toString();

                if (isInputValid(cardNumber, expiryMonth, expiryYear, cvv)) {
                    showProgressDialog();
                    MidtransSDK.getInstance().cardRegistration(cardNumber, cvv, expiryMonth, expiryYear, new CardRegistrationCallback() {
                        @Override
                        public void onSuccess(CardRegistrationResponse response) {
                            dismissProgressDialog();
                            savedTokenIdText.setText(buildSavedTokenIdText(response));
                        }

                        @Override
                        public void onFailure(CardRegistrationResponse response, String reason) {
                            dismissProgressDialog();
                            showToast(reason);
                        }

                        @Override
                        public void onError(Throwable error) {
                            dismissProgressDialog();
                            showToast(error.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog() {
        progressDialog.show();;
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String buildSavedTokenIdText(CardRegistrationResponse response) {
        return String.format(Locale.getDefault(), "Masked Card: %1$s.\nSaved Token Id: %2$s", response.getMaskedCard(), response.getSavedTokenId());
    }

    private boolean isInputValid(String cardNumber, String expiryMonth, String expiryYear, String cvv) {
        return !TextUtils.isEmpty(cardNumber) && !TextUtils.isEmpty(expiryMonth)
                && !TextUtils.isEmpty(expiryYear) && !TextUtils.isEmpty(cvv);
    }
}
