package com.midtrans.sdk.corekit.models;

import com.midtrans.sdk.corekit.core.Constants;

import java.util.ArrayList;

/**
 * Created by Ankit on 11/26/15.
 */
public class CIMBClickPayModel extends TransactionModel {

    public static final String PAYMENT_TYPE = Constants.PAYMENT_CIMB_CLICKS;

    /**
     * payment_type : cimb_clicks
     */

    public CIMBClickPayModel(DescriptionModel cimbDescription, TransactionDetails transactionDetails,
                             ArrayList<ItemDetails> itemDetails, ArrayList<BillingAddress>
                                     billingAddresses, ArrayList<ShippingAddress>
                                     shippingAddresses, CustomerDetails customerDetails) {

        this.paymentType = PAYMENT_TYPE;
        this.cimbClicks = cimbDescription;
        this.transactionDetails = transactionDetails;
        this.itemDetails = itemDetails;
        this.billingAddresses = billingAddresses;
        this.shippingAddresses = shippingAddresses;
        this.customerDetails = customerDetails;
    }


    public String getPayment_type() {
        return paymentType;
    }

    public DescriptionModel getCIMBDescription() {
        return cimbClicks;
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
}
