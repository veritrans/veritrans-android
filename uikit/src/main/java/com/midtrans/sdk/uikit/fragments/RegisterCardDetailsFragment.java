package com.midtrans.sdk.uikit.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.OffersActivity;
import com.midtrans.sdk.uikit.activities.SaveCreditCardActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

/**
 * @author rakawm
 */
public class RegisterCardDetailsFragment extends Fragment {

    private static final String ARG_PARAM = "card_detail";
    private SaveCardRequest cardDetail;
    private RelativeLayout rootLayout;
    private RelativeLayout cardContainerBack;
    private RelativeLayout cardContainerFront;
    private TextView bankNameTv;
    private TextView cardNoTv;
    private TextView expTv;
    private EditText cvvEt;
    private Button payNowBt;
    private ImageView deleteIv;
    private ImageView logo;
    private Button payNowFrontBt;
    private MidtransSDK midtransSDK;
    private Fragment parentFragment;
    private Activity activity;

    public static RegisterCardDetailsFragment newInstance(SaveCardRequest cardDetails, Fragment
            parentFragment, Activity activity) {
        RegisterCardDetailsFragment fragment = new RegisterCardDetailsFragment();
        fragment.parentFragment = parentFragment;
        fragment.activity = activity;
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, cardDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        midtransSDK = MidtransSDK.getInstance();
        cardDetail = (SaveCardRequest) getArguments().getSerializable(ARG_PARAM);
        if (cardDetail != null) {
            Logger.i("cardDetail:" + cardDetail.getMaskedCard());
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        SdkUIFlowUtil.hideKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initialiseViews(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initialiseViews(View view) {
        cardContainerFront = (RelativeLayout) view.findViewById(R.id.card_container_front_side);
        cardContainerBack = (RelativeLayout) view.findViewById(R.id.card_container_back_side);
        rootLayout = (RelativeLayout) view.findViewById(R.id.root_layout);
        float cardWidth = 0;

        if (activity != null) {
            if (activity instanceof SaveCreditCardActivity) {
                cardWidth = ((SaveCreditCardActivity) getActivity()).getScreenWidth();
            } else if (activity instanceof OffersActivity) {
                cardWidth = ((OffersActivity) getActivity()).getScreenWidth();
            }
        }
        cardWidth = cardWidth - getResources().getDimension(R.dimen.sixteen_dp) * getResources()
                .getDisplayMetrics().density;
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams((int) cardWidth,
                (int) cardHeight);
        Logger.i("card width:" + parms.width + ",height:" + parms.height);
        cardContainerFront.setLayoutParams(parms);
        cardContainerBack.setLayoutParams(parms);
        bankNameTv = (TextView) view.findViewById(R.id.text_bank_name);
        cardNoTv = (TextView) view.findViewById(R.id.text_card_number);
        expTv = (TextView) view.findViewById(R.id.text_exp_date);
        /*cvvCircle1 = (ImageView) view.findViewById(R.id.image_cvv1);
        cvvCircle2 = (ImageView) view.findViewById(R.id.image_cvv2);
        cvvCircle3 = (ImageView) view.findViewById(R.id.image_cvv3);*/
        cvvEt = (EditText) view.findViewById(R.id.et_cvv);
        /*cvvEt.addTextChangedListener(new TextWatcher() {
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
        });*/
        cardNoTv.setText(Utils.getFormattedCreditCardNumber(cardDetail.getMaskedCard().replace("-", "XXXXXX")));

        payNowBt = (Button) view.findViewById(R.id.btn_pay_now);
        payNowBt.setVisibility(View.GONE);

        payNowFrontBt = (Button) view.findViewById(R.id.btn_pay_now_front);
        payNowFrontBt.setVisibility(View.GONE);
        deleteIv = (ImageView) view.findViewById(R.id.image_delete_card);
        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("Card to delete:" + cardDetail.getMaskedCard());
                AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                        .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (parentFragment != null && parentFragment instanceof RegisterSavedCardFragment) {
                                    ((RegisterSavedCardFragment) parentFragment).deleteCreditCard(cardDetail.getSavedTokenId());
                                }
                            }
                        })
                        .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle(R.string.delete)
                        .setMessage(R.string.card_delete_message)
                        .create();
                dialog.show();
            }
        });
        logo = (ImageView) view.findViewById(R.id.logo_card);
        String type = Utils.getCardType(cardDetail.getMaskedCard().replace("-", "XXXXXX"));
        switch (type) {
            case Utils.CARD_TYPE_VISA:
                logo.setImageResource(R.drawable.ic_visa);
                break;
            case Utils.CARD_TYPE_MASTERCARD:
                logo.setImageResource(R.drawable.ic_mastercard);
                break;
            case Utils.CARD_TYPE_JCB:
                logo.setImageResource(R.drawable.ic_jcb);
                break;
            case Utils.CARD_TYPE_AMEX:
                logo.setImageResource(R.drawable.ic_amex);
                break;
        }
    }
}
