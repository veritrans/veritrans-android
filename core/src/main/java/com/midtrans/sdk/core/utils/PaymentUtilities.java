package com.midtrans.sdk.core.utils;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.core.environment.ProductionEnvironment;
import com.midtrans.sdk.core.environment.SandboxEnvironment;
import com.midtrans.sdk.core.environment.StagingEnvironment;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentParams;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentParams;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentParams;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentParams;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentRequest;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentParams;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentRequest;

/**
 * Created by rakawm on 1/25/17.
 */

public class PaymentUtilities {

    /**
     * Build Credit Card payment details.
     *
     * @param paymentParams   credit card payment parameters.
     * @param customerDetails customer details.
     * @return Credit Card payment request.
     */
    public static CreditCardPaymentRequest buildCreditCardPaymentRequest(CreditCardPaymentParams paymentParams,
                                                                         SnapCustomerDetails customerDetails) {
        return new CreditCardPaymentRequest(paymentParams, customerDetails);
    }

    /**
     * Build BCA Bank transfer payment details.
     *
     * @return BCA Bank Transfer payment request.
     */
    public static BcaBankTransferPaymentRequest buildBcaBankTransferPaymentRequest() {
        return new BcaBankTransferPaymentRequest(null);
    }

    /**
     * Build BCA Bank transfer payment details with customer details.
     *
     * @param customerDetails customer details.
     * @return BCA Bank Transfer payment request.
     */
    public static BcaBankTransferPaymentRequest buildBcaBankTransferPaymentRequest(SnapCustomerDetails customerDetails) {
        return new BcaBankTransferPaymentRequest(customerDetails);
    }

    /**
     * Build Permata Bank Transfer payment details.
     *
     * @return Permata Bank Transfer payment request.
     */
    public static PermataBankTransferPaymentRequest buildPermataBankTransferPaymentRequest() {
        return new PermataBankTransferPaymentRequest(null);
    }

    /**
     * Build Permata Bank Transfer payment details with customer details.
     *
     * @param customerDetails customer details.
     * @return Permata Bank Transfer payment request.
     */
    public static PermataBankTransferPaymentRequest buildPermataBankTransferPaymentRequest(SnapCustomerDetails customerDetails) {
        return new PermataBankTransferPaymentRequest(customerDetails);
    }

    /**
     * Build Mandiri Bank Transfer payment details.
     *
     * @return Mandiri Bank Transfer payment request.
     */
    public static MandiriBankTransferPaymentRequest buildMandiriBankTransferPaymentRequest() {
        return new MandiriBankTransferPaymentRequest(null);
    }

    /**
     * Build Mandiri Bank Transfer payment details with customer details.
     *
     * @param customerDetails customer details.
     * @return Mandiri Bank Transfer payment request.
     */
    public static MandiriBankTransferPaymentRequest buildMandiriBankTransferPaymentRequest(SnapCustomerDetails customerDetails) {
        return new MandiriBankTransferPaymentRequest(customerDetails);
    }

    /**
     * Build Other Bank Transfer payment details.
     *
     * @return Other Bank Transfer payment request.
     */
    public static OtherBankTransferPaymentRequest buildOtherBankTransferPaymentRequest() {
        return new OtherBankTransferPaymentRequest(null);
    }

    /**
     * Build Other Bank Transfer payment details with customer details.
     *
     * @param customerDetails customer details.
     * @return Other Bank Transfer payment request.
     */
    public static OtherBankTransferPaymentRequest buildOtherBankTransferPaymentRequest(SnapCustomerDetails customerDetails) {
        return new OtherBankTransferPaymentRequest(customerDetails);
    }

    /**
     * Build BCA KlikPay payment details.
     *
     * @return BCA KlikPay payment request.
     */
    public static BcaKlikpayPaymentRequest buildBcaKlikpayPaymentRequest() {
        return new BcaKlikpayPaymentRequest();
    }

    /**
     * Build KlikBCA payment details.
     *
     * @param klikBcaId user ID of KlikBCA account.
     * @return KlikBCA payment request.
     */
    public static KlikBcaPaymentRequest buildKlikBcaPaymentRequest(String klikBcaId) {
        return new KlikBcaPaymentRequest(new KlikBcaPaymentParams(klikBcaId));
    }

