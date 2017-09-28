package com.midtrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.widgets.CreditCardForm;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author rakawm
 */
public class WidgetExampleActivity extends AppCompatActivity {
    public static final String CARD_PAYMENT_TYPE = "card_payment_type";
    private static final int ONECLICK = 1;
    private static final int TWOCLICK = 2;
    private CreditCardForm creditCardForm;
    private Button getToken;
    private ProgressDialog dialog;
    private String userId = "random-userid-example";
    private int clickType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        clickType = getIntent().getIntExtra(CARD_PAYMENT_TYPE, 0);

        creditCardForm = (CreditCardForm) findViewById(R.id.credit_card_form);
        creditCardForm.setMidtransClientKey(BuildConfig.CLIENT_KEY);
        creditCardForm.setMerchantUrl(BuildConfig.BASE_URL);
        creditCardForm.setCardPaymentEnabled(clickType);
        creditCardForm.checkout(this.userId, initializePurchaseRequest());
        //enable twoclick on card widget

        getToken = (Button) findViewById(R.id.btn_get_token);
        getToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creditCardForm.checkCardValidity()) {
                    dialog.show();
                    creditCardForm.pay(new CreditCardForm.WidgetTransactionCallback() {
                        @Override
                        public void onSucceed(TransactionResponse response) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Response message: " + response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                            finish();
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

        //define customer detail (mandatory)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("24234234234");
        mCustomerDetails.setFirstName("samle full name");
        mCustomerDetails.setEmail("mail@mail.com");

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress("jalan kalangan");
        shippingAddress.setCity("yogyakarta");
        shippingAddress.setCountryCode("IDN");
        shippingAddress.setPostalCode("72168");
        shippingAddress.setFirstName(mCustomerDetails.getFirstName());
        shippingAddress.setPhone(mCustomerDetails.getPhone());
        mCustomerDetails.setShippingAddress(shippingAddress);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setAddress("jalan kalangan");
        billingAddress.setCity("yogyakarta");
        billingAddress.setCountryCode("IDN");
        billingAddress.setPostalCode("72168");
        billingAddress.setFirstName(mCustomerDetails.getFirstName());
        billingAddress.setPhone(mCustomerDetails.getPhone());
        mCustomerDetails.setBillingAddress(billingAddress);
        transactionRequestNew.setCustomerDetails(mCustomerDetails);


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


        String cardClickType;
        if (clickType == ONECLICK) {
            cardClickType = getString(R.string.card_click_type_one_click);
            CreditCard creditCard = new CreditCard();
            creditCard.setSaveCard(true);
            creditCard.setSecure(true);
            transactionRequestNew.setCreditCard(creditCard);

        } else if (clickType == TWOCLICK) {
            cardClickType = getString(R.string.card_click_type_two_click);
            CreditCard creditCard = new CreditCard();
            creditCard.setSaveCard(true);
            transactionRequestNew.setCreditCard(creditCard);
        } else {
            cardClickType = getString(R.string.card_click_type_none);
        }

        transactionRequestNew.setCardPaymentInfo(cardClickType, true);

        return transactionRequestNew;
    }

    @Override
    protected void onDestroy() {
        creditCardForm.removeAllViews();
        creditCardForm = null;
        super.onDestroy();
    }
}
