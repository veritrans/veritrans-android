package com.midtrans.sdk.widgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.widgets.adapter.CardPagerAdapter;
import com.midtrans.sdk.widgets.utils.CardUtils;
import com.midtrans.sdk.widgets.utils.CirclePageIndicator;
import com.midtrans.sdk.widgets.utils.WidgetException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Custom widget for showing credit card form.
 *
 * @author rakawm
 */
public class CreditCardForm extends NestedScrollView implements CardPagerAdapter.CardPagerListner {
    private static final int MONTH_COUNT = 12;
    private static final float CARD_ASPECT_RATIO = 0.62222f;

    private ImageView logo, cvvHelpImage, saveCardImage;
    private Button payBtn;
    private EditText cardNumber, cardCvvNumber, cardExpiry;
    private TextInputLayout cardNumberContainer, cardCvvNumberContainer, cardExpiryContainer;

    private AlertDialog webViewDialog;
    private WidgetTransactionCallback widgetTransactionCallback;
    private String midtransClientKey;
    private String merchantUrl;
    private String cardToken;
    private TransactionRequest transactionRequest;
    private MidtransSDK midtransSDK;
    private boolean enableTwoClick;
    private RelativeLayout layoutCardForm;
    private ArrayList<SaveCardRequest> creditCardList = new ArrayList<>();
    private RelativeLayout layoutSavedCards;
    private ViewPager savedCardPager;
    private RelativeLayout creditCardLayout;
    private CirclePageIndicator circlePageIndicator;
    private float cardWidth;
    private CardPagerAdapter cardPagerAdapter;
    private RelativeLayout layoutProgress;
    private boolean isPayBtnShown;
    private RelativeLayout saveCardContainer;
    private boolean iSTwoClickPayment;
    private CheckBox checkboxStoreCard;
    private CardTokenRequest cardTokenRequest;
    private Button backButton;
    private String userId;
    private WebView webView;
    private Animation layoutAnimation;

