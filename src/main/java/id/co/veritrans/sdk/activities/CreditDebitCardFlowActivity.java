package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.fragments.AddCardDetailsFragment;

public class CreditDebitCardFlowActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_card_flow);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
        replaceFragment(addCardDetailsFragment);
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
