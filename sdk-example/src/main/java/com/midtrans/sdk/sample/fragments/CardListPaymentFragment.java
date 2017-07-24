package com.midtrans.sdk.sample.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.sample.CreditCardPaymentActivity;
import com.midtrans.sdk.sample.R;
import com.midtrans.sdk.sample.adapters.CardListAdapter;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 10/18/16.
 */

public class CardListPaymentFragment extends Fragment implements CardListAdapter.CardListListener {
    public static final String TAG = "CardListPaymentFragment";
    private static final String ARGS_SAVED_CARDS = "saved_cards";
    private RecyclerView recyclerCards;
    private Button btnNew;
    private CardListAdapter adapter;
    private Dialog dialog;
    private TextView labelEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        recyclerCards = (RecyclerView) view.findViewById(R.id.list_cards);
        btnNew = (Button) view.findViewById(R.id.btn_add_newcard);
        labelEmpty = (TextView) view.findViewById(R.id.label_empty);
        recyclerCards.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CardListAdapter(this);
        recyclerCards.setAdapter(adapter);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CreditCardPaymentActivity) getActivity()).onNewCard();
            }
        });

        showSavedCards();
        return view;
    }

    private void showSavedCards() {
        Log.i(TAG, "savecard>showSavedCards");

        ArrayList<SaveCardRequest> savedCards = ((CreditCardPaymentActivity) getActivity()).getSavedCards();
        if (savedCards.isEmpty()) {
            labelEmpty.setVisibility(View.VISIBLE);
        } else {
            labelEmpty.setVisibility(View.GONE);
            Log.i(TAG, "savecard>size:" + savedCards.size());
            ArrayList<SaveCardRequest> filteredCards = filterCardsByType(savedCards);
            adapter.setData(filteredCards);
        }
    }

    private ArrayList<SaveCardRequest> filterCardsByType(ArrayList<SaveCardRequest> savedCards) {
        ArrayList<SaveCardRequest> filteredCards = new ArrayList<>();
        if (MidtransSDK.getInstance().isEnableBuiltInTokenStorage()) {
            for (SaveCardRequest card : savedCards) {
                if (MidtransSDK.getInstance().getTransactionRequest().getCardClickType().equals(getString(com.midtrans.sdk.uikit.R.string.card_click_type_one_click))
                        && card.getType().equals(getString(com.midtrans.sdk.uikit.R.string.saved_card_one_click))) {
                    filteredCards.add(card);
                } else if (MidtransSDK.getInstance().getTransactionRequest().getCardClickType().equals(getString(com.midtrans.sdk.uikit.R.string.card_click_type_two_click))
                        && card.getType().equals(getString(com.midtrans.sdk.uikit.R.string.saved_card_two_click))) {
                    filteredCards.add(card);
                }
            }
        } else {
            //if token storage on merchantserver then saved cards can be used just for two click
            if (MidtransSDK.getInstance().getTransactionRequest().getCardClickType().equals(R.string.card_click_type_two_click)) {
                filteredCards.addAll(savedCards);
            }
        }
        return filteredCards;
    }


    public static CardListPaymentFragment newInstance() {
        CardListPaymentFragment fragment = new CardListPaymentFragment();
        return fragment;
    }

    @Override
    public void onclick(int position) {
        showDialogPayment(adapter.getItem(position));
    }

    private void showDialogPayment(final SaveCardRequest card) {
        dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout_one_two_click);
        final EditText editCvv = (EditText) dialog.findViewById(R.id.edit_cvv);
        Button btnPay = (Button) dialog.findViewById(R.id.btn_pay_now);
        TextView textClickType = (TextView) dialog.findViewById(R.id.text_clicktype);


        if (MidtransSDK.getInstance().getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_one_click))) {
            editCvv.setVisibility(View.GONE);
            textClickType.setText(R.string.card_click_one_click);
        } else {
            editCvv.setVisibility(View.VISIBLE);
            textClickType.setText(R.string.card_click_two_click);
        }


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MidtransSDK.getInstance().getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_one_click))) {
                    ((CreditCardPaymentActivity) getActivity()).paymentOneclick(card.getMaskedCard(), false);
                } else {
                    String cvv = editCvv.getText().toString().trim();
                    ((CreditCardPaymentActivity) getActivity()).paymentTwoClick(cvv, card.getSavedTokenId(), false);
                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }
}
