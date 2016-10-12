package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public interface ISdkFlow {

    public void runUIFlow(Context context);

    public void runCreditCardPayment(Context context);

    public void runRegisterCard(Context context);
}
