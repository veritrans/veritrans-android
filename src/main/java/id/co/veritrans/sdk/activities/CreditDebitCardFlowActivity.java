package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.fragments.SavedCardFragment;

public class CreditDebitCardFlowActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private float cardWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_card_flow);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
        //replaceFragment(addCardDetailsFragment);
        calculateScreenWidth();
        SavedCardFragment savedCardFragment = SavedCardFragment.newInstance();
        replaceFragment(savedCardFragment);
    }

    private void calculateScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        cardWidth = outMetrics.widthPixels ;
        cardWidth = cardWidth-((2*getResources().getDimension(R.dimen.sixteen_dp))/density);
    }

    public float getScreenWidth() {
        return cardWidth;
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.card_container, fragment);
            ft.commit();
        }
    }
    public Toolbar getToolbar() {
        return toolbar;
    }
}
