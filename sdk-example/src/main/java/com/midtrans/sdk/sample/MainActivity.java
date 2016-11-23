package com.midtrans.sdk.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ExpiryModel;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.sample.core.CoreFlowActivity;
import com.midtrans.sdk.scancard.ScanCard;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TransactionFinishedCallback {
    private static final int CORE_FLOW = 1;
    private static final int UI_FLOW = 2;
    public static String SAMPLE_USER_ID = UUID.randomUUID().toString();
    ProgressDialog dialog;
    private int mysdkFlow = UI_FLOW;
    private Button coreBtn, uiBtn, widgetBtn, widgetRegisterBtn;
    private Button coreCardRegistration, uiCardRegistration;
    private RadioButton normal, twoClick, oneClick;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSDK();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Initialize Veritrans SDK using SdkCoreFlowBuilder.
     */
    private void initSDK() {
        // SDK initiation for coreflow
        if (mysdkFlow == CORE_FLOW) {
            SdkCoreFlowBuilder.init(this, BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL)
                    .enableLog(true)
                    .buildSDK();
        } else {
            // Init custom setting if needed
            UIKitCustomSetting uisetting = new UIKitCustomSetting();
            uisetting.setShowPaymentStatus(true);

            // SDK initiation for UIflow
            SdkUIFlowBuilder.init(this, BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL, this)
                    .setExternalScanner(new ScanCard()) // initialization for using external scancard
                    .enableLog(true)
                    .useBuiltInTokenStorage(true) // enable built in token storage
                    .setDefaultText("open_sans_regular.ttf")
                    .setSemiBoldText("open_sans_semibold.ttf")
                    .setBoldText("open_sans_bold.ttf")
                    .setUIkitCustomSetting(uisetting) //optional
                    .buildSDK();
        }
    }


    /**
     * Initialize transaction data.
     *
     * @return the transaction request.
     */
    private TransactionRequest initializePurchaseRequest(int sampleSDKType) {
        // Create new Transaction Request
        TransactionRequest transactionRequestNew = new
                TransactionRequest(Utils.generateOrderId(), 3000);
        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("624234234234");
        mCustomerDetails.setFirstName("sample full name");
        mCustomerDetails.setEmail("mail@mail.com");
        transactionRequestNew.setCustomerDetails(mCustomerDetails);


        // Define item details
        ItemDetails itemDetails = new ItemDetails("1", 1000, 1, "Trekking Shoes");
        ItemDetails itemDetails1 = new ItemDetails("2", 1000, 1, "Casual Shoes");
        ItemDetails itemDetails2 = new ItemDetails("3", 1000, 1, "Formal Shoes");

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        itemDetailsArrayList.add(itemDetails1);
        itemDetailsArrayList.add(itemDetails2);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);
        // Set Bill info
        BillInfoModel billInfoModel = new BillInfoModel("demo_label", "demo_value");
        transactionRequestNew.setBillInfoModel(billInfoModel);

        // Create transaction request
        String cardClickType = "";

        if (normal.isChecked()) {
            cardClickType = getString(R.string.card_click_type_none);
        } else if (twoClick.isChecked()) {
            cardClickType = getString(R.string.card_click_type_two_click);
            CreditCard creditCard = new CreditCard();
            creditCard.setSaveCard(true);
            transactionRequestNew.setCreditCard(creditCard);
        } else {
            cardClickType = getString(R.string.card_click_type_one_click);
            CreditCard creditCard = new CreditCard();
            creditCard.setSaveCard(true);
            creditCard.setSecure(true);
            transactionRequestNew.setCreditCard(creditCard);
        }
        if (sampleSDKType == CORE_FLOW) {
            transactionRequestNew.setCardPaymentInfo(cardClickType, false);
        } else {
            transactionRequestNew.setCardPaymentInfo(cardClickType, true);
        }

        Map<String, String> customMap = new HashMap<>();
        customMap.put("flight_id", "JT-214");
        customMap.put("airplane_type", "Boeing");
        transactionRequestNew.setCustomObject(customMap);

        ExpiryModel expiryModel = new ExpiryModel();
        long currentTime = System.currentTimeMillis();
        expiryModel.setStartTime(Utils.getFormattedTime(currentTime));
        expiryModel.setUnit("MINUTES");
        expiryModel.setDuration(1);

        transactionRequestNew.setExpiry(expiryModel);

        return transactionRequestNew;
    }

    /**
     * Initialize the view.
     */
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initialize progress dialog
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");

        widgetBtn = (Button) findViewById(R.id.show_card_widget);
        widgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WidgetExampleActivity.class);
                if (oneClick.isChecked()) {
                    i.putExtra(WidgetExampleActivity.CARD_PAYMENT_TYPE, 1);
                } else if (twoClick.isChecked()) {
                    i.putExtra(WidgetExampleActivity.CARD_PAYMENT_TYPE, 2);
                }
                startActivity(i);
            }
        });

        widgetRegisterBtn = (Button) findViewById(R.id.show_card_widget_register);
        widgetRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WidgetRegisterExampleActivity.class));
            }
        });

        // Initialize radio button
        normal = (RadioButton) findViewById(R.id.radio_card_normal);
        twoClick = (RadioButton) findViewById(R.id.radio_card_two_click);
        oneClick = (RadioButton) findViewById(R.id.radio_card_one_click);

        //
        coreBtn = (Button) findViewById(R.id.show_core_example);
        coreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(CORE_FLOW));
                Intent intent = new Intent(MainActivity.this, CoreFlowActivity.class);
                startActivity(intent);
            }
        });
        uiBtn = (Button) findViewById(R.id.show_ui_flow);

        uiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("config>clientkey:" + MidtransSDK.getInstance().getClientKey());
                Logger.i("config>merchantUrl:" + MidtransSDK.getInstance().getMerchantServerUrl());
                Logger.i("config>sdkurl:" + MidtransSDK.getInstance().getSdkBaseUrl());
                Logger.i("config>timeout:" + MidtransSDK.getInstance().getRequestTimeOut());
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this);
            }
        });

        // Handle Card registration using core flow
        coreCardRegistration = (Button) findViewById(R.id.btn_card_registration_core);
        coreCardRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CardRegistrationActivity.class);
                startActivity(intent);
            }
        });

        // Handle Card registration using UI flow
        uiCardRegistration = (Button) findViewById(R.id.btn_card_registration_ui);
        uiCardRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().startRegisterCardUIFlow(MainActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sdk_config:
                SdkConfigDialogFragment fragment = SdkConfigDialogFragment.createInstance();
                fragment.show(getSupportFragmentManager(), SdkConfigDialogFragment.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_INVALID:
                    Toast.makeText(this, "Transaction Invalid. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getValidationMessages().get(0), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
        }
    }
}
