package com.midtrans.sdk.uikit;

import com.midtrans.raygun.RaygunOnBeforeSend;
import com.midtrans.raygun.messages.RaygunErrorStackTraceLineMessage;
import com.midtrans.raygun.messages.RaygunMessage;

/**
 * Created by Fajar on 22/03/18.
 */

public class BeforeSendImplementation implements RaygunOnBeforeSend {

    /**
     * Iterate all lines of stack trace to check whether the error has something
     * to do with Midtrans SDK
     * @param raygunMessage raygun message that will be modified before being sent
     * @return raygun message that will be sent
     */
    @Override
    public RaygunMessage onBeforeSend(RaygunMessage raygunMessage) {
        RaygunErrorStackTraceLineMessage[] stackTrace = raygunMessage.getDetails().getError().getStackTrace();
        if (stackTrace != null) {
            for (RaygunErrorStackTraceLineMessage message : stackTrace) {
                String className = message.getClassName();
                if (className.contains("com.midtrans")) {
                    return raygunMessage;
                }
            }
        }
        return null;
    }
}
