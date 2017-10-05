package com.midtrans.sdk.uikit.utilities;

import com.midtrans.raygun.RaygunOnBeforeSend;
import com.midtrans.raygun.messages.RaygunMessage;
import com.midtrans.raygun.messages.RaygunMessageDetails;

/**
 * Created by ziahaqi on 10/5/17.
 */

public class RaygunBeforeSend implements RaygunOnBeforeSend {
    @Override
    public RaygunMessage onBeforeSend(RaygunMessage message) {
        RaygunMessageDetails details = message.getDetails();
        details.setGroupingKey(UiKitConstants.KEY_TRACKING_GROUP);
        return message;
    }

}
