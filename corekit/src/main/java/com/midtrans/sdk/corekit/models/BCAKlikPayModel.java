package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Contains information required to execute BCA KlikPay request.
 *
 * @author rakawm
 */
public class BCAKlikPayModel extends TransactionModel {
    public static final String PAYMENT_TYPE = "bca_klikpay";

    /**
     * payment_type : bca_klik_pay bca_klikpay : BCAKlikPayDescriptionModel Object
     * transaction_details : {"gross_amount":"100","order_id":"10938011"}
     */

    @SerializedName("bca_klikpay")
    private BCAKlikPayDescriptionModel bcaKlikPayDescriptionModel;

    /**
     * Default constructor for creating this class' object.
     *
     * @param bcaKlikPayDescriptionModel BCA specific transaction description.
     * @param transactionDetails         Transaction details.
     * @param itemDetails                List of item detail.
     * @param billingAddresses           List of billing address.
     * @param shippingAddresses          List of shipping address.
     * @param customerDetails            Detail of the customer.
     */
    public BCAKlikPayModel(BCAKlikPayDescriptionModel bcaKlikPayDescriptionModel,
                           TransactionDetails transactionDetails,
                           ArrayList<ItemDetails> itemDetails,
                           ArrayList<BillingAddress> billingAddresses,
                           ArrayList<ShippingAddress> shippingAddresses,
                           CustomerDetails customerDetails) {
        setBcaKlikPayDescriptionModel(bcaKlikPayDescriptionModel);
        setTransactionDetails(transactionDetails);
        this.paymentType = PAYMENT_TYPE;
        this.itemDetails = itemDetails;
        this.billingAddresses = billingAddresses;
        this.shippingAddresses = shippingAddresses;
        this.customerDetails = customerDetails;
    }

    public String getPaymentType() {
        return PAYMENT_TYPE;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public BCAKlikPayDescriptionModel getBcaKlikPayDescriptionModel() {
        return bcaKlikPayDescriptionModel;
    }

    public void setBcaKlikPayDescriptionModel(BCAKlikPayDescriptionModel bcaKlikPayDescriptionModel) {
        this.bcaKlikPayDescriptionModel = bcaKlikPayDescriptionModel;
    }
}
