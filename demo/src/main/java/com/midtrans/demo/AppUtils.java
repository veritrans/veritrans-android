package com.midtrans.demo;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.PaymentMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 11/17/17.
 */

public class AppUtils {

    private static final String GROUP_BANK_TRANSFER = "bank_transfer";
    private static final String PAYMENT_NAME_PERMATA_VA = "Permata VA";
    private static final String PAYMENT_NAME_BCA_VA = "BCA VA";
    private static final String PAYMENT_NAME_BNI_VA = "BNI VA";
    private static final String PAYMENT_NAME_OTHER_VA = "Other VA";
    private static final String PAYMENT_NAME_MANDIRI_ECHANNEL = "Mandiri VA";

    public static boolean isActivePaymentChannel(Context context, SelectPaymentMethodViewModel channel) {
        if (channel != null) {
            if (!channel.getMethodType().equals(context.getString(R.string.payment_indosat_dompetku))
                    && !channel.getMethodType().equals(context.getString(R.string.payment_xl_tunai)))

                return true;
        }

        return false;
    }

    public static List<EnabledPayment> getDefaultPaymentList() {
        List<EnabledPayment> paymentNameList = new ArrayList<>();
        paymentNameList.add(new EnabledPayment(PaymentType.CREDIT_CARD, null));
        paymentNameList.add(new EnabledPayment(PaymentType.PERMATA_VA, GROUP_BANK_TRANSFER));
        paymentNameList.add(new EnabledPayment(PaymentType.BCA_VA, GROUP_BANK_TRANSFER));
        paymentNameList.add(new EnabledPayment(PaymentType.BNI_VA, GROUP_BANK_TRANSFER));
        paymentNameList.add(new EnabledPayment(PaymentType.ALL_VA, GROUP_BANK_TRANSFER));
        paymentNameList.add(new EnabledPayment(PaymentType.E_CHANNEL, GROUP_BANK_TRANSFER));
        paymentNameList.add(new EnabledPayment(PaymentType.KLIK_BCA, null));
        paymentNameList.add(new EnabledPayment(PaymentType.BCA_KLIKPAY, null));
        paymentNameList.add(new EnabledPayment(PaymentType.CIMB_CLICKS, null));
        paymentNameList.add(new EnabledPayment(PaymentType.MANDIRI_CLICKPAY, null));
        paymentNameList.add(new EnabledPayment(PaymentType.BRI_EPAY, null));
        paymentNameList.add(new EnabledPayment(PaymentType.TELKOMSEL_CASH, null));
//        paymentNameList.add(new EnabledPayment(PaymentType.XL_TUNAI, null)); being deprecated
        paymentNameList.add(new EnabledPayment(PaymentType.MANDIRI_ECASH, null));
        paymentNameList.add(new EnabledPayment(PaymentType.INDOMARET, null));
//        paymentNameList.add(new EnabledPayment(PaymentType.INDOSAT_DOMPETKU, null)); being deprecated
        paymentNameList.add(new EnabledPayment(PaymentType.KIOSON, null));
        paymentNameList.add(new EnabledPayment(PaymentType.GCI, null));
        paymentNameList.add(new EnabledPayment(PaymentType.GOPAY, null));
        paymentNameList.add(new EnabledPayment(PaymentType.DANAMON_ONLINE, null));

        return paymentNameList;
    }

    public static PaymentMethodsModel getMethods(Context context, String type, String status) {
        PaymentMethodsModel model = PaymentMethods.getMethods(context, type, status);

        if (model == null && !TextUtils.isEmpty(type)) {
            switch (type) {
                case PaymentType.PERMATA_VA:
                    model = new PaymentMethodsModel(PAYMENT_NAME_PERMATA_VA, null, 0, -1, 2, status);
                    break;

                case PaymentType.BCA_VA:
                    model = new PaymentMethodsModel(PAYMENT_NAME_BCA_VA, null, 0, -1, 2, status);
                    break;

                case PaymentType.BNI_VA:
                    model = new PaymentMethodsModel(PAYMENT_NAME_BNI_VA, null, 0, -1, 2, status);
                    break;

                case PaymentType.ALL_VA:
                    model = new PaymentMethodsModel(PAYMENT_NAME_OTHER_VA, null, 0, -1, 2, status);
                    break;

                case PaymentType.E_CHANNEL:
                    model = new PaymentMethodsModel(PAYMENT_NAME_MANDIRI_ECHANNEL, null, 0, -1, 2, status);
                    break;
            }
        }
        return model;
    }


}
