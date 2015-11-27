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
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.fragments.InstructionBCAFragment;
import id.co.veritrans.sdk.fragments.InstructionMandiriFragment;
import id.co.veritrans.sdk.fragments.InstructionPermataFragment;

/**
 * It display information related to mandiri bill pay , bank transfer and BCA/Prima transaction.
 *
 * Created by shivam on 10/28/15.
 */
public class BankTransferInstructionActivity extends AppCompatActivity {

    private static final int PAGE_MARGIN = 20;
    private Toolbar mToolbar = null;
    private ViewPager mViewPager = null;
    private TabLayout mTabLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bank_transfer_instruction);
        initializeViews();

        if (getIntent() != null) {
            int position = getIntent().getIntExtra(Constants.POSITION, 0);
            mViewPager.setCurrentItem(position);
        }

    }


    /**
     * handles click of back arrow given on action bar.
     *
     * @param item
     * @return
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(
                getSupportFragmentManager());
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
        private static final int NUMBER_OF_PAGES = 3;

        public InstructionFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;

            if (position == 0) {

                InstructionMandiriFragment instructionMandiriFragment = new
                        InstructionMandiriFragment();
                fragment = instructionMandiriFragment;

            } else if (position == 1) {

                InstructionBCAFragment instructionBCAFragment = new
                        InstructionBCAFragment();
                fragment = instructionBCAFragment;

            } else {
                InstructionPermataFragment instructionPermataFragment = new
                        InstructionPermataFragment();
                fragment = instructionPermataFragment;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return getString(R.string.tab_mandiri);
            } else if (position == 1) {
                return getString(R.string.tab_bca_prima);
            } else {
                return getString(R.string.tab_permata_alto);
            }
        }
    }
}