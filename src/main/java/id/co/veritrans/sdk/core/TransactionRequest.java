package id.co.veritrans.sdk.core;

import android.app.Activity;

import java.util.ArrayList;

import id.co.veritrans.sdk.models.BillInfoModel;
import id.co.veritrans.sdk.models.BillingAddress;
import id.co.veritrans.sdk.models.CustomerDetails;
import id.co.veritrans.sdk.models.ItemDetails;
import id.co.veritrans.sdk.models.ShippingAddress;

/**
 * Created by shivam on 11/5/15.
 */
public class TransactionRequest {

    public static final String ERROR_MESSAGE = "can/'t change values at this moment , transaction is " +
            "already initialized.";
    private String orderId = null;

    private double amount = 0.0;

    /**
     * payment method using which user wants to perform transaction.
     * use payment methods from {@link id.co.veritrans.sdk.core.Constants}
     */
    protected int paymentMethod = Constants.PAYMENT_METHOD_NOT_SELECTED;

    private boolean isSecureCard = true;

    private String cardClickType;

    private BillInfoModel billInfoModel = null;

    private ArrayList<ItemDetails> itemDetails = new ArrayList();
    private ArrayList<BillingAddress> mBillingAddressArrayList = new ArrayList<>();
    private ArrayList<ShippingAddress> mShippingAddressArrayList = new ArrayList<>();
    private CustomerDetails mCustomerDetails = null;


    protected Activity activity = null;

    private boolean useUi = true;


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


    public void setBillInfoModel(BillInfoModel billInfoModel) {

        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            this.billInfoModel = billInfoModel;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
    }

    public void setItemDetails(ArrayList<ItemDetails> itemDetails) {

        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            this.itemDetails = itemDetails;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
    }


    public void setShippingAddressArrayList(ArrayList<ShippingAddress> shippingAddressArrayList) {
        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            mShippingAddressArrayList = shippingAddressArrayList;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
    }

    public void setBillingAddressArrayList(ArrayList<BillingAddress> billingAddressArrayList) {
        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            mBillingAddressArrayList = billingAddressArrayList;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
    }


    public void setCustomerDetails(CustomerDetails customerDetails) {
        if (!VeritransSDK.getVeritransSDK().isRunning()) {
            mCustomerDetails = customerDetails;
        } else {
            Logger.e(ERROR_MESSAGE);
        }
    }


    public CustomerDetails getCustomerDetails() {
        return mCustomerDetails;
    }


    public ArrayList<BillingAddress> getBillingAddressArrayList() {
        return mBillingAddressArrayList;
    }


    public ArrayList<ShippingAddress> getShippingAddressArrayList() {
        return mShippingAddressArrayList;
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

    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
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
    public void enableUi(boolean enableUi) {
        this.useUi = enableUi;
    }

    public boolean isUiEnabled() {
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