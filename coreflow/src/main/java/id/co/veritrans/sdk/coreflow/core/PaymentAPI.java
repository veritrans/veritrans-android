package id.co.veritrans.sdk.coreflow.core;

import id.co.veritrans.sdk.coreflow.models.AuthModel;
import id.co.veritrans.sdk.coreflow.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.coreflow.models.BCABankTransfer;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayModel;
import id.co.veritrans.sdk.coreflow.models.CIMBClickPayModel;
import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;
import id.co.veritrans.sdk.coreflow.models.CardResponse;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.DeleteCardResponse;
import id.co.veritrans.sdk.coreflow.models.EpayBriTransfer;
import id.co.veritrans.sdk.coreflow.models.GetOffersResponseModel;
import id.co.veritrans.sdk.coreflow.models.IndomaretRequestModel;
import id.co.veritrans.sdk.coreflow.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.coreflow.models.KlikBCAModel;
import id.co.veritrans.sdk.coreflow.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.MandiriECashModel;
import id.co.veritrans.sdk.coreflow.models.PermataBankTransfer;
import id.co.veritrans.sdk.coreflow.models.RegisterCardResponse;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionCancelResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionStatusResponse;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface PaymentAPI {

    //token?card_number=4811111111111114&card_cvv=123&card_exp_month=06&card_exp_year=2020
    // &client_key=VT-client-Lre_JFh5klhfGefF
   /* @GET("/token/")
    Observable<TokenDetailsResponse> getToken(@Query("card_number") String cardNumber,
                                              @Query("card_cvv") int cardCVV,
                                              @Query("card_exp_month") int cardExpiryMonth,
                                              @Query("card_exp_year") int cardExpiryYear,
                                              @Query("client_key") String clientKey,
                                              @Query("gross_amount") double grossAmount,
                                              @Query("bank") String bank
    );*/
































}