package id.co.veritrans.sdk.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.adapters.OffersAdapter;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.models.OffersListModel;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by Ankit on 12/8/15.
 */
public class OffersAddCardDetailsFragment extends Fragment {
    private TextViewFont textViewTitleOffers = null;
    private TextViewFont textViewTitleCardDetails = null;
    private TextViewFont textViewOfferName = null;

    private TextViewFont textViewOfferStatus = null;
    private Button buttonPayNow = null;
    private static final String OFFER_NAME = "offer_name";
    private String offerName = null;


    public static OffersAddCardDetailsFragment newInstance(String offerName) {
        OffersAddCardDetailsFragment fragment = new OffersAddCardDetailsFragment();
        Bundle data = new Bundle();
        data.putString(OFFER_NAME, offerName);
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers_add_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //retrieve data from bundle.
        Bundle data = getArguments();
        offerName = data.getString(OFFER_NAME);
        initialiseView(view);
    }

    private void initialiseView(View view) {

        if (view != null && getActivity() != null){
            textViewTitleOffers = (TextViewFont) getActivity().findViewById(R.id.text_title);
            textViewTitleCardDetails = (TextViewFont) getActivity().findViewById(R.id
                    .text_title_card_details);
            textViewOfferName = (TextViewFont) getActivity().findViewById(R.id.text_title_offer_name);
            setToolbar();

            textViewOfferStatus = (TextViewFont) view.findViewById(R.id.text_offer_status);
            buttonPayNow = (Button) view.findViewById(R.id.btn_pay_now);
        }
    }

    private void setToolbar(){
        textViewTitleOffers.setVisibility(View.GONE);
        textViewTitleCardDetails.setVisibility(View.VISIBLE);
        textViewOfferName.setVisibility(View.VISIBLE);
        textViewOfferName.setText(offerName);
    }
}
