package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.utilities.FlipAnimation;
import id.co.veritrans.sdk.widgets.TextViewFont;

public class CardDetailFragment extends Fragment {

    private static final String ARG_PARAM = "card_detail";
    private CardTokenRequest cardDetail;
    private RelativeLayout rootLayout;
    private RelativeLayout cardContainerBack;
    private RelativeLayout cardContainerFront;
    private TextViewFont bankNameTv;
    private TextViewFont cardNoTv;
    private TextViewFont expTv;
    private ImageView cardTypeIv;
    private ImageView cvvCircle1;
    private ImageView cvvCircle2;
    private ImageView cvvCircle3;
    private EditText cvvEt;
    private Button payNowBt;

    public static CardDetailFragment newInstance(CardTokenRequest cardDetails) {
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
        if (getArguments() != null) {
            cardDetail = (CardTokenRequest) getArguments().getSerializable(ARG_PARAM);
        }
        ((CreditDebitCardFlowActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.card_details));
        ((CreditDebitCardFlowActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View view = inflater.inflate(R.layout.fragment_card_detail, container, false);
        initialiseViews(view);

        return view;
    }

    private void initialiseViews(View view) {
        cardContainerFront = (RelativeLayout) view.findViewById(R.id.card_container_front_side);
        cardContainerBack = (RelativeLayout) view.findViewById(R.id.card_container_back_side);
        rootLayout = (RelativeLayout) view.findViewById(R.id.root_layout);
        float cardWidth = ((CreditDebitCardFlowActivity) getActivity()).getScreenWidth();
        cardWidth = cardWidth - getResources().getDimension(R.dimen.sixteen_dp) * getResources().getDisplayMetrics().density;
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams((int) cardWidth, (int) cardHeight);
        Logger.i("card width:" + parms.width + ",height:" + parms.height);
        cardContainerFront.setLayoutParams(parms);
        cardContainerBack.setLayoutParams(parms);
        cardContainerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });
        cardContainerFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });
        bankNameTv = (TextViewFont) view.findViewById(R.id.text_bank_name);
        cardNoTv = (TextViewFont) view.findViewById(R.id.text_card_number);
        expTv = (TextViewFont) view.findViewById(R.id.text_exp_date);
        cardTypeIv = (ImageView) view.findViewById(R.id.image_card_type);
        cvvCircle1 = (ImageView) view.findViewById(R.id.image_cvv1);
        cvvCircle2 = (ImageView) view.findViewById(R.id.image_cvv2);
        cvvCircle3 = (ImageView) view.findViewById(R.id.image_cvv3);
        cvvEt = (EditText) view.findViewById(R.id.et_cvv);
        cvvEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = cvvEt.getText().toString().trim().length();
                switch (length) {
                    case 0:
                        cvvCircle1.setImageResource(R.drawable.hollow_circle);
                        cvvCircle2.setImageResource(R.drawable.hollow_circle);
                        cvvCircle3.setImageResource(R.drawable.hollow_circle);
                        break;
                    case 1:
                        cvvCircle1.setImageResource(R.drawable.cvv_circle);
                        cvvCircle2.setImageResource(R.drawable.hollow_circle);
                        cvvCircle3.setImageResource(R.drawable.hollow_circle);
                        break;
                    case 2:
                        cvvCircle1.setImageResource(R.drawable.cvv_circle);
                        cvvCircle2.setImageResource(R.drawable.cvv_circle);
                        cvvCircle3.setImageResource(R.drawable.hollow_circle);
                        break;
                    case 3:
                        cvvCircle1.setImageResource(R.drawable.cvv_circle);
                        cvvCircle2.setImageResource(R.drawable.cvv_circle);
                        cvvCircle3.setImageResource(R.drawable.cvv_circle);
                        break;
                }

            }
        });
        bankNameTv.setText(cardDetail.getBank());
        cardNoTv.setText(cardDetail.getFormatedCardNumber());
        expTv.setText(cardDetail.getFormatedExpiryDate());
        payNowBt = (Button) view.findViewById(R.id.btn_pay_now);
        payNowBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdkUtil.hideKeyboard(getActivity());
                final String cvv = cvvEt.getText().toString().trim();
                if (TextUtils.isEmpty(cvv)) {
                    SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_message_cvv));
                    return;
                } else if (cvv.length() < 3) {
                    SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_message_invalid_cvv));
                    return;
                }
                //TODO  transaction process here
                cardTransactionProcess(cvv);
            }
        });

    }

    private void cardTransactionProcess(String cvv) {
        try {
            cardDetail.setCardCVV(Integer.parseInt(cvv));
            ((CreditDebitCardFlowActivity)getActivity()).getToken(cardDetail);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
       // ((SavedCardFragment)getParentFragment()).paymentUsingCard(cardDetail);
    }

    private void flipCard() {

        FlipAnimation flipAnimation = new FlipAnimation(cardContainerFront, cardContainerBack);
        SdkUtil.hideKeyboard(getActivity());
        if (cardContainerFront.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }
}
