package com.midtrans.sdk.ui.views.banktransfer.payment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseFragment;
import com.midtrans.sdk.ui.adapters.InstructionFragmentPagerAdapter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;

/**
 * Created by ziahaqi on 4/3/17.
 */

public class BankTransferPaymentFragment extends BaseFragment implements BankTransferPaymentContract.BankTransferPaymentView {
    public static final String ARGS_BANK_TYPE = "bank.type";
    public static final String PAGE = "page";
    public static final int KLIKBCA_PAGE = 1;
    private static final int PAGE_MARGIN = 20;

    private ViewPager pagerInstruction;
    private TabLayout tabLayout;
    private TextInputLayout textEmail;
    private AppCompatEditText editEmail;

    private BankTransferPaymentContract.Presenter presenter;
    private String bankType;
    private int POSITION = -1;

    public static BankTransferPaymentFragment newInstance(String bankType) {
        BankTransferPaymentFragment fragment = new BankTransferPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_BANK_TYPE, bankType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_banktransfer_payment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initProperties();
        bindView(view);
        setupView();
    }

    private void setupView() {
        setTextInputColorFilter(textEmail);
        setEditTextCompatBackgroundTintColor(editEmail);
        tabLayout.setSelectedTabIndicatorColor(getPrimaryColor());
        pagerInstruction.setPageMargin(PAGE_MARGIN);
        int pageNumber;
        String title;
        switch (bankType) {
            case PaymentType.BCA_VA:
                title = getString(R.string.bank_bca_transfer);
                pageNumber = 3;
                POSITION = getArguments().getInt(PAGE, -1);

                if (POSITION == KLIKBCA_PAGE) {
                    //track page bca va overview
                    presenter.trackEvent(AnalyticsEventName.PAGE_BCA_KLIKBCA_OVERVIEW);
                } else {
                    //track page bca va overview
                    presenter.trackEvent(AnalyticsEventName.PAGE_BCA_VA_OVERVIEW);
                }
                break;
            case PaymentType.E_CHANNEL:
                title = getString(R.string.mandiri_bill_transfer);
                pageNumber = 2;

                //track page mandiri bill overview
                presenter.trackEvent(AnalyticsEventName.PAGE_MANDIRI_BILL_OVERVIEW);
                break;
            case PaymentType.PERMATA_VA:
                title = getString(R.string.bank_permata_transfer);
                pageNumber = 2;

                //track page permata va overview
                presenter.trackEvent(AnalyticsEventName.PAGE_PERMATA_VA_OVERVIEW);
                break;
            case PaymentType.OTHER_VA:
                title = getString(R.string.other_bank_transfer);
                pageNumber = 3;

                //track page other bank va overview
                presenter.trackEvent(AnalyticsEventName.PAGE_OTHER_BANK_VA_OVERVIEW);
                break;
            case PaymentType.BNI_VA:
                title = getString(R.string.bank_transfer);
                pageNumber = 4;
                break;
            default:
                title = getString(R.string.bank_transfer);
                pageNumber = 0;
                break;
        }
        ((BankTransferPaymentActivity) getActivity()).setHeaderTitle(title);
        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(getContext(), bankType, getChildFragmentManager(), pageNumber);
        pagerInstruction.setAdapter(adapter);
        if (POSITION > -1) {
            pagerInstruction.setCurrentItem(POSITION);
        }
        setUpTabLayout();
    }


    private void bindView(View view) {
        pagerInstruction = (ViewPager) view.findViewById(R.id.tab_view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_instructions);
        editEmail = (AppCompatEditText) view.findViewById(R.id.et_email);
        textEmail = (TextInputLayout) view.findViewById(R.id.email_til);
    }

    private void initProperties() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            bankType = bundle.getString(ARGS_BANK_TYPE);
        }
    }


    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(pagerInstruction);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagerInstruction.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void setPresenter(@NonNull BankTransferPaymentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public String getUserEmail() {
        return editEmail.getText().toString().trim();
    }


    @Override
    public void onPaymentError(String error) {
        ((BankTransferPaymentActivity) getActivity()).showPaymentStatus(new PaymentResult(error));
    }


    @Override
    public void onPaymentFailure(PaymentResult paymentResult) {
        UiUtils.hideProgressDialog();
        ((BankTransferPaymentActivity) getActivity()).showPaymentStatus(paymentResult);
    }

    @Override
    public void onPaymentSuccess(PaymentResult paymentResult) {
        UiUtils.hideProgressDialog();
        ((BankTransferPaymentActivity) getActivity()).showPaymentStatus(paymentResult);
    }

}
