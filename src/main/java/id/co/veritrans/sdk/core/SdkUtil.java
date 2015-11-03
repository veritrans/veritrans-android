package id.co.veritrans.sdk.core;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.widgets.VeritransLoadingDialog;

/**
 * Created by chetan on 19/10/15.
 */
public class SdkUtil {

    private static VeritransLoadingDialog progressDialog;

    public static boolean isEmailValid(String email) {
        Logger.i("email:" + email);
        if ( !TextUtils.isEmpty(email) ) {
            Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email.trim());
            Logger.i("matcher:" + matcher.matches());
            return matcher.matches();
        } else {
            return false;
        }
    }

    public static boolean isPhoneNumberValid(String phoneNo) {
        if (!TextUtils.isEmpty(phoneNo)) {
            if (phoneNo.length() < Constants.PHONE_NUMBER_LENGTH) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static void showSnackbar(Activity activity, String message) {

        try {
            Snackbar.make(activity.getWindow().findViewById(android.R.id.content), message,
                    Snackbar.LENGTH_LONG)
                    .show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
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

    public static void showProgressDialog(Activity activity, boolean isCancelable) {

        hideProgressDialog();

        if (activity != null) {
            try {
                progressDialog = new VeritransLoadingDialog(activity);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setCancelable(isCancelable);
                progressDialog.show();
            } catch (WindowManager.BadTokenException ex) {
                Logger.e("error while creating progress dialog : " + ex.getMessage());
            }catch (NullPointerException ex){
                Logger.e("error while creating progress dialog : " + ex.getMessage());
            }
        } else {
            Logger.e("error while creating progress dialog : Context cann't be null.");
        }

    }

    public static VeritransLoadingDialog getProgressDialog() {
        return progressDialog;
    }

    public static void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {

            try {
                progressDialog.dismiss();
            } catch (IllegalArgumentException ex) {
                Logger.e("error while hiding progress dialog : " + ex.getMessage());
            } catch (NullPointerException ex) {
                Logger.e("error while hiding progress dialog : " + ex.getMessage());
            }
            progressDialog = null;
        }
    }


    public static void showApiFailedMessage(Activity activity,String errorMessage) {
        try {
            if (!TextUtils.isEmpty(errorMessage) && errorMessage.contains(Constants.RETROFIT_NETWORK_MESSAGE)) {
                SdkUtil.showSnackbar(activity, activity.getString(R.string.no_network_msg));
            } else {
                SdkUtil.showSnackbar(activity, activity.getString(R.string.api_fail_message));
            }
        } catch (NullPointerException e){
            Logger.i("Nullpointer:"+e.getMessage());
        }
    }

    /**
     * It will generate random 5 digit number. It is used as input3 in mandiri click pay.
     * @return
     */
    public static int generateRandomNumber(){
        int number = 0;
        int high = 99999;
        int low = 10000;

        Random random = new Random();
        number = random.nextInt(high-low) + low;
        return number;
    }

}