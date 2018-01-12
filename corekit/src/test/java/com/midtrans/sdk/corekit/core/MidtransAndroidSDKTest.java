package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.BanksPointCallback;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.ObtainPromoCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.models.BCABankTransfer;
import com.midtrans.sdk.corekit.models.BCAKlikPayDescriptionModel;
import com.midtrans.sdk.corekit.models.BCAKlikPayModel;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.CIMBClickPayModel;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CardTransfer;
import com.midtrans.sdk.corekit.models.CstoreEntity;
import com.midtrans.sdk.corekit.models.DescriptionModel;
import com.midtrans.sdk.corekit.models.EpayBriTransfer;
import com.midtrans.sdk.corekit.models.IndomaretRequestModel;
import com.midtrans.sdk.corekit.models.IndosatDompetkuRequest;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.KlikBCADescriptionModel;
import com.midtrans.sdk.corekit.models.KlikBCAModel;
import com.midtrans.sdk.corekit.models.MandiriBillPayTransferModel;
import com.midtrans.sdk.corekit.models.MandiriClickPayModel;
import com.midtrans.sdk.corekit.models.MandiriClickPayRequestModel;
import com.midtrans.sdk.corekit.models.MandiriECashModel;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.PermataBankTransfer;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by ziahaqi on 24/06/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalDataHandler.class, SdkUtil.class, Looper.class, Utils.class, Log.class, TextUtils.class, Logger.class, MixpanelAnalyticsManager.class})
@PowerMockIgnore("javax.net.ssl.*")
public class MidtransAndroidSDKTest {

    @Mock
    protected CallbackCollaborator callbackCollaborator;
    @InjectMocks
    protected CallbackImplementSample callbackSample;
    @Mock
    Context contextMock;
    @Mock
    SnapTransactionManager transactionManager;
    @Mock
    PromoEngineManager promoEngineManager;
    @Mock
    MerchantRestAPI merchantRestAPI;
    @Mock
    MidtransRestAPI midtransRestAPI;
    @Mock
    Resources resourceMock;
    @Mock
    ConnectivityManager connectivityManager;
    @Mock
    NetworkInfo networkInfo;
    @Mock
    boolean isconnected;
    @Mock
    SharedPreferences preference;
    String msisdnMock = "msisdnmock";
    @Mock
    IndosatDompetkuRequest indosatDompetkuRequestMock;
    Log fakeLog;
    private CardTokenCallback cardTokenCallbackMock;
    private String sampleToken = "token";
    private String sampleText = "text";
    private String sampleMerchantLogoStr = "merchantLogo";
    private String giftCardNumber = "4811111111111114";
    private String giftCardPassword = "123";
    private CheckoutCallback checkoutCallbackMock;
    private TransactionOptionsCallback transactionOptionCallbackMock;
    private TransactionCallback transactionCallbackMock;
    private BankBinsCallback getBankBinCallbackMock;

    @Mock
    private String sdkTokenMock;
    @Mock
    private boolean isRunningMock;
    @Mock
    private java.lang.Boolean isconnectedMock;
    @Mock
    private CardTokenRequest cardTokenRequestMock;
    private MidtransSDK midtransSDKSSpy;
    @Mock
    private TransactionRequest transactionRequestMock;
    @Mock
    private PermataBankTransfer permatabankTransferMok;
    @Mock
    private BCABankTransfer bcaTransferMok;
    @Mock
    private CardTransfer cardTransferMock;
    @Mock
    private BCAKlikPayDescriptionModel bcaklikpayDescModelMock;
    @Mock
    private BCAKlikPayModel bcaKlikPayModelMock;
    @Mock
    private KlikBCADescriptionModel klikbcaDescModelMock;
    @Mock
    private KlikBCAModel klikBCAModelMock;
    @Mock
    private BillInfoModel billInfoModelMock;
    @Mock
    private ArrayList<ItemDetails> itemDetailMock;
    @Mock
    private MandiriBillPayTransferModel mandiriBillPayTransModelMock;
    @Mock
    private DescriptionModel descriptionModelMock;
    @Mock
    private CIMBClickPayModel cimbClickPayModel;
    @Mock
    private MandiriECashModel mandiriEcashModelMock;
    @Mock
    private EpayBriTransfer epayBriTransferMock;
    @Mock
    private CstoreEntity cstoreEntityMock;
    @Mock
    private IndomaretRequestModel indomaretRequestModelMock;
    @Mock
    private SaveCardRequest savecardMock;
    @Mock
    private SaveCardRequest saveCardRequestMock;
    @Mock
    private MandiriClickPayModel mandiriClickPayModelMock;
    @Mock
    private MandiriClickPayRequestModel mandiriClickPayRequestModelMock;
    private String userId = "klikBCA";
    private String transactionId = "A1";
    private String cardToken = "card_token";
    private String token = "token";
    private String email = "email@domain.com";
    @Mock
    private ISdkFlow uiflowMock;
    private String exceptionMock;
    @Mock
    private Integer drawableIntCostumMock;
    @Mock
    private Drawable drawableCostumMock;
    @Mock
    private Integer drawableIntDefaultMock;
    @Mock
    private Drawable drawableDefaultMock;
    private String merchantLogoMock = "merchantLogo";
    @Mock
    private ArrayList<PaymentMethodsModel> paymentMethodMock;
    @Mock
    private UserDetail userDetailMock;
    @Mock
    private MixpanelAnalyticsManager mixpanelMock;
    @Mock
    private SnapTransactionManager snapTransctionmanagerMock;
    @Mock
    private ObtainPromoCallback obtainPromoCallback;
    @Mock
    private TokenRequestModel tokenRequestModelMock;
    @Mock
    private CardRegistrationCallback cardRegistrationCallbackMock;
    @Mock
    private ArrayList<SaveCardRequest> savedCardsMock;
    @Mock
    private SaveCardCallback saveCardCallbackMock;
    @Mock
    private GetCardCallback getCardCallbackMock;
    @Mock
    private BanksPointCallback bankpointCallbackMock;
    @Mock
    private TransactionFinishedCallback transactionFinishedCallbackMock;
    @Mock
    private TransactionResult transactionResultMock;

