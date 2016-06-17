package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rakawm on 6/16/16.
 */
public class KlikBcaModel extends TransactionModel {

    // Payment method parameter
    public static final String PAYMENT_TYPE = "bca_klikbca";

    @SerializedName("bca_klikbca")
    private KlikBCADescriptionModel descriptionModel;

    public KlikBcaModel() {
    }

    public KlikBcaModel(KlikBCADescriptionModel klikBCADescriptionModel,
                        TransactionDetails transactionDetails,
                        ArrayList<ItemDetails> itemDetails,
                        ArrayList<BillingAddress> billingAddresses,
                        ArrayList<ShippingAddress> shippingAddresses,
                        CustomerDetails customerDetails) {
        setTransactionDetails(transactionDetails);
        setDescriptionModel(klikBCADescriptionModel);
        this.paymentType = PAYMENT_TYPE;
        this.customerDetails = customerDetails;
        this.itemDetails = itemDetails;
        this.billingAddresses = billingAddresses;
        this.shippingAddresses = shippingAddresses;
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

    public KlikBCADescriptionModel getDescriptionModel() {
        return descriptionModel;
    }

    public void setDescriptionModel(KlikBCADescriptionModel descriptionModel) {
        this.descriptionModel = descriptionModel;
    }
}
