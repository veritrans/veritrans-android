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
import java.util.UUID;

import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.TransactionRequest;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TokenBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.BillingAddress;
import id.co.veritrans.sdk.coreflow.models.CardPaymentDetails;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.CustomerDetails;
import id.co.veritrans.sdk.coreflow.models.ItemDetails;
import id.co.veritrans.sdk.coreflow.models.ShippingAddress;
import id.co.veritrans.sdk.coreflow.models.TransactionDetails;

public class CreditCardPaymentActivity extends AppCompatActivity implements TokenBusCallback, TransactionBusCallback{

    TextInputLayout cardNumberContainer, cvvContainer, expiredDateContainer;
    EditText cardNumber, cvv, expiredDate;
    Button payBtn;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_payment);
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
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Payment is Successful")
                .create();
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
        String orderId = UUID.randomUUID().toString();
        TransactionRequest request = new TransactionRequest(orderId.toString(), 360000);
        request.setCardPaymentInfo(getString(R.string.card_click_type_none), false);
        Logger.e("rls", "run>post>another");
        VeritransSDK.getVeritransSDK().setTransactionRequest(request);
        ItemDetails itemDetails = new ItemDetails("1", 360000, 1, "shoes");
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        CardTransfer transfer = new CardTransfer(
                new CardPaymentDetails(
                        getTokenSuccessEvent.getResponse().getBank(),
                        getTokenSuccessEvent.getResponse().getTokenId(),
                        false),
                new TransactionDetails("360000", orderId),
                itemDetailsArrayList,
                new ArrayList<BillingAddress>(),
                new ArrayList<ShippingAddress>(),
                new CustomerDetails("Raka", "Mogandhi", "westumogandhi@gmail.com", "6285653956354")
        );
        VeritransSDK.getVeritransSDK().paymentUsingCard(transfer);
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
}
