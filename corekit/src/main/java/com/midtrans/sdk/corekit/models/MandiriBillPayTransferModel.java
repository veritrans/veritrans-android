package com.midtrans.sdk.corekit.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by shivam on 10/26/15.
 */
public class MandiriBillPayTransferModel extends TransactionModel {


    public static final String PAYMENT_TYPE = "echannel";

    /**
     * payment_type : bank_transfer bank_transfer : {"bank":"permata"} transaction_details :
     * {"gross_amount":"100","order_id":"10938011"}
     */


    @SerializedName("echannel")
    private BillInfoModel billInfoModel;


    public MandiriBillPayTransferModel(BillInfoModel billInfoModel, TransactionDetails
            transactionDetails,
                                       ArrayList<ItemDetails> itemDetails, ArrayList<BillingAddress>
                                               billingAddresses, ArrayList<ShippingAddress>
                                               shippingAddresses, CustomerDetails customerDetails) {

        this.paymentType = PAYMENT_TYPE;

        this.billInfoModel = billInfoModel;
        this.transactionDetails = transactionDetails;
        this.itemDetails = itemDetails;
        this.billingAddresses = billingAddresses;
        this.shippingAddresses = shippingAddresses;
        this.customerDetails = customerDetails;
    }


    public String getPayment_type() {
        return paymentType;
    }


    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public ArrayList<BillingAddress> getBillingAddresses() {
        return billingAddresses;
    }

    public ArrayList<ShippingAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public BillInfoModel getBillInfoModel() {
        return billInfoModel;
    }
}