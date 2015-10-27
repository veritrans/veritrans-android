package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.adapters.CardPagerAdapter;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.models.CardDetail;
import id.co.veritrans.sdk.widgets.CirclePageIndicator;

public class SavedCardFragment extends Fragment {
    private ViewPager savedCardPager;
    private CirclePageIndicator circlePageIndicator;

    public static SavedCardFragment newInstance() {
        SavedCardFragment fragment = new SavedCardFragment();
        return fragment;
    }

    public SavedCardFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_card, container, false);
        savedCardPager = (ViewPager) view.findViewById(R.id.saved_card_pager);
        float cardWidth = ((CreditDebitCardFlowActivity) getActivity()).getScreenWidth();
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)cardHeight);
        savedCardPager.setLayoutParams(parms);
        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        createDummyCards();

        CardPagerAdapter cardPagerAdapter = new CardPagerAdapter(getChildFragmentManager(),cardDetails);
        savedCardPager.setAdapter(cardPagerAdapter);
        savedCardPager.setPageMargin(-50);
        savedCardPager.setHorizontalFadingEdgeEnabled(true);
        savedCardPager.setFadingEdgeLength(30);
        /*savedCardPager.setPageMargin(16);
        savedCardPager.setClipChildren(false);*/
        circlePageIndicator.setViewPager(savedCardPager);
        return view;
    }
    ArrayList<CardDetail>cardDetails = new ArrayList<>();
    private void createDummyCards() {
        for(int i= 0;i<4;i++){
            CardDetail cardDetail = new CardDetail();
            cardDetail.setCardHolderName("James Anderson");
            cardDetail.setCardNumber("1234 XXXX XXXX 432" + i);
            cardDetails.add(cardDetail);
        }
    }
}
