package com.midtrans.sdk.coreflow.core;

import android.text.TextUtils;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import com.midtrans.sdk.coreflow.core.Constants;
import com.midtrans.sdk.coreflow.core.Logger;
import com.midtrans.sdk.coreflow.core.TransactionRequest;
import com.midtrans.sdk.coreflow.models.BillInfoModel;
import com.midtrans.sdk.coreflow.models.BillingAddress;
import com.midtrans.sdk.coreflow.models.CustomerDetails;
import com.midtrans.sdk.coreflow.models.ItemDetails;
import com.midtrans.sdk.coreflow.models.ShippingAddress;
import com.midtrans.sdk.coreflow.utilities.Utils;

/**
 * Created by ziahaqi on 7/13/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Utils.class,Log.class, TextUtils.class, Logger.class  })

public class TransactionRequestTest {

    TransactionRequest transactionRequest;
    private double amount = 10;
    private String orderId = "01";
    @Mock
    private CustomerDetails costumerDetailMock;
    private String clickType = "clickType";
    private boolean isScureCard = false;
    @Mock
    private ArrayList<ShippingAddress> shippingAddressMock;
    @Mock
    private ArrayList<BillingAddress> billingAddressMock;
    @Mock
    private BillInfoModel billingInfoMock;
    @Mock
    private ArrayList<ItemDetails> itemDetailsMock;

    @Before
    public void setup(){
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(Utils.class);
        transactionRequest = new TransactionRequest(orderId, amount);
    }

    @Test
    public void paymentMethod(){
        Assert.assertEquals(transactionRequest.paymentMethod, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    /*
     * constuctor
     */
    @Test
    public void constructor3ArgsTest(){
        transactionRequest = new TransactionRequest(orderId, amount, Constants.PAYMENT_METHOD_NOT_SELECTED);
        Assert.assertEquals(Constants.PAYMENT_METHOD_NOT_SELECTED, transactionRequest.getPaymentMethod());
        Assert.assertEquals(orderId, transactionRequest.getOrderId());
        Assert.assertTrue( transactionRequest.getAmount() == amount);

    }

    @Test
    public void constructor3ArgsTest_whenOrderIdNull(){
        transactionRequest = new TransactionRequest(null, amount, Constants.PAYMENT_METHOD_NOT_SELECTED);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());

    }

    @Test
    public void constructor2ArgsTest_whenAmount0(){
        transactionRequest = new TransactionRequest(orderId, 0, Constants.PAYMENT_METHOD_NOT_SELECTED);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());

    }


    @Test
    public void constructor2ArgsTest(){
        transactionRequest = new TransactionRequest(orderId, amount);
        Assert.assertEquals(Constants.PAYMENT_METHOD_NOT_SELECTED, transactionRequest.getPaymentMethod());
        Assert.assertEquals(orderId, transactionRequest.getOrderId());
        Assert.assertTrue(transactionRequest.getAmount() == amount);

    }

    @Test
    public void constructor2ArgsTest_whenOrderIdNull(){
        transactionRequest = new TransactionRequest(null, amount);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());

    }

    @Test
    public void constructor3ArgsTest_whenAmount0(){
        transactionRequest = new TransactionRequest(orderId, 0);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());

    }

    @Test
    public void customerDetailTest(){
        transactionRequest.setCustomerDetails(costumerDetailMock);
        Assert.assertEquals(costumerDetailMock, transactionRequest.getCustomerDetails());
    }

    @Test
    public void setCardPaymentInfoTest(){
        transactionRequest.setCardPaymentInfo(clickType, isScureCard);
        Assert.assertEquals(clickType, transactionRequest.getCardClickType());
        Assert.assertEquals(isScureCard, transactionRequest.isSecureCard());
    }

    @Test
    public void setShippingAddressTest(){
        transactionRequest.setShippingAddressArrayList(shippingAddressMock);
        Assert.assertEquals(shippingAddressMock, transactionRequest.getShippingAddressArrayList());

    }

    @Test
    public void setBillingAddressTest(){
        transactionRequest.setBillingAddressArrayList(billingAddressMock);
        Assert.assertEquals(billingAddressMock, transactionRequest.getBillingAddressArrayList());
    }

    @Test
    public void billingInfoTest(){
        transactionRequest.setBillInfoModel(billingInfoMock);
        Assert.assertEquals(billingInfoMock, transactionRequest.getBillInfoModel());
    }

    @Test
    public void itemDetailsTest(){
        transactionRequest.setItemDetails(itemDetailsMock);
        Assert.assertEquals(itemDetailsMock, transactionRequest.getItemDetails());
    }

    @Test
    public void enableUITest(){
        transactionRequest.enableUi(true);
        Assert.assertTrue(transactionRequest.isUiEnabled());
    }
}

