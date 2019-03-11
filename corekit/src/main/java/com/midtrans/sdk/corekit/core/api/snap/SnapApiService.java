package com.midtrans.sdk.corekit.core.api.snap;

import com.midtrans.sdk.corekit.core.api.snap.model.bins.BankBinsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.BasePaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.PaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.telkomsel.TelkomselCashPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AlfamartPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikPayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BniBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CreditCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriBillResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PermataBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.TelkomselCashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.payment.PaymentStatusResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.point.PointResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SnapApiService {

    String PAYMENT_INFO = "v1/transactions/{snap_token}";
    String PAYMENT_PAY = "v1/transactions/{snap_token}/pay";
    String BANK_POINT = "v1/transactions/{snap_token}/point_inquiry/{card_token}";
    String BANK_BINS = "v1/bank_bins";
    String DELETE_CARD = "v1/transactions/{snap_token}/saved_tokens/{masked_card}";

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
    Call<BcaBankTransferReponse> paymentBankTransferBca(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BniBankTransferResponse> paymentBankTransferBni(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<PermataBankTransferResponse> paymentBankTransferPermata(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<MandiriBillResponse> paymentBankTransferMandiriBill(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<ResponseBody> paymentBankTransferOther(
            @Path("snap_token") String snapToken,
            @Body PaymentRequest paymentRequest
    );

    /**
     * Charge payment using Mandiri Ecash.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<MandiriEcashResponse> paymentMandiriEcash(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using CIMB Clicks.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<CimbClicksResponse> paymentCimbClicks(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using Akulaku.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<AkulakuResponse> paymentAkulaku(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using Indomaret.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<IndomaretPaymentResponse> paymentIndomaret(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using Indomaret.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<AlfamartPaymentResponse> paymentAlfamart(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using BRI Epay.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BriEpayPaymentResponse> paymentBriEpay(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using Klik BCA.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<KlikBcaResponse> paymentKlikBca(
            @Path("snap_token") String snapToken,
            @Body KlikBcaPaymentRequest paymentRequest
    );


    /**
     * Charge payment using BRI Epay.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<MandiriClickpayResponse> paymentMandiriClickpay(
            @Path("snap_token") String snapToken,
            @Body MandiriClickpayPaymentRequest paymentRequest
    );


    /**
     * Charge payment using BCA Klik Pay.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<BcaKlikPayResponse> paymentBcaClickPay(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest paymentRequest);

    /**
     * Charge payment using GoPay
     */
    @POST(PAYMENT_PAY)
    Call<GopayResponse> paymentUsingGoPay(@Path("snap_token") String snapToken,
                                          @Body BasePaymentRequest paymentRequest
    );

    /**
     * Charge payment using Telkomsel Cash
     */
    @POST(PAYMENT_PAY)
    Call<TelkomselCashResponse> paymentUsingTelkomselCash(
            @Path("snap_token") String snapToken,
            @Body TelkomselCashPaymentRequest paymentRequest
    );

    /**
     * Charge payment using credit card token.
     *
     * @param creditCardPaymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<CreditCardResponse> paymentUsingCreditCard(
            @Path("snap_token") String snapToken,
            @Body CreditCardPaymentRequest creditCardPaymentRequest
    );

    /**
     * Charge payment using credit card token.
     *
     * @param basePaymentRequest Payment Request Details.
     */
    @POST(PAYMENT_PAY)
    Call<DanamonOnlineResponse> paymentUsingDanamonOnline(
            @Path("snap_token") String snapToken,
            @Body BasePaymentRequest basePaymentRequest
    );

    /**
     * Get bins of credit card
     */
    @GET(BANK_BINS)
    Call<List<BankBinsResponse>> getBankBins();

    /**
     * Get Banks Points from snap backend.
     *
     * @param snapToken snap token
     * @param cardToken credit card token
     */
    @GET(BANK_POINT)
    Call<PointResponse> getBanksPoint(
            @Path("snap_token") String snapToken,
            @Path("card_token") String cardToken
    );

    @DELETE(DELETE_CARD)
    Call<Void> deleteCard(
            @Path("snap_token") String snapToken,
            @Path("masked_card") String maskedCard
    );

    /**
     * Get Transaction Status.
     *
     * @param snapToken snap token
     */
    @GET("v1/transactions/{snap_token}/status")
    Call<PaymentStatusResponse> getPaymentStatus(
            @Path("snap_token") String snapToken
    );

}