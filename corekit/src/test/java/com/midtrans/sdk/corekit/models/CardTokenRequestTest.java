package com.midtrans.sdk.corekit.models;

import android.text.TextUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by ziahaqi on 7/14/16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({TextUtils.class})
public class CardTokenRequestTest {
    private CardTokenRequest cardTokenRequest;
    private String cardExpiredYear = "2020";
    private String cardNumber = "4811111111111114";
    private String cardCVV = "123";
    private String cardExpiredMonth = "01";
    private String clientKey = "1234";
    private String dataSample = "sample";
    private String prefixCardNumber = "XXXX-XXXX-XXXX-";

    @Before
    public void setup() {
        cardTokenRequest = new CardTokenRequest(cardNumber, cardCVV, cardExpiredMonth,
                cardExpiredYear, clientKey);

        PowerMockito.mockStatic(TextUtils.class);
    }


    @Test
    public void cardNumberTest() {
        cardTokenRequest.setCardNumber(dataSample);
        Assert.assertEquals(dataSample, cardTokenRequest.getCardNumber());
    }


    @Test
    public void cardCVV() {
        cardTokenRequest.setCardCVV(dataSample);
        Assert.assertEquals(dataSample, cardTokenRequest.getCardCVV());
    }


    @Test
    public void setCardExpiredMonthTest() {
        cardTokenRequest.setCardExpiryMonth(dataSample);
        Assert.assertEquals(dataSample, cardTokenRequest.getCardExpiryMonth());
    }


    @Test
    public void setCardExpiredYear() {
        cardTokenRequest.setCardExpiryYear(dataSample);
        Assert.assertEquals(dataSample, cardTokenRequest.getCardExpiryYear());
    }


    @Test
    public void clientKeyTest() {
        cardTokenRequest.setClientKey(dataSample);
        Assert.assertEquals(dataSample, cardTokenRequest.getClientKey());
    }


    @Test
    public void isScureTest() {
        cardTokenRequest.setSecure(true);
        Assert.assertEquals(true, cardTokenRequest.isSecure());
    }


    @Test
    public void twoClick() {
        cardTokenRequest.setTwoClick(true);
        Assert.assertEquals(true, cardTokenRequest.isTwoClick());
    }

    @Test
    public void bankTest() {
        cardTokenRequest.setBank(dataSample);
        Assert.assertEquals(dataSample, cardTokenRequest.getBank());
    }

    @Test
    public void grossAmount() {
        cardTokenRequest.setGrossAmount(10L);
        Assert.assertTrue(10 == cardTokenRequest.getGrossAmount());
    }

    @Test
    public void saveTest() {
        cardTokenRequest.setIsSaved(true);
        Assert.assertEquals(true, cardTokenRequest.isSaved());
    }

    @Test
    public void cardType() {
        cardTokenRequest.setCardType(dataSample);
        Assert.assertEquals(dataSample, cardTokenRequest.getCardType());
    }

    @Test
    public void formatedCardNumber() {
        cardTokenRequest.setCardNumber(cardNumber);
        Assert.assertEquals(prefixCardNumber + "1114", cardTokenRequest.getFormatedCardNumber());
    }

    @Test
    public void formatedCardNumber_whenNull() {
        cardTokenRequest.setCardNumber("");
        Assert.assertEquals(prefixCardNumber, cardTokenRequest.getFormatedCardNumber());
    }

    @Test
    public void formatedCardNumber_whenInvalidLength() {
        String invalidCardNumber = "48111111";
        cardTokenRequest.setCardNumber(invalidCardNumber);
        Assert.assertEquals(prefixCardNumber, cardTokenRequest.getFormatedCardNumber());
    }

    @Test
    public void formatExpDate() {
        cardTokenRequest.setCardExpiryYear(cardExpiredYear);
        Assert.assertEquals("XX/" + cardExpiredYear, cardTokenRequest.getFormatedExpiryDate());
    }

    @Test
    public void formatExpDate_whenLessThen1() {
        cardTokenRequest.setCardExpiryYear("0");
        Assert.assertEquals("XX/XX", cardTokenRequest.getFormatedExpiryDate());
    }


    @Test
    public void saveTokenId() {
        cardTokenRequest.setSavedTokenId(dataSample);
        Assert.assertEquals(dataSample, cardTokenRequest.getSavedTokenId());
    }

    @Test
    public void getStringTest() {
        Assert.assertNotNull(cardTokenRequest.getString());
    }
}
