package com.midtrans.sdk.corekit.models.snap;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.assertEquals;

/**
 * @author rakawm
 */

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({TextUtils.class})
public class CustomerDetailsTest {
    private CustomerDetails customerDetails;
    private String exampleText;

    @Before
    public void setUp() throws Exception {
        this.customerDetails = new CustomerDetails();
        this.exampleText = "example";
    }

    @Test
    public void testSetAddress() throws Exception {
        customerDetails.setAddress(exampleText);
        assertEquals(customerDetails.getAddress(), exampleText);
    }

    @Test
    public void testSetPhone() throws Exception {
        customerDetails.setPhone(exampleText);
        assertEquals(customerDetails.getPhone(), exampleText);
    }

    @Test
    public void testSetEmail() throws Exception {
        customerDetails.setEmail(exampleText);
        assertEquals(customerDetails.getEmail(), exampleText);
    }

    @Test
    public void testSetName() throws Exception {
        customerDetails.setName(exampleText);
        assertEquals(customerDetails.getName(), exampleText);
    }
}