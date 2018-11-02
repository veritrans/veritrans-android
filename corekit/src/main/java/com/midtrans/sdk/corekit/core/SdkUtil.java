package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.models.*;
import com.midtrans.sdk.corekit.models.promo.Promo;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.params.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.GCIPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.KlikBcaPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.PromoDetails;
import com.midtrans.sdk.corekit.models.snap.payment.*;
import com.midtrans.sdk.corekit.utilities.Installation;
import com.securepreferences.SecurePreferences;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 18/06/2016.
 */
public class SdkUtil {
    private static final String UNIT_MINUTES = "minutes";

    /**
     * helper method to add {@link CustomerDetails} in {@link TransactionRequest}.
     *
     * @param transactionRequest transaction request
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
    static TransactionRequest getUserDetails(TransactionRequest request) {

        UserDetail userDetail = null;
        CustomerDetails mCustomerDetails = null;

        try {
            userDetail = LocalDataHandler.readObject(Constants.USER_DETAILS, UserDetail.class);

            if (userDetail != null && !TextUtils.isEmpty(userDetail.getUserFullName())) {
                ArrayList<UserAddress> userAddresses = userDetail.getUserAddresses();
                if (userAddresses != null && !userAddresses.isEmpty()) {
                    Logger.i("Found " + userAddresses.size() + " user addresses.");
                    mCustomerDetails = new CustomerDetails();
                    mCustomerDetails.setPhone(userDetail.getPhoneNumber());
                    mCustomerDetails.setFirstName(userDetail.getUserFullName());
                    mCustomerDetails.setLastName(null);
                    mCustomerDetails.setEmail(userDetail.getEmail());
                    //added email in performTransaction()
                    request.setCustomerDetails(mCustomerDetails);

                    request = extractUserAddress(userDetail, userAddresses, request);
                }

            } else {
                Logger.e("User details not available.");
            }
        } catch (Exception ex) {
            Logger.e("Error while fetching user details : " + ex.getMessage());
        }

        return request;
    }

    static TransactionRequest extractUserAddress(UserDetail userDetail,
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

        CustomerDetails customerDetails = request.getCustomerDetails();

        if (customerDetails != null) {
            if (billingAddressArrayList.size() > 0 && billingAddressArrayList.get(0) != null) {
                customerDetails.setBillingAddress(billingAddressArrayList.get(0));
            }

            if (shippingAddressArrayList.size() > 0 && shippingAddressArrayList.get(0) != null) {
                customerDetails.setShippingAddress(shippingAddressArrayList.get(0));
            }
            request.setCustomerDetails(customerDetails);
        }
        return request;
    }

    @NonNull
    private static BillingAddress getBillingAddress(UserDetail userDetail, UserAddress userAddress) {
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setCity(userAddress.getCity());
        billingAddress.setFirstName(userDetail.getUserFullName());
        billingAddress.setLastName("");
        billingAddress.setPhone(userDetail.getPhoneNumber());
        billingAddress.setCountryCode(userAddress.getCountry());
        billingAddress.setPostalCode(userAddress.getZipcode());
        billingAddress.setAddress(userAddress.getAddress());
        return billingAddress;
    }


    @NonNull
    private static ShippingAddress getShippingAddress(UserDetail userDetail, UserAddress userAddress) {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setCity(userAddress.getCity());
        shippingAddress.setFirstName(userDetail.getUserFullName());
        shippingAddress.setLastName("");
        shippingAddress.setPhone(userDetail.getPhoneNumber());
        shippingAddress.setCountryCode(userAddress.getCountry());
        shippingAddress.setPostalCode(userAddress.getZipcode());
        shippingAddress.setAddress(userAddress.getAddress());
        return shippingAddress;
    }

    /**
     * Get device identifier using SDK context.
     *
     * @return device identifier
     */
    public static String getDeviceId(Context context) {
        String deviceId = "UNKNOWN";
        try {
            deviceId = Installation.id(context);
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
        return deviceId;
    }

    public static TokenRequestModel getSnapTokenRequestModel(TransactionRequest transactionRequest) {

        if (transactionRequest.isUiEnabled()) {
            //get user details only if using default ui.
            transactionRequest = initializeUserInfo(transactionRequest);
        }

        SnapTransactionDetails details = new SnapTransactionDetails(transactionRequest.getOrderId(), transactionRequest.getAmount());

        // set currency
        details.setCurrency(transactionRequest.getCurrency());

        TokenRequestModel requestModel = new TokenRequestModel(details, transactionRequest.getItemDetails(),
                transactionRequest.getCustomerDetails(), transactionRequest.getCreditCard());
        requestModel.setExpiry(transactionRequest.getExpiry());


        // Set expiry if it's available
        if (transactionRequest.getExpiry() != null) {
            requestModel.setExpiry(transactionRequest.getExpiry());
        }

        if (transactionRequest.getCustomField1() != null && !TextUtils.isEmpty(transactionRequest.getCustomField1())) {
            requestModel.setCustomField1(transactionRequest.getCustomField1());
        }

        if (transactionRequest.getCustomField2() != null && !TextUtils.isEmpty(transactionRequest.getCustomField2())) {
            requestModel.setCustomField2(transactionRequest.getCustomField2());
        }

        if (transactionRequest.getCustomField3() != null && !TextUtils.isEmpty(transactionRequest.getCustomField3())) {
            requestModel.setCustomField3(transactionRequest.getCustomField3());
        }

        if (transactionRequest.getPermataVa() != null) {
            requestModel.setPermataVa(transactionRequest.getPermataVa());
        }

        if (transactionRequest.getBcaVa() != null) {
            requestModel.setBcaVa(transactionRequest.getBcaVa());
        }

        if (transactionRequest.getBniVa() != null) {
            requestModel.setBcaVa(transactionRequest.getBniVa());
        }

        if (transactionRequest.getEnabledPayments() != null
                && !transactionRequest.getEnabledPayments().isEmpty()) {
            requestModel.setEnabledPayments(transactionRequest.getEnabledPayments());
        }

        if (transactionRequest.getGopay() != null) {
            requestModel.setGopay(transactionRequest.getGopay());
        }

        return requestModel;
    }

    public static CustomerDetailRequest initializePaymentDetails(Transaction transaction) {
        if (transaction != null) {
            CustomerDetails customerDetails = transaction.getCustomerDetails();
            if (customerDetails != null) {
                CustomerDetailRequest customerDetailRequest = new CustomerDetailRequest();
                customerDetailRequest.setFullName(customerDetails.getFirstName());
                customerDetailRequest.setPhone(customerDetailRequest.getPhone());
                customerDetailRequest.setEmail(customerDetailRequest.getEmail());

                return customerDetailRequest;
            }
        }

        return null;
    }

    public static CreditCardPaymentRequest getCreditCardPaymentRequest(CreditCardPaymentModel model, Transaction transaction) {

        CustomerDetailRequest customerDetailRequest = initializePaymentDetails(transaction);

        CreditCardPaymentParams paymentParams = new CreditCardPaymentParams(model.getCardToken(),
                model.isSavecard(), model.getMaskedCardNumber(), model.getInstallment());
        paymentParams.setPointRedeemed(model.getPointRedeemed());

        if (model.getBank() != null && model.getBank().equalsIgnoreCase(BankType.MANDIRI)) {
            paymentParams.setBank(model.getBank());
        }

        //for custom type adapter in gson, omit field point only
        // if the transaction is not from bank point page
        paymentParams.setFromBankPoint(model.isFromBankPoint());

        CreditCardPaymentRequest creditCardPaymentRequest = new CreditCardPaymentRequest(PaymentType.CREDIT_CARD, paymentParams, customerDetailRequest);

        //set promo is selected
        Promo promo = model.getPromoSelected();
        if (promo != null) {
            PromoDetails promoDetails = new PromoDetails(promo.getId(), promo.getDiscountedGrossAmount());
            creditCardPaymentRequest.setPromoDetails(promoDetails);
        }

        return creditCardPaymentRequest;
    }


    public static BankTransferPaymentRequest getBankTransferPaymentRequest(String email, String paymentType) {
        CustomerDetailRequest request = new CustomerDetailRequest();
        request.setEmail(email);
        BankTransferPaymentRequest paymentRequest = new BankTransferPaymentRequest(paymentType, request);
        return paymentRequest;
    }

    public static KlikBCAPaymentRequest getKlikBCAPaymentRequest(String userId, String paymentType) {

        KlikBCAPaymentRequest klikBCAPaymentRequest = new KlikBCAPaymentRequest(
                paymentType, new KlikBcaPaymentParams(userId));
        return klikBCAPaymentRequest;
    }


    public static GCIPaymentRequest getGCIPaymentRequest(String cardNumber, String password) {
        GCIPaymentParams paymentParams = new GCIPaymentParams(cardNumber, password);
        GCIPaymentRequest request = new GCIPaymentRequest(paymentParams, PaymentType.GCI);
        return request;
    }


    public static GoPayPaymentRequest getGoPayPaymentRequest() {
        return new GoPayPaymentRequest(PaymentType.GOPAY);
    }


    public static DanamonOnlinePaymentRequest getDanamonOnlinePaymentRequest() {
        return new DanamonOnlinePaymentRequest(PaymentType.DANAMON_ONLINE);
    }


    public static SecurePreferences newPreferences(Context context, String name) {

        SecurePreferences preferences = new SecurePreferences(context, context.getString(R.string.PREFERENCE_PASSWORD), name);
        int prefVersion;
        try {
            prefVersion = preferences.getInt(Constants.KEY_PREFERENCES_VERSION, 0);
        } catch (ClassCastException e) {
            prefVersion = 0;
        }
        if (prefVersion == 0 || prefVersion < Constants.PREFERENCES_VERSION) {
            SecurePreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putInt(Constants.KEY_PREFERENCES_VERSION, Constants.PREFERENCES_VERSION);
            editor.apply();
        }

        return preferences;
    }

    public static SnapServiceManager newSnapServiceManager(int requestTimeOut) {
        return new SnapServiceManager(MidtransRestAdapter.newSnapApiService(requestTimeOut));
    }

    public static MidtransServiceManager newMidtransServiceManager(int requestTimeOut) {
        return new MidtransServiceManager(MidtransRestAdapter.newMidtransApiService(requestTimeOut));
    }

    public static MerchantServiceManager newMerchantServiceManager(String merchantServerUrl, int requestTimeOut) {
        if (TextUtils.isEmpty(merchantServerUrl)) {
            return null;
        }

        return new MerchantServiceManager(MidtransRestAdapter.newMerchantApiService(merchantServerUrl, requestTimeOut));
    }

    public static MixpanelAnalyticsManager newMixpanelAnalyticsManager(String versionName, String deviceId, String merchantName, String flow, String deviceType, boolean isLogEnabled, Context context) {
        return new MixpanelAnalyticsManager(versionName, deviceId, merchantName, flow, deviceType, isLogEnabled, context);
    }
}
