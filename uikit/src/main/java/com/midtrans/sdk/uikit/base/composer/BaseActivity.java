package com.midtrans.sdk.uikit.base.composer;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.MidtransKitConfig;
import com.midtrans.sdk.uikit.R;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected void initializeTheme() {
        initBadgeTestView();

        MidtransKit midtransKit = MidtransKit.getInstance();
        if (midtransKit != null) {
            updateColorTheme(midtransKit);
        }
    }

    private void updateColorTheme(MidtransKit midtransKit) {
        try {
            MidtransKitConfig midtransKitConfig = midtransKit.getInstance().getMidtransKitConfig();
            if (midtransKitConfig.getColorTheme() != null) {
                int primaryColor = midtransKitConfig.getColorTheme().getPrimaryColor();
                int primaryDarkColor = midtransKitConfig.getColorTheme().getPrimaryDarkColor();
                if (primaryColor != 0) {
                    // Set primary button color
                }
                if (primaryDarkColor != 0) {
                    // Set amount text color
                }
            }
        } catch (Exception e) {
            Logger.error("themes", "init:" + e.getMessage());
        }
    }

    private void initBadgeTestView() {
        if (MidtransKit.getInstance().getEnvironment() == Environment.SANDBOX) {
            ImageView badgeView = findViewById(R.id.image_sandbox_badge);
            if (badgeView != null) {
                badgeView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void pendingSlideIn() {
        MidtransKitConfig midtransKitConfig = MidtransKit.getInstance().getMidtransKitConfig();
        if (midtransKitConfig != null && midtransKitConfig.isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        pendingSlideIn();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        pendingSlideIn();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MidtransKitConfig midtransKitConfig = MidtransKit.getInstance().getMidtransKitConfig();
        if (midtransKitConfig != null && midtransKitConfig.isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
        }
    }
}