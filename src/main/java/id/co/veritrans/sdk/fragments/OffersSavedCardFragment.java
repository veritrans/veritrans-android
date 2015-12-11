package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.activities.OffersActivity;
import id.co.veritrans.sdk.adapters.CardPagerAdapter;
import id.co.veritrans.sdk.callbacks.DeleteCardCallback;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.DeleteCardResponse;
import id.co.veritrans.sdk.widgets.CirclePageIndicator;
import id.co.veritrans.sdk.widgets.TextViewFont;

public class OffersSavedCardFragment extends Fragment {

    private TextViewFont textViewTitleOffers = null;
    private TextViewFont textViewTitleCardDetails = null;
    private TextViewFont textViewOfferName = null;
    private String offerName = null;

    private ViewPager savedCardPager;
    private CirclePageIndicator circlePageIndicator;
    private FloatingActionButton addCardBt;
    private VeritransSDK veritransSDK;
    private ArrayList<CardTokenRequest> creditCards;
    private CardPagerAdapter cardPagerAdapter;

    private TextViewFont emptyCardsTextViewFont;


    public OffersSavedCardFragment() {

    }

    public static OffersSavedCardFragment newInstance(String offerName) {
        OffersSavedCardFragment fragment = new OffersSavedCardFragment();
        Bundle data = new Bundle();
        data.putString(OffersActivity.OFFER_NAME, offerName);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veritransSDK = VeritransSDK.getVeritransSDK();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers_saved_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle data = getArguments();
        offerName = data.getString(OffersActivity.OFFER_NAME);
        bindViews(view);
        super.onViewCreated(view, savedInstanceState);
    }


    private void setToolbar(){
        textViewTitleOffers.setVisibility(View.GONE);
        textViewTitleCardDetails.setVisibility(View.VISIBLE);
        textViewOfferName.setVisibility(View.VISIBLE);
        textViewOfferName.setText(offerName);

        textViewTitleCardDetails.setText(getResources().getString(R.string.saved_card));
    }

    private void bindViews(View view) {

        textViewTitleOffers = (TextViewFont) getActivity().findViewById(R.id.text_title);
        textViewTitleCardDetails = (TextViewFont) getActivity().findViewById(R.id
                .text_title_card_details);
        textViewOfferName = (TextViewFont) getActivity().findViewById(R.id.text_title_offer_name);

        setToolbar();

        emptyCardsTextViewFont = (TextViewFont) view.findViewById(R.id.text_empty_saved_cards);
        savedCardPager = (ViewPager) view.findViewById(R.id.saved_card_pager);
        addCardBt = (FloatingActionButton) view.findViewById(R.id.btn_add_card);
        addCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OffersAddCardDetailsFragment offersAddCardDetailsFragment = OffersAddCardDetailsFragment
                        .newInstance(offerName);
                ((OffersActivity) getActivity()).replaceFragment
                        (offersAddCardDetailsFragment, true, false);
            }
        });
        float cardWidth = ((OffersActivity) getActivity()).getScreenWidth();
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, (int) cardHeight);
        savedCardPager.setLayoutParams(parms);
        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        creditCards = ((OffersActivity) getActivity()).getCreditCardList();

        setViewPagerValues();
    }


        private void setViewPagerValues(){
        if (creditCards != null) {
             cardPagerAdapter = new CardPagerAdapter(this,getChildFragmentManager(),
                    creditCards);
            savedCardPager.setAdapter(cardPagerAdapter);
            savedCardPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int
                        positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    /*SdkUtil.hideKeyboard(getActivity());*/
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            circlePageIndicator.setViewPager(savedCardPager);
            //to notify adapter when credit card details received
            ((OffersActivity)getActivity()).setAdapterViews(cardPagerAdapter,circlePageIndicator, emptyCardsTextViewFont);
            showHideNoCardMessage();
        }
    }

    private void showHideNoCardMessage() {
        if (creditCards.isEmpty()) {
            emptyCardsTextViewFont.setVisibility(View.VISIBLE);
            //savedCardPager.setVisibility(View.GONE);
        } else {
            emptyCardsTextViewFont.setVisibility(View.GONE);
            //savedCardPager.setVisibility(View.VISIBLE);
        }
    }

    public void deleteCreditCard(String cardNumber) {
            showHideNoCardMessage();
        deleteCards(cardNumber);

    }

    public void deleteCards(final String cardNumber) {
        CardTokenRequest creditCard = null;
        Logger.i("cardNumber:" + cardNumber);
        if (creditCards != null && !creditCards.isEmpty()) {

            for (int i = 0; i < creditCards.size(); i++) {
                if (creditCards.get(i).getCardNumber().equalsIgnoreCase(cardNumber)) {
                    creditCard = creditCards.get(i);
                }
            }
            try {
                Logger.i("position to delete:" + creditCard.getCardNumber() + ",creditCard size:" + creditCards.size());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        if (creditCard != null) {
            veritransSDK.deleteCard(getActivity(), creditCard, new DeleteCardCallback() {
                @Override
                public void onFailure(String errorMessage) {
                    SdkUtil.showSnackbar(getActivity(), errorMessage);
                }

                @Override
                public void onSuccess(DeleteCardResponse deleteResponse) {
                    if (deleteResponse == null || !deleteResponse.getMessage().equalsIgnoreCase(getString(R.string.success))) {
                        return;
                    }
                    int position = -1;
                    for (int i = 0; i < creditCards.size(); i++) {
                        if (creditCards.get(i).getCardNumber().equalsIgnoreCase(cardNumber)) {
                            position = i;
                        }
                    }
                    if (creditCards != null && !creditCards.isEmpty()) {
                        Logger.i("position to delete:" + position + "," + creditCards.size());
                        if(!creditCards.isEmpty()) {
                            for (int i = 0; i < creditCards.size(); i++) {
                                Logger.i("cards before:" + creditCards.get(i).getCardNumber());
                            }
                        }

                        creditCards.remove(position);


                        if(!creditCards.isEmpty()) {
                            for (int i = 0; i < creditCards.size(); i++) {

                                Logger.i("cards after:" + creditCards.get(i).getCardNumber());
                            }
                        }

                        //notifydataset change not worked properly for viewpager so setting it again
                        Logger.i("setting view pager value");
                        // setViewPagerValues(creditCardsNew);
                        /*if(creditCards.size()>1) {
                            try {
                                savedCardPager.setCurrentItem(position);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                savedCardPager.setCurrentItem(creditCards.size() - 1);
                            }
                        }*/
                        if (cardPagerAdapter != null && circlePageIndicator != null) {
                            Logger.i("notifying data");
                            cardPagerAdapter.notifyChangeInPosition(1);
                            cardPagerAdapter.notifyDataSetChanged();
                            circlePageIndicator.notifyDataSetChanged();
                            if(creditCards.isEmpty()){
                                emptyCardsTextViewFont.setVisibility(View.VISIBLE);
                            } else {
                                emptyCardsTextViewFont.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
        }
    }
}