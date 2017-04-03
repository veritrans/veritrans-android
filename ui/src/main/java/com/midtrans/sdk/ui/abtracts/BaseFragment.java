package com.midtrans.sdk.ui.abtracts;

import android.content.res.ColorStateList;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.EditText;

import com.midtrans.sdk.ui.widgets.FancyButton;

import java.lang.reflect.Field;

/**
 * Created by ziahaqi on 2/24/17.
 */

public class BaseFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();

    protected void filterBackgroundColor(View view) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).filterBackgroundColor(view);
        }
    }

    protected void setBackgroundColor(View view, String colorType) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setBackgroundColor(view, colorType);
        }
    }

    protected void filterColor(View view) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).filterColor(view);
        }
    }

    protected void setHintColor(View view) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setHintColor(view);
        }
    }

    protected void setTextColor(View view) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setTextColor(view);
        }
    }

    protected void setBorderColor(FancyButton fancyButton) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setBorderColor(fancyButton);
        }
    }

    protected void setCheckoxStateColor(AppCompatCheckBox checkBox) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setCheckoxStateColor(checkBox);
        }
    }

    public int getPrimaryDarkColor() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getPrimaryDarkColor();
        }
        return 0;
    }

    public int getSecondaryColor() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getPrimaryDarkColor();
        }
        return 0;
    }

    public int getPrimaryColor() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getPrimaryDarkColor();
        }
        return 0;
    }

    public void setEditTextColorFilter(AppCompatEditText editText){
        if(getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).setEditTextColorFilter(editText);
        }
    }
}
