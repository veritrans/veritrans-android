package com.midtrans.sdk.corekit.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.models.BCAKlikPayDescriptionModel;
import com.midtrans.sdk.corekit.models.BcaBankTransferRequestModel;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CardPaymentDetails;
import com.midtrans.sdk.corekit.models.CstoreEntity;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.DescriptionModel;
import com.midtrans.sdk.corekit.models.ExpiryModel;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.KlikBCADescriptionModel;
import com.midtrans.sdk.corekit.models.MandiriBillPayTransferModel;
import com.midtrans.sdk.corekit.models.MandiriClickPayModel;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.BankTransferRequestModel;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.payment.CustomerDetailRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.securepreferences.SecurePreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by ziahaqi on 7/13/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Settings.class, Settings.Secure.class, LocalDataHandler.class, Utils.class,
        Log.class, TextUtils.class, Logger.class, MixpanelAnalyticsManager.class})
@PowerMockIgnore("javax.net.ssl.*")
public class SDKUtilsTest {

    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";
    private static final String PHONE = "+6213123123";
    private static final String EMAIL = "mail@mail.com";
    private static final String CUSTOM_FIELD1 = "cf1";
    private static final String CUSTOM_FIELD2 = "cf2";
    private static final String CUSTOM_FIELD3 = "cf3";

    @Mock
    private TransactionRequest transactionRequestMock;

    @Mock
    private Transaction transactionMock;

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
    private Double amount = 20d;
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
    private SecurePreferences mpreferenceMock;
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
    @Mock
    private CustomerDetails customerDetailsMock;
    @Mock
    private ExpiryModel expiryMock;
    @Mock
    private BankTransferRequestModel permataVaRequestModelMock;
    @Mock
    private BcaBankTransferRequestModel bcaVaRequestModelMock;
    @Mock
    private BankTransferRequestModel bniVaRequestModelMock;

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.mockStatic(LocalDataHandler.class);
        PowerMockito.mockStatic(Settings.class);
        PowerMockito.mockStatic(Settings.Secure.class);
        PowerMockito.mockStatic(MixpanelAnalyticsManager.class);

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


