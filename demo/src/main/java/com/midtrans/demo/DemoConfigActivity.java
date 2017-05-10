package com.midtrans.demo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.demo.widgets.DemoRadioButton;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.ExpiryModel;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.BankTransferRequestModel;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.corekit.models.snap.Installment;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.scancard.ScanCard;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String PROMO_TYPE = "config.promo";
    private static final String PRE_AUTH_TYPE = "config.preauth";
    private static final String CUSTOM_BCA_VA_NUMBER = "config.va.bca";
    private static final String CUSTOM_PERMATA_VA_NUMBER = "config.va.permata";
    private static final String INSTALLMENT_TYPE = "config.installment";
    private static final String INSTALLMENT_REQUIRED = "config.installment.required";
    private static final String BNI_POINT_TYPE = "config.bni.point";
    private static final String PAYMENT_CHANNELS_TYPE = "config.channels";
    private static final String AUTO_READ_SMS_TYPE = "config.auto.otp";

    private static final String LABEL_INSTALLMENT_REQUIRED = " - Required";
    private static final String LABEL_INSTALLMENT_OPTIONAL = " - optional";

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
    private TextView promoTitle;
    private TextView preAuthTitle;
    private TextView colorThemeTitle;
    private TextView customPermataVaTitle;
    private TextView customBcaVaTitle;
    private TextView installmentTitle;
    private TextView bniPointTitle;
    private TextView paymentChannelsTitle;
    private TextView autoReadSmsTitle;

    /**
     * Selection Container
     **/
    private RadioGroup cardClickContainer;
    private RadioGroup secureContainer;
    private RadioGroup bankContainer;
    private RadioGroup expiryContainer;
    private RadioGroup saveCardContainer;
    private RadioGroup promoContainer;
    private RadioGroup preAuthContainer;
    private RadioGroup colorThemeContainer;
    private LinearLayout customBcaVaContainer;
    private LinearLayout customPermataVaContainer;
    private RadioGroup installmentContainer;
    private RadioGroup bniPointContainer;
    private LinearLayout paymentChannelsContainer;
    private LinearLayout changeInstallmentContainer;
    private RadioGroup autoReadSmsContainer;

    /**
     * Radio Button selection for installment
     */
    private AppCompatRadioButton installmentBniSelection;
    private AppCompatRadioButton installmentMandiriSelection;
    private AppCompatRadioButton installmentBcaSelection;
    private AppCompatRadioButton installmentBriSelection;
    private AppCompatRadioButton noInstallmentSelection;
    /**
     * Radio Button selection for BNI Point
     */
    private AppCompatRadioButton bniPointOnlyEnabledSelection;
    private AppCompatRadioButton bniPointOnlyDisabledSelection;
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
     * Radio Button Selection for Promo
     **/
    private AppCompatRadioButton promoDisabledSelection;
    private AppCompatRadioButton promoEnabledSelection;
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


    private FancyButton nextButton;
    private ImageButton editBcaVaButton;
    private ImageButton editPermataVaButton;
    private ImageButton editPaymentChannels;
    private ImageButton editInstallmentMandiri;
    private ImageButton editInstallmentBca;
    private ImageButton editInstallmentBni;
    private ImageButton editInstallmentBri;
    private boolean installmentRequired;

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
        initPromoSelection();
        initPreAuthSelection();
        initInstallmentSelection();
        initChangeInstallmentOption();
        initBniPointSelection();
        initAutoReadOtpSelection();
        initTitleClicks();
        initNextButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unselectAllTitles();
        hideAllSelections();
    }

    private void bindViews() {
        cardClickTitle = (TextView) findViewById(R.id.title_credit_card_type);
        secureTitle = (TextView) findViewById(R.id.title_secure_type);
        bankTitle = (TextView) findViewById(R.id.title_issuing_bank_type);
        expiryTitle = (TextView) findViewById(R.id.title_custom_expiry_type);
        saveCardTitle = (TextView) findViewById(R.id.title_default_save_card_type);
        promoTitle = (TextView) findViewById(R.id.title_promo_type);
        preAuthTitle = (TextView) findViewById(R.id.title_pre_auth_type);
        colorThemeTitle = (TextView) findViewById(R.id.title_color_theme_type);
        customPermataVaTitle = (TextView) findViewById(R.id.title_custom_permata_va);
        customBcaVaTitle = (TextView) findViewById(R.id.title_custom_bca_va);
        installmentTitle = (TextView) findViewById(R.id.title_installment_type);
        bniPointTitle = (TextView) findViewById(R.id.title_bni_point_type);
        paymentChannelsTitle = (TextView) findViewById(R.id.title_custom_payment_channels);
        autoReadSmsTitle = (TextView) findViewById(R.id.title_auto_read_type);

        cardClickContainer = (RadioGroup) findViewById(R.id.credit_card_type_container);
        secureContainer = (RadioGroup) findViewById(R.id.secure_type_container);
        bankContainer = (RadioGroup) findViewById(R.id.issuing_bank_type_container);
        expiryContainer = (RadioGroup) findViewById(R.id.expiry_type_container);
        saveCardContainer = (RadioGroup) findViewById(R.id.default_save_type_container);
        promoContainer = (RadioGroup) findViewById(R.id.promo_type_container);
        preAuthContainer = (RadioGroup) findViewById(R.id.preauth_type_container);
        colorThemeContainer = (RadioGroup) findViewById(R.id.theme_type_container);
        customBcaVaContainer = (LinearLayout) findViewById(R.id.custom_bca_va_type_container);
        customPermataVaContainer = (LinearLayout) findViewById(R.id.custom_permata_va_type_container);
        installmentContainer = (RadioGroup) findViewById(R.id.enable_installment_container);
        changeInstallmentContainer = (LinearLayout) findViewById(R.id.change_installment_container);
        bniPointContainer = (RadioGroup) findViewById(R.id.bni_point_type_container);
        paymentChannelsContainer = (LinearLayout) findViewById(R.id.payment_channels_type_container);
        autoReadSmsContainer = (RadioGroup) findViewById(R.id.auto_read_type_container);

        installmentBniSelection = (AppCompatRadioButton) findViewById(R.id.installment_type_bni);
        installmentMandiriSelection = (AppCompatRadioButton) findViewById(R.id.installment_type_mandiri);
        installmentBcaSelection = (AppCompatRadioButton) findViewById(R.id.installment_type_bca);
        installmentBriSelection = (AppCompatRadioButton) findViewById(R.id.installment_type_bri);
        noInstallmentSelection = (AppCompatRadioButton) findViewById(R.id.no_installment);

        normalSelection = (AppCompatRadioButton) findViewById(R.id.type_credit_card_normal);
        twoClicksSelection = (AppCompatRadioButton) findViewById(R.id.type_credit_card_two_clicks);
        oneClickSelection = (AppCompatRadioButton) findViewById(R.id.type_credit_card_one_click);

        secureDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_secure_disabled);
        secureEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_secure_enabled);

        bankNoneSelection = (AppCompatRadioButton) findViewById(R.id.type_bank_none);
        bankBniSelection = (AppCompatRadioButton) findViewById(R.id.type_bank_bni);
        bankMandiriSelection = (AppCompatRadioButton) findViewById(R.id.type_bank_mandiri);
        bankBcaSelection = (AppCompatRadioButton) findViewById(R.id.type_bank_bca);
        bankMaybankSelection = (AppCompatRadioButton) findViewById(R.id.type_bank_maybank);
        bankBriSelection = (AppCompatRadioButton) findViewById(R.id.type_bank_bri);
        bankCimbSelection = (AppCompatRadioButton) findViewById(R.id.type_bank_cimb);

        expiryNoneSelection = (AppCompatRadioButton) findViewById(R.id.type_expiry_none);
        expiryOneMinuteSelection = (AppCompatRadioButton) findViewById(R.id.type_expiry_one_minute);
        expiryOneHourSelection = (AppCompatRadioButton) findViewById(R.id.type_expiry_one_hour);

        saveCardDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_default_save_disabled);
        saveCardEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_default_save_enabled);

        customBcaVaDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_bca_va_disabled);
        customBcaVaEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_bca_va_enabled);

        customPermataVaDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_permata_va_disabled);
        customPermataVaEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_permata_va_enabled);

        promoDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_promo_disabled);
        promoEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_promo_enabled);

        preAuthDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_preauth_disabled);
        preAuthEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_preauth_enabled);

        defaultThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_default);
        redThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_red);
        greenThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_green);
        orangeThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_orange);
        blackThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_black);

        bniPointOnlyEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_bni_point_enabled);
        bniPointOnlyDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_bni_point_disabled);

        paymentChannelsAllSelection = (AppCompatRadioButton) findViewById(R.id.type_payment_channels_show_all);
        paymentChannelsSelectedSelection = (AppCompatRadioButton) findViewById(R.id.type_payment_channels_show_selected);

        autoReadSmsDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_auto_read_disabled);
        autoReadSmsEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_auto_read_enabled);

        nextButton = (FancyButton) findViewById(R.id.btn_launch_app);
        editBcaVaButton = (ImageButton) findViewById(R.id.btn_edit_bca_va);
        editPermataVaButton = (ImageButton) findViewById(R.id.btn_edit_permata_va);
        editPaymentChannels = (ImageButton) findViewById(R.id.btn_edit_payment_method);
        editInstallmentMandiri = (ImageButton) findViewById(R.id.button_mandiri_installment_edit);
        editInstallmentBca = (ImageButton) findViewById(R.id.button_bca_installment_edit);
        editInstallmentBni = (ImageButton) findViewById(R.id.button_bni_installment_edit);
        editInstallmentBri = (ImageButton) findViewById(R.id.button_bri_installment_edit);
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

        promoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!promoTitle.isSelected()) {
                            unselectAllTitles();
                            hideAllSelections();
                            promoTitle.setSelected(true);
                            setTextViewSelectedColor(promoTitle);
                            setTextViewDrawableLeftColorFilter(promoTitle);
                            // Show promo container
                            promoContainer.setVisibility(View.VISIBLE);
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
                }
            }
        });
    }

    private void initSecureSelection() {
        boolean secureEnabled = DemoPreferenceHelper.getBooleanPreference(this, SECURE_TYPE, false);

        if (secureEnabled) {
            secureTitle.setText(R.string.secure_type_enabled);
            secureEnabledSelection.setChecked(true);
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
                    break;
                case Constants.BANK_MANDIRI:
                    bankTitle.setText(R.string.acquiring_bank_by_mandiri);
                    bankMandiriSelection.setChecked(true);
                    break;
                case Constants.BANK_BCA:
                    bankTitle.setText(R.string.acquiring_bank_by_bca);
                    bankBcaSelection.setChecked(true);
                    break;
                case Constants.BANK_MAYBANK:
                    bankTitle.setText(R.string.acquiring_bank_by_maybank);
                    bankMaybankSelection.setChecked(true);
                    break;
                case Constants.BANK_BRI:
                    bankTitle.setText(R.string.acquiring_bank_by_bri);
                    bankBriSelection.setChecked(true);
                    break;
                case Constants.BANK_CIMB:
                    bankTitle.setText(R.string.acquiring_bank_by_cimb);
                    bankCimbSelection.setChecked(true);
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
            enabledPayments = mapPaymentMethods(listViewModel.getEnabledPayments());
            paymentChannelsTitle.setText(R.string.payment_channels_selected);
            if (isContainEChannel(enabledPayments)) {
                paymentChannelsSelectedSelection.setText(getString(R.string.payment_channels_selection_show_selected_format, enabledPayments.size() - 1));
            } else {
                paymentChannelsSelectedSelection.setText(getString(R.string.payment_channels_selection_show_selected_format, enabledPayments.size()));
            }
            paymentChannelsSelectedSelection.setChecked(true);
            initEditPaymentButton(PaymentMethods.getPaymentList(DemoConfigActivity.this, mapEnabledPayments()));
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
                                initEditPaymentButton(PaymentMethods.getPaymentList(DemoConfigActivity.this, mapEnabledPayments()));

                            }

                            @Override
                            public void onSelectAll(List<SelectPaymentMethodViewModel> enabledPayments) {
                                paymentChannelsTitle.setText(R.string.payment_channels_show_all);
                                paymentChannelsAllSelection.setChecked(true);
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
                        initEditPaymentButton(PaymentMethods.getPaymentList(DemoConfigActivity.this, mapEnabledPayments()));
                    }

                    @Override
                    public void onSelectAll(List<SelectPaymentMethodViewModel> enabledPayments) {
                        paymentChannelsTitle.setText(R.string.payment_channels_show_all);
                        paymentChannelsAllSelection.setChecked(true);
                        disableEditPaymentChannels();
                    }
                });
                fragment.show(getSupportFragmentManager(), "");
            }
        });
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
            initEditBcaVaButton(customBcaVaNumber);
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
                        final CustomBcaVaInputDialogFragment fragment = CustomBcaVaInputDialogFragment.newInstance(getSelectedColorPrimaryDark(), new CustomVaDialogListener() {
                            @Override
                            public void onOkClicked(String input) {
                                customBcaVaTitle.setText(R.string.custom_bca_va_enabled);
                                customBcaVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                                initEditBcaVaButton(input);
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
            initEditPermataVaButton(customPermataVaNumber);
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
                        final CustomPermataVaInputDialogFragment fragment = CustomPermataVaInputDialogFragment.newInstance(getSelectedColorPrimaryDark(), new CustomVaDialogListener() {
                            @Override
                            public void onOkClicked(String input) {
                                customPermataVaTitle.setText(R.string.custom_permata_va_enabled);
                                customPermataVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
                                initEditPermataVaButton(input);
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

    private void initEditBcaVaButton(final String number) {
        editBcaVaButton.setVisibility(View.VISIBLE);
        editBcaVaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CustomBcaVaInputDialogFragment fragment = CustomBcaVaInputDialogFragment.newInstance(number, getSelectedColorPrimaryDark(), new CustomVaDialogListener() {
                    @Override
                    public void onOkClicked(String input) {
                        customBcaVaTitle.setText(R.string.custom_bca_va_enabled);
                        customBcaVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
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

    private void initEditPermataVaButton(final String number) {
        editPermataVaButton.setVisibility(View.VISIBLE);
        editPermataVaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CustomPermataVaInputDialogFragment fragment = CustomPermataVaInputDialogFragment.newInstance(number, getSelectedColorPrimaryDark(), new CustomVaDialogListener() {
                    @Override
                    public void onOkClicked(String input) {
                        customPermataVaTitle.setText(R.string.custom_permata_va_enabled);
                        customPermataVaEnabledSelection.setText(getString(R.string.custom_va_enabled_format, input));
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

    private void initPromoSelection() {
        boolean promoEnabled = DemoPreferenceHelper.getBooleanPreference(this, PROMO_TYPE, false);

        if (promoEnabled) {
            promoTitle.setText(R.string.default_promo_enabled);
            promoEnabledSelection.setChecked(true);
        } else {
            promoTitle.setText(R.string.default_promo_disabled);
            promoDisabledSelection.setChecked(true);
        }

        promoEnabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    promoTitle.setText(R.string.default_promo_enabled);
                }
            }
        });

        promoDisabledSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    promoTitle.setText(R.string.default_promo_disabled);
                }
            }
        });
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
                    updateSelectedInstallment();
                    showEditInstallmentMandiriOption();
                    resetAcquiringBank();
                    resetOneClickSelection();
                    break;
                case Constants.INSTALLMENT_BCA:
                    String bcaTitle = getString(R.string.using_bca_installment);
                    installmentTitle.setText(bcaTitle);
                    installmentMandiriSelection.setChecked(true);
                    resetBniPointSelection();
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
                    updateSelectedInstallment();
                    showEditInstallmentBriOption();
                    setBriAcquiringBank();
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
                    showInstallmentDialog(false);
                    showEditInstallmentBriOption();
                    setBriAcquiringBank();
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
    }

    private void hideEditInstallmentOption() {
        editInstallmentMandiri.setVisibility(View.INVISIBLE);
        editInstallmentBca.setVisibility(View.INVISIBLE);
        editInstallmentBni.setVisibility(View.INVISIBLE);
        editInstallmentBri.setVisibility(View.INVISIBLE);
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
    }

    private void resetBniPointSelection() {
        bniPointOnlyDisabledSelection.setChecked(true);
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
                savePromoSelection();
                savePreAuthSelection();
                saveInstallmentSelection();
                saveBniPointSelection();
                saveAutoReadSmsSelection();
                saveEnabledPayments();

                TransactionRequest transactionRequest = initializePurchaseRequest();
                MidtransSDK.getInstance().setTransactionRequest(transactionRequest);

                startActivity(new Intent(DemoConfigActivity.this, DemoProductListActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
            DemoPreferenceHelper.setBooleanPreference(this, SECURE_TYPE, true);
        } else {
            DemoPreferenceHelper.setBooleanPreference(this, SECURE_TYPE, false);
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
            LocalDataHandler.saveObject(PAYMENT_CHANNELS_TYPE, new SelectPaymentMethodListViewModel(PaymentMethods.getPaymentList(this, mapEnabledPayments())));
        }
    }

    private void saveBcaVaNumber() {
        if (customBcaVaEnabledSelection.isChecked()) {
            String vaNumber = customBcaVaEnabledSelection.getText().toString().split(" - ")[1];
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_BCA_VA_NUMBER, vaNumber);
        } else {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_BCA_VA_NUMBER, "");
        }
    }

    private void savePermataVaNumber() {
        if (customPermataVaEnabledSelection.isChecked()) {
            String vaNumber = customPermataVaEnabledSelection.getText().toString().split(" - ")[1];
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_PERMATA_VA_NUMBER, vaNumber);
        } else {
            DemoPreferenceHelper.setStringPreference(this, CUSTOM_PERMATA_VA_NUMBER, "");
        }
    }

    private void savePromoSelection() {
        if (promoEnabledSelection.isChecked()) {
            DemoPreferenceHelper.setBooleanPreference(this, PROMO_TYPE, true);
        } else {
            DemoPreferenceHelper.setBooleanPreference(this, PROMO_TYPE, false);
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
        promoTitle.setSelected(false);
        promoTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(promoTitle);
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
        installmentTitle.setSelected(false);
        installmentTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(installmentTitle);
        bniPointTitle.setSelected(false);
        bniPointTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(bniPointTitle);
        paymentChannelsTitle.setSelected(false);
        paymentChannelsTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(paymentChannelsTitle);
        autoReadSmsTitle.setSelected(false);
        autoReadSmsTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        clearTextViewDrawableLeftColorFilter(autoReadSmsTitle);
    }

    private void hideAllSelections() {
        cardClickContainer.setVisibility(View.GONE);
        secureContainer.setVisibility(View.GONE);
        bankContainer.setVisibility(View.GONE);
        expiryContainer.setVisibility(View.GONE);
        saveCardContainer.setVisibility(View.GONE);
        promoContainer.setVisibility(View.GONE);
        preAuthContainer.setVisibility(View.GONE);
        colorThemeContainer.setVisibility(View.GONE);
        customBcaVaContainer.setVisibility(View.GONE);
        customPermataVaContainer.setVisibility(View.GONE);
        installmentContainer.setVisibility(View.GONE);
        changeInstallmentContainer.setVisibility(View.GONE);
        bniPointContainer.setVisibility(View.GONE);
        paymentChannelsContainer.setVisibility(View.GONE);
        autoReadSmsContainer.setVisibility(View.GONE);
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

        promoDisabledSelection.setSupportButtonTintList(colorStateList);
        promoEnabledSelection.setSupportButtonTintList(colorStateList);

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
        noInstallmentSelection.setSupportButtonTintList(colorStateList);

        bniPointOnlyEnabledSelection.setSupportButtonTintList(colorStateList);
        bniPointOnlyDisabledSelection.setSupportButtonTintList(colorStateList);

        paymentChannelsAllSelection.setSupportButtonTintList(colorStateList);
        paymentChannelsSelectedSelection.setSupportButtonTintList(colorStateList);

        autoReadSmsDisabledSelection.setSupportButtonTintList(colorStateList);
        autoReadSmsEnabledSelection.setSupportButtonTintList(colorStateList);
    }

    private void initMidtransSDK() {
        SdkUIFlowBuilder.init(this, BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL, this)
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
        TransactionRequest transactionRequestNew = new TransactionRequest(System.currentTimeMillis() + "", 20000);


        // Define item details
        ItemDetails itemDetails = new ItemDetails("1", 20000, 1, getString(R.string.product_name_sample));
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
        }

        if (preAuthEnabledSelection.isChecked()) {
            // Set Pre Auth mode
            creditCard.setType(CardTokenRequest.TYPE_AUTHORIZE);
        }

        String cardClickType;

        if (normalSelection.isChecked()) {
            cardClickType = getString(R.string.card_click_type_none);
            if (secureEnabledSelection.isChecked()) {
                creditCard.setSecure(true);
            } else {
                creditCard.setSecure(false);
            }
            transactionRequestNew.setCreditCard(creditCard);
        } else if (twoClicksSelection.isChecked()) {
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

        MidtransSDK.getInstance().setUIKitCustomSetting(uiKitCustomSetting);

        if (secureEnabledSelection.isChecked()) {
            transactionRequestNew.setCardPaymentInfo(cardClickType, true);
        } else {
            transactionRequestNew.setCardPaymentInfo(cardClickType, false);
        }

        if (paymentChannelsSelectedSelection.isChecked()) {
            transactionRequestNew.setEnabledPayments(mapEnabledPayments());
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

        if (promoEnabledSelection.isChecked()) {
            // Set promo
            transactionRequestNew.setPromoEnabled(true);
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
            userAddresses.add(userAddress);
            userDetail.setUserAddresses(userAddresses);
        } else {
            if (oneClickSelection.isChecked()) {
                userDetail.setUserId(getString(R.string.sample_user_id));
            } else if (twoClicksSelection.isChecked()) {
                userDetail.setUserId(getString(R.string.sample_user_id2));
            }
        }
        LocalDataHandler.saveObject(getString(R.string.user_details), userDetail);
        if (customPermataVaEnabledSelection.isChecked()) {
            String vaNumber = customPermataVaEnabledSelection.getText().toString().split(" - ")[1];
            transactionRequestNew.setPermataVa(
                    new BankTransferRequestModel(vaNumber)
            );
        }
        if (customBcaVaEnabledSelection.isChecked()) {
            String vaNumber = customBcaVaEnabledSelection.getText().toString().split(" - ")[1];
            transactionRequestNew.setBcaVa(
                    new BankTransferRequestModel(vaNumber)
            );
        }

        return transactionRequestNew;
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
            //add bni bin number for normal payment and 3DS
            whiteListBins.add("410505");
            whiteListBins.add("526422");
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
            setInstallmentBankTerm(bankTerms, Constants.INSTALLMENT__BANK_BCA);
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
        term.add(6);
        term.add(12);
        //set bank and term
        bankTerms.put(bank, term);
    }

    private List<String> mapEnabledPayments() {
        List<String> mappedPayments = new ArrayList<>();
        for (SelectPaymentMethodViewModel model : enabledPayments) {
            if (model.getMethodType().equalsIgnoreCase(getString(R.string.payment_bank_transfer))) {
                mappedPayments.add(getString(R.string.payment_mandiri_bill_payment));
            }
            mappedPayments.add(model.getMethodType());
        }
        return mappedPayments;
    }

    private List<SelectPaymentMethodViewModel> mapPaymentMethods(List<EnabledPayment> enabledPayments) {
        List<SelectPaymentMethodViewModel> viewModels = new ArrayList<>();
        for (int i = 0; i < enabledPayments.size(); i++) {
            EnabledPayment enabledPayment = enabledPayments.get(i);
            PaymentMethodsModel model = PaymentMethods.getMethods(this, enabledPayment.getType());
            if (model != null) {
                viewModels.add(new SelectPaymentMethodViewModel(model.getName(), enabledPayment.getType(), true));
            }
        }
        return viewModels;
    }
}
