package com.midtrans.sdk.core.api.snap;

import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bins.BankBinsResponse;
import com.midtrans.sdk.core.models.snap.card.BankPointResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentRequest;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentRequest;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by rakawm on 10/19/16.
 */

public interface SnapApi {

    /**
     * Get transaction details.
     *
     * @param checkoutToken checkout token.
     * @return transaction details
     */
    @GET("transactions/{checkout_token}")
    Call<SnapTransaction> getTransactionDetails(
            @Path("checkout_token") String checkoutToken
    );

    /**
     * Charge transaction using credit card.
     *
     * @param checkoutToken            checkout token.
     * @param creditCardPaymentRequest credit card payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<CreditCardPaymentResponse> paymentUsingCreditCard(
            @Path("checkout_token") String checkoutToken,
            @Body CreditCardPaymentRequest creditCardPaymentRequest
    );

    /**
     * Charge transaction using bank transfer via BCA.
     *
     * @param checkoutToken                 checkout token.
     * @param bcaBankTransferPaymentRequest BCA bank transfer payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<BcaBankTransferPaymentResponse> paymentUsingBcaBankTransfer(
            @Path("checkout_token") String checkoutToken,
            @Body BcaBankTransferPaymentRequest bcaBankTransferPaymentRequest
    );

    /**
     * Charge transaction using bank transfer via Permata.
     *
     * @param checkoutToken                     checkout token.
     * @param permataBankTransferPaymentRequest Permata bank transfer payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<PermataBankTransferPaymentResponse> paymentUsingPermataBankTransfer(
            @Path("checkout_token") String checkoutToken,
            @Body PermataBankTransferPaymentRequest permataBankTransferPaymentRequest
    );

    /**
     * Charge transaction using bank transfer via Mandiri.
     *
     * @param checkoutToken                     checkout token.
     * @param mandiriBankTransferPaymentRequest Mandiri bank transfer payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<MandiriBankTransferPaymentResponse> paymentUsingMandiriBankTransfer(
            @Path("checkout_token") String checkoutToken,
            @Body MandiriBankTransferPaymentRequest mandiriBankTransferPaymentRequest
    );

    /**
     * Charge payment using bank transfer via other bank.
     *
     * @param checkoutToken                   checkout token.
     * @param otherBankTransferPaymentRequest Other bank transfer payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<OtherBankTransferPaymentResponse> paymentUsingOtherBankTransfer(
            @Path("checkout_token") String checkoutToken,
            @Body OtherBankTransferPaymentRequest otherBankTransferPaymentRequest
    );

    /**
     * Charge payment using BCA KlikPay.
     *
     * @param checkoutToken            checkout token.
     * @param bcaKlikpayPaymentRequest BCA Klikpay payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<BcaKlikpayPaymentResponse> paymentUsingBcaKlikpay(
            @Path("checkout_token") String checkoutToken,
            @Body BcaKlikpayPaymentRequest bcaKlikpayPaymentRequest
    );

    /**
     * Charge payment using KlikBCA.
     *
     * @param checkoutToken         checkout token.
     * @param klikBcaPaymentRequest KlikBCA payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<KlikBcaPaymentResponse> paymentUsingKlikBca(
            @Path("checkout_token") String checkoutToken,
            @Body KlikBcaPaymentRequest klikBcaPaymentRequest
    );

    /**
     * Charge payment using Epay BRI.
     *
     * @param checkoutToken         checkout token.
     * @param epayBriPaymentRequest Epay BRI payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<EpayBriPaymentResponse> paymentUsingEpayBri(
            @Path("checkout_token") String checkoutToken,
            @Body EpayBriPaymentRequest epayBriPaymentRequest
    );

    /**
     * Charge payment using CIMB Clicks.
     *
     * @param checkoutToken            checkout token.
     * @param cimbClicksPaymentRequest CIMB Clicks payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<CimbClicksPaymentResponse> paymentUsingCimbClicks(
            @Path("checkout_token") String checkoutToken,
            @Body CimbClicksPaymentRequest cimbClicksPaymentRequest
    );

    /**
     * Charge payment using Mandiri Clickpay.
     *
     * @param checkoutToken                 checkout token.
     * @param mandiriClickpayPaymentRequest Mandiri Clickpay payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<MandiriClickpayPaymentResponse> paymentUsingMandiriClickpay(
            @Path("checkout_token") String checkoutToken,
            @Body MandiriClickpayPaymentRequest mandiriClickpayPaymentRequest
    );

    /**
     * Charge payment using Mandiri E Cash
     *
     * @param checkoutToken              checkout token.
     * @param mandiriECashPaymentRequest Mandiri E Cash payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<MandiriECashPaymentResponse> paymentUsingMandiriECash(
            @Path("checkout_token") String checkoutToken,
            @Body MandiriECashPaymentRequest mandiriECashPaymentRequest
    );

    /**
     * Charge payment using Telkomsel Cash.
     *
     * @param checkoutToken               checkout token.
     * @param telkomselCashPaymentRequest Telkomsel cash payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<TelkomselCashPaymentResponse> paymentUsingTelkomselCash(
            @Path("checkout_token") String checkoutToken,
            @Body TelkomselCashPaymentRequest telkomselCashPaymentRequest
    );

    /**
     * Charge payment using Indosat Dompetku.
     *
     * @param checkoutToken                 checkout token.
     * @param indosatDompetkuPaymentRequest Indosat Dompetku payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<IndosatDompetkuPaymentResponse> paymentUsingIndosatDompetku(
            @Path("checkout_token") String checkoutToken,
            @Body IndosatDompetkuPaymentRequest indosatDompetkuPaymentRequest
    );

    /**
     * Charge payment using XL Tunai.
     *
     * @param checkoutToken         checkout token.
     * @param xlTunaiPaymentRequest XL Tunai payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<XlTunaiPaymentResponse> paymentUsingXlTunai(
            @Path("checkout_token") String checkoutToken,
            @Body XlTunaiPaymentRequest xlTunaiPaymentRequest
    );

    /**
     * Charge payment using Indomaret.
     *
     * @param checkoutToken           checkout token.
     * @param indomaretPaymentRequest Indomaret payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<IndomaretPaymentResponse> paymentUsingIndomaret(
            @Path("checkout_token") String checkoutToken,
            @Body IndomaretPaymentRequest indomaretPaymentRequest
    );

    /**
     * Charge payment using Kioson.
     *
     * @param checkoutToken        checkout token.
     * @param kiosonPaymentRequest Kioson payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<KiosonPaymentResponse> paymentUsingKioson(
            @Path("checkout_token") String checkoutToken,
            @Body KiosonPaymentRequest kiosonPaymentRequest
    );

    /**
     * Charge payment using Gift Card.
     *
     * @param checkoutToken          checkout token.
     * @param giftCardPaymentRequest Gift Card payment details.
     * @return transaction response.
     */
    @POST("transactions/{checkout_token}/pay")
    Call<GiftCardPaymentResponse> paymentUsingGiftCard(
            @Path("checkout_token") String checkoutToken,
            @Body GiftCardPaymentRequest giftCardPaymentRequest
    );

    /**
     * Get bank bins list from Snap.
     *
     * @return bank bins list.
     */
    @GET("bank_bins")
    Call<List<BankBinsResponse>> getBankBins();

    /**
     * Delete selected card from user's account.
     */
    @DELETE("transactions/{token}/saved_tokens/{masked_card}")
    Call<Void> deleteCard(
            @Path("token") String checkoutToken,
            @Path("masked_card") String maskedCard
    );

    /**
     * Get bank point.
     *
     * @param checkoutToken checkout token.
     * @param cardToken     card token.
     * @return bank point details.
     */
    @GET("transactions/{token}/point_inquiry/{card_token}")
    Call<BankPointResponse> getBankPoint(
            @Path("token") String checkoutToken,
            @Path("card_token") String cardToken
    );
}
