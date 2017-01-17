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
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.models.BankType;
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
    private Button coreBtn, uiBtn, widgetBtn, widgetRegisterBtn, creditCardBtn, bankTransferBtn, permataBtn, mandiriBtn, bcaBtn, otherBankBtn, indomaretBtn, kiosonBtn, gciBtn;
    private Button coreCardRegistration, uiCardRegistration, klikBCABtn, BCAKlikpayBtn, mandiriClickpayBtn, mandiriEcashBtn, cimbClicksBtn, briEpayBtn, tcashBtn, indosatBtn, xlTunaiBtn;
    private RadioButton normal, twoClick, oneClick, bankBni, bankMandiri, bankBCA, bankMaybank, bankBri, secure, notSecure, expiryNone, expiryOneMinute, expiryOneHour, promoActive, promoInactive, savedCard, notSavedCard;
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
            // Init custom settings
            UIKitCustomSetting uisetting = new UIKitCustomSetting();
            uisetting.setShowPaymentStatus(true);

            // SDK initiation for UIflow
            SdkUIFlowBuilder.init(this, BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL, this)
                    .setExternalScanner(new ScanCard()) // initialization for using external scancard
                    .enableLog(true)
                    .useBuiltInTokenStorage(false) // enable built in token storage
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
                TransactionRequest(System.currentTimeMillis()+"", 2042000);

        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("624234234234");
        mCustomerDetails.setFirstName("sample full name");
        mCustomerDetails.setEmail("mail@mail.com");
        transactionRequestNew.setCustomerDetails(mCustomerDetails);


        // Define item details
//        ItemDetails itemDetails = new ItemDetails("1", 2000, 1, "Trekking Shoes");
//        ItemDetails itemDetails1 = new ItemDetails("2", 1000, 2, "Casual Shoes");
//        ItemDetails itemDetails2 = new ItemDetails("3", 1000, 3, "Formal Shoes");

        // Add item details into item detail list.