    private String snapTOken = "token";
    private String samplePromoId = "promo_id";
    private Double sampleAmount = 1000.0;
    private String sampleDiscountToken = "discount_token";
    private String sampleUserId = "userId";
    private String sampleMandiriCardNumber = "4111111111111111";
    private String sampleTokenResponse = "token_response";
    private String sampleInput3 = "input_3";
    private String sampleMsisdn = "msisdn";
    private String samplePhoneNumber = "08111111111";
    private String sampleCardNumber = "4811111111111114";
    private String sampleCvv = "123";
    private String sampleExpMonth = "01";
    private String sampleExpYear = "2020";
    private DeleteCardCallback deleteCardCallbackMock;
    private int sampleTimeOut = 1000;
    private String sampleClientKey = "client_key";
    private String sampleBaseUrl = "base_url";
    private String sampleMerchantBaseUrl = "merchant_url";
    private Long discountAmount = 1000l;

    @Test
    public void test() {
        Assert.assertEquals(1, 1);
    }

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.mockStatic(SdkUtil.class);
        PowerMockito.mockStatic(MixpanelAnalyticsManager.class);
        Mockito.when(contextMock.getApplicationContext()).thenReturn(contextMock);
        Mockito.when(contextMock.getString(R.string.error_unable_to_connect)).thenReturn("not connected");
        Mockito.when(contextMock.getResources()).thenReturn(resourceMock);
        Mockito.when(contextMock.getResources().getDrawable(drawableIntDefaultMock)).thenReturn(drawableDefaultMock);

        Mockito.when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        Mockito.when(contextMock.getSharedPreferences("local.data", Context.MODE_PRIVATE)).thenReturn(preference);

        this.checkoutCallbackMock = callbackSample.getCheckoutCallback();
        this.transactionOptionCallbackMock = callbackSample.getTransactionOptionCallback();
        this.transactionCallbackMock = callbackSample.getTransactionCallback();
        this.getBankBinCallbackMock = callbackSample.getBankBinCallback();
        this.cardTokenCallbackMock = callbackSample.getCardTokenCallback();
        this.saveCardCallbackMock = callbackSample.getSaveCardCallback();
        this.bankpointCallbackMock = callbackSample.getbankPointCallback();
        this.deleteCardCallbackMock = callbackSample.getDeleteCardCallback();

