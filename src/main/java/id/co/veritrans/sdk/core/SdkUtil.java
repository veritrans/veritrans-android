package id.co.veritrans.sdk.core;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.co.veritrans.sdk.BuildConfig;
import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.models.BBMCallBackUrl;
import id.co.veritrans.sdk.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.models.BBMUrlEncodeJson;
import id.co.veritrans.sdk.models.BCABankTransfer;
import id.co.veritrans.sdk.models.BCAKlikPayDescriptionModel;
import id.co.veritrans.sdk.models.BCAKlikPayModel;
import id.co.veritrans.sdk.models.BankTransfer;
import id.co.veritrans.sdk.models.BillingAddress;
import id.co.veritrans.sdk.models.CIMBClickPayModel;
import id.co.veritrans.sdk.models.CardPaymentDetails;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.CstoreEntity;
import id.co.veritrans.sdk.models.CustomerDetails;
import id.co.veritrans.sdk.models.DescriptionModel;
import id.co.veritrans.sdk.models.EpayBriTransfer;
import id.co.veritrans.sdk.models.IndomaretRequestModel;
import id.co.veritrans.sdk.models.IndosatDompetku;
import id.co.veritrans.sdk.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.models.MandiriClickPayModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.MandiriECashModel;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.ShippingAddress;
import id.co.veritrans.sdk.models.TransactionDetails;
import id.co.veritrans.sdk.models.UserAddress;
import id.co.veritrans.sdk.models.UserDetail;
import id.co.veritrans.sdk.widgets.VeritransLoadingDialog;

/**
 * It contains utility methods required for sdk.
 * <p/>
 * Created by chetan on 19/10/15.
 */
public class SdkUtil {

    private static VeritransLoadingDialog progressDialog;

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
     * @param phoneNo   phone number string
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
     * @param activity  activity instance
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
     * @param ccNumber  credit card number
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
                progressDialog = new VeritransLoadingDialog(activity);
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
                progressDialog = new VeritransLoadingDialog(activity, message);
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
    public static VeritransLoadingDialog getProgressDialog() {
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
     * @param activity      activity instance
     * @param errorMessage  error message
     */
    public static void showApiFailedMessage(Activity activity, String errorMessage) {
        try {
            if (!TextUtils.isEmpty(errorMessage)) {
                VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
                if (veritransSDK != null) {
                    Context context = veritransSDK.getContext();
                    if (context != null) {
                        if (errorMessage.contains(context.getString(R.string.retrofit_network_message))) {
                            SdkUtil.showSnackbar(activity, activity.getString(R.string.no_network_msg));
                        } else {
                            SdkUtil.showSnackbar(activity, errorMessage);
                        }
                    } else Logger.e("Context is not available.");
                } else {
                    Logger.e("Veritrans SDK is not started.");
                }

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
     * @return random number
     */
    public static int generateRandomNumber() {
        int number = 0;
        int high = 99999;
        int low = 10000;

        Random random = new Random();
        number = random.nextInt(high - low) + low;
        return number;
    }


    /**
     * helper method to extract {@link MandiriBillPayTransferModel} from {@link TransactionRequest}.
     *
     * @param request   transaction request object
     * @return Transfer model object
     */
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


    /**
     * helper method to extract {@link MandiriClickPayModel} from {@link TransactionRequest}.
     *
     * @param request   transaction request object
     * @return Transfer model object
     */
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

    protected static BCAKlikPayModel getBCAKlikPayModel(TransactionRequest request,
                                                        BCAKlikPayDescriptionModel descriptionModel) {
        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(), request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        return new BCAKlikPayModel(
          descriptionModel,
                transactionDetails,
                request.getItemDetails(),
                request.getBillingAddressArrayList(),
                request.getShippingAddressArrayList(),
                request.getCustomerDetails()
        );
    }


    /**
     * helper method to extract {@link PermataBankTransfer} from {@link TransactionRequest}.
     *
     * @param request   transaction request
     * @return Transfer model object
     */
    protected static PermataBankTransfer getPermataBankModel(TransactionRequest request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        // bank name
        BankTransfer bankTransfer = new BankTransfer();
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            if (veritransSDK.getContext() != null)
                bankTransfer.setBank(veritransSDK.getContext().getString(R.string.payment_permata));
            else Logger.e("Context is not available");
        } else Logger.e("Veritrans SDK is not started.");


        return new PermataBankTransfer(bankTransfer,
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());

    }

    /**
     * helper method to extract {@link PermataBankTransfer} from {@link TransactionRequest}.
     *
     * @param request   Transaction request
     * @return Transfer model object
     */
    protected static BCABankTransfer getBcaBankTransferRequest(TransactionRequest request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        // bank name
        BankTransfer bankTransfer = new BankTransfer();
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            bankTransfer.setBank(veritransSDK.getContext().getString(R.string.payment_bca));
        }

        BCABankTransfer model =
                new BCABankTransfer(bankTransfer,
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;

    }


    /**
     * helper method to extract {@link id.co.veritrans.sdk.models.IndomaretRequestModel} from
     * {@link TransactionRequest}.
     *
     * @param request   transaction request object
     * @return transfer model object
     */
    protected static IndomaretRequestModel getIndomaretRequestModel(TransactionRequest request,
                                                                    CstoreEntity cstoreEntity) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        IndomaretRequestModel model =
                new IndomaretRequestModel();
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            model.setPaymentType(veritransSDK.getContext().getString(R.string.payment_indomaret));
        }
        model.setItem_details(request.getItemDetails());
        model.setCustomerDetails(request.getCustomerDetails());
        model.setTransactionDetails(transactionDetails);
        model.setCstore(cstoreEntity);


        return model;

    }

    protected static BBMMoneyRequestModel getBBMMoneyRequestModel(TransactionRequest request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        BBMMoneyRequestModel model =
                new BBMMoneyRequestModel();
        model.setPaymentType("bbm_money");
        model.setTransactionDetails(transactionDetails);
        return model;
    }


    /**
     * helper method to extract {@link id.co.veritrans.sdk.models.IndosatDompetkuRequest} from
     * {@link TransactionRequest}.
     *
     * @param request   transaction request object
     * @return transfer model object
     */
    protected static IndosatDompetkuRequest getIndosatDompetkuRequestModel(TransactionRequest
                                                                                   request,
                                                                           String msisdn) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        //if (request.isUiEnabled()) {
        //get user details only if using default ui.
        request = initializeUserInfo(request);
        //}


        IndosatDompetku indosatDompetku = null;

        if (msisdn != null && !TextUtils.isEmpty(msisdn)) {
            indosatDompetku = new IndosatDompetku(msisdn.trim());
        }


        IndosatDompetkuRequest model =
                new IndosatDompetkuRequest();

        model.setCustomerDetails(request.getCustomerDetails(), request
                .getShippingAddressArrayList(), request.getBillingAddressArrayList());
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            model.setPaymentType(veritransSDK.getContext().getString(R.string.payment_indosat_dompetku));
        }

