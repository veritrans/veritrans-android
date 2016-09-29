package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * contains information required to execute mandiri click pay request.
 *
 * Created by shivam on 11/2/15.
 */
public class MandiriClickPayRequestModel extends TransactionModel {


    public static final String PAYMENT_TYPE = "mandiri_clickpay";

    /**
     * payment_type : bank_transfer bank_transfer : {"bank":"permata"} transaction_details :
     * {"gross_amount":"100","order_id":"10938011"}
     */


    @SerializedName("mandiri_clickpay")
    private MandiriClickPayModel mandiriClickPayModel;


    /**
     * payment_type : mandiri_clickpay mandiri_clickpay : {"card_number":"4111111111111111","input3":"11111","input2":"100",
     * "input1":"1111111111","token":"111111"} transaction_details : {"gross_amount":"100","order_id":"10938011"}
     */


    public MandiriClickPayRequestModel(MandiriClickPayModel mandiriClickPayModel,
                                       TransactionDetails transactionDetails,
                                       ArrayList<ItemDetails> itemDetails, ArrayList<BillingAddress>
                                               billingAddresses, ArrayList<ShippingAddress>
                                               shippingAddresses, CustomerDetails customerDetails) {

        this.paymentType = PAYMENT_TYPE;
        this.mandiriClickPayModel = mandiriClickPayModel;
        this.transactionDetails = transactionDetails;
        this.itemDetails = itemDetails;
        this.billingAddresses = billingAddresses;
        this.shippingAddresses = shippingAddresses;
        this.customerDetails = customerDetails;
    }


    public String getPaymentType() {
        return PAYMENT_TYPE;
    }

    public MandiriClickPayModel getMandiriClickPayModel() {
        return mandiriClickPayModel;
    }

    public void setMandiriClickPayModel(MandiriClickPayModel mandiriClickPayModel) {
        this.mandiriClickPayModel = mandiriClickPayModel;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
