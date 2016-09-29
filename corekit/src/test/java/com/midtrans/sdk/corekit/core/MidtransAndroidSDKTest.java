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

import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.models.BBMCallBackUrl;
import com.midtrans.sdk.corekit.models.BBMMoneyRequestModel;
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
import com.midtrans.sdk.corekit.models.UserDetail;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by ziahaqi on 24/06/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalDataHandler.class, SdkUtil.class, Looper.class, Utils.class, Log.class, TextUtils.class, Logger.class})

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
    private CheckoutCallback checkoutCallbackMock;
    private TransactionOptionsCallback transactionOptionCallbackMock;
    private TransactionCallback transactionCallbackMock;
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
    @Mock
    private BBMMoneyRequestModel bbmMoneyRequestModelMock;
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
    private BBMCallBackUrl bbmCallbackUrlMock;
    @Mock
    private ArrayList<PaymentMethodsModel> paymentMethodMock;
    @Mock
    private UserDetail userDetailMock;
    @Mock
    private MixpanelAnalyticsManager mixpanelMock;
    @Mock
    private SnapTransactionManager snapTransctionmanagerMock;
    private String snapTOken = "token";

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
        this.cardTokenCallbackMock = callbackSample.getCardTokenCallback();

        MidtransSDK midtransSDK = SdkCoreFlowBuilder.init(contextMock, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL)
                .enableLog(true)
                .setDefaultText("open_sans_regular.ttf")
                .setSemiBoldText("open_sans_semibold.ttf")
                .setBoldText("open_sans_bold.ttf")
                .buildSDK();
        Mockito.when(midtransSDK.readAuthenticationToken()).thenReturn(sdkTokenMock);
        midtransSDK.setTransactionManager(transactionManager);
        transactionManager.setAnalyticsManager(mixpanelMock);
        midtransSDKSSpy = spy(midtransSDK);

    }

    @Test
    public void sampelTest() {
        Assert.assertTrue(true);
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
    public void startRegisterCardUIFlow_whenUIFlowNotNull() {
        midtransSDKSSpy.uiflow = uiflowMock;
        midtransSDKSSpy.startRegisterCardUIFlow(contextMock);
        Mockito.verify(uiflowMock).runRegisterCard(contextMock);
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
    public void getMerhcantToken() {
        PowerMockito.mockStatic(LocalDataHandler.class);
        when(LocalDataHandler.readObject(contextMock.getString(R.string.user_details), UserDetail.class)).thenReturn(userDetailMock);
        when(userDetailMock.getMerchantToken()).thenReturn(sampleToken);
        Assert.assertEquals(sampleToken, midtransSDKSSpy.getMerchantToken());

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

    @Test
    public void checkout() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.checkout(checkoutCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
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
        midtransSDKSSpy.paymentUsingCard(cardToken, token, false, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning);
    }

    @Test
    public void snapPaymentUsingCard_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCard(cardToken, token, false, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingCard_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingCard(cardToken, token, false, transactionCallbackMock);
        Assert.assertEquals(false, midtransSDKSSpy.isRunning());
        Mockito.verify(callbackCollaborator).onError();
    }

    @Test
    public void snapPaymentUsingBankTransferBCA() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBCA(email, token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
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
    public void snapPaymentUsingKlikBCA() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKlikBCA(userId, token, transactionCallbackMock);
        Assert.assertEquals(true, midtransSDKSSpy.isRunning());
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
}
