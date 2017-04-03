package com.midtrans.sdk.ui.adapters;

/**
 * Created by ziahaqi on 4/3/17.
 */


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.views.instructions.InstructionATMBersamaFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionAltoFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionBCAFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionBCAKlikFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionBCAMobileFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionMandiriFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionMandiriInternetFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionPermataFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionPrimaFragment;
import com.midtrans.sdk.ui.widgets.MagicViewPager;

public class InstructionFragmentPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * number of pages / tabs.
     */
    private final Context context;
    private final int pages;
    private final String bank;
    private int currentPosition = -1;

    public InstructionFragmentPagerAdapter(Context context, String bank, FragmentManager fragmentManager, int pages) {
        super(fragmentManager);
        this.context = context;
        this.pages = pages;
        this.bank = bank;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (bank) {
            case PaymentType.BCA_VA:
                if (position == 0) {
                    fragment = new InstructionBCAFragment();
                } else if (position == 1) {
                    fragment = new InstructionBCAKlikFragment();
                } else {
                    fragment = new InstructionBCAMobileFragment();
                }
                break;
            case PaymentType.E_CHANNEL:
                if (position == 0) {
                    fragment = new InstructionMandiriFragment();
                } else {
                    fragment = new InstructionMandiriInternetFragment();
                }
                break;
            case PaymentType.PERMATA_VA:
                fragment = new InstructionPermataFragment();
                break;
            case PaymentType.BNI_VA:
                fragment = new InstructionPermataFragment();
                break;
            default:
                if (position == 0) {
                    fragment = new InstructionATMBersamaFragment();

                } else if (position == 1) {
                    fragment = new InstructionPrimaFragment();

                } else {
                    fragment = new InstructionAltoFragment();
                }
                break;
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
        String instructionTitle;

        switch (bank) {
            case PaymentType.BCA_VA:
                if (position == 0) {
                    instructionTitle = context.getString(R.string.tab_bca_atm);

                } else if (position == 1) {
                    instructionTitle = context.getString(R.string.tab_bca_klik);

                } else {
                    instructionTitle = context.getString(R.string.tab_bca_mobile);
                }
                break;
            case PaymentType.E_CHANNEL:
                if (position == 0) {
                    instructionTitle = context.getString(R.string.tab_mandiri_atm);
                } else {
                    instructionTitle = context.getString(R.string.tab_mandiri_internet);
                }
                break;
            case PaymentType.PERMATA_VA:
                if (position == 0) {
                    instructionTitle = context.getString(R.string.tab_permata_atm);

                } else {
                    instructionTitle = context.getString(R.string.tab_alto);
                }
                break;
            case PaymentType.BNI_VA:
                instructionTitle = context.getString(R.string.tab_atm_bersama);
                break;
            default:
                if (position == 0) {
                    instructionTitle = context.getString(R.string.tab_atm_bersama);

                } else if (position == 1) {
                    instructionTitle = context.getString(R.string.tab_prima);

                } else {
                    instructionTitle = context.getString(R.string.tab_alto);
                }
                break;
        }
        return instructionTitle;
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