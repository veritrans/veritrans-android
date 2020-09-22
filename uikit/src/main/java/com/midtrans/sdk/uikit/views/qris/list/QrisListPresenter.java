package com.midtrans.sdk.uikit.views.qris.list;

import android.content.Context;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import java.util.ArrayList;
import java.util.List;

public class QrisListPresenter {
    private final Context context;
    private final List<EnabledPayment> qrisList;

    public QrisListPresenter(Context context, EnabledPayments enabledPayments) {
        this.context = context;
        this.qrisList = new ArrayList<>();

        if (enabledPayments != null) {
            this.qrisList.addAll(enabledPayments.getEnabledPayments());
        }
    }
}