//        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
//        itemDetailsArrayList.add(itemDetails);
//        itemDetailsArrayList.add(itemDetails1);
//        itemDetailsArrayList.add(itemDetails2);
//        transactionRequestNew.setItemDetails(itemDetailsArrayList);
        // Set Bill info
        BillInfoModel billInfoModel = new BillInfoModel("demo_label", "demo_value");
        transactionRequestNew.setBillInfoModel(billInfoModel);

        // Create creditcard options for payment
        // noted : channel migs is needed if bank type is BCA, BRI or MyBank
        CreditCard creditCard = new CreditCard();
        if (bankMandiri.isChecked()) {
            // Set bank to Mandiri
            creditCard.setBank(BankType.MANDIRI);
        } else if (bankBni.isChecked()) {
            // Set bank to BNI
            creditCard.setBank(BankType.BNI);
        } else if (bankBCA.isChecked()) {
            //Set bank to BCA
            creditCard.setBank(BankType.BCA);
            // credit card payment using bank BCA need migs channel
            creditCard.setChannel(CreditCard.MIGS);
        } else if (bankMaybank.isChecked()) {
            //Set bank to Maybank
            creditCard.setBank(BankType.MAYBANK);
            // credit card payment using bank Maybank need migs channel
            creditCard.setChannel(CreditCard.MIGS);
        } else if (bankBri.isChecked()) {
            // Set bank to BRI
            creditCard.setBank(BankType.BRI);
            // credit card payment using bank BRI need migs channel
            creditCard.setChannel(CreditCard.MIGS);
        }

        String cardClickType;

        if (normal.isChecked()) {
            cardClickType = getString(R.string.card_click_type_none);
            if (secure.isChecked()) {
                creditCard.setSecure(true);
            } else {
                creditCard.setSecure(false);
            }
            transactionRequestNew.setCreditCard(creditCard);
        } else if (twoClick.isChecked()) {
            cardClickType = getString(R.string.card_click_type_two_click);
            creditCard.setSaveCard(true);
            transactionRequestNew.setCreditCard(creditCard);
        } else {
            cardClickType = getString(R.string.card_click_type_one_click);
            creditCard.setSaveCard(true);
            creditCard.setSecure(true);
            transactionRequestNew.setCreditCard(creditCard);
        }

        UIKitCustomSetting uiKitCustomSetting = MidtransSDK.getInstance().getUIKitCustomSetting();
        if (savedCard.isChecked()) {
            uiKitCustomSetting.setSaveCardChecked(true);
        } else {
            uiKitCustomSetting.setSaveCardChecked(false);
        }
        MidtransSDK.getInstance().setUIKitCustomSetting(uiKitCustomSetting);

        if (sampleSDKType == CORE_FLOW) {
            transactionRequestNew.setCardPaymentInfo(cardClickType, false);
        } else {
            if (secure.isChecked()) {
                transactionRequestNew.setCardPaymentInfo(cardClickType, true);
            } else {
                transactionRequestNew.setCardPaymentInfo(cardClickType, false);
            }
        }

        // apply promo transaction
        if (promoActive.isChecked()) {
            transactionRequestNew.setPaymentWithPromo(true);
        }

        ExpiryModel expiryModel = new ExpiryModel();
        expiryModel.setStartTime(Utils.getFormattedTime(System.currentTimeMillis()));
        expiryModel.setDuration(1);
        if (expiryOneMinute.isChecked()) {
            expiryModel.setUnit(ExpiryModel.UNIT_MINUTE);
            transactionRequestNew.setExpiry(expiryModel);
        } else if (expiryOneHour.isChecked()) {
            expiryModel.setUnit(ExpiryModel.UNIT_HOUR);
            transactionRequestNew.setExpiry(expiryModel);
        }

        Map<String, String> customMap = new HashMap<>();
        customMap.put("flight_id", "JT-214");
        customMap.put("airplane_type", "Boeing");
        transactionRequestNew.setCustomObject(customMap);

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

        expiryNone = (RadioButton) findViewById(R.id.radio_none);
        expiryOneMinute = (RadioButton) findViewById(R.id.radio_1_minute);
        expiryOneHour = (RadioButton) findViewById(R.id.radio_1_hour);

        bankBni = (RadioButton) findViewById(R.id.radio_bni);
        bankMandiri = (RadioButton) findViewById(R.id.radio_mandiri);
        bankBCA = (RadioButton) findViewById(R.id.radio_bca);
        bankMaybank = (RadioButton) findViewById(R.id.radio_maybank);
        bankBri = (RadioButton) findViewById(R.id.radio_bri);

        promoActive = (RadioButton) findViewById(R.id.radio_promo_active);
        promoInactive = (RadioButton) findViewById(R.id.radio_promo_inactive);

        savedCard = (RadioButton) findViewById(R.id.radio_save_active);
        notSavedCard = (RadioButton) findViewById(R.id.radio_save_inactive);

        bankMaybank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                secure.setChecked(true);
            }
        });

        bankBri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                secure.setChecked(true);
            }
        });

        notSecure = (RadioButton) findViewById(R.id.radio_not_secure);
        secure = (RadioButton) findViewById(R.id.radio_secure);

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

        oneClick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    secure.setChecked(true);
                }
            }
        });

        twoClick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    notSecure.setChecked(true);
                }
            }
        });

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

        // Handle credit card payment flow
        creditCardBtn = (Button) findViewById(R.id.show_credit_card_payment);
        creditCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startCreditCardUIFlow(MainActivity.this);
            }
        });

        // Handle bank transfer payment flow
        bankTransferBtn = (Button) findViewById(R.id.show_bank_transfer_payment);
        bankTransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startBankTransferUIFlow(MainActivity.this);
            }
        });

        permataBtn = (Button) findViewById(R.id.show_bank_transfer_permata_payment);
        permataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPermataBankTransferUIFlow(MainActivity.this);
            }
        });

        mandiriBtn = (Button) findViewById(R.id.show_bank_transfer_mandiri_payment);
        mandiriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startMandiriBankTransferUIFlow(MainActivity.this);
            }
        });

        bcaBtn = (Button) findViewById(R.id.show_bank_transfer_bca_payment);
        bcaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startBCABankTransferUIFlow(MainActivity.this);
            }
        });

        otherBankBtn = (Button) findViewById(R.id.show_bank_transfer_other_payment);
        otherBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startOtherBankTransferUIFlow(MainActivity.this);
            }
        });

        klikBCABtn = (Button) findViewById(R.id.show_klik_bca_payment);
        klikBCABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startKlikBCAUIFlow(MainActivity.this);
            }
        });

        BCAKlikpayBtn = (Button) findViewById(R.id.show_bca_klikpay_payment);
        BCAKlikpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startBCAKlikPayUIFlow(MainActivity.this);
            }
        });

        mandiriClickpayBtn = (Button) findViewById(R.id.show_mandiri_clickpay_payment);
        mandiriClickpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startMandiriClickpayUIFlow(MainActivity.this);
            }
        });

        mandiriEcashBtn = (Button) findViewById(R.id.show_mandiri_ecash_payment);
        mandiriEcashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startMandiriECashUIFlow(MainActivity.this);
            }
        });

        cimbClicksBtn = (Button) findViewById(R.id.show_cimb_clicks_payment);
        cimbClicksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startCIMBClicksUIFlow(MainActivity.this);
            }
        });

        briEpayBtn = (Button) findViewById(R.id.show_bri_epay_payment);
        briEpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startBRIEpayUIFlow(MainActivity.this);
            }
        });

        tcashBtn = (Button) findViewById(R.id.show_telkomsel_cash);
        tcashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startTelkomselCashUIFlow(MainActivity.this);
            }
        });

        indosatBtn = (Button) findViewById(R.id.show_indosat_dompetku);
        indosatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startIndosatDompetkuUIFlow(MainActivity.this);
            }
        });

        xlTunaiBtn = (Button) findViewById(R.id.show_xl_tunai);
        xlTunaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startXlTunaiUIFlow(MainActivity.this);
            }
        });

        indomaretBtn = (Button) findViewById(R.id.show_indomaret);
        indomaretBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startIndomaretUIFlow(MainActivity.this);
            }
        });

        kiosonBtn = (Button) findViewById(R.id.show_kioson);
        kiosonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startKiosonUIFlow(MainActivity.this);
            }
        });

        gciBtn = (Button) findViewById(R.id.show_gci);
        gciBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startGiftCardUIFlow(MainActivity.this);
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
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
