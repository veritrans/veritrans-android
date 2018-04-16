package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.BanksPointCallback;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.themes.BaseColorTheme;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.IndosatDompetkuRequest;
import com.midtrans.sdk.corekit.models.MandiriClickPayRequestModel;
import com.midtrans.sdk.corekit.models.PaymentDetails;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.MerchantData;
import com.midtrans.sdk.corekit.models.snap.PromoResponse;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.models.snap.params.NewMandiriClickPaymentParams;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.DanamonOnlinePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.NewMandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.securepreferences.SecurePreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by ziahaqi on 24/06/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalDataHandler.class, SdkUtil.class, Looper.class, Utils.class, Log.class, TextUtils.class,
        Logger.class, MixpanelAnalyticsManager.class, MidtransRestAdapter.class})
@PowerMockIgnore("javax.net.ssl.*")
public class MidtransAndroidSDKTest {
    @Mock
    Context contextMock;

    @Mock
    Resources resourceMock;
    @Mock
    ConnectivityManager connectivityManager;
    @Mock
    NetworkInfo networkInfo;
    @Mock
    boolean isconnected;
    @Mock
    SecurePreferences preference;
    @Mock
    IndosatDompetkuRequest indosatDompetkuRequestMock;
    @Mock
    private CardTokenCallback cardTokenCallbackMock;
    @Mock
    private CheckoutCallback checkoutCallbackMock;
    @Mock
    private TransactionOptionsCallback transactionOptionCallbackMock;
    @Mock
    private TransactionCallback transactionCallbackMock;
    @Mock
    private BankBinsCallback getBankBinCallbackMock;


    private String sampleToken = "token";
    private String sampleText = "text";
    private String sampleMerchantLogoStr = "merchantLogo";
    private String giftCardNumber = "4811111111111114";
    private String giftCardPassword = "123";

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
    private MandiriClickPayRequestModel mandiriClickPayRequestModelMock;
    private String userId = "klikBCA";
    private String cardToken = "card_token";
    private String email = "email@domain.com";
    @Mock
    private ISdkFlow uiflowMock;
    @Mock
    private Integer drawableIntCostumMock;
    @Mock
    private Drawable drawableCostumMock;
    @Mock
    private Integer drawableIntDefaultMock;
    @Mock
    private Drawable drawableDefaultMock;
    @Mock
    private ArrayList<PaymentMethodsModel> paymentMethodMock;
    @Mock
    private UserDetail userDetailMock;
    @Mock
    private MixpanelAnalyticsManager mixpanelMock;
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
    @Mock
    private DeleteCardCallback deleteCardCallbackMock;


    private String snapToken = "token";
    private String sampleUserId = "userId";
    private String sampleMsisdn = "msisdn";
    private String samplePhoneNumber = "08111111111";
    private String sampleCardNumber = "4811111111111114";
    private String sampleCvv = "123";
    private String sampleExpMonth = "01";
    private String sampleExpYear = "2020";
    private int sampleTimeOut = 1000;
    private String sampleClientKey = "client_key";
    private String sampleBaseUrl = "https://merchant.com";

    @Mock
    private SnapApiService snapServiceMock;
    @Mock
    private MidtransApiService midtransServiceMock;
    @Mock
    private MerchantApiService merchantServiceMock;

    @Mock
    private SnapServiceManager snapServiceManager;
    @Mock
    private MerchantServiceManager merchantServiceManager;
    @Mock
    private MidtransServiceManager midtransServiceManager;
    @Mock
    private MixpanelAnalyticsManager analyticsMock;
    @Mock
    private NewMandiriClickPaymentParams mandiriClickPayparamMock;
    @Mock
    private UIKitCustomSetting uiCustomSettingMock;
    @Mock
    private CreditCard creditCardMock;
    @Mock
    private List<PromoResponse> promosMock;
    @Mock
    private BaseColorTheme colorThemeMock;
    @Mock
    private Transaction transactionMock;
    @Mock
    private MerchantData merchantDataMock;
    @Mock
    private ArrayList<String> pointsMock;
    @Mock
    private PaymentDetails paymeDetailsMock;
    @Mock
    private GetTransactionStatusCallback getTransactionStatusMock;

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
        PowerMockito.mockStatic(MidtransRestAdapter.class);
        PowerMockito.mockStatic(MixpanelAnalyticsManager.class);

