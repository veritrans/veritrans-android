package id.co.veritrans.sdk.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.core.TransactionRequest;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.GetSnapTokenCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TokenBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.Events;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.utilities.Utils;
import id.co.veritrans.sdk.widgets.utils.CardUtils;
import id.co.veritrans.sdk.widgets.utils.WidgetException;

/**
 * Custom widget for showing credit card form.
 *
 * @author rakawm
 */
public class CreditCardForm extends NestedScrollView implements TokenBusCallback, TransactionBusCallback, GetSnapTokenCallback {
    private static final int MONTH_COUNT = 12;

    private ImageView logo;
    private Button payBtn, cvvHelpBtn;
    private EditText cardNumber, cardCvvNumber, cardExpiry;
    private TextInputLayout cardNumberContainer, cardCvvNumberContainer, cardExpiryContainer;

    private AlertDialog webViewDialog;
    private TokenCallback tokenCallback;
    private TransactionCallback transactionCallback;
    private String veritransClientKey;
    private String merchantUrl;
    private String cardToken;
    private TransactionRequest transactionRequest;
    private VeritransSDK veritransSDK;

    public CreditCardForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEventBus();
        init(context, attrs);
        initCardNumberEditText();
        initCardExpiryEditText();
        initCardCVVEditText();
    }

    private void initEventBus() {
        if (!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        // Inflate custom layout
        LayoutInflater.from(context).inflate(R.layout.credit_card_form, this, true);
        // Define layout components
        payBtn = (Button) findViewById(R.id.button_pay);
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

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardFormStyle);

        veritransClientKey = typedArray.getString(R.styleable.CreditCardFormStyle_vtcc_client_key);
        boolean isPayBtnShown = typedArray.getBoolean(R.styleable.CreditCardFormStyle_vtcc_show_pay, false);
        // Update charge button visibility
        if (isPayBtnShown) {
            showPayButton();
        } else {
            hidePayButton();
        }

        typedArray.recycle();
    }

    private void initPayButton() {
        payBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (transactionCallback != null) {
                    if (transactionRequest != null) {
                        pay(transactionRequest, transactionCallback);
                    } else {
                        throw new WidgetException(getResources().getString(R.string.error_transaction_request_missing));
                    }
                } else {
                    throw new WidgetException("Must provide transaction callback");
                }
            }
        });
    }

    private VeritransSDK getVeritransSDK() {
        if (veritransSDK != null) {
            return veritransSDK;
        } else {
            if (TextUtils.isEmpty(veritransClientKey)) {
                throw new WidgetException(getResources().getString(R.string.error_client_key_missing));
            }

            if (TextUtils.isEmpty(merchantUrl)) {
                throw new WidgetException(getResources().getString(R.string.error_merchant_url_missing));
            }

            return new SdkCoreFlowBuilder(getContext(), veritransClientKey, merchantUrl)
                    .enableLog(true)
                    .buildSDK();
        }
    }

    public void showPayButton() {
        payBtn.setVisibility(VISIBLE);
        initPayButton();
    }

    public void hidePayButton() {
        payBtn.setVisibility(GONE);
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

    public void pay(TransactionRequest transactionRequest, TransactionCallback transactionCallback) {
        if (checkCardValidity()) {
            setTransactionRequest(transactionRequest);
            setTransactionCallback(transactionCallback);
            CardTokenRequest cardTokenRequest = new CardTokenRequest(
                    cardNumber.getText().toString().replace(" ", ""),
                    cardCvvNumber.getText().toString(),
                    cardExpiry.getText().toString().split("/")[0],
                    cardExpiry.getText().toString().split("/")[1],
                    veritransClientKey
            );
            cardTokenRequest.setGrossAmount(transactionRequest.getAmount());
            cardTokenRequest.setSecure(transactionRequest.isSecureCard());
            getVeritransSDK().getToken(cardTokenRequest);
        }
    }

    private void charge(String cardToken, TransactionRequest request) {
        this.transactionRequest = request;
        getVeritransSDK().setTransactionRequest(request);
        this.cardToken = cardToken;
        getVeritransSDK().getSnapToken();
    }

    @Subscribe
    @Override
    public void onEvent(GetTokenSuccessEvent event) {
        if (tokenCallback != null) {
            tokenCallback.onSucceed(event.getResponse());
        }

        if (!TextUtils.isEmpty(event.getResponse().getRedirectUrl())) {
            start3DS(event.getResponse().getTokenId(), event.getResponse().getRedirectUrl());
        } else {
            charge(event.getResponse().getTokenId(), transactionRequest);
        }
    }

    @Subscribe
    @Override
    public void onEvent(GetTokenFailedEvent event) {
        if (tokenCallback != null) {
            tokenCallback.onFailed(new WidgetException(getResources().getString(R.string.error_failed_get_token)));
        }

        if (transactionCallback != null) {
            transactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_failed_get_token)));
        }
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        if (event.getSource().equals(Events.TOKENIZE)) {
            if (tokenCallback != null) {
                tokenCallback.onFailed(new WidgetException(getResources().getString(R.string.error_network)));
            }
        }

        if (event.getSource().equals(Events.SNAP_PAYMENT) || event.getSource().equals(Events.GET_SNAP_TOKEN)) {
            if (transactionCallback != null) {
                transactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_network)));
            }
        }

    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        if (event.getSource().equals(Events.TOKENIZE)) {
            if (tokenCallback != null) {
                tokenCallback.onFailed(new WidgetException(getResources().getString(R.string.error_unknown)));
            }
        }

        if (event.getSource().equals(Events.SNAP_PAYMENT) || event.getSource().equals(Events.GET_SNAP_TOKEN)) {
            if (transactionCallback != null) {
                transactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_unknown)));
            }
        }
    }

    public String getVeritransClientKey() {
        return veritransClientKey;
    }

    public void setVeritransClientKey(String veritransClientKey) {
        this.veritransClientKey = veritransClientKey;
    }

    public String getMerchantUrl() {
        return merchantUrl;
    }

    public void setMerchantUrl(String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }

    public void setTransactionCallback(TransactionCallback transactionCallback) {
        this.transactionCallback = transactionCallback;
    }

    public void setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent event) {
        if (transactionCallback != null) {
            transactionCallback.onSucceed(event.getResponse());
        }
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        if (transactionCallback != null) {
            transactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_failed_charge)));
        }
    }

    @Subscribe
    @Override
    public void onEvent(GetSnapTokenSuccessEvent event) {
        getVeritransSDK().snapPaymentUsingCard(event.getResponse().getTokenId(), cardToken, false);
    }

    @Subscribe
    @Override
    public void onEvent(GetSnapTokenFailedEvent event) {
        if (transactionCallback != null) {
            transactionCallback.onFailed(new WidgetException(getResources().getString(R.string.error_failed_charge)));
        }
    }

    private void start3DS(final String tokenId, String redirectUrl) {
        WebView webView = new WebView(getContext());
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
     * Will be used to get response callback after getting token in this view.
     */
    public interface TokenCallback {
        void onSucceed(TokenDetailsResponse tokenDetailsResponse);

        void onFailed(Throwable throwable);
    }

    /**
     * Will be used to get response callback after charging in this view.
     */
    public interface TransactionCallback {
        void onSucceed(TransactionResponse transactionResponse);

        void onFailed(Throwable throwable);
    }
}
