package com.midtrans.sdk.uikit.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.OffersListModel;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.OffersActivity;
import com.midtrans.sdk.uikit.adapters.CardPagerAdapter;
import com.midtrans.sdk.uikit.widgets.CirclePageIndicator;

import java.util.ArrayList;

public class OffersSavedCardFragment extends Fragment {

    private final String MONTH = "Month";
    int currentPosition, totalPositions;
    private TextView textViewTitleOffers = null;
    private TextView textViewTitleCardDetails = null;
    private TextView textViewOfferName = null;
    private int offerPosition = 0;
    private String offerName = null;
    private String offerType = null;
    private ImageView imageViewPlus = null;
    private ImageView imageViewMinus = null;
    private TextView textViewInstalment = null;
    private LinearLayout layoutPayWithInstalment = null;
    private ViewPager savedCardPager;
    private CirclePageIndicator circlePageIndicator;
    private FloatingActionButton addCardBt;
    private MidtransSDK midtransSDK;
    private ArrayList<SaveCardRequest> creditCards;
    private CardPagerAdapter cardPagerAdapter;
    private TextView emptyCardsTextView;
    private boolean isInstalment = false;
    private LinearLayout creditCardLayout;
    private RelativeLayout newCardButtonLayout;
    private int creditCardLayoutHeight;
    private String cardNumber;

    public OffersSavedCardFragment() {

    }

    public static OffersSavedCardFragment newInstance(int position, String offerName, String
            offerType) {
        OffersSavedCardFragment fragment = new OffersSavedCardFragment();
        Bundle data = new Bundle();
        data.putInt(OffersActivity.OFFER_POSITION, position);
        data.putString(OffersActivity.OFFER_NAME, offerName);
        data.putString(OffersActivity.OFFER_TYPE, offerType);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        midtransSDK = MidtransSDK.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers_saved_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle data = getArguments();
        offerPosition = data.getInt(OffersActivity.OFFER_POSITION);
        offerName = data.getString(OffersActivity.OFFER_NAME);
        offerType = data.getString(OffersActivity.OFFER_TYPE);
        bindViews(view);

        super.onViewCreated(view, savedInstanceState);
    }

    private void setToolbar() {
        textViewTitleOffers.setVisibility(View.GONE);
        textViewTitleCardDetails.setVisibility(View.VISIBLE);
        textViewOfferName.setVisibility(View.VISIBLE);
        textViewOfferName.setText(offerName);

        textViewTitleCardDetails.setText(getResources().getString(R.string.saved_card));
    }

    private void hideOrShowPayWithInstalment(boolean isShowLayout) {
        if (isShowLayout) {
            layoutPayWithInstalment.setVisibility(View.VISIBLE);
            isInstalment = true;
            showDuration();
        } else {
            layoutPayWithInstalment.setVisibility(View.GONE);
            isInstalment = false;
        }
    }

    private void showDuration() {

        ArrayList<OffersListModel> offersList = new ArrayList<>();

        if (((OffersActivity) getActivity()).offersListModels != null || !((OffersActivity) getActivity()).offersListModels.isEmpty()) {
            offersList.addAll(((OffersActivity) getActivity()).offersListModels);
            if (offersList.get(offerPosition).getDuration() != null || !offersList.get(offerPosition).getDuration().isEmpty()) {
                currentPosition = 0;
                totalPositions = offersList.get(offerPosition).getDuration().size() - 1;
                textViewInstalment.setText(offersList.get(offerPosition).getDuration().get(0)
                        .toString() + " " + MONTH);
                disableEnableMinusPlus();
            }
        }
    }

    private void disableEnableMinusPlus() {

        Logger.i("Positions: " + currentPosition + "----" + totalPositions);

        if (currentPosition == 0 && totalPositions == 0) {
            imageViewMinus.setEnabled(false);
            imageViewPlus.setEnabled(false);
        } else if (currentPosition > 0 && currentPosition < totalPositions) {
            imageViewMinus.setEnabled(true);
            imageViewPlus.setEnabled(true);
        } else if (currentPosition > 0 && currentPosition == totalPositions) {
            imageViewMinus.setEnabled(true);
            imageViewPlus.setEnabled(false);
        } else if (currentPosition == 0 && currentPosition < totalPositions) {
            imageViewMinus.setEnabled(false);
            imageViewPlus.setEnabled(true);
        }
    }

