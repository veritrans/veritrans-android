package com.midtrans.sdk.uikit.fragments;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BankTransferActivity;
import com.midtrans.sdk.uikit.adapters.InstructionFragmentPagerAdapter;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.MagicViewPager;

/**
 * Displays status information about mandiri bill pay's api call . Created by shivam on 10/28/15.
 * Deprecated, please refer to {@link com.midtrans.sdk.uikit.views.banktransfer.status.MandiriBillStatusActivity}
 */
@Deprecated
public class MandiriBillPayFragment extends Fragment {

    public static final String KEY_ARG = "args";
    public static final String TYPE_MANDIRI_BILL = "bank.mandiri.bill";
    private static final String LABEL_VA_NUMBER = "Virtual Account Number";
    private static final int PAGE_MARGIN = 20;
    private static final String DATA = "data";
    private static final String LABEL_BILL_CODE = "Bill Code Number";
    private static final String LABEL_COMPANY_CODE = "Company Code Number";
    private int POSITION = -1;
    private TransactionResponse mTransactionResponse = null;

    //views
    private TextView mTextViewCompanyCode = null;
    private TextView mTextViewBillpayCode = null;
    private TextView mTextViewValidity = null;
    private FancyButton btnCopyBillCode = null;
    private FancyButton btnCopyCompany = null;
    private FancyButton downloadInstructionButton;
    private TabLayout instructionTabs;
    private MagicViewPager instructionViewPager;


    /**
     * it creates new MandiriBillPayment object and set Transaction object to it, so later it can be
     * accessible using fragments getArgument().
     *
     * @param transactionResponse response of transaction call
     * @return instance of MandiriBillPayFragment.
     */
    public static MandiriBillPayFragment newInstance(TransactionResponse transactionResponse) {
        MandiriBillPayFragment fragment = new MandiriBillPayFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
        fragment.setArguments(data);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mandiri_bill_pay, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTransactionResponse = (TransactionResponse) getArguments().getSerializable(DATA);
        }
        initializeViews(view);
    }


    /**
     * initializes view and adds click listener for it.
     *
     * @param view view that needed to be initialized
     */
    private void initializeViews(View view) {
        mTextViewCompanyCode = (TextView) view.findViewById(R.id.text_company_code);
        mTextViewBillpayCode = (TextView) view.findViewById(R.id.text_bill_pay_code);

        mTextViewValidity = (TextView) view.findViewById(R.id.text_validity);
        btnCopyBillCode = (FancyButton) view.findViewById(R.id.btn_copy_va);
        btnCopyCompany = (FancyButton) view.findViewById(R.id.btn_copy_company_code);

        downloadInstructionButton = (FancyButton) view.findViewById(R.id.btn_download_instruction);
        instructionTabs = (TabLayout) view.findViewById(R.id.instruction_tabs);
        instructionViewPager = (MagicViewPager) view.findViewById(R.id.instruction_view_pager);

        setUpViewPager();
        setUpTabLayout();

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                btnCopyBillCode.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyBillCode.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyCompany.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyCompany.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                downloadInstructionButton.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                downloadInstructionButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }

            if (midtransSDK.getColorTheme().getPrimaryColor() !=0) {
                instructionTabs.setSelectedTabIndicatorColor(midtransSDK.getColorTheme().getPrimaryColor());
            }
        }

        if (mTransactionResponse != null) {
            if (mTransactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                    mTransactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_201))) {
                mTextViewCompanyCode.setText(mTransactionResponse.getCompanyCode());
            } else {
                mTextViewCompanyCode.setText(mTransactionResponse.getCompanyCode());
            }

            mTextViewBillpayCode.setText(mTransactionResponse.getPaymentCode());

            mTextViewValidity.setText(getString(R.string.text_format_valid_until, mTransactionResponse.getMandiriBillExpiration()));
        }

        downloadInstructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mTransactionResponse.getPdfUrl())) {
                    downloadInstructionButton.setVisibility(View.VISIBLE);
                    downloadInstructionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent webIntent = new Intent(Intent.ACTION_VIEW);
                            webIntent.setData(Uri.parse(mTransactionResponse.getPdfUrl()));
                            startActivity(webIntent);
                        }
                    });
                } else {
                    downloadInstructionButton.setVisibility(View.GONE);
                }
            }
        });
        btnCopyBillCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyBillCode();
            }
        });
        btnCopyCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCompanyCode();
            }
        });
    }

    /**
     * Copy generated Bill Code Number to clipboard.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyBillCode() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_BILL_CODE, mTextViewBillpayCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_bill_code, Toast.LENGTH_SHORT).show();
    }

    /**
     * Copy generated Company Code Number to clipboard.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyCompanyCode() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_COMPANY_CODE, mTextViewCompanyCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_company_code, Toast.LENGTH_SHORT).show();
    }

    private void setUpTabLayout() {
        instructionTabs.setupWithViewPager(instructionViewPager);
        instructionTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                instructionViewPager.setCurrentItem(position);
                changeCompletePaymentTitle(position);
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
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        instructionViewPager.setPageMargin(PAGE_MARGIN);

        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(getContext(), TYPE_MANDIRI_BILL, getChildFragmentManager(), 2);
        instructionViewPager.setAdapter(adapter);
        if (POSITION > -1) {
            instructionViewPager.setCurrentItem(POSITION);
        }

        instructionViewPager.clearOnPageChangeListeners();
        instructionViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeCompletePaymentTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeCompletePaymentTitle(int position) {
        FancyButton confirmButton = ((BankTransferActivity) getActivity()).getmButtonConfirmPayment();
        if (position == 0) {
            confirmButton.setText(getString(R.string.complete_payment_at_atm));
        } else {
            confirmButton.setText(getString(R.string.complete_payment_via_internet_banking));
        }
    }
}