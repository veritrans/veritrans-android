package com.midtrans.demo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.midtrans.demo.widgets.DemoRadioButton;
import com.midtrans.demo.widgets.DemoTextView;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.*;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.*;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.*;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.scancard.ScanCard;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.midtrans.demo.GopayStatusActivity.*;

/**
 * Created by rakawm on 3/15/17.
 */

public class DemoConfigActivity extends AppCompatActivity implements TransactionFinishedCallback {
    public static final String COLOR_THEME = "config.theme";

    private static final String CARD_CLICK_TYPE = "config.card";
    private static final String SECURE_TYPE = "config.secure";
    private static final String BANK_TYPE = "config.bank";
    private static final String EXPIRY_TPE = "config.expiry";
    private static final String SAVE_CARD_TYPE = "config.save";
    private static final String PRE_AUTH_TYPE = "config.preauth";
    private static final String CUSTOM_BCA_VA_NUMBER = "config.va.bca";
    private static final String CUSTOM_PERMATA_VA_NUMBER = "config.va.permata";
    private static final String CUSTOM_PERMATA_RECIPIENT = "config.recipient.permata";
    private static final String CUSTOM_BNI_VA_NUMBER = "config.va.bni";
    private static final String SUBCOMPANY_BCA_VA_NUMBER = "config.subcompany.va.bca";
    private static final String INSTALLMENT_TYPE = "config.installment";
    private static final String INSTALLMENT_REQUIRED = "config.installment.required";
    private static final String BNI_POINT_TYPE = "config.bni.point";
    private static final String MANDIRI_POINT_TYPE = "config.mandiri.point";
    private static final String PAYMENT_CHANNELS_TYPE = "config.channels";
    private static final String AUTO_READ_SMS_TYPE = "config.auto.otp";
    public static final String CURRENCY_TYPE = "config.currency";

    private static final String LABEL_INSTALLMENT_REQUIRED = " - Required";
    private static final String LABEL_INSTALLMENT_OPTIONAL = " - optional";

    private static final int AUTH_TYPE_3DS = 1;
    private static final int AUTH_TYPE_RBA = 2;
    private static final int AUTH_TYPE_RBA_NON_3DS = 3;
    private static final int AUTH_TYPE_NONE = 0;

    public static final int CURRENCY_TYPE_IDR = 0;
    public static final int CURRENCY_TYPE_SGD = 1;

    private static final String CLIENT_KEY_FOR_ONE_CLICK_MID = "VT-client-F91kdUrnE5w8zCja";

    private static int DELAY = 200;
    private String selectedColor = DemoThemeConstants.BLUE_THEME;
    private List<SelectPaymentMethodViewModel> enabledPayments;
    /**
     * Title of Config
     **/
    private TextView cardClickTitle;
    private TextView secureTitle;
    private TextView bankTitle;
    private TextView expiryTitle;
    private TextView saveCardTitle;
    private TextView preAuthTitle;
    private TextView colorThemeTitle;
    private TextView customPermataVaTitle;
    private TextView customPermataRecipientTitle;
    private TextView customBcaVaTitle;
    private TextView customBniVaTitle;
    private TextView subCompanyBcaVaTitle;
    private TextView installmentTitle;
    private TextView bniPointTitle;
    private TextView mandiriPointTitle;
    private TextView paymentChannelsTitle;
    private TextView autoReadSmsTitle;
    private TextView currencyTitle;

    /**
     * Selection Container
     **/
    private RadioGroup cardClickContainer;
    private RadioGroup secureContainer;
    private RadioGroup bankContainer;
    private RadioGroup expiryContainer;
    private RadioGroup saveCardContainer;
    private RadioGroup preAuthContainer;
    private RadioGroup colorThemeContainer;
    private LinearLayout customBcaVaContainer;
    private LinearLayout customPermataVaContainer;
    private LinearLayout customPermataRecipientContainer;
    private LinearLayout customBniVaContainer;
    private LinearLayout subCompanyBcaVaContainer;
    private RadioGroup installmentContainer;
    private RadioGroup bniPointContainer;
    private RadioGroup mandiriPointContainer;
    private LinearLayout paymentChannelsContainer;
    private LinearLayout changeInstallmentContainer;
    private RadioGroup autoReadSmsContainer;
    private RadioGroup currencyContainer;

    /**
     * Radio Button selection for installment
     */
    private AppCompatRadioButton installmentBniSelection;
    private AppCompatRadioButton installmentMandiriSelection;
    private AppCompatRadioButton installmentBcaSelection;
    private AppCompatRadioButton installmentBriSelection;
    private AppCompatRadioButton installmentCimbSelection;
    private AppCompatRadioButton installmentMaybankSelection;
    private AppCompatRadioButton noInstallmentSelection;
    /**
     * Radio Button selection for BNI Point
     */
    private AppCompatRadioButton bniPointOnlyEnabledSelection;
    private AppCompatRadioButton bniPointOnlyDisabledSelection;
    /**
     * Radio Button selection for Mandiri Point
     */
    private AppCompatRadioButton mandiriPointOnlyEnabledSelection;
    private AppCompatRadioButton mandiriPointOnlyDisabledSelection;
    /**
     * Radio Button Selection for Card Click
     **/
    private AppCompatRadioButton normalSelection;
    private AppCompatRadioButton twoClicksSelection;
    private AppCompatRadioButton oneClickSelection;

    /**
     * Radio Button Selection for Secure
     **/
    private AppCompatRadioButton secureDisabledSelection;
    private AppCompatRadioButton secureEnabledSelection;
    private AppCompatRadioButton secureRbaWith3dsSelection;
    private AppCompatRadioButton secureRbaNon3dsSelection;
    /**
     * Radio Button Selection for Issuing Bank
     **/
    private AppCompatRadioButton bankNoneSelection;
    private AppCompatRadioButton bankBniSelection;
    private AppCompatRadioButton bankMandiriSelection;
    private AppCompatRadioButton bankBcaSelection;
    private AppCompatRadioButton bankMaybankSelection;
    private AppCompatRadioButton bankBriSelection;
    private AppCompatRadioButton bankCimbSelection;
    private AppCompatRadioButton bankMegaSelection;
    /**
     * Radio Button Selection for Expiry
     **/
    private AppCompatRadioButton expiryNoneSelection;
    private AppCompatRadioButton expiryOneMinuteSelection;
    private AppCompatRadioButton expiryOneHourSelection;
    /**
     * Radio Button Selection for Default Save Card
     **/
    private AppCompatRadioButton saveCardDisabledSelection;
    private AppCompatRadioButton saveCardEnabledSelection;
    /**
     * Radio Button Selection for Custom BCA VA
     */
    private AppCompatRadioButton customBcaVaEnabledSelection;
    private AppCompatRadioButton customBcaVaDisabledSelection;
    /**
     * Radio Button Selection for Custom Permata VA
     */
    private AppCompatRadioButton customPermataVaEnabledSelection;
    private AppCompatRadioButton customPermataVaDisabledSelection;
    /**
     * Radio Button Selection for Custom Permata Recipient
     */
    private AppCompatRadioButton customPermataRecipientEnabledSelection;
    private AppCompatRadioButton customPermataRecipientDisabledSelection;

    /**
     * Radio Button Selection for Custom BNI VA
     */
    private AppCompatRadioButton customBniVaEnabledSelection;
    private AppCompatRadioButton customBniVaDisabledSelection;
    /**
     * Radio Button Selection for Subcompany BCA VA
     */
    private AppCompatRadioButton subCompanyBcaVaEnabledSelection;
    private AppCompatRadioButton subCompanyBcaVaDisabledSelection;
    /**
     * Radio Button Selection for Pre Authorization
     **/
    private AppCompatRadioButton preAuthDisabledSelection;
    private AppCompatRadioButton preAuthEnabledSelection;
    /**
     * Radio Button Selection for Color Theme
     **/
    private AppCompatRadioButton defaultThemeSelection;
    private AppCompatRadioButton redThemeSelection;
    private AppCompatRadioButton greenThemeSelection;
    private AppCompatRadioButton orangeThemeSelection;
    private AppCompatRadioButton blackThemeSelection;
    /**
     * Radio Button Selection for Payment Channels selection
     */
    private AppCompatRadioButton paymentChannelsAllSelection;
    private AppCompatRadioButton paymentChannelsSelectedSelection;
    /**
     * Radio Button Selection for Auto Read SMS selection.
     */
    private AppCompatRadioButton autoReadSmsEnabledSelection;
    private AppCompatRadioButton autoReadSmsDisabledSelection;

    /**
     * Radio Button Selection for currency
     */
    private AppCompatRadioButton currencyIdrSelection;
    private AppCompatRadioButton currencySgdSelection;


    private FancyButton nextButton;
    private ImageButton editBcaVaButton;
    private ImageButton editPermataVaButton;
    private ImageButton editPermataRecipientButton;
    private ImageButton editBniVaButton;
    private ImageButton editSubCompanyBcaVaButton;
    private ImageButton editPaymentChannels;
    private ImageButton editInstallmentMandiri;
    private ImageButton editInstallmentBca;
    private ImageButton editInstallmentBni;
    private ImageButton editInstallmentBri;
    private ImageButton editInstallmentCimb;
    private ImageButton editInstallmentMaybank;

    private boolean installmentRequired;