        IndosatDompetkuRequest.IndosatDompetkuEntity entity = new IndosatDompetkuRequest
                .IndosatDompetkuEntity();
        entity.setMsisdn("" + msisdn);

        model.setIndosatDompetku(entity);
        model.setItemDetails(request.getItemDetails());
        model.setTransactionDetails(transactionDetails);

        return model;

    }


    /**
     * helper method to extract {@link CIMBClickPayModel} from {@link TransactionRequest}.
     *
     * @param cimbDescription   CIMB bank description
     * @param request           transaction request
     * @return transfer model object
     */

    protected static CIMBClickPayModel getCIMBClickPayModel(TransactionRequest request,
                                                            DescriptionModel cimbDescription) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }


        CIMBClickPayModel model =
                new CIMBClickPayModel(cimbDescription, transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;
    }

    /**
     * helper method to extract {@link MandiriECashModel} from {@link TransactionRequest}.
     *
     * @return Mandiri E Cash Model object
     */

    protected static MandiriECashModel getMandiriECashModel(TransactionRequest request,
                                                            DescriptionModel description) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }


        MandiriECashModel model =
                new MandiriECashModel(description, transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;
    }

    /**
     * helper method to extract {@link CardTransfer} from {@link TransactionRequest}.
     *
     * @param request   transaction request
     * @param cardPaymentDetails   payment details
     * @return Card transfer model object
     */
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


    /**
     * helper method to add {@link CustomerDetails} in {@link TransactionRequest}.
     *
     * @param transactionRequest    transaction request
     * @return transactionRequest with  {@link CustomerDetails}.
     */
    protected static TransactionRequest initializeUserInfo(TransactionRequest transactionRequest) {
        transactionRequest = getUserDetails(transactionRequest);
        return transactionRequest;
    }

    /**
     * it extracts customer information from TransactionRequest.
     *
     * @param request instance of TransactionRequest
     * @return transaction request with {@link UserDetail}
     */
    private static TransactionRequest getUserDetails(TransactionRequest request) {

        UserDetail userDetail = null;
        CustomerDetails mCustomerDetails = null;

        try {
            VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
            if (veritransSDK != null) {
                userDetail = LocalDataHandler.readObject(veritransSDK.getContext().getString(R.string.user_details), UserDetail.class);
            }

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
                Logger.e("User details not available.");
                //SdkUtil.showSnackbar(VeritransSDK.getVeritransSDK().getContext(), "User details not available.");
                //request.getActivity().finish();
            }
        } catch (Exception ex) {
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

    /**
     * shows keyboard on screen forcefully.
     *
     * @param activity  activity instance
     * @param editText  edittext instance
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


    /**
     * return user details if available else return null
     *
     * @param context   Application context
     * @return UserDetail
     */
    protected static UserDetail getUserDetails(Context context) {

        StorageDataHandler storageDataHandler = new StorageDataHandler();
        try {
            VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
            if (veritransSDK != null) {
                UserDetail userDetail = LocalDataHandler.readObject(veritransSDK.getContext().getString(R.string.user_details), UserDetail.class);
                return userDetail;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * helper method to extract {@link EpayBriTransfer} from {@link TransactionRequest}.
     *
     * @param request   transaction request object
     * @return E Pay BRI transfer model
     */
    protected static EpayBriTransfer getEpayBriBankModel(TransactionRequest request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }


        EpayBriTransfer model =
                new EpayBriTransfer(transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;

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

    /**
     * Get device identifier using SDK context.
     *
     * @return device identifier
     */
    public static String getDeviceId() {
        String deviceId = "UNKNOWN";
        try {
            deviceId = Settings.Secure.getString(VeritransSDK.getVeritransSDK().getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
        return deviceId;
    }
}