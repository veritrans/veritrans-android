package com.midtrans.sdk.uikit.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditCardFlowActivity;
import com.midtrans.sdk.uikit.adapters.SavedCardsAdapter;
import com.midtrans.sdk.uikit.models.SavedCardList;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakawm on 3/7/17.
 */

public class SavedCardListFragment extends Fragment {
    private static final String PARAM_CARD_LIST = "card.list";

    private SavedCardsAdapter savedCardsAdapter;
    private RecyclerView savedCardsContainer;
    private FancyButton addCardButton;
    private ArrayList<SaveCardRequest> savedCards;

    public static SavedCardListFragment newInstance(List<SaveCardRequest> saveCardRequests) {
        SavedCardListFragment fragment = new SavedCardListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_CARD_LIST, new SavedCardList(saveCardRequests));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchCards();
        bindViews(view);
        initColorThemes();
        initRecyclerView();
        initAddCardButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.saved_card);
        hideDeleteIcon();
    }

    private void fetchCards() {
        SavedCardList savedCardList = (SavedCardList) getArguments().getSerializable(PARAM_CARD_LIST);
        if (savedCardList != null) {
            savedCards = (ArrayList<SaveCardRequest>) savedCardList.saveCardRequests;
        }
    }

    private void bindViews(View view) {
        addCardButton = (FancyButton) view.findViewById(R.id.btn_add_card);
        savedCardsContainer = (RecyclerView) view.findViewById(R.id.rv_saved_cards);
    }

    private void initRecyclerView() {
        savedCardsContainer.setHasFixedSize(true);
        savedCardsContainer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        savedCardsAdapter = new SavedCardsAdapter();
        savedCardsAdapter.setData(savedCards);
        savedCardsAdapter.setListener(new SavedCardsAdapter.SavedCardAdapterEventListener() {
            @Override
            public void onItemClick(int position) {
                // TODO: Go to card details
                CardDetailsFragment cardDetailsFragment = CardDetailsFragment.newInstance(savedCardsAdapter.getItem(position));
                getFragmentManager().beginTransaction().replace(R.id.card_container, cardDetailsFragment).addToBackStack("").commit();
            }
        });

        savedCardsAdapter.setDeleteCardListener(new SavedCardsAdapter.DeleteCardListener() {
            @Override
            public void onItemDelete(final int position) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setMessage(R.string.card_delete_message)
                        .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                dialogInterface.dismiss();
                                SaveCardRequest selectedCard = savedCardsAdapter.getItem(position);
                                if (MidtransSDK.getInstance().isEnableBuiltInTokenStorage()) {
                                    // Handle delete for built in token storage
                                    deleteCardFromTokenStorage(selectedCard);
                                } else {
                                    ArrayList<SaveCardRequest> cardList = new ArrayList<>();
                                    if (savedCards != null && !savedCards.isEmpty()) {
                                        cardList.addAll(savedCards);
                                        for (int i = 0; i < cardList.size(); i++) {
                                            if (cardList.get(i).getSavedTokenId().equalsIgnoreCase(selectedCard.getSavedTokenId())) {
                                                cardList.remove(cardList.get(i));
                                            }
                                        }
                                    }
                                    // Handle delete for merchant server implementation
                                    deleteCardFromMerchantServer(selectedCard.getMaskedCard(), cardList);
                                }
                            }
                        })
                        .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        savedCardsContainer.setAdapter(savedCardsAdapter);
    }

    private void initAddCardButton() {
        addCardButton.setVisibility(View.VISIBLE);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                getFragmentManager().beginTransaction().replace(R.id.card_container, CardDetailsFragment.newInstance()).addToBackStack("").commit();
            }
        });
    }

    private void initColorThemes() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                // set custom color for add card btn
                addCardButton.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                addCardButton.setIconColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor());
                addCardButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }
        }
    }

    private void deleteCardFromTokenStorage(final SaveCardRequest saveCardRequest) {
        SdkUIFlowUtil.showProgressDialog(getActivity(), getString(R.string.processing_delete), false);
        final MidtransSDK midtransSDK = MidtransSDK.getInstance();
        midtransSDK.deleteCard(midtransSDK.readAuthenticationToken(), saveCardRequest.getMaskedCard(), new DeleteCardCallback() {
            @Override
            public void onSuccess(Void object) {
                SdkUIFlowUtil.hideProgressDialog();
                removeFromCreditCardInstance(saveCardRequest.getMaskedCard());
                if (checkIfCreditCardTokensAvailable()) {
                    savedCardsAdapter.removeCard(saveCardRequest.getMaskedCard());
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.card_container, CardDetailsFragment.newInstance()).commit();
                }
            }

            @Override
            public void onFailure(Void object) {
                SdkUIFlowUtil.hideProgressDialog();
            }

            @Override
            public void onError(Throwable throwable) {
                SdkUIFlowUtil.hideProgressDialog();
            }
        });
    }

    private void deleteCardFromMerchantServer(final String maskedCard, final ArrayList<SaveCardRequest> saveCardRequests) {
        SdkUIFlowUtil.showProgressDialog(getActivity(), getString(R.string.processing_delete), false);
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        midtransSDK.saveCards(userDetail.getUserId(), saveCardRequests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                SdkUIFlowUtil.hideProgressDialog();
                ((CreditCardFlowActivity) getActivity()).setCreditCards(saveCardRequests);
                if (saveCardRequests.isEmpty()) {
                    savedCards = saveCardRequests;
                    getFragmentManager().beginTransaction().replace(R.id.card_container, CardDetailsFragment.newInstance()).commit();
                } else {
                    savedCards = saveCardRequests;
                    savedCardsAdapter.removeCard(maskedCard);
                }
            }

            @Override
            public void onFailure(String reason) {
                SdkUIFlowUtil.hideProgressDialog();
            }

            @Override
            public void onError(Throwable error) {
                SdkUIFlowUtil.hideProgressDialog();
            }
        });
    }

    private void removeFromCreditCardInstance(String maskedCard) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        CreditCard creditCard = midtransSDK.getCreditCard();
        List<SavedToken> savedTokens = creditCard.getSavedTokens();
        SavedToken savedToken = searchSavedToken(savedTokens, maskedCard);
        if (savedToken != null) {
            savedTokens.remove(savedTokens.indexOf(savedToken));
            creditCard.setSavedTokens(savedTokens);
            midtransSDK.setCreditCard(creditCard);
        }
    }

    private SavedToken searchSavedToken(List<SavedToken> savedTokens, String maskedCard) {
        for (SavedToken savedToken : savedTokens) {
            if (savedToken.getMaskedCard().equals(maskedCard)) {
                return savedToken;
            }
        }
        return null;
    }

    public void updateSavedCardsData(ArrayList<SaveCardRequest> saveCardRequests) {
        this.savedCards = saveCardRequests;
        savedCardsAdapter.setData(savedCards);
    }

    private boolean checkIfCreditCardTokensAvailable() {
        List<SavedToken> savedTokens = MidtransSDK.getInstance().getCreditCard().getSavedTokens();
        String type = MidtransSDK.getInstance().getTransactionRequest().getCardClickType();
        for (SavedToken savedToken : savedTokens) {
            if (savedToken.getTokenType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    private void hideDeleteIcon() {
        getActivity().findViewById(R.id.image_saved_card_delete).setVisibility(View.GONE);
    }
}
