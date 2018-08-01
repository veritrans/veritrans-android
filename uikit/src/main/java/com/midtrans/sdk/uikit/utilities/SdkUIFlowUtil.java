package com.midtrans.sdk.uikit.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Currency;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.corekit.models.snap.PromoResponse;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.BankTransfer;
import com.midtrans.sdk.uikit.models.CreditCardType;
import com.midtrans.sdk.uikit.models.PromoData;
import com.midtrans.sdk.uikit.widgets.MidtransProgressDialogFragment;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It contains utility methods required for sdk.
 * <p/>
 * Created by chetan on 19/10/15.
 */
public class SdkUIFlowUtil {

    public static final String TYPE_PHONE = "PHONE";
    public static final String TYPE_TABLET = "TABLET";

    private static final String TAG = SdkUIFlowUtil.class.getSimpleName();
    private static MidtransProgressDialogFragment progressDialogFragment;
    private static int maskedExpDate;

    /**
     * it will validate an given email-id.
     *
     * @param email email string
     * @return true if given email-id is valid else returns false
     */
    public static boolean isEmailValid(String email) {

        try {
            if (!TextUtils.isEmpty(email)) {
                Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
                if (pattern != null) {
                    Matcher matcher = pattern.matcher(email.trim());
                    return matcher.matches();
                }
            }
        } catch (RuntimeException e) {
            Logger.e(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * it will validate an given phone number.
     *
     * @param phoneNo phone number string
     * @return true if given phone number is valid else returns false
     */
    public static boolean isPhoneNumberValid(String phoneNo) {
        if (!TextUtils.isEmpty(phoneNo)) {
            return phoneNo.length() >= Constants.PHONE_NUMBER_LENGTH || phoneNo.length() <= Constants.PHONE_NUMBER_MAX_LENGTH;
        } else {
            return false;
        }
    }

    /**
     * Show snack bar with given message.
     *
     * @param activity instance of an activity.
     * @param message  message to display on snackbar.
     */
    public static void showToast(Activity activity, String message) {
        try {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

        } catch (RuntimeException e) {
            Logger.e("showToast", "message:" + e.getMessage());
        }
    }


    /**
     * Utility method which will help to close the keyboard.
     *
     * @param activity activity instance
     */
    public static void hideKeyboard(Activity activity) {
        try {
            Logger.i("hide keyboard");
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (RuntimeException e) {
            Logger.d(TAG, "hideKeyboard():" + e.getMessage());
        }
    }

    /**
     * it will validate an given card number.
     *
     * @param ccNumber credit card number
     * @return true if given card number is valid else returns false.
     */
    public static boolean isValidCardNumber(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        boolean isvalid = (sum % 10 == 0);
        Logger.i("isValid:" + isvalid);
        return isvalid;
    }

    /**
     * Displays an progress dialog.
     *
     * @param activity     instance of an activity
     * @param isCancelable set whether dialog is cancellable or not.
     */
    public static void showProgressDialog(AppCompatActivity activity, boolean isCancelable) {
        hideProgressDialog();
        if (activity != null) {
            try {
                progressDialogFragment = MidtransProgressDialogFragment.newInstance();
                progressDialogFragment.setCancelable(isCancelable);
                progressDialogFragment.show(activity.getSupportFragmentManager(), "");
            } catch (Exception ex) {
                Logger.e("error while creating progress dialog : " + ex.getMessage());
            }
        } else {
            Logger.e("error while creating progress dialog : Context cann't be null.");
        }

    }


    /**
     * Displays an progress dialog with an message.
     *
     * @param activity     instance of an activity
     * @param message      message to display information about on going task.
     * @param isCancelable set whether dialog is cancellable or not.
     */
    public static void showProgressDialog(AppCompatActivity activity, String message, boolean isCancelable) {

        hideProgressDialog();

        if (activity != null) {
            try {
                progressDialogFragment = MidtransProgressDialogFragment.newInstance(message);
                progressDialogFragment.setCancelable(isCancelable);
                progressDialogFragment.show(activity.getSupportFragmentManager(), "");
            } catch (Exception ex) {
                Logger.e("error while creating progress dialog : " + ex.getMessage());
            }
        } else {
            Logger.e("error while creating progress dialog : Context cann't be null.");
        }

    }

    /**
     * It will close the progress dialog if visible any.
     */
    public static void hideProgressDialog() {

        if (progressDialogFragment != null) {
            try {
                progressDialogFragment.dismiss();
            } catch (RuntimeException ex) {
                Logger.e("error while hiding progress dialog : " + ex.getMessage());
            }
            progressDialogFragment = null;
        }
    }

    /**
     * display snackbar with message about failed api call.
     *
     * @param activity     activity instance
     * @param errorMessage error message
     */
    public static void showApiFailedMessage(Activity activity, String errorMessage) {
        try {
            if (!TextUtils.isEmpty(errorMessage)) {
                MidtransSDK midtransSDK = MidtransSDK.getInstance();
                if (midtransSDK != null) {
                    Context context = midtransSDK.getContext();
                    if (context != null) {
                        if (errorMessage.contains(context.getString(R.string.retrofit_network_message))) {
                            Toast.makeText(activity, activity.getString(R.string.no_network_msg), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } else Logger.e("Context is not available.");
                } else {
                    Logger.e("Veritrans SDK is not started.");
                }

            } else {
                Toast.makeText(activity, activity.getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
            }
        } catch (RuntimeException e) {
            Logger.i("Nullpointer:" + e.getMessage());
        }
    }

    /**
     * It will generate random 5 digit number. It is used as input3 in mandiri click pay.
     *
     * @return random number
     */
    public static int generateRandomNumber() {
        int number;
        int high = 99999;
        int low = 10000;
        byte[] randomBytes = new byte[128];

        try {
            SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            secureRandomGenerator.nextBytes(randomBytes);
            number = secureRandomGenerator.nextInt(high - low) + low;
        } catch (NoSuchAlgorithmException e) {
            Logger.e("random number : " + e.getMessage());
            Random random = new Random();
            number = random.nextInt(high - low) + low;
        }
        return number;
    }

    /**
     * shows keyboard on screen forcefully.
     *
     * @param activity activity instance
     * @param editText edittext instance
     */
    public static void showKeyboard(Activity activity, EditText editText) {
        Logger.i("show keyboard");
        if (editText != null) {
            editText.requestFocus();
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

    }


    public static boolean isBBMMoneyInstalled(Activity activity) {

        boolean isInstalled = false;

        if (activity != null) {

            PackageManager pm = activity.getPackageManager();
            try {
                pm.getPackageInfo(BuildConfig.BBM_MONEY_PACKAGE, PackageManager.GET_ACTIVITIES);
                isInstalled = true;
            } catch (PackageManager.NameNotFoundException e) {
                isInstalled = false;
            }
        }

        return isInstalled;
    }

    public static int fetchAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public static int fetchPrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public static int fetchPrimaryDarkColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimaryDark});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }


    public static ArrayList<BankBinsResponse> getBankBins(Context context) {
        ArrayList<BankBinsResponse> list = null;
        String data;
        try {
            InputStream is = context.getAssets().open("bank_bins.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            data = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            list = gson.fromJson(data, new TypeToken<ArrayList<BankBinsResponse>>() {
            }.getType());

        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }

        return list;
    }

    public static String getMaskedCardNumber(String maskedCard) {
        StringBuilder builder = new StringBuilder();
        String bulletMask = "●●●●●●";
        String newMaskedCard = maskedCard.replace("-", bulletMask);

        for (int i = 0; i < newMaskedCard.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                builder.append(' ');
                builder.append(newMaskedCard.charAt(i));
            } else {
                builder.append(newMaskedCard.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String getMaskedExpDate() {
        String bulletMask = "●●";
        String maskedDate = bulletMask + " / " + bulletMask;
        return maskedDate;
    }

    public static String getMaskedCardCvv() {
        String bulletMask = "●●●";

        return bulletMask;
    }

    public static Drawable filterDrawableImage(Context context, int image, int filterColor) {
        Resources res = context.getResources();
        Drawable background = res.getDrawable(image);
        background.setColorFilter(filterColor, PorterDuff.Mode.SRC_IN);
        return background;
    }

    /**
     * Sorting bank payment method by priority (Ascending)
     * <p>
     * this method is deprecated, use {@link SdkUIFlowUtil#sortBankTransferMethodsByPriority(List)} instead
     */
    @Deprecated
    public static void sortBankPaymentMethodsByPriority(List<BankTransferModel> paymentMethodsModels) {
        if (paymentMethodsModels != null) {
            Collections.sort(paymentMethodsModels, new Comparator<BankTransferModel>() {
                @Override
                public int compare(BankTransferModel lhs, BankTransferModel rhs) {
                    return lhs.getPriority().compareTo(rhs.getPriority());
                }
            });
        }
    }

    /**
     * Sorting bank payment method by priority (Ascending)
     */
    public static void sortBankTransferMethodsByPriority(List<BankTransfer> paymentMethodsModels) {
        if (paymentMethodsModels != null) {
            Collections.sort(paymentMethodsModels, new Comparator<BankTransfer>() {
                @Override
                public int compare(BankTransfer lhs, BankTransfer rhs) {
                    return lhs.getPriority().compareTo(rhs.getPriority());
                }
            });
        }
    }

    /**
     * Check if payment method is enabled.
     *
     * @param enabledPayments list of enabled payment method.
     * @param method          selected payment.
     * @return true if selected payment is enabled.
     */
    public static boolean isPaymentMethodEnabled(List<EnabledPayment> enabledPayments, String method) {
        if (enabledPayments != null) {
            for (EnabledPayment enabledPayment : enabledPayments) {
                if (enabledPayment.getType().equalsIgnoreCase(method)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if bank transfer payment method is enabled.
     *
     * @param context         application or activity context.
     * @param enabledPayments list of enabled payment method.
     * @return true if bank transfer payment is enabled.
     */
    public static boolean isBankTransferMethodEnabled(Context context, List<EnabledPayment> enabledPayments) {
        if (enabledPayments != null) {
            for (EnabledPayment enabledPayment : enabledPayments) {
                if (enabledPayment.getType().equalsIgnoreCase(PaymentType.BCA_VA)
                        || enabledPayment.getType().equalsIgnoreCase(PaymentType.PERMATA_VA)
                        || enabledPayment.getType().equalsIgnoreCase(PaymentType.E_CHANNEL)
                        || enabledPayment.getType().equalsIgnoreCase(PaymentType.ALL_VA)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static String getDeviceType(Activity activity) {
        String deviceType;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        if (diagonalInches >= 6.5) {
            deviceType = TYPE_TABLET;
        } else {
            deviceType = TYPE_PHONE;
        }

        return deviceType;
    }

    public static PromoResponse getPromoFromCardBins(List<PromoResponse> promoResponses, String cardBins) {
        List<PromoData> promoDatas = mapPromoResponseIntoData(promoResponses);
        Collections.sort(promoDatas, new PromoComparator());
        for (PromoData promoData : promoDatas) {
            if (isBinCardValidForPromo(promoData.getPromoResponse(), cardBins)) {
                return promoData.getPromoResponse();
            }
        }
        return null;
    }

    private static boolean isBinCardValidForPromo(PromoResponse promo, String cardBin) {
        List<String> bins = promo.getBins();
        if (bins != null && !bins.isEmpty()) {
            if (bins.contains(cardBin)) {
                return true;
            }
        }
        return false;
    }

    private static List<PromoData> mapPromoResponseIntoData(List<PromoResponse> promoResponses) {
        List<PromoData> promoDatas = new ArrayList<>();
        double grossAmount = MidtransSDK.getInstance().getTransactionRequest().getAmount();
        for (PromoResponse promoResponse : promoResponses) {
            if (grossAmount > calculateDiscountAmount(promoResponse)) {
                promoDatas.add(new PromoData(promoResponse, grossAmount));
            }
        }
        return promoDatas;
    }

    public static double calculateDiscountAmount(PromoResponse promoResponse) {
        if (promoResponse.getDiscountType().equalsIgnoreCase("FIXED")) {
            return promoResponse.getDiscountAmount();
        } else {
            return promoResponse.getDiscountAmount() * MidtransSDK.getInstance().getTransactionRequest().getAmount() / 100;
        }
    }

    public static List<SavedToken> removeCardFromSavedCards(List<SavedToken> savedTokens, String maskedCard) {
        List<SavedToken> updatedTokens = new ArrayList<>();
        if (savedTokens != null) {
            for (SavedToken savedToken : savedTokens) {
                if (!savedToken.getMaskedCard().equals(maskedCard)) {
                    updatedTokens.add(savedToken);
                }
            }
        }
        return updatedTokens;
    }

    public static ArrayList<SaveCardRequest> convertSavedToken(List<SavedToken> savedTokens) {
        ArrayList<SaveCardRequest> cards = new ArrayList<>();
        if (savedTokens != null && !savedTokens.isEmpty()) {
            for (SavedToken saved : savedTokens) {
                cards.add(new SaveCardRequest(saved.getToken(), saved.getMaskedCard(), saved.getTokenType()));
            }
        }
        return cards;
    }

    public static List<SaveCardRequest> filterCardsByClickType(Context context, List<SaveCardRequest> cards) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        ArrayList<SaveCardRequest> filteredCards = new ArrayList<>();
        if (cards != null && !cards.isEmpty()) {
            if (midtransSDK.isEnableBuiltInTokenStorage()) {
                for (SaveCardRequest card : cards) {
                    if (midtransSDK.getTransactionRequest().getCardClickType().equals(context.getString(R.string.card_click_type_one_click))
                            && card.getType().equals(context.getString(R.string.saved_card_one_click))) {
                        filteredCards.add(card);
                    } else if (midtransSDK.getTransactionRequest().getCardClickType().equals(context.getString(R.string.card_click_type_two_click))
                            && card.getType().equals(context.getString(R.string.saved_card_two_click))) {
                        filteredCards.add(card);
                    }
                }
            } else {
                //if token storage on merchant server then saved cards can be used just for two click
                String clickType = midtransSDK.getTransactionRequest().getCardClickType();
                if (!TextUtils.isEmpty(clickType) && clickType.equals(context.getString(R.string.card_click_type_two_click))) {
                    filteredCards.addAll(cards);
                }
            }
        }

        return filteredCards;
    }

    public static List<SaveCardRequest> filterMultipleSavedCard(ArrayList<SaveCardRequest> savedCards) {
        if (savedCards != null) {
            Collections.reverse(savedCards);
            Set<String> maskedCardSet = new HashSet<>();
            for (Iterator<SaveCardRequest> it = savedCards.iterator(); it.hasNext(); ) {
                if (!maskedCardSet.add(it.next().getMaskedCard())) {
                    it.remove();
                }
            }
        }
        return savedCards;
    }

    @ColorInt
    public static int getThemeColor(@NonNull final Context context, @AttrRes final int attributeColor) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attributeColor, value, true);
        return value.data;
    }

    public static int getCreditCardIconType() {
        if (MidtransSDK.getInstance().getMerchantData() != null) {
            List<String> principles = MidtransSDK.getInstance().getMerchantData().getEnabledPrinciples();
            if (principles != null && principles.contains(CreditCardType.MASTERCARD) && principles.contains(CreditCardType.VISA)) {
                if (principles.contains(CreditCardType.JCB)) {
                    if (principles.contains(CreditCardType.AMEX)) {
                        return CreditCardType.TYPE_MASTER_VISA_JCB_AMEX;
                    }
                    return CreditCardType.TYPE_MASTER_VISA_JCB;
                } else if (principles.contains(CreditCardType.AMEX)) {
                    return CreditCardType.TYPE_MASTER_VISA_AMEX;
                }
                return CreditCardType.TYPE_MASTER_VISA;
            }
        }

        return CreditCardType.TYPE_UNKNOWN;
    }

    public static List<SaveCardRequest> filterMultipleSavedCard(List<SaveCardRequest> savedCards) {
        if (savedCards != null) {
            Collections.reverse(savedCards);
            Set<String> maskedCardSet = new HashSet<>();
            for (Iterator<SaveCardRequest> it = savedCards.iterator(); it.hasNext(); ) {
                if (!maskedCardSet.add(it.next().getMaskedCard())) {
                    it.remove();
                }
            }
        }
        return savedCards;
    }

    public static List<SaveCardRequest> convertSavedTokens(List<SavedToken> savedTokens) {
        List<SaveCardRequest> cards = new ArrayList<>();
        if (savedTokens != null && !savedTokens.isEmpty()) {
            for (SavedToken saved : savedTokens) {
                cards.add(new SaveCardRequest(saved.getToken(), saved.getMaskedCard(), saved.getTokenType()));
            }
        }
        return cards;
    }

    public static List<SavedToken> convertSavedCards(List<SaveCardRequest> savedCards) {
        List<SavedToken> cards = new ArrayList<>();
        if (savedCards != null && !savedCards.isEmpty()) {
            for (SaveCardRequest saved : savedCards) {
                SavedToken savedToken = new SavedToken();
                savedToken.setTokenType(saved.getType());
                savedToken.setMaskedCard(saved.getMaskedCard());
                savedToken.setToken(saved.getSavedTokenId());
                cards.add(savedToken);
            }
        }
        return cards;
    }


    public static void startWebIntent(Activity activity, String instructionUrl) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(instructionUrl));
        activity.startActivity(webIntent);
    }

    public static UserDetail getSavedUserDetails() throws RuntimeException {
        return LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
    }

    public static void saveUserDetails() throws RuntimeException {
        UserDetail userDetail = LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
        saveUserDetails(userDetail);
    }

    public static void saveUserDetails(UserDetail userDetail) throws RuntimeException {
        if (userDetail == null) {
            userDetail = new UserDetail();
        }

        if (TextUtils.isEmpty(userDetail.getUserId())) {
            userDetail.setUserId(UUID.randomUUID().toString());
        }

        LocalDataHandler.saveObject(UiKitConstants.KEY_USER_DETAILS, userDetail);
    }

    public static String getImagePath(Activity activity) {
        return "android.resource://" + activity.getPackageName() + "/";
    }

    public static String getFormattedAmount(Context context, double amount, String currency) {
        String formattedAmount = context.getString(R.string.prefix_money, Utils.getFormattedAmount(amount));
        if (!TextUtils.isEmpty(currency)) {
            switch (currency) {
                case Currency.SGD:
                    formattedAmount = context.getString(R.string.prefix_money_sgd, Utils.getFormattedAmount(amount));
                    break;

                case Currency.IDR:
                    formattedAmount = context.getString(R.string.prefix_money, Utils.getFormattedAmount(amount));
                    break;
            }
        }

        return formattedAmount;
    }

    public static String getFormattedNegativeAmount(Context context, double amount, String currency) {
        String formattedAmount = context.getString(R.string.prefix_money_negative, Utils.getFormattedAmount(amount));
        if (!TextUtils.isEmpty(currency)) {
            switch (currency) {
                case Currency.SGD:
                    formattedAmount = context.getString(R.string.prefix_money_negative_sgd, Utils.getFormattedAmount(amount));
                    break;

                case Currency.IDR:
                    formattedAmount = context.getString(R.string.prefix_money_negative, Utils.getFormattedAmount(amount));
                    break;
            }
        }

        return formattedAmount;
    }
}
