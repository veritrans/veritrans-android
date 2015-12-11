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
import id.co.veritrans.sdk.activities.OffersActivity;
import id.co.veritrans.sdk.adapters.OffersAdapter;
import id.co.veritrans.sdk.callbacks.AnyOfferClickedListener;
import id.co.veritrans.sdk.callbacks.GetOffersCallback;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.GetOffersResponseModel;
import id.co.veritrans.sdk.models.OffersListModel;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by Ankit on 12/7/15.
 */
public class OffersListFragment extends Fragment implements AnyOfferClickedListener {

    private TextViewFont textViewTitleOffers = null;
    private TextViewFont textViewTitleCardDetails = null;
    private TextViewFont textViewOfferName = null;

    RecyclerView recyclerViewOffers = null;
    private OffersAdapter offersAdapter = null;
    private VeritransSDK veritransSDK = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        veritransSDK = VeritransSDK.getVeritransSDK();
        initialiseView(view);
        setUpOffersList();
        return view;
    }

    private void initialiseView(View view) {
        textViewTitleOffers = (TextViewFont) getActivity().findViewById(R.id.text_title);
        textViewTitleCardDetails = (TextViewFont) getActivity().findViewById(R.id
                .text_title_card_details);
        textViewOfferName = (TextViewFont) getActivity().findViewById(R.id.text_title_offer_name);
        setToolbar();

        recyclerViewOffers = (RecyclerView) view.findViewById(R.id.rv_offers);
    }

    private void setToolbar() {
        textViewTitleOffers.setVisibility(View.VISIBLE);
        textViewTitleCardDetails.setVisibility(View.GONE);
        textViewOfferName.setVisibility(View.GONE);

        textViewTitleOffers.setText(getResources().getString(R.string.offers));
    }


    /**
     * bind views , initializes adapter and set it to recycler view.
     */
    private void setUpOffersList() {

        // setUp recyclerView
        if (getActivity() != null) {
            offersAdapter = new
                    OffersAdapter(getActivity(), ((OffersActivity) getActivity()).offersListModels, this);
            recyclerViewOffers.setHasFixedSize(true);
            recyclerViewOffers.setLayoutManager(
                    new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerViewOffers.setAdapter(offersAdapter);
        }

        //Call OffersApi if the model is empty...
        if (((OffersActivity) getActivity()).offersListModels == null || ((OffersActivity)
                getActivity()).offersListModels.isEmpty
                ()) {
            initialiseAdapterData();
        }
    }

    /**
     * initialize adapter offersListModels model by dummy values.
     */
    private void initialiseAdapterData() {

        if (getActivity() != null) {
            SdkUtil.showProgressDialog(getActivity(), getString(R.string.fetching_offers),
                    false);

            if (veritransSDK != null) {
                veritransSDK.getOffersList(getActivity(), new GetOffersCallback() {
                    @Override
                    public void onSuccess(GetOffersResponseModel getOffersResponseModel) {

                        SdkUtil.hideProgressDialog();

                        Logger.i("offers api successful" + getOffersResponseModel);
                        if (getOffersResponseModel != null && getOffersResponseModel.getOffers()
                                != null) {
                            ((OffersActivity) getActivity()).offersListModels.clear();
                            if (!getOffersResponseModel.getOffers().getBinpromo().isEmpty()) {
                                ((OffersActivity) getActivity()).offersListModels.addAll(getOffersResponseModel.getOffers().getBinpromo());
                            }

                            if (!getOffersResponseModel.getOffers().getInstallments().isEmpty()) {
                                ((OffersActivity) getActivity()).offersListModels.addAll(getOffersResponseModel.getOffers().getInstallments());
                            }

                            if (offersAdapter != null) {
                                offersAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, GetOffersResponseModel getOffersResponseModel) {
                        SdkUtil.hideProgressDialog();
                        Logger.i("offers fetching failed :" + errorMessage);
                    }
                });
            }
        }
    }

    @Override
    public void onOfferClicked(int position, String offerName, String offerType) {
        if (getActivity() != null) {
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//            OffersAddCardDetailsFragment offersAddCardDetailsFragment =
//                    OffersAddCardDetailsFragment.newInstance(offerName);
//            fragmentTransaction.replace(R.id.offers_container,
//                    offersAddCardDetailsFragment, OffersAddCardDetailsFragment.class.getName());
//            fragmentTransaction.addToBackStack(OffersListFragment.class.getName());
//            fragmentTransaction.commit();

            if (((OffersActivity) getActivity()).creditCards.isEmpty()) {
                OffersAddCardDetailsFragment addCardDetailsFragment = OffersAddCardDetailsFragment
                        .newInstance(offerName, offerType);
                ((OffersActivity) getActivity()).replaceFragment(addCardDetailsFragment, true,
                        false);
            } else {
                OffersSavedCardFragment savedCardFragment = OffersSavedCardFragment
                        .newInstance(offerName, offerType);
                ((OffersActivity) getActivity()).replaceFragment(savedCardFragment, true, false);
            }

        }
    }
}
