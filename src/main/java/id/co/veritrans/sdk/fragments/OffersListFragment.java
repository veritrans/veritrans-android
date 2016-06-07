package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.OffersActivity;
import id.co.veritrans.sdk.adapters.OffersAdapter;
import id.co.veritrans.sdk.callbacks.AnyOfferClickedListener;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.eventbus.callback.GetOfferBusCallback;
import id.co.veritrans.sdk.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.eventbus.events.GetOfferFailedEvent;
import id.co.veritrans.sdk.eventbus.events.GetOfferSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.models.GetOffersResponseModel;
import id.co.veritrans.sdk.utilities.RecyclerItemClickListener;

/**
 * Created by Ankit on 12/7/15.
 */
public class OffersListFragment extends Fragment implements AnyOfferClickedListener, GetOfferBusCallback {

    private TextView textViewTitleOffers = null;
    private TextView textViewTitleCardDetails = null;
    private TextView textViewOfferName = null;
    private RecyclerView recyclerViewOffers = null;
    private OffersAdapter offersAdapter = null;
    private VeritransSDK veritransSDK = null;
    private RelativeLayout emptyOffersLayout;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        if (!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
    }

    @Override
    public void onDestroy() {
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
        super.onDestroy();
    }

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
        textViewTitleOffers = (TextView) getActivity().findViewById(R.id.text_title);
        textViewTitleCardDetails = (TextView) getActivity().findViewById(R.id
                .text_title_card_details);
        textViewOfferName = (TextView) getActivity().findViewById(R.id.text_title_offer_name);
        setToolbar();
        recyclerViewOffers = (RecyclerView) view.findViewById(R.id.rv_offers);
        emptyOffersLayout = (RelativeLayout)view.findViewById(R.id.empty_offers_layout);
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
                public void onItemClick(View view, int position) {
                    String offerType = null;
                    if (offersAdapter.getItemAtPosition(position).getDuration() != null && offersAdapter.getItemAtPosition(position).getDuration().isEmpty()) {
                        offerType = OffersActivity.OFFER_TYPE_INSTALMENTS;
                    } else {
                        offerType = OffersActivity.OFFER_TYPE_BINPROMO;
                    }

                    onOfferClicked(position, offersAdapter.getItemAtPosition(position).getOfferName(), offerType);
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
            SdkUtil.showProgressDialog(getActivity(), getString(R.string.fetching_offers),
                    false);
            showHideEmptyOffersMessage(false);
            if (veritransSDK != null) {

                veritransSDK.getOffersList();
            }
        }
    }

    private void showHideEmptyOffersMessage(boolean showLayout) {
        if(showLayout){
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
                ((OffersActivity) getActivity()).replaceFragment(addCardDetailsFragment, true,
                        false);
            } else {
                OffersSavedCardFragment savedCardFragment = OffersSavedCardFragment
                        .newInstance(position, offerName, offerType);
                ((OffersActivity) getActivity()).replaceFragment(savedCardFragment, true, false);
            }

        }
    }

    @Subscribe
    @Override
    public void onEvent(GetOfferSuccessEvent event) {
        GetOffersResponseModel getOffersResponseModel = event.getResponse();
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
            if (((OffersActivity) getActivity()).offersListModels.isEmpty()) {
                showHideEmptyOffersMessage(true);
            } else {
                showHideEmptyOffersMessage(false);
            }
        } else {
            showHideEmptyOffersMessage(true);
        }
    }

    @Subscribe
    @Override
    public void onEvent(GetOfferFailedEvent event) {
        SdkUtil.hideProgressDialog();
        Logger.i("offers fetching failed :" + event.getMessage());
        //todo
        showHideEmptyOffersMessage(true);
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        SdkUtil.hideProgressDialog();
        Logger.i("offers fetching failed :" + getString(R.string.no_network_msg));
        //todo
        showHideEmptyOffersMessage(true);
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        SdkUtil.hideProgressDialog();
        Logger.i("offers fetching failed :" + event.getMessage());
        //todo
        showHideEmptyOffersMessage(true);
    }
}
