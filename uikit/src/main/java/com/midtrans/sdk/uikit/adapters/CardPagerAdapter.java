package com.midtrans.sdk.uikit.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.uikit.fragments.CardDetailFragment;

import java.util.ArrayList;

/**
 * Created by chetan on 27/10/15.
 */
public class CardPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<SaveCardRequest> cardDetails;
    private Fragment parentFragment;
    private long baseId = 0;
    private Activity activity;

    public CardPagerAdapter(Fragment fragment, FragmentManager fm, ArrayList<SaveCardRequest>
            cardDetails, Activity activity) {
        super(fm);
        this.cardDetails = cardDetails;
        parentFragment = fragment;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        return CardDetailFragment.newInstance(cardDetails.get(position), parentFragment, activity);
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
