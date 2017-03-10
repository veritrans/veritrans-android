package com.midtrans.sdk.ui.constants;

/**
 * Created by ziahaqi on 2/26/17.
 */

public class Payment {

    public static class Type {
        /**
         * Payment types for charging to Snap API
         * <p>
         * VA(Virtual Account) equeals to bank transfer
         * E_CHANNEL equals to Mandiri Bill
         */

        public static final String CREDIT_CARD = "credit_card";
        public static final String PERMATA_VA = "permata_va";
        public static final String BCA_VA = "bca_va";
        public static final String OTHER_VA = "other_va";
        public static final String KLIK_BCA = "bca_klikbca";
        public static final String BCA_KLIKPAY = "bca_klikpay";
        public static final String CIMB_CLICKS = "cimb_clicks";
        public static final String MANDIRI_CLICKPAY = "mandiri_clickpay";
        public static final String BRI_EPAY = "bri_epay";
        public static final String TELKOMSEL_CASH = "telkomsel_cash";
        public static final String XL_TUNAI = "xl_tunai";
        public static final String BBM_MONEY = "bbm_money";
        public static final String MANDIRI_ECASH = "mandiri_ecash";
        public static final String E_CHANNEL = "echannel";
        public static final String INDOMARET = "indomaret";
        public static final String INDOSAT_DOMPETKU = "indosat_dompetku";
        public static final String KIOSON = "kioson";
        public static final String GCI = "gci";

        //custom payment type
        public static final String BANK_TRANSFER = "bank_transfer";
    }


    /***
     * payment methods categories
     */
    public class Category {
        public static final String CREDIT_CARD = "bank_transfer";
        public static final String BANK_TRANSFER = "bank_transfer";
        public static final String E_WALLET = "e_wallet";
        public static final String CONVENIENCE_STORE = "bank_transfer";
        public static final String DIRECT_DEBIT = "direct_debit";
    }

    public class CreditCard {
        public static final String ONE_CLICK = "one_click";
        public static final String TWO_CLICKS = "two_clicks";
    }

    public class Param {
        public static final String PAYMENT_RESULT = "payment_result";
        public static final String PAYMENT_TYPE = "payment_type";
    }

    public class Status {
        public static final String CODE_200 = "200";
        public static final String CODE_201 = "201";
        public static final String CODE_400 = "400";

        public static final String SUCCESS = "SUCCESS";
        public static final String PENDING = "PENDING";
        public static final String FAILED = "FAILED";
        public static final String INVALID = "INVALID";
        public static final String ERROR = "ERROR";
        public static final String SETTLEMENT = "SETTLEMENT";
        public static final String CHALLENGE = "CHALLENGE";
        public static final String DENY = "DENY";
    }
}
