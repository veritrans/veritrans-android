package com.midtrans.sdk.uikit.fragments;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.snap.PromoResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uikit.adapters.SavedCardsAdapter;
import com.midtrans.sdk.uikit.models.PromoData;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Deprecated, please use {@link com.midtrans.sdk.uikit.views.creditcard.saved.SavedCreditCardActivity}
 */
@Deprecated
public class SavedCardFragment extends Fragment implements SavedCardsAdapter.SavedCardAdapterEventListener {
    private static final String PARAM_CARD_BINS = "param_card_bins";
    private static final String TAG = SavedCardFragment.class.getSimpleName();
    private FancyButton addCardBt;
    private ArrayList<SaveCardRequest> creditCards;
    private int creditCardLayoutHeight;
    private String cardNumber;
    private SavedCardsAdapter cardsAdapter;
    private RecyclerView rvSavedCards;
    private SaveCardRequest selectedCard;
    private PromoResponse selectedPromo;
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
        if (getActivity() != null && ((CreditDebitCardFlowActivity) getActivity()).isRemoveExistCard()) {
            updateSavedCards();
            ((CreditDebitCardFlowActivity) getActivity()).setRemoveExistCard(false);
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
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateSavedCards() {
        ((CreditDebitCardFlowActivity) getActivity()).setRemoveExistCard(false);
        SdkUIFlowUtil.hideProgressDialog();
        if (!creditCards.isEmpty()) {
            setAdapter();
        } else {
            showNewCardFragment(null, null);
        }
    }

    private void bindViews(final View view) {
        addCardBt = (FancyButton) view.findViewById(R.id.btn_add_card);
        rvSavedCards = (RecyclerView) view.findViewById(R.id.rv_saved_cards);
        rvSavedCards.setHasFixedSize(true);
        rvSavedCards.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cardsAdapter = new SavedCardsAdapter();
        rvSavedCards.setAdapter(cardsAdapter);

        addCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewCardFragment(null, null);
                ((CreditDebitCardFlowActivity) getActivity()).setFromSavedCard(true);
            }
        });

        Drawable filteredDrawable = SdkUIFlowUtil.filterDrawableImage(getContext(), R.drawable.ic_plus_new, addCardBt.getmDefaultIconColor());
        addCardBt.setIconResource(filteredDrawable);

        creditCards = ((CreditDebitCardFlowActivity) getActivity()).getCreditCardList();
        setAdapter();

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                // set custom color for add card btn
                addCardBt.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                addCardBt.setIconColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor());
                addCardBt.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }
        }

    }


    private void setAdapter() {
        if (creditCards != null && !creditCards.isEmpty()) {
            if (getActivity() != null) {
                cardsAdapter.setData(creditCards);
                cardsAdapter.setListener(this);
                cardsAdapter.setDeleteCardListener(new SavedCardsAdapter.DeleteCardListener() {
                    @Override
                    public void onItemDelete(final int position) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                .setMessage(R.string.card_delete_message)
                                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int j) {
                                        dialogInterface.dismiss();
                                        selectedCard = cardsAdapter.getItem(position);
                                        if (MidtransSDK.getInstance().isEnableBuiltInTokenStorage()) {
                                            // Handle delete for built in token storage
                                            SdkUIFlowUtil.showProgressDialog((AppCompatActivity) getActivity(), getString(R.string.processing_delete), false);
                                            ((CreditDebitCardFlowActivity) getActivity()).setFromSavedCard(true);
                                            ((CreditDebitCardFlowActivity) getActivity()).deleteCardFromTokenStorage(selectedCard);
                                        } else {
                                            // Handle delete for merchant server implementation
                                            SdkUIFlowUtil.showProgressDialog((AppCompatActivity) getActivity(), getString(R.string.processing_delete), false);
                                            ((CreditDebitCardFlowActivity) getActivity()).setFromSavedCard(true);
                                            ArrayList<SaveCardRequest> cardList = new ArrayList<>();
                                            if (creditCards != null && !creditCards.isEmpty()) {
                                                cardList.addAll(creditCards);
                                                for (int i = 0; i < cardList.size(); i++) {
                                                    if (cardList.get(i).getSavedTokenId().equalsIgnoreCase(selectedCard.getSavedTokenId())) {
                                                        cardList.remove(cardList.get(i));
                                                    }
                                                }
                                            }
                                            ((CreditDebitCardFlowActivity) getActivity()).saveCreditCards(cardList, true);
                                        }
                                    }
                                })
                                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        selectedCard = null;
                                    }
                                })
                                .create();
                        alertDialog.show();
                    }
                });
                initPromoData();
                fadeInAnimation(addCardBt);
            }
        }
    }

    private void initPromoData() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK.getTransactionRequest().isPromoEnabled()
                && midtransSDK.getPromoResponses() != null
                && !midtransSDK.getPromoResponses().isEmpty()) {
            // Fill promo data
            final ArrayList<PromoData> promoDatas = getPromoData();
            cardsAdapter.setPromoDatas(promoDatas);
            cardsAdapter.setPromoListener(new SavedCardsAdapter.SavedCardPromoListener() {
                @Override
                public void onItemPromo(int position) {
                    PromoData promoData = promoDatas.get(position);
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.promo_dialog_title)
                            .setMessage(getString(R.string.promo_dialog_message, Utils.getFormattedAmount(SdkUIFlowUtil.calculateDiscountAmount(promoData.getPromoResponse())), promoData.getPromoResponse().getSponsorName()))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create();
                    alertDialog.show();
                }
            });
        } else {
            // Reset promo
            cardsAdapter.setPromoDatas(initNullPromoData());
            cardsAdapter.setPromoListener(null);
        }
    }

    public void deleteCreditCard(String saveTokenId) {
        SdkUIFlowUtil.showProgressDialog((AppCompatActivity) getActivity(), getString(R.string.processing_delete), false);
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

    public void showNewCardFragment(final SaveCardRequest card, final PromoResponse promoResponse) {
        this.selectedCard = card;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            ((CreditDebitCardFlowActivity) getActivity()).showAddCardDetailFragment(card, promoResponse);
            return;
        }
    }

    public void showNewCardFragment(final SaveCardRequest card) {
        this.selectedCard = card;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            ((CreditDebitCardFlowActivity) getActivity()).showAddCardDetailFragment(card);
            return;
        }
    }


    @Override
    public void onItemClick(int position) {
        SaveCardRequest card = cardsAdapter.getItem(position);
        if (getActivity() != null) {
            if (cardsAdapter.getPromoDatas() != null
                    && !cardsAdapter.getPromoDatas().isEmpty()
                    && cardsAdapter.getPromoDatas().get(position) != null) {
                selectedPromo = cardsAdapter.getPromoDatas().get(position).getPromoResponse();
                if (selectedPromo != null && selectedPromo.getDiscountAmount() > 0) {
                    showNewCardFragment(card, selectedPromo);
                } else {
                    showNewCardFragment(card);
                }
            } else {
                showNewCardFragment(card);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //fromBackStack = this.selectedCard != null;
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

    private ArrayList<PromoData> getPromoData() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        ArrayList<PromoData> promoDatas = new ArrayList<>();
        List<PromoResponse> promoResponses = MidtransSDK.getInstance().getPromoResponses();
        ArrayList<SaveCardRequest> saveCardRequests = cardsAdapter.getData();
        double grossAmount = midtransSDK.getTransactionRequest().getAmount();
        for (int i = 0; i < cardsAdapter.getItemCount(); i++) {
            SaveCardRequest cardRequest = saveCardRequests.get(i);
            String cardBins = cardRequest.getMaskedCard().substring(0, 6);
            PromoResponse promoResponse = SdkUIFlowUtil.getPromoFromCardBins(promoResponses, cardBins);
            if (promoResponse != null) {
                promoDatas.add(new PromoData(promoResponse, grossAmount));
            } else {
                promoDatas.add(null);
            }
        }
        return promoDatas;
    }

    private ArrayList<PromoData> initNullPromoData() {
        int count = cardsAdapter.getItemCount();
        ArrayList<PromoData> promoDatas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            promoDatas.add(null);
        }
        return promoDatas;
    }
}