package id.co.veritrans.sdk.coreflow.veritransandroid;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;

/**
 * Created by ziahaqi on 24/06/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class VeritransAndroidSDKTest {

    VeritransSDK veritransSDK;
    private String clientKey = "CLIENT_KEY";
    private String serverKey = "SERVER_KEY";

    @Mock
    Context context;

    @Before
    public void setupVeritransSDK(){

        veritransSDK = new SdkCoreFlowBuilder(context, clientKey, serverKey)
                .enableLog(true)
                .setDefaultText("open_sans_regular.ttf")
                .setSemiBoldText("open_sans_semibold.ttf")
                .setBoldText("open_sans_bold.ttf")
        .buildSDK();
    }

    @Test
    public void getTokenTest(){

    }
}
