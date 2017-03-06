package com.midtrans.sdk.ui.abtracts;

import java.io.Serializable;

/**
 * Created by ziahaqi on 2/27/17.
 */

public interface IPresenter extends Serializable{

    boolean isPrimaryDarkColorAvailable();

    int getPrimaryDarkColor();

    boolean isPrimaryColorAvailable();

    int getPrimaryColor();
}
