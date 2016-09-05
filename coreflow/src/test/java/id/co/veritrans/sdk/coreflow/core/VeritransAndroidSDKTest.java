package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.SDKConfigTest;
import id.co.veritrans.sdk.coreflow.models.BBMCallBackUrl;
import id.co.veritrans.sdk.coreflow.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.coreflow.models.BCABankTransfer;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayDescriptionModel;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayModel;
import id.co.veritrans.sdk.coreflow.models.CIMBClickPayModel;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.CstoreEntity;
import id.co.veritrans.sdk.coreflow.models.DescriptionModel;
import id.co.veritrans.sdk.coreflow.models.EpayBriTransfer;
import id.co.veritrans.sdk.coreflow.models.IndomaretRequestModel;
import id.co.veritrans.sdk.coreflow.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.coreflow.models.ItemDetails;
import id.co.veritrans.sdk.coreflow.models.KlikBCADescriptionModel;
import id.co.veritrans.sdk.coreflow.models.KlikBCAModel;
import id.co.veritrans.sdk.coreflow.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.MandiriECashModel;
import id.co.veritrans.sdk.coreflow.models.PaymentMethodsModel;
import id.co.veritrans.sdk.coreflow.models.PermataBankTransfer;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.UserDetail;
import id.co.veritrans.sdk.coreflow.transactionmanager.BusCollaborator;
import id.co.veritrans.sdk.coreflow.utilities.Utils;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by ziahaqi on 24/06/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalDataHandler.class, SdkUtil.class, Looper.class, Utils.class, Log.class, TextUtils.class, Logger.class})

