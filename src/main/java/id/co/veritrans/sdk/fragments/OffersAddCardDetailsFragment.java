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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.OffersActivity;
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

    private TextViewFont textViewOfferApplied = null;
    private TextViewFont textViewOfferNotApplied = null;
    private LinearLayout layoutOfferStatus = null;
    private Button buttonPayNow = null;

    private String offerName = null;
    private ImageView imageViewPlus = null;
    private ImageView imageViewMinus = null;
    private TextViewFont textViewInstalment = null;
    private RelativeLayout layoutPayWithInstalment = null;



    public static OffersAddCardDetailsFragment newInstance(String offerName) {
        OffersAddCardDetailsFragment fragment = new OffersAddCardDetailsFragment();
        Bundle data = new Bundle();
        data.putString(OffersActivity.OFFER_NAME, offerName);
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
        offerName = data.getString(OffersActivity.OFFER_NAME);
        initialiseView(view);
    }

    private void initialiseView(View view) {

        if (view != null && getActivity() != null){
            textViewTitleOffers = (TextViewFont) getActivity().findViewById(R.id.text_title);
            textViewTitleCardDetails = (TextViewFont) getActivity().findViewById(R.id
                    .text_title_card_details);
            textViewOfferName = (TextViewFont) getActivity().findViewById(R.id.text_title_offer_name);

            setToolbar();

            textViewOfferApplied = (TextViewFont) view.findViewById(R.id.text_offer_status_applied);
            textViewOfferNotApplied = (TextViewFont) view.findViewById(R.id
                    .text_offer_status_not_applied);
            layoutOfferStatus = (LinearLayout) view.findViewById(R.id.layout_offer_status);
            buttonPayNow = (Button) view.findViewById(R.id.btn_pay_now);

            imageViewPlus = (ImageView) view.findViewById(R.id.img_plus);
            imageViewMinus = (ImageView) view.findViewById(R.id.img_minus);
            textViewInstalment = (TextViewFont) view.findViewById(R.id.text_instalment);
            layoutPayWithInstalment = (RelativeLayout) view.findViewById(R.id
                    .layout_pay_with_instalments);

            hideOrShowPayWithInstalment(false);
            hideOrShowOfferStatus(false, false);
        }
    }

    private void setToolbar(){
        textViewTitleOffers.setVisibility(View.GONE);
        textViewTitleCardDetails.setVisibility(View.VISIBLE);
        textViewOfferName.setVisibility(View.VISIBLE);
        textViewOfferName.setText(offerName);

        textViewTitleCardDetails.setText(getResources().getString(R.string.card_details));
    }

    private void hideOrShowPayWithInstalment(boolean isShowLayout){
        if (isShowLayout){
            layoutPayWithInstalment.setVisibility(View.VISIBLE);
        } else {
            layoutPayWithInstalment.setVisibility(View.GONE);
        }
    }

    private void hideOrShowOfferStatus(boolean isShowLayout, boolean isOfferApplied){
        if (isShowLayout){
            layoutOfferStatus.setVisibility(View.VISIBLE);
            if (isOfferApplied){
                textViewOfferApplied.setVisibility(View.VISIBLE);
                textViewOfferNotApplied.setVisibility(View.GONE);
            } else {
                textViewOfferApplied.setVisibility(View.GONE);
                textViewOfferNotApplied.setVisibility(View.VISIBLE);
            }
        } else {
            layoutOfferStatus.setVisibility(View.GONE);
        }
    }
}
