package com.midtrans.sdk.corekit.core;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.snap.CreditCard;

import java.util.ArrayList;

/**
 * It contains information about transaction like {@literal orderId}, {@literal amount},
 * itemDetails
 * <p/>
 * Created by shivam on 11/5/15.
 */
public class TransactionRequest {

    public static final String ERROR_MESSAGE = "can/'t change values at this moment , transaction" +
            " is " +
            "already initialized.";
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


    /**
     * @param orderId       order id of transaction.
     * @param amount        amount to charge.
     * @param paymentMethod payment method.
     */
    public TransactionRequest(String orderId, double amount, int paymentMethod) {

        if (orderId != null && amount > 0) {
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

        if (orderId != null && amount > 0) {
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
        mCustomerDetails = customerDetails;

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
     *
     * @param clickType    use click type from Constants.
     * @param isSecureCard is secure
     */
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
}