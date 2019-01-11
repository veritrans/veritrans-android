package com.midtrans.sdk.corekit.core.api.snap;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.BasePaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.PaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.gopay.GopayPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.telkomsel.TelkomselCashPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BankTransferVaBcaPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BankTransferVaBniPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BankTransferVaOtherPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BankTransferVaPermataPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.TelkomselCashPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SnapApiService {

    String PAYMENT_INFO = "v1/transactions/{snap_token}";
    String PAYMENT_PAY = "v1/transactions/{snap_token}/pay";
    String BANK_POINT = "v1/transactions/{snap_token}/point_inquiry/{card_token}";

    /**
     * Get transaction options using Snap with snap token.
     *
     * @param snapToken snap token
     */
    @GET(PAYMENT_INFO)
    Call<PaymentInfoResponse> getTransactionOptions(
            @Path("snap_token") String snapToken
    );

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BankTransferVaBcaPaymentResponse> paymentBankTransferBca(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BankTransferVaBniPaymentResponse> paymentBankTransferBni(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BankTransferVaPermataPaymentResponse> paymentBankTransferPermata(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BankTransferVaOtherPaymentResponse> paymentBankTransferOther(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using Mandiri Ecash.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<MandiriEcashPaymentResponse> paymentMandiriEcash(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using CIMB Clicks.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BasePaymentResponse> paymentCimbClicks(@Path("snap_token") String snapToken,
                                                @Body BasePaymentRequest paymentRequest);

    /**
     * Charge payment using Akulaku.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BasePaymentResponse> paymentAkulaku(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using Indomaret.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BasePaymentResponse> paymentIndomaret(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using BRI Epay.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BasePaymentResponse> paymentBriEpay(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using Klik BCA.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BasePaymentResponse> paymentKlikBca(
            @Path("snap_token") String snapToken,
            @Body KlikBcaPaymentRequest paymentRequest
    );


    /**
     * Charge payment using BRI Epay.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BasePaymentResponse> paymentMandiriClickpay(
            @Path("snap_token") String snapToken,
            @Body MandiriClickpayPaymentRequest paymentRequest
    );


    /**
     * Charge payment using BCA Klik Pay.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BasePaymentResponse> paymentBcaClickPay(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest);

    /**
     * Charge payment using GoPay
     */
    @POST(PAYMENT_PAY)
    Call<GopayPaymentResponse> paymentUsingGoPay(@Path("snap_token") String snapToken,
                                                 @Body GopayPaymentRequest paymentRequest
    );

    /**
     * Charge payment using Telkomsel Cash
     */
    @POST(PAYMENT_PAY)
    Call<TelkomselCashPaymentResponse> paymentUsingTelkomselCash(
            @Path("snap_token") String snapToken,
            @Body TelkomselCashPaymentRequest paymentRequest
    );

    /**
     * Charge payment using credit card token.
     *
     * @param creditCardPaymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BasePaymentResponse> paymentUsingCreditCard(
            @Path("snap_token") String snapToken,
            @Body CreditCardPaymentRequest creditCardPaymentRequest
    );

    /**
     * Get Banks Points from snap backend.
     *
     * @param snapToken snap token
     * @param cardToken credit card token
     */
    @GET(BANK_POINT)
    Call<BasePaymentResponse> getBanksPoint(
            @Path("snap_token") String snapToken,
            @Path("card_token") String cardToken
    );

}