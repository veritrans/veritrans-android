package com.midtrans.sdk.uikit.models;

import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.models.snap.Installment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by ziahaqi on 1/19/17.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class})
public class CreditCardInstallmentTest {

    private static final String BANK_BNI = "bni";
    private static final String BANK_MANDIRI = "mandiri";
    private CreditCardInstallment creditCardInstallment;

    @Mock
    private Installment installmentMock;
    private Map<String, ArrayList<Integer>> sampleTerms = new HashMap<>();


    private void initSampleTerms() {
        ArrayList<Integer> terms = new ArrayList<>();
        terms.add(6);
        terms.add(12);
        sampleTerms.put(BANK_BNI, terms);
    }

    @Before
    public void setup() {
        mockStatic(TextUtils.class);
        mockStatic(Log.class);
        creditCardInstallment = spy(new CreditCardInstallment());
        creditCardInstallment.setInstallment(installmentMock);
    }

    @Test
    public void setInstallment() {
        assertEquals(creditCardInstallment.getInstallment(), installmentMock);
    }

    @Test
    public void init() {
        assertEquals(creditCardInstallment.getInstallment(), installmentMock);
    }


    @Test
    public void isInstallmentAvailable_whenReturnTrue() {
        initSampleTerms();
        when(installmentMock.getTerms()).thenReturn(sampleTerms);
        creditCardInstallment.setInstallment(installmentMock);
        assertTrue(creditCardInstallment.isInstallmentAvailable());
    }

    @Test
    public void isInstallmentAvailable_whenReturnFalse() {
        assertFalse(creditCardInstallment.isInstallmentAvailable());
    }

    @Test
    public void getTerm_whenInstallmentNotAvailable() {
        creditCardInstallment.setInstallment(null);
        assertEquals(creditCardInstallment.getTerms(BANK_BNI), null);
    }

    @Test
    public void getTerm_whenInstallmentBankNotEqual() {
        initSampleTerms();
        when(installmentMock.getTerms()).thenReturn(sampleTerms);
        creditCardInstallment.setInstallment(installmentMock);
        assertEquals(creditCardInstallment.getTerms(BANK_MANDIRI), null);
    }

    @Test
    public void getTerm_whenInstallmentBankEqual() {
        initSampleTerms();
        when(installmentMock.getTerms()).thenReturn(sampleTerms);
        creditCardInstallment.setInstallment(installmentMock);
        assertNotEquals(creditCardInstallment.getTerms(BANK_BNI).size(), 2);
    }

    @Test
    public void getTermByPosition() {
        initSampleTerms();
        when(installmentMock.getTerms()).thenReturn(sampleTerms);
        creditCardInstallment.setInstallment(installmentMock);
        creditCardInstallment.getTerms(BANK_BNI);
        assertEquals(12, creditCardInstallment.getTermByPosition(2));
    }

    @Test
    public void setTermSelected() {
        initSampleTerms();
        when(installmentMock.getTerms()).thenReturn(sampleTerms);
        creditCardInstallment.setInstallment(installmentMock);
        creditCardInstallment.getTerms(BANK_BNI);
        creditCardInstallment.setTermSelected(1);
        assertEquals(creditCardInstallment.getTermSelected(), 6);
    }


    @Test
    public void isInstallmentValid_whenTrue(){
        initSampleTerms();
        when(installmentMock.getTerms()).thenReturn(null);
        creditCardInstallment.setInstallment(installmentMock);
        assertEquals(creditCardInstallment.isInstallmentValid(), true);
    }

    @Test
    public void isInstallmentValid_whenNotRequired(){
        initSampleTerms();
        when(installmentMock.getTerms()).thenReturn(sampleTerms);
        when(installmentMock.isRequired()).thenReturn(false);
        creditCardInstallment.setInstallment(installmentMock);
        assertEquals(creditCardInstallment.isInstallmentValid(), true);
    }

    @Test
    public void isInstallmentValid_whenRequired(){
        initSampleTerms();
        when(installmentMock.getTerms()).thenReturn(sampleTerms);
        when(installmentMock.isRequired()).thenReturn(true);
        creditCardInstallment.setInstallment(installmentMock);
        assertEquals(creditCardInstallment.isInstallmentValid(), false);
    }


    @Test
    public void isInstallmentValid_whenRequiredValid(){
        initSampleTerms();
        when(installmentMock.getTerms()).thenReturn(sampleTerms);
        when(installmentMock.isRequired()).thenReturn(true);
        creditCardInstallment.setInstallment(installmentMock);
        creditCardInstallment.getTerms(BANK_BNI);
        creditCardInstallment.setTermSelected(1);
        assertEquals(creditCardInstallment.isInstallmentValid(), true);
    }
}
