package id.co.veritrans.sdk.core;

import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.PermataBankTransferResponse;
import id.co.veritrans.sdk.models.TokenDetails;
import id.co.veritrans.sdk.models.TransactionCancelResponse;
import id.co.veritrans.sdk.models.TransactionStatusResponse;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by shivam on 10/26/15.
 */
public interface VeritranceApiInterface {


    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    @POST("/charge/")
    Observable<PermataBankTransferResponse> paymentUsingPermataBank(@Header("Authorization") String authorization, PermataBankTransfer permataBankTransfer);



    //token?card_number=4811111111111114&card_cvv=123&card_exp_month=06&card_exp_year=2020&client_key=VT-client-Lre_JFh5klhfGefF
    @GET("/token/")
    Observable<TokenDetails> getToken(@Query("card_number") String cardNumber,
                                      @Query("card_cvv") String cardCVV,
                                      @Query("card_exp_month") String cardExpiryMonth,
                                      @Query("card_exp_year") String cardExpiryYear,
                                      @Query("client_key") String clientKey);




    //http://api.sandbox.veritrans.co.id/v2/10938010/cancel/
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    @POST("/{id}/cancel/")
    Observable<TransactionCancelResponse> cancelTransaction(
            @Header("Authorization") String authorization, @Path("id") String transactionId);



    //http://api.sandbox.veritrans.co.id/v2/39b690a3-d626-4577-a6ab-14e29a1c74ac/status/
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    @POST("/{id}/status/")
    Observable<TransactionStatusResponse> transactionStatus(
            @Header("Authorization") String authorization, @Path("id") String transactionId);

}
