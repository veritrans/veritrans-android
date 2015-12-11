package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.OffersAddCardDetailsFragment;
import id.co.veritrans.sdk.fragments.OffersListFragment;
import id.co.veritrans.sdk.models.OffersListModel;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by Ankit on 12/7/15.
 */
public class OffersActivity extends AppCompatActivity {

    public static final String OFFERS_FRAGMENT = "offersList";
    public static final String ADD_CARD_FRAGMENT = "addCard";
    public String currentFragment = "offersList";
    private Toolbar toolbar = null;
    private TextViewFont textViewTitleOffers = null;
    private VeritransSDK veritransSDK = null;
    private TextViewFont textViewTitleCardDetails = null;
    private TextViewFont textViewOfferName = null;

    private OffersListFragment offersListFragment = null;
    private int position = Constants.PAYMENT_METHOD_OFFERS;
    public ArrayList<OffersListModel> offersListModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        veritransSDK = VeritransSDK.getVeritransSDK();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(Constants.POSITION, Constants
                    .PAYMENT_METHOD_OFFERS);
        } else {
            SdkUtil.showSnackbar(OffersActivity.this, Constants.ERROR_SOMETHING_WENT_WRONG);
            finish();
        }

        initializeView();
        setUpHomeFragment();
    }

    private void setUpHomeFragment() {
        // setup home fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        offersListFragment = new OffersListFragment();

        fragmentTransaction.add(R.id.offers_container,
                offersListFragment, OFFERS_FRAGMENT);
        fragmentTransaction.commit();
        currentFragment = OFFERS_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        textViewTitleOffers = (TextViewFont) findViewById(R.id.text_title);
        textViewTitleCardDetails = (TextViewFont) findViewById(R.id.text_title_card_details);
        textViewOfferName = (TextViewFont) findViewById(R.id.text_title_offer_name);
        //setup tool bar
        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

