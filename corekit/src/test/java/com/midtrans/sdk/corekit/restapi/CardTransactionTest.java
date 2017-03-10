package com.midtrans.sdk.corekit.restapi;

import android.util.Log;

import com.midtrans.sdk.corekit.APIClientMain;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.core.MerchantRestAPI;
import com.midtrans.sdk.corekit.core.MidtransRestAPI;
import com.midtrans.sdk.corekit.models.CardTransfer;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.CountDownLatch;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ziahaqi on 27/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, android.text.TextUtils.class})
public class CardTransactionTest extends APIClientMain {
    private TokenDetailsResponse mTokenDetailResponse;
    private TransactionResponse mTranscsationResponseSuccess;
    private RetrofitError mTranscsationResponseErrorExpired;
    private RetrofitError mTranscsationResponseErrorOrderExist;


    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.text.TextUtils.class);
    }
    /*
     * GET TOKEN
     */

    @Test
    public void vt_testGetTokenSuccess() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        MidtransRestAPI paymentAPI = createVeritransPaymentAPIMock(VT_GET_TOKEN_SUCCESS, 200, "success");
        paymentAPI.get3DSToken(CARD_NUMBER, CARD_CVV, CARD_EXP_MONTH, CARD_EXP_YEAR,
                SDKConfigTest.CLIENT_KEY, null, false, false, 0.0, null, null, false, new Callback<TokenDetailsResponse>() {
                    @Override
                    public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                        mTokenDetailResponse = tokenDetailsResponse;
                        latch.countDown();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mTokenDetailResponse = null;
                        latch.countDown();
                    }
                });
        latch.await();
        Assert.assertNotNull(mTokenDetailResponse);
        Assert.assertEquals("200", mTokenDetailResponse.getStatusCode());
    }


    /*
     * PAYMENT
     */
    @Test
    public void testCardPaymentSuccess() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        MerchantRestAPI paymentAPI = createMerchantPaymentAPIMock(MERCHANT_PAYMENT_SUCCESS, 200, "success");
        CardTransfer cardTransfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_permata_bank_transfer.json");

        Assert.assertNotNull(cardTransfer);
        paymentAPI.paymentUsingCard(X_AUTH, cardTransfer, new Callback<TransactionResponse>() {
            @Override
            public void success(TransactionResponse transactionResponse, Response response) {
                mTranscsationResponseSuccess = transactionResponse;
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                mTranscsationResponseSuccess = null;
                latch.countDown();
            }
        });
        latch.await();
        Assert.assertNotNull(mTranscsationResponseSuccess);
    }

    @Test
    public void testCardPaymentFailed_whenTokenExpired() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        MerchantRestAPI paymentAPI = createMerchantPaymentAPIMock(MERCHANT_PAYMENT_FAILED_EXPIRED, 401, "tokenMissingOrExpired");
        CardTransfer cardTransfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_permata_bank_transfer.json");
        Assert.assertNotNull(cardTransfer);
        Assert.assertNull(mTranscsationResponseErrorExpired);
        paymentAPI.paymentUsingCard(X_AUTH, cardTransfer, new Callback<TransactionResponse>() {
            @Override
            public void success(TransactionResponse transactionResponse, Response response) {
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                mTranscsationResponseErrorExpired = error;
                latch.countDown();
            }
        });
        latch.await();
        Assert.assertNotNull(mTranscsationResponseErrorExpired);
    }


    @Test
    public void testCardPaymentFailed_whenOrderIdAlreadyExist() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        MerchantRestAPI paymentAPI = createMerchantPaymentAPIMock(MERCHANT_PAYMENT_SUCCESS, 406, "OrderAlreadyExist");
        CardTransfer cardTransfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_permata_bank_transfer.json");
        Assert.assertNotNull(cardTransfer);
        Assert.assertNull(mTranscsationResponseErrorOrderExist);
        paymentAPI.paymentUsingCard(X_AUTH, cardTransfer, new Callback<TransactionResponse>() {
            @Override
            public void success(TransactionResponse transactionResponse, Response response) {
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                mTranscsationResponseErrorOrderExist = error;
                latch.countDown();
            }
        });
        latch.await();
        Assert.assertNotNull(mTranscsationResponseErrorOrderExist);
    }
}