public class VeritransAndroidSDKTest {

//    @Mock
//    protected BusCollaborator callbackCollaborator;
//    @InjectMocks
//    protected EventBusImplementSample eventBustImplementSample;
//    @Mock
//    Context contextMock;
//    @Mock
//    TransactionManager transactionManager;
//    @Mock
//    MerchantRestAPI merchantRestAPI;
//    @Mock
//    VeritransRestAPI veritransRestAPI;
//    @Mock
//    Resources resourceMock;
//    @Mock
//    ConnectivityManager connectivityManager;
//    @Mock
//    NetworkInfo networkInfo;
//    @Mock
//    boolean isconnected;
//    @Mock
//    SharedPreferences preference;
//    String msisdnMock = "msisdnmock";
//    @Mock
//    IndosatDompetkuRequest indosatDompetkuRequestMock;
//    Log fakeLog;
//    @Mock
//    private String sdkTokenMock;
//    @Mock
//    private boolean isRunningMock;
//    @Mock
//    private java.lang.Boolean isconnectedMock;
//    @Mock
//    private CardTokenRequest cardTokenRequestMock;
//    private VeritransSDK veritransSDKSSpy;
//    @Mock
//    private TransactionRequest transactionRequestMock;
//    @Mock
//    private PermataBankTransfer permatabankTransferMok;
//    @Mock
//    private BCABankTransfer bcaTransferMok;
//    @Mock
//    private CardTransfer cardTransferMock;
//    @Mock
//    private BCAKlikPayDescriptionModel bcaklikpayDescModelMock;
//    @Mock
//    private BCAKlikPayModel bcaKlikPayModelMock;
//    @Mock
//    private KlikBCADescriptionModel klikbcaDescModelMock;
//    @Mock
//    private KlikBCAModel klikBCAModelMock;
//    @Mock
//    private id.co.veritrans.sdk.coreflow.models.BillInfoModel billInfoModelMock;
//    @Mock
//    private ArrayList<ItemDetails> itemDetailMock;
//    @Mock
//    private MandiriBillPayTransferModel mandiriBillPayTransModelMock;
//    @Mock
//    private DescriptionModel descriptionModelMock;
//    @Mock
//    private CIMBClickPayModel cimbClickPayModel;
//    @Mock
//    private MandiriECashModel mandiriEcashModelMock;
//    @Mock
//    private EpayBriTransfer epayBriTransferMock;
//    @Mock
//    private CstoreEntity cstoreEntityMock;
//    @Mock
//    private IndomaretRequestModel indomaretRequestModelMock;
//    @Mock
//    private SaveCardRequest savecardMock;
//    @Mock
//    private SaveCardRequest saveCardRequestMock;
//    @Mock
//    private MandiriClickPayModel mandiriClickPayModelMock;
//    @Mock
//    private MandiriClickPayRequestModel mandiriClickPayRequestModelMock;
//    @Mock
//    private BBMMoneyRequestModel bbmMoneyRequestModelMock;
//    private String userId = "klikBCA";
//    private String transactionId = "A1";
//    private String cardToken = "card_token";
//    private String token = "token";
//    private String email = "email@domain.com";
//    @Mock
//    private ISdkFlow uiflowMock;
//    private String exceptionMock;
//    @Mock
//    private Integer drawableIntCostumMock;
//    @Mock
//    private Drawable drawableCostumMock;
//    @Mock
//    private Integer drawableIntDefaultMock;
//    @Mock
//    private Drawable drawableDefaultMock;
//    private String merchantLogoMock = "merchantLogo";
//    @Mock
//    private BBMCallBackUrl bbmCallbackUrlMock;
//    @Mock
//    private ArrayList<PaymentMethodsModel> paymentMethodMock;
//    @Mock
//    private UserDetail userDetailMock;
//    @Mock
//    private MixpanelAnalyticsManager mixpanelMock;
//    @Mock
//    private SnapTransactionManager snapTransctionmanagerMock;
//    private String snapTOken = "token";
//
//    @Before
//    public void setup() {
//        PowerMockito.mockStatic(TextUtils.class);
//        PowerMockito.mockStatic(Log.class);
//        PowerMockito.mockStatic(Logger.class);
//        PowerMockito.mockStatic(Looper.class);
//        PowerMockito.mockStatic(Utils.class);
//        PowerMockito.mockStatic(SdkUtil.class);
//
//        Mockito.when(contextMock.getApplicationContext()).thenReturn(contextMock);
//        Mockito.when(contextMock.getString(R.string.error_unable_to_connect)).thenReturn("not connected");
//        Mockito.when(contextMock.getResources()).thenReturn(resourceMock);
//        Mockito.when(contextMock.getResources().getDrawable(drawableIntDefaultMock)).thenReturn(drawableDefaultMock);
//
//        Mockito.when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
//        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
//        Mockito.when(networkInfo.isConnected()).thenReturn(false);
//        Mockito.when(contextMock.getSharedPreferences("local.data", Context.MODE_PRIVATE)).thenReturn(preference);
//
//        VeritransSDK veritransSDK = (new SdkCoreFlowBuilder(contextMock, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL)
//                .enableLog(true)
//                .setDefaultText("open_sans_regular.ttf")
//                .setSemiBoldText("open_sans_semibold.ttf")
//                .setBoldText("open_sans_bold.ttf")
//                .buildSDK());
//        Mockito.when(veritransSDK.readAuthenticationToken()).thenReturn(sdkTokenMock);
//        veritransSDK.setTransactionManager(transactionManager);
//        veritransSDK.setSnapTransactionManager(snapTransctionmanagerMock);
//        transactionManager.setAnalyticsManager(mixpanelMock);
//        veritransSDKSSpy = spy(veritransSDK);
//
//    }
//
//    @Test
//    public void sampelTest(){
//        Assert.assertTrue(true);
//    }
//
//    @Test
//    public void getToken_whenCardTokenRequestNull() {
//        veritransSDKSSpy.getCardToken(null);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning);
//    }
//
//    @Test
//    public void getToken_whenCardTokenRequestNotNull_networkAvailable() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//
//        veritransSDKSSpy.getCardToken(cardTokenRequestMock);
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning);
//    }
//
//    @Test
//    public void getToken_whenCardTokenRequestNotNull_networkUnavailable() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(false);
//
//        veritransSDKSSpy.getCardToken(cardTokenRequestMock);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning);
//        callbackCollaborator.onGeneralErrorEvent();
//    }
//
//    @Test
//    public void getOffersList_whenNetworkAvailable() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.getOffersList();
//        Mockito.verify(transactionManager).getOffers("");
//        Assert.assertTrue(veritransSDKSSpy.isRunning);
//    }
//
//    @Test
//    public void startRegisterCardUIFlow_whenUIFlowNotNull() {
//        veritransSDKSSpy.uiflow = uiflowMock;
//        veritransSDKSSpy.startRegisterCardUIFlow(contextMock);
//        Mockito.verify(uiflowMock).runRegisterCard(contextMock);
//    }
//
//    @Test
//    public void startPaymentUiFlow_whenUIFlowNotNull() {
//        when(transactionRequestMock.getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
//        veritransSDKSSpy.uiflow = uiflowMock;
//        veritransSDKSSpy.isRunning = false;
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//
//        veritransSDKSSpy.startPaymentUiFlow(contextMock);
//        Mockito.verify(veritransSDKSSpy).startPaymentUiFlow(contextMock);
//    }
//
//
//    @Test
//    public void startPaymentUiFlow_whenTransactionRequestNull() {
//        veritransSDKSSpy.uiflow = uiflowMock;
//        veritransSDKSSpy.isRunning = true;
//        veritransSDKSSpy.setTransactionRequest(null);
//
//        veritransSDKSSpy.startPaymentUiFlow(contextMock);
//        verifyStatic(Mockito.times(2));
//        Logger.e(Matchers.anyString());
//
//    }
//
//    @Test
//    public void startPaymentUiFlow_whenSdkRunning() {
//        veritransSDKSSpy.uiflow = uiflowMock;
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        veritransSDKSSpy.isRunning = true;
//
//        veritransSDKSSpy.startPaymentUiFlow(contextMock);
//        verifyStatic(Mockito.times(1));
//        Logger.e(Matchers.anyString());
//
//    }
//
//    @Test
//    public void getMerhcantToken() {
//        PowerMockito.mockStatic(LocalDataHandler.class);
//        when(LocalDataHandler.readObject(contextMock.getString(R.string.user_details), UserDetail.class)).thenReturn(userDetailMock);
//        when(userDetailMock.getMerchantToken()).thenReturn("token");
//        Assert.assertEquals("token", veritransSDKSSpy.getMerchantToken());
//
//    }
//
//    @Test
//    public void isLogEnabled() {
//        Assert.assertTrue(veritransSDKSSpy.isLogEnabled());
//    }
//
//    @Test
//    public void getMerchantUrl() {
//        Assert.assertEquals(SDKConfigTest.MERCHANT_BASE_URL, veritransSDKSSpy.getMerchantServerUrl());
//    }
//
//    @Test
//    public void getInstanceTest() {
//        VeritransSDK.getInstance(null);
//        verifyStatic(Mockito.times(1));
//        Logger.e(Matchers.anyString());
//    }
//
//    @Test
//    public void getDefaultText() {
//        String defaultText = "text";
//        veritransSDKSSpy.setDefaultText(defaultText);
//        Assert.assertEquals(defaultText, veritransSDKSSpy.getDefaultText());
//    }
//
//    @Test
//    public void boldtext() {
//        String boldtext = "text";
//        veritransSDKSSpy.setBoldText(boldtext);
//        Assert.assertEquals(boldtext, veritransSDKSSpy.getBoldText());
//
//    }
//
//    @Test
//    public void semiboldtext() {
//        String text = "text";
//        veritransSDKSSpy.setSemiBoldText(text);
//        Assert.assertEquals(text, veritransSDKSSpy.getSemiBoldText());
//
//    }
//
//    @Test
//    public void isRunningTest() {
//        veritransSDKSSpy.isRunning = true;
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning());
//
//    }
//
//
//    @Test
//    public void merchantLogo() {
//        String merchantLogo = "merchantLogo";
//        veritransSDKSSpy.setMerchantLogo(merchantLogo);
//        Assert.assertEquals(merchantLogo, veritransSDKSSpy.getMerchantLogo());
//
//    }
//
//    @Test
//    public void paymentMethod() {
//
//        veritransSDKSSpy.setSelectedPaymentMethods(paymentMethodMock);
//        Assert.assertEquals(paymentMethodMock, veritransSDKSSpy.getSelectedPaymentMethods());
//    }
//
//    @Test
//    public void getOverLimit_whenNoetworkAvailable() throws Exception {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//
//        veritransSDKSSpy.getOffersList();
//        Mockito.verify(transactionManager, Mockito.times(1)).getOffers(sdkTokenMock);
//        Assert.assertTrue(veritransSDKSSpy.isRunning);
//    }
//
//
//    @Test
//    public void TransactionRequestTest() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        Assert.assertEquals(transactionRequestMock, veritransSDKSSpy.getTransactionRequest());
//    }
//
//    @Test
//    public void getTokenTest() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.checkout();
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning());
//    }
//
//    @Test
//    public void getTokenTest_whenTransactionRequestNull() {
//        veritransSDKSSpy.setTransactionRequest(null);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.checkout();
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void getTokenTest_whenNetworkUnAvailable() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(false);
//        veritransSDKSSpy.checkout();
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//
//
//    @Test
//    public void getSnapTransaction() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.getTransactionOptions(snapTOken);
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning());
//
//    }
//
//    @Test
//    public void getSnapTransaction_whenTokenNull() {
//        when(TextUtils.isEmpty(null)).thenReturn(true);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.getTransactionOptions(null);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void getSnapTransaction_whenNetworkUnAvailable() {
//        when(TextUtils.isEmpty(sdkTokenMock)).thenReturn(true);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(false);
//        veritransSDKSSpy.getTransactionOptions(null);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void snapPaymentUsingCard() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingCard(cardToken, token, false);
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning);
//    }
//
//    @Test
//    public void snapPaymentUsingCard_whenTransactionRequestNull() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingCard(cardToken, token, false);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void snapPaymentUsingCard_whenNetworkUnavailable() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(false);
//        veritransSDKSSpy.snapPaymentUsingCard(cardToken, token, false);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onNetworkUnAvailable();
//    }
//
//    @Test
//    public void snapPaymentUsingBankTransferBCA() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingBankTransferBCA(email, token);
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning());
//    }
//
//    @Test
//    public void snapPaymentUsingBankTransferBCA_whenNetworkUnavailable() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(false);
//        veritransSDKSSpy.snapPaymentUsingBankTransferBCA(email, token);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onNetworkUnAvailable();
//    }
//
//    @Test
//    public void snapPaymentUsingBankTransferBCA_whenTransactionRequestNull() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingBankTransferBCA(email, token);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void snapPaymentUsingBankTransferPermata() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingBankTransferPermata(email, token);
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning());
//    }
//
//    @Test
//    public void snapPaymentUsingBankTransferPermata_whenNetworkUnavailable() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(false);
//        veritransSDKSSpy.snapPaymentUsingBankTransferPermata(email, token);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onNetworkUnAvailable();
//    }
//
//    @Test
//    public void snapPaymentUsingBankTransferPermata_whenTransactionRequestNull() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingBankTransferPermata(email, token);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void snapPaymentUsingKlikBCA() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingKlikBCA(userId, token);
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning());
//    }
//
//    @Test
//    public void snapPaymentUsingKlikBCA_whenTransactionRequestNull() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingKlikBCA(userId, token);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void snapPaymentUsingKlikBCA_whenNetworkUnavailable() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(false);
//        veritransSDKSSpy.snapPaymentUsingKlikBCA(userId, token);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onNetworkUnAvailable();
//    }
//
//    @Test
//    public void snapPaymentUsingBCAKlikpay() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingBCAKlikpay(token);
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning());
//    }
//
//    @Test
//    public void snapPaymentUsingBCAKlikpay_whenTransactionRequestNull() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingBCAKlikpay(token);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//
//    @Test
//    public void snapPaymentUsingBCAKlikpay_whenNetworkUnavailable() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(false);
//        veritransSDKSSpy.snapPaymentUsingBCAKlikpay(token);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onNetworkUnAvailable();
//    }
//
//    public void snapPaymentUsingBankTransferAllBank() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingBankTransferAllBank(email, token);
//        Assert.assertEquals(true, veritransSDKSSpy.isRunning());
//    }
//
//    @Test
//    public void snapPaymentUsingBankTransferAllBank_whenNetworkUnavailable() {
//        veritransSDKSSpy.setTransactionRequest(transactionRequestMock);
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(false);
//        veritransSDKSSpy.snapPaymentUsingBankTransferAllBank(email, token);
//        Assert.assertEquals(false, veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onNetworkUnAvailable();
//    }
//
//    @Test
//    public void snapPaymentUsingBankTransferAllBank_whenTransactionRequestNull() {
//        when(veritransSDKSSpy.isNetworkAvailable()).thenReturn(true);
//        veritransSDKSSpy.snapPaymentUsingBankTransferAllBank(email, token);
//        Assert.assertFalse(veritransSDKSSpy.isRunning());
//        Mockito.verify(callbackCollaborator).onGeneralErrorEvent();
//    }
//

}