        Mockito.when(contextMock.getApplicationContext()).thenReturn(contextMock);
        Mockito.when(contextMock.getString(R.string.error_unable_to_connect)).thenReturn("not connected");
        Mockito.when(contextMock.getResources()).thenReturn(resourceMock);
        Mockito.when(contextMock.getResources().getDrawable(drawableIntDefaultMock)).thenReturn(drawableDefaultMock);

        Mockito.when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(false);

        Mockito.when(contextMock.getSharedPreferences("local.data", Context.MODE_PRIVATE)).thenReturn(preference);
        Mockito.when(SdkUtil.newPreferences(contextMock, "local.data")).thenReturn(preference);

        Mockito.when(SdkUtil.newSnapServiceManager(sampleTimeOut)).thenReturn(snapServiceManager);


        MidtransSDK midtransSDK = SdkCoreFlowBuilder.init(contextMock, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL)
                .enableLog(true)
                .buildSDK();

        midtransSDK.setSnapServiceManager(snapServiceManager);
        midtransSDK.setMerchantServiceManager(merchantServiceManager);
        midtransSDK.setMidtransServiceManager(midtransServiceManager);

        midtransSDKSSpy = Mockito.spy(midtransSDK);

    }

    @Test
    public void sampelTest() {
        Assert.assertTrue(true);
    }

    @Test
    public void getToken_whenCardTokenCallbackNull() {
        midtransSDKSSpy.getCardToken(cardTokenRequestMock, null);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }


    @Test
    public void getToken_whenCardTokenRequestNotNull_networkAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getCardToken(cardTokenRequestMock, cardTokenCallbackMock);
        Mockito.verify(midtransServiceManager).getToken(cardTokenRequestMock, cardTokenCallbackMock);
    }

    @Test
    public void getToken_whenCardTokenRequestNotNull_networkUnavailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.getCardToken(cardTokenRequestMock, cardTokenCallbackMock);
        Mockito.verify(cardTokenCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void getToken_whenCardTokenRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.getCardToken(cardTokenRequestMock, cardTokenCallbackMock);
        Mockito.verify(cardTokenCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void startPaymentUiFlow_whenUIFlowNotNull() throws Exception {
        when(transactionRequestMock.getPaymentMethod()).thenReturn(Constants.PAYMENT_METHOD_NOT_SELECTED);
        midtransSDKSSpy.uiflow = uiflowMock;
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        midtransSDKSSpy.startPaymentUiFlow(contextMock);
        PowerMockito.verifyPrivate(midtransSDKSSpy).invoke("runUiSdk", contextMock, null);
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
    public void merchantLogo() {
        String merchantLogo = sampleMerchantLogoStr;
        midtransSDKSSpy.setMerchantLogo(merchantLogo);
        Assert.assertEquals(merchantLogo, midtransSDKSSpy.getMerchantLogo());

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
        Mockito.verify(merchantServiceManager).checkout(Matchers.any(TokenRequestModel.class), Matchers.any(CheckoutCallback.class));
    }

    @Test
    public void checkout_whenCallbackNull() {
        midtransSDKSSpy.checkout(null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }


    @Test
    public void checkout_whenTransactionRequestUnAvailable() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
//        PowerMockito.doReturn(true).when(midtransSDKSSpy, "isTransactionRequestAvailable");

        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.checkout(checkoutCallbackMock);
        Mockito.verify(checkoutCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void checkout_whenNetworkUnAvailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.checkout(checkoutCallbackMock);
        Mockito.verify(checkoutCallbackMock).onError(Matchers.any(Throwable.class));
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
        Mockito.verify(merchantServiceManager).checkout(Matchers.any(TokenRequestModel.class), Matchers.any(CheckoutCallback.class));

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
        Mockito.verify(checkoutCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void checkoutWithUserId_whenNetworkUnAvailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.checkout(userId, checkoutCallbackMock);
        Mockito.verify(checkoutCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * get transaction option
     */

    @Test
    public void getSnapTransaction() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getTransactionOptions(snapToken, transactionOptionCallbackMock);
        Mockito.verify(snapServiceManager).getTransactionOptions(snapToken, transactionOptionCallbackMock);
    }

    @Test
    public void getSnapTransaction_whenCallbackNull() {
        midtransSDKSSpy.getTransactionOptions(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void getSnapTransaction_whenTokenNull() {
        when(TextUtils.isEmpty(null)).thenReturn(true);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getTransactionOptions(null, transactionOptionCallbackMock);
        Mockito.verify(transactionOptionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void getSnapTransaction_whenNetworkUnAvailable() {
        when(TextUtils.isEmpty(sdkTokenMock)).thenReturn(true);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.getTransactionOptions(null, transactionOptionCallbackMock);
        Mockito.verify(transactionOptionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * credit card payment
     */

    @Test
    public void snapPaymentUsingCard() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        CreditCardPaymentModel model = new CreditCardPaymentModel(snapToken, false);
        midtransSDKSSpy.paymentUsingCard(snapToken, model, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingCreditCard(Matchers.anyString(),
                Matchers.any(CreditCardPaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void snapPaymentCreditCard_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCard(cardToken, new CreditCardPaymentModel(snapToken, false), null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingCard_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCard(cardToken, new CreditCardPaymentModel(snapToken, false), transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingCard_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingCard(cardToken, new CreditCardPaymentModel(snapToken, false), transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * Bank Transfer / VA
     */

    @Test
    public void snapPaymentUsingBankTransferBCA() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBCA(email, snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingVa(Matchers.anyString(), Matchers.any(BankTransferPaymentRequest.class),
                Matchers.any(TransactionCallback.class));
    }

    @Test
    public void snapPaymentUsingBankTransferBCA_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBCA(email, snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBankTransferBCA_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBankTransferBCA(email, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingBankTransferBCA_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBCA(email, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingBankTransferPermata() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferPermata(email, snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingVa(Matchers.anyString(), Matchers.any(BankTransferPaymentRequest.class),
                Matchers.any(TransactionCallback.class));
    }

    @Test
    public void snapPaymentUsingBankTransferPermata_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferPermata(email, snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBankTransferPermata_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBankTransferPermata(email, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingBankTransferPermata_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferPermata(email, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }


    @Test
    public void snapPaymentUsingBankTransferBni() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBni(email, snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingVa(Matchers.anyString(), Matchers.any(BankTransferPaymentRequest.class),
                Matchers.any(TransactionCallback.class));
    }

    @Test
    public void snapPaymentUsingBankTransferBni_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBni(email, snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBankTransferBni_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBankTransferBni(email, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingBankTransferBni_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferBni(email, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }


    @Test
    public void snapPaymentUsingBankTransferAllBank() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferAllBank(email, snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingVa(Matchers.anyString(), Matchers.any(BankTransferPaymentRequest.class),
                Matchers.any(TransactionCallback.class));
    }

    @Test
    public void snapPaymentUsingBankTransferAllBank_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferAllBank(email, snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBankTransferAllBank_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBankTransferAllBank(email, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingBankTransferAllBank_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBankTransferAllBank(email, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }


    /**
     * payment using mandiri bill pay
     */

    @Test
    public void paymentUsingMandiriBill() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriBillPay(snapToken, email, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingVa(Matchers.anyString(),
                Matchers.any(BankTransferPaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingMandiriBill_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriBillPay(snapToken, email, null);

        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingMandiriBill_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingMandiriBillPay(snapToken, email, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void paymentUsingMandiriBill_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriBillPay(snapToken, email, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * payment using mandiri Click Pay
     */

    @Test
    public void paymentUsingMandiriClickPay() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriClickPay(snapToken, mandiriClickPayparamMock, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingMandiriClickPay(Matchers.anyString(),
                Matchers.any(NewMandiriClickPayPaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingMandiriClickPay_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriClickPay(snapToken, mandiriClickPayparamMock, null);

        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingMandiriClickPay_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingMandiriClickPay(snapToken, mandiriClickPayparamMock, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void paymentUsingMandiriClickPay_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriClickPay(snapToken, mandiriClickPayparamMock, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }


    /**
     * klik bca
     */

    @Test
    public void snapPaymentUsingKlikBCA() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKlikBCA(userId, snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingKlikBca(Matchers.anyString(), Matchers.any(KlikBCAPaymentRequest.class),
                Matchers.any(TransactionCallback.class));
    }

    @Test
    public void snapPaymentUsingKlikBca_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKlikBCA(userId, snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingKlikBCA_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKlikBCA(userId, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingKlikBCA_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingKlikBCA(userId, snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingBCAKlikpay() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBCAKlikpay(snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingBaseMethod(Matchers.anyString(),
                Matchers.any(BasePaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void snapPaymentUsingBcaKlikPay_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBCAKlikpay(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void snapPaymentUsingBCAKlikpay_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingBCAKlikpay(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingBCAKlikpay_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingBCAKlikpay(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
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
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.getBankBins(getBankBinCallbackMock);
        Mockito.verify(getBankBinCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void getBankBins() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getBankBins(getBankBinCallbackMock);
        Mockito.verify(snapServiceManager).getBankBins(getBankBinCallbackMock);
    }

    /**
     * test gci payment
     */
    @Test
    public void snapPaymentUsingGCI() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingGCI(snapToken, giftCardNumber, giftCardPassword, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingGci(Matchers.anyString(),
                Matchers.any(GCIPaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void snapPaymentUsingGCI_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingGCI(snapToken, giftCardNumber, giftCardPassword, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void snapPaymentUsingGCI_whenCallbackNull() {
        midtransSDKSSpy.paymentUsingGCI(snapToken, giftCardNumber, giftCardPassword, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void startPaymentUiFLow_whenSnapTokenEmpty() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startPaymentUiFlow", contextMock);
    }

    @Test
    public void startPaymentUiFLow_withSnapToken() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, snapToken);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("runUiSdk", contextMock, snapToken);
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
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.BCA_KLIKPAY);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startBCAKlikPayUIFlow", contextMock, null);
    }


    @Test
    public void startPaymentUiFLow_whenUsingGopay() throws Exception {
        midtransSDKSSpy.startPaymentUiFlow(contextMock, PaymentMethod.GO_PAY);
        PowerMockito.verifyPrivate(midtransSDKSSpy, Mockito.times(1)).invoke("startGoPayUIFlow", contextMock, null);
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

    @Test
    public void startBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBankTransferUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startPermataBankTransferUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runPermataBankTransfer(contextMock, snapToken);
    }


    @Test
    public void startPermataBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startPermataBankTransferUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriBankTransferUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runMandiriBankTransfer(contextMock, snapToken);
    }


    @Test
    public void startMandiriBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriBankTransferUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startBniBankTransferUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runBniBankTransfer(contextMock, snapToken);
    }


    @Test
    public void startBniBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBniBankTransferUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCABankTransferUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runBCABankTransfer(contextMock, snapToken);
    }


    @Test
    public void startBCABankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCABankTransferUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCAKlikPayUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runBCAKlikPay(contextMock, snapToken);
    }


    @Test
    public void startBCAKlikPayUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBCAKlikPayUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startKlikBCAUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runKlikBCA(contextMock, snapToken);
    }


    @Test
    public void startKlikBCAUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startKlikBCAUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriClickpayUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runMandiriClickpay(contextMock, snapToken);
    }


    @Test
    public void startMandiriClickpayUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriClickpayUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriECashUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runMandiriECash(contextMock, snapToken);
    }


    @Test
    public void startMandiriECashUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startMandiriECashUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startCIMBClicksUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runCIMBClicks(contextMock, snapToken);
    }


    @Test
    public void startCIMBClicksUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startCIMBClicksUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startTelkomselCashUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runTelkomselCash(contextMock, snapToken);
    }


    @Test
    public void startTelkomselCashUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startTelkomselCashUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndosatDompetkuUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runIndosatDompetku(contextMock, snapToken);
    }


    @Test
    public void startIndosatDompetkuUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndosatDompetkuUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndomaretUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runIndomaret(contextMock, snapToken);
    }


    @Test
    public void startIndomaretUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startIndomaretUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startKiosonUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runKioson(contextMock, snapToken);
    }


    @Test
    public void startKiosonUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startKiosonUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startXlTunaiUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runXlTunai(contextMock, snapToken);
    }


    @Test
    public void startXlTunaiUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startXlTunaiUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startGiftCardUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runGci(contextMock, snapToken);
    }


    @Test
    public void startGiftCardUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startGiftCardUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startBRIEpayUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runBRIEpay(contextMock, snapToken);
    }


    @Test
    public void startBRIEpayUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startBRIEpayUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startOtherBankTransferUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runOtherBankTransfer(contextMock, snapToken);
    }


    @Test
    public void startOtherBankTransferUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startOtherBankTransferUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
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
        Whitebox.invokeMethod(midtransSDKSSpy, "startCreditCardUIFlow", contextMock, snapToken);
        Mockito.verify(midtransSDKSSpy.uiflow).runCreditCard(contextMock, snapToken);
    }


    @Test
    public void startCreditCardUIFlow_whenTransactionRequestNull() throws Exception {
        midtransSDKSSpy.setTransactionRequest(null);
        Whitebox.invokeMethod(midtransSDKSSpy, "startCreditCardUIFlow", contextMock, snapToken);
        verifyStatic(Mockito.times(2));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }


    /**
     * payment using cimb click
     */

    @Test
    public void paymentUsingCimbClick() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCIMBClick(snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingBaseMethod(Matchers.anyString(),
                Mockito.any(BasePaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingCimbClick_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCIMBClick(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingCimbClick_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingCIMBClick(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void paymentUsingCimbClick_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingCIMBClick(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * payment using Xl Tunai
     */

    @Test
    public void paymentUsingXlTunai() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingXLTunai(snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingBaseMethod(Matchers.anyString(),
                Mockito.any(BasePaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingXlTunai_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingXLTunai(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingXlTunai_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingXLTunai(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void paymentUsingXlTunai_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingXLTunai(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * payment using Indomaret
     */

    @Test
    public void paymentUsingIndomaret() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndomaret(snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingBaseMethod(Matchers.anyString(),
                Mockito.any(BasePaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingIndomaret_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndomaret(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingIndomaret_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingIndomaret(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void paymentUsingIndomaret_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndomaret(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * payment using Indosat Dompetku
     */

    @Test
    public void paymentUsingIndosatDompetku() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndosatDompetku(snapToken, sampleMsisdn, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingIndosatDompetku(Matchers.anyString(),
                Mockito.any(IndosatDompetkuPaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingIndosatDompetku_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndosatDompetku(snapToken, sampleMsisdn, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingIndosatDompetku_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingIndosatDompetku(snapToken, sampleMsisdn, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void paymentUsingIndosatDompetku_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingIndosatDompetku(snapToken, sampleMsisdn, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * payment using Kioson
     */

    @Test
    public void paymentUsingKioson() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKiosan(snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingBaseMethod(Matchers.anyString(),
                Mockito.any(IndosatDompetkuPaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingKioson_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKiosan(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingKioson_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingKiosan(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));

    }

    @Test
    public void paymentUsingKioson_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingKiosan(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * payment using Epay BRI
     */

    @Test
    public void paymentUsingEpayBri() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingEpayBRI(snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingBaseMethod(Matchers.anyString(),
                Mockito.any(IndosatDompetkuPaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingEpayBri_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingEpayBRI(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingEpayBri_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingEpayBRI(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void paymentUsingEpayBri_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingEpayBRI(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * payment using Telkomsel Cash
     */

    @Test
    public void paymentUsingTcash() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingTelkomselEcash(snapToken, samplePhoneNumber, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingTelkomselCash(Matchers.anyString(),
                Mockito.any(TelkomselEcashPaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingTcash_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingTelkomselEcash(snapToken, samplePhoneNumber, null);

        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingTcash_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingTelkomselEcash(snapToken, samplePhoneNumber, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void paymentUsingTcash_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingTelkomselEcash(snapToken, samplePhoneNumber, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * Test Card Registration
     */

    @Test
    public void cardRegistration() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.cardRegistration(sampleCardNumber, sampleCvv, sampleExpMonth, sampleExpYear, cardRegistrationCallbackMock);
        Mockito.verify(midtransServiceManager).cardRegistration(sampleCardNumber, sampleCvv, sampleExpMonth,
                sampleExpYear, sampleClientKey, cardRegistrationCallbackMock);

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
        midtransSDKSSpy.cardRegistration(sampleCardNumber, sampleCvv, sampleExpMonth, sampleExpYear, cardRegistrationCallbackMock);
        Mockito.verify(cardRegistrationCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * Test Save Card
     */

    @Test
    public void saveCard() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.saveCards(sampleUserId, savedCardsMock, saveCardCallbackMock);
        Mockito.verify(merchantServiceManager).saveCards(Matchers.anyString(),
                Matchers.anyList(), Matchers.any(SaveCardCallback.class));
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
        Mockito.verify(saveCardCallbackMock).onError(Matchers.any(Throwable.class));
    }


    @Test
    public void saveCard_whenNetworkUnAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.saveCards(sampleUserId, savedCardsMock, saveCardCallbackMock);
        Mockito.verify(saveCardCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * test Get Saved Cards
     */

    @Test
    public void getCard() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getCards(sampleUserId, getCardCallbackMock);
        Mockito.verify(merchantServiceManager).getCards(sampleUserId, getCardCallbackMock);
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
        Mockito.verify(getCardCallbackMock).onError(Matchers.any(Throwable.class));

    }

    /**
     * payment using Mandiri ECash
     */

    @Test
    public void paymentUsingMandiriEcash() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriEcash(sampleToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingBaseMethod(Matchers.anyString(),
                Matchers.any(BasePaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingMandiriEcash_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriEcash(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingMandiriEcash_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingMandiriEcash(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void paymentUsingMandiriEcash_whenTransactionRequestNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingMandiriEcash(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * payment using gopay
     */

    @Test
    public void paymentUsingGopay() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingGoPay(snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingGoPay(Matchers.anyString(),
                Matchers.any(GoPayPaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingGopay_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingGoPay(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingGopay_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingGoPay(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * payment using danamon online
     */

    @Test
    public void paymentUsingDanamonOnline() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingDanamonOnline(snapToken, transactionCallbackMock);
        Mockito.verify(snapServiceManager).paymentUsingDanamonOnline(Matchers.anyString(),
                Matchers.any(DanamonOnlinePaymentRequest.class), Matchers.any(TransactionCallback.class));
    }

    @Test
    public void paymentUsingDanamonOnline_whenCallbackNull() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.paymentUsingDanamonOnline(snapToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void paymentUsingDanamonOnline_whenNetworkUnavailable() {
        midtransSDKSSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.paymentUsingDanamonOnline(snapToken, transactionCallbackMock);
        Mockito.verify(transactionCallbackMock).onError(Matchers.any(Throwable.class));
    }


    /**
     * get bank point
     */

    @Test
    public void getBankPoint() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        when(midtransSDKSSpy.readAuthenticationToken()).thenReturn(snapToken);
        midtransSDKSSpy.getBanksPoint(cardToken, bankpointCallbackMock);
        Mockito.verify(snapServiceManager).getBanksPoint(snapToken, cardToken, bankpointCallbackMock);
    }

    @Test
    public void getBankPoint_whenCallbackNull() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        when(midtransSDKSSpy.readAuthenticationToken()).thenReturn(snapToken);
        midtransSDKSSpy.getBanksPoint(cardToken, null);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void getBankPoint_whenNetworkUnAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        when(midtransSDKSSpy.readAuthenticationToken()).thenReturn(snapToken);
        midtransSDKSSpy.getBanksPoint(cardToken, bankpointCallbackMock);
        Mockito.verify(bankpointCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * delete card
     */

    @Test
    public void deleteCard() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.deleteCard(snapToken, sampleCardNumber, deleteCardCallbackMock);
        Mockito.verify(snapServiceManager).deleteCard(snapToken, sampleCardNumber, deleteCardCallbackMock);
    }

    @Test
    public void deleteCard_whenNetworkUnAvailable() {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.deleteCard(snapToken, sampleCardNumber, deleteCardCallbackMock);
        Mockito.verify(deleteCardCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * change sdk config
     */

    @Test
    public void changeSdkConfig() {
        midtransSDKSSpy.changeSdkConfig(sampleBaseUrl, sampleBaseUrl, sampleClientKey, sampleTimeOut);
        Assert.assertEquals(midtransSDKSSpy.getMerchantServerUrl(), sampleBaseUrl);
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

    @Test
    public void getFlowTest() throws Exception {
        String flow = Whitebox.invokeMethod(midtransSDKSSpy, "getFlow", BaseSdkBuilder.CORE_FLOW);
        Assert.assertEquals(MixpanelAnalyticsManager.CORE_FLOW, flow);

        flow = Whitebox.invokeMethod(midtransSDKSSpy, "getFlow", BaseSdkBuilder.UI_FLOW);
        Assert.assertEquals(MixpanelAnalyticsManager.UI_FLOW, flow);

        flow = Whitebox.invokeMethod(midtransSDKSSpy, "getFlow", BaseSdkBuilder.WIDGET);
        Assert.assertEquals(MixpanelAnalyticsManager.WIDGET, flow);
    }

    @Test
    public void setGetMerchantNameTest() throws Exception {
        String merchantName = "mercant midtrans";
        midtransSDKSSpy.setMerchantName(merchantName);
        Assert.assertEquals(merchantName, midtransSDKSSpy.getMerchantName());
    }

    @Test
    public void setGetFlowTest() throws Exception {
        String flow = "core";
        midtransSDKSSpy.setFlow(flow);
        Assert.assertEquals(flow, midtransSDKSSpy.getFlow());
    }

    @Test
    public void getContextTest() throws Exception {
        Assert.assertEquals(contextMock, midtransSDKSSpy.getContext());
    }

    @Test
    public void getmMixpanelAnalyticsManagerTest() throws Exception {
        String deviceid = "dev_242ed234";
        when(SdkUtil.getDeviceId(contextMock)).thenReturn(deviceid);
        when(SdkUtil.newMixpanelAnalyticsManager(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),
                Matchers.anyString(), Matchers.anyString(), Matchers.anyBoolean(), Matchers.any(Context.class))).thenReturn(analyticsMock);
        MixpanelAnalyticsManager analyticsManager = midtransSDKSSpy.getmMixpanelAnalyticsManager();
        Assert.assertEquals(analyticsManager, analyticsMock);
    }

    @Test
    public void setGetAuthenticationTokenTest() throws Exception {
        midtransSDKSSpy.setAuthenticationToken(snapToken);
        Assert.assertEquals(snapToken, midtransSDKSSpy.readAuthenticationToken());
    }

    @Test
    public void cardRegistrationUi() throws Exception {
        midtransSDKSSpy.uiflow = uiflowMock;
        midtransSDKSSpy.UiCardRegistration(contextMock, cardRegistrationCallbackMock);
        Mockito.verify(uiflowMock).runCardRegistration(contextMock, cardRegistrationCallbackMock);
    }

    @Test
    public void cardRegistrationUi_whenUiFlowNull() throws Exception {
        midtransSDKSSpy.UiCardRegistration(contextMock, cardRegistrationCallbackMock);
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void isSnapTokenAvailable() throws Exception {
        Assert.assertEquals(true, Whitebox.invokeMethod(midtransSDKSSpy, "snapTokenAvailable", snapToken));
    }

    @Test
    public void isSnapTokenAvailable_whenTokenEMpty() throws Exception {
        when(TextUtils.isEmpty(null)).thenReturn(true);
        Assert.assertEquals(false, Whitebox.invokeMethod(midtransSDKSSpy, "snapTokenAvailable", null));
    }

    @Test
    public void isMerchantUrlAvailable() throws Exception {
        Assert.assertEquals(true, Whitebox.invokeMethod(midtransSDKSSpy, "merchantBaseUrlAvailable"));
    }
    @Test
    public void isMerchantUrlAvailable_whenMerchantUrlEmpty() throws Exception {
        when(TextUtils.isEmpty(Matchers.anyString())).thenReturn(true);
        Assert.assertEquals(false, Whitebox.invokeMethod(midtransSDKSSpy, "merchantBaseUrlAvailable"));
    }

    @Test
    public void getSdkBaseUrl() throws Exception {
        Assert.assertEquals(BuildConfig.SNAP_BASE_URL, midtransSDKSSpy.getSdkBaseUrl());
    }

    @Test
    public void getSdkTimeout() throws Exception {
        Assert.assertEquals(30, midtransSDKSSpy.getRequestTimeOut());
    }

    @Test
    public void getUiCustomSetting_whenNull() throws Exception {
        midtransSDKSSpy.setUIKitCustomSetting(null);
        Assert.assertNotEquals(uiCustomSettingMock, midtransSDKSSpy.getUIKitCustomSetting());
        Assert.assertNotNull(midtransSDKSSpy.getUIKitCustomSetting());
    }

    @Test
    public void getUiCustomSetting() throws Exception {
        midtransSDKSSpy.setUIKitCustomSetting(uiCustomSettingMock);
        Assert.assertEquals(uiCustomSettingMock, midtransSDKSSpy.getUIKitCustomSetting());
    }

    @Test
    public void getCreditCard() throws Exception {
        midtransSDKSSpy.setCreditCard(creditCardMock);
        midtransSDKSSpy.setTransaction(null);
        Assert.assertEquals(creditCardMock, midtransSDKSSpy.getCreditCard());
    }

    @Test
    public void getCreditCard_whenNull() throws Exception {
        transactionMock.setCreditCard(null);
        Assert.assertNotEquals(creditCardMock, midtransSDKSSpy.getCreditCard());
        Assert.assertNotNull(midtransSDKSSpy.getCreditCard());
    }

    @Test
    public void setGetPromos() throws Exception {
        midtransSDKSSpy.setPromoResponses(promosMock);
        Assert.assertEquals(promosMock, midtransSDKSSpy.getPromoResponses());
    }

    @Test
    public void setGetColorTheme() throws Exception {
        midtransSDKSSpy.setColorTheme(colorThemeMock);
        Assert.assertEquals(colorThemeMock, midtransSDKSSpy.getColorTheme());
    }

    @Test
    public void getBankPointEnable() throws Exception {
        when(transactionMock.getMerchantData()).thenReturn(merchantDataMock);
        when(merchantDataMock.getPointBanks()).thenReturn(pointsMock);
        midtransSDKSSpy.setTransaction(transactionMock);
        Assert.assertEquals(pointsMock, midtransSDKSSpy.getBanksPointEnabled());
    }

    @Test
    public void getMerchantData() throws Exception {
        when(transactionMock.getMerchantData()).thenReturn(merchantDataMock);
        when(merchantDataMock.getPointBanks()).thenReturn(pointsMock);
        midtransSDKSSpy.setTransaction(transactionMock);
        Assert.assertEquals(merchantDataMock, midtransSDKSSpy.getMerchantData());
    }

    @Test
    public void getMerchantData_whenMerchantDataNull() throws Exception {
        when(transactionMock.getMerchantData()).thenReturn(null);
        midtransSDKSSpy.setTransaction(transactionMock);
        Assert.assertNotEquals(merchantDataMock, midtransSDKSSpy.getMerchantData());
    }

    @Test
    public void getTransaction() throws Exception {
        midtransSDKSSpy.setTransaction(transactionMock);
        Assert.assertEquals(transactionMock, midtransSDKSSpy.getTransaction());
    }

    @Test
    public void getCardRegisterCallback() throws Exception {
        midtransSDKSSpy.uiflow = uiflowMock;
        midtransSDKSSpy.UiCardRegistration(contextMock, cardRegistrationCallbackMock);
        Assert.assertEquals(cardRegistrationCallbackMock, midtransSDKSSpy.getUiCardRegistrationCallback());
    }

    @Test
    public void setGetPaymentDetails() throws Exception {
        midtransSDKSSpy.setPaymentDetails(paymeDetailsMock);
        Assert.assertEquals(paymeDetailsMock, midtransSDKSSpy.getPaymentDetails());
    }

    @Test
    public void resetPaymentDetails() throws Exception {
        midtransSDKSSpy.setPaymentDetails(paymeDetailsMock);
        midtransSDKSSpy.resetPaymentDetails();
        Assert.assertNotEquals(paymeDetailsMock, midtransSDKSSpy.getPaymentDetails());
    }

    @Test
    public void getServiceManager() throws Exception {
        Assert.assertEquals(snapServiceManager, midtransSDKSSpy.getSnapServiceManager());
        Assert.assertEquals(midtransServiceManager, midtransSDKSSpy.getMidtransServiceManager());
        Assert.assertEquals(merchantServiceManager, midtransSDKSSpy.getMerchantServiceManager());
    }

    @Test
    public void getTransactionStatus() throws Exception {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(true);
        midtransSDKSSpy.getTransactionStatus(snapToken, getTransactionStatusMock);
        Mockito.verify(snapServiceManager).getTransactionStatus(snapToken, getTransactionStatusMock);
    }
    @Test
    public void getTransactionStatus_whenNetworkUnAvailable() throws Exception {
        when(midtransSDKSSpy.isNetworkAvailable()).thenReturn(false);
        midtransSDKSSpy.getTransactionStatus(snapToken, getTransactionStatusMock);
        Mockito.verify(getTransactionStatusMock).onError(Matchers.any(Throwable.class));
    }
}
