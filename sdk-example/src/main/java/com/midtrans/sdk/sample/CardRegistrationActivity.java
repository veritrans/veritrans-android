package com.midtrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;

import java.util.ArrayList;

public class CardRegistrationActivity extends AppCompatActivity{
    TextInputLayout cardNumberContainer, cvvContainer, expiredDateContainer;
    EditText cardNumber, cvv, expiredDate;
    Button saveBtn;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_registration);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        //Initialize TextInputLayout
        cardNumberContainer = (TextInputLayout) findViewById(R.id.card_number_container);
        expiredDateContainer = (TextInputLayout) findViewById(R.id.exp_date_container);
        cvvContainer = (TextInputLayout) findViewById(R.id.cvv_number_container);
        //Initialize progress dialog
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");
        //Initialize EditText
        cardNumber = (EditText)findViewById(R.id.card_number);
        cvv = (EditText)findViewById(R.id.cvv_number);
        expiredDate = (EditText)findViewById(R.id.exp_date);
        saveBtn = (Button)findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh validator
                refreshView();
                if (inputValidator()) {
                    dialog.show();
                    // Create token request before payment
                    String date = expiredDate.getText().toString();
                    MidtransSDK.getInstance().cardRegistration(cardNumber.getText().toString(),
                            cvv.getText().toString(),
                            date.split("/")[0],
                            "20" + date.split("/")[1], new CardRegistrationCallback() {
                                @Override
                                public void onSuccess(CardRegistrationResponse response) {
                                    // Handle card registration success
                                    SaveCardRequest request = new SaveCardRequest();
                                    request.setCode(response.getStatusCode());
                                    request.setSavedTokenId(response.getSavedTokenId());
                                    request.setMaskedCard(response.getMaskedCard());
                                    request.setTransactionId(response.getTransactionId());
                                    ArrayList<SaveCardRequest> saveCardsRequests = new ArrayList<>();
                                    SaveCardRequest req = new SaveCardRequest(response.getSavedTokenId(),
                                            response.getMaskedCard(), "visa");
                                    saveCardsRequests.add(req);
                                    saveCreditCards(saveCardsRequests);
                                }

                                @Override
                                public void onFailure(CardRegistrationResponse response, String reason) {
                                    // Handle card registration failed
                                    dialog.dismiss();
                                    AlertDialog dialog = new AlertDialog.Builder(CardRegistrationActivity.this)
                                            .setMessage(reason)
                                            .create();
                                    dialog.show();
                                }

                                @Override
                                public void onError(Throwable error) {
                                    actionOnError(error);
                                }
                            });
                }
            }
        });
    }

    private void actionOnError(Throwable error) {
        // Handle generic error condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Unknown error: " + error.getMessage())
                .create();
        dialog.show();
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
        if (cvv.getText().toString().isEmpty()) {
            cvvContainer.setError("Must not be empty.");
        } else {
            if (cvv.getText().toString().length() == 3) {
                cvvContainer.setError(null);
            } else {
                cvvContainer.setError("Must be 3 digits.");
            }
        }

        // Check exp date
        if (expiredDate.getText().toString().isEmpty()) {
            expiredDateContainer.setError("Must not be empty.");
        } else {
            if (expiredDate.getText().toString().split("/").length == 2) {
                expiredDateContainer.setError(null);
            } else {
                expiredDateContainer.setError("Must be (mm/yy) formatted.");
            }
        }
    }

    /**
     * @return if all input fields is not empty
     */
    private boolean inputValidator() {
        return !cardNumber.getText().toString().isEmpty()
                && cardNumber.getText().toString().length() == 16
                && !cvv.getText().toString().isEmpty()
                && cvv.getText().toString().length() == 3
                && !expiredDate.getText().toString().isEmpty()
                && expiredDate.getText().toString().split("/").length == 2;
    }


    private void saveCreditCards(ArrayList<SaveCardRequest> saveCardsRequests) {
        MidtransSDK.getInstance().saveCards("user01", saveCardsRequests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                dialog.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(CardRegistrationActivity.this)
                        .setMessage("Card is successfully registered")
                        .create();
                dialog.show();
            }

            @Override
            public void onFailure(String reason) {
                // Handle card registration failed
                dialog.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(CardRegistrationActivity.this)
                        .setMessage(reason)
                        .create();
                dialog.show();
            }

            @Override
            public void onError(Throwable error) {
                actionOnError(error);
            }
        });
    }

}
