package com.midtrans.sdk.corekit.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.models.BCAKlikPayDescriptionModel;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CardPaymentDetails;
import com.midtrans.sdk.corekit.models.CstoreEntity;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.DescriptionModel;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.KlikBCADescriptionModel;
import com.midtrans.sdk.corekit.models.MandiriBillPayTransferModel;
import com.midtrans.sdk.corekit.models.MandiriClickPayModel;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.payment.CustomerDetailRequest;
import com.midtrans.sdk.corekit.utilities.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by ziahaqi on 7/13/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Settings.class, Settings.Secure.class, LocalDataHandler.class, Utils.class, Log.class, TextUtils.class, Logger.class})

public class SDKUtilsTest {

    @Mock
    private TransactionRequest transactionRequestMock;

    @Mock
    private CustomerDetailRequest customerDetailRequest;

    @Mock
    private BillInfoModel billingInfoModelMock;
    @Mock
    private ArrayList<ItemDetails> itemDetailMock;
    @Mock
    private ArrayList<BillingAddress> billingAddressMock;
    @Mock
    private ArrayList<ShippingAddress> shippingAddressMock;
    private java.lang.String orderId = "01";
    private java.lang.Double amount = 20.0;
    @Mock
    private CustomerDetails costumerDetailMock;
    @Mock
    private MandiriBillPayTransferModel modelMock;
    @Mock
    private MandiriClickPayModel mandiriClickPayModelMock;
    @Mock
    private KlikBCADescriptionModel klikBCAModelMock;
    @Mock
    private String descriptionMock;
    @Mock
    private BCAKlikPayDescriptionModel bcaKlikPayMock;
    @Mock
    private Context contextMock;

    private MidtransSDK midtransSDK;
    private String paymermataName;
    @Mock
    private CstoreEntity cstoreMock;
    @Mock
    private DescriptionModel descriptionCIMBMock;
    @Mock
    private CardPaymentDetails cardPaymentDetailMock;
    private String msisdn = "msisdn";
    @Mock
    private TransactionRequest transactionRequestChangedMock;
    @Mock
    private Resources resourceMock;
    private String userDetail = "user_details";
    @Mock
    private UserDetail userDetailMock;
    @Mock
    private SharedPreferences mpreferenceMock;
    private String fullname = "fullname";
    private String email = "email@domain.com";
    private String phone = "phone";
    private String klikBCAUserId = "klikBCA";

    private String cardToken = "card_token";
    private boolean saveCard = false;
    private String token = "snap_token";

    @Mock
    private UserAddress userAddressMock1;
    @Mock
    private UserAddress userAddressMock2;
    @Mock
    private UserAddress userAddressMock3;
    @Mock
    private ArrayList<UserAddress> userAddressListMock;
    @Mock
    private ContentResolver contentResolverMock;
    private String cardPaymentType = "credit_card";
    private String bankTransferPaymentType = "bank_transfer";
    private String klikBCAPaymentType = "bca_klikbca";

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.mockStatic(LocalDataHandler.class);
        PowerMockito.mockStatic(Settings.class);
        PowerMockito.mockStatic(Settings.Secure.class);

        Mockito.when(transactionRequestMock.getBillInfoModel()).thenReturn(billingInfoModelMock);
        Mockito.when(transactionRequestMock.getItemDetails()).thenReturn(itemDetailMock);
        Mockito.when(transactionRequestMock.getBillingAddressArrayList()).thenReturn(billingAddressMock);
        Mockito.when(transactionRequestMock.getShippingAddressArrayList()).thenReturn(shippingAddressMock);
        Mockito.when(transactionRequestMock.getCustomerDetails()).thenReturn(costumerDetailMock);
        Mockito.when(transactionRequestMock.getOrderId()).thenReturn(orderId);
        Mockito.when(transactionRequestMock.getAmount()).thenReturn(amount);

