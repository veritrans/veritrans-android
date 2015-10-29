package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.models.CardDetail;
import id.co.veritrans.sdk.widgets.CirclePageIndicator;

public class SavedCardFragment extends Fragment {
    private ViewPager savedCardPager;
    private CirclePageIndicator circlePageIndicator;
    private FloatingActionButton addCardBt;

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
        ((CreditDebitCardFlowActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.saved_card));
        ((CreditDebitCardFlowActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        savedCardPager = (ViewPager) view.findViewById(R.id.saved_card_pager);
        addCardBt = (FloatingActionButton)view.findViewById(R.id.btn_add_card);
        addCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
                ((CreditDebitCardFlowActivity) getActivity()).replaceFragment(addCardDetailsFragment,true,false);
            }
        });
        float cardWidth = ((CreditDebitCardFlowActivity) getActivity()).getScreenWidth();
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)cardHeight);
        savedCardPager.setLayoutParams(parms);
        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        createDummyCards();
        CardPagerAdapter cardPagerAdapter = new CardPagerAdapter(getChildFragmentManager(),cardDetails);
        savedCardPager.setAdapter(cardPagerAdapter);
        savedCardPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SdkUtil.hideKeyboard(getActivity());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        circlePageIndicator.setViewPager(savedCardPager);
    }

    ArrayList<CardDetail>cardDetails = new ArrayList<>();
    private void createDummyCards() {
        if(cardDetails.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                CardDetail cardDetail = new CardDetail();
                cardDetail.setCardHolderName("James Anderson");
                cardDetail.setCardNumber("1234 XXXX XXXX 432" + i);
                cardDetail.setBankName("Bank Permata");
                cardDetail.setExpiryDate("XX/12");
                cardDetails.add(cardDetail);
            }
        }
    }
}
