package com.midtrans.sdk.uikit.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.midtrans.sdk.uikit.R;

/**
 * Created by rakawm on 1/19/17.
 */

public class KlikBCAInstructionActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private ImageView logo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_klikbca);
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
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * set up action bar, view pager and tabs.
     */
    private void initializeViews() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        initializeTheme();
        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        mToolbar.setNavigationIcon(closeIcon);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
