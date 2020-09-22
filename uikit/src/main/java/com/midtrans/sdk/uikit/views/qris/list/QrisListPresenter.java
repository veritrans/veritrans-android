package com.midtrans.sdk.uikit.views.qris.list;

import android.content.Context;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.models.Qris;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
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

    public List<Qris> getQrisList() {
        List<Qris> qrisList = new ArrayList<>();
        if (qrisList != null && !qrisList.isEmpty()) {
            for (EnabledPayment qris : this.qrisList) {
                Qris model = PaymentMethods.createQrisModel(context, qris.getType(), qris.getStatus());
                if (model != null) {
                    qrisList.add(model);
                }
            }
        }
        //SdkUIFlowUtil.sortBankTransferMethodsByPriority(banks);
        return qrisList;
    }

}
