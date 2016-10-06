package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.InstructionATMBersamaFragment;
import com.midtrans.sdk.uikit.fragments.InstructionAltoFragment;
import com.midtrans.sdk.uikit.fragments.InstructionBCAFragment;
import com.midtrans.sdk.uikit.fragments.InstructionBCAKlikFragment;
import com.midtrans.sdk.uikit.fragments.InstructionBCAMobileFragment;
import com.midtrans.sdk.uikit.fragments.InstructionMandiriFragment;
import com.midtrans.sdk.uikit.fragments.InstructionMandiriInternetFragment;
import com.midtrans.sdk.uikit.fragments.InstructionPermataFragment;
import com.midtrans.sdk.uikit.fragments.InstructionPrimaFragment;

/**
 * It display information related to mandiri bill pay , bank transfer and BCA/Prima transaction.
 *
 * Created by shivam on 10/28/15.
 */
public class BankTransferInstructionActivity extends BaseActivity {

    public static final String DOWNLOAD_URL = "url";
    public static final String BANK = "bank";
    public static final String TYPE_BCA = "bank.bca";
    public static final String TYPE_PERMATA = "bank.permata";
    public static final String TYPE_MANDIRI = "bank.mandiri";
    public static final String TYPE_MANDIRI_BILL = "bank.mandiri.bill";
    public static final String TYPE_ALL_BANK = "bank.others";
    public static final String PAGE = "page";
    public static final int KLIKBCA_PAGE = 1;
    private static final int PAGE_MARGIN = 20;
    private int POSITION = -1;

    private Toolbar mToolbar = null;
    private ViewPager mViewPager = null;
    private TabLayout mTabLayout = null;
    private Button downloadInstruction = null;
    private MidtransSDK midtransSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        midtransSDK = MidtransSDK.getInstance();
        setContentView(R.layout.activity_bank_transfer_instruction);
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
        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        mToolbar.setNavigationIcon(closeIcon);
        mToolbar.setTitle(getResources().getString(R.string.payment_instrution));
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.instruction_tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager_bank_instruction);
        downloadInstruction = (Button) findViewById(R.id.btn_download_instruction);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(DOWNLOAD_URL))) {
            downloadInstruction.setVisibility(View.VISIBLE);
            downloadInstruction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(Uri.parse(getIntent().getStringExtra(DOWNLOAD_URL)));
                    startActivity(webIntent);
                }
            });
        } else {
            downloadInstruction.setVisibility(View.GONE);
        }
        initializeTheme();

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
                POSITION = getIntent().getIntExtra(PAGE, -1);
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
            case TYPE_ALL_BANK:
                pageNumber = 3;
                break;
            default:
                pageNumber = 0;
                break;
        }
        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(
                getSupportFragmentManager(), pageNumber);
        mViewPager.setAdapter(adapter);
        if (POSITION > -1) {
            mViewPager.setCurrentItem(POSITION);
        }
    }


    /**
     * adapter for view pager which will provide instruction fragment depending upon tab click and
     * view pager swap position.
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

            if (getIntent().getStringExtra(BANK).equals(TYPE_BCA)) {
                if (position == 0) {
                    fragment = new InstructionBCAFragment();
                } else if (position == 1) {
                    fragment = new InstructionBCAKlikFragment();
                } else fragment = new InstructionBCAMobileFragment();
            } else if (getIntent().getStringExtra(BANK).equals(TYPE_PERMATA)) {
                fragment = new InstructionPermataFragment();
            } else if (getIntent().getStringExtra(BANK).equals(TYPE_MANDIRI) ||
                    getIntent().getStringExtra(BANK).equals(TYPE_MANDIRI_BILL)) {
                if (position == 0) {
                    fragment = new InstructionMandiriFragment();
                } else {
                    fragment = new InstructionMandiriInternetFragment();
                }
            } else {
                if (position == 0) {
                    fragment = new InstructionATMBersamaFragment();
                } else if (position == 1) {
                    fragment = new InstructionPrimaFragment();
                } else {
                    fragment = new InstructionAltoFragment();
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

            if (getIntent().getStringExtra(BANK).equals(TYPE_BCA)) {
                if (position == 0) {
                    return getString(R.string.tab_bca_atm);
                } else if (position == 1) {
                    return getString(R.string.tab_bca_klik);
                } else return getString(R.string.tab_bca_mobile);
            } else if (getIntent().getStringExtra(BANK).equals(TYPE_PERMATA)) {
                if (position == 0) return getString(R.string.tab_permata_atm);
                else return getString(R.string.tab_alto);
            } else if (getIntent().getStringExtra(BANK).equals(TYPE_MANDIRI_BILL) ||
                    getIntent().getStringExtra(BANK).equals(TYPE_MANDIRI)) {
                if (position == 0) {
                    return getString(R.string.tab_mandiri_atm);
                } else {
                    return getString(R.string.tab_mandiri_internet);
                }
            } else {
                if (position == 0) {
                    return getString(R.string.tab_atm_bersama);
                } else if (position == 1) {
                    return getString(R.string.tab_prima);
                } else {
                    return getString(R.string.tab_alto);
                }
            }
        }
    }
}