    private void onMinusClicked() {
        if (currentPosition > 0 && currentPosition <= totalPositions) {
            currentPosition = currentPosition - 1;
            textViewInstalment.setText(((OffersActivity) getActivity()).offersListModels.get
                    (offerPosition).getDuration().get(currentPosition)
                    .toString() + " " + MONTH);
        }
        disableEnableMinusPlus();
    }

    private void onPlusClicked() {
        if (currentPosition >= 0 && currentPosition < totalPositions) {
            currentPosition = currentPosition + 1;
            textViewInstalment.setText(((OffersActivity) getActivity()).offersListModels.get
                    (offerPosition).getDuration().get(currentPosition)
                    .toString() + " " + MONTH);
        }
        disableEnableMinusPlus();
    }

    private void bindViews(View view) {
        creditCardLayout = (LinearLayout) view.findViewById(R.id.credit_card_holder);
        newCardButtonLayout = (RelativeLayout) view.findViewById(R.id.new_card_button_layout);
        layoutPayWithInstalment = (LinearLayout) view.findViewById(R.id.layout_pay_with_instalments);
       /* ViewTreeObserver vto = creditCardLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                OffersSavedCardFragment.this.creditCardLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //width = SavedCardFragment.this.addCardBt.getMeasuredWidth();


            }
        });*/

        textViewTitleOffers = (TextView) getActivity().findViewById(R.id.text_title);
        textViewTitleCardDetails = (TextView) getActivity().findViewById(R.id
                .text_title_card_details);
        textViewOfferName = (TextView) getActivity().findViewById(R.id.text_title_offer_name);

        setToolbar();

        imageViewPlus = (ImageView) view.findViewById(R.id.img_plus);
        imageViewMinus = (ImageView) view.findViewById(R.id.img_minus);
        textViewInstalment = (TextView) view.findViewById(R.id.text_instalment);


        if (offerType.equalsIgnoreCase(OffersActivity.OFFER_TYPE_INSTALMENTS)) {
            hideOrShowPayWithInstalment(true);
        } else {
            hideOrShowPayWithInstalment(false);
        }

        imageViewMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMinusClicked();
            }
        });

        imageViewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlusClicked();
            }
        });

        emptyCardsTextView = (TextView) view.findViewById(R.id.text_empty_saved_cards);
        savedCardPager = (ViewPager) view.findViewById(R.id.saved_card_pager);
        addCardBt = (FloatingActionButton) view.findViewById(R.id.btn_add_card);
        addCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideLayouts();
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


    private void setViewPagerValues() {
        if (creditCards != null) {
            if (getActivity() != null) {
                cardPagerAdapter = new CardPagerAdapter(this, getChildFragmentManager(),
                        creditCards, getActivity());
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
                ((OffersActivity) getActivity()).setAdapterViews(cardPagerAdapter, circlePageIndicator, emptyCardsTextView);
                showHideNoCardMessage();
            }
        }
    }

    private void showHideNoCardMessage() {
        if (creditCards.isEmpty()) {
            emptyCardsTextView.setVisibility(View.VISIBLE);
            //savedCardPager.setVisibility(View.GONE);
        } else {
            emptyCardsTextView.setVisibility(View.GONE);
            //savedCardPager.setVisibility(View.VISIBLE);
        }
    }

    public void deleteCreditCard(String tokenId) {
        showHideNoCardMessage();
        deleteCards(tokenId);

    }

    public int gettingInstalmentTerm() {
        int instalmentTerm = 0;
        if (isInstalment) {
            String duration = textViewInstalment.getText().toString().trim();
            String durationSplit[] = duration.split(" ");
            duration = durationSplit[0];

            try {
                instalmentTerm = Integer.parseInt(duration);
            } catch (NumberFormatException ne) {
                ne.printStackTrace();
            }
        }
        return instalmentTerm;
    }

    public boolean isInstalment() {
        boolean isInstalmentValue = false;
        isInstalmentValue = isInstalment;
        return isInstalmentValue;
    }

    public void deleteCards(final String tokenId) {
        this.cardNumber = tokenId;
        SaveCardRequest creditCard = null;
        Logger.i("cardNumber:" + cardNumber);
        if (creditCards != null && !creditCards.isEmpty()) {

            for (int i = 0; i < creditCards.size(); i++) {
                if (creditCards.get(i).getSavedTokenId().equalsIgnoreCase(tokenId)) {
                    creditCard = creditCards.get(i);
                }
            }
        }
        if (creditCard != null) {
            Logger.i("position to delete:" + creditCard.getSavedTokenId() + ",creditCard size:" + creditCards.size());
            SaveCardRequest saveCardRequest = new SaveCardRequest();
            saveCardRequest.setSavedTokenId(creditCard.getSavedTokenId());
        }
    }

    public void hideLayouts() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            OffersAddCardDetailsFragment addCardDetailsFragment = OffersAddCardDetailsFragment.newInstance(offerPosition, offerName, offerType);
            ((OffersActivity) getActivity()).replaceFragment(addCardDetailsFragment, R.id.offers_container, true, false);
            return;
        }
        ((OffersActivity) (getActivity())).morphingAnimation();
        creditCardLayoutHeight = OffersSavedCardFragment.this.creditCardLayout.getMeasuredHeight();
        Logger.i("creditCardLayoutHeight:" + creditCardLayoutHeight);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(newCardButtonLayout, "alpha", 1f, 0f);
        fadeOut.setDuration(Constants.CARD_ANIMATION_TIME);
        ObjectAnimator fadeOutOffer = ObjectAnimator.ofFloat(layoutPayWithInstalment, "alpha", 1f, 0f);
        fadeOut.setDuration(Constants.CARD_ANIMATION_TIME);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(creditCardLayout, "translationY",
                -creditCardLayoutHeight);
        translateY.setDuration(Constants.CARD_ANIMATION_TIME);
        translateY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                addCardBt.setVisibility(View.GONE);
                OffersAddCardDetailsFragment offersAddCardDetailsFragment = OffersAddCardDetailsFragment
                        .newInstance(offerPosition, offerName, offerType);
                ((OffersActivity) getActivity()).replaceFragment
                        (offersAddCardDetailsFragment, R.id.offers_container, true, false);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        translateY.start();
        fadeOut.start();
        fadeOutOffer.start();

    }

    @Override
    public void onResume() {
        super.onResume();
        showLayouts();
    }

    public void showLayouts() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return;
        }
        ((OffersActivity) getActivity()).getBtnMorph().setVisibility(View.VISIBLE);
        ((OffersActivity) getActivity()).morphToCircle((int) Constants.CARD_ANIMATION_TIME);
        //creditCardLayoutHeight = SavedCardFragment.this.creditCardLayout.getMeasuredHeight();
        Logger.i("creditCardLayoutHeight:" + creditCardLayoutHeight);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(newCardButtonLayout, "alpha", 0f, 1f);
        fadeIn.setDuration(Constants.CARD_ANIMATION_TIME);
        ObjectAnimator fadeInOffer = ObjectAnimator.ofFloat(layoutPayWithInstalment, "alpha", 0f, 1f);
        fadeIn.setDuration(Constants.CARD_ANIMATION_TIME);
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(creditCardLayout, "translationY",
                -creditCardLayoutHeight);
        slideOut.setDuration(0);
        slideOut.start();
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(creditCardLayout, "translationY",
                0);
        slideIn.setDuration(Constants.CARD_ANIMATION_TIME);
        slideIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //payNowBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        slideIn.start();
        fadeIn.start();
        fadeInOffer.start();
        addCardBt.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((OffersActivity) getActivity()).getBtnMorph().setVisibility(View.GONE);
            }
        }, Constants.CARD_ANIMATION_TIME);
    }
}