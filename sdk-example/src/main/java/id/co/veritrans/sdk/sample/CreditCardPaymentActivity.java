package id.co.veritrans.sdk.sample;

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

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.UUID;

import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.SaveCardBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TokenBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;

public class CreditCardPaymentActivity extends AppCompatActivity implements TokenBusCallback, SaveCardBusCallback,TransactionBusCallback{

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
        // Register this class into event bus
        VeritransBusProvider.getInstance().register(this);
        initView();
        VeritransSDK.getVeritransSDK().snapGetCards(MainActivity.userId);

        if(!VeritransSDK.getVeritransSDK().getTransactionRequest()
                .getCardClickType().equals(getString(R.string.card_click_normal))){
            checkSaveCard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        // Unregister this class into event bus
        VeritransBusProvider.getInstance().unregister(this);
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
                if(checked){
                    saveCard = true;
                }else{
                    saveCard = false;
                }
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
                            VeritransSDK.getVeritransSDK().getClientKey());
                    cardTokenRequest.setGrossAmount(20.0);
                    VeritransSDK.getVeritransSDK().getToken(cardTokenRequest);
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

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent networkUnavailableEvent) {
        // Handle network not available condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Network is unavailable")
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent generalErrorEvent) {
        // Handle generic error condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Unknown error: " + generalErrorEvent.getMessage() )
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent transactionSuccessEvent) {
        // Handle success transaction
        dialog.dismiss();
        if(!TextUtils.isEmpty(transactionSuccessEvent.getResponse().getSavedTokenId())){
            ArrayList<SaveCardRequest> requests = new ArrayList<>();
            requests.add(new SaveCardRequest(transactionSuccessEvent.getResponse().getSavedTokenId(),
                    transactionSuccessEvent.getResponse().getMaskedCard(), "visa"));
            VeritransSDK.getVeritransSDK().snapSaveCard(MainActivity.userId, requests);
        }else{
            ArrayList<SaveCardRequest> requests = new ArrayList<>();
            requests.add(new SaveCardRequest(UUID.randomUUID().toString(),
                    transactionSuccessEvent.getResponse().getMaskedCard(), "visa"));
            VeritransSDK.getVeritransSDK().snapSaveCard(MainActivity.userId, requests);
        }
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(transactionSuccessEvent.getResponse().getStatusMessage())
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
    public void onEvent(GetTokenSuccessEvent getTokenSuccessEvent) {
        // Handle get token success
        // Do the charge/payment
        VeritransSDK.getVeritransSDK().snapPaymentUsingCard(VeritransSDK.getVeritransSDK().readAuthenticationToken(),
                getTokenSuccessEvent.getResponse().getTokenId(), saveCard);
    }

    @Subscribe
    @Override
    public void onEvent(GetTokenFailedEvent getTokenFailedEvent) {
        // Handle error when get token
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getTokenFailedEvent.getMessage())
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(SaveCardSuccessEvent event) {
        Logger.i("savecard>success");
        Toast.makeText(getApplicationContext(), event.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    @Override
    public void onEvent(SaveCardFailedEvent event) {
        Logger.i("savecard>failed");
        Toast.makeText(getApplicationContext(), event.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
    }
}
