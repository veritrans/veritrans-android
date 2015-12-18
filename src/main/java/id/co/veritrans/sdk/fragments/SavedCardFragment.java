package id.co.veritrans.sdk.fragments;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
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

public class SavedCardFragment extends Fragment{
    private ViewPager savedCardPager;
    private CirclePageIndicator circlePageIndicator;
    private FloatingActionButton addCardBt;
    private VeritransSDK veritransSDK;
    private ArrayList<CardTokenRequest> creditCards;
    private CardPagerAdapter cardPagerAdapter;

    private TextViewFont emptyCardsTextViewFont;
    //private MorphingButton btnMorph;
    private LinearLayout creditCardLayout;
    private RelativeLayout newCardButtonLayout;
    private int creditCardLayoutHeight;

    public SavedCardFragment() {

    }

    public static SavedCardFragment newInstance() {
        SavedCardFragment fragment = new SavedCardFragment();
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
        return inflater.inflate(R.layout.fragment_saved_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bindViews(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        showLayouts();


    }

    private void bindViews(final View view) {
        emptyCardsTextViewFont = (TextViewFont) view.findViewById(R.id.text_empty_saved_cards);
        savedCardPager = (ViewPager) view.findViewById(R.id.saved_card_pager);
        creditCardLayout = (LinearLayout)view.findViewById(R.id.credit_card_holder);
        newCardButtonLayout = (RelativeLayout)view.findViewById(R.id.new_card_button_layout);
       /* ViewTreeObserver vto = creditCardLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SavedCardFragment.this.creditCardLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //width = SavedCardFragment.this.addCardBt.getMeasuredWidth();


            }
        });*/

        addCardBt = (FloatingActionButton) view.findViewById(R.id.btn_add_card);
        addCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnMorph.setVisibility(View.VISIBLE);
                hideLayouts();

            }
        });

        /*ViewTreeObserver vto = addCardBt.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SavedCardFragment.this.addCardBt.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //width = SavedCardFragment.this.addCardBt.getMeasuredWidth();
                ((CreditDebitCardFlowActivity)getActivity()).setFabHeight(SavedCardFragment.this.addCardBt.getMeasuredHeight());

            }
        });*/
        float cardWidth = ((CreditDebitCardFlowActivity) getActivity()).getScreenWidth();
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, (int) cardHeight);
        savedCardPager.setLayoutParams(parms);
        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        creditCards = ((CreditDebitCardFlowActivity) getActivity()).getCreditCardList();
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
                ((CreditDebitCardFlowActivity) getActivity()).setAdapterViews(cardPagerAdapter, circlePageIndicator, emptyCardsTextViewFont);
                showHideNoCardMessage();
            }
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
                        if (!creditCards.isEmpty()) {
                            for (int i = 0; i < creditCards.size(); i++) {
                                Logger.i("cards before:" + creditCards.get(i).getCardNumber());
                            }
                        }

                        creditCards.remove(position);

                        if (!creditCards.isEmpty()) {
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
                            if (creditCards.isEmpty()) {
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

    public void hideLayouts() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment
                    .newInstance();
            ((CreditDebitCardFlowActivity) getActivity()).replaceFragment
                    (addCardDetailsFragment, true, false);
            return;
        }
        ((CreditDebitCardFlowActivity)(getActivity())).morphingAnimation();
        creditCardLayoutHeight = SavedCardFragment.this.creditCardLayout.getMeasuredHeight();
        Logger.i("creditCardLayoutHeight:" + creditCardLayoutHeight);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(newCardButtonLayout, "alpha", 1f, 0f);
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
                //payNowBtn.setVisibility(View.VISIBLE);
                addCardBt.setVisibility(View.GONE);
                AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment
                        .newInstance();
                ((CreditDebitCardFlowActivity) getActivity()).replaceFragment
                        (addCardDetailsFragment, true, false);

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

    }
    public void showLayouts() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return;
        }
        ((CreditDebitCardFlowActivity) getActivity()).getBtnMorph().setVisibility(View.VISIBLE);
        ((CreditDebitCardFlowActivity) getActivity()).morphToCircle((int) Constants.CARD_ANIMATION_TIME);
        //creditCardLayoutHeight = SavedCardFragment.this.creditCardLayout.getMeasuredHeight();
        Logger.i("creditCardLayoutHeight:" + creditCardLayoutHeight);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(newCardButtonLayout, "alpha", 0f, 1f);
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
        addCardBt.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((CreditDebitCardFlowActivity) getActivity()).getBtnMorph().setVisibility(View.GONE);
            }
        }, Constants.CARD_ANIMATION_TIME);
    }
}