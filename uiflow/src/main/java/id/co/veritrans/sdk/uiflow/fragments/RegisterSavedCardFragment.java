package id.co.veritrans.sdk.uiflow.fragments;

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

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.DeleteCardBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.DeleteCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.DeleteCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.activities.SaveCreditCardActivity;
import id.co.veritrans.sdk.uiflow.adapters.RegisterCardPagerAdapter;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;
import id.co.veritrans.sdk.uiflow.widgets.CirclePageIndicator;

/**
 * @author rakawm
 */
public class RegisterSavedCardFragment extends Fragment implements DeleteCardBusCallback {
    private ViewPager savedCardPager;
    private CirclePageIndicator circlePageIndicator;
    private FloatingActionButton addCardBt;
    private VeritransSDK veritransSDK;
    private ArrayList<SaveCardRequest> creditCards;
    private RegisterCardPagerAdapter cardPagerAdapter;

    private LinearLayout emptyContainer;
    //private MorphingButton btnMorph;
    private LinearLayout creditCardLayout;
    private RelativeLayout newCardButtonLayout;
    private int creditCardLayoutHeight;
    private String cardNumber;

    public static RegisterSavedCardFragment newInstance() {
        return new RegisterSavedCardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veritransSDK = VeritransSDK.getVeritransSDK();
        if (!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
    }

    @Override
    public void onDestroy() {
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty_card, container, false);
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
        emptyContainer = (LinearLayout) view.findViewById(R.id.container_no_card);
        savedCardPager = (ViewPager) view.findViewById(R.id.saved_card_pager);
        creditCardLayout = (LinearLayout) view.findViewById(R.id.credit_card_holder);
        newCardButtonLayout = (RelativeLayout) view.findViewById(R.id.new_card_button_layout);

        addCardBt = (FloatingActionButton) view.findViewById(R.id.btn_add_card);
        addCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnMorph.setVisibility(View.VISIBLE);
                hideLayouts();

            }
        });

        float cardWidth = ((SaveCreditCardActivity) getActivity()).getScreenWidth();
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, (int) cardHeight);
        savedCardPager.setLayoutParams(parms);
        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        circlePageIndicator.setFillColor(veritransSDK.getThemeColor());
        creditCards = ((SaveCreditCardActivity) getActivity()).getCreditCardList();
        setViewPagerValues();

    }


    private void setViewPagerValues() {
        if (creditCards != null) {
            if (getActivity() != null) {
                cardPagerAdapter = new RegisterCardPagerAdapter(this, getChildFragmentManager(), creditCards, getActivity());
                savedCardPager.setAdapter(cardPagerAdapter);
                savedCardPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                circlePageIndicator.setViewPager(savedCardPager);
                ((SaveCreditCardActivity) getActivity()).setAdapterViews(cardPagerAdapter, circlePageIndicator, emptyContainer);
                showHideNoCardMessage();
            }
        }
    }

    private void showHideNoCardMessage() {
        if (creditCards.isEmpty()) {
            emptyContainer.setVisibility(View.VISIBLE);
            //savedCardPager.setVisibility(View.GONE);
        } else {
            emptyContainer.setVisibility(View.GONE);
            //savedCardPager.setVisibility(View.VISIBLE);
        }
    }

    public void deleteCreditCard(String cardNumber) {
        SdkUIFlowUtil.showProgressDialog(getActivity(), getString(R.string.processing_delete), false);
        showHideNoCardMessage();
        deleteCards(cardNumber);
    }

    public void addCreditCard(SaveCardRequest request) {
        creditCards.add(request);
        cardPagerAdapter.notifyDataSetChanged();
    }

    public void deleteCards(final String tokenId) {
        SaveCardRequest creditCard = null;
        this.cardNumber = tokenId;
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
            veritransSDK.deleteCard(saveCardRequest);
        }
    }

    public void hideLayouts() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            RegisterCardFragment addCardDetailsFragment = RegisterCardFragment
                    .newInstance();
            ((SaveCreditCardActivity) getActivity()).replaceFragment
                    (addCardDetailsFragment, true, false);
            return;
        }
        ((SaveCreditCardActivity) (getActivity())).morphingAnimation();
        creditCardLayoutHeight = RegisterSavedCardFragment.this.creditCardLayout.getMeasuredHeight();
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
                addCardBt.setVisibility(View.GONE);
                RegisterCardFragment addCardDetailsFragment = RegisterCardFragment
                        .newInstance();
                ((SaveCreditCardActivity) getActivity()).replaceFragment
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
        ((SaveCreditCardActivity) getActivity()).getBtnMorph().setVisibility(View.VISIBLE);
        ((SaveCreditCardActivity) getActivity()).morphToCircle((int) Constants.CARD_ANIMATION_TIME);
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
                ((SaveCreditCardActivity) getActivity()).getBtnMorph().setVisibility(View.GONE);
                ((SaveCreditCardActivity) getActivity()).getTitleHeaderTextView().setText(R.string.saved_card);
            }
        }, Constants.CARD_ANIMATION_TIME);
    }

    @Subscribe
    @Override
    public void onEvent(DeleteCardSuccessEvent event) {
        SdkUIFlowUtil.hideProgressDialog();
        int position = -1;
        for (int i = 0; i < creditCards.size(); i++) {
            if (creditCards.get(i).getSavedTokenId().equalsIgnoreCase(cardNumber)) {
                position = i;
            }
        }
        if (creditCards != null && !creditCards.isEmpty()) {
            Logger.i("position to delete:" + position + "," + creditCards.size());
            if (!creditCards.isEmpty()) {
                for (int i = 0; i < creditCards.size(); i++) {
                    Logger.i("cards before:" + creditCards.get(i).getSavedTokenId());
                }
            }

            creditCards.remove(position);

            if (!creditCards.isEmpty()) {
                for (int i = 0; i < creditCards.size(); i++) {

                    Logger.i("cards after:" + creditCards.get(i).getSavedTokenId());
                }
            }

            //notifydataset change not worked properly for viewpager so setting it again
            Logger.i("setting view pager value");

            if (cardPagerAdapter != null && circlePageIndicator != null) {
                Logger.i("notifying data");
                cardPagerAdapter.notifyChangeInPosition(1);
                cardPagerAdapter.notifyDataSetChanged();
                circlePageIndicator.notifyDataSetChanged();
                if (creditCards.isEmpty()) {
                    emptyContainer.setVisibility(View.VISIBLE);
                } else {
                    emptyContainer.setVisibility(View.GONE);
                }
            }
        }
    }

    @Subscribe
    @Override
    public void onEvent(DeleteCardFailedEvent event) {
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showSnackbar(getActivity(), event.getMessage());
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string.no_network_msg));
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showSnackbar(getActivity(), event.getMessage());
    }
}
