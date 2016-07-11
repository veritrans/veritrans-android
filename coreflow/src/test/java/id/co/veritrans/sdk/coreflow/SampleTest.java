package id.co.veritrans.sdk.coreflow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by ziahaqi on 10/07/2016.
 */
@RunWith(PowerMockRunner.class)

public class SampleTest {

    Sample sampleSpy;

    @Before
    public void setup(){
       Sample sample = new Sample();
        sampleSpy = PowerMockito.spy(sample);
    }

    @Test
    public void testsquare(){
        Assert.assertEquals(1, sampleSpy.square(1));
        Assert.assertEquals(4, sampleSpy.square(2));
    }
}
