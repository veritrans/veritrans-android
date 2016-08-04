package id.co.veritrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.UUID;

import id.co.veritrans.sdk.coreflow.core.TransactionRequest;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.BillInfoModel;
import id.co.veritrans.sdk.coreflow.models.ItemDetails;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayModel;

public class MandiriClickPaymentActivity extends AppCompatActivity implements TransactionBusCallback {
    TextInputLayout cardNumberContainer, tokenContainer;
    EditText cardNumber, challengeToken;
    Button payBtn;
    ProgressDialog dialog;
    private String sampleMandiriCardNumber = "4111111111111111";
    private String sampleTokenResponse = "000000";
    private String input3 = "59478";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandiri_click_payment);
        // Register this class into event bus
        VeritransBusProvider.getInstance().register(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        // Unregister this class into event bus
        VeritransBusProvider.getInstance().unregister(this);
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
                    VeritransSDK.getVeritransSDK().snapPaymentUsingMandiriClickPay(
                            VeritransSDK.getVeritransSDK().readAuthenticationToken(),
                            sampleMandiriCardNumber,
                            sampleTokenResponse,
                            input3
                    );
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

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent transactionSuccessEvent) {
        // Handle success transaction
        dialog.dismiss();
        Toast.makeText(this, "transaction successfull (" + transactionSuccessEvent.getResponse().getStatusMessage() + ")", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent transactionFailedEvent) {
        // Handle failed transaction
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(transactionFailedEvent.getMessage())
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent networkUnavailableEvent) {
        // Handle network not available condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.no_network)
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent generalErrorEvent) {
        // Handle generic error condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Unknown error: " + generalErrorEvent.getMessage())
                .create();
        dialog.show();
    }
}
