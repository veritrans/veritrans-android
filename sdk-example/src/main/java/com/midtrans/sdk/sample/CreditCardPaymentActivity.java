package com.midtrans.sdk.sample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;

import java.util.ArrayList;

public class CreditCardPaymentActivity extends AppCompatActivity{
    TextInputLayout cardNumberContainer, cvvContainer, expiredDateContainer;
    EditText cardNumber, cvv, expiredDate;
    Button payBtn;
    ProgressDialog dialog;
    private CheckBox checkSaveCard;
    private boolean saveCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_payment);
        initView();
        if(!MidtransSDK.getInstance().getTransactionRequest()
                .getCardClickType().equals(getString(R.string.card_click_normal))){
            checkSaveCard.setVisibility(View.VISIBLE);
        }
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
        checkSaveCard = (CheckBox) findViewById(R.id.check_paycard_savecard);
        //Initialize progress dialog
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");
        //Initialize EditText
        cardNumber = (EditText)findViewById(R.id.card_number);
        cvv = (EditText)findViewById(R.id.cvv_number);
        expiredDate = (EditText)findViewById(R.id.exp_date);
        checkSaveCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                saveCard = checked;
            }
        });
        payBtn = (Button)findViewById(R.id.btn_payment);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh validator
                refreshView();
                if (inputValidator()) {
                    dialog.show();
                    // Create token request before payment
                    String date = expiredDate.getText().toString();
                    CardTokenRequest cardTokenRequest = new CardTokenRequest(
                            // Card number
                            cardNumber.getText().toString(),
                            cvv.getText().toString(),
                            date.split("/")[0],
                            date.split("/")[1],
                            MidtransSDK.getInstance().getClientKey());
                    cardTokenRequest.setGrossAmount(20.0);
                    MidtransSDK.getInstance().getCardToken(cardTokenRequest, new CardTokenCallback() {
                        @Override
                        public void onSuccess(TokenDetailsResponse response) {
                            String tokenId = response.getTokenId();
                            payWithCreditCard(MidtransSDK.getInstance().readAuthenticationToken(), tokenId);
                        }

                        @Override
                        public void onFailure(TokenDetailsResponse response, String reason) {
                            // Handle error when get token
                            dialog.dismiss();
                            AlertDialog dialog = new AlertDialog.Builder(CreditCardPaymentActivity.this)
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

    private void payWithCreditCard(String authenticationToken, String cardToken) {
        MidtransSDK.getInstance().paymentUsingCard(authenticationToken, cardToken, saveCard, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                // Handle success transaction
                dialog.dismiss();
                if(!TextUtils.isEmpty(response.getSavedTokenId()) && saveCard){
                    ArrayList<SaveCardRequest> requests = new ArrayList<>();
                    requests.add(new SaveCardRequest(response.getSavedTokenId(),
                            response.getMaskedCard(), "visa"));
                    saveCreditCard(requests);
                }
                final AlertDialog dialog = new AlertDialog.Builder(CreditCardPaymentActivity.this)
                        .setMessage(response.getStatusMessage())
                        .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                        }).create();
                dialog.show();
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                // Handle failed transaction
                dialog.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(CreditCardPaymentActivity.this)
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

    private void actionOnError(Throwable error) {
        // Handle generic error condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Unknown error: " + error.getMessage())
                .create();
        dialog.show();
    }

    private void saveCreditCard(ArrayList<SaveCardRequest> requests) {

        MidtransSDK.getInstance().saveCards(MainActivity.userId, requests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {

            }

            @Override
            public void onFailure(String reason) {
                Logger.i("savecard>failed");
                Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error) {
                actionOnError(error);
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

}
