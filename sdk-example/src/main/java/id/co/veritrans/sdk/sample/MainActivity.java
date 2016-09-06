package id.co.veritrans.sdk.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import id.co.veritrans.sdk.coreflow.callback.TransactionFinishedCallback;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.core.TransactionRequest;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.models.BillInfoModel;
import id.co.veritrans.sdk.coreflow.models.ItemDetails;
import id.co.veritrans.sdk.coreflow.models.snap.CreditCard;
import id.co.veritrans.sdk.coreflow.models.snap.TransactionResult;
import id.co.veritrans.sdk.sample.core.CoreFlowActivity;
import id.co.veritrans.sdk.scancard.ScanCard;
import id.co.veritrans.sdk.uiflow.SdkUIFlowBuilder;

public class MainActivity extends AppCompatActivity implements TransactionFinishedCallback{
    private static final int CORE_FLOW = 1;
    private static final int UI_FLOW = 2;
    public static String userId = "user214";
    ProgressDialog dialog;
    private int mysdkFlow = UI_FLOW;
    private TextView authToken;
    private Button coreBtn, uiBtn, widgetBtn;
    private Button coreCardRegistration, uiCardRegistration,
            getAuthenticationToken, refresh_token;
    private RadioButton normal, twoClick, oneClick;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSDK();
        initView();
        Logger.i("timestamp:" + new Timestamp(new Date().getTime()));
        Logger.i("timestamp2:" + new Date().getTime());
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
        if(mysdkFlow == CORE_FLOW){
            VeritransSDK veritransSDK = SdkCoreFlowBuilder.init(this, BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL)
                    .enableLog(true)
                    .setDefaultText("open_sans_regular.ttf")
                    .setSemiBoldText("open_sans_semibold.ttf")
                    .setBoldText("open_sans_bold.ttf")
                    .buildSDK();
        } else {
            // SDK initiation for UIflow
            VeritransSDK veritransSDK = SdkUIFlowBuilder.init(this, BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL, this)
                    .setExternalScanner(new ScanCard()) // initialization for using external scancard
                    .enableLog(true)
                    .setDefaultText("open_sans_regular.ttf")
                    .setSemiBoldText("open_sans_semibold.ttf")
                    .setBoldText("open_sans_bold.ttf")
                    .buildSDK();
        }
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
        }
        transactionRequestNew.setCardPaymentInfo(cardClickType, true);

        return transactionRequestNew;
    }

    /**
     * Initialize the view.
     */
    private void initView() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initialize progress dialog
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");

        widgetBtn = (Button) findViewById(R.id.show_card_widget);
        widgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WidgetExampleActivity.class));
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
                VeritransSDK.getInstance().setTransactionRequest(initializePurchaseRequest());
                Intent intent = new Intent(MainActivity.this, CoreFlowActivity.class);
                startActivity(intent);
            }
        });
        uiBtn = (Button) findViewById(R.id.show_ui_flow);

        uiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("config>clientkey:" + VeritransSDK.getInstance().getClientKey());
                Logger.i("config>merchantUrl:" + VeritransSDK.getInstance().getMerchantServerUrl());
                Logger.i("config>sdkurl:" + VeritransSDK.getInstance().getSdkBaseUrl());
                Logger.i("config>timeout:" + VeritransSDK.getInstance().getRequestTimeOut());
                VeritransSDK.getInstance().setTransactionRequest(initializePurchaseRequest());
                VeritransSDK.getInstance().startPaymentUiFlow(MainActivity.this);
            }
        });

        // Handle Card registration using core flow
        coreCardRegistration = (Button)findViewById(R.id.btn_card_registration_core);
        coreCardRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CardRegistrationActivity.class);
                startActivity(intent);
            }
        });

        // Handle Card registration using UI flow
        uiCardRegistration = (Button)findViewById(R.id.btn_card_registration_ui);
        uiCardRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeritransSDK.getInstance().startRegisterCardUIFlow(MainActivity.this);
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
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
        }
    }
}
