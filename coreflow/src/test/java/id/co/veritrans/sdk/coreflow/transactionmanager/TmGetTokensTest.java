package id.co.veritrans.sdk.coreflow.transactionmanager;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.Collections;

import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by ziahaqi on 24/06/2016.
 */

public class TmGetTokensTest extends TransactionMangerMain{


    @Captor
    private ArgumentCaptor<Callback<TokenDetailsResponse>> callbackgetTokenArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> cardNumberCaptor;
    @Captor
    private ArgumentCaptor<String> cardCVVCaptor;
    @Captor
    private ArgumentCaptor<String> cardExpMonthCaptor;
    @Captor
    private ArgumentCaptor<String> cardExpYearCaptor;
    @Captor
    private ArgumentCaptor<String> clientKeyCaptor;
    @Captor
    private ArgumentCaptor<String> bankCaptor;
    @Captor
    private ArgumentCaptor<Boolean> instalmentCaptor;
    @Captor
    private ArgumentCaptor<String> instalmentTermCaptor;
    @Captor
    private ArgumentCaptor<Boolean> scureCaptor;
    @Captor
    private ArgumentCaptor<Boolean> twoClickCaptor;
    @Captor
    private ArgumentCaptor<Double> grossAmountCaptor;

    CardTokenRequest cardTokenRequest = new CardTokenRequest();

    @Captor
    ArgumentCaptor<VeritransBus> captor = ArgumentCaptor
            .forClass(VeritransBus.class);
    @Captor
    private ArgumentCaptor<String> tokenIdCaptor;


    //get3DSToken

    @Test public void testGetTokenSuccess_get3DSToken_whenResponseNotNull(){
        TokenDetailsResponse tokenDetailsResponse = new TokenDetailsResponse();
        tokenDetailsResponse.setStatusCode("200");



        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSToken(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(), callbackgetTokenArgumentCaptor.capture());

        // when retrofitResponse 20
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();

        //when retrofitResponse not 200
        tokenDetailsResponse.setStatusCode("212");
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenFailedEvent();

        tokenDetailsResponse.setStatusMessage("statusnotnull");
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(2)).onGetTokenFailedEvent();
    }


    @Test public void testGetTokenSuccess_get3DSToken_whenresponseNull(){
        TokenDetailsResponse tokenDetailsResponse = null;

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSToken(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(), callbackgetTokenArgumentCaptor.capture());

        // when retrofitResponse null
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testGetTokenError_get3DSToken(){
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSToken(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(), callbackgetTokenArgumentCaptor.capture());

        //when valid certification
        callbackgetTokenArgumentCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }


    //getTokenInstalmentOfferTwoClick


    @Test public void testGetTokenSuccess_get3DSTokenInstalmentOffers_whenResponseNotNull(){
        TokenDetailsResponse tokenDetailsResponse = new TokenDetailsResponse();
        tokenDetailsResponse.setStatusCode("200");


        cardTokenRequest.setInstalment(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSTokenInstalmentOffers(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackgetTokenArgumentCaptor.capture());

        // when retrofitResponse 20
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();

        //when retrofitResponse not 200
        tokenDetailsResponse.setStatusCode("212");
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenFailedEvent();

        tokenDetailsResponse.setStatusMessage("statusnotnull");
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(2)).onGetTokenFailedEvent();
    }



    @Test public void testGetTokenSuccess_get3DSTokenInstalmentOffers_whenresponseNull(){
        TokenDetailsResponse tokenDetailsResponse = null;

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSTokenInstalmentOffers(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackgetTokenArgumentCaptor.capture());

        // when retrofitResponse null
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testGetTokenError_get3DSTokenInstalmentOffers(){
        cardTokenRequest.setInstalment(true);
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSTokenInstalmentOffers(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackgetTokenArgumentCaptor.capture());

        //when valid certification
        callbackgetTokenArgumentCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }

    //getTokenTwoClick
    @Test public void testGetTokenSuccess_getTokenTwoClick_whenResponseNotNull(){
        TokenDetailsResponse tokenDetailsResponse = new TokenDetailsResponse();
        tokenDetailsResponse.setStatusCode("200");

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(false);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                callbackgetTokenArgumentCaptor.capture());

        // when retrofitResponse 20
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();

        //when retrofitResponse not 200
        tokenDetailsResponse.setStatusCode("212");
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenFailedEvent();

        tokenDetailsResponse.setStatusMessage("statusnotnull");
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(2)).onGetTokenFailedEvent();
    }



    @Test public void testGetTokenSuccess_getTokenTwoClick_whenresponseNull(){
        TokenDetailsResponse tokenDetailsResponse = null;

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(false);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                callbackgetTokenArgumentCaptor.capture());

        // when retrofitResponse null
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testGetTokenError_getTokenTwoClick(){
        cardTokenRequest.setInstalment(false);
        cardTokenRequest.setTwoClick(true);
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);


        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                callbackgetTokenArgumentCaptor.capture());

        //when valid certification
        callbackgetTokenArgumentCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }


    //getTokenInstalmentOfferTwoClick
    @Test public void testGetTokenSuccess_getTokenInstalmentOfferTwoClick_whenResponseNotNull(){
        TokenDetailsResponse tokenDetailsResponse = new TokenDetailsResponse();
        tokenDetailsResponse.setStatusCode("200");

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(true);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenInstalmentOfferTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackgetTokenArgumentCaptor.capture());

        // when retrofitResponse 20
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();

        //when retrofitResponse not 200
        tokenDetailsResponse.setStatusCode("212");
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenFailedEvent();

        tokenDetailsResponse.setStatusMessage("statusnotnull");
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(2)).onGetTokenFailedEvent();
    }



    @Test public void testGetTokenSuccess_getTokenInstalmentOfferTwoClick_whenresponseNull(){
        TokenDetailsResponse tokenDetailsResponse = null;

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(true);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenInstalmentOfferTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackgetTokenArgumentCaptor.capture());

        // when retrofitResponse null
        callbackgetTokenArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testGetTokenError_getTokenInstalmentOfferTwoClick(){
        cardTokenRequest.setInstalment(true);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenInstalmentOfferTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackgetTokenArgumentCaptor.capture());

        //when valid certification
        callbackgetTokenArgumentCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }
}
