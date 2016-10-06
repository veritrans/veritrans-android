package com.midtrans.sdk.corekit.models;

import java.util.ArrayList;

/**
 * Created by Ankit on 11/30/15.
 */
public class MandiriECashModel extends TransactionModel {

    public static final String PAYMENT_TYPE = "mandiri_ecash";

    /**
     * payment_type : mandiri_ecash
     */

    public MandiriECashModel(DescriptionModel description, TransactionDetails transactionDetails,
                             ArrayList<ItemDetails> itemDetails, ArrayList<BillingAddress>
                                     billingAddresses, ArrayList<ShippingAddress>
                                     shippingAddresses, CustomerDetails customerDetails) {

        this.paymentType = PAYMENT_TYPE;
        this.mandiriECash = description;
        this.transactionDetails = transactionDetails;
        this.itemDetails = itemDetails;
        this.billingAddresses = billingAddresses;
        this.shippingAddresses = shippingAddresses;
        this.customerDetails = customerDetails;
    }


    public String getPayment_type() {
        return paymentType;
    }

    public DescriptionModel getDescription() {
        return mandiriECash;
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
