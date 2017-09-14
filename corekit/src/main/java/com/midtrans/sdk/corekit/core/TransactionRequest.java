package com.midtrans.sdk.corekit.core;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.models.BcaBankTransferRequestModel;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ExpiryModel;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.snap.BankTransferRequestModel;
import com.midtrans.sdk.corekit.models.snap.CreditCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * It contains information about transaction like {@literal orderId}, {@literal amount},
 * itemDetails
 * <p/>
 * Created by shivam on 11/5/15.
 */
public class TransactionRequest {

    /**
     * payment method using which user wants to perform transaction. use payment methods from {@link
     * Constants}
     */
    protected int paymentMethod = Constants.PAYMENT_METHOD_NOT_SELECTED;

    /**
     * unique order id to identify this transaction. <p/>Using this id later u can check status of
     * transaction.
     */
    private String orderId = null;

    /**
     * amount to charge customer.
     */
    private double amount = 0.0;

    /**
     * It helps to identify whether to execute transaction in secure manner or not.
     */
    private boolean isSecureCard = true;

    private String cardClickType;

    private boolean promoEnabled;
    private List<String> promoCodes;

    /**
     * It contains an extra information that you want to display on bill.
     */
    private BillInfoModel billInfoModel = null;

    /**
     * list of purchased items.
     */
    private ArrayList<ItemDetails> itemDetails = new ArrayList();

    /**
     * List of billing addresses.
     */
    private ArrayList<BillingAddress> mBillingAddressArrayList = new ArrayList<>();

    /**
     * List of shipping addresses.
     */
    private ArrayList<ShippingAddress> mShippingAddressArrayList = new ArrayList<>();

    /**
     * contains details about customer
     */
    private CustomerDetails mCustomerDetails = null;

    /**
     * helps to identify whether to use ui or not.
     */
    private boolean useUi = true;
    private CreditCard creditCard;
    private Map<String, String> customObject;
    private ExpiryModel expiry;
    private String customField1;
    private String customField2;
    private String customField3;

    private BankTransferRequestModel permataVa;
    private BcaBankTransferRequestModel bcaVa;
    private BankTransferRequestModel bniVa;
    private List<String> enabledPayments;

    /**
     * @param orderId       order id of transaction.
     * @param amount        amount to charge.
     * @param paymentMethod payment method.
     */
    public TransactionRequest(String orderId, double amount, int paymentMethod) {

        if (!TextUtils.isEmpty(orderId) && amount > 0) {
            this.orderId = orderId;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
        } else {
            Logger.e("Invalid transaction data.");
        }
    }


    /**
     * @param orderId order id of transaction.
     * @param amount  amount to charge.
     */
    public TransactionRequest(String orderId, double amount) {

        if (!TextUtils.isEmpty(orderId) && amount > 0) {
            this.orderId = orderId;
            this.amount = amount;
            this.paymentMethod = Constants.PAYMENT_METHOD_NOT_SELECTED;
        } else {
            Logger.e("Invalid transaction data.");
        }
    }


    public CustomerDetails getCustomerDetails() {
        return mCustomerDetails;
    }

    public void setCustomerDetails(@NonNull CustomerDetails customerDetails) {
        mCustomerDetails = sanitizeCustomerDetails(customerDetails);
    }

    private CustomerDetails sanitizeCustomerDetails(CustomerDetails customerDetails) {
        if (customerDetails != null) {
            if (TextUtils.isEmpty(customerDetails.getFirstName())) {
                customerDetails.setFirstName(null);
            }

            if (TextUtils.isEmpty(customerDetails.getLastName())) {
                customerDetails.setLastName(null);
            }

            if (TextUtils.isEmpty(customerDetails.getEmail())) {
                customerDetails.setEmail(null);
            }

            if (TextUtils.isEmpty(customerDetails.getPhone())) {
                customerDetails.setPhone(null);
            }

            sanitizeBillingAddress(customerDetails.getBillingAddress());
            sanitizeShippingAddress(customerDetails.getShippingAddress());
        }
        return customerDetails;
    }

    private void sanitizeBillingAddress(BillingAddress billingAddress) {
        if (billingAddress != null) {
            if (TextUtils.isEmpty(billingAddress.getAddress())) {
                billingAddress.setAddress(null);
            }

            if (TextUtils.isEmpty(billingAddress.getFirstName())) {
                billingAddress.setFirstName(null);
            }

            if (TextUtils.isEmpty(billingAddress.getLastName())) {
                billingAddress.setLastName(null);
            }

            if (TextUtils.isEmpty(billingAddress.getCity())) {
                billingAddress.setCity(null);
            }

            if (TextUtils.isEmpty(billingAddress.getPostalCode())) {
                billingAddress.setPostalCode(null);
            }

            if (TextUtils.isEmpty(billingAddress.getPhone())) {
                billingAddress.setPhone(null);
            }

            if (TextUtils.isEmpty(billingAddress.getCountryCode())) {
                billingAddress.setCountryCode(null);
            }
        }
    }

