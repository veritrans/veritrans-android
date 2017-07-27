package com.midtrans.sdk.uikit.models;

/**
 * Created by rakawm on 3/16/17.
 */

public class CreditCardType {
    public static final String VISA = "visa";
    public static final String MASTERCARD = "mastercard";
    public static final String JCB = "jcb";
    public static final String AMEX = "amex";

    public static final int TYPE_MASTER_VISA_JCB_AMEX = 1;
    public static final int TYPE_MASTER_VISA = 2;
    public static final int TYPE_MASTER_VISA_JCB = 3;
    public static final int TYPE_MASTER_VISA_AMEX = 4;
    public static final int TYPE_UNKNOWN = 0;

    public static final String ONE_CLICK = "one_click";
    public static final String TWO_CLICKS = "two_clicks";
}
