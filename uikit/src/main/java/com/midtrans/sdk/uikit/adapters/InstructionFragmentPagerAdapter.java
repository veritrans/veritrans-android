package com.midtrans.sdk.uikit.adapters;

/**
 * Created by rakawm on 2/28/17.
 */


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BankTransferFragment;
import com.midtrans.sdk.uikit.fragments.InstructionATMBersamaFragment;
import com.midtrans.sdk.uikit.fragments.InstructionAltoFragment;
import com.midtrans.sdk.uikit.fragments.InstructionAtmBniFragment;
import com.midtrans.sdk.uikit.fragments.InstructionBCAFragment;
import com.midtrans.sdk.uikit.fragments.InstructionBCAKlikFragment;
import com.midtrans.sdk.uikit.fragments.InstructionBCAMobileFragment;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionBniVaFragment;
import com.midtrans.sdk.uikit.fragments.InstructionBniMobileFragment;
import com.midtrans.sdk.uikit.fragments.InstructionMandiriFragment;
import com.midtrans.sdk.uikit.fragments.InstructionMandiriInternetFragment;
import com.midtrans.sdk.uikit.fragments.InstructionPermataFragment;
import com.midtrans.sdk.uikit.fragments.InstructionPrimaFragment;
import com.midtrans.sdk.uikit.widgets.MagicViewPager;

public class InstructionFragmentPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * number of pages / tabs.
     */
    private final Context context;
    private final int pages;
    private final String bank;
    private int currentPosition = -1;

    public InstructionFragmentPagerAdapter(Context context, String bank, FragmentManager fragmentManager, int number) {
        super(fragmentManager);
        this.context = context;
        this.pages = number;
        this.bank = bank;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        if (bank.equals(BankTransferFragment.TYPE_BCA)) {
            if (position == 0) {
                fragment = new InstructionBCAFragment();
            } else if (position == 1) {
                fragment = new InstructionBCAKlikFragment();
            } else fragment = new InstructionBCAMobileFragment();

        } else if (bank.equals(BankTransferFragment.TYPE_PERMATA)) {
           if(position == 0){
               fragment = new InstructionPermataFragment();
           }else{
               fragment = new InstructionAltoFragment();
           }
        } else if (bank.equals(BankTransferFragment.TYPE_MANDIRI) ||
                bank.equals(BankTransferFragment.TYPE_MANDIRI_BILL)) {
            if (position == 0) {
                fragment = new InstructionMandiriFragment();
            } else {
                fragment = new InstructionMandiriInternetFragment();
            }
        } else if (bank.equals(BankTransferFragment.TYPE_BNI)) {
            if (position == 0) {
                fragment = new InstructionAtmBniFragment();
            } else if (position == 1) {
                fragment = new InstructionBniMobileFragment();
            }else{
                fragment = new InstructionBniVaFragment();
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

        if (bank.equals(BankTransferFragment.TYPE_BCA)) {
            if (position == 0) {
                return context.getString(R.string.tab_bca_atm);
            } else if (position == 1) {
                return context.getString(R.string.tab_bca_klik);
            } else return context.getString(R.string.tab_bca_mobile);
        } else if (bank.equals(BankTransferFragment.TYPE_PERMATA)) {
            if (position == 0) return context.getString(R.string.tab_permata_atm);
            else return context.getString(R.string.tab_alto);
        } else if (bank.equals(BankTransferFragment.TYPE_MANDIRI_BILL) ||
                bank.equals(BankTransferFragment.TYPE_MANDIRI)) {
            if (position == 0) {
                return context.getString(R.string.tab_mandiri_atm);
            } else {
                return context.getString(R.string.tab_mandiri_internet);
            }
        } else if (bank.equals(BankTransferFragment.TYPE_BNI)) {
            if (position == 0) {
                return context.getString(R.string.tab_atm_bni);
            } else if (position == 1) {
                return context.getString(R.string.tab_bni_mobile);
            } else {
                return context.getString(R.string.tab_bni_internet);
            }
        } else {
            if (position == 0) {
                return context.getString(R.string.tab_atm_bersama);
            } else if (position == 1) {
                return context.getString(R.string.tab_prima);
            } else {
                return context.getString(R.string.tab_alto);
            }
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != currentPosition) {
            Fragment fragment = (Fragment) object;
            MagicViewPager pager = (MagicViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                currentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }
}