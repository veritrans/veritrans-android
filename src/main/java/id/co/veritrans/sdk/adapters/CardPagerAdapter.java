package id.co.veritrans.sdk.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import id.co.veritrans.sdk.fragments.CardDetailFragment;
import id.co.veritrans.sdk.models.CardTokenRequest;

/**
 * Created by chetan on 27/10/15.
 */
public class CardPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<CardTokenRequest> cardDetails;

    public CardPagerAdapter(FragmentManager fm, ArrayList<CardTokenRequest> cardDetails) {
        super(fm);
        this.cardDetails = cardDetails;
    }

    @Override
    public Fragment getItem(int position) {
        return CardDetailFragment.newInstance(cardDetails.get(position));
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
}