    @Test
    public void initializeUserInfo() throws ClassNotFoundException {
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "getUserDetails", TransactionRequest.class)).toReturn(transactionRequestMock);

        Assert.assertEquals(transactionRequestMock, SdkUtil.initializeUserInfo(transactionRequestMock));
    }


    @Test
    public void getUserDetailTest() {
        MidtransSDK.setmPreferences(mpreferenceMock);
        mockStatic(LocalDataHandler.class);
        when(LocalDataHandler.readObject(userDetail, UserDetail.class)).thenReturn(userDetailMock);
        when(userDetailMock.getUserFullName()).thenReturn(fullname);
        SdkUtil.getUserDetails(transactionRequestMock);

        verifyStatic(Mockito.times(1));
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
        TransactionRequest transactionRequest = new TransactionRequest(orderId, amount);
        CustomerDetails customerDetails = new CustomerDetails(FIRST_NAME, LAST_NAME, EMAIL, PHONE);
        transactionRequest.setCustomerDetails(customerDetails);

        userAddressListMock.add(userAddressMock1);

        TransactionRequest request = SdkUtil.extractUserAddress(userDetailMock, userAddressListMock, transactionRequest);
        Assert.assertEquals(1, request.getBillingAddressArrayList().size());
        Assert.assertEquals(1, request.getShippingAddressArrayList().size());

        Assert.assertNotNull(request.getCustomerDetails());
        Assert.assertEquals(request.getCustomerDetails().getShippingAddress(), request.getShippingAddressArrayList().get(0));
        Assert.assertEquals(request.getCustomerDetails().getBillingAddress(), request.getBillingAddressArrayList().get(0));

    }

    @Test
    public void extractUserAddress_whenTypeAddressShippingOnly() {
        when(userAddressMock1.getAddressType()).thenReturn(Constants.ADDRESS_TYPE_SHIPPING);
        when(userAddressMock1.getAddress()).thenReturn("address1");
        when(userAddressMock1.getCity()).thenReturn("city1");
        when(userAddressMock1.getCountry()).thenReturn("indonesia1");
        when(userDetailMock.getUserFullName()).thenReturn("fullname");
        when(userDetailMock.getPhoneNumber()).thenReturn("phoneNumber");
        userAddressListMock = new ArrayList<>();
        TransactionRequest transactionRequest = new TransactionRequest(orderId, amount);
        CustomerDetails customerDetails = new CustomerDetails(FIRST_NAME, LAST_NAME, EMAIL, PHONE);
        transactionRequest.setCustomerDetails(customerDetails);
        userAddressListMock.add(userAddressMock1);

        TransactionRequest request = SdkUtil.extractUserAddress(userDetailMock, userAddressListMock, transactionRequest);


        Assert.assertEquals(0, request.getBillingAddressArrayList().size());
        Assert.assertEquals(1, request.getShippingAddressArrayList().size());

        Assert.assertNotNull(request.getCustomerDetails());
        Assert.assertEquals(request.getCustomerDetails().getShippingAddress(), request.getShippingAddressArrayList().get(0));

    }

    @Test
    public void extractUserAddress_whenAddressBillingOnly() {
        when(userAddressMock1.getAddressType()).thenReturn(Constants.ADDRESS_TYPE_BILLING);
        when(userAddressMock1.getAddress()).thenReturn("address1");
        when(userAddressMock1.getCity()).thenReturn("city1");
        when(userAddressMock1.getCountry()).thenReturn("indonesia1");
        when(userDetailMock.getUserFullName()).thenReturn("fullname");
        when(userDetailMock.getPhoneNumber()).thenReturn("phoneNumber");
        userAddressListMock = new ArrayList<>();
        TransactionRequest transactionRequest = new TransactionRequest(orderId, amount);
        CustomerDetails customerDetails = new CustomerDetails(FIRST_NAME, LAST_NAME, EMAIL, PHONE);
        transactionRequest.setCustomerDetails(customerDetails);

        userAddressListMock.add(userAddressMock1);

        TransactionRequest request = SdkUtil.extractUserAddress(userDetailMock, userAddressListMock, transactionRequest);
        Assert.assertEquals(1, request.getBillingAddressArrayList().size());
        Assert.assertEquals(0, request.getShippingAddressArrayList().size());

        Assert.assertNotNull(request.getCustomerDetails());
        Assert.assertEquals(request.getCustomerDetails().getBillingAddress(), request.getBillingAddressArrayList().get(0));
    }

    @Test
    public void getSnapTokenRequestModelTest() {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(true);
        Mockito.when(transactionRequestMock.getExpiry()).thenReturn(expiryMock);
        Mockito.when(transactionRequestMock.getCustomField1()).thenReturn(CUSTOM_FIELD1);
        Mockito.when(transactionRequestMock.getCustomField2()).thenReturn(CUSTOM_FIELD2);
        Mockito.when(transactionRequestMock.getCustomField3()).thenReturn(CUSTOM_FIELD3);
        Mockito.when(transactionRequestMock.getPermataVa()).thenReturn(permataVaRequestModelMock);
        Mockito.when(transactionRequestMock.getBcaVa()).thenReturn(bcaVaRequestModelMock);
        Mockito.when(transactionRequestMock.getBniVa()).thenReturn(bniVaRequestModelMock);

        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);

        Assert.assertNotNull(transactionRequestMock.getBillInfoModel());
        Assert.assertEquals(itemDetailMock, SdkUtil.getSnapTokenRequestModel(transactionRequestMock).getItemDetails());
        Assert.assertEquals(SdkUtil.getSnapTokenRequestModel(transactionRequestMock).getExpiry(), expiryMock);
        Assert.assertEquals(SdkUtil.getSnapTokenRequestModel(transactionRequestMock).getCustomField1(), CUSTOM_FIELD1);
        Assert.assertEquals(SdkUtil.getSnapTokenRequestModel(transactionRequestMock).getCustomField2(), CUSTOM_FIELD2);
        Assert.assertEquals(SdkUtil.getSnapTokenRequestModel(transactionRequestMock).getCustomField3(), CUSTOM_FIELD3);
        Assert.assertEquals(SdkUtil.getSnapTokenRequestModel(transactionRequestMock).getPermataVa(), permataVaRequestModelMock);
        Assert.assertEquals(SdkUtil.getSnapTokenRequestModel(transactionRequestMock).getBcaVa(), bcaVaRequestModelMock);
        Assert.assertEquals(SdkUtil.getSnapTokenRequestModel(transactionRequestMock).getBniVa(), bniVaRequestModelMock);
    }

    @Test
    public void getCreditCardPaymentRequest() {
        Mockito.when(transactionRequestMock.isUiEnabled()).thenReturn(false);
        MemberModifier.stub(MemberMatcher.method(SdkUtil.class, "initializeUserInfo", TransactionRequest.class)).toReturn(transactionRequestMock);

        Assert.assertEquals(cardToken, SdkUtil.getCreditCardPaymentRequest(new CreditCardPaymentModel(cardToken, saveCard), transactionMock).getPaymentParams().getCardToken());
        Assert.assertEquals(saveCard, SdkUtil.getCreditCardPaymentRequest(new CreditCardPaymentModel(cardToken, saveCard), transactionMock).getPaymentParams().isSaveCard());
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
    public void getGciPaymentModelTest() {
        GCIPaymentRequest request = SdkUtil.getGCIPaymentRequest(SDKConfigTest.CARD_NUMBER, SDKConfigTest.PASSWORD);
        Assert.assertEquals(SDKConfigTest.CARD_NUMBER, request.getPaymentParams().getCardNumber());
        Assert.assertEquals(SDKConfigTest.PASSWORD, request.getPaymentParams().getPassword());
    }
}
