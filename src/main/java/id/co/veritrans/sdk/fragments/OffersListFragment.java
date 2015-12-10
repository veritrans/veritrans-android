package id.co.veritrans.sdk.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.adapters.OffersAdapter;
import id.co.veritrans.sdk.callbacks.AnyOfferClickedListener;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.OffersListModel;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by Ankit on 12/7/15.
 */
public class OffersListFragment extends Fragment implements AnyOfferClickedListener{

    private TextViewFont textViewTitleOffers = null;
    private TextViewFont textViewTitleCardDetails = null;
    private TextViewFont textViewOfferName = null;

    RecyclerView recyclerViewOffers = null;
    private ArrayList<OffersListModel> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        initialiseView(view);
        setUpOffersList();
        return view;
    }

    private void initialiseView(View view){
        textViewTitleOffers = (TextViewFont) getActivity().findViewById(R.id.text_title);
        textViewTitleCardDetails = (TextViewFont) getActivity().findViewById(R.id
                .text_title_card_details);
        textViewOfferName = (TextViewFont) getActivity().findViewById(R.id.text_title_offer_name);
        setToolbar();

        recyclerViewOffers = (RecyclerView) view.findViewById(R.id.rv_offers);
    }

    private void setToolbar(){
        textViewTitleOffers.setVisibility(View.VISIBLE);
        textViewTitleCardDetails.setVisibility(View.GONE);
        textViewOfferName.setVisibility(View.GONE);
    }


    /**
     * bind views , initializes adapter and set it to recycler view.
     */
    private void setUpOffersList() {

        // setUp recyclerView
        initialiseAdapterData();

        if (getActivity() != null){
            OffersAdapter offersAdapter = new
                    OffersAdapter(getActivity(), data, this);
            recyclerViewOffers.setHasFixedSize(true);
            recyclerViewOffers.setLayoutManager(
                    new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerViewOffers.setAdapter(offersAdapter);
        }
    }

    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseAdapterData() {
        data.clear();
        data.add(new OffersListModel("Bank BSA- Buy 1 Get 1 Offer on credit cards", false));
    }

    @Override
    public void onOfferClicked(int position, String offerName) {

        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            OffersAddCardDetailsFragment offersAddCardDetailsFragment =
                    OffersAddCardDetailsFragment.newInstance(offerName);
            fragmentTransaction.replace(R.id.offers_container,
                    offersAddCardDetailsFragment, OffersAddCardDetailsFragment.class.getName());
            fragmentTransaction.addToBackStack(OffersListFragment.class.getName());
            fragmentTransaction.commit();
        }
    }
}
