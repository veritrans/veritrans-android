package com.midtrans.sdk.corekit.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * model class to hold information bout Indosat Dompetku api call
 *
 * Created by shivam on 11/26/15.
 */
public class IndosatDompetkuRequest {


    /**
     * payment_type : indosat_dompetku transaction_details : {"order_id":"1388q","gross_amount":100000}
     * item_details : [{"id":"1388","price":100000,"quantity":1,"name":"Mie Ayam Original"}]
     * customer_details : {"email":"obet.supriadi@gmail.com","first_name":"Obet",
     * "last_name":"Supriadi","phone":"081311874839"} indosat_dompetku : {"msisdn":"08123456789"}
     */

    @SerializedName("payment_type")
    private String paymentType;
    /**
     * order_id : 1388q gross_amount : 100000
     */

    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;
    /**
     * email : obet.supriadi@gmail.com first_name : Obet last_name : Supriadi phone : 081311874839
     */

    @SerializedName("customer_details")
    private CustomerDetailsEntity customerDetails;
    /**
     * msisdn : 08123456789
     */

    @SerializedName("indosat_dompetku")
    private IndosatDompetkuEntity indosatDompetku;
    /**
     * id : 1388 price : 100000 quantity : 1 name : Mie Ayam Original
     */

    @SerializedName("item_details")
    private List<ItemDetails> itemDetails;

    public void setCustomerDetails(CustomerDetails customerDetails, ArrayList<ShippingAddress> shippingAddresses, ArrayList<BillingAddress> billingAddresses) {

        if (customerDetails != null) {

            this.customerDetails = new CustomerDetailsEntity();
            this.customerDetails.setPhone(customerDetails.getPhone());
            this.customerDetails.setFirstName(customerDetails.getFirstName());
            this.customerDetails.setLastName(customerDetails.getLastName());
            this.customerDetails.setEmail(customerDetails.getEmail());

            /*if( shippingAddresses != null) {
                this.customerDetails.setShippingAddresses(shippingAddresses);
            }

            if( billingAddresses != null) {
                this.customerDetails.setBillingAddresses(billingAddresses);
            }*/
        }
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public CustomerDetailsEntity getCustomerDetails() {
        return customerDetails;
    }

    public IndosatDompetkuEntity getIndosatDompetku() {
        return indosatDompetku;
    }

    public void setIndosatDompetku(IndosatDompetkuEntity indosatDompetku) {
        this.indosatDompetku = indosatDompetku;
    }

    public List<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(List<ItemDetails> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public static class CustomerDetailsEntity {
        private String email;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("last_name")
        private String lastName;
        private String phone;

       /* @SerializedName("billing_address")
        ArrayList<BillingAddress> billingAddresses = new ArrayList<>();

        @SerializedName("shipping_address")
        ArrayList<ShippingAddress> shippingAddresses = new ArrayList<>();
*/

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

       /* public ArrayList<BillingAddress> getBillingAddresses() {
            return billingAddresses;
        }

        public void setBillingAddresses(ArrayList<BillingAddress> billingAddresses) {
            this.billingAddresses = billingAddresses;
        }

        public ArrayList<ShippingAddress> getShippingAddresses() {
            return shippingAddresses;
        }

        public void setShippingAddresses(ArrayList<ShippingAddress> shippingAddresses) {
            this.shippingAddresses = shippingAddresses;
        }*/


    }

    public static class IndosatDompetkuEntity {
        private String msisdn;

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }
    }


}