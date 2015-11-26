package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ankit on 11/26/15.
 */
public class CIMBClickPayRequestModel extends TransactionModel {


    public static final String PAYMENT_TYPE = "cimb_clicks";

    /**
     * payment_type : cimb_clicks
     */


    @SerializedName("cimb_clicks")
    private CIMBClickPayModel cimbClickPayModel;

    /**
     * payment_type : cimb_clicks
     * cimb_clicks : {"card_number":"4111111111111111","input3":"11111","input2":"100",
     * "input1":"1111111111","token":"111111"}
     * transaction_details : {"gross_amount":"100","order_id":"10938011"}
     */


    public CIMBClickPayRequestModel(CIMBClickPayModel cimbClickPayModel,
                                       TransactionDetails transactionDetails,
                                       ArrayList<ItemDetails> itemDetails, ArrayList<BillingAddress>
                                               billingAddresses, ArrayList<ShippingAddress>
                                               shippingAddresses, CustomerDetails customerDetails) {

        this.paymentType = PAYMENT_TYPE;
        this.cimbClickPayModel = cimbClickPayModel;
        this.transactionDetails = transactionDetails;
        this.itemDetails = itemDetails;
        this.billingAddresses = billingAddresses;
        this.shippingAddresses = shippingAddresses;
        this.customerDetails = customerDetails;
    }


    public String getPaymentType() {
        return PAYMENT_TYPE;
    }

    public CIMBClickPayModel getCimbClickPayModel() {
        return cimbClickPayModel;
    }

    public void setCimbClickPayModel(CIMBClickPayModel cimbClickPayModel) {
        this.cimbClickPayModel = cimbClickPayModel;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
