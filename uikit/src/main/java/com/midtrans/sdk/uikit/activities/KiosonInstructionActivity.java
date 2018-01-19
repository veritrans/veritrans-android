package com.midtrans.sdk.uikit.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by ziahaqi on 8/26/16.
 * @Deprecated please refer to included instruction in {@link com.midtrans.sdk.uikit.views.kioson.status.KiosonStatusActivity}
 */
public class KiosonInstructionActivity extends BaseActivity {

    private Toolbar mToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_kioson);
        initializeViews();
    }

    /**
     * handles click of back arrow given on action bar.
     *
     * @param item selected menu
     * @return is handled or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //close activity on click of cross button.
            finish();
            if (MidtransSDK.getInstance().getUIKitCustomSetting()!=null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * set up action bar, view pager and tabs.
     */
    private void initializeViews() {

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        initializeTheme();
        Drawable closeIcon = ContextCompat.getDrawable(this, R.drawable.ic_close);
        closeIcon.setColorFilter(ContextCompat.getColor(this, R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        SemiBoldTextView pageTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
        pageTitle.setText(getString(R.string.kioson_payment_instruction));
        mToolbar.setNavigationIcon(closeIcon);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adjustToolbarSize();
    }

}
