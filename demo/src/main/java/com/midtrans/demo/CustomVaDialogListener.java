package com.midtrans.demo;

import java.io.Serializable;

/**
 * Created by rakawm on 4/27/17.
 */

public interface CustomVaDialogListener extends Serializable {
    void onOkClicked(String input);

    void onCancelClicked();
}
