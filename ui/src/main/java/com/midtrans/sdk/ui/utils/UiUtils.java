package com.midtrans.sdk.ui.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.midtrans.sdk.core.models.CreditCardType;
import com.midtrans.sdk.core.models.snap.CustomerDetails;
import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bins.BankBinsResponse;
import com.midtrans.sdk.core.models.snap.transaction.SnapEnabledPayment;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.constants.Constants;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It contains utility methods required for sdk.
 * <p/>
 * Created by chetan on 19/10/15.
 */
public class UiUtils {

    private static final String TAG = UiUtils.class.getSimpleName();
    private static final String LABEL_CLIPBOARD = "clipboard";
    private static ProgressDialog progressDialog;
    private static int maskedExpDate;

    /**
     * it will validate an given email-id.
     *
     * @param email email string
     * @return true if given email-id is valid else returns false
     */
    public static boolean isEmailValid(String email) {

        if (!TextUtils.isEmpty(email)) {
            Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email.trim());
            return matcher.matches();
        } else {
            return false;
        }
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
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * Utility method which will help to close the keyboard.
     *
     * @param activity activity instance
     */
    public static void hideKeyboard(EditText editText, Activity activity) {
        try {
            Logger.d(TAG, "hide keyboard");
            if (editText != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
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
        Logger.d(TAG, "isValid:" + isvalid);
        return isvalid;
    }

    /**
     * Displays an progress dialog.
     *
     * @param context      instance of an Context
     * @param isCancelable set whether dialog is cancellable or not.
     */
    public static void showProgressDialog(Context context, boolean isCancelable) {

        hideProgressDialog();

        if (context != null) {
            try {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage(context.getString(R.string.loading));
                progressDialog.setCancelable(isCancelable);
                progressDialog.show();
            } catch (WindowManager.BadTokenException ex) {
                Logger.e(TAG, "error while creating progress dialog : " + ex.getMessage());
            }
        } else {
            Logger.e(TAG, "error while creating progress dialog : Context cann't be null.");
        }

    }


    /**
     * Displays an progress dialog with an message.
     *
     * @param activity     instance of an activity
     * @param message      message to display information about on going task.
     * @param isCancelable set whether dialog is cancellable or not.
     */
    public static void showProgressDialog(Activity activity, String message, boolean isCancelable) {

        hideProgressDialog();

        if (activity != null) {
            try {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage(message);
                progressDialog.setCancelable(isCancelable);
                progressDialog.show();
            } catch (WindowManager.BadTokenException ex) {
                Logger.e(TAG, "error while creating progress dialog : " + ex.getMessage());
            }
        } else {
            Logger.e(TAG, "error while creating progress dialog : Context cann't be null.");
        }

    }

    /**
     * @return an instance of progress dialog if visible any else returns null.
     */
    public static ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    /**
     * It will close the progress dialog if visible any.
     */
    public static void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {

            try {
                progressDialog.dismiss();
            } catch (IllegalArgumentException ex) {
                Logger.e(TAG, "error while hiding progress dialog : " + ex.getMessage());
            }
            progressDialog = null;
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
            Logger.e(TAG, "random number : " + e.getMessage());
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
        Logger.d(TAG, "show keyboard");
        if (editText != null) {
            editText.requestFocus();
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

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
            e.printStackTrace();
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
     * Check if payment method is enabled.
     *
     * @param enabledPayments list of enabled payment method.
     * @param method          selected payment.
     * @return true if selected payment is enabled.
     */
    public static boolean isPaymentMethodEnabled(List<SnapEnabledPayment> enabledPayments, String method) {
        for (SnapEnabledPayment enabledPayment : enabledPayments) {
            if (enabledPayment.type.equalsIgnoreCase(method)) {
                return true;
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
            deviceType = "TABLET";
        } else {
            deviceType = "PHONE";
        }

        return deviceType;
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
                if (errorMessage.contains(activity.getString(R.string.retrofit_network_message))) {
                    Toast.makeText(activity, activity.getString(R.string.no_network_msg), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, activity.getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Logger.e(TAG, "Nullpointer:" + e.getMessage());
        }
    }

    public static int getCreditCardIconType() {
        List<String> principles = MidtransUi.getInstance().getTransaction().merchant.enabledPrinciples;
        if (principles.contains(CreditCardType.MASTERCARD) && principles.contains(CreditCardType.VISA)) {
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
        return CreditCardType.TYPE_UNKNOWN;
    }

    public static SnapCustomerDetails buildCustomerDetails() {
        if (MidtransUi.getInstance().getTransaction() != null) {
            CustomerDetails customerDetails = MidtransUi.getInstance().getTransaction().customerDetails;
            return new SnapCustomerDetails(
                    customerDetails.firstName,
                    customerDetails.email,
                    customerDetails.phone
            );
        }
        return null;
    }

    public static void removeCardFromMidtransTransaction(SavedToken savedToken) {
        List<SavedToken> savedTokens = MidtransUi.getInstance().getTransaction().creditCard.savedTokens;
        int position = -1;
        for (SavedToken token : savedTokens) {
            if (token.maskedCard.equals(savedToken.maskedCard)) {
                position = savedTokens.indexOf(token);
            }
        }

        if (position != -1) {
            savedTokens.remove(position);
        }
    }

    public static void copyTextIntoClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_CLIPBOARD, text);
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    public static void startWebIntent(Activity activity, String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        activity.startActivity(webIntent);
    }
}
