package id.co.veritrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

import id.co.veritrans.sdk.coreflow.core.TransactionRequest;
import id.co.veritrans.sdk.coreflow.models.BillInfoModel;
import id.co.veritrans.sdk.coreflow.models.ItemDetails;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.snap.CreditCard;
import id.co.veritrans.sdk.widgets.CreditCardForm;

/**
 * @author rakawm
 */
public class WidgetExampleActivity extends AppCompatActivity {
    private CreditCardForm creditCardForm;
    private Button getToken;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        creditCardForm = (CreditCardForm) findViewById(R.id.credit_card_form);
        creditCardForm.setVeritransClientKey(BuildConfig.CLIENT_KEY);
        creditCardForm.setMerchantUrl(BuildConfig.BASE_URL);

        getToken = (Button) findViewById(R.id.btn_get_token);
        getToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creditCardForm.checkCardValidity()) {
                    dialog.show();
                    creditCardForm.pay(initializePurchaseRequest(), new CreditCardForm.TransactionCallback() {
                        @Override
                        public void onSucceed(TransactionResponse response) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Response message: " + response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * Initialize transaction data.
     *
     * @return the transaction request.
     */
    private TransactionRequest initializePurchaseRequest() {
        // Create new Transaction Request
        TransactionRequest transactionRequestNew = new
                TransactionRequest(UUID.randomUUID().toString(), 360000);

        // Define item details
        ItemDetails itemDetails = new ItemDetails("1", 120000, 1, "Trekking Shoes");
        ItemDetails itemDetails1 = new ItemDetails("2", 100000, 1, "Casual Shoes");
        ItemDetails itemDetails2 = new ItemDetails("3", 140000, 1, "Formal Shoes");

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        itemDetailsArrayList.add(itemDetails1);
        itemDetailsArrayList.add(itemDetails2);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);
        // Set Bill info
        BillInfoModel billInfoModel = new BillInfoModel("demo_label", "demo_value");
        transactionRequestNew.setBillInfoModel(billInfoModel);

        CreditCard creditCard = new CreditCard();
        creditCard.setSecure(true);
        transactionRequestNew.setCreditCard(creditCard);

        return transactionRequestNew;
    }
}