    /**
     * Build Epay BRI payment details.
     *
     * @return Epay BRI payment request.
     */
    public static EpayBriPaymentRequest buildEpayBriPaymentRequest() {
        return new EpayBriPaymentRequest();
    }

    /**
     * Build CIMB Clicks payment details.
     *
     * @return CIMB Clicks payment request.
     */
    public static CimbClicksPaymentRequest buildCimbClicksPaymentRequest() {
        return new CimbClicksPaymentRequest();
    }

    /**
     * Build Mandiri Clickpay payment details.
     *
     * @param mandiriClickpayPaymentParams Mandiri Clickpay payment parameters.
     * @return Mandiri Clickpay payment request.
     */
    public static MandiriClickpayPaymentRequest buildMandiriClickpayPaymentRequest(MandiriClickpayPaymentParams mandiriClickpayPaymentParams) {
        return new MandiriClickpayPaymentRequest(mandiriClickpayPaymentParams);
    }

    /**
     * Build Mandiri E Cash payment details.
     *
     * @return Mandiri E Cash payment request.
     */
    public static MandiriECashPaymentRequest buildMandiriECashPaymentRequest() {
        return new MandiriECashPaymentRequest();
    }

    /**
     * Build Telkomsel Cash payment details.
     *
     * @param customer customer credential.
     * @return Telkomsel Cash payment request.
     */
    public static TelkomselCashPaymentRequest buildTelkomselCashPaymentRequest(String customer) {
        return new TelkomselCashPaymentRequest(new TelkomselCashPaymentParams(customer));
    }

    /**
     * Build Indosat Dompetku payment details.
     *
     * @param msisdn customer phone number.
     * @return Indosat Dompetku payment request.
     */
    public static IndosatDompetkuPaymentRequest buildIndosatDompetkuPaymentRequest(String msisdn) {
        return new IndosatDompetkuPaymentRequest(new IndosatDompetkuPaymentParams(msisdn));
    }

    /**
     * Build XL Tunai payment details.
     *
     * @return XL Tunai payment request.
     */
    public static XlTunaiPaymentRequest buildXlTunaiPaymentRequest() {
        return new XlTunaiPaymentRequest();
    }

    /**
     * Build Indomaret payment details.
     *
     * @return Indomaret payment request.
     */
    public static IndomaretPaymentRequest buildIndomaretPaymentRequest() {
        return new IndomaretPaymentRequest();
    }

    /**
     * Build Kioson payment details.
     *
     * @return Kioson payment request.
     */
    public static KiosonPaymentRequest buildKiosonPaymentRequest() {
        return new KiosonPaymentRequest();
    }

    /**
     * Build Gift Card payment details.
     *
     * @param cardNumber gift card number.
     * @param pin        gift card pin number.
     * @return Gift Card payment request.
     */
    public static GiftCardPaymentRequest buildGiftCardPaymentRequest(String cardNumber, String pin) {
        return new GiftCardPaymentRequest(new GiftCardPaymentParams(cardNumber, pin));
    }

    /**
     * Get payment base URL base don environment.
     *
     * @param environment enviroment enum.
     * @return Payment base URL.
     */
    public static String getPaymentBaseUrl(Environment environment) {
        switch (environment) {
            case PRODUCTION:
                return ProductionEnvironment.PAYMENT_API_URL;
            case STAGING:
                return StagingEnvironment.PAYMENT_API_URL;
            case SANDBOX:
                return SandboxEnvironment.PAYMENT_API_URL;
            default:
                return SandboxEnvironment.PAYMENT_API_URL;
        }
    }

    /**
     * Get Snap base URL.
     *
     * @param environment environment enum.
     * @return Snap base URL.
     */
    public static String getSnapBaseUrl(Environment environment) {
        switch (environment) {
            case PRODUCTION:
                return ProductionEnvironment.SNAP_API_URL;
            case STAGING:
                return StagingEnvironment.SNAP_API_URL;
            case SANDBOX:
                return SandboxEnvironment.SNAP_API_URL;
            default:
                return SandboxEnvironment.SNAP_API_URL;
        }
    }
}
