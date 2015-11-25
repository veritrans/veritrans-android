package id.co.veritrans.sdk.core;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.models.BankTransfer;
import id.co.veritrans.sdk.models.BillingAddress;
import id.co.veritrans.sdk.models.CardPaymentDetails;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.CustomerDetails;
import id.co.veritrans.sdk.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.models.MandiriClickPayModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.ShippingAddress;
import id.co.veritrans.sdk.models.TransactionDetails;
import id.co.veritrans.sdk.models.UserAddress;
import id.co.veritrans.sdk.models.UserDetail;
import id.co.veritrans.sdk.widgets.VeritransLoadingDialog;

/**
 * Created by chetan on 19/10/15.
 */
public class SdkUtil {

    private static VeritransLoadingDialog progressDialog;

    public static boolean isEmailValid(String email) {
        Logger.i("email:" + email);
        if (!TextUtils.isEmpty(email)) {
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
            } catch (NullPointerException ex) {
                Logger.e("error while creating progress dialog : " + ex.getMessage());
            }
        } else {
            Logger.e("error while creating progress dialog : Context cann't be null.");
        }

    }

    public static void showProgressDialog(Activity activity, String message, boolean isCancelable) {

        hideProgressDialog();

        if (activity != null) {
            try {
                progressDialog = new VeritransLoadingDialog(activity, message);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setCancelable(isCancelable);
                progressDialog.show();
            } catch (WindowManager.BadTokenException ex) {
                Logger.e("error while creating progress dialog : " + ex.getMessage());
            } catch (NullPointerException ex) {
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

    public static void showApiFailedMessage(Activity activity, String errorMessage) {
        try {
            if (!TextUtils.isEmpty(errorMessage) && errorMessage.contains(Constants
                    .RETROFIT_NETWORK_MESSAGE)) {
                SdkUtil.showSnackbar(activity, activity.getString(R.string.no_network_msg));
            } else {
                SdkUtil.showSnackbar(activity, activity.getString(R.string.api_fail_message));
            }
        } catch (NullPointerException e) {
            Logger.i("Nullpointer:" + e.getMessage());
        }
    }

    /**
     * It will generate random 5 digit number. It is used as input3 in mandiri click pay.
     *
     * @return
     */
    public static int generateRandomNumber() {
        int number = 0;
        int high = 99999;
        int low = 10000;

        Random random = new Random();
        number = random.nextInt(high - low) + low;
        return number;
    }

    protected static MandiriBillPayTransferModel getMandiriBillPayModel(TransactionRequest
                                                                                request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        MandiriBillPayTransferModel model =
                new MandiriBillPayTransferModel(request.getBillInfoModel(),
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(), request.getCustomerDetails());
        return model;
    }

    protected static MandiriClickPayRequestModel getMandiriClickPayRequestModel(TransactionRequest
                                                                                        request,
                                                                                MandiriClickPayModel mandiriClickPayModel) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        MandiriClickPayRequestModel model =
                new MandiriClickPayRequestModel(mandiriClickPayModel,
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(), request.getCustomerDetails());
        return model;
    }

    protected static PermataBankTransfer getPermataBankModel(TransactionRequest request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        // bank name
        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setBank("permata");

        PermataBankTransfer model =
                new PermataBankTransfer(bankTransfer,
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;

    }

    public static CardTransfer getCardTransferModel(TransactionRequest request,
                                                    CardPaymentDetails cardPaymentDetails) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        CardTransfer model =
                new CardTransfer(cardPaymentDetails,
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;
    }

    protected static TransactionRequest initializeUserInfo(TransactionRequest transactionRequest) {

        transactionRequest = getUserDetails(transactionRequest);

        return transactionRequest;
    }

    private static TransactionRequest getUserDetails(TransactionRequest request) {

        UserDetail userDetail = null;
        CustomerDetails mCustomerDetails = null;

        try {
            userDetail = (UserDetail) StorageDataHandler.readObject(request.getActivity()
                            .getApplicationContext(),
                    Constants.USER_DETAILS);

            if (userDetail != null && !TextUtils.isEmpty(userDetail.getUserFullName())) {

                ArrayList<UserAddress> userAddresses = userDetail.getUserAddresses();
                if (userAddresses != null && !userAddresses.isEmpty()) {

                    Logger.i("Found " + userAddresses.size() + " user addresses.");

                    mCustomerDetails = new CustomerDetails();
                    mCustomerDetails.setPhone(userDetail.getPhoneNumber());
                    mCustomerDetails.setFirstName(userDetail.getUserFullName());
                    mCustomerDetails.setLastName(" ");
                    mCustomerDetails.setEmail("");

                    //added email in performTransaction()
                    request.setCustomerDetails(mCustomerDetails);

                    request = extractUserAddress(userDetail, userAddresses, request);
                }

            } else {
                SdkUtil.showSnackbar(request.getActivity(), "User details not available.");
                request.getActivity().finish();
            }
        } catch (ClassNotFoundException ex) {
            Logger.e("Error while fetching user details : " + ex.getMessage());
        } catch (IOException ex) {
            Logger.e("Error while fetching user details : " + ex.getMessage());
        }

        return request;
    }

    private static TransactionRequest extractUserAddress(UserDetail userDetail,
                                                         ArrayList<UserAddress> userAddresses,
                                                         TransactionRequest request) {

        ArrayList<BillingAddress> billingAddressArrayList = new ArrayList<>();
        ArrayList<ShippingAddress> shippingAddressArrayList = new ArrayList<>();

        for (int i = 0; i < userAddresses.size(); i++) {

            UserAddress userAddress = userAddresses.get(i);

            if (userAddress.getAddressType() == Constants.ADDRESS_TYPE_BOTH) {

                BillingAddress billingAddress = getBillingAddress(userDetail, userAddress);
                billingAddressArrayList.add(billingAddress);
                ShippingAddress shippingAddress = getShippingAddress(userDetail, userAddress);
                shippingAddressArrayList.add(shippingAddress);

            } else if (userAddress.getAddressType() == Constants.ADDRESS_TYPE_SHIPPING) {
                ShippingAddress shippingAddress = getShippingAddress(userDetail, userAddress);
                shippingAddressArrayList.add(shippingAddress);
            } else {
                BillingAddress billingAddress = getBillingAddress(userDetail, userAddress);
                billingAddressArrayList.add(billingAddress);
            }

        }

        request.setBillingAddressArrayList(billingAddressArrayList);
        request.setShippingAddressArrayList(shippingAddressArrayList);

        return request;
    }

    @NonNull
    private static ShippingAddress getShippingAddress(UserDetail userDetail, UserAddress
            userAddress) {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setCity(userAddress.getCity());
        shippingAddress.setFirstName(userDetail.getUserFullName());
        shippingAddress.setLastName("");
        shippingAddress.setPhone(userDetail.getPhoneNumber());
        shippingAddress.setCountryCode(userAddress.getCountry());
        shippingAddress.setPostalCode(userAddress.getZipcode());
        return shippingAddress;
    }

    @NonNull
    private static BillingAddress getBillingAddress(UserDetail userDetail, UserAddress
            userAddress) {
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setCity(userAddress.getCity());
        billingAddress.setFirstName(userDetail.getUserFullName());
        billingAddress.setLastName("");
        billingAddress.setPhone(userDetail.getPhoneNumber());
        billingAddress.setCountryCode(userAddress.getCountry());
        billingAddress.setPostalCode(userAddress.getZipcode());
        return billingAddress;
    }

    public static void showKeyboard(Activity activity, EditText editText) {
        Logger.i("show keyboard");
        if(editText!=null) {
            editText.requestFocus();
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

    }

   /* public static BankDetailArray loadBankDetailJsonFromAsset(final Activity activity) {




    }*/
}