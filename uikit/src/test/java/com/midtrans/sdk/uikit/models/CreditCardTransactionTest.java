package com.midtrans.sdk.uikit.models;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.Installment;
import com.midtrans.sdk.uikit.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by ziahaqi on 1/19/17.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class})
public class CreditCardTransactionTest {

    private static final String BIN_VALID = "481111";
    private static final String BIN_INVALID = "123456";
    private static final String BANK_MANDIRI = "mandiri";
    private static final String MANDIRI_BIN = "400376";
    private static final String BANK_BNI = "bni";
    private static final String BANK_OFFLINE = "offline";
    private CreditCardTransaction cardTransaction;
    private ArrayList<String> bankBins = new ArrayList<>();
    private CreditCard card;
    private CreditCardInstallment cardInstallment;
    private Installment installment;
    private Map<String, ArrayList<Integer>> sampleTerms = new HashMap<>();


    private ArrayList<String> initDefaultWhiteListBin() {
        bankBins.add(BIN_VALID);
        return bankBins;
    }

    private void initSampleTerms() {
        ArrayList<Integer> terms = new ArrayList<>();
        terms.add(6);
        terms.add(12);
        sampleTerms.put(BANK_BNI, terms);
    }


    private Type createBankBinsType() {
        Type type = new TypeToken<ArrayList<BankBinsResponse>>() {
        }.getType();
        return type;
    }

    private ArrayList<BankBinsResponse> readDefaultBankBins() throws Exception {
        return TestUtils.getJsonDataFromMapFile(getClass().getClassLoader(), createBankBinsType(), "bank_bins_sample.json");
    }

    @Before
    public void setup() throws Exception {
        mockStatic(TextUtils.class);
        mockStatic(Log.class);

        cardTransaction = spy(new CreditCardTransaction());
        installment = spy(new Installment());
        cardInstallment = spy(new CreditCardInstallment());
        card = spy(new CreditCard());
        when(card.getWhitelistBins()).thenReturn(initDefaultWhiteListBin());
        when(card.getInstallment()).thenReturn(installment);


        cardInstallment.setInstallment(installment);
        cardTransaction.setProperties(card, readDefaultBankBins());
    }

    @Test
    public void isInWhitlistbins_whenValid() {
        assertTrue(cardTransaction.isInWhiteList(BIN_VALID));
    }

    @Test
    public void isInWhitlistbins_whenInvalid() {
        assertFalse(cardTransaction.isInWhiteList(BIN_INVALID));
    }


    @Test
    public void getInstallmentTerm() throws Exception {
        initSampleTerms();
        when(installment.getTerms()).thenReturn(sampleTerms);
        when(installment.isRequired()).thenReturn(true);
        when(card.getInstallment()).thenReturn(installment);

        cardInstallment.setInstallment(installment);
        cardTransaction.setProperties(card, readDefaultBankBins());
        cardTransaction.getInstallmentTerms(BIN_VALID);
        int term = cardTransaction.getInstallmentTerm(1);
        assertEquals(6, term);
    }

    @Test
    public void getInstallmentTerms_whenBankOffline() throws Exception {
        initSampleTerms();
        ArrayList<Integer> newTerm = new ArrayList<>();
        newTerm.add(12);
        sampleTerms.put(BANK_OFFLINE, newTerm);

        when(installment.getTerms()).thenReturn(sampleTerms);
        when(card.getInstallment()).thenReturn(installment);
        when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(true);
        cardInstallment.setInstallment(installment);
        cardTransaction.setProperties(card, readDefaultBankBins());
        cardTransaction.getInstallmentTerms(BIN_INVALID);
        assertEquals(BANK_OFFLINE, cardTransaction.getInstallmentBankSelected());
    }

    @Test
    public void getInstallmentTerms_whenBankOfflineWithValidTerm() throws Exception {
        initSampleTerms();
        ArrayList<Integer> newTerm = new ArrayList<>();
        newTerm.add(12);
        sampleTerms.put(BANK_OFFLINE, newTerm);

        when(installment.getTerms()).thenReturn(sampleTerms);
        when(card.getInstallment()).thenReturn(installment);
        when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(true);
        cardInstallment.setInstallment(installment);
        cardTransaction.setProperties(card, readDefaultBankBins());
        int term = cardTransaction.getInstallmentTerms(BIN_INVALID).get(1);
        assertEquals(12, term);
    }

    @Test
    public void getInstallmentTerms_whenBankOnlineWithValidTerm() throws Exception {
        initSampleTerms();

        when(installment.getTerms()).thenReturn(sampleTerms);
        when(card.getInstallment()).thenReturn(installment);
        cardInstallment.setInstallment(installment);
        cardTransaction.setProperties(card, readDefaultBankBins());
        cardTransaction.getInstallmentTerms(BIN_VALID);
        int term = cardTransaction.getInstallmentTerms(BIN_VALID).get(1);
        assertEquals(6, term);

    }

    @Test
    public void getInstallmentTerms_whenBankOnline() throws Exception {
        initSampleTerms();

        when(installment.getTerms()).thenReturn(sampleTerms);
        when(card.getInstallment()).thenReturn(installment);
        cardInstallment.setInstallment(installment);
        cardTransaction.setProperties(card, readDefaultBankBins());
        cardTransaction.getInstallmentTerms(BIN_VALID);
        assertEquals(BANK_BNI, cardTransaction.getInstallmentBankSelected());
    }
}
