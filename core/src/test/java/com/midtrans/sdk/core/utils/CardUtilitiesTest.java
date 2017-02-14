package com.midtrans.sdk.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rakawm on 2/14/17.
 */
public class CardUtilitiesTest {
    @Test
    public void isValidCardNumber() throws Exception {
        String validCard = "4811111111111114";
        Assert.assertTrue(CardUtilities.isValidCardNumber(validCard));
        String invalidCard = "4811111111111111";
        Assert.assertFalse(CardUtilities.isValidCardNumber(invalidCard));
    }

    @Test
    public void getCardType() throws Exception {
        String visaCard = "4811111111111114";
        String masterCard = "5211111111111117";
        String jcbCard = "3530111333300000";
        String amexCard = "378282246310005";
        String invalidCard = "678282246310005";
        Assert.assertEquals(CardUtilities.CARD_TYPE_VISA, CardUtilities.getCardType(visaCard));
        Assert.assertEquals(CardUtilities.CARD_TYPE_MASTERCARD, CardUtilities.getCardType(masterCard));
        Assert.assertEquals(CardUtilities.CARD_TYPE_JCB, CardUtilities.getCardType(jcbCard));
        Assert.assertEquals(CardUtilities.CARD_TYPE_AMEX, CardUtilities.getCardType(amexCard));
        Assert.assertEquals("", CardUtilities.getCardType(invalidCard));
    }

    @Test
    public void getFormattedCreditCardNumber() throws Exception {
        String card = "4811111111111114";
        String formattedCard = "4811 1111 1111 1114";
        Assert.assertEquals(formattedCard, CardUtilities.getFormattedCreditCardNumber(card));
    }

}