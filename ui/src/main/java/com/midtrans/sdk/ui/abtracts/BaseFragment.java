package com.midtrans.sdk.ui.abtracts;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.ui.CustomSetting;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.themes.BaseColorTheme;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by ziahaqi on 2/24/17.
 */

public class BaseFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();

    protected void filterBackgroundColor(View view) {
        if(getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).filterBackgroundColor(view);
        }
    }

    protected void setBackgroundColor(View view, String colorType) {
        if(getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).setBackgroundColor(view, colorType);
        }
    }

    protected void filterColor(View view) {
        if(getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).filterColor(view);
        }
    }

    protected void setHintColor(View view) {
        if(getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).setHintColor(view);
        }
    }

    protected void setTextColor(View view) {
        if(getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).setTextColor(view);
        }
    }

    protected void setBorderColor(FancyButton fancyButton) {
        if(getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).setBorderColor(fancyButton);
        }
    }

    protected void setCheckoxStateColor(AppCompatCheckBox checkBox) {
        if(getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).setCheckoxStateColor(checkBox);
        }
    }
}
