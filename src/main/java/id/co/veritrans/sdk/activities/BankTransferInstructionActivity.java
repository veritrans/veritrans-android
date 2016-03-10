package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.fragments.InstructionBCAFragment;
import id.co.veritrans.sdk.fragments.InstructionBCAKlikFragment;
import id.co.veritrans.sdk.fragments.InstructionBCAMobileFragment;
import id.co.veritrans.sdk.fragments.InstructionMandiriFragment;
import id.co.veritrans.sdk.fragments.InstructionMandiriInternetFragment;

/**
 * It display information related to mandiri bill pay , bank transfer and BCA/Prima transaction.
 *
 * Created by shivam on 10/28/15.
 */
public class BankTransferInstructionActivity extends AppCompatActivity {

    public static final String BANK = "bank";
    public static final String TYPE_BCA = "bank.bca";
    public static final String TYPE_PERMATA = "bank.permata";
    public static final String TYPE_MANDIRI = "bank.mandiri";
    public static final String TYPE_MANDIRI_BILL = "bank.mandiri.bill";
    private static final int PAGE_MARGIN = 20;
    private Toolbar mToolbar = null;
    private ViewPager mViewPager = null;
    private TabLayout mTabLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bank_transfer_instruction);
        initializeViews();

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
        mToolbar.setTitle(getResources().getString(R.string.payment_instrution));
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.instruction_tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager_bank_instruction);
        mToolbar.setTitle(getResources().getString(R.string.payment_instrution));
        setUpViewPager();
        setUpTabLayout();

    }


    private void setUpTabLayout() {

        // on need to set names to tab just attach viewpager and then using
        // adapters page title method it will take name for tab.

        /* mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_mandiri));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_bca_prima));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_permata_alto));
        */

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    /**
     * set adapter to view pager and also adds page margin between view pages.
     */
    private void setUpViewPager() {

        mViewPager.setPageMargin(PAGE_MARGIN);
        int pageNumber = 0;
        switch (getIntent().getStringExtra(BANK)) {
            case TYPE_BCA:
                pageNumber = 3;
                break;
            case TYPE_PERMATA:
                pageNumber = 2;
                break;
            case TYPE_MANDIRI:
                pageNumber = 2;
                break;
            case TYPE_MANDIRI_BILL:
                pageNumber = 2;
                break;
            default:
                pageNumber = 0;
                break;
        }
        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(
                getSupportFragmentManager(), pageNumber);
        mViewPager.setAdapter(adapter);
    }


    /**
     * adapter for view pager which will provide instruction fragment depending upon tab click
     * and view pager swap position.
     */
    public class InstructionFragmentPagerAdapter extends FragmentStatePagerAdapter {

        /**
         * number of pages / tabs.
         */
        private final int pages;

        public InstructionFragmentPagerAdapter(FragmentManager fragmentManager, int number) {
            super(fragmentManager);
            this.pages = number;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;

            if(getIntent().getStringExtra(BANK).equals(TYPE_BCA)) {
                if(position == 0) {
                    fragment = new InstructionBCAFragment();
                } else if(position == 1) {
                    fragment = new InstructionBCAKlikFragment();
                } else fragment = new InstructionBCAMobileFragment();
            } else if(getIntent().getStringExtra(BANK).equals(TYPE_PERMATA)){
                fragment = new InstructionMandiriFragment();
            } else {
                if(position == 0) {
                    fragment = new InstructionMandiriFragment();
                } else {
                    fragment = new InstructionMandiriInternetFragment();
                }
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return pages;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public CharSequence getPageTitle(int position) {

            if(getIntent().getStringExtra(BANK).equals(TYPE_BCA)) {
                if(position == 0) {
                    return getString(R.string.tab_bca_atm);
                } else if(position == 1) {
                    return getString(R.string.tab_bca_klik);
                } else return getString(R.string.tab_bca_mobile);
            } else if(getIntent().getStringExtra(BANK).equals(TYPE_PERMATA)){
                if(position==0) return getString(R.string.tab_permata_atm);
                else return getString(R.string.tab_alto);
            } else {
                if(position == 0) {
                    return getString(R.string.tab_mandiri_atm);
                } else {
                    return getString(R.string.tab_mandiri_internet);
                }
            }
        }
    }
}