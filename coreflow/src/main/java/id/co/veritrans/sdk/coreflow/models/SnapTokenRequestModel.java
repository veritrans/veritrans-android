package id.co.veritrans.sdk.coreflow.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 7/19/16.
 */
public class SnapTokenRequestModel {

    @SerializedName("customer_details")
    private final CustomerDetails costumerDetails;
    @SerializedName("billing_address")

    private final ArrayList<BillingAddress> billingAddressArrayList;

    @SerializedName("expiry")
    private final ExpiryModel expiry;

    @SerializedName("shipping_address")
    private ArrayList<ShippingAddress> shippingAdressArrayList;

    @SerializedName("item_details")
    private  ArrayList<ItemDetails> itemDetails;


    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;

    public SnapTokenRequestModel(TransactionDetails transactionDetails, ArrayList<BillingAddress> billingAddressArrayList, ArrayList<ShippingAddress> shippingAddressArrayList, ArrayList<ItemDetails> itemDetails,
                                 CustomerDetails customerDetails, ExpiryModel expiry) {
        this.transactionDetails = transactionDetails;
        this.billingAddressArrayList = billingAddressArrayList;
        this.shippingAdressArrayList = shippingAddressArrayList;
        this.itemDetails = itemDetails;
        this.costumerDetails = customerDetails;
        this.expiry = expiry;

    }

    public CustomerDetails getCostumerDetails() {
        return costumerDetails;
    }

    public ArrayList<BillingAddress> getBillingAddressArrayList() {
        return billingAddressArrayList;
    }

    public ExpiryModel getExpiry() {
        return expiry;
    }

    public ArrayList<ShippingAddress> getShippingAdressArrayList() {
        return shippingAdressArrayList;
    }

    public void setShippingAdressArrayList(ArrayList<ShippingAddress> shippingAdressArrayList) {
        this.shippingAdressArrayList = shippingAdressArrayList;
    }

    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ArrayList<ItemDetails> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String getString(){
        String json = "";
        try {
            Gson gson = new Gson();
            json = gson.toJson(this);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return json;
    }

}