    public CreditCardForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        initCardNumberEditText();
        initCardExpiryEditText();
        initCardCVVEditText();
        midtransSDK = MidtransSDK.getInstance();
    }

    @Override
    protected void onDetachedFromWindow() {
        if(cardPagerAdapter != null){
            cardPagerAdapter.clearAll(savedCardPager);
            cardPagerAdapter = null;
        }
        savedCardPager = null;
        if(webView != null){
            webView.removeAllViews();
            webView.destroyDrawingCache();
            webView.clearHistory();
            webView.clearCache(true);
            webView.pauseTimers();
            webView.destroy();
        }
        layoutAnimation = null;
        layoutProgress = null;
        layoutSavedCards = null;
        layoutCardForm = null;
        creditCardLayout = null;
        webViewDialog = null;
        super.onDetachedFromWindow();
    }

    private void init(Context context, AttributeSet attrs) {
        // Inflate custom layout
        LayoutInflater.from(context).inflate(R.layout.credit_card_form, this, true);
        // Define layout components
        payBtn = (Button) findViewById(R.id.button_pay);
        cvvHelpImage = (ImageView) findViewById(R.id.image_what_is_cvv);
        saveCardImage = (ImageView) findViewById(R.id.image_question_save_card);
        cardNumber = (EditText) findViewById(R.id.card_number);
        cardCvvNumber = (EditText) findViewById(R.id.card_cvv_number);
        cardExpiry = (EditText) findViewById(R.id.card_expiry);
        cardNumberContainer = (TextInputLayout) findViewById(R.id.card_number_container);
        cardCvvNumberContainer = (TextInputLayout) findViewById(R.id.card_cvv_number_container);
        cardExpiryContainer = (TextInputLayout) findViewById(R.id.card_expiry_container);
        logo = (ImageView) findViewById(R.id.payment_card_logo);
        layoutCardForm = (RelativeLayout) findViewById(R.id.layout_card_form);
        layoutProgress = (RelativeLayout) findViewById(R.id.layout_progress);
        saveCardContainer = (RelativeLayout) findViewById(R.id.container_save_card);
        checkboxStoreCard = (CheckBox) findViewById(R.id.cb_store_card);
        backButton = (Button) findViewById(R.id.button_back);
        //saved cards layouts
        layoutSavedCards = (RelativeLayout) findViewById(R.id.layout_saved_cards);
        savedCardPager = (ViewPager) findViewById(R.id.saved_card_pager);
        creditCardLayout = (RelativeLayout) findViewById(R.id.credit_card_holder);

        calculateScreenWidth();
        // Show dialog for CVV hint
        cvvHelpImage.setOnClickListener(new OnClickListener() {
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

        saveCardImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getContext().getApplicationContext())
                        .setView(R.layout.dialog_save_card)
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

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iSTwoClickPayment = true;
                showCreditCardLayout(false);
            }
        });


        // Get values from XML
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardFormStyle);
        midtransClientKey = typedArray.getString(R.styleable.CreditCardFormStyle_mtcc_client_key);
        merchantUrl = typedArray.getString(R.styleable.CreditCardFormStyle_mtcc_merchant_url);
        userId = typedArray.getString(R.styleable.CreditCardFormStyle_mtcc_user_id);
        boolean isPayBtnShown = typedArray.getBoolean(R.styleable.CreditCardFormStyle_mtcc_show_pay, false);

        // Update charge button visibility
        showHidePayButton(isPayBtnShown);
        typedArray.recycle();

    }

    private void initNormalMode() {
        checkboxStoreCard.setChecked(false);
        saveCardContainer.setVisibility(GONE);
        showCreditCardLayout(true);
    }

    private void initTwoClickMode() {
        saveCardContainer.setVisibility(VISIBLE);
        getCardList();
    }

    private void showCreditCardLayout(boolean normalMode) {
        if (normalMode) {
            if (layoutCardForm.getVisibility() == View.GONE) {
                layoutCardForm.setVisibility(View.VISIBLE);
            }
            fadeInAnimation(layoutCardForm);
            if (layoutSavedCards.getVisibility() == View.VISIBLE) {
                layoutSavedCards.setVisibility(View.GONE);
            }
        } else {
            if (layoutSavedCards.getVisibility() == View.GONE) {
                layoutSavedCards.setVisibility(View.VISIBLE);
            }
            fadeInAnimation(layoutSavedCards);
            if (layoutCardForm.getVisibility() == View.VISIBLE) {
                layoutCardForm.setVisibility(View.GONE);
            }
        }
    }


    private void initSavedCards() {
        float cardHeight = cardWidth * CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, (int) cardHeight);
        savedCardPager.setLayoutParams(parms);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        setViewPagerValues();
    }

    private void setViewPagerValues() {
        cardPagerAdapter = new CardPagerAdapter(this);
        savedCardPager.setAdapter(cardPagerAdapter);
        savedCardPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        circlePageIndicator.setViewPager(savedCardPager);
        cardPagerAdapter.notifyDataSetChanged();
        if (creditCardList.isEmpty()) {
            backButton.setVisibility(GONE);
        }
    }

    private void calculateScreenWidth() {
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        cardWidth = outMetrics.widthPixels;
        cardWidth = cardWidth - ((2 * getResources().getDimension(R.dimen.sixteen_dp)) / density);
    }

    private void initPayButton() {
        payBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (widgetTransactionCallback != null) {
                    if (transactionRequest != null) {
                        pay(transactionRequest, widgetTransactionCallback);
                    } else {
                        throw new WidgetException(getResources().getString(R.string.error_transaction_request_missing));
                    }
                } else {
                    throw new WidgetException("Must provide transaction callback");
                }
            }
        });
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

            return SdkCoreFlowBuilder.init(getContext().getApplicationContext(), midtransClientKey, merchantUrl)
                    .enableLog(true)
                    .buildSDK();
        }
    }

    public void showHidePayButton(boolean show) {
        if (show) {
            payBtn.setVisibility(VISIBLE);
        } else {
            payBtn.setVisibility(GONE);
        }
        initPayButton();

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

    /*
     * check form validity when normal payment
     */
    public boolean checkCardValidity() {
        if (!enableTwoClick || (enableTwoClick && !iSTwoClickPayment)) {
            boolean cardNumberValidity = checkCardNumberValidity();
            boolean cardExpiryValidity = checkCardExpiryValidity();
            boolean cardCVVValidity = checkCardCVVValidity();
            return cardNumberValidity && cardExpiryValidity && cardCVVValidity;
        }
        return true;
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
            cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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

    public void pay(TransactionRequest transactionRequest, WidgetTransactionCallback widgetTransactionCallback) {
        if (enableTwoClick && iSTwoClickPayment) {
            SaveCardRequest cardDetailForm = cardPagerAdapter.getCurrentItem(savedCardPager);
            if (cardDetailForm != null && cardPagerAdapter.checkCardValidity()) {
                setTransactionRequest(transactionRequest);
                setWidgetTransactionCallback(widgetTransactionCallback);
                actionPaymentTwoClick(cardDetailForm, cardPagerAdapter.getCardCVV());
            } else {
                widgetTransactionCallback.onFailed(new WidgetException(getContext().getString(R.string.cvv_empty)));
            }
        } else {
            if (checkCardValidity()) {
                setTransactionRequest(transactionRequest);
                setWidgetTransactionCallback(widgetTransactionCallback);
                CardTokenRequest cardTokenRequest = new CardTokenRequest(
                        cardNumber.getText().toString().replace(" ", ""),
                        cardCvvNumber.getText().toString(),
                        cardExpiry.getText().toString().split("/")[0],
                        cardExpiry.getText().toString().split("/")[1],
                        midtransClientKey
                );
                cardTokenRequest.setIsSaved(checkboxStoreCard.isChecked());
                cardTokenRequest.setGrossAmount(transactionRequest.getAmount());
                cardTokenRequest.setSecure(transactionRequest.isSecureCard());
                this.cardTokenRequest = cardTokenRequest;
                getMidtransSDK().getCardToken(cardTokenRequest, new CardTokenCallback() {
                    @Override
                    public void onSuccess(TokenDetailsResponse response) {
                        actionGetCardTokenSuccess(response);
                    }

                    @Override
                    public void onFailure(TokenDetailsResponse response, String reason) {
                        actionGetCardTokenFailure(response, reason);
                    }

                    @Override
                    public void onError(Throwable error) {
                        actionGetCardTokenError(error);
                    }
                });
            }
        }

    }

    private void actionPaymentTwoClick(SaveCardRequest selectedCard, String cardCVV) {
        CardTokenRequest request = new CardTokenRequest();
        request.setSavedTokenId(selectedCard.getSavedTokenId());
        request.setCardCVV(cardCVV);
        request.setTwoClick(true);
        request.setGrossAmount(transactionRequest.getAmount());
        request.setSecure(transactionRequest.isSecureCard());
        request.setClientKey(this.midtransClientKey);
        midtransSDK.getCardToken(request, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse response) {
                actionGetCardTokenSuccess(response);
            }

            @Override
            public void onFailure(TokenDetailsResponse response, String reason) {
                actionGetCardTokenFailure(response, reason);
            }

            @Override
            public void onError(Throwable error) {
                actionGetCardTokenError(error);
            }
        });

    }

    private void actionGetCardTokenError(Throwable error) {
        if (widgetTransactionCallback != null) {
            widgetTransactionCallback.onFailed(new WidgetException(error.getMessage()));
        }
    }

    private void actionGetCardTokenFailure(TokenDetailsResponse response, String reason) {
        if (CreditCardForm.this.widgetTransactionCallback != null) {
            CreditCardForm.this.widgetTransactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_failed_get_token)));
        }
    }

    private void actionGetCardTokenSuccess(TokenDetailsResponse response) {
        if (!TextUtils.isEmpty(response.getRedirectUrl())) {
            start3DS(response.getTokenId(), response.getRedirectUrl());
        } else {
            charge(response.getTokenId(), CreditCardForm.this.transactionRequest);
        }
    }

    private void charge(String cardToken, TransactionRequest request) {
        this.transactionRequest = request;
        getMidtransSDK().setTransactionRequest(request);
        this.cardToken = cardToken;
        getMidtransSDK().checkout(new CheckoutCallback() {
            @Override
            public void onSuccess(Token token) {
                payUsingCreditCard(token);
            }

            @Override
            public void onFailure(Token token, String reason) {
                if (widgetTransactionCallback != null) {
                    widgetTransactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_failed_charge)));
                }
            }

            @Override
            public void onError(Throwable error) {
                if (widgetTransactionCallback != null) {
                    widgetTransactionCallback.onFailed(new WidgetException(error.getMessage()));
                }
            }
        });
    }

    private void payUsingCreditCard(Token token) {
        getMidtransSDK().paymentUsingCard(token.getTokenId(), CreditCardForm.this.cardToken,
                checkboxStoreCard.isChecked(), new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        if (widgetTransactionCallback != null) {
                            widgetTransactionCallback.onSucceed(response);
                        }
                        if (enableTwoClick && checkboxStoreCard.isChecked()) {
                            saveCards(response);
                        }
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        if (widgetTransactionCallback != null) {
                            widgetTransactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_failed_charge)));
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        if (widgetTransactionCallback != null) {
                            widgetTransactionCallback.onFailed(new WidgetException(error.getMessage()));
                        }
                    }
                });

    }

    private void saveCards(TransactionResponse response) {
        if (cardTokenRequest == null) {
            return;
        }
        initUserIdIfNotExist();
        String firstPart = cardTokenRequest.getCardNumber().substring(0, 6);
        String secondPart = cardTokenRequest.getCardNumber().substring(12);
        String maskedCard = firstPart + "-" + secondPart;
        SaveCardRequest newSaveCardRequest = new SaveCardRequest(response.getSavedTokenId(), maskedCard, cardTokenRequest.getCardType());

        ArrayList<SaveCardRequest> newCardRequests = new ArrayList<>();
        newCardRequests.add(newSaveCardRequest);
        if (this.creditCardList != null && !this.creditCardList.isEmpty()) {
            newCardRequests.addAll(this.creditCardList);
        }

        getMidtransSDK().saveCards(this.userId, newCardRequests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
            }

            @Override
            public void onFailure(String reason) {
            }

            @Override
            public void onError(Throwable error) {
            }
        });
    }

    public String getMidtransClientKey() {
        return midtransClientKey;
    }

    public void setMidtransClientKey(@NonNull String midtransClientKey) {
        this.midtransClientKey = midtransClientKey;
    }

    public String getMerchantUrl() {
        return merchantUrl;
    }

    public void setMerchantUrl(@NonNull String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }

    public void setWidgetTransactionCallback(WidgetTransactionCallback widgetTransactionCallback) {
        this.widgetTransactionCallback = widgetTransactionCallback;
    }

    public void setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    private void start3DS(final String tokenId, String redirectUrl) {
        webView = new WebView(getContext().getApplicationContext());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("/token/callback/")) {
                    webViewDialog.dismiss();
                    charge(tokenId, transactionRequest);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webViewDialog.dismiss();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDatabaseEnabled(false);
        webView.getSettings().setDomStorageEnabled(false);
        webView.getSettings().setGeolocationEnabled(false);
        webView.getSettings().setSaveFormData(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(redirectUrl);
        webViewDialog = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setView(webView)
                .create();

        webViewDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(webViewDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        webViewDialog.show();
        webViewDialog.getWindow().setAttributes(layoutParams);
    }

    /**
     * Will be used to enable two click payment type
     *
     * @param enableTwoClick
     */
    public void setEnableTwoClick(boolean enableTwoClick) {
        this.enableTwoClick = enableTwoClick;
        if (enableTwoClick) {
            initSavedCards();
            initTwoClickMode();
        } else {
            initNormalMode();
            backButton.setVisibility(GONE);
        }
    }

    private void showBackButton() {
        if (creditCardList != null && !creditCardList.isEmpty()) {
            backButton.setVisibility(VISIBLE);
        }
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @Override
    public void onDelete(SaveCardRequest card) {
        if(creditCardList != null && !creditCardList.isEmpty()){
            ArrayList<SaveCardRequest> newCards = getNewCards(card);
            layoutProgress.setVerticalGravity(View.VISIBLE);
            updateCardOnMerchantServer(newCards, card);
        }
    }

    @Override
    public void onAddNew() {
        showCreditCardLayout(true);
        layoutSavedCards.setVisibility(GONE);
        iSTwoClickPayment = false;
    }

    public void getCardList() {
        layoutProgress.setVisibility(VISIBLE);
        initUserIdIfNotExist();
        getMidtransSDK().getCards(this.userId, new GetCardCallback() {
            @Override
            public void onSuccess(ArrayList<SaveCardRequest> response) {
                showCreditCardLayout(false);

                if (response != null && !response.isEmpty()) {
                    creditCardList.clear();
                    creditCardList.addAll(response);
                    setCardsListToCardAdapter();
                    iSTwoClickPayment = true;
                }
                showBackButton();
                if (layoutProgress.getVisibility() == VISIBLE) {
                    layoutProgress.setVisibility(GONE);
                }
            }

            @Override
            public void onFailure(String reason) {
                showCreditCardLayout(true);
                if (layoutProgress.getVisibility() == VISIBLE) {
                    layoutProgress.setVisibility(GONE);
                }
            }

            @Override
            public void onError(Throwable error) {
                if (layoutProgress.getVisibility() == VISIBLE) {
                    layoutProgress.setVisibility(GONE);
                }
            }
        });
    }

    /*
     * Saved Cards Stuff
     */

    private void setCardsListToCardAdapter() {
        cardPagerAdapter.addViews(savedCardPager, this.creditCardList);
    }

    private void addView (SaveCardRequest card)
    {
        int pageIndex = cardPagerAdapter.addView (card);
        savedCardPager.setCurrentItem (pageIndex, true);
    }

    private void initUserIdIfNotExist(){
        if(TextUtils.isEmpty(this.userId)){
            this.userId = UUID.randomUUID().toString();
        }
    }

    //get new cards
    private ArrayList<SaveCardRequest> getNewCards(SaveCardRequest deletedItem) {
        ArrayList<SaveCardRequest> newCards = new ArrayList<>();
        newCards.addAll(creditCardList);
        SaveCardRequest newItemDelete = findCardByNumber(deletedItem.getMaskedCard());
        if(newItemDelete != null){
            newCards.remove(newItemDelete);
        }
        return newCards;
    }

    /*
     * Card Detail Callback
     */

    //update cards on server when a card deleted
    private void updateCardOnMerchantServer(ArrayList<SaveCardRequest> newCards, final SaveCardRequest card) {
        layoutProgress.setVisibility(VISIBLE);
        getMidtransSDK().saveCards(this.userId, newCards, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                if(cardRemoved(card)){
                    int pageIndex = cardPagerAdapter.removeView (savedCardPager, card);
                    cardPagerAdapter.notifyDataSetChanged();
                    if (pageIndex == cardPagerAdapter.getCount())
                        pageIndex--;
                    savedCardPager.setCurrentItem (pageIndex);
                }
                if(creditCardList.isEmpty()){
                    showCreditCardLayout(true);
                    backButton.setVisibility(GONE);
                }
                if(layoutProgress.getVisibility() == VISIBLE){
                    layoutProgress.setVisibility(GONE);
                }
                layoutProgress.setVisibility(GONE);
            }

            @Override
            public void onFailure(String reason) {
                if(layoutProgress.getVisibility() == VISIBLE){
                    layoutProgress.setVisibility(GONE);
                }
                Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error) {
                if(layoutProgress.getVisibility() == VISIBLE){
                    layoutProgress.setVisibility(GONE);
                }
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean cardRemoved(SaveCardRequest cardDetail) {
        for(SaveCardRequest request : creditCardList){
            if(request.getMaskedCard().equalsIgnoreCase(cardDetail.getMaskedCard())){
                creditCardList.remove(request);
                return true;
            }
        }
        return false;
    }

    private SaveCardRequest findCardByNumber(String maskedCard) {
        for(SaveCardRequest request : this.creditCardList){
            if(request.getMaskedCard().equalsIgnoreCase(maskedCard)){
                return request;
            }
        }
        return null;
    }

    private void fadeInAnimation(final RelativeLayout layout){
        layoutAnimation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.fade_in);

        layoutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setAnimation(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.startAnimation(layoutAnimation);
    }

    /**
     * Will be used to get response callback after charging in this view.
     */
    public interface WidgetTransactionCallback {
        void onSucceed(TransactionResponse transactionResponse);

        void onFailed(Throwable throwable);
    }
}
