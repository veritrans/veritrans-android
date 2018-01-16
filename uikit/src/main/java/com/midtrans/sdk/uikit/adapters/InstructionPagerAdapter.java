package com.midtrans.sdk.uikit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionBcaVaFragment;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionBniVaFragment;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionMandiriVaFragment;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionOtherBankFragment;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionPermataVaFragment;
import com.midtrans.sdk.uikit.widgets.MagicViewPager;

/**
 * Created by ziahaqi on 8/15/17.
 */

public class InstructionPagerAdapter extends FragmentStatePagerAdapter {
    private final Context context;
    private final int pageNumber;
    private final String paymentType;
    private int currentPosition = -1;

    public InstructionPagerAdapter(Context context, @NonNull String paymentType, FragmentManager fragmentManager, int pageNumber) {
        super(fragmentManager);
        this.context = context;
        this.pageNumber = pageNumber;
        this.paymentType = paymentType;
    }

    @Override
    public Fragment getItem(int position) {
        String title = getPageTitle(position).toString();
        Fragment fragment;

        if (TextUtils.isEmpty(paymentType)) {
            title = changeTitleIfTypeAltoOrPrima(title);
            fragment = InstructionOtherBankFragment.newInstance(UiKitConstants.INSTRUCTION_THIRD_POSITION, title);
        } else {
            switch (paymentType) {
                case PaymentType.BCA_VA:
                    fragment = InstructionBcaVaFragment.newInstance(position, title);
                    break;
                case PaymentType.PERMATA_VA:
                    title = changeTitleIfTypeAltoOrPrima(title);
                    fragment = InstructionPermataVaFragment.newInstance(position, title);
                    break;
                case PaymentType.E_CHANNEL:
                    fragment = InstructionMandiriVaFragment.newInstance(position, title);
                    break;
                case PaymentType.BNI_VA:
                    fragment = InstructionBniVaFragment.newInstance(position, title);
                    break;
                default:
                    title = changeTitleIfTypeAltoOrPrima(title);
                    fragment = InstructionOtherBankFragment.newInstance(position, title);
                    break;
            }
        }

        return fragment;
    }

    private String changeTitleIfTypeAltoOrPrima(String title) {
        String newTitle = title;
        if (!TextUtils.isEmpty(title)) {
            if (newTitle.equalsIgnoreCase(context.getString(R.string.tab_alto)) ||
                    newTitle.equalsIgnoreCase(context.getString(R.string.tab_prima))) {
                newTitle = context.getString(R.string.instruction_atm_with, title);
            }
        }
        return newTitle;
    }

    @Override
    public int getCount() {
        return pageNumber;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle;

        if (TextUtils.isEmpty(paymentType)) {
            pageTitle = context.getString(R.string.tab_alto);
        } else {
            switch (paymentType) {

                case PaymentType.BCA_VA:
                    if (position == 0) {
                        pageTitle = context.getString(R.string.tab_bca_atm);
                    } else if (position == 1) {
                        pageTitle = context.getString(R.string.tab_bca_klik);
                    } else {
                        pageTitle = context.getString(R.string.tab_bca_mobile);
                    }

                    break;

                case PaymentType.PERMATA_VA:
                    if (position == 0) {
                        pageTitle = context.getString(R.string.tab_permata_atm);
                    } else {
                        pageTitle = context.getString(R.string.tab_alto);
                    }
                    break;

                case PaymentType.E_CHANNEL:
                    if (position == 0) {
                        pageTitle = context.getString(R.string.tab_mandiri_atm);

                    } else {
                        pageTitle = context.getString(R.string.tab_mandiri_internet);
                    }
                    break;

                case PaymentType.BNI_VA:
                    if (position == 0) {
                        pageTitle = context.getString(R.string.tab_atm_bni);
                    } else if (position == 1) {
                        pageTitle = context.getString(R.string.tab_bni_mobile);
                    } else {
                        pageTitle = context.getString(R.string.tab_bni_internet);
                    }
                    break;

                default:
                    if (position == 0) {
                        pageTitle = context.getString(R.string.tab_prima);
                    } else if (position == 1) {
                        pageTitle = context.getString(R.string.tab_atm_bersama);
                    } else {
                        pageTitle = context.getString(R.string.tab_alto);
                    }

                    break;
            }
        }

        return pageTitle;
    }

    /**
     * Get payment button text for other ATM network such as ATM Bersama, Prima, and Alto
     *
     * @param position to recognize which text should be displayed, -1 for default text
     */
    public String getPayButtonText(int position) {
        switch (position) {
            case UiKitConstants.ATM_BERSAMA:
                return context.getString(R.string.pay_with_atm_bersama);
            case UiKitConstants.PRIMA:
                return context.getString(R.string.pay_with_prima);
            case UiKitConstants.ALTO:
                return context.getString(R.string.pay_with_alto);
            default:
                return context.getString(R.string.pay_now);
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
