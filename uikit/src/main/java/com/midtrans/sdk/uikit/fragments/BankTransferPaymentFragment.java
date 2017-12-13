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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BankTransferActivity;
import com.midtrans.sdk.uikit.activities.BankTransferInstructionActivity;
import com.midtrans.sdk.uikit.adapters.InstructionFragmentPagerAdapter;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.MagicViewPager;

/**
 * Displays status information about bank transfer's api call .
 * <p>
 * Created by shivam on 10/27/15.
 */
public class BankTransferPaymentFragment extends Fragment {

    public static final String KEY_ARG = "args";
    public static final String TYPE_BCA = "bank.bca";
    public static final String TYPE_BNI = "bank.bni";
    public static final String TYPE_PERMATA = "bank.permata";
    public static final String TYPE_MANDIRI = "bank.mandiri";
    public static final String TYPE_MANDIRI_BILL = "bank.mandiri.bill";
    public static final String TYPE_ALL_BANK = "bank.others";
    public static final String PAGE = "page";
    private static final String LABEL_VA_NUMBER = "Virtual Account Number";
    private static final int PAGE_MARGIN = 20;
    private static final String TAG = "BankTransferFragment";
    private int POSITION = -1;

    private TransactionResponse transactionResponse;
    private MagicViewPager instructionViewPager;
    private FancyButton downloadInstructionButton;
    private TabLayout instructionTabs;

    //views
    private TextView mTextViewVirtualAccountNumber = null;
    private TextView mTextViewValidity = null;
    private FancyButton btnCopyToClipboard = null;


