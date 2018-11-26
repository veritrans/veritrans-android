package com.midtrans.sdk.corekit.utilities;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.TransactionRequest;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.BillingAddress;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.ShippingAddress;

public class ModelHelper {

    /*public static CustomerDetails sanitizeCustomerDetails(CustomerDetails customerDetails) {
        if (customerDetails != null) {
            if (TextUtils.isEmpty(customerDetails.getFirstName())) {
                customerDetails.setFirstName(null);
            }

            if (TextUtils.isEmpty(customerDetails.getLastName())) {
                customerDetails.setLastName(null);
            }

            if (TextUtils.isEmpty(customerDetails.getEmail())) {
                customerDetails.setEmail(null);
            }

            if (TextUtils.isEmpty(customerDetails.getPhone())) {
                customerDetails.setPhone(null);
            }

            sanitizeBillingAddress(customerDetails.getBillingAddress());
            sanitizeShippingAddress(customerDetails.getShippingAddress());
        }
        return customerDetails;
    }

    private static void sanitizeBillingAddress(BillingAddress billingAddress) {
        if (billingAddress != null) {
            if (TextUtils.isEmpty(billingAddress.getAddress())) {
                billingAddress.setAddress(null);
            }

            if (TextUtils.isEmpty(billingAddress.getFirstName())) {
                billingAddress.setFirstName(null);
            }

            if (TextUtils.isEmpty(billingAddress.getLastName())) {
                billingAddress.setLastName(null);
            }

            if (TextUtils.isEmpty(billingAddress.getCity())) {
                billingAddress.setCity(null);
            }

            if (TextUtils.isEmpty(billingAddress.getPostalCode())) {
                billingAddress.setPostalCode(null);
            }

            if (TextUtils.isEmpty(billingAddress.getPhone())) {
                billingAddress.setPhone(null);
            }

            if (TextUtils.isEmpty(billingAddress.getCountryCode())) {
                billingAddress.setCountryCode(null);
            }
        }
    }

    private static void sanitizeShippingAddress(ShippingAddress shippingAddress) {
        if (shippingAddress != null) {
            if (TextUtils.isEmpty(shippingAddress.getAddress())) {
                shippingAddress.setAddress(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getFirstName())) {
                shippingAddress.setFirstName(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getLastName())) {
                shippingAddress.setLastName(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getCity())) {
                shippingAddress.setCity(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getPostalCode())) {
                shippingAddress.setPostalCode(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getPhone())) {
                shippingAddress.setPhone(null);
            }

            if (TextUtils.isEmpty(shippingAddress.getCountryCode())) {
                shippingAddress.setCountryCode(null);
            }
        }
    }*/
}