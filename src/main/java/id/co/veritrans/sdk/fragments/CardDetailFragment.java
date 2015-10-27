package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.models.CardDetail;

public class CardDetailFragment extends Fragment {

    private static final String ARG_PARAM = "card_detail";
    private CardDetail cardDetails;
    public static CardDetailFragment newInstance(CardDetail cardDetails) {
        CardDetailFragment fragment = new CardDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, cardDetails);
        fragment.setArguments(args);
        return fragment;
    }

    public CardDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments()!=null){
           cardDetails = (CardDetail) getArguments().getSerializable(ARG_PARAM);
        }
        View view = inflater.inflate(R.layout.fragment_card_detail, container, false);
        LinearLayout cardContainer = (LinearLayout) view.findViewById(R.id.card_container);
        float cardWidth = ((CreditDebitCardFlowActivity) getActivity()).getScreenWidth();
        cardWidth = cardWidth - getResources().getDimension(R.dimen.sixteen_dp)*getResources().getDisplayMetrics().density;
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((int)cardWidth,(int)cardHeight);
        Logger.i("card width:" + parms.width + ",height:" + parms.height);
  //      int margin = (int) (getResources().getDimension(R.dimen.sixteen_dp)*getResources().getDisplayMetrics().density);
//        parms.setMargins(margin,0,margin,0);
        cardContainer.setLayoutParams(parms);
        cardContainer.invalidate();
        return view;
    }
}
