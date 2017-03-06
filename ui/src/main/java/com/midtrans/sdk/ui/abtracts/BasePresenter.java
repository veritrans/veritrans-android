package com.midtrans.sdk.ui.abtracts;

import com.midtrans.sdk.ui.MidtransUi;

/**
 * Created by ziahaqi on 2/22/17.
 */

public abstract class BasePresenter{
    protected MidtransUi midtransUiSdk;


    protected  boolean hasPrimaryColor(){
         if(midtransUiSdk.getColorTheme() != null && midtransUiSdk.getColorTheme().getPrimaryColor() != 0){
             return true;
         }

        return false;
    }

}
