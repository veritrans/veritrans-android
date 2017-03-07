package com.midtrans.sdk.ui.views.creditcard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseFragment;
import com.midtrans.sdk.ui.adapters.SavedCardsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.models.CreditCardDetails;
import com.midtrans.sdk.ui.models.SavedCreditCard;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by ziahaqi on 2/26/17.
 */

public class SavedCreditCardsFragment extends BaseFragment implements CreditCardContract.SavedCreditCardsView,
        SavedCardsAdapter.SavedCardAdapterEventListener {
    private static final String ARGS_SAVED_CARD = "saved_card";
    private CreditCardContract.Presenter presenter;
    private SavedCreditCard savedCreditCards;

    private FancyButton buttonNewCard;
    private LinearLayout layoutCreditCards;
    private RelativeLayout layoutNewCardButtonn;
    private int creditCardLayoutHeight;
    private String cardNumber;
    private SavedCardsAdapter cardsAdapter;
    private RecyclerView rvSavedCards;

    public static SavedCreditCardsFragment newInstance(SavedCreditCard savedCreditCard) {
        SavedCreditCardsFragment fragment = new SavedCreditCardsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_SAVED_CARD, savedCreditCard);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initProperties();
        return inflater.inflate(R.layout.fragment_saved_cards, container, false);
    }

    private void initProperties() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.savedCreditCards = (SavedCreditCard) bundle.getSerializable(ARGS_SAVED_CARD);
        }
    }

    @Override
    public void setPresenter(@NonNull CreditCardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutCreditCards = (LinearLayout) view.findViewById(R.id.credit_card_holder);
        layoutNewCardButtonn = (RelativeLayout) view.findViewById(R.id.new_card_button_layout);
        buttonNewCard = (FancyButton) view.findViewById(R.id.btn_add_card);
        rvSavedCards = (RecyclerView) view.findViewById(R.id.rv_saved_cards);
        rvSavedCards.setHasFixedSize(true);
        rvSavedCards.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cardsAdapter = new SavedCardsAdapter();
        rvSavedCards.setAdapter(cardsAdapter);
        setupViews();
        bindData();
    }

    private void bindData() {
        Drawable filteredDrawable = UiUtils.filterDrawableImage(getContext(), R.mipmap.ic_plus_new,
                buttonNewCard.getmDefaultIconColor());
        buttonNewCard.setIconResource(filteredDrawable);

        setBorderColor(buttonNewCard);
        buttonNewCard.setIconColorFilter(getPrimaryDarkColor());
        buttonNewCard.setTextColor(getPrimaryDarkColor());
        setViewPagerValues();
    }

    private void setViewPagerValues() {
        if (savedCreditCards.haveSavedCards()) {
            if (getActivity() != null) {
                cardsAdapter.setData(savedCreditCards.getSavedCards());
                cardsAdapter.setListener(this);
                fadeInAnimation(buttonNewCard);
            }
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

    private void setupViews() {
        buttonNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCardDetails(null);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showLayouts();
    }

    private void showCardDetails(final SavedToken savedCard) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            ((CreditCardActivity) getActivity()).showCreditCardDetailFragment(new CreditCardDetails(savedCard, null));
            return;
        }

        creditCardLayoutHeight = this.layoutCreditCards.getMeasuredHeight();
        Logger.d(TAG, "cardLayoutHeight:" + creditCardLayoutHeight);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(layoutNewCardButtonn, "alpha", 1f, 0f);
        fadeOut.setDuration(Constants.Animation.CARD_ANIMATION_TIME);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(layoutCreditCards, "translationY",
                -creditCardLayoutHeight);
        translateY.setDuration(Constants.Animation.CARD_ANIMATION_TIME);
        translateY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                buttonNewCard.setVisibility(View.GONE);
                ((CreditCardActivity) getActivity()).showCreditCardDetailFragment(new CreditCardDetails(savedCard, null));
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
        Logger.d(TAG, "creditLayoutHeight:" + creditCardLayoutHeight);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(layoutNewCardButtonn, "alpha", 0f, 1f);
        fadeIn.setDuration(Constants.Animation.CARD_ANIMATION_TIME);
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(layoutCreditCards, "translationY",
                -creditCardLayoutHeight);
        slideOut.setDuration(0);
        slideOut.start();
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(layoutCreditCards, "translationY",
                0);
        slideIn.setDuration(Constants.Animation.CARD_ANIMATION_TIME);
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
        buttonNewCard.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((CreditCardActivity) getActivity()).setHeaderTitle(getString(R.string.choose_card));
            }
        }, Constants.Animation.CARD_ANIMATION_TIME);
    }

    @Override
    public void onItemClick(int position) {
        SavedToken savedToken = cardsAdapter.getItem(position);
        showCardDetails(savedToken);
    }
}
