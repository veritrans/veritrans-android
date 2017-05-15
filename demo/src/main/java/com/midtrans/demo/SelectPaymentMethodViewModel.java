package com.midtrans.demo;

import java.io.Serializable;

/**
 * Created by rakawm on 5/2/17.
 */

public class SelectPaymentMethodViewModel implements Serializable {
    private String methodName;
    private String methodType;
    private boolean selected;

    public SelectPaymentMethodViewModel(String methodName, String methodType, boolean selected) {
        setMethodName(methodName);
        setMethodType(methodType);
        setSelected(selected);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
