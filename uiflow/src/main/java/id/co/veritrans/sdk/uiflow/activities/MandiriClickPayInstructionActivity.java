package id.co.veritrans.sdk.uiflow.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import id.co.veritrans.sdk.uiflow.R;

/**
 * It display information related to mandiri click pay transaction.
 *
 * Created by shivam on 12/03/15.
 */
public class MandiriClickPayInstructionActivity extends BaseActivity {

    private Toolbar mToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_instruction_mandiri_click_pay);
        initializeViews();
        initializeTheme();
    }


    /**
     * handles click of back arrow given on action bar.
     *
     * @param item  selected menu
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
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}