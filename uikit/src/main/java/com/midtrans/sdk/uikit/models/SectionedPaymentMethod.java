package com.midtrans.sdk.uikit.models;

import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.uikit.PaymentMethods;

/**
 * Created by ziahaqi on 11/9/16.
 */

public class SectionedPaymentMethod {

    private PaymentMethodsModel model;
    private int sectionPriority;
    private int type;
    private int parentType;

    private SectionedPaymentMethod(PaymentMethodsModel model, int sectionPriority, int type, int parentType) {
        this.model = model;
        this.sectionPriority = sectionPriority;
        this.type = type;
        this.parentType = parentType;
    }

    public PaymentMethodsModel getModel() {
        return model;
    }

    public int getSectionPriority() {
        return sectionPriority;
    }

    public int getType() {
        return type;
    }

    public static SectionedPaymentMethod createSection(PaymentMethodsModel model, int sectionPriority){
        return new SectionedPaymentMethod(model, sectionPriority, PaymentMethods.TYPE_SECTION, -1);
    }

    public static SectionedPaymentMethod createItem(PaymentMethodsModel model, int parentType){
        return new SectionedPaymentMethod(model, -1, PaymentMethods.TYPE_ITEM, parentType);
    }
}
