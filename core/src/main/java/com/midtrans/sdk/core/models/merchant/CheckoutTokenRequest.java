package com.midtrans.sdk.core.models.merchant;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rakawm on 10/19/16.
 */

public class CheckoutTokenRequest implements Serializable {
    public String userId;
    public CreditCard creditCard;
    public List<ItemDetails> itemDetails;
    public CustomerDetails customerDetails;
    public CheckoutOrderDetails transactionDetails;
    public Expiry expiry;
    public String customField1;
    public String customField2;
    public String customField3;

    /**
     * Create minimal checkout using transaction details.
     *
     * @param orderId     order identifier.
     * @param grossAmount transaction amount.
     * @return checkout token request.
     */
    public static CheckoutTokenRequest newMinimalCheckout(String orderId, int grossAmount) {
        CheckoutTokenRequest checkoutTokenRequest = new CheckoutTokenRequest();
        checkoutTokenRequest.transactionDetails = new CheckoutOrderDetails(orderId, grossAmount);
        return checkoutTokenRequest;
    }

    /**
     * Create complete checkout request.
     *
     * @param userId             user ID to map saved card token.
     * @param creditCard         credit card options.
     * @param itemDetails        item details.
     * @param customerDetails    customer details.
     * @param transactionDetails transaction details.
     * @param expiry             custom expiry details.
     * @param customField1       custom field 1.
     * @param customField2       custom field 2.
     * @param customField3       custom field 3
     * @return checkout token request.
     */
    public static CheckoutTokenRequest newCompleteCheckout(String userId,
                                                           CreditCard creditCard,
                                                           List<ItemDetails> itemDetails,
                                                           CustomerDetails customerDetails,
                                                           CheckoutOrderDetails transactionDetails,
                                                           Expiry expiry,
                                                           String customField1,
                                                           String customField2,
                                                           String customField3) {
        CheckoutTokenRequest checkoutTokenRequest = new CheckoutTokenRequest();
        checkoutTokenRequest.userId = userId;
        checkoutTokenRequest.creditCard = creditCard;
        checkoutTokenRequest.itemDetails = itemDetails;
        checkoutTokenRequest.customerDetails = customerDetails;
        checkoutTokenRequest.transactionDetails = transactionDetails;
        checkoutTokenRequest.expiry = expiry;
        checkoutTokenRequest.customField1 = customField1;
        checkoutTokenRequest.customField2 = customField2;
        checkoutTokenRequest.customField3 = customField3;
        return checkoutTokenRequest;
    }
}
