package com.midtrans.sdk.corekit.models;

import android.text.TextUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by ziahaqi on 7/14/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TextUtils.class})
public class BankDetailTest {

    private BankDetail bankDetail;
    private String sampelDataStr = "data";

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        Mockito.when(TextUtils.equals(Mockito.any(CharSequence.class), Mockito.any(CharSequence.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CharSequence a = (CharSequence) invocation.getArguments()[0];
                CharSequence b = (CharSequence) invocation.getArguments()[1];
                if (a == b) return true;
                int length;
                if (a != null && b != null && (length = a.length()) == b.length()) {
                    if (a instanceof String && b instanceof String) {
                        return a.equals(b);
                    } else {
                        for (int i = 0; i < length; i++) {
                            if (a.charAt(i) != b.charAt(i)) return false;
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        this.bankDetail = new BankDetail();
    }

    @Test
    public void binTest() {
        bankDetail.setBin(sampelDataStr);
        Assert.assertEquals(sampelDataStr, bankDetail.getBin());
    }

    @Test
    public void issuingBankTest() {
        bankDetail.setIssuing_bank(sampelDataStr);
        Assert.assertEquals(sampelDataStr, bankDetail.getIssuing_bank());
    }

    @Test
    public void countryTest() {
        bankDetail.setCountry(sampelDataStr);
        Assert.assertEquals(sampelDataStr, bankDetail.getCountry());
    }

    @Test
    public void binTypeTest() {
        bankDetail.setBin_type(sampelDataStr);
        Assert.assertEquals(sampelDataStr, bankDetail.getBin_type());
    }

    @Test
    public void binClassTest() {
        bankDetail.setBin_class(sampelDataStr);
        Assert.assertEquals(sampelDataStr, bankDetail.getBin_class());
    }

    @Test
    public void cardAssouciation() {
        bankDetail.setCard_association(sampelDataStr);
        Assert.assertEquals(sampelDataStr, bankDetail.getCard_association());
    }

    @Test
    public void createAtTest() {
        bankDetail.setCreated_at(sampelDataStr);
        Assert.assertEquals(sampelDataStr, bankDetail.getCreated_at());
    }

    @Test
    public void updateAtTest() {
        bankDetail.setUpdated_at(sampelDataStr);
        Assert.assertEquals(sampelDataStr, bankDetail.getUpdated_at());
    }


    @Test
    public void bankCodeTest() {
        bankDetail.setBank_code(sampelDataStr);
        Assert.assertEquals(sampelDataStr, bankDetail.getBank_code());
    }


}