    /**
     * it creates new BankTransferPaymentFragment object and set Transaction object to it, so later
     * it can be accessible using fragments getArgument().
     *
     * @param transactionResponse response of transaction call
     * @return instance of BankTransferPaymentFragment
     */
    public static BankTransferPaymentFragment newInstance(TransactionResponse transactionResponse, String bank) {
        BankTransferPaymentFragment fragment = new BankTransferPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ARG, transactionResponse);
        bundle.putString(BankTransferInstructionActivity.BANK, bank);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_transfer_payment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transactionResponse = (TransactionResponse) getArguments().getSerializable(KEY_ARG);
        initializeViews(view);
    }


    /**
     * initializes view and adds click listener for it.
     *
     * @param view view that needed to be initialized
     */
    private void initializeViews(View view) {
        mTextViewVirtualAccountNumber = (TextView) view.findViewById(R.id.text_virtual_account_number);
        mTextViewValidity = (TextView) view.findViewById(R.id.text_validity);
        btnCopyToClipboard = (FancyButton) view.findViewById(R.id.btn_copy_va);
        instructionViewPager = (MagicViewPager) view.findViewById(R.id.instruction_view_pager);
        downloadInstructionButton = (FancyButton) view.findViewById(R.id.btn_download_instruction);
        instructionTabs = (TabLayout) view.findViewById(R.id.instruction_tabs);

        setUpViewPager();
        setUpTabLayout();

        try {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();
            if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
                if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                    downloadInstructionButton.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                    downloadInstructionButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                    btnCopyToClipboard.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                    btnCopyToClipboard.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                }

                if (midtransSDK.getColorTheme().getPrimaryColor() != 0) {
                    instructionTabs.setSelectedTabIndicatorColor(midtransSDK.getColorTheme().getPrimaryColor());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "theme:" + e.getMessage());
        }

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_200))
                    || transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_201))) {
                if (getArguments() != null && getArguments().getString(BankTransferInstructionActivity.BANK) != null) {
                    if (getArguments().getString(BankTransferInstructionActivity.BANK).equals(BankTransferInstructionActivity.TYPE_BCA)) {
                        mTextViewVirtualAccountNumber.setText(transactionResponse.getBcaVaNumber());
                        mTextViewValidity.setText(getString(R.string.text_format_valid_until, transactionResponse.getBcaExpiration()));
                    } else if (getArguments().getString(BankTransferInstructionActivity.BANK).equals(BankTransferInstructionActivity.TYPE_PERMATA)) {
                        mTextViewVirtualAccountNumber.setText(transactionResponse.getPermataVANumber());
                        mTextViewValidity.setText(getString(R.string.text_format_valid_until, transactionResponse.getPermataExpiration()));
                    } else if (getArguments().getString(BankTransferInstructionActivity.BANK).equals(BankTransferInstructionActivity.TYPE_BNI)) {
                        mTextViewVirtualAccountNumber.setText(transactionResponse.getBniVaNumber());
                        mTextViewValidity.setText(getString(R.string.text_format_valid_until, transactionResponse.getBniExpiration()));
                    } else {
                        mTextViewVirtualAccountNumber.setText(transactionResponse.getPermataVANumber());
                        mTextViewValidity.setText(getString(R.string.text_format_valid_until, transactionResponse.getPermataExpiration()));
                    }
                }
            } else {
                mTextViewVirtualAccountNumber.setText("");
                mTextViewValidity.setText(transactionResponse.getStatusMessage());
            }


        }
        if (!TextUtils.isEmpty(transactionResponse.getPdfUrl())) {
            downloadInstructionButton.setVisibility(View.VISIBLE);
            downloadInstructionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(Uri.parse(transactionResponse.getPdfUrl()));
                    startActivity(webIntent);
                }
            });
        } else {
            downloadInstructionButton.setVisibility(View.GONE);
        }
        btnCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyVANumber();
            }
        });
    }

    /**
     * Copy generated Virtual Account Number to clipboard.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyVANumber() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_VA_NUMBER, mTextViewVirtualAccountNumber.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
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
        int pageNumber;
        switch (getArguments().getString(BankTransferInstructionActivity.BANK)) {
            case TYPE_BCA:
                pageNumber = 3;
                break;
            case TYPE_PERMATA:
                pageNumber = 2;
                break;
            case TYPE_BNI:
                pageNumber = 3;
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
        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(getContext(), getArguments().getString(BankTransferInstructionActivity.BANK), getChildFragmentManager(), pageNumber);
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
        String title = getArguments().getString(BankTransferInstructionActivity.BANK);
        FancyButton confirmBtn = ((BankTransferActivity) getActivity()).getmButtonConfirmPayment();
        if (title.equals(BankTransferFragment.TYPE_BCA)) {
            if (position == 0) {
                confirmBtn.setText(getString(R.string.complete_payment_at_atm));
            } else if (position == 1) {
                confirmBtn.setText(getString(R.string.complete_payment_at_klik_bca_va));
            } else {
                confirmBtn.setText(getString(R.string.complete_payment_at_mobile_bca));
            }
        } else if (title.equals(BankTransferFragment.TYPE_PERMATA)) {
            if (position == 0) {
                confirmBtn.setText(getString(R.string.complete_payment_at_atm));
            } else {
                confirmBtn.setText(getString(R.string.complete_payment_at_atm));
            }
        } else if (title.equals(BankTransferFragment.TYPE_MANDIRI_BILL) ||
                title.equals(BankTransferFragment.TYPE_MANDIRI)) {
            if (position == 0) {
                confirmBtn.setText(getString(R.string.complete_payment_at_atm));
            } else {
                confirmBtn.setText(getString(R.string.complete_payment_via_internet_banking));
            }
        } else if (title.equals(BankTransferFragment.TYPE_BNI)) {
            if (position == 0) {
                confirmBtn.setText(getString(R.string.complete_payment_at_atm));
            } else if (position == 1) {
                confirmBtn.setText(getString(R.string.complete_payment_va_bni_mobile));
            } else {
                confirmBtn.setText(getString(R.string.complete_payment_via_internet_banking));
            }
        } else {
            if (position == 0) {
                confirmBtn.setText(getString(R.string.complete_payment_at_atm));
            } else if (position == 1) {
                confirmBtn.setText(getString(R.string.complete_payment_at_atm));
            } else {
                confirmBtn.setText(getString(R.string.complete_payment_at_atm));
            }
        }
    }
}