        Mockito.when(customerDetailRequest.getEmail()).thenReturn(email);
        Mockito.when(customerDetailRequest.getFullName()).thenReturn(fullname);
        Mockito.when(customerDetailRequest.getPhone()).thenReturn(phone);
    }

    private void initSDK() {
        Mockito.when(klikBCAModelMock.getDescription()).thenReturn(descriptionMock);
        Mockito.when(contextMock.getApplicationContext()).thenReturn(contextMock);
        Mockito.when(contextMock.getResources()).thenReturn(resourceMock);

        MidtransSDK midtransSDK = (SdkCoreFlowBuilder.init(contextMock, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL)
                .enableLog(true)
                .setDefaultText("open_sans_regular.ttf")
                .setSemiBoldText("open_sans_semibold.ttf")
                .setBoldText("open_sans_bold.ttf")
                .buildSDK());
        midtransSDK = spy(midtransSDK);

        when(contextMock.getString(R.string.payment_permata)).thenReturn(paymermataName);
    }


    @Test
    public void getMandiriBillPayModel() throws Exception {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        mockStatic(SdkUtil.class);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(null);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertEquals(billingInfoModelMock, SdkUtil.getMandiriBillPayModel(transactionRequestMock).getBillInfoModel());
        verifyStatic();
    }

    @Test
    public void getMandiriClickPayRequestModel() throws ClassNotFoundException {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertEquals(mandiriClickPayModelMock, SdkUtil.getMandiriClickPayRequestModel(transactionRequestMock, mandiriClickPayModelMock).getMandiriClickPayModel());
    }

    @Test
    public void getKlikBCAModelTest() throws ClassNotFoundException {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertNotNull(SdkUtil.getKlikBCAModel(transactionRequestMock, klikBCAModelMock).getDescriptionModel());
    }

    @Test
    public void getBCAKlickPayModelTest() throws ClassNotFoundException {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertNotNull(SdkUtil.getBCAKlikPayModel(transactionRequestMock, bcaKlikPayMock).getTransactionDetails());
    }

    @Test
    public void getPermataBankModelTest() throws ClassNotFoundException {
        initSDK();

        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertEquals(itemDetailMock, SdkUtil.getPermataBankModel(transactionRequestMock).getItemDetails());

    }


    @Test
    public void getPermataBankModelTest_whenSDKNull() throws ClassNotFoundException {

        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());
        Assert.assertEquals(itemDetailMock, SdkUtil.getPermataBankModel(transactionRequestMock).getItemDetails());
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());

    }


    @Test
    public void getBcaBankTransferRequest() throws ClassNotFoundException {
        initSDK();

        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertEquals(itemDetailMock, SdkUtil.getBcaBankTransferRequest(transactionRequestMock).getItemDetails());

    }


    @Test
    public void getBcaBankTransferRequest_whenSDKNull() throws ClassNotFoundException {

        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());
        Assert.assertEquals(itemDetailMock, SdkUtil.getBcaBankTransferRequest(transactionRequestMock).getItemDetails());
        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());

    }


    @Test
    public void getIndomaretRequestModel() throws ClassNotFoundException {
        initSDK();

        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertNotNull(SdkUtil.getIndomaretRequestModel(transactionRequestMock, cstoreMock).getCustomerDetails());

    }


    @Test
    public void getBBMMoneyRequestModel() throws ClassNotFoundException {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertNotNull(SdkUtil.getBBMMoneyRequestModel(transactionRequestMock).getTransactionDetails());
    }

    @Test
    public void getCIMBClickPayModelTest() throws ClassNotFoundException {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertEquals(billingAddressMock, SdkUtil.getCIMBClickPayModel(transactionRequestMock, descriptionCIMBMock).getBillingAddresses());
    }

    @Test
    public void getMandiriECashModel() throws ClassNotFoundException {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());
        Assert.assertEquals(billingAddressMock, SdkUtil.getMandiriECashModel(transactionRequestMock, descriptionCIMBMock).getBillingAddresses());
    }

    @Test
    public void getCardTransferModel() throws ClassNotFoundException {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());
        Assert.assertEquals(billingAddressMock, SdkUtil.getCardTransferModel(transactionRequestMock, cardPaymentDetailMock).getBillingAddresses());
    }

    @Test
    public void getEpayBriBankModel() throws ClassNotFoundException {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());
        Assert.assertEquals(billingAddressMock, SdkUtil.getEpayBriBankModel(transactionRequestMock).getBillingAddresses());
    }

    @Test
    public void getIndosatDompetkuRequestModel() throws ClassNotFoundException {
        initSDK();

        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertEquals(itemDetailMock, SdkUtil.getIndosatDompetkuRequestModel(transactionRequestMock, msisdn).getItemDetails());

    }

    @Test
    public void getIndosatDompetkuRequestModel_whenMSISDNNull() throws ClassNotFoundException {
        initSDK();

        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertEquals(itemDetailMock, SdkUtil.getIndosatDompetkuRequestModel(transactionRequestMock, null).getItemDetails());

    }

    @Test
    public void getIndosatDompetkuRequestModel_whenMSISDNEmpty() throws ClassNotFoundException {
        initSDK();

        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());

        Assert.assertEquals(itemDetailMock, SdkUtil.getIndosatDompetkuRequestModel(transactionRequestMock, "").getItemDetails());

    }

    @Test
    public void initializeUserInfo() throws ClassNotFoundException {
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "getUserDetails", TransactionRequest.class)).toReturn(transactionRequestMock);

        Assert.assertEquals(transactionRequestMock, SdkUtil.initializeUserInfo(transactionRequestMock));

    }

    @Test
    public void initializePaymentDetails_whenCustomerDetailsNotNull() throws ClassNotFoundException {
        Mockito.when(transactionRequestMock.getCustomerDetails().getFirstName()).thenReturn(fullname);
        Mockito.when(transactionRequestMock.getCustomerDetails().getEmail()).thenReturn(email);
        Mockito.when(transactionRequestMock.getCustomerDetails().getPhone()).thenReturn(phone);

        Assert.assertEquals(customerDetailRequest.getEmail(), SdkUtil.initializePaymentDetails(transactionRequestMock).getEmail());
        Assert.assertEquals(customerDetailRequest.getFullName(), SdkUtil.initializePaymentDetails(transactionRequestMock).getFullName());
        Assert.assertEquals(customerDetailRequest.getPhone(), SdkUtil.initializePaymentDetails(transactionRequestMock).getPhone());
    }

    @Test
    public void initializePaymentDetails_whenCustomerDetailsNull() throws ClassNotFoundException {
        Assert.assertNotNull(SdkUtil.initializePaymentDetails(transactionRequestMock));
        Assert.assertNull(SdkUtil.initializePaymentDetails(transactionRequestMock).getEmail());
        Assert.assertNull(SdkUtil.initializePaymentDetails(transactionRequestMock).getPhone());
        Assert.assertNull(SdkUtil.initializePaymentDetails(transactionRequestMock).getFullName());
    }

    @Test
    public void getUserDetailTest() {
        initSDK();
        MidtransSDK.setmPreferences(mpreferenceMock);
        mockStatic(LocalDataHandler.class);
        when(LocalDataHandler.readObject(userDetail, UserDetail.class)).thenReturn(userDetailMock);
        when(contextMock.getString(R.string.user_details)).thenReturn(userDetail);
        when(userDetailMock.getUserFullName()).thenReturn(fullname);
        SdkUtil.getUserDetails(transactionRequestMock);

        verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());

    }


    @Test
    public void getUserDetailTest_whenUserDetailNotNull() {
        initSDK();
        MidtransSDK.setmPreferences(mpreferenceMock);
        mockStatic(LocalDataHandler.class);
        when(TextUtils.isEmpty(Matchers.anyString())).thenReturn(false);
        when(userDetailMock.getMerchantToken()).thenReturn("token");
        when((userDetailMock).getUserAddresses()).thenReturn(userAddressListMock);
        when(userDetailMock.getUserFullName()).thenReturn("name");
        when(LocalDataHandler.readObject(Matchers.anyString(), Matchers.any(Class.class))).thenReturn(userDetailMock);
        when(userDetailMock.getUserFullName()).thenReturn(fullname);
        SdkUtil.getUserDetails(transactionRequestMock);
        verifyStatic(Mockito.times(1));
        Logger.i(Matchers.anyString());

    }

    @Test
    public void getUserDetailTest_whenUserUserFullNameNull() {
        initSDK();
        MidtransSDK.setmPreferences(mpreferenceMock);
        mockStatic(LocalDataHandler.class);
        when(TextUtils.isEmpty(Matchers.anyString())).thenReturn(false);
        when(userDetailMock.getMerchantToken()).thenReturn("token");
        when((userDetailMock).getUserAddresses()).thenReturn(userAddressListMock);
        when(userDetailMock.getUserFullName()).thenReturn(null);
        when(LocalDataHandler.readObject(Matchers.anyString(), Matchers.any(Class.class))).thenReturn(userDetailMock);
        when(userDetailMock.getUserFullName()).thenReturn(fullname);
        SdkUtil.getUserDetails(transactionRequestMock);
        verifyStatic(Mockito.times(1));
        Logger.i(Matchers.anyString());

    }


    @Test
    public void extractUserAddress_whenBoth() {
        when(userAddressMock1.getAddressType()).thenReturn(Constants.ADDRESS_TYPE_BOTH);
        when(userAddressMock1.getAddress()).thenReturn("address1");
        when(userAddressMock1.getCity()).thenReturn("city1");
        when(userAddressMock1.getCountry()).thenReturn("indonesia1");
        when(userDetailMock.getUserFullName()).thenReturn("fullname");
        when(userDetailMock.getPhoneNumber()).thenReturn("phoneNumber");

        userAddressListMock = new ArrayList<>();
        TransactionRequest transactionRequest = new TransactionRequest(orderId, amount, Constants.PAYMENT_METHOD_NOT_SELECTED);

        userAddressListMock.add(userAddressMock1);
        TransactionRequest request = SdkUtil.extractUserAddress(userDetailMock, userAddressListMock, transactionRequest);
        Assert.assertEquals(1, request.getBillingAddressArrayList().size());
        Assert.assertEquals(1, request.getShippingAddressArrayList().size());

    }

    @Test
    public void extractUserAddress_whenTypeAddressShipping() {
        when(userAddressMock1.getAddressType()).thenReturn(Constants.ADDRESS_TYPE_SHIPPING);
        when(userAddressMock1.getAddress()).thenReturn("address1");
        when(userAddressMock1.getCity()).thenReturn("city1");
        when(userAddressMock1.getCountry()).thenReturn("indonesia1");
        when(userDetailMock.getUserFullName()).thenReturn("fullname");
        when(userDetailMock.getPhoneNumber()).thenReturn("phoneNumber");
        userAddressListMock = new ArrayList<>();
        TransactionRequest transactionRequest = new TransactionRequest(orderId, amount, Constants.PAYMENT_METHOD_NOT_SELECTED);
        userAddressListMock.add(userAddressMock1);

        TransactionRequest request = SdkUtil.extractUserAddress(userDetailMock, userAddressListMock, transactionRequest);
        Assert.assertEquals(0, request.getBillingAddressArrayList().size());
        Assert.assertEquals(1, request.getShippingAddressArrayList().size());
    }

    @Test
    public void extractUserAddress_whenBillingTypeShipping() {
        when(userAddressMock1.getAddressType()).thenReturn(Constants.ADDRESS_TYPE_BILLING);
        when(userAddressMock1.getAddress()).thenReturn("address1");
        when(userAddressMock1.getCity()).thenReturn("city1");
        when(userAddressMock1.getCountry()).thenReturn("indonesia1");
        when(userDetailMock.getUserFullName()).thenReturn("fullname");
        when(userDetailMock.getPhoneNumber()).thenReturn("phoneNumber");
        userAddressListMock = new ArrayList<>();
        TransactionRequest transactionRequest = new TransactionRequest(orderId, amount, Constants.PAYMENT_METHOD_NOT_SELECTED);
        userAddressListMock.add(userAddressMock1);

        TransactionRequest request = SdkUtil.extractUserAddress(userDetailMock, userAddressListMock, transactionRequest);
        Assert.assertEquals(1, request.getBillingAddressArrayList().size());
        Assert.assertEquals(0, request.getShippingAddressArrayList().size());
    }

    @Test
    public void getSnapTokenRequestModelTest() {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);
        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());
        Assert.assertEquals(itemDetailMock, SdkUtil.getSnapTokenRequestModel(transactionRequestMock).getItemDetails());
    }

    @Test
    public void getCreditCardPaymentRequest() {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(false);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);

        Assert.assertEquals(cardToken, SdkUtil.getCreditCardPaymentRequest(cardToken, saveCard, transactionRequestMock).getPaymentParams().getCardToken());
        Assert.assertEquals(saveCard, SdkUtil.getCreditCardPaymentRequest(cardToken, saveCard, transactionRequestMock).getPaymentParams().isSaveCard());
    }

    @Test
    public void getBankTransferPaymentRequest() {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(false);
        Assert.assertEquals(email, SdkUtil.getBankTransferPaymentRequest(email, bankTransferPaymentType).getCustomerDetails().getEmail());
    }

    @Test
    public void getKlikBCAPaymentRequest() {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(false);
        Assert.assertEquals(klikBCAUserId, SdkUtil.getKlikBCAPaymentRequest(klikBCAUserId, klikBCAPaymentType).getPaymentParams().getUserId());
    }

    @Test
    public void getEmailAddress() {
        Mockito.when(transactionRequestMock.getCustomerDetails().getEmail()).thenReturn(email);
        Assert.assertEquals(email, SdkUtil.getEmailAddress(transactionRequestMock));
    }
}
