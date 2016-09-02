package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.util.Collections;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.models.AuthModel;
import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;
import id.co.veritrans.sdk.coreflow.models.CardResponse;
import id.co.veritrans.sdk.coreflow.models.DeleteCardResponse;
import id.co.veritrans.sdk.coreflow.models.GetOffersResponseModel;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;
import id.co.veritrans.sdk.coreflow.transactionmanager.TransactionMangerMain;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by ziahaqi on 30/06/2016.
 */
public class TransactionManagerCardsTest extends TransactionMangerMain {

//    @Captor
//    private ArgumentCaptor<Callback<CardRegistrationResponse>> callbackArgumentCaptor;
//    @Captor
//    private ArgumentCaptor<String> callbackArgumentCaptorCardNumber;
//    @Captor
//    private ArgumentCaptor<String> callbackArgumentCaptorCardCVV;
//    @Captor
//    private ArgumentCaptor<String> callbackArgumentCaptorCardYear;
//    @Captor
//    private ArgumentCaptor<String> callbackArgumentCaptorCardMonth;
//    @Captor
//    private ArgumentCaptor<String> callbackArgumentCaptorCardKey;
//
//    @Captor
//    ArgumentCaptor<VeritransBus> captor = ArgumentCaptor
//            .forClass(VeritransBus.class);
//    private String mAuthToken = "VT-1dqwd34dwed23e2dw";
//
//    @Test
//    public void testCardRegistration_whenResponseSuccess() throws Exception {
//        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
//                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));
//        CardRegistrationResponse registrationResponse = new CardRegistrationResponse();
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//
//        //registration success
//        registrationResponse.setStatusCode("200");
//        eventBustImplementSample.cardRegistration(veritransRestAPIMock, CARD_NUMBER, CARD_CVV, CARD_EXP_MONTH,
//                CARD_EXP_YEAR, mAuthToken);
//        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).registerCard(callbackArgumentCaptorCardNumber.capture(),
//                callbackArgumentCaptorCardCVV.capture(),
//                callbackArgumentCaptorCardMonth.capture(),
//                callbackArgumentCaptorCardYear.capture(),
//                callbackArgumentCaptorCardKey.capture(),
//                callbackArgumentCaptor.capture());
//        callbackArgumentCaptor.getValue().success(registrationResponse, response);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onCardRegistrationSuccess();
//    }
//
//    @Test
//    public void testCardRegistration_whenResponseNull() throws Exception {
//        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
//                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));
//        CardRegistrationResponse registrationResponse = null;
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//
//        //retrofitResponse success but transacation not success
//        eventBustImplementSample.cardRegistration(veritransRestAPIMock, CARD_NUMBER, CARD_CVV, CARD_EXP_MONTH,
//                CARD_EXP_YEAR, mAuthToken);
//        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).registerCard(callbackArgumentCaptorCardNumber.capture(),
//                callbackArgumentCaptorCardCVV.capture(),
//                callbackArgumentCaptorCardMonth.capture(),
//                callbackArgumentCaptorCardYear.capture(),
//                callbackArgumentCaptorCardKey.capture(),
//                callbackArgumentCaptor.capture());
//        callbackArgumentCaptor.getValue().success(registrationResponse, response);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void testCardRegistration_whenResponseSuccess_codeNot200() throws Exception {
//        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
//                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));
//        CardRegistrationResponse registrationResponse = new CardRegistrationResponse();
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//
//        registrationResponse.setStatusCode("212");
//
//        //retrofitResponse success but transacation not success
//        eventBustImplementSample.cardRegistration(veritransRestAPIMock, CARD_NUMBER, CARD_CVV, CARD_EXP_MONTH,
//                CARD_EXP_YEAR, mAuthToken);
//        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).registerCard(callbackArgumentCaptorCardNumber.capture(),
//                callbackArgumentCaptorCardCVV.capture(),
//                callbackArgumentCaptorCardMonth.capture(),
//                callbackArgumentCaptorCardYear.capture(),
//                callbackArgumentCaptorCardKey.capture(),
//                callbackArgumentCaptor.capture());
//        callbackArgumentCaptor.getValue().success(registrationResponse, response);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onCardRegistrationFailed();
//    }
//
//
//    @Test
//    public void testCardRegistrationError_whenValidSSL() throws Exception {
//        CardRegistrationResponse registrationResponse = new CardRegistrationResponse();
//        registrationResponse.setStatusCode("400");
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//
//        eventBustImplementSample.cardRegistration(veritransRestAPIMock, CARD_NUMBER, CARD_CVV, CARD_EXP_MONTH,
//                CARD_EXP_YEAR, mAuthToken);
//        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).registerCard(callbackArgumentCaptorCardNumber.capture(),
//                callbackArgumentCaptorCardCVV.capture(),
//                callbackArgumentCaptorCardMonth.capture(),
//                callbackArgumentCaptorCardYear.capture(),
//                callbackArgumentCaptorCardKey.capture(),
//                callbackArgumentCaptor.capture());
//        callbackArgumentCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//
//    }
//
//    @Test
//    public void testCardRegistrationError_whenInValidSSL() throws Exception {
//        CardRegistrationResponse registrationResponse = new CardRegistrationResponse();
//        registrationResponse.setStatusCode("400");
//        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
//                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//
//        eventBustImplementSample.cardRegistration(veritransRestAPIMock, CARD_NUMBER, CARD_CVV, CARD_EXP_MONTH,
//                CARD_EXP_YEAR, mAuthToken);
//        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).registerCard(callbackArgumentCaptorCardNumber.capture(),
//                callbackArgumentCaptorCardCVV.capture(),
//                callbackArgumentCaptorCardMonth.capture(),
//                callbackArgumentCaptorCardYear.capture(),
//                callbackArgumentCaptorCardKey.capture(),
//                callbackArgumentCaptor.capture());
//        callbackArgumentCaptor.getValue().failure(retrofitErrorMock);
//
//        //when certificate invalid
//        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
//        callbackArgumentCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
//
//    }
//
//    /*
//     * delete card
//     */
//
//    @Captor
//    private ArgumentCaptor<String> xauthCaptor;
//    @Captor
//    private ArgumentCaptor<Callback<DeleteCardResponse>> responseCallbackCaptor;
//    @Captor
//    private ArgumentCaptor<String> savedTokenIdCaptor;
//    @Test
//    public void testPaymentDeleteCard_whentokennull() throws Exception {
//        SaveCardRequest cardRequest = new SaveCardRequest();
//        cardRequest.setSavedTokenId(mToken);
//        DeleteCardResponse deleteCardResponse = new DeleteCardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.deleteCard(merchantRestAPIMock, cardRequest, null);
//
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void testPaymentDeleteCardSuccess_whenResponseNotNull() throws Exception {
//        SaveCardRequest cardRequest = new SaveCardRequest();
//        cardRequest.setSavedTokenId(mToken);
//        DeleteCardResponse deleteCardResponse = new DeleteCardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.deleteCard(merchantRestAPIMock, cardRequest, mToken);
//
//        Mockito.verify(merchantRestAPIMock).deleteCard(xauthCaptor.capture(), savedTokenIdCaptor.capture(), responseCallbackCaptor.capture());
//
//        //response code 200 /201
//        responseCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onDeleteCardSuccessEvent();
//
//    }
//
//    @Test
//    public void testPaymentDeleteCardSuccess_whenResponseNotNull_codenot200() throws Exception {
//        SaveCardRequest cardRequest = new SaveCardRequest();
//        cardRequest.setSavedTokenId(mToken);
//        DeleteCardResponse deleteCardResponse = new DeleteCardResponse();
//        deleteCardResponse.setCode(212);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.deleteCard(merchantRestAPIMock, cardRequest, mToken);
//
//        //response not code 200 /201
//        deleteCardResponse.setCode(300);
//        Mockito.verify(merchantRestAPIMock).deleteCard(xauthCaptor.capture(), savedTokenIdCaptor.capture(), responseCallbackCaptor.capture());
//        responseCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onDeleteCardFailedEvent();
//    }
//
//
//
//    @Test
//    public void testPaymentDeleteCardSuccess_whenResponseNull() throws Exception {
//        SaveCardRequest cardRequest = new SaveCardRequest();
//        cardRequest.setSavedTokenId(mToken);
//        DeleteCardResponse deleteCardResponse = null;
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.deleteCard(merchantRestAPIMock, cardRequest, mToken);
//
//        Mockito.verify(merchantRestAPIMock).deleteCard(xauthCaptor.capture(), savedTokenIdCaptor.capture(), responseCallbackCaptor.capture());
//
//
//        responseCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//    }
//
//
//    @Test
//    public void testPaymentDeleteCardError_whenvalidSSL() throws Exception {
//        SaveCardRequest cardRequest = new SaveCardRequest();
//        cardRequest.setSavedTokenId(mToken);
//        DeleteCardResponse deleteCardResponse = new DeleteCardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.deleteCard(merchantRestAPIMock, cardRequest, mToken);
//
//        Mockito.verify(merchantRestAPIMock).deleteCard(xauthCaptor.capture(), savedTokenIdCaptor.capture(), responseCallbackCaptor.capture());
//
//        //when valid certification
//        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//
//    }
//
//    @Test
//    public void testPaymentDeleteCardError_whenInvalidSSL() throws Exception {
//        SaveCardRequest cardRequest = new SaveCardRequest();
//        cardRequest.setSavedTokenId(mToken);
//        DeleteCardResponse deleteCardResponse = new DeleteCardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.deleteCard(merchantRestAPIMock, cardRequest, mToken);
//        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
//
//        Mockito.verify(merchantRestAPIMock).deleteCard(xauthCaptor.capture(), savedTokenIdCaptor.capture(), responseCallbackCaptor.capture());
//        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
//    }
//
//
//    /*
//     * getAuthenticationToken
//     */
//
//
//    @Captor
//    private ArgumentCaptor<Callback<AuthModel>> callbackTokenArgumentCaptor;
//    @Captor
//    private ArgumentCaptor<String> cardNumberCaptor;
//    @Captor
//    private ArgumentCaptor<String> cardCVVCaptor;
//    @Captor
//    private ArgumentCaptor<String> cardExpMonthCaptor;
//    @Captor
//    private ArgumentCaptor<String> cardExpYearCaptor;
//    @Captor
//    private ArgumentCaptor<String> clientKeyCaptor;
//    @Captor
//    private ArgumentCaptor<String> bankCaptor;
//    @Captor
//    private ArgumentCaptor<Boolean> instalmentCaptor;
//    @Captor
//    private ArgumentCaptor<String> instalmentTermCaptor;
//    @Captor
//    private ArgumentCaptor<Boolean> scureCaptor;
//    @Captor
//    private ArgumentCaptor<Boolean> twoClickCaptor;
//    @Captor
//    private ArgumentCaptor<Double> grossAmountCaptor;
//
//    @Captor
//    private ArgumentCaptor<String> tokenIdCaptor;
//
//
//
//    @Test
//    public void testGetAuthenticationTokenSuccess(){
//        AuthModel model= new AuthModel();
//        model.setxAuth("481111-1114-c4623fb5-5bfe-4b58-83c5-15794a10239e");
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getAuthenticationToken(merchantRestAPIMock);
//
//        Mockito.verify(merchantRestAPIMock, Mockito.times(1)).getAuthenticationToken(callbackTokenArgumentCaptor.capture());
//
//        callbackTokenArgumentCaptor.getValue().success(model, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onAuthenticationEvent();
//
//    }
//
//    @Test
//    public void testGetAuthenticationTokenError_whenValidSSL(){
//        AuthModel model= new AuthModel();
//        model.setxAuth("481111-1114-c4623fb5-5bfe-4b58-83c5-15794a10239e");
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getAuthenticationToken(merchantRestAPIMock);
//
//        Mockito.verify(merchantRestAPIMock, Mockito.times(1)).getAuthenticationToken(callbackTokenArgumentCaptor.capture());
//
//
//        //when valid certification
//        callbackTokenArgumentCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//
//
//    }
//
//    @Test
//    public void testGetAuthenticationTokenError_whenInvalidSSL(){
//        AuthModel model= new AuthModel();
//        model.setxAuth("481111-1114-c4623fb5-5bfe-4b58-83c5-15794a10239e");
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getAuthenticationToken(merchantRestAPIMock);
//
//        Mockito.verify(merchantRestAPIMock, Mockito.times(1)).getAuthenticationToken(callbackTokenArgumentCaptor.capture());
//        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
//        callbackTokenArgumentCaptor.getValue().failure(retrofitErrorMock);
//
//        // when invalid certification
//        Assert.assertNotNull(mSslHandshakeException);
//
//    }
//
//    /*
//     * get cards
//     */
//
//
//    @Captor
//    private ArgumentCaptor<Callback<CardResponse>> responseGeCardCallbackCaptor;
//
//    @Test
//    public void testPaymentGetCardsCard_whenTokenNull() throws Exception {
//
//        CardResponse deleteCardResponse = new CardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getCards(merchantRestAPIMock, null);
//
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//    }
//
//
//    @Test
//    public void testPaymentGetCardsCardSuccess_whenResponseNotNull() throws Exception {
//
//        CardResponse deleteCardResponse = new CardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getCards(merchantRestAPIMock, mToken);
//
//        Mockito.verify(merchantRestAPIMock).getCard(xauthCaptor.capture(),  responseGeCardCallbackCaptor.capture());
//
//        //response code 200 /201
//        responseGeCardCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGetCardSuccess();
//
//    }
//
//    @Test
//    public void testPaymentGetCardsCardSuccess_whenResponseNotNull_codeNot200() throws Exception {
//
//        CardResponse deleteCardResponse = new CardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getCards(merchantRestAPIMock, mToken);
//        //response not code 200 /201
//        deleteCardResponse.setCode(300);
//        Mockito.verify(merchantRestAPIMock).getCard(xauthCaptor.capture(), responseGeCardCallbackCaptor.capture());
//        responseGeCardCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGetCardFailed();
//    }
//
//
//    @Test
//    public void testPaymentGetCardsCardSuccess_whenResponseNull() throws Exception {
//
//        CardResponse cardResponse = null;
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getCards(merchantRestAPIMock, mToken);
//
//        Mockito.verify(merchantRestAPIMock).getCard(xauthCaptor.capture(),  responseGeCardCallbackCaptor.capture());
//
//        responseGeCardCallbackCaptor.getValue().success(cardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//    }
//
//
//    @Test
//    public void testPaymentGetCardsCardError() throws Exception {
//
//        CardResponse cardResponse = null;
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getCards(merchantRestAPIMock, mToken);
//
//        Mockito.verify(merchantRestAPIMock).getCard(xauthCaptor.capture(),  responseGeCardCallbackCaptor.capture());
//        //when valid certification
//        responseGeCardCallbackCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//
//        // when invalid certification
//        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
//        Assert.assertNotNull(mSslHandshakeException);
//    }
//
//    @Test
//    public void testPaymentGetCardsCardError_whenInvalidSSL() throws Exception {
//
//        CardResponse cardResponse = null;
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getCards(merchantRestAPIMock, mToken);
//
//        Mockito.verify(merchantRestAPIMock).getCard(xauthCaptor.capture(),  responseGeCardCallbackCaptor.capture());
//        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
//
//        responseGeCardCallbackCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
//
//    }
//
//    /*
//    * getoffer()
//     */
//
//    @Captor
//    private ArgumentCaptor<Callback<GetOffersResponseModel>> responseOfferCallbackCaptor;
//    @Captor
//    private ArgumentCaptor<GetOffersResponseModel> getOfferModelCaptor;
//
//    @Test
//    public void testGetOffersSuccess_whenTokenNull() throws Exception {
//        GetOffersResponseModel responseModel = new GetOffersResponseModel();
//        responseModel.setCode(200);
//        responseModel.setMessage("success");
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getOffers(merchantRestAPIMock, null);
//
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//    }
//    /*
//     * !!!!!!!!!!!!!!!!!!
//     */
//
//    @Test
//    public void testGetOffersSuccess_whenResponseNotNull() throws Exception {
//        Mockito.when(context.getString(R.string.success)).thenReturn("success");
//        GetOffersResponseModel responseModel = new GetOffersResponseModel();
//        responseModel.setCode(200);
//        responseModel.setMessage("success");
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getOffers(merchantRestAPIMock, mToken);
//
//        Mockito.verify(merchantRestAPIMock).getOffers(xauthCaptor.capture(), responseOfferCallbackCaptor.capture());
//
//        //response message success
//        responseOfferCallbackCaptor.getValue().success(responseModel, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGetOfferSuccesEvent();
//
//    }
//
//    @Test
//    public void testGetOffersSuccess_whenResponseNotNull_codeNotSuccess() throws Exception {
//        GetOffersResponseModel responseModel = new GetOffersResponseModel();
//        responseModel.setCode(200);
//        responseModel.setMessage("failed");
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getOffers(merchantRestAPIMock, mToken);
//
//
//        //response message not success
//        Mockito.verify(merchantRestAPIMock).getOffers(xauthCaptor.capture(), responseOfferCallbackCaptor.capture());
//        responseOfferCallbackCaptor.getValue().success(responseModel, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGetOfferFailedEvent();
//    }
//
//    @Test
//    public void testGetOffersSuccess_whenResponseNull() throws Exception {
//        GetOffersResponseModel responseModel =  null;
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getOffers(merchantRestAPIMock, mToken);
//
//        Mockito.verify(merchantRestAPIMock).getOffers(xauthCaptor.capture(), responseOfferCallbackCaptor.capture());
//
//        responseOfferCallbackCaptor.getValue().success(responseModel, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void testGetOffersError_whenSSLValid() throws Exception {
//        GetOffersResponseModel responseModel = new GetOffersResponseModel();
//        responseModel.setCode(200);
//        responseModel.setMessage("success");
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getOffers(merchantRestAPIMock, mToken);
//
//        Mockito.verify(merchantRestAPIMock).getOffers(xauthCaptor.capture(), responseOfferCallbackCaptor.capture());
//
//        //when valid certification
//        responseOfferCallbackCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//
//    }
//
//    @Test
//    public void testGetOffersError_whenSSLInvalid() throws Exception {
//        GetOffersResponseModel responseModel = new GetOffersResponseModel();
//        responseModel.setCode(200);
//        responseModel.setMessage("success");
//
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.getOffers(merchantRestAPIMock, mToken);
//
//        Mockito.verify(merchantRestAPIMock).getOffers(xauthCaptor.capture(), responseOfferCallbackCaptor.capture());
//        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
//
//        responseOfferCallbackCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
//
//
//        eventBustImplementSample.getOffers(merchantRestAPIMock, mToken);
//
//        Mockito.verify(merchantRestAPIMock, Mockito.times(2)).getOffers(xauthCaptor.capture(), responseOfferCallbackCaptor.capture());
//        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mCertPathValidatorException);
//
//        responseOfferCallbackCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(2)).onSSLErrorEvent();
//
//    }
//
//    /*
//     * save card
//     */
//
//    @Captor
//    private ArgumentCaptor<Callback<SaveCardResponse>> responseSaveCardCallbackCaptor;
//
//    @Captor
//    private ArgumentCaptor<SaveCardRequest> requestCaptor;
//
//    @Test
//    public void testPaymentSaveCardsCard_whenTokenNull() throws Exception {
//        SaveCardRequest request = new SaveCardRequest();
//
//        SaveCardResponse deleteCardResponse = new SaveCardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.saveCard(merchantRestAPIMock, request,  null);
//
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void testPaymentSaveCardsCardSuccess_whenResponseNotNull() throws Exception {
//        SaveCardRequest request = new SaveCardRequest();
//
//        SaveCardResponse deleteCardResponse = new SaveCardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.saveCard(merchantRestAPIMock, request,  mToken);
//
//        Mockito.verify(merchantRestAPIMock).saveCard(xauthCaptor.capture(), requestCaptor.capture(), responseSaveCardCallbackCaptor.capture());
//        //response code 200 /201
//        responseSaveCardCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onSaveCardSuccessEvent();
//
//        //response not code 200 /201
//        deleteCardResponse.setCode(300);
//        Mockito.verify(merchantRestAPIMock).saveCard(xauthCaptor.capture(), requestCaptor.capture(), responseSaveCardCallbackCaptor.capture());
//        responseSaveCardCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onsaveCardFailedEvent();
//    }
//
//    @Test
//    public void testPaymentSaveCardSuccess_whenResponseNotNull_codeNot200() throws Exception {
//        SaveCardRequest request = new SaveCardRequest();
//
//        SaveCardResponse deleteCardResponse = new SaveCardResponse();
//        deleteCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.saveCard(merchantRestAPIMock, request,  mToken);
//
//        //response not code 200 /201
//        deleteCardResponse.setCode(300);
//        Mockito.verify(merchantRestAPIMock).saveCard(xauthCaptor.capture(), requestCaptor.capture(), responseSaveCardCallbackCaptor.capture());
//        responseSaveCardCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onsaveCardFailedEvent();
//    }
//
//
//    @Test
//    public void testPaymentSaveCardCardsCardSuccess_whenResponseNull() throws Exception {
//        SaveCardRequest request = new SaveCardRequest();
//
//        SaveCardResponse saveCardResponse = null;
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.saveCard(merchantRestAPIMock, request,  mToken);
//
//        Mockito.verify(merchantRestAPIMock).saveCard(xauthCaptor.capture(), requestCaptor.capture(), responseSaveCardCallbackCaptor.capture());
//        responseSaveCardCallbackCaptor.getValue().success(saveCardResponse, retrofitResponse);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void testPaymentSaveCardError_whenValidSSL() throws Exception {
//        SaveCardRequest request = new SaveCardRequest();
//
//        SaveCardResponse saveCardResponse = new SaveCardResponse();
//        saveCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.saveCard(merchantRestAPIMock, request,  mToken);
//
//        Mockito.verify(merchantRestAPIMock).saveCard(xauthCaptor.capture(), requestCaptor.capture(), responseSaveCardCallbackCaptor.capture());
//
//        //when valid certification
//        responseSaveCardCallbackCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
//
//    }
//
//    @Test
//    public void testPaymentSaveCardError_invalidSSL() throws Exception {
//        SaveCardRequest request = new SaveCardRequest();
//
//        SaveCardResponse saveCardResponse = new SaveCardResponse();
//        saveCardResponse.setCode(200);
//        eventBustImplementSample.setTransactionManager(transactionManager);
//        eventBustImplementSample.registerBus(veritransBus);
//        eventBustImplementSample.saveCard(merchantRestAPIMock, request,  mToken);
//
//        Mockito.verify(merchantRestAPIMock).saveCard(xauthCaptor.capture(), requestCaptor.capture(), responseSaveCardCallbackCaptor.capture());
//        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
//
//        responseSaveCardCallbackCaptor.getValue().failure(retrofitErrorMock);
//        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
//
//    }

}
