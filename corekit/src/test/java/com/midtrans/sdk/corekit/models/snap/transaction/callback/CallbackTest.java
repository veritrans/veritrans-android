package com.midtrans.sdk.corekit.models.snap.transaction.callback;

import com.midtrans.sdk.corekit.core.snap.model.transaction.response.callback.Callbacks;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CallbackTest {
    private Callbacks callbacks;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() throws Exception {
        this.callbacks = new Callbacks();
        this.exampleTextPositive = "examplePositive";
        this.exampleTextNegative = "exampleNegative";
    }

    @Test
    public void test_setError_positive() throws Exception {
        callbacks.setError(exampleTextPositive);
        assertEquals(callbacks.getError(), exampleTextPositive);
    }

    @Test
    public void test_setError_negative() throws Exception {
        callbacks.setError(exampleTextPositive);
        assertNotEquals(callbacks.getError(), exampleTextNegative);
    }

    @Test
    public void test_setFinish_positive() throws Exception {
        callbacks.setFinish(exampleTextPositive);
        assertEquals(callbacks.getFinish(), exampleTextPositive);
    }

    @Test
    public void test_setFinish_negative() throws Exception {
        callbacks.setFinish(exampleTextPositive);
        assertNotEquals(callbacks.getFinish(), exampleTextNegative);
    }
}