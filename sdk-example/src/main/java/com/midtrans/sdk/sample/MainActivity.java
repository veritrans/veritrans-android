package com.midtrans.sdk.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ExpiryModel;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.sample.core.CoreFlowActivity;
import com.midtrans.sdk.scancard.ScanCard;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TransactionFinishedCallback {
    private static final String PRIMARY_BROWN = "#795548";
    private static final String PRIMARY_DARK_BROWN = "#5D4037";
    private static final String SECONDARY_BROWN = "#A1887F";

    private static final String PRIMARY_BLUE_GREY = "#607D8B";
    private static final String PRIMARY_DARK_BLUE_GREY = "#455A64";
    private static final String SECONDARY_DARK_BLUE_GREY = "#90A4AE";

    private static final String PRIMARY_ORANGE = "#FF5722";
    private static final String PRIMARY_DARK_ORANGE = "#E64A19";
    private static final String SECONDARY_ORANGE = "#FF8A65";

    private static final int CORE_FLOW = 1;
    private static final int UI_FLOW = 2;
    public static String SAMPLE_USER_ID = UUID.randomUUID().toString();


    ProgressDialog dialog;
    private int mysdkFlow = UI_FLOW;
    private Button coreBtn, uiBtn, widgetBtn, widgetRegisterBtn, creditCardBtn, bankTransferBtn, permataBtn, mandiriBtn, bcaBtn, otherBankBtn, indomaretBtn, kiosonBtn, gciBtn;
    private Button coreCardRegistration, uiCardRegistration, klikBCABtn, BCAKlikpayBtn, mandiriClickpayBtn, mandiriEcashBtn, cimbClicksBtn, briEpayBtn, tcashBtn, indosatBtn, xlTunaiBtn;
    private RadioButton normal, twoClick, oneClick, bankBni, bankMandiri,
            bankBCA, bankMaybank, bankBri, secure, notSecure, expiryNone,
            expiryOneMinute, expiryOneHour, savedCard, notSavedCard,
            promoActive, promoInactive,
            preAuthActive, preAuthInactive,
            notSetColor, brown, blueGrey, orange;
    private EditText customField1, customField2, customField3;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSDK();
        initCustomerDetails();
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
                    .useBuiltInTokenStorage(true) // enable built in token storage
                    .setDefaultText("open_sans_regular.ttf")
                    .setSemiBoldText("open_sans_semibold.ttf")
                    .setBoldText("open_sans_bold.ttf")
                    .setUIkitCustomSetting(uisetting) //optional
                    .buildSDK();


        }
    }

    private void initCustomerDetails() {

        //define customer detail

        UserDetail userDetail;

        userDetail = LocalDataHandler.readObject("user_details", UserDetail.class);

        if (userDetail == null) {
            userDetail = new UserDetail();

            userDetail.setUserFullName("user fullname");
            userDetail.setEmail("mail@example.com");
            userDetail.setPhoneNumber("012345678");
            userDetail.setUserId(UUID.randomUUID().toString());

            List<UserAddress> userAddresses = new ArrayList<>();

            UserAddress userAddress = new UserAddress();
            userAddress.setAddress("alamat");
            userAddress.setCity("jakarta");
            userAddress.setCountry("IDN");
            userAddress.setZipcode("72168");
            userAddress.setAddressType(Constants.ADDRESS_TYPE_BOTH);
            userAddresses.add(userAddress);

            userDetail.setUserAddresses(new ArrayList<>(userAddresses));

            LocalDataHandler.saveObject("user_details", userDetail);
        }

    }


    /**
     * Initialize transaction data.
     *
     * @return the transaction request.
     */
    private TransactionRequest initializePurchaseRequest(int sampleSDKType) {
        // Init theme
        if (brown.isChecked()) {
            MidtransSDK.getInstance().setColorTheme(new CustomColorTheme(PRIMARY_BROWN, PRIMARY_DARK_BROWN, SECONDARY_BROWN));
        } else if (blueGrey.isChecked()) {
            MidtransSDK.getInstance().setColorTheme(new CustomColorTheme(PRIMARY_BLUE_GREY, PRIMARY_DARK_BLUE_GREY, SECONDARY_DARK_BLUE_GREY));
        } else if (orange.isChecked()) {
            MidtransSDK.getInstance().setColorTheme(new CustomColorTheme(PRIMARY_ORANGE, PRIMARY_DARK_ORANGE, SECONDARY_ORANGE));
        } else {
            MidtransSDK.getInstance().setColorTheme(null);
        }
        // Create new Transaction Request
        TransactionRequest transactionRequestNew = new
                TransactionRequest(System.currentTimeMillis() + "", 6000);

        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("624234234234");
        mCustomerDetails.setFirstName("sample full name");
        mCustomerDetails.setEmail("mail@mail.com");
        transactionRequestNew.setCustomerDetails(mCustomerDetails);


        // Define item details
        ItemDetails itemDetails = new ItemDetails("1", 1000, 1, "Trekking Shoes");
        ItemDetails itemDetails1 = new ItemDetails("2", 1000, 2, "Casual Shoes");
        ItemDetails itemDetails2 = new ItemDetails("3", 1000, 3, "Formal Shoes");

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        itemDetailsArrayList.add(itemDetails1);
        itemDetailsArrayList.add(itemDetails2);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);
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

        creditCard.setAuthentication(CreditCard.RBA);

        if (preAuthActive.isChecked()) {
            // Set Pre Auth mode
            creditCard.setType(CardTokenRequest.TYPE_AUTHORIZE);
        }

        String cardClickType;

        if (normal.isChecked()) {
            cardClickType = getString(R.string.card_click_type_none);
            if (secure.isChecked()) {
                creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_3DS);
            } else {
                creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_NONE);
            }
            transactionRequestNew.setCreditCard(creditCard);
        } else if (twoClick.isChecked()) {
            cardClickType = getString(R.string.card_click_type_two_click);
            creditCard.setSaveCard(true);
            transactionRequestNew.setCreditCard(creditCard);
        } else {
            cardClickType = getString(R.string.card_click_type_one_click);
            creditCard.setSaveCard(true);
            creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_3DS);
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

        if (promoActive.isChecked()) {
            // Set promo
            transactionRequestNew.setPromoEnabled(true);
        }

        // Set custom field 1
        if (!TextUtils.isEmpty(customField1.getText().toString())) {
            transactionRequestNew.setCustomField1(customField1.getText().toString());
        }

        // Set custom field 2
        if (!TextUtils.isEmpty(customField2.getText().toString())) {
            transactionRequestNew.setCustomField2(customField2.getText().toString());
        }

        // Set custom field 3
        if (!TextUtils.isEmpty(customField3.getText().toString())) {
            transactionRequestNew.setCustomField3(customField3.getText().toString());
        }

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

        customField1 = (EditText) findViewById(R.id.custom_field1);
        customField2 = (EditText) findViewById(R.id.custom_field2);
        customField3 = (EditText) findViewById(R.id.custom_field3);

        expiryNone = (RadioButton) findViewById(R.id.radio_none);
        expiryOneMinute = (RadioButton) findViewById(R.id.radio_1_minute);
        expiryOneHour = (RadioButton) findViewById(R.id.radio_1_hour);

        bankBni = (RadioButton) findViewById(R.id.radio_bni);
        bankMandiri = (RadioButton) findViewById(R.id.radio_mandiri);
        bankBCA = (RadioButton) findViewById(R.id.radio_bca);
        bankMaybank = (RadioButton) findViewById(R.id.radio_maybank);
        bankBri = (RadioButton) findViewById(R.id.radio_bri);

        savedCard = (RadioButton) findViewById(R.id.radio_save_active);
        notSavedCard = (RadioButton) findViewById(R.id.radio_save_inactive);

        promoActive = (RadioButton) findViewById(R.id.radio_promo_active);
        promoInactive = (RadioButton) findViewById(R.id.radio_promo_inactive);

        preAuthActive = (RadioButton) findViewById(R.id.radio_pre_auth_active);
        preAuthInactive = (RadioButton) findViewById(R.id.radio_pre_auth_inactive);

        notSetColor = (RadioButton) findViewById(R.id.radio_color_not_set);
        brown = (RadioButton) findViewById(R.id.radio_color_brown);
        blueGrey = (RadioButton) findViewById(R.id.radio_color_blue_grey);
        orange = (RadioButton) findViewById(R.id.radio_color_orange);

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
                Intent intent = new Intent(MainActivity.this, ExampleRegisterActivity.class);
                startActivity(intent);
            }
        });

        // Handle Card registration using UI flow
        uiCardRegistration = (Button) findViewById(R.id.btn_card_registration_ui);

        // Handle credit card payment flow
        creditCardBtn = (Button) findViewById(R.id.show_credit_card_payment);
        creditCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.CREDIT_CARD);
            }
        });

        // Handle bank transfer payment flow
        bankTransferBtn = (Button) findViewById(R.id.show_bank_transfer_payment);
        bankTransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER);
            }
        });

        permataBtn = (Button) findViewById(R.id.show_bank_transfer_permata_payment);
        permataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_PERMATA);
            }
        });

        mandiriBtn = (Button) findViewById(R.id.show_bank_transfer_mandiri_payment);
        mandiriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_MANDIRI);
            }
        });

        bcaBtn = (Button) findViewById(R.id.show_bank_transfer_bca_payment);
        bcaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_BCA);
            }
        });

        otherBankBtn = (Button) findViewById(R.id.show_bank_transfer_other_payment);
        otherBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_OTHER);
            }
        });

        klikBCABtn = (Button) findViewById(R.id.show_klik_bca_payment);
        klikBCABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.KLIKBCA);
            }
        });

        BCAKlikpayBtn = (Button) findViewById(R.id.show_bca_klikpay_payment);
        BCAKlikpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BCA_KLIKPAY);
            }
        });

        mandiriClickpayBtn = (Button) findViewById(R.id.show_mandiri_clickpay_payment);
        mandiriClickpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.MANDIRI_CLICKPAY);
            }
        });

        mandiriEcashBtn = (Button) findViewById(R.id.show_mandiri_ecash_payment);
        mandiriEcashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.MANDIRI_ECASH);
            }
        });

        cimbClicksBtn = (Button) findViewById(R.id.show_cimb_clicks_payment);
        cimbClicksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.CIMB_CLICKS);
            }
        });

        briEpayBtn = (Button) findViewById(R.id.show_bri_epay_payment);
        briEpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.EPAY_BRI);
            }
        });

        tcashBtn = (Button) findViewById(R.id.show_telkomsel_cash);
        tcashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.TELKOMSEL_CASH);
            }
        });

        indosatBtn = (Button) findViewById(R.id.show_indosat_dompetku);
        indosatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.INDOSAT_DOMPETKU);
            }
        });

        xlTunaiBtn = (Button) findViewById(R.id.show_xl_tunai);
        xlTunaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.XL_TUNAI);
            }
        });

        indomaretBtn = (Button) findViewById(R.id.show_indomaret);
        indomaretBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.INDOMARET);
            }
        });

        kiosonBtn = (Button) findViewById(R.id.show_kioson);
        kiosonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.KIOSON);
            }
        });

        gciBtn = (Button) findViewById(R.id.show_gci);
        gciBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().setTransactionRequest(initializePurchaseRequest(UI_FLOW));
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.GIFT_CARD_INDONESIA);
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
        Log.d("finalx", "rsultd:" + result.getResponse());

        if (result.getResponse() != null) {
            Log.d("finalx", "result:" + result.getResponse().getStatusMessage());
            Log.d("finalx", "result>fraud:" + result.getResponse().getFraudStatus());
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
            result.getResponse().getValidationMessages();
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
