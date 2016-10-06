package com.midtrans.sdk.uikit.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.uikit.fragments.RegisterCardDetailsFragment;

import java.util.ArrayList;

/**
 * @author rakawm
 */
public class RegisterCardPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<SaveCardRequest> cardDetails;
    private Fragment parentFragment;
    private long baseId = 0;
    private Activity activity;

    public RegisterCardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public RegisterCardPagerAdapter(Fragment fragment, FragmentManager fm, ArrayList<SaveCardRequest>
            cardDetails, Activity activity) {
        super(fm);
        this.cardDetails = cardDetails;
        parentFragment = fragment;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        return RegisterCardDetailsFragment.newInstance(cardDetails.get(position), parentFragment, activity);
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }

    @Override
    public int getCount() {
        if (cardDetails != null && cardDetails.size() > 0) {
            return cardDetails.size();
        } else {
            return 0;
        }
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        // give an ID different from position when position has been changed
        return baseId + position;
    }

    /**
     * Notify that the position of a fragment has been changed. Create a new ID for each position to
     * force recreation of the fragment
     *
     * @param n number of items which have been changed
     */
    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }
}
