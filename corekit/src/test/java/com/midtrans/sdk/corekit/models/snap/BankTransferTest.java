package com.midtrans.sdk.corekit.models.snap;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author rakawm
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({TextUtils.class})
public class BankTransferTest {

    private BankTransfer bankTransfer;
    private String exampleText;
    private List<String> exampleStringList;

    @Before
    public void setup() {
        this.bankTransfer = new BankTransfer();
        this.exampleText = "example";
        this.exampleStringList = new ArrayList<>();
    }

    @Test
    public void testBanks() throws Exception {
        exampleStringList.add(exampleText);
        bankTransfer.setBanks(exampleStringList);
        assertEquals(bankTransfer.getBanks().size(), 1);
        assertEquals(bankTransfer.getBanks().get(0), exampleText);
    }
}