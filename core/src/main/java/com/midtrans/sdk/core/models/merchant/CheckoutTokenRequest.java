package com.midtrans.sdk.core.models.merchant;

import com.midtrans.sdk.core.utils.Utilities;

import java.util.List;
import java.util.Map;

/**
 * Created by rakawm on 10/19/16.
 */

public class CheckoutTokenRequest {
    public String userId;
    public CreditCard creditCard;
    public List<ItemDetails> itemDetails;
    public CustomerDetails customerDetails;
    public CheckoutOrderDetails transactionDetails;
    public Expiry expiry;
    public Object custom;
    public boolean secureCreditCardPayment;

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
     * @param custom             custom map (key-value) object.
     * @return checkout token request.
     */
    public static CheckoutTokenRequest  newCompleteCheckout(String userId,
                                                           CreditCard creditCard,
                                                           List<ItemDetails> itemDetails,
                                                           CustomerDetails customerDetails,
                                                           CheckoutOrderDetails transactionDetails,
                                                            boolean secureCreditCardPayment,
                                                           Expiry expiry,
                                                           Map<String, String> custom) {
        CheckoutTokenRequest checkoutTokenRequest = new CheckoutTokenRequest();
        checkoutTokenRequest.userId = userId;
        checkoutTokenRequest.creditCard = creditCard;
        checkoutTokenRequest.itemDetails = itemDetails;
        checkoutTokenRequest.customerDetails = customerDetails;
        checkoutTokenRequest.transactionDetails = transactionDetails;
        checkoutTokenRequest.expiry = expiry;
        checkoutTokenRequest.secureCreditCardPayment = secureCreditCardPayment;
        if (custom != null && !custom.isEmpty()) {
            checkoutTokenRequest.custom = Utilities.createCustomMapObject(custom);
        }
        return checkoutTokenRequest;
    }
}