    private DemoTextView resetSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_configuration);
        initMidtransSDK();
        bindViews();
        initColorThemeSelection();
        initCardClickSelection();
        initSecureSelection();
        initIssuingBankSelection();
        initCustomExpirySelection();
        initDefaultSaveCardSelection();
        initPaymentChannelsSelections();
        initCustomBcaVaSelection();
        initCustomPermataVaSelection();
        initCustomPermataRecipientSelection();
        initCustomBniVaSelection();
        initSubCompanyBcaVaSelection();
        initPreAuthSelection();
        initInstallmentSelection();
        initChangeInstallmentOption();
        initBniPointSelection();
        initMandiriPointSelection();
        initAutoReadOtpSelection();
        initCurrencySelection();

        initTitleClicks();
        initNextButton();
        initResetSettings();
        checkGopayCallback();
    }

    private void checkGopayCallback() {
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null && data.isHierarchical()) {
            final String orderId = data.getQueryParameter("order_id");
            final String result = data.getQueryParameter("result");
            Intent intentToResult = new Intent(DemoConfigActivity.this, GopayStatusActivity.class);
            intentToResult.putExtra(INTENT_ORDERID, orderId);
            intentToResult.putExtra(INTENT_AMOUNT, "10000");
            intentToResult.putExtra(INTENT_TYPE, "GO-PAY");
            intentToResult.putExtra(INTENT_STATUS, result);
            startActivity(intentToResult);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unselectAllTitles();
        hideAllSelections();
    }

    private void bindViews() {
        cardClickTitle = findViewById(R.id.title_credit_card_type);
        secureTitle = findViewById(R.id.title_secure_type);
        bankTitle = findViewById(R.id.title_issuing_bank_type);
        expiryTitle = findViewById(R.id.title_custom_expiry_type);
        saveCardTitle = findViewById(R.id.title_default_save_card_type);
        preAuthTitle = findViewById(R.id.title_pre_auth_type);
        colorThemeTitle = findViewById(R.id.title_color_theme_type);
        customPermataVaTitle = findViewById(R.id.title_custom_permata_va);
        customPermataRecipientTitle = findViewById(R.id.title_custom_permata_recipient);
        customBcaVaTitle = findViewById(R.id.title_custom_bca_va);
        customBniVaTitle = findViewById(R.id.title_custom_bni_va);
        subCompanyBcaVaTitle = findViewById(R.id.title_subcompany_bca_va);
        installmentTitle = findViewById(R.id.title_installment_type);
        bniPointTitle = findViewById(R.id.title_bni_point_type);
        mandiriPointTitle = findViewById(R.id.title_mandiri_point_type);
        paymentChannelsTitle = findViewById(R.id.title_custom_payment_channels);
        autoReadSmsTitle = findViewById(R.id.title_auto_read_type);
        currencyTitle = findViewById(R.id.title_currency_type);

        resetSetting = findViewById(R.id.text_reset);

        cardClickContainer = findViewById(R.id.credit_card_type_container);
        secureContainer = findViewById(R.id.secure_type_container);
        bankContainer = findViewById(R.id.issuing_bank_type_container);
        expiryContainer = findViewById(R.id.expiry_type_container);
        saveCardContainer = findViewById(R.id.default_save_type_container);
        preAuthContainer = findViewById(R.id.preauth_type_container);
        colorThemeContainer = findViewById(R.id.theme_type_container);
        customBcaVaContainer = findViewById(R.id.custom_bca_va_type_container);
        customPermataVaContainer = findViewById(R.id.custom_permata_va_type_container);
        customPermataRecipientContainer = findViewById(R.id.custom_permata_recipient_type_container);
        customBniVaContainer = findViewById(R.id.custom_bni_va_type_container);
        subCompanyBcaVaContainer = findViewById(R.id.subcompany_bca_va_type_container);
        installmentContainer = findViewById(R.id.enable_installment_container);
        changeInstallmentContainer = findViewById(R.id.change_installment_container);
        bniPointContainer = findViewById(R.id.bni_point_type_container);
        mandiriPointContainer = findViewById(R.id.mandiri_point_type_container);
        paymentChannelsContainer = findViewById(R.id.payment_channels_type_container);
        autoReadSmsContainer = findViewById(R.id.auto_read_type_container);
        currencyContainer = findViewById(R.id.currency_container);

        installmentBniSelection = findViewById(R.id.installment_type_bni);
        installmentMandiriSelection = findViewById(R.id.installment_type_mandiri);
        installmentBcaSelection = findViewById(R.id.installment_type_bca);
        installmentBriSelection = findViewById(R.id.installment_type_bri);
        installmentCimbSelection = findViewById(R.id.installment_type_cimb);
        installmentMaybankSelection = findViewById(R.id.installment_type_maybank);
        noInstallmentSelection = findViewById(R.id.no_installment);

        normalSelection = findViewById(R.id.type_credit_card_normal);
        twoClicksSelection = findViewById(R.id.type_credit_card_two_clicks);
        oneClickSelection = findViewById(R.id.type_credit_card_one_click);

        secureDisabledSelection = findViewById(R.id.type_secure_disabled);
        secureEnabledSelection = findViewById(R.id.type_secure_enabled);
        secureRbaWith3dsSelection = findViewById(R.id.type_secure_rba);
        secureRbaNon3dsSelection = findViewById(R.id.type_secure_rba_non_3ds);

        bankNoneSelection = findViewById(R.id.type_bank_none);
        bankBniSelection = findViewById(R.id.type_bank_bni);
        bankMandiriSelection = findViewById(R.id.type_bank_mandiri);
        bankBcaSelection = findViewById(R.id.type_bank_bca);
        bankMaybankSelection = findViewById(R.id.type_bank_maybank);
        bankBriSelection = findViewById(R.id.type_bank_bri);
        bankCimbSelection = findViewById(R.id.type_bank_cimb);
        bankMegaSelection = findViewById(R.id.type_bank_mega);

        expiryNoneSelection = findViewById(R.id.type_expiry_none);
        expiryOneMinuteSelection = findViewById(R.id.type_expiry_one_minute);
        expiryOneHourSelection = findViewById(R.id.type_expiry_one_hour);

        saveCardDisabledSelection = findViewById(R.id.type_default_save_disabled);
        saveCardEnabledSelection = findViewById(R.id.type_default_save_enabled);

        customBcaVaDisabledSelection = findViewById(R.id.type_bca_va_disabled);
        customBcaVaEnabledSelection = findViewById(R.id.type_bca_va_enabled);

        customPermataVaDisabledSelection = findViewById(R.id.type_permata_va_disabled);
        customPermataVaEnabledSelection = findViewById(R.id.type_permata_va_enabled);

        customPermataRecipientDisabledSelection = findViewById(R.id.type_permata_recipient_disabled);
        customPermataRecipientEnabledSelection = findViewById(R.id.type_permata_recipient_enabled);

        customBniVaEnabledSelection = findViewById(R.id.type_bni_va_enabled);
        customBniVaDisabledSelection = findViewById(R.id.type_bni_va_disabled);

        subCompanyBcaVaDisabledSelection = findViewById(R.id.type_subcompany_bca_va_disabled);
        subCompanyBcaVaEnabledSelection = findViewById(R.id.type_subcompany_bca_va_enabled);

        preAuthDisabledSelection = findViewById(R.id.type_preauth_disabled);
        preAuthEnabledSelection = findViewById(R.id.type_preauth_enabled);

        defaultThemeSelection = findViewById(R.id.type_theme_default);
        redThemeSelection = findViewById(R.id.type_theme_red);
        greenThemeSelection = findViewById(R.id.type_theme_green);
        orangeThemeSelection = findViewById(R.id.type_theme_orange);
        blackThemeSelection = findViewById(R.id.type_theme_black);

        bniPointOnlyEnabledSelection = findViewById(R.id.type_bni_point_enabled);
        bniPointOnlyDisabledSelection = findViewById(R.id.type_bni_point_disabled);

        mandiriPointOnlyEnabledSelection = findViewById(R.id.type_mandiri_point_enabled);
        mandiriPointOnlyDisabledSelection = findViewById(R.id.type_mandiri_point_disabled);

        paymentChannelsAllSelection = findViewById(R.id.type_payment_channels_show_all);
        paymentChannelsSelectedSelection = findViewById(R.id.type_payment_channels_show_selected);

        autoReadSmsDisabledSelection = findViewById(R.id.type_auto_read_disabled);
        autoReadSmsEnabledSelection = findViewById(R.id.type_auto_read_enabled);

        currencyIdrSelection = findViewById(R.id.currency_idr_selection);
        currencySgdSelection = findViewById(R.id.currency_sgd_selection);

        nextButton = findViewById(R.id.button_primary);
        editBcaVaButton = findViewById(R.id.btn_edit_bca_va);
        editPermataVaButton = findViewById(R.id.btn_edit_permata_va);
        editPermataRecipientButton = findViewById(R.id.btn_edit_permata_recipient);
        editPaymentChannels = findViewById(R.id.btn_edit_payment_method);
        editBniVaButton = findViewById(R.id.btn_edit_bni_va);
        editSubCompanyBcaVaButton = findViewById(R.id.btn_edit_subcompany_bca_va);
        editInstallmentMandiri = findViewById(R.id.button_mandiri_installment_edit);
        editInstallmentBca = findViewById(R.id.button_bca_installment_edit);
        editInstallmentBni = findViewById(R.id.button_bni_installment_edit);
        editInstallmentBri = findViewById(R.id.button_bri_installment_edit);
        editInstallmentCimb = findViewById(R.id.button_cimb_installment_edit);
        editInstallmentMaybank = findViewById(R.id.button_maybank_installment_edit);
    }

    private void initTitleClicks() {
        cardClickTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!cardClickTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            cardClickTitle.setSelected(true);
                            setTextViewSelectedColor(cardClickTitle);
                            setTextViewDrawableLeftColorFilter(cardClickTitle);
                            // Show card click container
                            cardClickContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        secureTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!secureTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            secureTitle.setSelected(true);
                            setTextViewSelectedColor(secureTitle);
                            setTextViewDrawableLeftColorFilter(secureTitle);
                            // Show secure container
                            secureContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        bankTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!bankTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            bankTitle.setSelected(true);
                            setTextViewSelectedColor(bankTitle);
                            setTextViewDrawableLeftColorFilter(bankTitle);
                            // Show bank container
                            bankContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        expiryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!expiryTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            expiryTitle.setSelected(true);
                            setTextViewSelectedColor(expiryTitle);
                            setTextViewDrawableLeftColorFilter(expiryTitle);
                            // Show expiry title container
                            expiryContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        saveCardTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!saveCardTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            saveCardTitle.setSelected(true);
                            setTextViewSelectedColor(saveCardTitle);
                            setTextViewDrawableLeftColorFilter(saveCardTitle);
                            // Show save card container
                            saveCardContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        preAuthTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!preAuthTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            preAuthTitle.setSelected(true);
                            setTextViewSelectedColor(preAuthTitle);
                            setTextViewDrawableLeftColorFilter(preAuthTitle);
                            // Show pre auth container
                            preAuthContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        colorThemeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!colorThemeTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            colorThemeTitle.setSelected(true);
                            setTextViewSelectedColor(colorThemeTitle);
                            setTextViewDrawableLeftColorFilter(colorThemeTitle);
                            // Show color theme container
                            colorThemeContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        customBcaVaTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!customBcaVaTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            customBcaVaTitle.setSelected(true);
                            setTextViewSelectedColor(customBcaVaTitle);
                            setTextViewDrawableLeftColorFilter(customBcaVaTitle);
                            // Show color theme container
                            customBcaVaContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        subCompanyBcaVaTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!subCompanyBcaVaTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            subCompanyBcaVaTitle.setSelected(true);
                            setTextViewSelectedColor(subCompanyBcaVaTitle);
                            setTextViewDrawableLeftColorFilter(subCompanyBcaVaTitle);
                            // Show color theme container
                            subCompanyBcaVaContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        paymentChannelsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!paymentChannelsTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            paymentChannelsTitle.setSelected(true);
                            setTextViewSelectedColor(paymentChannelsTitle);
                            setTextViewDrawableLeftColorFilter(paymentChannelsTitle);
                            // Show color theme container
                            paymentChannelsContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        customPermataVaTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!customPermataVaTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            customPermataVaTitle.setSelected(true);
                            setTextViewSelectedColor(customPermataVaTitle);
                            setTextViewDrawableLeftColorFilter(customPermataVaTitle);
                            // Show color theme container
                            customPermataVaContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        customPermataRecipientTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!customPermataRecipientTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            customPermataRecipientTitle.setSelected(true);
                            setTextViewSelectedColor(customPermataRecipientTitle);
                            setTextViewDrawableLeftColorFilter(customPermataRecipientTitle);
                            // Show color theme container
                            customPermataRecipientContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        customBniVaTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!customBniVaTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            customBniVaTitle.setSelected(true);
                            setTextViewSelectedColor(customBniVaTitle);
                            setTextViewDrawableLeftColorFilter(customBniVaTitle);
                            // Show color theme container
                            customBniVaContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        installmentTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!installmentTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            installmentTitle.setSelected(true);
                            setTextViewSelectedColor(installmentTitle);
                            setTextViewDrawableLeftColorFilter(installmentTitle);
                            // Show pre auth container
                            installmentContainer.setVisibility(View.VISIBLE);
                            changeInstallmentContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        bniPointTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!bniPointTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            bniPointTitle.setSelected(true);
                            setTextViewSelectedColor(bniPointTitle);
                            setTextViewDrawableLeftColorFilter(bniPointTitle);
                            // Show pre auth container
                            bniPointContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        mandiriPointTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mandiriPointTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            mandiriPointTitle.setSelected(true);
                            setTextViewSelectedColor(mandiriPointTitle);
                            setTextViewDrawableLeftColorFilter(mandiriPointTitle);
                            // Show pre auth container
                            mandiriPointContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        autoReadSmsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!autoReadSmsTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            autoReadSmsTitle.setSelected(true);
                            setTextViewSelectedColor(autoReadSmsTitle);
                            setTextViewDrawableLeftColorFilter(autoReadSmsTitle);
                            // Show auto read selection container
                            autoReadSmsContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });

        currencyTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!currencyTitle.isSelected()) {
                            currencyTitle.setSelected(true);
                            setTextViewSelectedColor(currencyTitle);
                            setTextViewDrawableLeftColorFilter(currencyTitle);
                            currencyContainer.setVisibility(View.VISIBLE);
                        } else {
                            unselectAllTitles();
                            hideAllSelections();
                        }
                    }
                }, DELAY);
            }
        });
    }

    private void initColorThemeSelection() {
        selectedColor = DemoPreferenceHelper.getStringPreference(this, COLOR_THEME);
        if (selectedColor != null && !TextUtils.isEmpty(selectedColor)) {
            switch (selectedColor) {
                case DemoThemeConstants.BLUE_THEME:
                    defaultThemeSelection.setChecked(true);
                    break;
                case DemoThemeConstants.RED_THEME:
                    redThemeSelection.setChecked(true);
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    greenThemeSelection.setChecked(true);
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    orangeThemeSelection.setChecked(true);
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    blackThemeSelection.setChecked(true);
                    break;
                default:
                    defaultThemeSelection.setChecked(true);
                    break;
            }
        } else {
            selectedColor = DemoThemeConstants.BLUE_THEME;
            defaultThemeSelection.setChecked(true);
        }

        updateButtonColor();
        updateColorThemeTitle();
        updateRadioButtonColor();

        defaultThemeSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedColor = DemoThemeConstants.BLUE_THEME;
                    updateButtonColor();
                    setTextViewSelectedColor(colorThemeTitle);
                    setTextViewDrawableLeftColorFilter(colorThemeTitle);
                    updateColorThemeTitle();
                    updateRadioButtonColor();
                }
            }
        });

        redThemeSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedColor = DemoThemeConstants.RED_THEME;
                    updateButtonColor();
                    setTextViewSelectedColor(colorThemeTitle);
                    setTextViewDrawableLeftColorFilter(colorThemeTitle);
                    updateColorThemeTitle();
                    updateRadioButtonColor();
                }
            }
        });

        greenThemeSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedColor = DemoThemeConstants.GREEN_THEME;
                    updateButtonColor();
                    setTextViewSelectedColor(colorThemeTitle);
                    setTextViewDrawableLeftColorFilter(colorThemeTitle);
                    updateColorThemeTitle();
                    updateRadioButtonColor();
                }
            }
        });

        orangeThemeSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedColor = DemoThemeConstants.ORANGE_THEME;
                    updateButtonColor();
                    setTextViewSelectedColor(colorThemeTitle);
                    setTextViewDrawableLeftColorFilter(colorThemeTitle);
                    updateColorThemeTitle();
                    updateRadioButtonColor();
                }
            }
        });

        blackThemeSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedColor = DemoThemeConstants.BLACK_THEME;
                    updateButtonColor();
                    setTextViewSelectedColor(colorThemeTitle);
                    setTextViewDrawableLeftColorFilter(colorThemeTitle);
                    updateColorThemeTitle();
                    updateRadioButtonColor();
                }
            }
        });

    }

    private void initCardClickSelection() {
        String cardClickType = DemoPreferenceHelper.getStringPreference(this, CARD_CLICK_TYPE);

        if (cardClickType != null && !TextUtils.isEmpty(cardClickType)) {
            switch (cardClickType) {
                case Constants.CARD_NORMAL:
                    cardClickTitle.setText(R.string.credit_card_type_normal);
                    normalSelection.setChecked(true);
                    break;
                case Constants.CARD_TWO_CLICKS:
                    cardClickTitle.setText(R.string.credit_card_type_two_clicks);
                    twoClicksSelection.setChecked(true);
                    break;
                case Constants.CARD_ONE_CLICK:
                    cardClickTitle.setText(R.string.credit_card_type_one_click);
                    oneClickSelection.setChecked(true);
                    resetInstallmentSelection();
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    break;
                default:
                    cardClickTitle.setText(R.string.credit_card_type_normal);
                    normalSelection.setChecked(true);
                    break;
            }
        } else {
            cardClickTitle.setText(R.string.credit_card_type_normal);
            normalSelection.setChecked(true);
        }

        normalSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    cardClickTitle.setText(R.string.credit_card_type_normal);
                }
            }
        });

        twoClicksSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    cardClickTitle.setText(R.string.credit_card_type_two_clicks);
                    secureEnabledSelection.setChecked(true);
                }
            }
        });

        oneClickSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    cardClickTitle.setText(R.string.credit_card_type_one_click);
                    secureEnabledSelection.setChecked(true);
                    resetInstallmentSelection();
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                }
            }
        });
    }

    private void initSecureSelection() {
        int authenticationType = DemoPreferenceHelper.getIntegerPreference(this, SECURE_TYPE, 0);

        if (authenticationType == AUTH_TYPE_3DS) {
            secureTitle.setText(R.string.secure_type_enabled);
            secureEnabledSelection.setChecked(true);
        } else if (authenticationType == AUTH_TYPE_RBA) {
            secureTitle.setText(R.string.secure_type_rba_with_3ds);
            secureRbaWith3dsSelection.setChecked(true);
        } else if (authenticationType == AUTH_TYPE_RBA_NON_3DS) {
            secureTitle.setText(R.string.secure_type_rba_non_3ds);
            secureRbaNon3dsSelection.setChecked(true);
        } else {
            secureTitle.setText(R.string.secure_type_disabled);
            secureDisabledSelection.setChecked(true);
        }

        secureDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    secureTitle.setText(R.string.secure_type_disabled);
                }
            }
        });

        secureEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    secureTitle.setText(R.string.secure_type_enabled);
                }
            }
        });


        secureRbaWith3dsSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    secureTitle.setText(R.string.secure_type_rba_with_3ds);
                    if (oneClickSelection.isChecked()) {
                        normalSelection.setChecked(true);
                    }
                }
            }
        });

        secureRbaNon3dsSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    secureTitle.setText(R.string.secure_type_rba_non_3ds);
                    if (oneClickSelection.isChecked()) {
                        normalSelection.setChecked(true);
                    }
                }
            }
        });
    }

    private void initIssuingBankSelection() {
        String bank = DemoPreferenceHelper.getStringPreference(this, BANK_TYPE);
        if (bank != null && !TextUtils.isEmpty(bank)) {
            switch (bank) {
                case Constants.BANK_NONE:
                    bankTitle.setText(R.string.acquiring_bank_none);
                    bankNoneSelection.setChecked(true);
                    break;
                case Constants.BANK_BNI:
                    bankTitle.setText(R.string.acquiring_bank_by_bni);
                    bankBniSelection.setChecked(true);
                    secureEnabledSelection.setChecked(true);

                    break;
                case Constants.BANK_MANDIRI:
                    bankTitle.setText(R.string.acquiring_bank_by_mandiri);
                    bankMandiriSelection.setChecked(true);
                    secureEnabledSelection.setChecked(true);

                    break;
                case Constants.BANK_BCA:
                    bankTitle.setText(R.string.acquiring_bank_by_bca);
                    bankBcaSelection.setChecked(true);
                    secureEnabledSelection.setChecked(true);

                    break;
                case Constants.BANK_MAYBANK:
                    bankTitle.setText(R.string.acquiring_bank_by_maybank);
                    bankMaybankSelection.setChecked(true);
                    secureEnabledSelection.setChecked(true);

                    break;
                case Constants.BANK_BRI:
                    bankTitle.setText(R.string.acquiring_bank_by_bri);
                    bankBriSelection.setChecked(true);
                    secureEnabledSelection.setChecked(true);

                    break;
                case Constants.BANK_CIMB:
                    bankTitle.setText(R.string.acquiring_bank_by_cimb);
                    bankCimbSelection.setChecked(true);
                    secureEnabledSelection.setChecked(true);
                    break;
                case Constants.BANK_MEGA:
                    bankTitle.setText(R.string.acquiring_bank_by_mega);
                    bankCimbSelection.setChecked(true);
                    secureEnabledSelection.setChecked(true);
                    break;
                default:
                    bankTitle.setText(R.string.acquiring_bank_none);
                    bankNoneSelection.setChecked(true);
                    break;
            }
        } else {
            bankTitle.setText(R.string.acquiring_bank_none);
            bankNoneSelection.setChecked(true);
        }

        bankNoneSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bankTitle.setText(R.string.acquiring_bank_none);
                }
            }
        });

        bankBniSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bankTitle.setText(R.string.acquiring_bank_by_bni);
                }
            }
        });

        bankMandiriSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bankTitle.setText(R.string.acquiring_bank_by_mandiri);
                }
            }
        });

        bankBcaSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bankTitle.setText(R.string.acquiring_bank_by_bca);
                }
            }
        });

        bankMaybankSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bankTitle.setText(R.string.acquiring_bank_by_maybank);
                }
            }
        });

        bankBriSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bankTitle.setText(R.string.acquiring_bank_by_bri);
                }
            }
        });

        bankCimbSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bankTitle.setText(R.string.acquiring_bank_by_cimb);
                }
            }
        });

        bankMegaSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bankTitle.setText(R.string.acquiring_bank_by_mega);
                }
            }
        });
    }

    private void initCustomExpirySelection() {
        String expiry = DemoPreferenceHelper.getStringPreference(this, EXPIRY_TPE);
        if (expiry != null && !TextUtils.isEmpty(expiry)) {
            switch (expiry) {
                case Constants.EXPIRY_NONE:
                    expiryTitle.setText(R.string.custom_expiry_none);
                    expiryNoneSelection.setChecked(true);
                    break;
                case Constants.EXPIRY_MINUTE:
                    expiryTitle.setText(R.string.custom_expiry_one_minute);
                    expiryOneMinuteSelection.setChecked(true);
                    break;
                case Constants.EXPIRY_HOUR:
                    expiryTitle.setText(R.string.custom_expiry_one_hour);
                    expiryOneHourSelection.setChecked(true);
                    break;
                default:
                    expiryTitle.setText(R.string.custom_expiry_none);
                    expiryNoneSelection.setChecked(true);
                    break;
            }
        } else {
            expiryTitle.setText(R.string.custom_expiry_none);
            expiryNoneSelection.setChecked(true);
        }

        expiryNoneSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    expiryTitle.setText(R.string.custom_expiry_none);
                }
            }
        });

        expiryOneMinuteSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    expiryTitle.setText(R.string.custom_expiry_one_minute);
                }
            }
        });

        expiryOneHourSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    expiryTitle.setText(R.string.custom_expiry_one_hour);
                }
            }
        });
    }

    private void initDefaultSaveCardSelection() {
        boolean defaultSaveCardEnabled = DemoPreferenceHelper.getBooleanPreference(this, SAVE_CARD_TYPE, false);
        if (defaultSaveCardEnabled) {
            saveCardTitle.setText(R.string.default_save_card_enabled);
            saveCardEnabledSelection.setChecked(true);
        } else {
            saveCardTitle.setText(R.string.default_save_card_disabled);
            saveCardDisabledSelection.setChecked(true);
        }

        saveCardEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    saveCardTitle.setText(R.string.default_save_card_enabled);
                }
            }
        });

        saveCardDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    saveCardTitle.setText(R.string.default_save_card_disabled);
                }
            }
        });
    }

    private void initPaymentChannelsSelections() {
        SelectPaymentMethodListViewModel listViewModel = LocalDataHandler.readObject(PAYMENT_CHANNELS_TYPE, SelectPaymentMethodListViewModel.class);
        if (listViewModel != null
                && listViewModel.getEnabledPayments() != null
                && !listViewModel.getEnabledPayments().isEmpty()) {
            enabledPayments = AppUtils.mapPaymentMethods(this, listViewModel.getEnabledPayments());
            paymentChannelsTitle.setText(R.string.payment_channels_selected);
            if (isContainEChannel(enabledPayments)) {
                paymentChannelsSelectedSelection.setText(getString(R.string.payment_channels_selection_show_selected_format, enabledPayments.size() - 1));
            } else {
                paymentChannelsSelectedSelection.setText(getString(R.string.payment_channels_selection_show_selected_format, enabledPayments.size()));
            }
            paymentChannelsSelectedSelection.setChecked(true);
            initEditPaymentButton(AppUtils.getPaymentList(DemoConfigActivity.this, mapEnabledPayments()));
        } else {
            paymentChannelsTitle.setText(R.string.payment_channels_show_all);
            paymentChannelsAllSelection.setText(R.string.payment_channels_selection_show_all);
            paymentChannelsAllSelection.setChecked(true);
            disableEditPaymentChannels();
        }

        paymentChannelsAllSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    List<EnabledPayment> enabledPayments = AppUtils.getDefaultPaymentList(DemoConfigActivity.this);
                    DemoConfigActivity.this.enabledPayments = AppUtils.mapPaymentMethods(DemoConfigActivity.this, enabledPayments);
                    resetSelectedPaymentChannels();
                    paymentChannelsTitle.setText(R.string.payment_channels_show_all);
                }
            }
        });

        paymentChannelsSelectedSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (editPaymentChannels.getVisibility() != View.VISIBLE) {
                    if (checked) {
                        SelectPaymentMethodDialogFragment fragment = SelectPaymentMethodDialogFragment.newInstance(getSelectedColorPrimaryDark(), new SelectPaymentMethodListener() {
                            @Override
                            public void onCanceled() {
                                paymentChannelsAllSelection.setChecked(true);
                            }

                            @Override
                            public void onSelectPaymentMethod(List<SelectPaymentMethodViewModel> enabledPayments) {
                                DemoConfigActivity.this.enabledPayments = enabledPayments;
                                paymentChannelsTitle.setText(R.string.payment_channels_selected);
                                paymentChannelsSelectedSelection.setText(getString(R.string.payment_channels_selection_show_selected_format, enabledPayments.size()));
                                initEditPaymentButton(AppUtils.getPaymentList(DemoConfigActivity.this, mapEnabledPayments()));

                            }

                            @Override
                            public void onSelectAll(List<SelectPaymentMethodViewModel> enabledPayments) {
                                paymentChannelsTitle.setText(R.string.payment_channels_show_all);
                                paymentChannelsAllSelection.setChecked(true);
                                resetSelectedPaymentChannels();
                                disableEditPaymentChannels();
                            }
                        });
                        fragment.show(getSupportFragmentManager(), "");
                    }
                }
            }
        });
    }

    private boolean isContainEChannel(List<SelectPaymentMethodViewModel> enabledPayments) {
        for (SelectPaymentMethodViewModel enabledPayment : enabledPayments) {
            if (enabledPayment.getMethodType().equals(getString(R.string.payment_mandiri_bill_payment))) {
                return true;
            }
        }
        return false;
    }

    private void initEditPaymentButton(final List<EnabledPayment> enabledPayments) {
        editPaymentChannels.setVisibility(View.VISIBLE);
        editPaymentChannels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPaymentMethodDialogFragment fragment = SelectPaymentMethodDialogFragment.newInstance(getSelectedColorPrimaryDark(), enabledPayments, new SelectPaymentMethodListener() {
                    @Override
                    public void onCanceled() {
                        // Do nothing
                    }

                    @Override
                    public void onSelectPaymentMethod(List<SelectPaymentMethodViewModel> enabledPayments) {
                        DemoConfigActivity.this.enabledPayments = enabledPayments;
                        paymentChannelsTitle.setText(R.string.payment_channels_selected);
                        paymentChannelsSelectedSelection.setText(getString(R.string.payment_channels_selection_show_selected_format, enabledPayments.size()));
                        initEditPaymentButton(AppUtils.getPaymentList(DemoConfigActivity.this, mapEnabledPayments()));
                    }

                    @Override
                    public void onSelectAll(List<SelectPaymentMethodViewModel> enabledPayments) {
                        paymentChannelsTitle.setText(R.string.payment_channels_show_all);
                        paymentChannelsAllSelection.setChecked(true);
                        resetSelectedPaymentChannels();
                        disableEditPaymentChannels();
                    }
                });
                fragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void resetSelectedPaymentChannels() {
        paymentChannelsSelectedSelection.setText(getString(R.string.payment_channels_selection_show_selected));
    }

    private void disableEditPaymentChannels() {
        editPaymentChannels.setVisibility(View.INVISIBLE);
        editPaymentChannels.setOnClickListener(null);
    }

    private void initCustomBcaVaSelection() {
        String customBcaVaNumber = DemoPreferenceHelper.getStringPreference(this, CUSTOM_BCA_VA_NUMBER);
        if (!TextUtils.isEmpty(customBcaVaNumber)) {
            customBcaVaTitle.setText(getString(R.string.custom_bca_va_enabled));
            customBcaVaEnabledSelection.setChecked(true);
            customBcaVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, customBcaVaNumber));
            initEditBcaVaButton();
        } else {
            customBcaVaTitle.setText(getString(R.string.custom_bca_va_disabled));
            customBcaVaDisabledSelection.setChecked(true);
            customBcaVaEnabledSelection.setText(getString(R.string.enabled));
            disableEditBcaVa();
        }

        customBcaVaEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (editBcaVaButton.getVisibility() != View.VISIBLE) {
                        final CustomVaInputDialogFragment fragment = CustomVaInputDialogFragment.newInstance(getString(R.string.bca_va_title), getSelectedColorPrimaryDark(), new CustomVaDialogListener() {
                            @Override
                            public void onOkClicked(String input) {
                                customBcaVaTitle.setText(R.string.custom_bca_va_enabled);
                                customBcaVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                                saveBcaVaNumber();
                                initEditBcaVaButton();
                            }

                            @Override
                            public void onCancelClicked() {
                                customBcaVaDisabledSelection.setChecked(true);
                            }
                        });
                        fragment.show(getSupportFragmentManager(), "");
                    }
                }
            }
        });

        customBcaVaDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    customBcaVaTitle.setText(R.string.custom_bca_va_disabled);
                }
            }
        });
    }

    private void initCustomPermataVaSelection() {
        String customPermataVaNumber = DemoPreferenceHelper.getStringPreference(this, CUSTOM_PERMATA_VA_NUMBER);
        if (!TextUtils.isEmpty(customPermataVaNumber)) {
            customPermataVaTitle.setText(getString(R.string.custom_permata_va_enabled));
            customPermataVaEnabledSelection.setChecked(true);
            customPermataVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, customPermataVaNumber));
            initEditPermataVaButton();
        } else {
            customPermataVaTitle.setText(getString(R.string.custom_permata_va_disabled));
            customPermataVaDisabledSelection.setChecked(true);
            customPermataVaEnabledSelection.setText(getString(R.string.enabled));
            disableEditPermataVa();
        }

        customPermataVaEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (editPermataVaButton.getVisibility() != View.VISIBLE) {
                        final CustomPermataVaInputDialogFragment fragment = CustomPermataVaInputDialogFragment.newInstance(getSelectedColorPrimaryDark(), false, new CustomVaDialogListener() {
                            @Override
                            public void onOkClicked(String input) {
                                customPermataVaTitle.setText(R.string.custom_permata_va_enabled);
                                customPermataVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                                savePermataVaNumber();
                                initEditPermataVaButton();
                            }

                            @Override
                            public void onCancelClicked() {
                                customPermataVaDisabledSelection.setChecked(true);
                            }
                        });
                        fragment.show(getSupportFragmentManager(), "");
                    }
                }
            }
        });

        customPermataVaDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    customPermataVaTitle.setText(R.string.custom_permata_va_disabled);
                }
            }
        });
    }

    private void initCustomPermataRecipientSelection() {
        String customPermataRecipient = DemoPreferenceHelper.getStringPreference(this, CUSTOM_PERMATA_RECIPIENT);
        if (!TextUtils.isEmpty(customPermataRecipient)) {
            customPermataRecipientTitle.setText(getString(R.string.custom_permata_recipient_enabled));
            customPermataRecipientEnabledSelection.setChecked(true);
            customPermataRecipientEnabledSelection.setText(getString(R.string.custom_va_enabled_format, customPermataRecipient));
            initEditPermataRecipientButton();
        } else {
            customPermataRecipientTitle.setText(getString(R.string.custom_permata_recipient_disabled));
            customPermataRecipientDisabledSelection.setChecked(true);
            customPermataRecipientEnabledSelection.setText(getString(R.string.enabled));
            disableEditPermataRecipient();
        }

        customPermataRecipientEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (editPermataRecipientButton.getVisibility() != View.VISIBLE) {
                        final CustomPermataVaInputDialogFragment fragment = CustomPermataVaInputDialogFragment.newInstance(getSelectedColorPrimaryDark(), true, new CustomVaDialogListener() {
                            @Override
                            public void onOkClicked(String input) {
                                customPermataRecipientTitle.setText(R.string.custom_permata_recipient_enabled);
                                customPermataRecipientEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input.toUpperCase()));
                                savePermataRecipient();
                                initEditPermataRecipientButton();
                            }

                            @Override
                            public void onCancelClicked() {
                                customPermataRecipientDisabledSelection.setChecked(true);
                            }
                        });
                        fragment.show(getSupportFragmentManager(), "");
                    }
                }
            }
        });

        customPermataRecipientDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    customPermataRecipientTitle.setText(R.string.custom_permata_recipient_disabled);
                }
            }
        });
    }

    private void initCustomBniVaSelection() {
        String customBniVaNumber = DemoPreferenceHelper.getStringPreference(this, CUSTOM_BNI_VA_NUMBER);
        if (!TextUtils.isEmpty(customBniVaNumber)) {
            customBniVaTitle.setText(getString(R.string.custom_bni_va_enabled));
            customBniVaEnabledSelection.setChecked(true);
            customBniVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, customBniVaNumber));
            initEditBniVaButton();
        } else {
            customBniVaTitle.setText(getString(R.string.custom_bni_va_disabled));
            customBniVaDisabledSelection.setChecked(true);
            customBniVaEnabledSelection.setText(getString(R.string.enabled));
            disableEditBniVa();
        }

        customBniVaEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (editBniVaButton.getVisibility() != View.VISIBLE) {
                        final CustomVaInputDialogFragment fragment = CustomVaInputDialogFragment.newInstance(getString(R.string.bni_va_title), getSelectedColorPrimaryDark(), new CustomVaDialogListener() {
                            @Override
                            public void onOkClicked(String input) {
                                customBniVaTitle.setText(R.string.custom_bni_va_enabled);
                                customBniVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                                saveBniVaNumber();
                                initEditBniVaButton();
                            }

                            @Override
                            public void onCancelClicked() {
                                customBniVaDisabledSelection.setChecked(true);
                            }
                        });
                        fragment.show(getSupportFragmentManager(), "");
                    }
                }
            }
        });

        customBniVaDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    customBniVaTitle.setText(R.string.custom_bni_va_disabled);
                }
            }
        });
    }

    private void initSubCompanyBcaVaSelection() {
        String subCompanyBcaVaNumber = DemoPreferenceHelper.getStringPreference(this, SUBCOMPANY_BCA_VA_NUMBER);
        if (!TextUtils.isEmpty(subCompanyBcaVaNumber)) {
            subCompanyBcaVaTitle.setText(getString(R.string.subcompany_bca_va_enabled));
            subCompanyBcaVaEnabledSelection.setChecked(true);
            subCompanyBcaVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, subCompanyBcaVaNumber));
            initEditSubCompanyBcaVaButton();
        } else {
            subCompanyBcaVaTitle.setText(getString(R.string.subcompany_bca_va_disabled));
            subCompanyBcaVaDisabledSelection.setChecked(true);
            subCompanyBcaVaEnabledSelection.setText(getString(R.string.enabled));
            disableEditSubCompanyBcaVa();
        }

        subCompanyBcaVaEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (editSubCompanyBcaVaButton.getVisibility() != View.VISIBLE) {
                        final CustomVaInputDialogFragment fragment = CustomVaInputDialogFragment.newInstance(getString(R.string.subcompany_bca_va_title), getSelectedColorPrimaryDark(), true, new CustomVaDialogListener() {
                            @Override
                            public void onOkClicked(String input) {
                                subCompanyBcaVaTitle.setText(R.string.subcompany_bca_va_enabled);
                                subCompanyBcaVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                                saveSubCompanyBcaVaNumber();
                                initEditSubCompanyBcaVaButton();
                            }

                            @Override
                            public void onCancelClicked() {
                                subCompanyBcaVaDisabledSelection.setChecked(true);
                            }
                        });
                        fragment.show(getSupportFragmentManager(), "");
                    }
                }
            }
        });

        subCompanyBcaVaDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    subCompanyBcaVaTitle.setText(R.string.subcompany_bca_va_disabled);
                }
            }
        });
    }

    private void initEditBcaVaButton() {
        editBcaVaButton.setVisibility(View.VISIBLE);
        editBcaVaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaNumber = DemoPreferenceHelper.getStringPreference(DemoConfigActivity.this, CUSTOM_BCA_VA_NUMBER);
                final CustomVaInputDialogFragment fragment = CustomVaInputDialogFragment.newInstance(vaNumber, getString(R.string.bca_va_title), getSelectedColorPrimaryDark(), new CustomVaDialogListener() {
                    @Override
                    public void onOkClicked(String input) {
                        customBcaVaTitle.setText(R.string.custom_bca_va_enabled);
                        customBcaVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                        saveBcaVaNumber();
                    }

                    @Override
                    public void onCancelClicked() {
                        // Do nothing
                    }
                });
                fragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void disableEditBcaVa() {
        editBcaVaButton.setVisibility(View.INVISIBLE);
        editBcaVaButton.setOnClickListener(null);
    }

    private void initEditPermataRecipientButton() {
        editPermataRecipientButton.setVisibility(View.VISIBLE);
        editPermataRecipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipient = DemoPreferenceHelper.getStringPreference(DemoConfigActivity.this, CUSTOM_PERMATA_RECIPIENT);

                final CustomPermataVaInputDialogFragment fragment = CustomPermataVaInputDialogFragment.newInstance(recipient, getSelectedColorPrimaryDark(), true, new CustomVaDialogListener() {
                    @Override
                    public void onOkClicked(String input) {
                        customPermataRecipientTitle.setText(R.string.custom_permata_recipient_enabled);
                        customPermataRecipientEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                        savePermataRecipient();
                    }

                    @Override
                    public void onCancelClicked() {
                        // Do nothing
                    }
                });
                fragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void initEditPermataVaButton() {
        editPermataVaButton.setVisibility(View.VISIBLE);
        editPermataVaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaNumber = DemoPreferenceHelper.getStringPreference(DemoConfigActivity.this, CUSTOM_PERMATA_VA_NUMBER);

                final CustomPermataVaInputDialogFragment fragment = CustomPermataVaInputDialogFragment.newInstance(vaNumber, getSelectedColorPrimaryDark(), false, new CustomVaDialogListener() {
                    @Override
                    public void onOkClicked(String input) {
                        customPermataVaTitle.setText(R.string.custom_permata_va_enabled);
                        customPermataVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                        savePermataVaNumber();
                    }

                    @Override
                    public void onCancelClicked() {
                        // Do nothing
                    }
                });
                fragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void initEditBniVaButton() {
        editBniVaButton.setVisibility(View.VISIBLE);
        editBniVaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaNumber = DemoPreferenceHelper.getStringPreference(DemoConfigActivity.this, CUSTOM_BNI_VA_NUMBER);

                final CustomVaInputDialogFragment fragment = CustomVaInputDialogFragment.newInstance(vaNumber, getString(R.string.bni_va_title), getSelectedColorPrimaryDark(), new CustomVaDialogListener() {
                    @Override
                    public void onOkClicked(String input) {
                        customBniVaTitle.setText(R.string.custom_bni_va_enabled);
                        customBniVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                        saveBniVaNumber();
                    }

                    @Override
                    public void onCancelClicked() {
                        // Do nothing
                    }
                });
                fragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void disableEditPermataVa() {
        editPermataVaButton.setVisibility(View.GONE);
        editPermataVaButton.setOnClickListener(null);
    }

    private void disableEditPermataRecipient() {
        editPermataRecipientButton.setVisibility(View.GONE);
        editPermataRecipientButton.setOnClickListener(null);
    }

    private void disableEditBniVa() {
        editBniVaButton.setVisibility(View.GONE);
        editBniVaButton.setOnClickListener(null);
    }

    private void initEditSubCompanyBcaVaButton() {
        editSubCompanyBcaVaButton.setVisibility(View.VISIBLE);
        editSubCompanyBcaVaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subCompany = DemoPreferenceHelper.getStringPreference(DemoConfigActivity.this, SUBCOMPANY_BCA_VA_NUMBER);
                final CustomVaInputDialogFragment fragment = CustomVaInputDialogFragment.newInstance(subCompany, getString(R.string.subcompany_bca_va_title), getSelectedColorPrimaryDark(), true, new CustomVaDialogListener() {
                    @Override
                    public void onOkClicked(String input) {
                        subCompanyBcaVaTitle.setText(R.string.subcompany_bca_va_enabled);
                        subCompanyBcaVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                        saveSubCompanyBcaVaNumber();
                    }

                    @Override
                    public void onCancelClicked() {
                        // Do nothing
                    }
                });
                fragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void disableEditSubCompanyBcaVa() {
        editSubCompanyBcaVaButton.setVisibility(View.INVISIBLE);
        editSubCompanyBcaVaButton.setOnClickListener(null);
    }

    private void initPreAuthSelection() {
        boolean preAuthEnabled = DemoPreferenceHelper.getBooleanPreference(this, PRE_AUTH_TYPE, false);
        if (preAuthEnabled) {
            preAuthTitle.setText(R.string.pre_auth_enabled);
            preAuthEnabledSelection.setChecked(true);
        } else {
            preAuthTitle.setText(R.string.pre_auth_disabled);
            preAuthDisabledSelection.setChecked(true);
        }

        preAuthEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    preAuthTitle.setText(R.string.pre_auth_enabled);
                }
            }
        });

        preAuthDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    preAuthTitle.setText(R.string.pre_auth_disabled);
                }
            }
        });
    }

    private void initInstallmentSelection() {
        String installmentType = DemoPreferenceHelper.getStringPreference(this, INSTALLMENT_TYPE);
        installmentRequired = DemoPreferenceHelper.getBooleanPreference(this, INSTALLMENT_REQUIRED, false);
        if (installmentType != null && !TextUtils.isEmpty(installmentType)) {
            hideEditInstallmentOption();
            switch (installmentType) {
                case Constants.INSTALLMENT_BNI:
                    String bniTitle = getString(R.string.using_bni_installment);
                    installmentTitle.setText(bniTitle);
                    installmentBniSelection.setChecked(true);
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    updateSelectedInstallment();
                    showEditInstallmentBniOption();
                    resetAcquiringBank();
                    resetOneClickSelection();
                    break;
                case Constants.INSTALLMENT_MANDIRI:
                    String mandiriTitle = getString(R.string.using_mandiri_installment);
                    installmentTitle.setText(mandiriTitle);
                    installmentMandiriSelection.setChecked(true);
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    updateSelectedInstallment();
                    showEditInstallmentMandiriOption();
                    resetAcquiringBank();
                    resetOneClickSelection();
                    break;
                case Constants.INSTALLMENT_BCA:
                    String bcaTitle = getString(R.string.using_bca_installment);
                    installmentTitle.setText(bcaTitle);
                    installmentBcaSelection.setChecked(true);
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    updateSelectedInstallment();
                    showEditInstallmentBcaOption();
                    setBcaAcquiringBank();
                    resetOneClickSelection();
                    break;
                case Constants.INSTALLMENT_BRI:
                    String briTitle = getString(R.string.using_bri_installment);
                    installmentTitle.setText(briTitle);
                    installmentBriSelection.setChecked(true);
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    updateSelectedInstallment();
                    showEditInstallmentBriOption();
                    setBriAcquiringBank();
                    resetOneClickSelection();
                    break;

                case Constants.INSTALLMENT_CIMB:
                    String cimbTitle = getString(R.string.using_cimb_installment);
                    installmentTitle.setText(cimbTitle);
                    installmentCimbSelection.setChecked(true);
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    updateSelectedInstallment();
                    showEditInstallmentCimbOption();
                    setCimbAcquiringBank();
                    resetOneClickSelection();
                    break;

                case Constants.INSTALLMENT_MAYBANK:
                    String maybankTitle = getString(R.string.using_maybank_installment);
                    installmentTitle.setText(maybankTitle);
                    installmentMaybankSelection.setChecked(true);
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    updateSelectedInstallment();
                    showEditInstallmentMaybankOption();
                    setMaybankAcquiringBank();
                    resetOneClickSelection();
                    break;

                default:
                    installmentTitle.setText(R.string.no_installment);
                    noInstallmentSelection.setChecked(true);
                    hideEditInstallmentOption();
                    resetAcquiringBank();
                    break;
            }
        } else {
            installmentTitle.setText(R.string.no_installment);
            noInstallmentSelection.setChecked(true);
            hideEditInstallmentOption();
            resetAcquiringBank();
        }

        noInstallmentSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    installmentTitle.setText(getString(R.string.no_installment));
                    noInstallmentSelection.setChecked(true);
                    hideAllSelections();
                    resetSelectedInstallment();
                    resetAcquiringBank();
                }
            }
        });

        installmentBniSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    String title = getString(R.string.using_bni_installment);
                    installmentTitle.setText(title);
                    installmentBniSelection.setChecked(true);
                    hideEditInstallmentOption();
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    showInstallmentDialog(false);
                    showEditInstallmentBniOption();
                    resetAcquiringBank();
                    resetOneClickSelection();
                }
            }
        });

        installmentMandiriSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    String title = getString(R.string.using_mandiri_installment);
                    installmentTitle.setText(title);
                    installmentMandiriSelection.setChecked(true);
                    hideEditInstallmentOption();
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    showInstallmentDialog(false);
                    showEditInstallmentMandiriOption();
                    resetAcquiringBank();
                    resetOneClickSelection();
                }
            }
        });

        installmentBcaSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    String title = getString(R.string.using_bca_installment);
                    installmentTitle.setText(title);
                    installmentBcaSelection.setChecked(true);
                    hideEditInstallmentOption();
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    showInstallmentDialog(false);
                    showEditInstallmentBcaOption();
                    setBcaAcquiringBank();
                    resetOneClickSelection();
                }
            }
        });

        installmentBriSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    String title = getString(R.string.using_bri_installment);
                    installmentTitle.setText(title);
                    installmentBriSelection.setChecked(true);
                    hideEditInstallmentOption();
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    showInstallmentDialog(false);
                    showEditInstallmentBriOption();
                    setBriAcquiringBank();
                    resetOneClickSelection();
                }
            }
        });

        installmentCimbSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    String title = getString(R.string.using_cimb_installment);
                    installmentTitle.setText(title);
                    installmentCimbSelection.setChecked(true);
                    hideEditInstallmentOption();
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    showInstallmentDialog(false);
                    showEditInstallmentCimbOption();
                    setCimbAcquiringBank();
                    resetOneClickSelection();
                }
            }
        });

        installmentMaybankSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    String title = getString(R.string.using_maybank_installment);
                    installmentTitle.setText(title);
                    installmentMaybankSelection.setChecked(true);
                    hideEditInstallmentOption();
                    resetBniPointSelection();
                    resetMandiriPointSelection();
                    showInstallmentDialog(false);
                    showEditInstallmentMaybankOption();
                    setMaybankAcquiringBank();
                    resetOneClickSelection();
                }
            }
        });
    }

    private void resetAcquiringBank() {
        bankNoneSelection.setChecked(true);
    }

    private void setBriAcquiringBank() {
        bankBriSelection.setChecked(true);
    }

    private void setCimbAcquiringBank() {
        bankCimbSelection.setChecked(true);
    }

    private void setMaybankAcquiringBank() {
        bankMaybankSelection.setChecked(true);
    }

    private void setMegaAcquiringBank() {
        bankMegaSelection.setChecked(true);
    }

    private void setBcaAcquiringBank() {
        bankBcaSelection.setChecked(true);
        secureEnabledSelection.setChecked(true);
    }

    private void initChangeInstallmentOption() {
        editInstallmentMandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstallmentDialog(installmentRequired);
            }
        });

        editInstallmentBca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstallmentDialog(installmentRequired);
            }
        });

        editInstallmentBni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstallmentDialog(installmentRequired);
            }
        });

        editInstallmentBri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstallmentDialog(installmentRequired);
            }
        });

        editInstallmentCimb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstallmentDialog(installmentRequired);
            }
        });

        editInstallmentMaybank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstallmentDialog(installmentRequired);
            }
        });
    }

    private void hideEditInstallmentOption() {
        editInstallmentMandiri.setVisibility(View.INVISIBLE);
        editInstallmentBca.setVisibility(View.INVISIBLE);
        editInstallmentBni.setVisibility(View.INVISIBLE);
        editInstallmentBri.setVisibility(View.INVISIBLE);
        editInstallmentCimb.setVisibility(View.INVISIBLE);
        editInstallmentMaybank.setVisibility(View.INVISIBLE);
    }

    private void showEditInstallmentMandiriOption() {
        editInstallmentMandiri.setVisibility(View.VISIBLE);
    }

    private void showEditInstallmentBcaOption() {
        editInstallmentBca.setVisibility(View.VISIBLE);
    }

    private void showEditInstallmentBniOption() {
        editInstallmentBni.setVisibility(View.VISIBLE);
    }

    private void showEditInstallmentBriOption() {
        editInstallmentBri.setVisibility(View.VISIBLE);
    }

    private void showEditInstallmentCimbOption() {
        editInstallmentCimb.setVisibility(View.VISIBLE);
    }

    private void showEditInstallmentMaybankOption() {
        editInstallmentMaybank.setVisibility(View.VISIBLE);
    }

    private void showInstallmentDialog(boolean isRequired) {
        InstallmentDialogFragment installmentDialogFragment = InstallmentDialogFragment.newInstance(isRequired, getSelectedColorPrimaryDark(), new CustomInstallmentDialogListener() {
            @Override
            public void onOkClicked(boolean reqired) {
                installmentRequired = reqired;
                updateSelectedInstallment();
            }

            @Override
            public void onCancelClicked() {

            }
        });

        installmentDialogFragment.show(getSupportFragmentManager(), "");

    }

    private void updateSelectedInstallment() {
        resetSelectedInstallment();
        int id = installmentContainer.getCheckedRadioButtonId();
        DemoRadioButton selectedSelection = ((DemoRadioButton) findViewById(id));
        String newLabel = selectedSelection.getText().toString();
        if (installmentRequired) {
            newLabel = newLabel + LABEL_INSTALLMENT_REQUIRED;
        } else {
            newLabel = newLabel + LABEL_INSTALLMENT_OPTIONAL;
        }

        selectedSelection.setText(newLabel);
    }

    private void resetSelectedInstallment() {
        installmentMandiriSelection.setText(getString(R.string.installment_mandiri));
        installmentBcaSelection.setText(getString(R.string.installment_bca));
        installmentBniSelection.setText(getString(R.string.installment_bni));
        installmentBriSelection.setText(getString(R.string.installment_bri));
        installmentCimbSelection.setText(getString(R.string.installment_cimb));
        installmentMaybankSelection.setText(getString(R.string.installment_maybank));
    }

    private void resetBniPointSelection() {
        bniPointOnlyDisabledSelection.setChecked(true);
    }

    private void resetMandiriPointSelection() {
        mandiriPointOnlyDisabledSelection.setChecked(true);
    }


    private void initBniPointSelection() {
        final boolean bniPointEnabled = DemoPreferenceHelper.getBooleanPreference(this, BNI_POINT_TYPE, false);
        if (bniPointEnabled) {
            bniPointTitle.setText(R.string.bni_point_only_enabled);
            bniPointOnlyEnabledSelection.setChecked(true);
            resetInstallmentSelection();
            resetOneClickSelection();
        } else {
            bniPointTitle.setText(R.string.bni_point_only_disabled);
            bniPointOnlyDisabledSelection.setChecked(true);
        }

        bniPointOnlyEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bniPointTitle.setText(R.string.bni_point_only_enabled);
                    resetInstallmentSelection();
                    resetOneClickSelection();
                    resetMandiriPointSelection();
                }
            }
        });

        bniPointOnlyDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    bniPointTitle.setText(R.string.bni_point_only_disabled);
                }
            }
        });
    }

    private void initMandiriPointSelection() {
        final boolean mandiriPointEnabled = DemoPreferenceHelper.getBooleanPreference(this, MANDIRI_POINT_TYPE, false);
        if (mandiriPointEnabled) {
            mandiriPointTitle.setText(R.string.mandiri_point_only_enabled);
            mandiriPointOnlyEnabledSelection.setChecked(true);
            resetInstallmentSelection();
            resetOneClickSelection();
        } else {
            mandiriPointTitle.setText(R.string.mandiri_point_only_disabled);
            mandiriPointOnlyDisabledSelection.setChecked(true);
        }

        mandiriPointOnlyEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    mandiriPointTitle.setText(R.string.mandiri_point_only_enabled);
                    resetInstallmentSelection();
                    resetOneClickSelection();
                    resetBniPointSelection();
                    secureEnabledSelection.setChecked(true);
                }
            }
        });

        mandiriPointOnlyDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    mandiriPointTitle.setText(R.string.mandiri_point_only_disabled);
                }
            }
        });
    }

    private void initAutoReadOtpSelection() {
        boolean autoReadOtpEnabled = DemoPreferenceHelper.getBooleanPreference(this, AUTO_READ_SMS_TYPE, false);
        if (autoReadOtpEnabled) {
            autoReadSmsTitle.setText(R.string.auto_read_sms_enabled);
            autoReadSmsEnabledSelection.setChecked(true);
        } else {
            autoReadSmsTitle.setText(R.string.auto_read_sms_disabled);
            autoReadSmsDisabledSelection.setChecked(true);
        }

        autoReadSmsEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    autoReadSmsTitle.setText(R.string.auto_read_sms_enabled);
                }
            }
        });

        autoReadSmsDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    autoReadSmsTitle.setText(R.string.auto_read_sms_disabled);
                }
            }
        });
    }

    private void initCurrencySelection() {
        final int currencyType = DemoPreferenceHelper.getIntegerPreference(this, CURRENCY_TYPE, 0);
        if (currencyType == 1) {
            currencyTitle.setText(R.string.currency_type_sgd);
            currencySgdSelection.setChecked(true);
        } else {
            currencyTitle.setText(R.string.currency_type_idr);
            currencyIdrSelection.setChecked(true);
        }

        currencyIdrSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    currencyTitle.setText(R.string.currency_type_idr);
                }
            }
        });

        currencySgdSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    currencyTitle.setText(R.string.currency_type_sgd);
                }
            }
        });
    }


    private void resetInstallmentSelection() {
        noInstallmentSelection.setChecked(true);
    }

    private void resetOneClickSelection() {
        if (oneClickSelection.isChecked()) {
            normalSelection.setChecked(true);
        }
    }

    private void initNextButton() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveColorThemeSelection();
                saveCardClickSelection();
                saveSecureTypeSelection();
                saveIssuingBankSelection();
                saveCustomExpirySelection();
                saveDefaultSaveCardSelection();
                saveBcaVaNumber();
                savePermataVaNumber();
                savePermataRecipient();
                saveBniVaNumber();
                savePreAuthSelection();
                saveInstallmentSelection();
                saveBniPointSelection();
                saveMandiriPointSelection();
                saveAutoReadSmsSelection();
                saveEnabledPayments();
                saveCurrencySelection();

                TransactionRequest transactionRequest = initializePurchaseRequest();
                MidtransSDK.getInstance().setTransactionRequest(transactionRequest);

                // change just for demo app purpose
                changeClientKeyIfOneClick();
                startActivity(new Intent(DemoConfigActivity.this, DemoProductListActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    private void changeClientKeyIfOneClick() {
        if (oneClickSelection.isChecked()) {
            MidtransSDK.getInstance().setClientKey(CLIENT_KEY_FOR_ONE_CLICK_MID);
            MidtransSDK.getInstance().getTransactionRequest().setCustomField1("one_click");
        }
    }

    private void initResetSettings() {
        resetSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalSelection.setChecked(true);
                secureDisabledSelection.setChecked(true);
                noInstallmentSelection.setChecked(true);
                bankNoneSelection.setChecked(true);
                expiryNoneSelection.setChecked(true);
                saveCardDisabledSelection.setChecked(true);
                paymentChannelsAllSelection.setChecked(true);
                customBcaVaDisabledSelection.setChecked(true);
                customPermataVaDisabledSelection.setChecked(true);
                customPermataRecipientDisabledSelection.setChecked(true);
                subCompanyBcaVaDisabledSelection.setChecked(true);
                preAuthDisabledSelection.setChecked(true);
                bniPointOnlyDisabledSelection.setChecked(true);
                mandiriPointOnlyDisabledSelection.setChecked(true);
                autoReadSmsDisabledSelection.setChecked(true);
                currencyIdrSelection.setChecked(true);
                Toast.makeText(DemoConfigActivity.this, getString(R.string.reset_setting_notification), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveColorThemeSelection() {
        if (redThemeSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, COLOR_THEME, DemoThemeConstants.RED_THEME);
        } else if (greenThemeSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, COLOR_THEME, DemoThemeConstants.GREEN_THEME);
        } else if (orangeThemeSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, COLOR_THEME, DemoThemeConstants.ORANGE_THEME);
        } else if (blackThemeSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, COLOR_THEME, DemoThemeConstants.BLACK_THEME);
        } else {
            DemoPreferenceHelper.setStringPreference(this, COLOR_THEME, DemoThemeConstants.BLUE_THEME);
        }
    }

    private void saveCardClickSelection() {
        if (twoClicksSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, CARD_CLICK_TYPE, Constants.CARD_TWO_CLICKS);
        } else if (oneClickSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, CARD_CLICK_TYPE, Constants.CARD_ONE_CLICK);
        } else {
            DemoPreferenceHelper.setStringPreference(this, CARD_CLICK_TYPE, Constants.CARD_NORMAL);
        }
    }

    private void saveSecureTypeSelection() {
        if (secureEnabledSelection.isChecked()) {
            DemoPreferenceHelper.setIntegerPreference(this, SECURE_TYPE, AUTH_TYPE_3DS);
        } else if (secureRbaWith3dsSelection.isChecked()) {
            DemoPreferenceHelper.setIntegerPreference(this, SECURE_TYPE, AUTH_TYPE_RBA);
        } else if (secureRbaNon3dsSelection.isChecked()) {
            DemoPreferenceHelper.setIntegerPreference(this, SECURE_TYPE, AUTH_TYPE_RBA_NON_3DS);
        } else {
            DemoPreferenceHelper.setIntegerPreference(this, SECURE_TYPE, AUTH_TYPE_NONE);
        }
    }

    private void saveIssuingBankSelection() {
        if (bankBniSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, BANK_TYPE, Constants.BANK_BNI);
        } else if (bankMandiriSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, BANK_TYPE, Constants.BANK_MANDIRI);
        } else if (bankBcaSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, BANK_TYPE, Constants.BANK_BCA);
        } else if (bankMaybankSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, BANK_TYPE, Constants.BANK_MAYBANK);
        } else if (bankBriSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, BANK_TYPE, Constants.BANK_BRI);
        } else if (bankCimbSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, BANK_TYPE, Constants.BANK_CIMB);
        } else if (bankMegaSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, BANK_TYPE, Constants.BANK_MEGA);
        } else {
            DemoPreferenceHelper.setStringPreference(this, BANK_TYPE, Constants.BANK_NONE);
        }
    }

    private void saveCustomExpirySelection() {
        if (expiryOneMinuteSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, EXPIRY_TPE, Constants.EXPIRY_MINUTE);
        } else if (expiryOneHourSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, EXPIRY_TPE, Constants.EXPIRY_HOUR);
        } else {
            DemoPreferenceHelper.setStringPreference(this, EXPIRY_TPE, Constants.EXPIRY_NONE);
        }
    }

    private void saveDefaultSaveCardSelection() {
        if (saveCardEnabledSelection.isChecked()) {
            DemoPreferenceHelper.setBooleanPreference(this, SAVE_CARD_TYPE, true);
        } else {
            DemoPreferenceHelper.setBooleanPreference(this, SAVE_CARD_TYPE, false);
        }
    }

    private void saveEnabledPayments() {
        if (paymentChannelsAllSelection.isChecked()) {
            LocalDataHandler.saveString(PAYMENT_CHANNELS_TYPE, "{}");
        } else {
            LocalDataHandler.saveObject(PAYMENT_CHANNELS_TYPE, new SelectPaymentMethodListViewModel(AppUtils.getPaymentList(this, mapEnabledPayments())));
        }
    }

    private void saveCurrencySelection() {
        if (currencySgdSelection.isChecked()) {
            DemoPreferenceHelper.setIntegerPreference(this, CURRENCY_TYPE, CURRENCY_TYPE_SGD);
        } else {
            DemoPreferenceHelper.setIntegerPreference(this, CURRENCY_TYPE, CURRENCY_TYPE_IDR);
        }
    }

    private void saveBcaVaNumber() {
        String vaNumber = "";

        try {
            vaNumber = customBcaVaEnabledSelection.getText().toString().split(" - ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {

        }

        if (customBcaVaEnabledSelection.isChecked() && !TextUtils.isEmpty(vaNumber)) {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_BCA_VA_NUMBER, vaNumber);
        } else {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_BCA_VA_NUMBER, "");
        }
    }

    private void savePermataVaNumber() {
        String vaNumber = "";

        try {
            vaNumber = customPermataVaEnabledSelection.getText().toString().split(" - ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        if (customPermataVaEnabledSelection.isChecked() && !TextUtils.isEmpty(vaNumber)) {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_PERMATA_VA_NUMBER, vaNumber);
        } else {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_PERMATA_VA_NUMBER, "");
        }
    }

    private void savePermataRecipient() {
        String recipient = "";

        try {
            recipient = customPermataRecipientEnabledSelection.getText().toString().split(" - ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        if (customPermataRecipientEnabledSelection.isChecked() && !TextUtils.isEmpty(recipient)) {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_PERMATA_RECIPIENT, recipient);
        } else {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_PERMATA_RECIPIENT, "");
        }
    }

    private void saveBniVaNumber() {

        String vaNumber = "";

        try {
            vaNumber = customBniVaEnabledSelection.getText().toString().split(" - ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        if (customBniVaEnabledSelection.isChecked() && !TextUtils.isEmpty(vaNumber)) {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_BNI_VA_NUMBER, vaNumber);
        } else {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_BNI_VA_NUMBER, "");
        }
    }

    private void saveSubCompanyBcaVaNumber() {
        String subCompany = "";

        try {
            subCompany = subCompanyBcaVaEnabledSelection.getText().toString().split(" - ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        if (subCompanyBcaVaEnabledSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, SUBCOMPANY_BCA_VA_NUMBER, subCompany);
        } else {
            DemoPreferenceHelper.setStringPreference(this, SUBCOMPANY_BCA_VA_NUMBER, "");
        }
    }

    private void savePreAuthSelection() {
        if (preAuthEnabledSelection.isChecked()) {
            DemoPreferenceHelper.setBooleanPreference(this, PRE_AUTH_TYPE, true);
        } else {
            DemoPreferenceHelper.setBooleanPreference(this, PRE_AUTH_TYPE, false);
        }
    }

    private void saveInstallmentSelection() {
        if (installmentBniSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, INSTALLMENT_TYPE, Constants.INSTALLMENT_BNI);
        } else if (installmentMandiriSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, INSTALLMENT_TYPE, Constants.INSTALLMENT_MANDIRI);
        } else if (installmentBcaSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, INSTALLMENT_TYPE, Constants.INSTALLMENT_BCA);
        } else if (installmentBriSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, INSTALLMENT_TYPE, Constants.INSTALLMENT_BRI);
        } else if (installmentCimbSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, INSTALLMENT_TYPE, Constants.INSTALLMENT_CIMB);
        } else if (installmentMaybankSelection.isChecked()) {
            DemoPreferenceHelper.setStringPreference(this, INSTALLMENT_TYPE, Constants.INSTALLMENT_MAYBANK);
        } else {
            DemoPreferenceHelper.setStringPreference(this, INSTALLMENT_TYPE, Constants.NO_INSTALLMENT);
        }
        DemoPreferenceHelper.setBooleanPreference(this, INSTALLMENT_REQUIRED, installmentRequired);
    }

    private void saveBniPointSelection() {
        if (bniPointOnlyEnabledSelection.isChecked()) {
            DemoPreferenceHelper.setBooleanPreference(this, BNI_POINT_TYPE, true);
        } else {
            DemoPreferenceHelper.setBooleanPreference(this, BNI_POINT_TYPE, false);
        }
    }

    private void saveMandiriPointSelection() {
        if (mandiriPointOnlyEnabledSelection.isChecked()) {
            DemoPreferenceHelper.setBooleanPreference(this, MANDIRI_POINT_TYPE, true);
        } else {
            DemoPreferenceHelper.setBooleanPreference(this, MANDIRI_POINT_TYPE, false);
        }
    }

    private void saveAutoReadSmsSelection() {
        if (autoReadSmsEnabledSelection.isChecked()) {
            DemoPreferenceHelper.setBooleanPreference(this, AUTO_READ_SMS_TYPE, true);
        } else {
            DemoPreferenceHelper.setBooleanPreference(this, AUTO_READ_SMS_TYPE, false);
        }
    }

    private void unselectAllTitles() {
        cardClickTitle.setSelected(false);
        cardClickTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(cardClickTitle);

        secureTitle.setSelected(false);
        secureTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(secureTitle);

        bankTitle.setSelected(false);
        bankTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(bankTitle);

        expiryTitle.setSelected(false);
        expiryTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(expiryTitle);

        saveCardTitle.setSelected(false);
        saveCardTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(saveCardTitle);

        preAuthTitle.setSelected(false);
        preAuthTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(preAuthTitle);

        colorThemeTitle.setSelected(false);
        colorThemeTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(colorThemeTitle);

        customBcaVaTitle.setSelected(false);
        customBcaVaTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(customBcaVaTitle);

        customPermataVaTitle.setSelected(false);
        customPermataVaTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(customPermataVaTitle);

        customPermataRecipientTitle.setSelected(false);
        customPermataRecipientTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(customPermataRecipientTitle);

        customBniVaTitle.setSelected(false);
        customBniVaTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(customBniVaTitle);

        subCompanyBcaVaTitle.setSelected(false);
        subCompanyBcaVaTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(subCompanyBcaVaTitle);

        installmentTitle.setSelected(false);
        installmentTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(installmentTitle);

        bniPointTitle.setSelected(false);
        bniPointTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(bniPointTitle);

        mandiriPointTitle.setSelected(false);
        mandiriPointTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(mandiriPointTitle);

        paymentChannelsTitle.setSelected(false);
        paymentChannelsTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(paymentChannelsTitle);

        autoReadSmsTitle.setSelected(false);
        autoReadSmsTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(autoReadSmsTitle);

        currencyTitle.setSelected(false);
        currencyTitle.setTextColor((ContextCompat.getColor(this, R.color.black)));
        clearTextViewDrawableLeftColorFilter(currencyTitle);
    }

    private void hideAllSelections() {
        cardClickContainer.setVisibility(View.GONE);
        secureContainer.setVisibility(View.GONE);
        bankContainer.setVisibility(View.GONE);
        expiryContainer.setVisibility(View.GONE);
        saveCardContainer.setVisibility(View.GONE);
        preAuthContainer.setVisibility(View.GONE);
        colorThemeContainer.setVisibility(View.GONE);
        customBcaVaContainer.setVisibility(View.GONE);
        customPermataVaContainer.setVisibility(View.GONE);
        customPermataRecipientContainer.setVisibility(View.GONE);
        customBniVaContainer.setVisibility(View.GONE);
        subCompanyBcaVaContainer.setVisibility(View.GONE);
        installmentContainer.setVisibility(View.GONE);
        changeInstallmentContainer.setVisibility(View.GONE);
        bniPointContainer.setVisibility(View.GONE);
        mandiriPointContainer.setVisibility(View.GONE);
        paymentChannelsContainer.setVisibility(View.GONE);
        autoReadSmsContainer.setVisibility(View.GONE);
        currencyContainer.setVisibility(View.GONE);
    }

    private void setTextViewDrawableLeftColorFilter(TextView textView) {
        Drawable drawable = textView.getCompoundDrawables()[0];
        if (drawable != null) {
            int color;
            switch (selectedColor) {
                case DemoThemeConstants.BLUE_THEME:
                    color = Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX);
                    break;
                case DemoThemeConstants.RED_THEME:
                    color = Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX);
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    color = Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX);
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    color = Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX);
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    color = Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX);
                    break;
                default:
                    color = Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX);
                    break;
            }
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }
    }

    private void setTextViewSelectedColor(TextView textView) {
        int color;
        switch (selectedColor) {
            case DemoThemeConstants.BLUE_THEME:
                color = Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX);
                break;
            case DemoThemeConstants.RED_THEME:
                color = Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX);
                break;
            case DemoThemeConstants.GREEN_THEME:
                color = Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX);
                break;
            case DemoThemeConstants.ORANGE_THEME:
                color = Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX);
                break;
            case DemoThemeConstants.BLACK_THEME:
                color = Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX);
                break;
            default:
                color = Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX);
                break;
        }
        textView.setTextColor(color);
    }

    private void clearTextViewDrawableLeftColorFilter(TextView textView) {
        Drawable drawable = textView.getCompoundDrawables()[0];
        if (drawable != null) {
            drawable.setColorFilter(null);
        }
    }

    private void updateButtonColor() {
        int color;
        switch (selectedColor) {
            case DemoThemeConstants.BLUE_THEME:
                color = Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX);
                break;
            case DemoThemeConstants.RED_THEME:
                color = Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX);
                break;
            case DemoThemeConstants.GREEN_THEME:
                color = Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX);
                break;
            case DemoThemeConstants.ORANGE_THEME:
                color = Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX);
                break;
            case DemoThemeConstants.BLACK_THEME:
                color = Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX);
                break;
            default:
                color = Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX);
                break;
        }
        nextButton.setBackgroundColor(color);
        nextButton.setText(getString(R.string.btn_launch_demo));
        nextButton.setTextBold();
        resetSetting.setTextColor(color);
    }

    private void updateColorThemeTitle() {
        String selected;
        switch (selectedColor) {
            case DemoThemeConstants.BLUE_THEME:
                selected = getString(R.string.default_color_theme);
                break;
            case DemoThemeConstants.RED_THEME:
                selected = getString(R.string.color_theme_red);
                break;
            case DemoThemeConstants.GREEN_THEME:
                selected = getString(R.string.color_theme_green);
                break;
            case DemoThemeConstants.ORANGE_THEME:
                selected = getString(R.string.color_theme_orange);
                break;
            case DemoThemeConstants.BLACK_THEME:
                selected = getString(R.string.color_theme_black);
                break;
            default:
                selected = getString(R.string.default_color_theme);
                break;
        }

        colorThemeTitle.setText(selected);
    }

    private void updateRadioButtonColor() {
        int color;
        switch (selectedColor) {
            case DemoThemeConstants.BLUE_THEME:
                color = Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX);
                break;
            case DemoThemeConstants.RED_THEME:
                color = Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX);
                break;
            case DemoThemeConstants.GREEN_THEME:
                color = Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX);
                break;
            case DemoThemeConstants.ORANGE_THEME:
                color = Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX);
                break;
            case DemoThemeConstants.BLACK_THEME:
                color = Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX);
                break;
            default:
                color = Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX);
                break;
        }
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked},
        };

        int[] colors = new int[]{
                Color.WHITE,
                color,
        };
        ColorStateList colorStateList = new ColorStateList(
                states,
                colors
        );

        normalSelection.setSupportButtonTintList(colorStateList);
        twoClicksSelection.setSupportButtonTintList(colorStateList);
        oneClickSelection.setSupportButtonTintList(colorStateList);

        secureDisabledSelection.setSupportButtonTintList(colorStateList);
        secureEnabledSelection.setSupportButtonTintList(colorStateList);

        bankNoneSelection.setSupportButtonTintList(colorStateList);
        bankBniSelection.setSupportButtonTintList(colorStateList);
        bankMandiriSelection.setSupportButtonTintList(colorStateList);
        bankBcaSelection.setSupportButtonTintList(colorStateList);
        bankMaybankSelection.setSupportButtonTintList(colorStateList);
        bankBriSelection.setSupportButtonTintList(colorStateList);

        expiryNoneSelection.setSupportButtonTintList(colorStateList);
        expiryOneMinuteSelection.setSupportButtonTintList(colorStateList);
        expiryOneHourSelection.setSupportButtonTintList(colorStateList);

        saveCardDisabledSelection.setSupportButtonTintList(colorStateList);
        saveCardEnabledSelection.setSupportButtonTintList(colorStateList);

        customBcaVaEnabledSelection.setSupportButtonTintList(colorStateList);
        customBcaVaDisabledSelection.setSupportButtonTintList(colorStateList);

        customPermataVaEnabledSelection.setSupportButtonTintList(colorStateList);
        customPermataVaDisabledSelection.setSupportButtonTintList(colorStateList);

        customPermataRecipientEnabledSelection.setSupportButtonTintList(colorStateList);
        customPermataRecipientDisabledSelection.setSupportButtonTintList(colorStateList);

        customBniVaEnabledSelection.setSupportButtonTintList(colorStateList);
        customBniVaDisabledSelection.setSupportButtonTintList(colorStateList);

        subCompanyBcaVaEnabledSelection.setSupportButtonTintList(colorStateList);
        subCompanyBcaVaDisabledSelection.setSupportButtonTintList(colorStateList);

        preAuthDisabledSelection.setSupportButtonTintList(colorStateList);
        preAuthEnabledSelection.setSupportButtonTintList(colorStateList);

        defaultThemeSelection.setSupportButtonTintList(colorStateList);
        redThemeSelection.setSupportButtonTintList(colorStateList);
        greenThemeSelection.setSupportButtonTintList(colorStateList);
        orangeThemeSelection.setSupportButtonTintList(colorStateList);
        blackThemeSelection.setSupportButtonTintList(colorStateList);

        installmentBniSelection.setSupportButtonTintList(colorStateList);
        installmentMandiriSelection.setSupportButtonTintList(colorStateList);
        installmentBcaSelection.setSupportButtonTintList(colorStateList);
        installmentBriSelection.setSupportButtonTintList(colorStateList);
        installmentCimbSelection.setSupportButtonTintList(colorStateList);
        installmentMaybankSelection.setSupportButtonTintList(colorStateList);
        noInstallmentSelection.setSupportButtonTintList(colorStateList);

        bniPointOnlyEnabledSelection.setSupportButtonTintList(colorStateList);
        bniPointOnlyDisabledSelection.setSupportButtonTintList(colorStateList);

        mandiriPointOnlyEnabledSelection.setSupportButtonTintList(colorStateList);
        mandiriPointOnlyDisabledSelection.setSupportButtonTintList(colorStateList);

        paymentChannelsAllSelection.setSupportButtonTintList(colorStateList);
        paymentChannelsSelectedSelection.setSupportButtonTintList(colorStateList);

        autoReadSmsDisabledSelection.setSupportButtonTintList(colorStateList);
        autoReadSmsEnabledSelection.setSupportButtonTintList(colorStateList);
    }

    private void initMidtransSDK() {
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(BuildConfig.BASE_URL)
                .setClientKey(BuildConfig.CLIENT_KEY)
                .setExternalScanner(new ScanCard())
                .enableLog(true)
                .setDefaultText("fonts/SourceSansPro-Regular.ttf")
                .setBoldText("fonts/SourceSansPro-Bold.ttf")
                .setSemiBoldText("fonts/SourceSansPro-Semibold.ttf")
                .buildSDK();
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

    private TransactionRequest initializePurchaseRequest() {
        // Init theme
        if (defaultThemeSelection.isChecked()) {
            MidtransSDK.getInstance().setColorTheme(new CustomColorTheme(DemoThemeConstants.BLUE_PRIMARY_HEX, DemoThemeConstants.BLUE_PRIMARY_DARK_HEX, DemoThemeConstants.BLUE_SECONDARY_HEX));
        } else if (redThemeSelection.isChecked()) {
            MidtransSDK.getInstance().setColorTheme(new CustomColorTheme(DemoThemeConstants.RED_PRIMARY_HEX, DemoThemeConstants.RED_PRIMARY_DARK_HEX, DemoThemeConstants.RED_SECONDARY_HEX));
        } else if (greenThemeSelection.isChecked()) {
            MidtransSDK.getInstance().setColorTheme(new CustomColorTheme(DemoThemeConstants.GREEN_PRIMARY_HEX, DemoThemeConstants.GREEN_PRIMARY_DARK_HEX, DemoThemeConstants.GREEN_SECONDARY_HEX));
        } else if (orangeThemeSelection.isChecked()) {
            MidtransSDK.getInstance().setColorTheme(new CustomColorTheme(DemoThemeConstants.ORANGE_PRIMARY_HEX, DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX, DemoThemeConstants.ORANGE_SECONDARY_HEX));
        } else if (blackThemeSelection.isChecked()) {
            MidtransSDK.getInstance().setColorTheme(new CustomColorTheme(DemoThemeConstants.BLACK_PRIMARY_HEX, DemoThemeConstants.BLACK_PRIMARY_DARK_HEX, DemoThemeConstants.BLACK_SECONDARY_HEX));
        }

        // Create new Transaction Request
        TransactionRequest transactionRequestNew = new TransactionRequest(System.currentTimeMillis() + "", 10000);
        ItemDetails itemDetails = new ItemDetails("1", 10000, 1, getString(R.string.product_name_sample));

        if (currencySgdSelection.isChecked()) {
            transactionRequestNew = new TransactionRequest(System.currentTimeMillis() + "", 5000.5, Currency.SGD);
            itemDetails = new ItemDetails("1", 5000.5, 1, getString(R.string.product_name_sample));
        }

        // Define item details
        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);
        // Set Bill info
        BillInfoModel billInfoModel = new BillInfoModel("demo_label", "demo_value");
        transactionRequestNew.setBillInfoModel(billInfoModel);

        // Create credit card options for payment
        // noted : channel migs is needed if bank type is BCA, BRI or MyBank
        CreditCard creditCard = new CreditCard();

        // set installment option on creditcard
        setInstallmentOption(creditCard);

        // set creditcard payment by BNI only
        setBniPointOption(creditCard);

        // set creditcard payment by Mandiri only
        setMandiriPointOption(creditCard);

        // set bank and channel on creditcard
        if (bankMandiriSelection.isChecked()) {
            // Set bank to Mandiri
            creditCard.setBank(BankType.MANDIRI);
        } else if (bankBniSelection.isChecked()) {
            // Set bank to BNI
            creditCard.setBank(BankType.BNI);
        } else if (bankBcaSelection.isChecked()) {
            //Set bank to BCA
            creditCard.setBank(BankType.BCA);
            // credit card payment using bank BCA need migs channel
            creditCard.setChannel(CreditCard.MIGS);
        } else if (bankMaybankSelection.isChecked()) {
            //Set bank to Maybank
            creditCard.setBank(BankType.MAYBANK);
            // credit card payment using bank Maybank need migs channel
            creditCard.setChannel(CreditCard.MIGS);
        } else if (bankBriSelection.isChecked()) {
            // Set bank to BRI
            creditCard.setBank(BankType.BRI);
            // credit card payment using bank BRI need migs channel
            creditCard.setChannel(CreditCard.MIGS);
        } else if (bankCimbSelection.isChecked()) {
            // Set bank to CIMB
            creditCard.setBank(BankType.CIMB);
        } else if (bankMegaSelection.isChecked()) {
            // Set bank to CIMB
            creditCard.setBank(BankType.MEGA);
        }

        if (preAuthEnabledSelection.isChecked()) {
            // Set Pre Auth mode
            creditCard.setType(CardTokenRequest.TYPE_AUTHORIZE);
        }

        String cardClickType;

        if (normalSelection.isChecked()) {
            cardClickType = getString(R.string.card_click_type_none);
            if (secureEnabledSelection.isChecked()) {
                creditCard.setAuthentication(Authentication.AUTH_3DS);
            } else if (secureRbaWith3dsSelection.isChecked()
                    || secureRbaNon3dsSelection.isChecked()) {
                creditCard.setAuthentication(Authentication.AUTH_RBA);
            } else {
                creditCard.setAuthentication(Authentication.AUTH_NONE);
            }

            transactionRequestNew.setCreditCard(creditCard);
        } else if (twoClicksSelection.isChecked()) {
            cardClickType = getString(R.string.card_click_type_two_click);
            creditCard.setSaveCard(true);
            transactionRequestNew.setCreditCard(creditCard);

        } else {
            cardClickType = getString(R.string.card_click_type_one_click);
            creditCard.setSaveCard(true);
            creditCard.setAuthentication(Authentication.AUTH_3DS);
            transactionRequestNew.setCreditCard(creditCard);
        }

        UIKitCustomSetting uiKitCustomSetting = MidtransSDK.getInstance().getUIKitCustomSetting();
        uiKitCustomSetting.setShowPaymentStatus(true);
        uiKitCustomSetting.setSkipCustomerDetailsPages(true);

        uiKitCustomSetting.setEnableAutoReadSms(true);
        if (saveCardEnabledSelection.isChecked()) {
            uiKitCustomSetting.setSaveCardChecked(true);
        } else {
            uiKitCustomSetting.setSaveCardChecked(false);
        }

        if (autoReadSmsEnabledSelection.isChecked()) {
            uiKitCustomSetting.setEnableAutoReadSms(true);
        } else {
            uiKitCustomSetting.setEnableAutoReadSms(false);
        }

        uiKitCustomSetting.setShowEmailInCcForm(true);

        MidtransSDK.getInstance().setUIKitCustomSetting(uiKitCustomSetting);

        if (secureEnabledSelection.isChecked()) {
            creditCard.setAuthentication(Authentication.AUTH_3DS);

            // this method being deprecated, please use creditCard.setAuthentication() method
            transactionRequestNew.setCardPaymentInfo(cardClickType, true);
        } else {
            creditCard.setAuthentication(Authentication.AUTH_NONE);

            // this method being deprecated, please use creditCard.setAuthentication() method
            transactionRequestNew.setCardPaymentInfo(cardClickType, false);
        }

        if (paymentChannelsSelectedSelection.isChecked()) {
            transactionRequestNew.setEnabledPayments(mapEnabledPayments());
        } else {
            transactionRequestNew.setEnabledPayments(initActivePaymentMethods());
        }

        // set expiry time
        ExpiryModel expiryModel = new ExpiryModel();
        expiryModel.setStartTime(Utils.getFormattedTime(System.currentTimeMillis()));
        expiryModel.setDuration(1);

        if (expiryOneMinuteSelection.isChecked()) {
            expiryModel.setUnit(ExpiryModel.UNIT_MINUTE);
            transactionRequestNew.setExpiry(expiryModel);
        } else if (expiryOneHourSelection.isChecked()) {
            expiryModel.setUnit(ExpiryModel.UNIT_HOUR);
            transactionRequestNew.setExpiry(expiryModel);
        }

        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        if (userDetail == null) {
            userDetail = new UserDetail();
            userDetail.setUserFullName(getString(R.string.order_review_customer_details_name));
            userDetail.setEmail(getString(R.string.order_review_customer_details_email));
            userDetail.setPhoneNumber(getString(R.string.order_review_customer_details_phone));
            if (oneClickSelection.isChecked()) {
                userDetail.setUserId(getString(R.string.sample_user_id));
            } else if (twoClicksSelection.isChecked()) {
                userDetail.setUserId(getString(R.string.sample_user_id2));
            }
            ArrayList<UserAddress> userAddresses = new ArrayList<>();
            UserAddress userAddress = new UserAddress();
            userAddress.setAddress(getString(R.string.order_review_delivery_address_sample));
            userAddress.setCity(getString(R.string.order_review_delivery_address_city_sample));
            userAddress.setAddressType(com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BOTH);
            userAddress.setZipcode("10220");
            userAddress.setCountry("IDN");
            userAddresses.add(userAddress);
            userDetail.setUserAddresses(userAddresses);
        } else {

            if (oneClickSelection.isChecked()) {
                userDetail.setUserId(getString(R.string.sample_user_id));
            } else if (twoClicksSelection.isChecked()) {
                userDetail.setUserId(getString(R.string.sample_user_id2));
            }
        }

        // if rba activated
        if (secureRbaWith3dsSelection.isChecked()) {
            userDetail.setEmail("secure_email_rba@example.com");
            creditCard.setAuthentication(Authentication.AUTH_RBA);
        } else if (secureRbaNon3dsSelection.isChecked()) {
            userDetail.setEmail("not_secure_email_rba@example.com");
            creditCard.setAuthentication(Authentication.AUTH_RBA);
        }
        LocalDataHandler.saveObject(getString(R.string.user_details), userDetail);
        if (customPermataVaEnabledSelection.isChecked()) {
            String vaNumber = "";
            try {
                vaNumber = customPermataVaEnabledSelection.getText().toString().split(" - ")[1];

            } catch (RuntimeException e) {

            }
            transactionRequestNew.setPermataVa(
                    new PermataBankTransferRequestModel(vaNumber)
            );
        }

        if (customPermataRecipientEnabledSelection.isChecked()) {
            String recipient = "";
            try {
                recipient = customPermataRecipientEnabledSelection.getText().toString().split(" - ")[1];
            } catch (RuntimeException e) {

            }
            if (transactionRequestNew.getPermataVa() != null) {
                BankTransferRequestModel permataVa = transactionRequestNew.getPermataVa();
                if (permataVa instanceof PermataBankTransferRequestModel) {
                    ((PermataBankTransferRequestModel) permataVa).setRecipientName(recipient);
                } else {
                    PermataBankTransferRequestModel newPermataVa = new PermataBankTransferRequestModel(permataVa.getVaNumber());
                    newPermataVa.setRecipientName(recipient);
                    transactionRequestNew.setPermataVa(newPermataVa);
                }
            } else {
                PermataBankTransferRequestModel newPermataVa = new PermataBankTransferRequestModel();
                newPermataVa.setRecipientName(recipient);
                transactionRequestNew.setPermataVa(newPermataVa);
            }
        }

        BcaBankTransferRequestModel requestModel = null;
        if (customBcaVaEnabledSelection.isChecked()) {
            // set free text on BCA VA Payment
            FreeText freeText = createSampleBcaFreeText();
            String vaNumber = "";
            try {
                vaNumber = customBcaVaEnabledSelection.getText().toString().split(" - ")[1];

            } catch (RuntimeException e) {

            }
            requestModel = new BcaBankTransferRequestModel(vaNumber, freeText);
            if (subCompanyBcaVaDisabledSelection.isChecked()) {
                transactionRequestNew.setBcaVa(requestModel);
            }
        }

        //add custom VA and subcompany for BCA
        if (subCompanyBcaVaEnabledSelection.isChecked()) {
            if (requestModel == null) {
                requestModel = new BcaBankTransferRequestModel();
            }

            String subCompanyCode = "";
            try {
                subCompanyCode = subCompanyBcaVaEnabledSelection.getText().toString().split(" - ")[1];
            } catch (RuntimeException e) {

            }

            requestModel.setSubCompanyCode(subCompanyCode);
            transactionRequestNew.setBcaVa(requestModel);
        }

        if (customBniVaEnabledSelection.isChecked()) {
            String vaNumber = "";
            try {
                vaNumber = customBniVaEnabledSelection.getText().toString().split(" - ")[1];
            } catch (RuntimeException e) {

            }
            transactionRequestNew.setBniVa(
                    new BankTransferRequestModel(vaNumber)
            );
        }

        transactionRequestNew.setGopay(new Gopay("demo://midtrans"));

        return transactionRequestNew;
    }

    private FreeText createSampleBcaFreeText() {
        List<FreeTextLanguage> inquiryLang = new ArrayList<>();
        inquiryLang.add(new FreeTextLanguage("Text ID inquiry 0", "Text EN inquiry 0"));
        inquiryLang.add(new FreeTextLanguage("Text ID inquiry 1", "Text EN inquiry 1"));

        List<FreeTextLanguage> paymentLang = new ArrayList<>();
        paymentLang.add(new FreeTextLanguage("Text ID payment 0", "Text EN payment 0"));
        paymentLang.add(new FreeTextLanguage("Text ID payment 1", "Text EN payment 1"));

        FreeText freeText = new FreeText(inquiryLang, paymentLang);
        return freeText;
    }

    private int getSelectedColorPrimaryDark() {
        switch (selectedColor) {
            case DemoThemeConstants.BLUE_THEME:
                return Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX);
            case DemoThemeConstants.RED_THEME:
                return Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX);
            case DemoThemeConstants.GREEN_THEME:
                return Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX);
            case DemoThemeConstants.ORANGE_THEME:
                return Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX);
            case DemoThemeConstants.BLACK_THEME:
                return Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX);
            default:
                return Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX);
        }
    }

    private void setBniPointOption(CreditCard creditCard) {
        if (bniPointOnlyEnabledSelection.isChecked()) {
            ArrayList<String> whiteListBins = new ArrayList<>();
            whiteListBins.add("410505");
            whiteListBins.add("526422");
            creditCard.setWhiteListBins(whiteListBins);
        }
    }

    private void setMandiriPointOption(CreditCard creditCard) {
        if (mandiriPointOnlyEnabledSelection.isChecked()) {
            ArrayList<String> whiteListBins = new ArrayList<>();
            //add mandiri bin number
            whiteListBins.add("400376");
            whiteListBins.add("400378");
            whiteListBins.add("400385");
            whiteListBins.add("400479");
            whiteListBins.add("400481");
            whiteListBins.add("405651");
            whiteListBins.add("409766");
            whiteListBins.add("413718");
            whiteListBins.add("413719");
            whiteListBins.add("414931");
            whiteListBins.add("415016");
            whiteListBins.add("415017");
            whiteListBins.add("415018");
            whiteListBins.add("415030");
            whiteListBins.add("415031");
            whiteListBins.add("415032");
            whiteListBins.add("416230");
            whiteListBins.add("416231");
            whiteListBins.add("416232");
            whiteListBins.add("421057");
            whiteListBins.add("421195");
            whiteListBins.add("421197");
            whiteListBins.add("421313");
            whiteListBins.add("425945");
            whiteListBins.add("434075");
            whiteListBins.add("437527");
            whiteListBins.add("437528");
            whiteListBins.add("437529");
            whiteListBins.add("445076");
            whiteListBins.add("450183");
            whiteListBins.add("450184");
            whiteListBins.add("450185");
            whiteListBins.add("461699");
            whiteListBins.add("461700");
            whiteListBins.add("463199");
            whiteListBins.add("468748");
            whiteListBins.add("468749");
            whiteListBins.add("468984");
            whiteListBins.add("483795");
            whiteListBins.add("483796");
            whiteListBins.add("486682");
            whiteListBins.add("489594");
            whiteListBins.add("489764");
            whiteListBins.add("490283");
            whiteListBins.add("490284");
            whiteListBins.add("512676");
            whiteListBins.add("512724");
            whiteListBins.add("523536");
            whiteListBins.add("524325");
            whiteListBins.add("537793");
            whiteListBins.add("550615");
            whiteListBins.add("557338");
            whiteListBins.add("467840");
            whiteListBins.add("409790");
            creditCard.setWhiteListBins(whiteListBins);
        }
    }

    private void setInstallmentOption(CreditCard creditCard) {
        Installment installment = new Installment();

        Map<String, ArrayList<Integer>> bankTerms = new HashMap<>();

        if (installmentBriSelection.isChecked()) {
            setInstallmentBankTerm(bankTerms, Constants.INSTALLMENT_BANK_BRI);
        } else if (installmentBniSelection.isChecked()) {
            setInstallmentBankTerm(bankTerms, Constants.INSTALLMENT_BANK_BNI);
        } else if (installmentMandiriSelection.isChecked()) {
            setInstallmentBankTerm(bankTerms, Constants.INSTALLMENT_BANK_MANDIRI);
        } else if (installmentBcaSelection.isChecked()) {
            setInstallmentBankTerm(bankTerms, Constants.INSTALLMENT_BANK_BCA);
        } else if (installmentCimbSelection.isChecked()) {
            setInstallmentBankTerm(bankTerms, Constants.INSTALLMENT_BANK_CIMB);
        } else if (installmentMaybankSelection.isChecked()) {
            setInstallmentBankTerm(bankTerms, Constants.INSTALLMENT_BANK_MAYBANK);
        } else {
            installment = null;
        }
        if (installment != null) {
            installment.setTerms(bankTerms);

            if (installmentRequired) {
                installment.setRequired(true);
            } else {
                installment.setRequired(false);
            }
        }


        creditCard.setInstallment(installment);
    }

    private void setInstallmentBankTerm(Map<String, ArrayList<Integer>> bankTerms, String bank) {
        //set term installment
        ArrayList<Integer> term = new ArrayList<>();
        term.add(3);
        term.add(6);
        term.add(12);
        //set bank and term
        bankTerms.put(bank, term);
    }

    private List<String> mapEnabledPayments() {
        List<String> mappedPayments = new ArrayList<>();

        if (enabledPayments != null) {
            for (SelectPaymentMethodViewModel model : enabledPayments) {

                if (model.getMethodType().equalsIgnoreCase(getString(R.string.payment_bank_transfer))) {
                    mappedPayments.add(getString(R.string.payment_mandiri_bill_payment));
                }

                if (AppUtils.isActivePaymentChannel(this, model)) {
                    mappedPayments.add(model.getMethodType());
                }
            }
        }

        return mappedPayments;
    }

    private List<String> initActivePaymentMethods() {
        List<String> mappedPayments = new ArrayList<>();

        List<EnabledPayment> defaultPayment = AppUtils.getDefaultPaymentList(this);
        enabledPayments = AppUtils.mapPaymentMethods(this, defaultPayment);

        if (enabledPayments != null) {
            for (SelectPaymentMethodViewModel model : enabledPayments) {

                if (model.getMethodType().equalsIgnoreCase(getString(R.string.payment_bank_transfer))) {
                    mappedPayments.add(getString(R.string.payment_mandiri_bill_payment));
                }

                if (AppUtils.isActivePaymentChannel(this, model)) {
                    mappedPayments.add(model.getMethodType());
                }
            }
        }

        return mappedPayments;
    }

}
