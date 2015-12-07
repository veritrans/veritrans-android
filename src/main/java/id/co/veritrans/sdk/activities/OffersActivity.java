package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.fragments.OffersListFragment;

/**
 * Created by Ankit on 12/7/15.
 */
public class OffersActivity extends AppCompatActivity {

    public static final String HOME_FRAGMENT = "home";
    private Toolbar toolbar = null;

    private OffersListFragment offersListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        initializeView();
        setUpHomeFragment();
    }

    private void setUpHomeFragment() {
        // setup home fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        offersListFragment = new OffersListFragment();

        fragmentTransaction.add(R.id.offers_container,
                offersListFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return false;
    }

    private void initializeView() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        //setup tool bar
        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
