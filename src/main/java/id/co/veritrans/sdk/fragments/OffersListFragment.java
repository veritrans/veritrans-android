package id.co.veritrans.sdk.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.adapters.OffersAdapter;
import id.co.veritrans.sdk.adapters.PaymentMethodsAdapter;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.OffersListModel;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.utilities.Utils;
import id.co.veritrans.sdk.widgets.HeaderView;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by Ankit on 12/7/15.
 */
public class OffersListFragment extends Fragment{

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
        recyclerViewOffers = (RecyclerView) view.findViewById(R.id.rv_offers);
    }


    /**
     * bind views , initializes adapter and set it to recycler view.
     */
    private void setUpOffersList() {

        // setUp recyclerView
        initialiseAdapterData();

        if (getActivity() != null){
            Logger.i("Coming in Setup: "+data.size());
            OffersAdapter offersAdapter = new
                    OffersAdapter(getActivity(), data);
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
        data.add(new OffersListModel("Bank BSA- Buy 1 Get 1 Offer on credit cards", "21 December 2015", false));
        data.add(new OffersListModel("Buy 2 get 1 free", "22 December 2015", false));
        data.add(new OffersListModel("Buy 3 get 1 free", "23 December 2015", false));
        data.add(new OffersListModel("Buy 4 get 1 free", "24 December 2015", false));
        data.add(new OffersListModel("Buy 5 get 1 free", "25 December 2015", false));
        data.add(new OffersListModel("Buy 6 get 1 free", "26 December 2015", false));
        data.add(new OffersListModel("Buy 7 get 1 free", "27 December 2015", false));
        data.add(new OffersListModel("Buy 8 get 1 free", "28 December 2015", false));
        data.add(new OffersListModel("Buy 9 get 1 free", "29 December 2015", false));
        data.add(new OffersListModel("Buy 10 get 1 free", "30 December 2015", false));
        data.add(new OffersListModel("Buy 11 get 1 free", "31 December 2015", false));
        data.add(new OffersListModel("Buy 12 get 1 free", "32 December 2015", false));
        data.add(new OffersListModel("Buy 13 get 1 free", "33 December 2015", false));
        data.add(new OffersListModel("Buy 14 get 1 free", "34 December 2015", false));
    }

}
