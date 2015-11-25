package id.co.veritrans.sdk.core;

import android.app.Activity;

import java.util.ArrayList;

import id.co.veritrans.sdk.models.BillInfoModel;
import id.co.veritrans.sdk.models.BillingAddress;
import id.co.veritrans.sdk.models.CustomerDetails;
import id.co.veritrans.sdk.models.ItemDetails;
import id.co.veritrans.sdk.models.ShippingAddress;

/**
 *
 * It contains information about transaction like {@literal orderId}, {@literal amount}, itemDetails
 *
 * Created by shivam on 11/5/15.
 */
public class TransactionRequest {

    public static final String ERROR_MESSAGE = "can/'t change values at this moment , transaction" +
            " is " +
            "already initialized.";
    /**
     * payment method using which user wants to perform transaction.
     * use payment methods from {@link id.co.veritrans.sdk.core.Constants}
     */
    protected int paymentMethod = Constants.PAYMENT_METHOD_NOT_SELECTED;

    protected Activity activity = null;

    /**
     * unique order id to identify this transaction.
     * <p/>Using this id later u can check status of transaction.
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
     *  contains details about customer
     */
    private CustomerDetails mCustomerDetails = null;

    /**
     * helps to identify whether to use ui or not.
     */
    private boolean useUi = true;


    /**
     * @param orderId order id of transaction.
     * @param activity instance of activity.
     * @param amount  amount to charge.
     * @param paymentMethod payment method.
     */
    public TransactionRequest(String orderId, Activity
            activity, double amount, int paymentMethod) {

        if (orderId != null && activity != null && amount > 0) {
            this.orderId = orderId;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.activity = activity;
        } else {
            Logger.e("Invalid transaction data.");
        }
    }

    public CustomerDetails getCustomerDetails() {
        return mCustomerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            mCustomerDetails = customerDetails;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
    }

    public ArrayList<BillingAddress> getBillingAddressArrayList() {
        return mBillingAddressArrayList;
    }

    public void setBillingAddressArrayList(ArrayList<BillingAddress> billingAddressArrayList) {
        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            mBillingAddressArrayList = billingAddressArrayList;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
    }

    public ArrayList<ShippingAddress> getShippingAddressArrayList() {
        return mShippingAddressArrayList;
    }

    public void setShippingAddressArrayList(ArrayList<ShippingAddress> shippingAddressArrayList) {
        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            mShippingAddressArrayList = shippingAddressArrayList;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
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

        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            this.billInfoModel = billInfoModel;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
    }

    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ArrayList<ItemDetails> itemDetails) {

        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            this.itemDetails = itemDetails;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
    }

    public Activity getActivity() {
        return activity;
    }


    /**
     * It will help to enable/disable default ui provided by sdk.
     * By default it is true, set it to false to use your own ui to show transaction.
     *
     * @param enableUi
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
     * @param isSecureCard
     */
    public void setCardPaymentInfo(String clickType, boolean isSecureCard) {
        Logger.i("clicktype:" + clickType + ",isSecured:" + isSecureCard);
        this.cardClickType = clickType;
        this.isSecureCard = isSecureCard;
    }

}