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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.scancard.ScanCard;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;

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
    private static int DELAY = 200;
    private String selectedColor = DemoThemeConstants.BLUE_THEME;
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

    private FancyButton nextButton;

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
        initPromoSelection();
        initPreAuthSelection();
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

        cardClickContainer = (RadioGroup) findViewById(R.id.credit_card_type_container);
        secureContainer = (RadioGroup) findViewById(R.id.secure_type_container);
        bankContainer = (RadioGroup) findViewById(R.id.issuing_bank_type_container);
        expiryContainer = (RadioGroup) findViewById(R.id.expiry_type_container);
        saveCardContainer = (RadioGroup) findViewById(R.id.default_save_type_container);
        promoContainer = (RadioGroup) findViewById(R.id.promo_type_container);
        preAuthContainer = (RadioGroup) findViewById(R.id.preauth_type_container);
        colorThemeContainer = (RadioGroup) findViewById(R.id.theme_type_container);

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

        expiryNoneSelection = (AppCompatRadioButton) findViewById(R.id.type_expiry_none);
        expiryOneMinuteSelection = (AppCompatRadioButton) findViewById(R.id.type_expiry_one_minute);
        expiryOneHourSelection = (AppCompatRadioButton) findViewById(R.id.type_expiry_one_hour);

        saveCardDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_default_save_disabled);
        saveCardEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_default_save_enabled);

        promoDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_promo_disabled);
        promoEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_promo_enabled);

        preAuthDisabledSelection = (AppCompatRadioButton) findViewById(R.id.type_preauth_disabled);
        preAuthEnabledSelection = (AppCompatRadioButton) findViewById(R.id.type_preauth_enabled);

        defaultThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_default);
        redThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_red);
        greenThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_green);
        orangeThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_orange);
        blackThemeSelection = (AppCompatRadioButton) findViewById(R.id.type_theme_black);

        nextButton = (FancyButton) findViewById(R.id.btn_launch_app);
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
                savePromoSelection();
                savePreAuthSelection();

                TransactionRequest transactionRequest = initializePurchaseRequest();
                MidtransSDK.getInstance().setTransactionRequest(transactionRequest);

                startActivity(new Intent(DemoConfigActivity.this, DemoProductPageActivity.class));
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

        promoDisabledSelection.setSupportButtonTintList(colorStateList);
        promoEnabledSelection.setSupportButtonTintList(colorStateList);

        preAuthDisabledSelection.setSupportButtonTintList(colorStateList);
        preAuthEnabledSelection.setSupportButtonTintList(colorStateList);

        defaultThemeSelection.setSupportButtonTintList(colorStateList);
        redThemeSelection.setSupportButtonTintList(colorStateList);
        greenThemeSelection.setSupportButtonTintList(colorStateList);
        orangeThemeSelection.setSupportButtonTintList(colorStateList);
        blackThemeSelection.setSupportButtonTintList(colorStateList);
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
        if (saveCardEnabledSelection.isChecked()) {
            uiKitCustomSetting.setSaveCardChecked(true);
        } else {
            uiKitCustomSetting.setSaveCardChecked(false);
        }
        MidtransSDK.getInstance().setUIKitCustomSetting(uiKitCustomSetting);

        if (secureEnabledSelection.isChecked()) {
            transactionRequestNew.setCardPaymentInfo(cardClickType, true);
        } else {
            transactionRequestNew.setCardPaymentInfo(cardClickType, false);
        }

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

        if (LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class) == null) {
            UserDetail userDetail = new UserDetail();
            userDetail.setUserFullName(getString(R.string.order_review_customer_details_name));
            userDetail.setEmail(getString(R.string.order_review_customer_details_email));
            userDetail.setPhoneNumber(getString(R.string.order_review_customer_details_phone));
            userDetail.setUserId(getString(R.string.sample_user_id));
            ArrayList<UserAddress> userAddresses = new ArrayList<>();
            UserAddress userAddress = new UserAddress();
            userAddress.setAddress(getString(R.string.order_review_delivery_address_sample));
            userAddress.setCity(getString(R.string.order_review_delivery_address_city_sample));
            userAddress.setAddressType(com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BOTH);
            userAddresses.add(userAddress);
            userDetail.setUserAddresses(userAddresses);

            LocalDataHandler.saveObject(getString(R.string.user_details), userDetail);
        }
        return transactionRequestNew;
    }
}
