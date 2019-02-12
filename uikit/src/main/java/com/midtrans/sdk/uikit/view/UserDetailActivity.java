package com.midtrans.sdk.uikit.view;

import android.os.Bundle;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.BaseActivity;

import androidx.annotation.Nullable;

public class UserDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbarAndView();
    }

    private void initToolbarAndView() {
        setContentView(R.layout.activity_user_details);
    }
}