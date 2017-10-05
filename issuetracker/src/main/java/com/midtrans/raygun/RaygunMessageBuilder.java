package com.midtrans.raygun;

import android.content.Context;

import com.midtrans.raygun.messages.RaygunClientMessage;
import com.midtrans.raygun.messages.RaygunEnvironmentMessage;
import com.midtrans.raygun.messages.RaygunErrorMessage;
import com.midtrans.raygun.messages.RaygunMessage;

import java.util.List;
import java.util.Map;

public class RaygunMessageBuilder implements IRaygunMessageBuilder {
    private RaygunMessage raygunMessage;

    public RaygunMessageBuilder() {
        raygunMessage = new RaygunMessage();
    }

    @Override
    public RaygunMessage build() {
        return raygunMessage;
    }

    @Override
    public IRaygunMessageBuilder setMachineName(String machineName) {
        raygunMessage.getDetails().setMachineName(machineName);
        return this;
    }

    @Override
    public IRaygunMessageBuilder setExceptionDetails(Throwable throwable) {
        raygunMessage.getDetails().setError(new RaygunErrorMessage(throwable));
        return this;
    }

    @Override
    public IRaygunMessageBuilder setClientDetails() {
        raygunMessage.getDetails().setClient(new RaygunClientMessage());
        return this;
    }

    @Override
    public IRaygunMessageBuilder setEnvironmentDetails(Context context) {
        raygunMessage.getDetails().setEnvironment(new RaygunEnvironmentMessage(context));
        return this;
    }

    @Override
    public IRaygunMessageBuilder setVersion(String version) {
        raygunMessage.getDetails().setVersion(version);
        return this;
    }

    @Override
    public IRaygunMessageBuilder setTags(List tags) {
        raygunMessage.getDetails().setTags(tags);
        return this;
    }

    @Override
    public IRaygunMessageBuilder setUserCustomData(Map userCustomData) {
        raygunMessage.getDetails().setUserCustomData(userCustomData);
        return this;
    }

    @Override
    public IRaygunMessageBuilder setAppContext(String identifier) {
        raygunMessage.getDetails().setAppContext(identifier);
        return this;
    }

    @Override
    public IRaygunMessageBuilder setUserContext(Context context) {
        raygunMessage.getDetails().setUserContext(context);
        return this;
    }

    @Override
    public IRaygunMessageBuilder setNetworkInfo(Context context) {
        raygunMessage.getDetails().setNetworkInfo(context);
        return this;
    }

    @Override
    public IRaygunMessageBuilder setGroupingKey(String groupingKey) {
        raygunMessage.getDetails().setGroupingKey(groupingKey);
        return this;
    }

    public static RaygunMessageBuilder instance() {
        return new RaygunMessageBuilder();
    }
}
