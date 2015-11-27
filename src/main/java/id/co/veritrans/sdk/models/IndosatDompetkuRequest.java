package id.co.veritrans.sdk.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 * model class to hold information bout Bank Transfer api call
 *
 * Created by shivam on 10/26/15.
 */
public class IndosatDompetkuRequest extends TransactionModel {


    public static final String PAYMENT_TYPE = "indosat_dompetku";

    /**
     * payment_type : bank_transfer
     * bank_transfer : {"bank":"permata"}
     * transaction_details : {"gross_amount":"100","order_id":"10938011"}
     *
     *
     * {
     "payment_type" : "indosat_dompetku",

     "transaction_details" : {"gross_amount":"100000","order_id":"5f5f870d812332"},

     "item_details" : [ {
     "id": "1388",
     "price": 100000,
     "quantity": 1,
     "name": "Mie Ayam Original"
     } ] ,

     "customer_details":
     {
     "email":"shivam.gosavi340@gmail.com",
     "first_name":"shivam",
     "last_name":"s",
     "phone":"081311874839"
     },

     "indosat_dompetku":
     {
     "msisdn":"081311874839"
     }

     }
     *
     */


    @SerializedName("indosat_dompetku")
    private BankTransfer bankTransfer;


    public IndosatDompetkuRequest(BankTransfer bankTransfer, TransactionDetails transactionDetails,
                                  ArrayList<ItemDetails> itemDetails, ArrayList<BillingAddress>
                                          billingAddresses, ArrayList<ShippingAddress>
                                          shippingAddresses, CustomerDetails customerDetails) {

        this.paymentType = PAYMENT_TYPE;

        this.bankTransfer = bankTransfer;
        this.transactionDetails = transactionDetails;
        this.itemDetails = itemDetails;
        this.billingAddresses = billingAddresses;
        this.shippingAddresses = shippingAddresses;
        this.customerDetails = customerDetails;
    }


    public String getPayment_type() {
        return paymentType;
    }

    public BankTransfer getBankTransfer() {
        return bankTransfer;
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