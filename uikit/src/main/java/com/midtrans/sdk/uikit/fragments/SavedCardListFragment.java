package com.midtrans.sdk.uikit.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.PromoResponse;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditCardFlowActivity;
import com.midtrans.sdk.uikit.adapters.SavedCardsAdapter;
import com.midtrans.sdk.uikit.models.PromoData;
import com.midtrans.sdk.uikit.models.SavedCardList;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakawm on 3/7/17.
 * Deprecated, please see {@link com.midtrans.sdk.uikit.views.creditcard.saved.SavedCreditCardActivity}
 */
@Deprecated
public class SavedCardListFragment extends Fragment {
    private static final String PARAM_CARD_LIST = "card.list";

    private SavedCardsAdapter savedCardsAdapter;
    private RecyclerView savedCardsContainer;
    private FancyButton addCardButton;
    private ArrayList<SaveCardRequest> savedCards;
    private boolean fromBackStack;

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

    }

    private void fetchCards() {
        if (!fromBackStack) {
            SavedCardList savedCardList = (SavedCardList) getArguments().getSerializable(PARAM_CARD_LIST);
            if (savedCardList != null) {
                savedCards = (ArrayList<SaveCardRequest>) savedCardList.saveCardRequests;
            }
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
            public void onItemClick(final int position) {
                if (MidtransSDK.getInstance().isEnableBuiltInTokenStorage()
                        && savedCardsAdapter.getItem(position).getType().equalsIgnoreCase(getString(R.string.card_click_type_one_click))) {
                    // Show confirmation dialog
                    String card = SdkUIFlowUtil.getMaskedCardNumber(savedCardsAdapter.getItem(position).getMaskedCard());
                    String amount = getString(R.string.prefix_money, Utils.getFormattedAmount(MidtransSDK.getInstance().getTransactionRequest().getAmount()));
                    String message = getString(R.string.one_click_confirmation_message, card, amount);
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.confirm_payment)
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    SaveCardRequest saveCardRequest = savedCardsAdapter.getItem(position);
                                    ((CreditCardFlowActivity) getActivity()).oneClickPayment(saveCardRequest);
                                }
                            })
                            .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create();

                    dialog.show();
                    changeDialogButtonColor(dialog);
                } else {
                    CardDetailsFragment cardDetailsFragment = CardDetailsFragment.newInstance(savedCardsAdapter.getItem(position));

                    if (savedCardsAdapter.getPromoDatas() != null
                            && !savedCardsAdapter.getPromoDatas().isEmpty()
                            && savedCardsAdapter.getPromoDatas().get(position) != null) {
                        PromoResponse selectedPromo = savedCardsAdapter.getPromoDatas().get(position).getPromoResponse();
                        if (selectedPromo != null && selectedPromo.getDiscountAmount() > 0) {
                            cardDetailsFragment = CardDetailsFragment.newInstance(savedCardsAdapter.getItem(position), selectedPromo);
                        }
                    }

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                            && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_back, R.anim.slide_out_back);
                    }
                    fragmentTransaction.replace(R.id.card_container, cardDetailsFragment)
                            .addToBackStack("")
                            .commit();
                }
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
        initPromoData();
        savedCardsContainer.setAdapter(savedCardsAdapter);
    }

    private void initAddCardButton() {
        addCardButton.setVisibility(View.VISIBLE);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_back, R.anim.slide_out_back);
                }
                fragmentTransaction
                        .replace(R.id.card_container, CardDetailsFragment.newInstance())
                        .addToBackStack("")
                        .commit();
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
        SdkUIFlowUtil.showProgressDialog((AppCompatActivity) getActivity(), getString(R.string.processing_delete), false);
        final MidtransSDK midtransSDK = MidtransSDK.getInstance();
        midtransSDK.deleteCard(midtransSDK.readAuthenticationToken(), saveCardRequest.getMaskedCard(), new DeleteCardCallback() {
            @Override
            public void onSuccess(Void object) {
                SdkUIFlowUtil.hideProgressDialog();
                removeFromCreditCardInstance(saveCardRequest.getMaskedCard());
                removeCardFromInstance(saveCardRequest.getMaskedCard());
                ((CreditCardFlowActivity) getActivity()).setCreditCards(savedCards);
                if (((CreditCardFlowActivity) getActivity()).checkIfCreditCardTokensAvailable()) {
                    savedCardsAdapter.removeCard(saveCardRequest.getMaskedCard());
                } else {
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                            && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_back, R.anim.slide_out_back);
                    }
                    fragmentTransaction.replace(R.id.card_container, CardDetailsFragment.newInstance()).commit();
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
        SdkUIFlowUtil.showProgressDialog((AppCompatActivity) getActivity(), getString(R.string.processing_delete), false);
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        midtransSDK.saveCards(userDetail.getUserId(), saveCardRequests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                SdkUIFlowUtil.hideProgressDialog();
                ((CreditCardFlowActivity) getActivity()).setCreditCards(saveCardRequests);
                savedCards = saveCardRequests;
                fromBackStack = true;
                if (saveCardRequests.isEmpty()) {
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.card_container, CardDetailsFragment.newInstance())
                            .commit();
                } else {
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

    public void updateSavedCardsData(List<SaveCardRequest> saveCardRequests, boolean fromBackStack) {
        this.fromBackStack = fromBackStack;
        this.savedCards = new ArrayList<>(saveCardRequests);
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

    private void removeCardFromInstance(String card) {
        SaveCardRequest cardRequest = searchCardIndex(card);
        if (cardRequest != null) {
            savedCards.remove(savedCards.indexOf(cardRequest));
        }
    }

    private SaveCardRequest searchCardIndex(String card) {
        for (SaveCardRequest saveCardRequest : savedCards) {
            if (saveCardRequest.getMaskedCard().equals(card)) {
                return saveCardRequest;
            }
        }
        return null;
    }

    private void initPromoData() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK.getTransactionRequest().isPromoEnabled()
                && midtransSDK.getPromoResponses() != null
                && !midtransSDK.getPromoResponses().isEmpty()) {
            // Fill promo data
            final ArrayList<PromoData> promoDatas = getPromoData();
            savedCardsAdapter.setPromoDatas(promoDatas);
            savedCardsAdapter.setPromoListener(new SavedCardsAdapter.SavedCardPromoListener() {
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
            savedCardsAdapter.setPromoDatas(initNullPromoData());
            savedCardsAdapter.setPromoListener(null);
        }
    }

    private ArrayList<PromoData> getPromoData() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        ArrayList<PromoData> promoDatas = new ArrayList<>();
        List<PromoResponse> promoResponses = MidtransSDK.getInstance().getPromoResponses();
        ArrayList<SaveCardRequest> saveCardRequests = savedCardsAdapter.getData();
        double grossAmount = midtransSDK.getTransactionRequest().getAmount();
        for (int i = 0; i < savedCardsAdapter.getItemCount(); i++) {
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
        int count = savedCardsAdapter.getItemCount();
        ArrayList<PromoData> promoDatas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            promoDatas.add(null);
        }
        return promoDatas;
    }

    private void changeDialogButtonColor(AlertDialog alertDialog) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (alertDialog.isShowing()
                && midtransSDK != null
                && midtransSDK.getColorTheme() != null
                && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            positiveButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            negativeButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
        }
    }
}
