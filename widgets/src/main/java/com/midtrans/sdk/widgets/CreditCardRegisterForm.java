package com.midtrans.sdk.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.widgets.utils.CardUtils;
import com.midtrans.sdk.widgets.utils.WidgetException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rakawm on 9/13/16.
 */
public class CreditCardRegisterForm extends LinearLayout {
    private static final int MONTH_COUNT = 12;

    private ImageView logo;
    private Button registerButton, cvvHelpBtn;
    private EditText cardNumber, cardCvvNumber, cardExpiry;
    private TextInputLayout cardNumberContainer, cardCvvNumberContainer, cardExpiryContainer;
    private WidgetSaveCardCallback widgetTransactionCallback;
    private String midtransClientKey;
    private String merchantUrl;
    private String userId;
    private MidtransSDK midtransSDK;

    public CreditCardRegisterForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        initCardNumberEditText();
        initCardExpiryEditText();
        initCardCVVEditText();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void init(Context context, AttributeSet attrs) {
        // Inflate custom layout
        LayoutInflater.from(context).inflate(R.layout.credit_card_register_form, this, true);
        // Define layout components
        registerButton = (Button) findViewById(R.id.button_pay);
        cvvHelpBtn = (Button) findViewById(R.id.button_what_is_cvv);
        cardNumber = (EditText) findViewById(R.id.card_number);
        cardCvvNumber = (EditText) findViewById(R.id.card_cvv_number);
        cardExpiry = (EditText) findViewById(R.id.card_expiry);
        cardNumberContainer = (TextInputLayout) findViewById(R.id.card_number_container);
        cardCvvNumberContainer = (TextInputLayout) findViewById(R.id.card_cvv_number_container);
        cardExpiryContainer = (TextInputLayout) findViewById(R.id.card_expiry_container);
        logo = (ImageView) findViewById(R.id.payment_card_logo);

        // Show dialog for CVV hint
        cvvHelpBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(R.layout.dialog_cvv)
                        .setPositiveButton(R.string.cvv_hint_got_it, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        // Get values from XML
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardFormStyle);
        userId = typedArray.getString(R.styleable.CreditCardFormStyle_mtcc_user_id);
        midtransClientKey = typedArray.getString(R.styleable.CreditCardFormStyle_mtcc_client_key);
        merchantUrl = typedArray.getString(R.styleable.CreditCardFormStyle_mtcc_merchant_url);
        boolean isRegisterButtonShown = typedArray.getBoolean(R.styleable.CreditCardFormStyle_mtcc_show_pay, false);

        // Update charge button visibility
        if (isRegisterButtonShown) {
            showRegisterButton();
        } else {
            hideRegisterButton();
        }

        typedArray.recycle();
    }

