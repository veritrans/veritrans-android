package id.co.veritrans.sdk.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by shivam on 10/26/15.
 */
public class PermataBankTransfer {


    /**
     * payment_type : bank_transfer
     * bank_transfer : {"bank":"permata"}
     * transaction_details : {"gross_amount":"100","order_id":"10938011"}
     */
    private final String payment_type = "bank_transfer";

    @SerializedName("bank_transfer")
    private BankTransfer bankTransfer;

    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;

    @SerializedName("item_details")
    private ArrayList<ItemDetails> mItemDetails = new ArrayList<>();

    @SerializedName("billing_address")
    private ArrayList<BillingAddress> mBillingAddresses = new ArrayList<>();

    @SerializedName("shipping_address")
    private ArrayList<ShippingAddress> mShippingAddresses = new ArrayList<>();

    @SerializedName("customer_details")
    private CustomerDetails mCustomerDetails = null;


    public PermataBankTransfer(BankTransfer bankTransfer, TransactionDetails transactionDetails,
                               ArrayList<ItemDetails> itemDetails, ArrayList<BillingAddress>
                                       billingAddresses, ArrayList<ShippingAddress>
                                       shippingAddresses, CustomerDetails customerDetails) {
        this.bankTransfer = bankTransfer;
        this.transactionDetails = transactionDetails;
        mItemDetails = itemDetails;
        mBillingAddresses = billingAddresses;
        mShippingAddresses = shippingAddresses;
        mCustomerDetails = customerDetails;
    }


    public String getPayment_type() {
        return payment_type;
    }

    public BankTransfer getBankTransfer() {
        return bankTransfer;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public ArrayList<ItemDetails> getItemDetails() {
        return mItemDetails;
    }

    public ArrayList<BillingAddress> getBillingAddresses() {
        return mBillingAddresses;
    }

    public ArrayList<ShippingAddress> getShippingAddresses() {
        return mShippingAddresses;
    }

    public CustomerDetails getCustomerDetails() {
        return mCustomerDetails;
    }




}
