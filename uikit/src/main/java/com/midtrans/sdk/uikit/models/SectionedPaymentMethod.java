package com.midtrans.sdk.uikit.models;

import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.uikit.PaymentMethods;

/**
 * Created by ziahaqi on 11/9/16.
 */

public class SectionedPaymentMethod {

    private PaymentMethodsModel model;
    private int sectionType;
    private int type;
    private int sectionParent;

    private SectionedPaymentMethod(PaymentMethodsModel model, int type, int sectionType, int sectionParent) {
        this.model = model;
        this.sectionType = sectionType;
        this.type = type;
        this.sectionParent = sectionParent;
    }

    public PaymentMethodsModel getModel() {
        return model;
    }

    public int getSectionType() {
        return sectionType;
    }

    public int getType() {
        return type;
    }

    public int getSectionParent() {
        return sectionParent;
    }

    public static SectionedPaymentMethod createSection(PaymentMethodsModel model, int sectionType){
        return new SectionedPaymentMethod(model,  PaymentMethods.SECTION, sectionType, -1);
    }

    public static SectionedPaymentMethod createItem(PaymentMethodsModel model, int parentType){
        return new SectionedPaymentMethod(model, PaymentMethods.ITEM, -1, parentType);
    }
}