    private void sanitizeShippingAddress(ShippingAddress shippingAddress) {
        if (shippingAddress != null) {
            if (TextUtils.isEmpty(shippingAddress.getAddress())) {
                shippingAddress.setAddress(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getFirstName())) {
                shippingAddress.setFirstName(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getLastName())) {
                shippingAddress.setLastName(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getCity())) {
                shippingAddress.setCity(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getPostalCode())) {
                shippingAddress.setPostalCode(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getPhone())) {
                shippingAddress.setPhone(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getCountryCode())) {
                shippingAddress.setCountryCode(null);
            }
        }
    }

    public ArrayList<BillingAddress> getBillingAddressArrayList() {
        return mBillingAddressArrayList;
    }

    public void setBillingAddressArrayList(@NonNull ArrayList<BillingAddress> billingAddressArrayList) {

        mBillingAddressArrayList = billingAddressArrayList;

    }

    public ArrayList<ShippingAddress> getShippingAddressArrayList() {
        return mShippingAddressArrayList;
    }

    public void setShippingAddressArrayList(@NonNull ArrayList<ShippingAddress> shippingAddressArrayList) {
        mShippingAddressArrayList = shippingAddressArrayList;

    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isSecureCard() {
        return isSecureCard;
    }

    public String getCardClickType() {
        return cardClickType;
    }

    public BillInfoModel getBillInfoModel() {
        return billInfoModel;
    }

    public void setBillInfoModel(BillInfoModel billInfoModel) {
        this.billInfoModel = billInfoModel;
    }

    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ArrayList<ItemDetails> itemDetails) {
        this.itemDetails = itemDetails;
    }

    /**
     * It will help to enable/disable default ui provided by sdk. By default it is true, set it to
     * false to use your own ui to show transaction.
     *
     * @param enableUi is UI mode enabled
     */
    protected void enableUi(boolean enableUi) {
        this.useUi = enableUi;
    }

    protected boolean isUiEnabled() {
        return useUi;
    }

    /**
     * It is used in case of payment using credit card.
     * this method being deprecated since v1.9.x, please don't implement this method
     *
     * @param clickType    use click type from Constants.
     * @param isSecureCard is secure
     */
    @Deprecated
    public void setCardPaymentInfo(String clickType, boolean isSecureCard) {
        Logger.i("clicktype:" + clickType + ",isSecured:" + isSecureCard);
        this.cardClickType = clickType;
        this.isSecureCard = isSecureCard;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public ExpiryModel getExpiry() {
        return expiry;
    }

    public void setExpiry(ExpiryModel expiry) {
        this.expiry = expiry;
    }

    public List<String> getPromoCodes() {
        return promoCodes;
    }

    public void setPromoCodes(List<String> promoCodes) {
        this.promoCodes = promoCodes;
    }

    public boolean isPromoEnabled() {
        return promoEnabled;
    }

    public void setPromoEnabled(boolean promoEnabled) {
        this.promoEnabled = promoEnabled;
    }

    public String getCustomField1() {
        return customField1;
    }

    public void setCustomField1(String customField1) {
        this.customField1 = customField1;
    }

    public String getCustomField2() {
        return customField2;
    }

    public void setCustomField2(String customField2) {
        this.customField2 = customField2;
    }

    public String getCustomField3() {
        return customField3;
    }

    public void setCustomField3(String customField3) {
        this.customField3 = customField3;
    }

    public BankTransferRequestModel getPermataVa() {
        return permataVa;
    }

    public void setPermataVa(BankTransferRequestModel permataVa) {
        this.permataVa = permataVa;
    }

    public BcaBankTransferRequestModel getBcaVa() {
        return bcaVa;
    }

    public void setBcaVa(BcaBankTransferRequestModel bcaVa) {
        this.bcaVa = bcaVa;
    }

    public BankTransferRequestModel getBniVa() {
        return bniVa;
    }

    public void setBniVa(BankTransferRequestModel bniVa) {
        this.bniVa = bniVa;
    }

    public List<String> getEnabledPayments() {
        return enabledPayments;
    }

    public void setEnabledPayments(List<String> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }
}