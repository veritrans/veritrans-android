package com.midtrans.raygun;

import com.midtrans.raygun.messages.RaygunMessage;

public interface RaygunOnBeforeSend {
    RaygunMessage onBeforeSend(RaygunMessage message);
}