        MidtransSDK midtransSDK = SdkCoreFlowBuilder.init(contextMock, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL)
                .enableLog(true)
                .buildSDK();
        Mockito.when(midtransSDK.readAuthenticationToken()).thenReturn(sdkTokenMock);
        midtransSDK.setTransactionManager(transactionManager);
        midtransSDKSSpy = spy(midtransSDK);

    }

    @Test
    public void sampelTest() {
        Assert.assertTrue(true);
    }

    @Test
    public void getToken_whenCardTokenCallbackNull() {
        midtransSDKSSpy.getCardToken(cardTokenRequestMock, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void getToken_whenCardTokenRequestNull() {
        midtransSDKSSpy.getCardToken(null, cardTokenCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning);
    }

    @Test
    public void getToken_whenCardTokenRequestNotNull_networkAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);

        midtransSDKSSpy.getCardToken(cardTokenRequestMock, cardTokenCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning);
    }

    @Test
    public void getToken_whenCardTokenRequestNotNull_networkUnavailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);

        midtransSDKSSpy.getCardToken(cardTokenRequestMock, cardTokenCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning);
        callbackCollaborator.onError();
    }

    @Test
    public void startPaymentUiFlow_whenUIFlowNotNull() {
        when(transactionRequestMock.getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        midtransSDKSSpy.isRunning = false;
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);

        midtransSDKSSpy.startPaymentUiFlow(contextMock);
        Mockito.verify(midtransSDKSSpy).startPaymentUiFlow(contextMock);
    }


    @Test
    public void startPaymentUiFlow_whenTransactionRequestNull() {
        midtransSDKSSpy.uiflow = uiflowMock;
        midtransSDKSSpy.isRunning = true;
        midtransSDKSSpy.setTransactionRequest(null);

        midtransSDKSSpy.startPaymentUiFlow(contextMock);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());

    }

    @Test
    public void startPaymentUiFlow_whenSdkRunning() {
        midtransSDKSSpy.uiflow = uiflowMock;
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        midtransSDKSSpy.isRunning = true;

        midtransSDKSSpy.startPaymentUiFlow(contextMock);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());

    }

    @Test
    public void isLogEnabled() {
        Assert.assertTrue(midtransSDKSSpy.isLogEnabled());
    }

    @Test
    public void getMerchantUrl() {
        Assert.assertEquals(SDKConfigTest.MERCHANT_BASE_URL, midtransSDKSSpy.getMerchantServerUrl());
    }

    @Test
    public void getInstanceTest() {
        MidtransSDK.delegateInstance(null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());
    }

    @Test
    public void getDefaultText() {
        String defaultText = sampleText;
        midtransSDKSSpy.setDefaultText(defaultText);
        Assert.assertEquals(defaultText, midtransSDKSSpy.getDefaultText());
    }

    @Test
    public void boldtext() {
        String boldtext = sampleText;
        midtransSDKSSpy.setBoldText(boldtext);
        Assert.assertEquals(boldtext, midtransSDKSSpy.getBoldText());

    }

    @Test
    public void semiboldtext() {
        String text = sampleText;
        midtransSDKSSpy.setSemiBoldText(text);
        Assert.assertEquals(text, midtransSDKSSpy.getSemiBoldText());

    }

    @Test
    public void isRunningTest() {
        midtransSDKSSpy.isRunning = true;
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());

    }


    @Test
    public void merchantLogo() {
        String merchantLogo = sampleMerchantLogoStr;
        midtransSDKSSpy.setMerchantLogo(merchantLogo);
        Assert.assertEquals(merchantLogo, midtransSDKSSpy.getMerchantLogo());

    }

    @Test
    public void paymentMethod() {

        midtransSDKSSpy.setSelectedPaymentMethods(paymentMethodMock);
        Assert.assertEquals(paymentMethodMock, midtransSDKSSpy.getSelectedPaymentMethods());
    }


    @Test
    public void TransactionRequestTest() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Assert.assertEquals(transactionRequestMock, midtransSDKSSpy.getTransactionRequest());
    }

    /**
     * test checkout
     */

    @Test
    public void checkout() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.checkout(checkoutCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void checkout_whenCallbackNull() {
        midtransSDKSSpy.checkout(null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void checkout_whenTransactionRequestNull() {
        midtransSDKSSpy.setTransactionRequest(null);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.checkout(checkoutCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void checkout_whenNetworkUnAvailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.checkout(checkoutCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }


    /**
     * Test checkout with UserId
     */

    @Test
    public void checkoutWithuserId() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        when(SdkUtil.getSnapTokenRequestModel(transactionRequestMock)).thenReturn(tokenRequestModelMock);
        midtransSDKSSpy.checkout(sampleUserId, checkoutCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void checkoutWithUserId_whenCallbackNull() {
        midtransSDKSSpy.checkout(userId, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void checkoutWithUserId_whenTransactionRequestNull() {
        midtransSDKSSpy.setTransactionRequest(null);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.checkout(userId, checkoutCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void checkoutWithUserId_whenNetworkUnAvailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.checkout(userId, checkoutCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }


    @Test
    public void getSnapTransaction() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getTransactionOptions(snapTOken, transactionOptionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void getSnapTransaction_whenTokenNull() {
        when(TextUtils.isEmpty(null)).thenReturn(true);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getTransactionOptions(null, transactionOptionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void getSnapTransaction_whenNetworkUnAvailable() {
        when(TextUtils.isEmpty(sdkTokenMock)).thenReturn(true);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.getTransactionOptions(null, transactionOptionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingCard() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCard(cardToken, new CreditCardPaymentModel(token, false), transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning);
    }

    @Test
    public void snapPaymentCreditCard_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCard(cardToken, new CreditCardPaymentModel(token, false), null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingCard_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCard(cardToken, new CreditCardPaymentModel(token, false), transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingCard_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingCard(cardToken, new CreditCardPaymentModel(token, false), transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * payment creditcard with promo
     */

    @Test
    public void snapPaymentUsingCardWithPromo() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCard(cardToken, sampleDiscountToken, discountAmount, new CreditCardPaymentModel(token, false), transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning);
    }

    @Test
    public void snapPaymentCreditCardWithPromo_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCard(cardToken, sampleDiscountToken, discountAmount, new CreditCardPaymentModel(token, false), null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingCardPromo_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCard(cardToken, sampleDiscountToken, discountAmount, new CreditCardPaymentModel(token, false), transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingCardPromo_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingCard(cardToken, sampleDiscountToken, discountAmount, new CreditCardPaymentModel(token, false), transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * test BCA Bank Transfer
     */

    @Test
    public void snapPaymentUsingBankTransferBCA() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBCA(email, token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void snapPaymentUsingBankTransferBCA_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBCA(email, token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBankTransferBCA_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBankTransferBCA(email, token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingBankTransferBCA_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBCA(email, token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingBankTransferPermata() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferPermata(email, token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void snapPaymentUsingBankTransferPermata_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferPermata(email, token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBankTransferPermata_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBankTransferPermata(email, token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingBankTransferPermata_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferPermata(email, token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }


    @Test
    public void snapPaymentUsingBankTransferBni() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBni(email, token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void snapPaymentUsingBankTransferBni_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBni(email, token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBankTransferBni_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBankTransferBni(email, token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingBankTransferBni_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBni(email, token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingKlikBCA() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKlikBCA(userId, token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void snapPaymentUsingKlikBca_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKlikBCA(userId, token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingKlikBCA_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKlikBCA(userId, token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingKlikBCA_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingKlikBCA(userId, token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingBCAKlikpay() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBCAKlikpay(token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void snapPaymentUsingBcaKlikPay_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBCAKlikpay(token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBCAKlikpay_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBCAKlikpay(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingBCAKlikpay_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBCAKlikpay(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    public void snapPaymentUsingBankTransferAllBank() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferAllBank(email, token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void snapPaymentUsingBankTransferAllBank_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferAllBank(email, token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBankTransferAllBank_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBankTransferAllBank(email, token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingBankTransferAllBank_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferAllBank(email, token, transactionCallbackMock);
        Assert.assertFalse(midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void getBankBins_whenCallbackNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getBankBins(null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void getBankBins_whenInternetNotConnected() {
        midtransSDKSSpy.getBankBins(getBankBinCallbackMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        Mockito.verify(callbackCollaborator).onGetBankBinError();
    }

    /**
     * test gci payment
     */
    public void snapPaymentUsingGCI() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingGCI(token, giftCardNumber, giftCardPassword, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void snapPaymentUsingGCI_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingGCI(token, giftCardNumber, giftCardPassword, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingGCI_whenCallbackNull() {
        midtransSDKSSpy.paymentUsingGCI(token, giftCardNumber, giftCardPassword, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test  direct payment creditcard
     */

    @Test
    public void startPaymentUiFLow_whenUsingCreditCard() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.CREDIT_CARD);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startCreditCardUIFlow", contextMock, null);
    }

    /**
     * test direct payment bankTransfer
     */

    @Test
    public void startPaymentUiFLow_whenUsingBankTranfer() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.BANK_TRANSFER);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startBankTransferUIFlow", contextMock, null);
    }

    /**
     * test direct payment BCA bank transfer
     */

    @Test
    public void startPaymentUiFLow_whenUsingBcaBankTranfer() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.BANK_TRANSFER_BCA);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startBCABankTransferUIFlow", contextMock, null);
    }

    /**
     * test direct payment Permata bank transfer
     */

    @Test
    public void startPaymentUiFLow_whenUsingPermataBankTranfer() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.BANK_TRANSFER_PERMATA);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startPermataBankTransferUIFlow", contextMock, null);
    }

    /**
     * test direct payment Mandiri bank transfer
     */

    @Test
    public void startPaymentUiFLow_whenUsingMandiriBankTranfer() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.BANK_TRANSFER_MANDIRI);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startMandiriBankTransferUIFlow", contextMock, null);
    }

    /**
     * test direct payment BNI bank transfer
     */

    @Test
    public void startPaymentUiFLow_whenUsingBniBankTranfer() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.BANK_TRANSFER_BNI);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startBniBankTransferUIFlow", contextMock, null);
    }

    /**
     * test direct payment Other bank transfer
     */

    @Test
    public void startPaymentUiFLow_whenUsingOtherBankTranfer() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.BANK_TRANSFER_OTHER);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startOtherBankTransferUIFlow", contextMock, null);
    }

    /**
     * test direct payment BCA klikpay
     */

    @Test
    public void startPaymentUiFLow_whenUsingBcaKlikpay() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.BANK_TRANSFER_PERMATA);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startBCAKlikPayUIFlow", contextMock, null);
    }

    /**
     * test direct payment Klik BCA
     */

    @Test
    public void startPaymentUiFLow_whenUsingKlikBCA() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.KLIKBCA);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startKlikBCAUIFlow", contextMock, null);
    }

    /**
     * test direct payment Mandiri Click Pay
     */

    @Test
    public void startPaymentUiFLow_whenMandiriClickPay() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.MANDIRI_CLICKPAY);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startMandiriClickpayUIFlow", contextMock, null);
    }

    /**
     * test direct payment Mandiri Ecash
     */

    @Test
    public void startPaymentUiFLow_whenUsingMandirEcash() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.MANDIRI_ECASH);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startMandiriECashUIFlow", contextMock, null);
    }

    /**
     * test direct payment BRI Epay
     */

    @Test
    public void startPaymentUiFLow_whenUsingBriEpay() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.EPAY_BRI);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startBRIEpayUIFlow", contextMock, null);
    }

    /**
     * test direct payment CIMB Clicks
     */

    @Test
    public void startPaymentUiFLow_whenCimbClicks() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.CIMB_CLICKS);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startCIMBClicksUIFlow", contextMock, null);
    }

    /**
     * test direct payment TelkomselCash
     */

    @Test
    public void startPaymentUiFLow_whenUsingTCash() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.TELKOMSEL_CASH);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startTelkomselCashUIFlow", contextMock, null);
    }

    /**
     * test direct payment Indosat Dompetku
     */

    @Test
    public void startPaymentUiFLow_whenUsingIndosatDompetku() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.INDOSAT_DOMPETKU);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startIndosatDompetkuUIFlow", contextMock, null);
    }

    /**
     * test direct payment Xl Tunai
     */

    @Test
    public void startPaymentUiFLow_whenUsingXlTunai() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.XL_TUNAI);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startXlTunaiUIFlow", contextMock, null);
    }

    /**
     * test direct payment Indomaret
     */

    @Test
    public void startPaymentUiFLow_whenUsingIndomaret() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.INDOMARET);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startIndomaretUIFlow", contextMock, null);
    }

    /**
     * test direct payment Kioson
     */

    @Test
    public void startPaymentUiFLow_whenUsingKioson() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.KIOSON);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startKiosonUIFlow", contextMock, null);
    }

    /**
     * test direct payment GCI
     */

    @Test
    public void startPaymentUiFLow_whenUsingGci() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.GIFT_CARD_INDONESIA);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startGiftCardUIFlow", contextMock, null);
    }

    /**
     * test startBankTransferUIFlow method
     */

    @Test
    public void startBankTransferUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startBankTransferUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runBankTransfer(contextMock, snapTOken);
    }


    @Test
    public void startBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startBankTransferUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }


    /**
     * test startPermataBankTransferUIFlow method
     */

    @Test
    public void startPermataBankTransferUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startPermataBankTransferUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runPermataBankTransfer(contextMock, snapTOken);
    }


    @Test
    public void startPermataBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startPermataBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startPermataBankTransferUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startPermataBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startMandiriBankTransferUIFlow method
     */

    @Test
    public void startMandiriBankTransferUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriBankTransferUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runMandiriBankTransfer(contextMock, snapTOken);
    }


    @Test
    public void startMandiriBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startMandiriBankTransferUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startBniBankTransferUIFlow method
     */

    @Test
    public void startBniBankTransferUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startBniBankTransferUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runBniBankTransfer(contextMock, snapTOken);
    }


    @Test
    public void startBniBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBniBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startBniBankTransferUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBniBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startBCABankTransferUIFlow method
     */

    @Test
    public void startBCABankTransferUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCABankTransferUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runBCABankTransfer(contextMock, snapTOken);
    }


    @Test
    public void startBCABankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCABankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startBCABankTransferUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCABankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startBCAKlikPayUIFlow method
     */

    @Test
    public void startBCAKlikPayUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCAKlikPayUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runBCAKlikPay(contextMock, snapTOken);
    }


    @Test
    public void startBCAKlikPayUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCAKlikPayUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startBCAKlikPayUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCAKlikPayUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startKlikBCAUIFlow method
     */

    @Test
    public void startKlikBCAUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startKlikBCAUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runKlikBCA(contextMock, snapTOken);
    }


    @Test
    public void startKlikBCAUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startKlikBCAUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startKlikBCAUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startKlikBCAUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startMandiriClickpayUIFlow method
     */

    @Test
    public void startMandiriClickpayUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriClickpayUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runMandiriClickpay(contextMock, snapTOken);
    }


    @Test
    public void startMandiriClickpayUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriClickpayUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startMandiriClickpayUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriClickpayUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startMandiriECashUIFlow method
     */

    @Test
    public void startMandiriECashUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriECashUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runMandiriECash(contextMock, snapTOken);
    }


    @Test
    public void startMandiriECashUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriECashUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startMandiriECashUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriECashUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startCIMBClicksUIFlow method
     */

    @Test
    public void startCIMBClicksUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startCIMBClicksUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runCIMBClicks(contextMock, snapTOken);
    }


    @Test
    public void startCIMBClicksUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startCIMBClicksUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startCIMBClicksUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startCIMBClicksUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startTelkomselCashUIFlow method
     */

    @Test
    public void startTelkomselCashUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startTelkomselCashUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runTelkomselCash(contextMock, snapTOken);
    }


    @Test
    public void startTelkomselCashUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startTelkomselCashUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startTelkomselCashUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startTelkomselCashUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startIndosatDompetkuUIFlow method
     */

    @Test
    public void startIndosatDompetkuUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndosatDompetkuUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runIndosatDompetku(contextMock, snapTOken);
    }


    @Test
    public void startIndosatDompetkuUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndosatDompetkuUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startIndosatDompetkuUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndosatDompetkuUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startIndomaretUIFlow method
     */

    @Test
    public void startIndomaretUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndomaretUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runIndomaret(contextMock, snapTOken);
    }


    @Test
    public void startIndomaretUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndomaretUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startIndomaretUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndomaretUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startKiosonUIFlow method
     */

    @Test
    public void startKiosonUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startKiosonUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runKioson(contextMock, snapTOken);
    }


    @Test
    public void startKiosonUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startKiosonUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startKiosonUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startKiosonUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startXlTunaiUIFlow method
     */

    @Test
    public void startXlTunaiUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startXlTunaiUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runXlTunai(contextMock, snapTOken);
    }


    @Test
    public void startXlTunaiUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startXlTunaiUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startXlTunaiUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startXlTunaiUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startGiftCardUIFlow method
     */

    @Test
    public void startGiftCardUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startGiftCardUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runGci(contextMock, snapTOken);
    }


    @Test
    public void startGiftCardUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startGiftCardUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startGiftCardUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startGiftCardUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startBRIEpayUIFlow method
     */

    @Test
    public void startBRIEpayUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startBRIEpayUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runBRIEpay(contextMock, snapTOken);
    }


    @Test
    public void startBRIEpayUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBRIEpayUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startBRIEpayUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBRIEpayUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startOtherBankTransferUIFlow method
     */

    @Test
    public void startOtherBankTransferUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startOtherBankTransferUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runOtherBankTransfer(contextMock, snapTOken);
    }


    @Test
    public void startOtherBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startOtherBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startOtherBankTransferUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startOtherBankTransferUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * test startCreditCardUIFlow method
     */

    @Test
    public void startCreditCardUIFlow_whenPaymentMethodAlreadySelected() throws Exception {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.getTransactionRequest().getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        Whitebox.invokeMethod(midtransSDKSSpy, "startCreditCardUIFlow", contextMock, snapTOken);
        Mockito.verify(midtransSDKSSpy.uiflow).runCreditCard(contextMock, snapTOken);
    }


    @Test
    public void startCreditCardUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startCreditCardUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startCreditCardUIFlow_whenSdkIsrunning() throws Exception {
        when(midtransSDKSSpy.isRunning()).thenReturn(true);
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        Whitebox.invokeMethod(midtransSDKSSpy, "startCreditCardUIFlow", contextMock, snapTOken);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    /**
     * Obtain Promo Test
     */


    @Test
    public void obtainPromoByCardNumber_whenCallbackNull() {
        midtransSDKSSpy.obtainPromoByCardNumber(samplePromoId, sampleAmount, sampleClientKey, sampleCardNumber, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void obtainPromoByCardNumber_whenClientKeyNull() {
        midtransSDKSSpy.obtainPromoByCardNumber(samplePromoId, sampleAmount, sampleClientKey, null, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }


    @Test
    public void obtainPromoByCardToken_whenCallbackNull() {
        midtransSDKSSpy.obtainPromoByCardToken(samplePromoId, sampleAmount, sampleClientKey, sampleCardNumber, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void obtainPromoByCardToken_whenClientKeyNull() {
        midtransSDKSSpy.obtainPromoByCardToken(samplePromoId, sampleAmount, sampleClientKey, null, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }


    @Test
    public void obtainPromoByCardNumber_whenNetworkNotAvailabel() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.obtainPromoByCardNumber(samplePromoId, sampleAmount, sampleClientKey, sampleCardNumber, obtainPromoCallback);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());
    }

    @Test
    public void obtainPromoByCardNumber_whenNetworkAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.obtainPromoByCardNumber(samplePromoId, sampleAmount, sampleClientKey, sampleCardNumber, obtainPromoCallback);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning);
    }

    @Test
    public void obtainPromoByCardToken_whenNetworkNotAvailabel() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.obtainPromoByCardToken(samplePromoId, sampleAmount, sampleClientKey, cardToken, obtainPromoCallback);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());
    }

    @Test
    public void obtainPromoByCardToken_whenNetworkAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.obtainPromoByCardNumber(samplePromoId, sampleAmount, sampleClientKey, cardToken, obtainPromoCallback);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning);
    }

    /**
     * payment using mandiri bill pay
     */

    @Test
    public void paymentUsingMandiriBill() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriBillPay(token, email, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingMandiriBill_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriBillPay(token, email, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingMandiriBill_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingMandiriBillPay(token, email, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingMandiriBill_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriBillPay(token, email, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * payment using mandiri ClickPay
     */

    @Test
    public void paymentUsingMandiriClickpay() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriClickPay(token, sampleMandiriCardNumber, sampleTokenResponse, sampleInput3, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingMandiriClickpay_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);/**/
        midtransSDKSSpy.paymentUsingMandiriClickPay(token, sampleMandiriCardNumber, sampleTokenResponse, sampleInput3, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingMandiriClickpay_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingMandiriClickPay(token, sampleMandiriCardNumber, sampleTokenResponse, sampleInput3, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingMandiriClickpay_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriClickPay(token, sampleMandiriCardNumber, sampleTokenResponse, sampleInput3, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * payment using cimb click
     */

    @Test
    public void paymentUsingCimbClick() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCIMBClick(token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingCimbClick_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCIMBClick(token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingCimbClick_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingCIMBClick(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingCimbClick_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCIMBClick(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * payment using Xl Tunai
     */

    @Test
    public void paymentUsingXlTunai() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingXLTunai(token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingXlTunai_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingXLTunai(token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingXlTunai_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingXLTunai(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingXlTunai_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingXLTunai(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * payment using Indomaret
     */

    @Test
    public void paymentUsingIndomaret() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndomaret(token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingIndomaret_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndomaret(token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingIndomaret_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingIndomaret(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingIndomaret_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndomaret(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * payment using Indosat Dompetku
     */

    @Test
    public void paymentUsingIndosatDompetku() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndosatDompetku(token, sampleMsisdn, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingIndosatDompetku_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndosatDompetku(token, sampleMsisdn, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingIndosatDompetku_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingIndosatDompetku(token, sampleMsisdn, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingIndosatDompetku_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndosatDompetku(token, sampleMsisdn, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * payment using Kioson
     */

    @Test
    public void paymentUsingKioson() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKiosan(token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingKioson_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKiosan(token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingKioson_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingKiosan(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingKioson_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKiosan(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * payment using Epay BRI
     */

    @Test
    public void paymentUsingEpayBri() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKiosan(token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingEpayBri_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingEpayBRI(token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingEpayBri_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingEpayBRI(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingEpayBri_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingEpayBRI(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * payment using Telkomsel Cash
     */

    @Test
    public void paymentUsingTcash() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingTelkomselEcash(token, samplePhoneNumber, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingTcash_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingTelkomselEcash(token, samplePhoneNumber, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingTcash_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingTelkomselEcash(token, samplePhoneNumber, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingTcash_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingTelkomselEcash(token, samplePhoneNumber, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * Test Card Registration
     */

    @Test
    public void cardRegistration() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.cardRegistration(sampleCardNumber, sampleCvv, sampleExpMonth, sampleExpYear, cardRegistrationCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void cardRegistration_whenCallbackNull() {
        midtransSDKSSpy.cardRegistration(sampleCardNumber, sampleCvv, sampleExpMonth, sampleExpYear, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void cardRegistration_whenNetworkUnAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.cardRegistration(sampleCardNumber, sampleCvv, sampleExpMonth, sampleExpYear, null);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
    }

    /**
     * Test Save Card
     */

    @Test
    public void saveCard() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.saveCards(sampleUserId, savedCardsMock, saveCardCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void saveCard_whenCallbackNull() {
        midtransSDKSSpy.saveCards(sampleUserId, savedCardsMock, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void saveCard_whenSaveCardRequestNull() {
        midtransSDKSSpy.saveCards(sampleUserId, null, saveCardCallbackMock);
        Mockito.verify(callbackCollaborator).onError();
    }


    @Test
    public void saveCard_whenNetworkUnAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.saveCards(sampleUserId, savedCardsMock, saveCardCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
    }

    /**
     * test Get Saved Cards
     */

    @Test
    public void getCard() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getCards(sampleUserId, getCardCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void getCard_whenCallbackNull() {
        midtransSDKSSpy.getCards(sampleUserId, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }


    @Test
    public void getCard_whenNetworkUnAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.getCards(sampleUserId, getCardCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
    }

    /**
     * payment using Mandiri ECash
     */

    @Test
    public void paymentUsingMandiriEcash() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriEcash(token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
    }

    @Test
    public void paymentUsingMandiriEcash_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriEcash(token, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingMandiriEcash_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingMandiriEcash(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void paymentUsingMandiriEcash_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriEcash(token, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * get bank point
     */

    @Test
    public void getBankPoint() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        when(midtransSDKSSpy.readAuthenticationToken()).thenReturn(token);
        midtransSDKSSpy.getBanksPoint(cardToken, bankpointCallbackMock);
        Mockito.verify(transactionManager).getBanksPoint(token, cardToken, bankpointCallbackMock);
    }

    @Test
    public void getBankPoint_whenCallbackNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        when(midtransSDKSSpy.readAuthenticationToken()).thenReturn(token);
        midtransSDKSSpy.getBanksPoint(cardToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void getBankPoint_whenNetworkUnAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        when(midtransSDKSSpy.readAuthenticationToken()).thenReturn(token);
        midtransSDKSSpy.getBanksPoint(cardToken, bankpointCallbackMock);
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * delete card
     */

    @Test
    public void deleteCard() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.deleteCard(token, sampleCardNumber, deleteCardCallbackMock);
        Mockito.verify(transactionManager).deleteCard(token, sampleCardNumber, deleteCardCallbackMock);
    }

    @Test
    public void deleteCard_whenNetworkUnAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.deleteCard(token, sampleCardNumber, deleteCardCallbackMock);
        Mockito.verify(callbackCollaborator).onError();
    }

    /**
     * change sdk config
     */

    @Test
    public void changeSdkConfig() {
        midtransSDKSSpy.changeSdkConfig(sampleBaseUrl, sampleMerchantBaseUrl, sampleClientKey, sampleTimeOut);
        Assert.assertEquals(midtransSDKSSpy.getMerchantServerUrl(), sampleMerchantBaseUrl);
    }

    /**
     * notify transaction finished
     */

    @Test
    public void notifyTransactionFinish() {
        midtransSDKSSpy.setTransactionFinishedCallback(transactionFinishedCallbackMock);
        midtransSDKSSpy.notifyTransactionFinished(transactionResultMock);
        Mockito.verify(transactionFinishedCallbackMock).onTransactionFinished(transactionResultMock);
    }

    @Test
    public void notifyTransactionFinish_whenCallbackNull() {
        midtransSDKSSpy.setTransactionFinishedCallback(null);
        midtransSDKSSpy.notifyTransactionFinished(transactionResultMock);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

}
