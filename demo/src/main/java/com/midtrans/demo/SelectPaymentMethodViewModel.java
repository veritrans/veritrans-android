package com.midtrans.demo;

import java.io.Serializable;

/**
 * Created by rakawm on 5/2/17.
 */

public class SelectPaymentMethodViewModel implements Serializable {
    public static final String CATEGORY_HEADER = "header";
    public static final String CATEGORY_ITEM = "item";

    private String methodName;
    private String methodType;
    private boolean selected;

    private String category;
    private Integer priority = 100;

    public SelectPaymentMethodViewModel(String methodName, String methodType, boolean selected, String category) {
        setMethodName(methodName);
        setMethodType(methodType);
        setSelected(selected);
        setCategory(category);
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