    private void initRegisterButton() {
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (widgetTransactionCallback != null) {
                    register(widgetTransactionCallback);
                } else {
                    throw new WidgetException("Must provide save card callback");
                }
            }
        });
    }

    public void showRegisterButton() {
        registerButton.setVisibility(VISIBLE);
        initRegisterButton();
    }

    public void hideRegisterButton() {
        registerButton.setVisibility(GONE);
    }

    private void initCardNumberEditText() {
        cardNumber.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cardType = Utils.getCardType(s.toString());
                cardNumberContainer.setError(null);
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf
                            (space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }

                setCardType();

                // Move to next input
                if (s.length() >= 18 && cardType.equals(CardUtils.CARD_TYPE_AMEX)) {
                    if (s.length() == 19) {
                        s.delete(s.length() - 1, s.length());
                    }
                    cardExpiry.requestFocus();
                } else if (s.length() == 19) {
                    cardExpiry.requestFocus();
                }
            }
        });

        cardNumber.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardNumberValidity();
                }
            }
        });
    }

    private void initCardExpiryEditText() {
        cardExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String expiryDateText = cardExpiry.getText().toString();
                if (s.length() == 2) {
                    if (!expiryDateText.endsWith("/")) {
                        try {
                            int month = Integer.parseInt(input);
                            if (month <= 12) {
                                cardExpiry.setText(getResources().getString(R.string.payment_credit_card_expiry_month_format, cardExpiry.getText().toString()));
                                cardExpiry.setSelection(cardExpiry.getText().toString().length());
                            } else {
                                cardExpiry.setText(getResources().getString(R.string.payment_credit_card_expiry_month_int_format, MONTH_COUNT));
                                cardExpiry.setSelection(cardExpiry.getText().toString().length());
                            }
                        } catch (Exception exception) {

                        }
                    } else {
                        try {
                            int month = Integer.parseInt(input);
                            if (month <= 12) {
                                cardExpiry.setText(cardExpiry.getText().toString().substring(0, 1));
                                cardExpiry.setSelection(cardExpiry.getText().toString().length());
                            } else {
                                cardExpiry.setText("");
                                cardExpiry.setSelection(cardExpiry.getText().toString().length());
                            }
                        } catch (Exception exception) {
                            //LogUtils.error(TAG, exception.toString(), exception);
                        }
                    }
                } else if (s.length() == 1) {
                    try {
                        int month = Integer.parseInt(input);
                        if (month > 1) {
                            cardExpiry.setText(
                                    getResources().getString(
                                            R.string.payment_credit_card_expiry_month_single_digit_format,
                                            cardExpiry.getText().toString()
                                    )
                            );
                            cardExpiry.setSelection(cardExpiry.getText().toString().length());
                        }
                    } catch (Exception exception) {
                        //LogUtils.error(TAG, exception.toString(), exception);
                    }
                }

                // Move to next input
                if (s.length() == 5) {
                    cardCvvNumber.requestFocus();
                }
            }
        });

        cardExpiry.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardExpiryValidity();
                }
            }
        });
    }

    private void initCardCVVEditText() {
        cardCvvNumber.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardCVVValidity();
                }
            }
        });
    }

    public boolean checkCardValidity() {
        boolean cardNumberValidity = checkCardNumberValidity();
        boolean cardExpiryValidity = checkCardExpiryValidity();
        boolean cardCVVValidity = checkCardCVVValidity();
        return cardNumberValidity && cardExpiryValidity && cardCVVValidity;
    }

    private boolean checkCardNumberValidity() {
        boolean isValid = true;

        String cardNumberText = cardNumber.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumberText)) {
            cardNumberContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_card_no));
            isValid = false;
        } else {
            cardNumberContainer.setError(null);
        }

        if (cardNumberText.length() < 13 || !CardUtils.isValidCardNumber(cardNumberText)) {
            cardNumberContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_invalid_card_no));
            isValid = false;
        } else {
            cardNumberContainer.setError(null);
        }
        return isValid;
    }

    private boolean checkCardExpiryValidity() {
        boolean isValid = true;
        String expiryDateText = cardExpiry.getText().toString().trim();
        int expMonth = 0, expYear = 0;
        String[] expDateArray;

        if (TextUtils.isEmpty(expiryDateText)) {
            cardExpiryContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_expiry_date));
            isValid = false;
        } else {
            expDateArray = expiryDateText.split("/");

            if (!expiryDateText.contains("/")) {
                cardExpiryContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_invalid_expiry_date));
                isValid = false;
            } else if (expDateArray.length != 2) {
                cardExpiryContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_invalid_expiry_date));
                isValid = false;
            } else {
                try {
                    expMonth = Integer.parseInt(expDateArray[0]);
                } catch (NumberFormatException e) {
                    cardExpiryContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_invalid_expiry_date));
                    isValid = false;
                }
                try {
                    expYear = Integer.parseInt(expDateArray[1]);
                } catch (NumberFormatException e) {
                    cardExpiryContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_invalid_expiry_date));
                    isValid = false;
                }
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yy");
                String year = format.format(date);

                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentYear = Integer.parseInt(year);

                if (expYear < currentYear) {
                    cardExpiryContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_invalid_expiry_date));
                    isValid = false;
                } else if (expYear == currentYear && currentMonth > expMonth) {
                    cardExpiryContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_invalid_expiry_date));
                    isValid = false;
                } else {
                    cardExpiryContainer.setError(null);
                }
            }
        }
        return isValid;
    }

    private boolean checkCardCVVValidity() {
        boolean isValid = true;
        String cvvText = cardCvvNumber.getText().toString().trim();
        if (TextUtils.isEmpty(cvvText)) {
            cardCvvNumberContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_cvv));
            isValid = false;
        } else {
            if (cvvText.length() < 3) {
                cardCvvNumberContainer.setError(getResources().getString(R.string.payment_credit_card_validation_message_invalid_cvv));
                isValid = false;
            } else {
                cardCvvNumberContainer.setError(null);
            }
        }
        return isValid;
    }

    private void setCardType() {
        // Don't set card type when card number is empty
        String cardNumberText = cardNumber.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 2) {
            logo.setImageResource(0);
            return;
        }

        // Check card type before setting logo
        String cardType = CardUtils.getCardType(cardNumberText);
        switch (cardType) {
            case CardUtils.CARD_TYPE_VISA:
                logo.setImageResource(R.drawable.ic_visa);
                break;
            case CardUtils.CARD_TYPE_MASTERCARD:
                logo.setImageResource(R.drawable.ic_mastercard);
                break;
            case CardUtils.CARD_TYPE_JCB:
                logo.setImageResource(R.drawable.ic_jcb);
                break;
            case CardUtils.CARD_TYPE_AMEX:
                logo.setImageResource(R.drawable.ic_amex);
                break;
        }
    }

    private MidtransSDK getMidtransSDK() {
        if (midtransSDK != null) {
            return midtransSDK;
        } else {
            if (TextUtils.isEmpty(midtransClientKey)) {
                throw new WidgetException(getResources().getString(R.string.error_client_key_missing));
            }

            if (TextUtils.isEmpty(merchantUrl)) {
                throw new WidgetException(getResources().getString(R.string.error_merchant_url_missing));
            }

            return SdkCoreFlowBuilder.init(getContext(), midtransClientKey, merchantUrl)
                    .enableLog(true)
                    .buildSDK();
        }
    }

    public void register(WidgetSaveCardCallback cardCallback) {
        if (checkCardValidity()) {
            setWidgetTransactionCallback(cardCallback);
            getMidtransSDK().cardRegistration(
                    cardNumber.getText().toString().replace(" ", ""),
                    cardCvvNumber.getText().toString(),
                    cardExpiry.getText().toString().split("/")[0],
                    "20" + cardExpiry.getText().toString().split("/")[1],
                    new CardRegistrationCallback() {
                        @Override
                        public void onSuccess(CardRegistrationResponse response) {
                            getCards(response);
                        }

                        @Override
                        public void onFailure(CardRegistrationResponse response, String reason) {
                            if (widgetTransactionCallback != null) {
                                widgetTransactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_failed_get_token)));
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (widgetTransactionCallback != null) {
                                widgetTransactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_failed_get_token)));
                            }
                        }
                    }
            );
        }
    }

    private void getCards(final CardRegistrationResponse cardRegistrationResponse) {
        if (TextUtils.isEmpty(userId)) {
            userId = UUID.randomUUID().toString();
        }
        getMidtransSDK().getCards(userId, new GetCardCallback() {
            @Override
            public void onSuccess(ArrayList<SaveCardRequest> response) {
                SaveCardRequest request = new SaveCardRequest(
                        cardRegistrationResponse.getSavedTokenId(),
                        cardRegistrationResponse.getMaskedCard(),
                        Utils.getCardType(cardNumber.getText().toString().replace(" ", ""))
                );
                response.add(request);
                saveCard(response);
            }

            @Override
            public void onFailure(String reason) {
                SaveCardRequest request = new SaveCardRequest(
                        cardRegistrationResponse.getSavedTokenId(),
                        cardRegistrationResponse.getMaskedCard(),
                        Utils.getCardType(cardNumber.getText().toString().replace(" ", ""))
                );
                ArrayList<SaveCardRequest> saveCardRequests = new ArrayList<>();
                saveCardRequests.add(request);
                saveCard(saveCardRequests);
            }

            @Override
            public void onError(Throwable error) {
                SaveCardRequest request = new SaveCardRequest(
                        cardRegistrationResponse.getSavedTokenId(),
                        cardRegistrationResponse.getMaskedCard(),
                        Utils.getCardType(cardNumber.getText().toString().replace(" ", ""))
                );
                ArrayList<SaveCardRequest> saveCardRequests = new ArrayList<>();
                saveCardRequests.add(request);
                saveCard(saveCardRequests);
            }
        });

    }

    private void saveCard(ArrayList<SaveCardRequest> saveCardRequests) {
        getMidtransSDK().saveCards(userId, saveCardRequests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                if (widgetTransactionCallback != null) {
                    widgetTransactionCallback.onSucceed(response);
                }
            }

            @Override
            public void onFailure(String reason) {
                if (widgetTransactionCallback != null) {
                    widgetTransactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_saving_card)));
                }
            }

            @Override
            public void onError(Throwable error) {
                if (widgetTransactionCallback != null) {
                    widgetTransactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_saving_card)));
                }
            }
        });
    }

    public void setWidgetTransactionCallback(WidgetSaveCardCallback widgetTransactionCallback) {
        this.widgetTransactionCallback = widgetTransactionCallback;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMidtransClientKey() {
        return midtransClientKey;
    }

    public void setMidtransClientKey(String midtransClientKey) {
        this.midtransClientKey = midtransClientKey;
    }

    public String getMerchantUrl() {
        return merchantUrl;
    }

    public void setMerchantUrl(String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }

    /**
     * Will be used to get response callback after charging in this view.
     */
    public interface WidgetSaveCardCallback {
        void onSucceed(SaveCardResponse transactionResponse);

        void onFailed(Throwable throwable);
    }
    

}
