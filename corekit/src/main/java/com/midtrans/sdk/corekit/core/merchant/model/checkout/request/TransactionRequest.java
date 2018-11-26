package com.midtrans.sdk.corekit.core.merchant.model.checkout.request;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.mandatory.TransactionDetails;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.BillInfoModel;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.ExpiryModel;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.GopayDeepLink;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.ItemDetails;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.SnapPromo;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.banktransfer.BankTransferRequestModel;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.banktransfer.BcaBankTransferRequestModel;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.utilities.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionRequest implements Serializable {

    private static TransactionRequest SINGLETON_INSTANCE = null;

    /**
     * contains details about transaction.
     * Mandatory
     */
    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails = null;

    /**
     * contains details about customer.
     * Mandatory
     */
    @SerializedName("customer_details")
    private CustomerDetails customerDetails = null;

    /**
     * List of purchased items.
     * Mandatory
     */
    @SerializedName("item_details")
    private ArrayList<ItemDetails> itemDetails = new ArrayList<>();

    /**
     * List of enable payment.
     * Optional
     */
    @SerializedName("enabled_payments")
    private List<String> enabledPayments = new ArrayList<>();

    /**
     * Contains user app deeplink for merchant app.
     * Optional
     */
    @SerializedName("gopay")
    private GopayDeepLink gopayDeepLink = null;

    /**
     * Set custom expiry of token that will be created
     */
    @SerializedName("expiry")
    private ExpiryModel expiry = null;

    /**
     * Contain maximum 3 of custom field for user.
     * Optional
     */
    @SerializedName("custom_field1")
    private String customField1 = null;
    @SerializedName("custom_field2")
    private String customField2 = null;
    @SerializedName("custom_field3")
    private String customField3 = null;

    /**
     * It contains an extra information that you want to display on bill.
     * Optional
     */
    private BillInfoModel billInfoModel = null;

    /**
     * It's user id.
     * Optional
     */
    @SerializedName("user_id")
    private String userId = null;

    /**
     * It contains an promo information.
     * Optional
     */
    private SnapPromo promo = null;

    /**
     * Contain Permata VA details.
     * Specific for bank transfer Permata VA
     */
    @SerializedName("permata_va")
    private BankTransferRequestModel permataVa = null;

    /**
     * Contain BCA VA details.
     * Specific for bank transfer BCA VA
     */
    @SerializedName("bca_va")
    private BcaBankTransferRequestModel bcaVa = null;

    /**
     * Contain BNI VA details.
     * Specific for bank transfer BNI VA
     */
    @SerializedName("bni_va")
    private BankTransferRequestModel bniVa = null;

    /**
     * Contain credit card details.
     * Specific for credit card
     */
    @SerializedName("credit_card")
    private CreditCard creditCard = null;

    /**
     * helps to identify whether to use ui or not.
     */
    private Map<String, String> customObject = null;


    private TransactionRequest(String orderId,
                               double grossAmount,
                               String currency,
                               String gopayDeepLink,
                               CreditCard creditCard,
                               CustomerDetails customerDetails) {
        this.transactionDetails = new TransactionDetails(orderId, grossAmount, currency);
        this.gopayDeepLink = new GopayDeepLink(gopayDeepLink);
        this.creditCard = creditCard;
    }

    public static Builder builder(String orderId,
                                  double grossAmount) {
        return new Builder(orderId, grossAmount);
    }

    public synchronized static TransactionRequest getInstance() {
        if (SINGLETON_INSTANCE == null) {
            String message = "Transaction isn't build. Please use Transaction.builder() to initialize and build request object.";
            RuntimeException runtimeException = new RuntimeException(message);
            Logger.error(message, runtimeException);
        }
        return SINGLETON_INSTANCE;
    }

    public static class Builder {
        private String orderId;
        private double grossAmount;

        private String currency;
        private String gopayDeepLink;
        private CreditCard creditCard;
        private CustomerDetails customerDetails;

        private Builder(
                String orderId,
                double grossAmount) {
            this.orderId = orderId;
            this.grossAmount = grossAmount;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setGopayCallbackDeepLink(String gopayDeepLink) {
            this.gopayDeepLink = gopayDeepLink;
            return this;
        }

        public Builder setCreditCard(CreditCard creditCard) {
            this.creditCard = creditCard;
            return this;
        }

        public Builder setCustomerDetails(CustomerDetails customerDetails) {
            this.customerDetails = customerDetails;
            return this;
        }

        public TransactionRequest build() {
            if (isValidData(orderId, grossAmount)) {
                SINGLETON_INSTANCE = new TransactionRequest(orderId,
                        grossAmount,
                        currency,
                        gopayDeepLink,
                        creditCard,
                        customerDetails);
                return SINGLETON_INSTANCE;
            } else {
                Logger.error("Already performing an transaction");
            }
            return null;
        }

        private boolean isValidData(String orderId,
                                    double grossAmount) {
            if (orderId == null) {
                String message = "Please set order id";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
            }

            if (grossAmount == 0) {
                String message = "Please set gross amount";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
            }
            return true;
        }
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public GopayDeepLink getGopayDeepLink() {
        return gopayDeepLink;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }
}