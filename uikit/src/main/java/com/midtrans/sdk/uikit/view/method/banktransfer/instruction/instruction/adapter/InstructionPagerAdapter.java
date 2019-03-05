package com.midtrans.sdk.uikit.view.method.banktransfer.instruction.instruction.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.view.method.banktransfer.instruction.instruction.fragment.InstructionVaBcaFragment;
import com.midtrans.sdk.uikit.view.method.banktransfer.instruction.instruction.fragment.InstructionVaBniFragment;
import com.midtrans.sdk.uikit.view.method.banktransfer.instruction.instruction.fragment.InstructionVaMandiriFragment;
import com.midtrans.sdk.uikit.view.method.banktransfer.instruction.instruction.fragment.InstructionVaOtherFragment;
import com.midtrans.sdk.uikit.view.method.banktransfer.instruction.instruction.fragment.InstructionVaPermataFragment;
import com.midtrans.sdk.uikit.widget.MagicViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class InstructionPagerAdapter extends FragmentStatePagerAdapter {
    private final Context context;
    private final int pageNumber;
    private final String paymentType;
    private int currentPosition = -1;
    private PaymentInfoResponse paymentInfoResponse;

    public InstructionPagerAdapter(
            Context context,
            @NonNull String paymentType,
            FragmentManager fragmentManager,
            int pageNumber,
            PaymentInfoResponse paymentInfoResponse
    ) {
        super(fragmentManager);
        this.context = context;
        this.pageNumber = pageNumber;
        this.paymentType = paymentType;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    @Override
    public Fragment getItem(int position) {
        String title = getPageTitle(position).toString();
        Fragment fragment;

        if (TextUtils.isEmpty(paymentType)) {
            title = changeTitleIfTypeAltoOrPrima(title);
            fragment = InstructionVaOtherFragment.newInstance(Constants.INSTRUCTION_THIRD_POSITION, title, paymentInfoResponse);
        } else {
            switch (paymentType) {
                case PaymentType.BCA_VA:
                    fragment = InstructionVaBcaFragment.newInstance(position, title, paymentInfoResponse);
                    break;
                case PaymentType.PERMATA_VA:
                    title = changeTitleIfTypeAltoOrPrima(title);
                    fragment = InstructionVaPermataFragment.newInstance(position, title, paymentInfoResponse);
                    break;
                case PaymentType.ECHANNEL:
                    fragment = InstructionVaMandiriFragment.newInstance(position, title, paymentInfoResponse);
                    break;
                case PaymentType.BNI_VA:
                    fragment = InstructionVaBniFragment.newInstance(position, title, paymentInfoResponse);
                    break;
                default:
                    title = changeTitleIfTypeAltoOrPrima(title);
                    fragment = InstructionVaOtherFragment.newInstance(position, title, paymentInfoResponse);
                    break;
            }
        }

        return fragment;
    }

    private String changeTitleIfTypeAltoOrPrima(String title) {
        String newTitle = title;
        if (!TextUtils.isEmpty(title)) {
            if (newTitle.equalsIgnoreCase(context.getString(R.string.tab_alto)) ||
                    newTitle.equalsIgnoreCase(context.getString(R.string.tab_atm_bersama))) {
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

                case PaymentType.ECHANNEL:
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
            case Constants.ATM_BERSAMA:
                return context.getString(R.string.pay_with_atm_bersama);
            case Constants.PRIMA:
                return context.getString(R.string.pay_with_prima);
            case Constants.ALTO:
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
