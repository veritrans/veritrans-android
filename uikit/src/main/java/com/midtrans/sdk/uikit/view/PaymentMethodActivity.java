package com.midtrans.sdk.uikit.view;

import android.os.Bundle;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.BaseActivity;
import com.midtrans.sdk.uikit.utilities.ActivityUtilities;

import androidx.annotation.Nullable;

public class UserDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbarAndView();
    }

    private void initToolbarAndView() {
        setContentView(R.layout.activity_user_details);

        ImageView progressImage = findViewById(R.id.progress_bar_image);
        Ion.with(progressImage)
                .load(ActivityUtilities.getImagePath(this) + R.drawable.midtrans_loader);
    }
}