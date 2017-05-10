package com.midtrans.demo;

import java.io.Serializable;

/**
 * Created by ziahaqi on 5/8/17.
 */

public interface CustomInstallmentDialogListener extends Serializable {
    void onOkClicked(boolean reqired);

    void onCancelClicked();
}
