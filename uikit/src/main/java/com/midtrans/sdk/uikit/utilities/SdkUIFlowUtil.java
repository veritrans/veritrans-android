package com.midtrans.sdk.uikit.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.BBMCallBackUrl;
import com.midtrans.sdk.corekit.models.BBMUrlEncodeJson;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.MidtransLoadingDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It contains utility methods required for sdk.
 * <p/>
 * Created by chetan on 19/10/15.
 */
public class SdkUIFlowUtil {

    private static MidtransLoadingDialog progressDialog;

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
    public static void showSnackbar(Activity activity, String message) {

        try {
            Snackbar.make(activity.getWindow().findViewById(android.R.id.content), message,
                    Snackbar.LENGTH_LONG)
                    .show();
        } catch (NullPointerException e) {
            e.printStackTrace();
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
        Logger.i("isValid:" + isvalid);
        return isvalid;
    }

    /**
     * Displays an progress dialog.
     *
     * @param activity     instance of an activity
     * @param isCancelable set whether dialog is cancellable or not.
     */
    public static void showProgressDialog(Activity activity, boolean isCancelable) {

        hideProgressDialog();

        if (activity != null) {
            try {
                progressDialog = new MidtransLoadingDialog(activity);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setCancelable(isCancelable);
                progressDialog.show();
            } catch (WindowManager.BadTokenException ex) {
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
    public static void showProgressDialog(Activity activity, String message, boolean isCancelable) {

        hideProgressDialog();

        if (activity != null) {
            try {
                progressDialog = new MidtransLoadingDialog(activity, message);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setCancelable(isCancelable);
                progressDialog.show();
            } catch (WindowManager.BadTokenException ex) {
                Logger.e("error while creating progress dialog : " + ex.getMessage());
            }
        } else {
            Logger.e("error while creating progress dialog : Context cann't be null.");
        }

    }

    /**
     * @return an instance of progress dialog if visible any else returns null.
     */
    public static MidtransLoadingDialog getProgressDialog() {
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
                Logger.e("error while hiding progress dialog : " + ex.getMessage());
            }
            progressDialog = null;
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
                            SdkUIFlowUtil.showSnackbar(activity, activity.getString(R.string.no_network_msg));
                        } else {
                            SdkUIFlowUtil.showSnackbar(activity, errorMessage);
                        }
                    } else Logger.e("Context is not available.");
                } else {
                    Logger.e("Veritrans SDK is not started.");
                }

            } else {
                SdkUIFlowUtil.showSnackbar(activity, activity.getString(R.string.api_fail_message));
            }
        } catch (NullPointerException e) {
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

    /**
     * it returns the encoded url in string format.
     *
     * @param permataVA Permata virtual account
     * @return url in encoded format
     */
    public static String createEncodedUrl(String permataVA, String checkStatus, String
            beforePaymentError, String userCancel) {

        String encodedUrl = null;

        if (permataVA != null && checkStatus != null && beforePaymentError != null && userCancel
                != null) {

            BBMCallBackUrl bbmCallBackUrl = new BBMCallBackUrl(checkStatus, beforePaymentError,
                    userCancel);
            BBMUrlEncodeJson bbmUrlEncodeJson = new BBMUrlEncodeJson();

            bbmUrlEncodeJson.setReference(permataVA);

            bbmUrlEncodeJson.setCallbackUrl(bbmCallBackUrl);
            String jsonString = bbmUrlEncodeJson.getString();
            Logger.i("JSON String: " + jsonString);

            try {
                encodedUrl = URLEncoder.encode(jsonString, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return encodedUrl;
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

}