package com.midtrans.sdk.uikit.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uikit.adapters.SavedCardsAdapter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;

public class SavedCardFragment extends Fragment implements SavedCardsAdapter.SavedCardAdapterEventListener {
    private static final String PARAM_CARD_BINS = "param_card_bins";
    private static final String TAG = SavedCardFragment.class.getSimpleName();
    private FancyButton addCardBt;
    private ArrayList<SaveCardRequest> creditCards;
    private LinearLayout creditCardLayout;
    private RelativeLayout newCardButtonLayout;
    private int creditCardLayoutHeight;
    private String cardNumber;
    private SavedCardsAdapter cardsAdapter;
    private RecyclerView rvSavedCards;
    private SaveCardRequest selectedCard;
    private boolean fromBackStack;

    public SavedCardFragment() {

    }

    public static SavedCardFragment newInstance() {
        SavedCardFragment fragment = new SavedCardFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fromBackStack) {
            if (getActivity() != null && ((CreditDebitCardFlowActivity) getActivity()).isRemoveExistCard()) {
                updateSavedCards();
                ((CreditDebitCardFlowActivity) getActivity()).setRemoveExistCard(false);
            }
            fromBackStack = false;
        }
        return inflater.inflate(R.layout.fragment_saved_card, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromBackStack = false;
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

    private void updateSavedCards() {
        SdkUIFlowUtil.hideProgressDialog();
        int position = -1;

        if (creditCards != null && selectedCard != null) {
            for (int i = 0; i < creditCards.size(); i++) {
                if (creditCards.get(i).getSavedTokenId().equalsIgnoreCase(selectedCard.getSavedTokenId())) {
                    position = i;
                }
            }

            if (!creditCards.isEmpty()) {
                for (int i = 0; i < creditCards.size(); i++) {
                    Logger.i(TAG, "before:" + creditCards.get(i).getSavedTokenId());
                }

                creditCards.remove(position);

                if (!creditCards.isEmpty()) {
                    for (int i = 0; i < creditCards.size(); i++) {
                        Logger.i(TAG, "after:" + creditCards.get(i).getSavedTokenId());
                    }
                    if (cardsAdapter != null) {
                        cardsAdapter.notifyDataSetChanged();
                    }
                } else {
                    showNewCardFragment(null);
                }
            }
        }
    }

    private void bindViews(final View view) {
        creditCardLayout = (LinearLayout) view.findViewById(R.id.credit_card_holder);
        newCardButtonLayout = (RelativeLayout) view.findViewById(R.id.new_card_button_layout);
        addCardBt = (FancyButton) view.findViewById(R.id.btn_add_card);
        rvSavedCards = (RecyclerView) view.findViewById(R.id.rv_saved_cards);
        rvSavedCards.setHasFixedSize(true);
        rvSavedCards.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cardsAdapter = new SavedCardsAdapter(this);
        rvSavedCards.setAdapter(cardsAdapter);

        addCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewCardFragment(null);
                ((CreditDebitCardFlowActivity) getActivity()).setFromSavedCard(true);
            }
        });

        Drawable filteredDrawable = SdkUIFlowUtil.filterDrawableImage(getContext(), R.drawable.ic_plus_new,
                addCardBt.getmDefaultIconColor());
        addCardBt.setIconResource(filteredDrawable);

        creditCards = ((CreditDebitCardFlowActivity) getActivity()).getCreditCardList();
        setViewPagerValues();

    }


    private void setViewPagerValues() {
        if (creditCards != null && !creditCards.isEmpty()) {
            if (getActivity() != null) {
                cardsAdapter.setData(creditCards);
                fadeInAnimation(addCardBt);
            }
        }
    }

    public void deleteCreditCard(String saveTokenId) {
        SdkUIFlowUtil.showProgressDialog(getActivity(), getString(R.string.processing_delete), false);
        deleteCards(saveTokenId);

    }


    public void deleteCards(final String tokenId) {
        ArrayList<SaveCardRequest> cardList = new ArrayList<>();
        this.cardNumber = tokenId;
        if (creditCards != null && !creditCards.isEmpty()) {
            cardList.addAll(creditCards);
            for (int i = 0; i < cardList.size(); i++) {
                if (cardList.get(i).getSavedTokenId().equalsIgnoreCase(tokenId)) {
                    cardList.remove(cardList.get(i));
                }
            }
        }
        ((CreditDebitCardFlowActivity) getActivity()).saveCreditCards(cardList, true);
    }

    public void showNewCardFragment(final SaveCardRequest card) {
        this.selectedCard = card;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            ((CreditDebitCardFlowActivity) getActivity()).showAddCardDetailFragment(card);
            return;
        }

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
                ((CreditDebitCardFlowActivity) getActivity()).showAddCardDetailFragment(card);

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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return;
        }
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
                ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.choose_card);
            }
        }, Constants.CARD_ANIMATION_TIME);
    }


    @Override
    public void onItemClick(int position) {
        SaveCardRequest card = cardsAdapter.getItem(position);
        if (getActivity() != null) {
            showNewCardFragment(card);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.selectedCard != null) {
            fromBackStack = true;
        } else {
            fromBackStack = false;
        }
    }

    private void fadeInAnimation(final View view) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAnimation(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }
}