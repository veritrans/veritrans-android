package id.co.veritrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.CardRegistrationBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.SaveCardBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.snap.SaveCardsRequest;

public class CardRegistrationActivity extends AppCompatActivity implements CardRegistrationBusCallback, SaveCardBusCallback {
    TextInputLayout cardNumberContainer, cvvContainer, expiredDateContainer;
    EditText cardNumber, cvv, expiredDate;
    Button saveBtn;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_registration);
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
                    VeritransSDK.getVeritransSDK().snapCardRegistration(cardNumber.getText().toString(),
                            cvv.getText().toString(),
                            date.split("/")[0],
                            "20" + date.split("/")[1]);
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
    public void onEvent(CardRegistrationSuccessEvent cardRegistrationSuccessEvent) {
        // Handle card registration success
        SaveCardRequest request = new SaveCardRequest();
        request.setCode(cardRegistrationSuccessEvent.getResponse().getStatusCode());
        request.setSavedTokenId(cardRegistrationSuccessEvent.getResponse().getSavedTokenId());
        request.setMaskedCard(cardRegistrationSuccessEvent.getResponse().getMaskedCard());
        request.setTransactionId(cardRegistrationSuccessEvent.getResponse().getTransactionId());
        ArrayList<SaveCardRequest> saveCardsRequests = new ArrayList<>();
        SaveCardRequest req = new SaveCardRequest(cardRegistrationSuccessEvent.getResponse().getSavedTokenId(),
                cardRegistrationSuccessEvent.getResponse().getMaskedCard(), "visa");
        saveCardsRequests.add(req);
        VeritransSDK.getVeritransSDK().snapSaveCard("user01",saveCardsRequests);
    }

    @Subscribe
    @Override
    public void onEvent(CardRegistrationFailedEvent cardRegistrationFailedEvent) {
        // Handle card registration failed
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(cardRegistrationFailedEvent.getMessage())
                .create();
        dialog.show();
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
    public void onEvent(SaveCardSuccessEvent saveCardSuccessEvent) {
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Card is successfully registered")
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(SaveCardFailedEvent saveCardFailedEvent) {
        // Handle card registration failed
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(saveCardFailedEvent.getMessage())
                .create();
        dialog.show();
    }
}
