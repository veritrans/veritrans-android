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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BaseActivity;
import com.midtrans.sdk.uikit.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uikit.adapters.SavedCardsAdapter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;

public class SavedCardFragment extends Fragment implements SavedCardsAdapter.SavedCardAdapterEventListener {
    private static final String PARAM_CARD_BINS = "param_card_bins";
    private static final String TAG = SavedCardFragment.class.getSimpleName();
    int installmentCurrentPosition, installmentTotalPositions;
    private FancyButton addCardBt;
    private ArrayList<SaveCardRequest> creditCards;
    //private MorphingButton btnMorph;
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
        Log.d("onresume", "fromnewcard:" + fromBackStack);
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
            }
        });

        Drawable filteredDrawable = SdkUIFlowUtil.filterDrawableImage(getContext(), R.drawable.ic_plus_new,
                addCardBt.getmDefaultIconColor());
        addCardBt.setIconResource(filteredDrawable);

        creditCards = ((CreditDebitCardFlowActivity) getActivity()).getCreditCardList();
        setViewPagerValues();

    }


    private void setViewPagerValues() {
        if (creditCards != null) {
            if (getActivity() != null) {
                cardsAdapter.setData(creditCards);
                fadeInAnimation(addCardBt);
//                cardPagerAdapter = new CardPagerAdapter(this, getChildFragmentManager(),
//                        creditCards, getActivity());
//                savedCardPager.setAdapter(cardPagerAdapter);
//                savedCardPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                    @Override
//                    public void onPageScrolled(int position, float positionOffset, int
//                            positionOffsetPixels) {
//                    }
//
//                    @Override
//                    public void onPageSelected(int position) {
//                        setupCardInstallment();
//                        setupPromoValidation();
//                    }
//
//                    @Override
//                    public void onPageScrollStateChanged(int state) {
//
//                    }
//                });
//                circlePageIndicator.setViewPager(savedCardPager);
//                ((CreditDebitCardFlowActivity) getActivity()).setAdapterViews(cardPagerAdapter, circlePageIndicator, emptyCardsTextView);
//                showHideNoCardMessage();
//                setupCardInstallment();
//                setupPromoValidation();
            }

        }
    }
//
//    private void setupPromoValidation() {
//        if (((CreditDebitCardFlowActivity) getActivity()).isPaymentWithPromo()) {
//            if (checkPromoValidity()) {
//                ((CreditDebitCardFlowActivity) getActivity()).setPromoValidationStatus(true);
//            } else {
//                ((CreditDebitCardFlowActivity) getActivity()).setPromoValidationStatus(false);
//            }
//        }
//    }


//    private boolean checkPromoValidity() {
//        boolean valid = true;
//        SaveCardRequest currentCard = cardPagerAdapter.getCurrentItem(savedCardPager.getCurrentItem());
//        if (!TextUtils.isEmpty(currentCard.getMaskedCard())) {
//            String cardBin = currentCard.getMaskedCard().substring(0, 6);
//
//            if (((CreditDebitCardFlowActivity) getActivity()).isCardBinValid(cardBin)) {
//                showInvalidPromoStatus(false);
//            } else {
//                showInvalidPromoStatus(true);
//                valid = false;
//            }
//        } else {
//            showInvalidPromoStatus(true);
//            valid = false;
//        }
//
//        return valid;
//    }

//    private void showInvalidPromoStatus(boolean show) {
//        if (show) {
//            textInvalidPromoStatus.setVisibility(View.VISIBLE);
//        } else {
//            textInvalidPromoStatus.setVisibility(View.GONE);
//        }
//    }

//    private void setupCardInstallment() {
//        MidtransSDK midtransSDK = MidtransSDK.getInstance();
//        if (midtransSDK.getCreditCard() != null
//                && midtransSDK.getCreditCard().getBank() != null
//                && (midtransSDK.getCreditCard().getBank().equalsIgnoreCase(BankType.MAYBANK)
//                || midtransSDK.getCreditCard().getBank().equalsIgnoreCase(BankType.BRI))) {
//            showInstallmentLayout(false);
//        } else {
//            SaveCardRequest currentCard = cardPagerAdapter.getCurrentItem(savedCardPager.getCurrentItem());
//            ArrayList<Integer> installmentTerms = ((CreditDebitCardFlowActivity) getActivity()).getInstallmentTerms(currentCard.getMaskedCard().substring(0, 6));
//            if (installmentTerms != null && installmentTerms.size() > 1) {
//                installmentCurrentPosition = 0;
//                installmentTotalPositions = installmentTerms.size() - 1;
//                setInstallmentTerm();
//                showInstallmentLayout(true);
//                setPaymentInstallment();
//            } else {
//                showInstallmentLayout(false);
//            }
//        }
//    }

//    private void setInstallmentTerm() {
//        String installmentTerm;
//        int term = ((CreditDebitCardFlowActivity) getActivity()).getInstallmentTerm(installmentCurrentPosition);
//        if (term < 1) {
//            installmentTerm = getString(R.string.no_installment);
//        } else {
//            installmentTerm = getString(R.string.formatted_installment_month, term + "");
//        }
//        textInstallmentTerm.setText(installmentTerm);
//    }
//
//    private void showInstallmentLayout(boolean show) {
//        if (show) {
//            if (layoutInstallment.getVisibility() == View.GONE) {
//                layoutInstallment.setVisibility(View.VISIBLE);
//            }
//            buttonDecrease.setEnabled(false);
//            buttonIncrease.setEnabled(true);
//        } else {
//            if (layoutInstallment.getVisibility() == View.VISIBLE) {
//                layoutInstallment.setVisibility(View.GONE);
//            }
//            ((CreditDebitCardFlowActivity) getActivity()).setInstallment(0);
//        }
//    }

//    private void showHideNoCardMessage() {
//        if (creditCards.isEmpty()) {
//            emptyCardsTextView.setVisibility(View.VISIBLE);
//        } else {
//            emptyCardsTextView.setVisibility(View.GONE);
//        }
//    }

    public void deleteCreditCard(String saveTokenId) {
        SdkUIFlowUtil.showProgressDialog(getActivity(), getString(R.string.processing_delete), false);
//        showHideNoCardMessage();
        deleteCards(saveTokenId);

    }

    public void deleteCreditCard() {
        SdkUIFlowUtil.showProgressDialog(getActivity(), getString(R.string.processing_delete), false);
//        showHideNoCardMessage();
//        deleteCards(saveTokenId);

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
        Animation animation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.fade_in);

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