package com.midtrans.sdk.ui.abtracts;

import android.support.annotation.NonNull;

/**
 * Created by ziahaqi on 2/22/17.
 */

public interface IBaseView<T> {

    void setPresenter(@NonNull T presenter);

}
