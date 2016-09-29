package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.OffersActivity;
import com.midtrans.sdk.uikit.adapters.OffersAdapter;
import com.midtrans.sdk.uikit.callbacks.AnyOfferClickedListener;
import com.midtrans.sdk.uikit.utilities.RecyclerItemClickListener;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

/**
 * Created by ziahaqi on 14/06/2016.
 */
public class OffersListFragment extends Fragment implements AnyOfferClickedListener {

    private TextView textViewTitleOffers = null;
    private TextView textViewTitleCardDetails = null;
    private TextView textViewOfferName = null;
    private RecyclerView recyclerViewOffers = null;
    private OffersAdapter offersAdapter = null;
    private MidtransSDK midtransSDK = null;
    private RelativeLayout emptyOffersLayout;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        midtransSDK = MidtransSDK.getInstance();
        initialiseView(view);
        setUpOffersList();
        return view;
    }

    private void initialiseView(View view) {
        textViewTitleOffers = (TextView) getActivity().findViewById(R.id.text_title);
        textViewTitleCardDetails = (TextView) getActivity().findViewById(R.id
                .text_title_card_details);
        textViewOfferName = (TextView) getActivity().findViewById(R.id.text_title_offer_name);
        setToolbar();
        recyclerViewOffers = (RecyclerView) view.findViewById(R.id.rv_offers);
        emptyOffersLayout = (RelativeLayout) view.findViewById(R.id.empty_offers_layout);
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
            offersAdapter = new OffersAdapter(((OffersActivity) getActivity()).offersListModels);
            recyclerViewOffers.setHasFixedSize(true);
            recyclerViewOffers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerViewOffers.setAdapter(offersAdapter);
            recyclerViewOffers.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String offerType = null;
                            if (offersAdapter.getItemAtPosition(position).getDuration() != null && !offersAdapter.getItemAtPosition(position).getDuration().isEmpty()) {
                                offerType = OffersActivity.OFFER_TYPE_INSTALMENTS;
                            } else {
                                offerType = OffersActivity.OFFER_TYPE_BINPROMO;
                            }

                            onOfferClicked(position, offersAdapter.getItemAtPosition(position).getOfferName(), offerType);
                        }
                    }, 300);
                }
            }));
        }

        //Call OffersApi if the model is empty...
        OffersActivity offersActivity = (OffersActivity) getActivity();
        if (offersActivity != null && (offersActivity.offersListModels == null || offersActivity.offersListModels.isEmpty())) {
            initialiseAdapterData();
        }
    }

    /**
     * initialize adapter offersListModels model by dummy values.
     */
    private void initialiseAdapterData() {

        if (getActivity() != null) {
            SdkUIFlowUtil.showProgressDialog(getActivity(), getString(R.string.fetching_offers),
                    false);
            showHideEmptyOffersMessage(false);
            if (midtransSDK != null) {

                midtransSDK.getOffersList();
            }
        }
    }

    private void showHideEmptyOffersMessage(boolean showLayout) {
        if (showLayout) {
            emptyOffersLayout.setVisibility(View.VISIBLE);

        } else {
            emptyOffersLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOfferClicked(int position, String offerName, String offerType) {
        if (getActivity() != null) {

            try {
                ((OffersActivity) getActivity()).setSelectedOffer(((OffersActivity) getActivity())
                        .offersListModels.get(position));
            } catch (ArrayIndexOutOfBoundsException array) {
                array.printStackTrace();
            }

            if (((OffersActivity) getActivity()).creditCards.isEmpty()) {
                OffersAddCardDetailsFragment addCardDetailsFragment = OffersAddCardDetailsFragment
                        .newInstance(position, offerName, offerType);
                ((OffersActivity) getActivity()).replaceFragment(addCardDetailsFragment, R.id.offers_container, true,
                        false);
            } else {
                OffersSavedCardFragment savedCardFragment = OffersSavedCardFragment
                        .newInstance(position, offerName, offerType);
                ((OffersActivity) getActivity()).replaceFragment(savedCardFragment, R.id.offers_container, true, false);
            }

        }
    }
}
