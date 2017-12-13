package com.midtrans.sdk.uikit.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BankTransferInstructionActivity;
import com.midtrans.sdk.uikit.adapters.InstructionFragmentPagerAdapter;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import java.lang.reflect.Field;

/**
 * It displays payment related instructions on the screen. Created by shivam on 10/27/15.
 */
public class BankTransferFragment extends Fragment {
    public static final String DOWNLOAD_URL = "url";
    public static final String BANK = "bank";
    public static final String TYPE_BCA = "bank.bca";
    public static final String TYPE_BNI = "bank.bni";
    public static final String TYPE_PERMATA = "bank.permata";
    public static final String TYPE_MANDIRI = "bank.mandiri";
    public static final String TYPE_MANDIRI_BILL = "bank.mandiri.bill";
    public static final String TYPE_ALL_BANK = "bank.others";
    public static final String PAGE = "page";
    public static final int KLIKBCA_PAGE = 1;
    private static final int PAGE_MARGIN = 20;
    private static final String TAG = "BankTransferFragment";
    private int POSITION = -1;

    private ViewPager instructionViewPager = null;
    private TabLayout instructionTab = null;
    private TextInputLayout mTextInputEmailId = null;
    private AppCompatEditText mEditTextEmailId = null;
    private DefaultTextView textNotificationToken;
    private DefaultTextView textNotificationOtp;

    private UserDetail userDetail;

    public static BankTransferFragment newInstance(String bank, int position) {
        BankTransferFragment bankTransferFragment = new BankTransferFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE, position);
        bundle.putString(BANK, bank);
        bankTransferFragment.setArguments(bundle);
        return bankTransferFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_transfer, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
        setUpViewPager();
        setUpTabLayout();
    }

    /**
     * initializes view and adds click listener for it.
     *
     * @param view view that needed to be initialized
     */
    private void initializeViews(View view) {
        instructionViewPager = (ViewPager) view.findViewById(R.id.tab_view_pager);
        instructionTab = (TabLayout) view.findViewById(R.id.tab_instructions);
        mEditTextEmailId = (AppCompatEditText) view.findViewById(R.id.et_email);
        mTextInputEmailId = (TextInputLayout) view.findViewById(R.id.email_til);
        textNotificationToken = (DefaultTextView) view.findViewById(R.id.text_notificationToken);
        textNotificationOtp = (DefaultTextView) view.findViewById(R.id.text_notificationOtp);
        try {
            userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mEditTextEmailId.setText(userDetail.getEmail());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                // Set color filter in edit text
                try {
                    Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                    fDefaultTextColor.setAccessible(true);
                    fDefaultTextColor.set(mTextInputEmailId, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                    fFocusedTextColor.setAccessible(true);
                    fFocusedTextColor.set(mTextInputEmailId, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    mEditTextEmailId.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            if (midtransSDK.getColorTheme().getPrimaryColor() != 0) {
                instructionTab.setSelectedTabIndicatorColor(midtransSDK.getColorTheme().getPrimaryColor());
            }
        }
    }


    /**
     * created to give access to email id field from {
     * .BankTransferActivity}.
     *
     * @return email identifier
     */
    public String getEmailId() {
        if (mEditTextEmailId != null) {
            return mEditTextEmailId.getText().toString();
        } else {
            return null;
        }
    }

    private void setUpViewPager() {
        instructionViewPager.setPageMargin(PAGE_MARGIN);
        int pageNumber;
        String bankInstruction = getArguments().getString(BANK);
        switch (bankInstruction) {
            case TYPE_BCA:
                pageNumber = 3;
                POSITION = getArguments().getInt(PAGE, -1);
                break;
            case TYPE_PERMATA:
                pageNumber = 2;
                break;
            case TYPE_MANDIRI:
                pageNumber = 2;
                break;
            case TYPE_BNI:
                pageNumber = 3;
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
        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(getContext(), bankInstruction, getChildFragmentManager(), pageNumber);
        instructionViewPager.setAdapter(adapter);
        if (POSITION > -1) {
            instructionViewPager.setCurrentItem(POSITION);
        }
    }

    private void setUpTabLayout() {
        instructionTab.setupWithViewPager(instructionViewPager);
        instructionTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                instructionViewPager.setCurrentItem(tab.getPosition());
                initTopNotification(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initTopNotification(int position) {
        if (getArguments() != null) {
            String bank = getArguments().getString(BankTransferInstructionActivity.BANK);
            if (!TextUtils.isEmpty(bank) && bank.equals(BankTransferFragment.TYPE_BCA)) {
                if (position == 1) {
                    showTokenNotification(true);
                } else {
                    showTokenNotification(false);
                }
            } else if (!TextUtils.isEmpty(bank) && bank.equals(BankTransferFragment.TYPE_BNI)) {
                if (position == 1) {
                    showOtpNotification(true);
                } else {
                    showOtpNotification(false);
                }
            } else {
                showOtpNotification(false);
                showTokenNotification(false);
            }
        }
    }

    private void showOtpNotification(boolean show) {
        if (show) {
            textNotificationOtp.setVisibility(View.VISIBLE);
            final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
            textNotificationOtp.startAnimation(animation);

        } else {
            textNotificationOtp.setVisibility(View.GONE);
            textNotificationOtp.setAnimation(null);
        }
    }

    private void showTokenNotification(boolean show) {
        if (show) {
            textNotificationToken.setVisibility(View.VISIBLE);
            final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
            textNotificationToken.startAnimation(animation);

        } else {
            textNotificationToken.setVisibility(View.GONE);
            textNotificationToken.setAnimation(null);
        }
    }

}