package com.midtrans.demo;

import android.content.Context;

/**
 * Created by ziahaqi on 11/17/17.
 */

public class AppUtils {

    public static boolean isActivePaymentChannel(Context context, SelectPaymentMethodViewModel channel) {
        if (channel != null) {
            if (!channel.getMethodType().equals(context.getString(R.string.payment_indosat_dompetku))
                    && !channel.getMethodType().equals(context.getString(R.string.payment_xl_tunai)))

                return true;
        }

        return false;
    }
}
