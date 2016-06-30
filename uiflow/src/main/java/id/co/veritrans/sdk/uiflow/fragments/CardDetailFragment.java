package id.co.veritrans.sdk.uiflow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.utilities.Utils;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.uiflow.activities.OffersActivity;
import id.co.veritrans.sdk.uiflow.utilities.FlipAnimation;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;
import id.co.veritrans.sdk.uiflow.widgets.VeritransDialog;

public class CardDetailFragment extends Fragment {

    private static final String ARG_PARAM = "card_detail";
    private SaveCardRequest cardDetail;
    private RelativeLayout rootLayout;
    private RelativeLayout cardContainerBack;
    private RelativeLayout cardContainerFront;
    private TextView cardNoTv;
    private TextView expTv;
    private EditText cvvEt;
    private Button payNowBt;
    private ImageButton deleteIv;
    private ImageView logo;
    private Button payNowFrontBt;
    private VeritransSDK veritransSDK;
    private Fragment parentFragment;
    private Activity activity;

    public CardDetailFragment() {

    }

    public static CardDetailFragment newInstance(SaveCardRequest cardDetails, Fragment
            parentFragment, Activity activity) {
        CardDetailFragment fragment = new CardDetailFragment();
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
        veritransSDK = VeritransSDK.getVeritransSDK();
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
        //float cardWidth = 0;

        /*if (activity != null) {
            if (activity instanceof CreditDebitCardFlowActivity) {
                cardWidth = ((CreditDebitCardFlowActivity) getActivity()).getScreenWidth();
            } else if (activity instanceof OffersActivity) {
                cardWidth = ((OffersActivity) getActivity()).getScreenWidth();
            }
        }
        cardWidth = cardWidth - getResources().getDimension(R.dimen.sixteen_dp) * getResources()
                .getDisplayMetrics().density;
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams((int) cardWidth, (int) cardHeight);
        Logger.i("card width:" + parms.width + ",height:" + parms.height);
        cardContainerFront.setLayoutParams(parms);
        cardContainerBack.setLayoutParams(parms);*/
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
        cardNoTv = (TextView) view.findViewById(R.id.text_card_number);
        expTv = (TextView) view.findViewById(R.id.text_exp_date);
        cvvEt = (EditText) view.findViewById(R.id.et_cvv);
        cardNoTv.setText(Utils.getFormattedCreditCardNumber(cardDetail.getMaskedCard().replace("-", "XXXXXX")));

        payNowBt = (Button) view.findViewById(R.id.btn_pay_now);
        payNowBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdkUIFlowUtil.hideKeyboard(getActivity());
                final String cvv = cvvEt.getText().toString().trim();
                if (TextUtils.isEmpty(cvv)) {
                    SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string.validation_message_cvv));
                    return;
                } else if (cvv.length() < 3) {
                    SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                            .validation_message_invalid_cvv));
                    return;
                }
                //TODO  transaction process here
                cardTransactionProcess(cvv);
            }
        });
        payNowFrontBt = (Button) view.findViewById(R.id.btn_pay_now_front);
        payNowFrontBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardTransactionProcess("");
            }
        });

        Logger.i("veritransSDK.getCardClickType()" + veritransSDK.getTransactionRequest()
                .getCardClickType());
        if (veritransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(getString(R.string.card_click_type_one_click))) {
            payNowFrontBt.setVisibility(View.VISIBLE);
        } else {
            payNowFrontBt.setVisibility(View.GONE);
        }
        deleteIv = (ImageButton) view.findViewById(R.id.image_delete_card);
        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("Card to delete:" + cardDetail.getMaskedCard());
                VeritransDialog veritransDialog = new VeritransDialog(getActivity(), getString(R.string.delete), getString(R.string.card_delete_message), getString(R.string.text_yes), getString(R.string.text_no));
                View.OnClickListener positiveClickListner = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (parentFragment != null && parentFragment instanceof SavedCardFragment) {

                            ((SavedCardFragment) parentFragment).deleteCreditCard(cardDetail.getSavedTokenId());
                        } else if (parentFragment != null && parentFragment instanceof OffersSavedCardFragment) {
                            ((OffersSavedCardFragment) parentFragment).deleteCreditCard(cardDetail.getSavedTokenId());
                        }
                    }
                };
                veritransDialog.setOnAcceptButtonClickListener(positiveClickListner);
                veritransDialog.show();
            }
        });
        logo = (ImageView) view.findViewById(R.id.logo_card);
        String type = Utils.getCardType(cardDetail.getMaskedCard().replace("-", "XXXXXX"));
        switch (type) {
            case Utils.CARD_TYPE_VISA:
                logo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_visa));
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

    private void cardTransactionProcess(String cvv) {
        Logger.i("Card to delete:" + cardDetail.getMaskedCard());

        if (activity != null) {
            if (activity instanceof OffersActivity) {
                if (((OffersActivity) getActivity()).getSelectedOffer() != null) {
                    if (parentFragment != null && parentFragment instanceof OffersSavedCardFragment) {

                    }
                }
            }
        }

        if (veritransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(getString(R.string.card_click_type_one_click))) {

            if (activity != null) {
                CardTokenRequest request = new CardTokenRequest();
                request.setSavedTokenId(cardDetail.getSavedTokenId());
                if (activity instanceof CreditDebitCardFlowActivity) {
                    ((CreditDebitCardFlowActivity) getActivity()).oneClickPayment(request);
                } else if (activity instanceof OffersActivity) {
                    ((OffersActivity) getActivity()).oneClickPayment(request);
                }
            }
        } else if (veritransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase
                (getString(R.string.card_click_type_two_click))) {

            if (activity != null) {
                CardTokenRequest request = new CardTokenRequest();
                request.setSavedTokenId(cardDetail.getSavedTokenId());
                request.setCardCVV(cvv);
                if (activity instanceof CreditDebitCardFlowActivity) {
                    ((CreditDebitCardFlowActivity) getActivity()).twoClickPayment(request);
                } else if (activity instanceof OffersActivity) {
                    ((OffersActivity) getActivity()).twoClickPayment(request);
                }
            }
        } else {
            if (activity != null) {
                CardTokenRequest request = new CardTokenRequest();
                request.setSavedTokenId(cardDetail.getSavedTokenId());
                if (activity instanceof CreditDebitCardFlowActivity) {
                    ((CreditDebitCardFlowActivity) getActivity()).getToken(request);
                } else if (activity instanceof OffersActivity) {
                    ((OffersActivity) getActivity()).getToken(request);
                }
            }
        }

        // ((SavedCardFragment)getParentFragment()).paymentUsingCard(cardDetail);
    }

    private void flipCard() {
        if (veritransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(getString(R.string.card_click_type_one_click))) {
            return;
        }
       /* Animation scaleDown = new ScaleAnimation(
                1f, 0.5f, // Start and end values for the X axis scaling
                1f, 0.5f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_PARENT, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_PARENT, 0.5f); // Pivot point of Y scaling*/
        //Animation scaleDown = new ScaleAnimation(1.0f,0.5f,1.0f,0.5f);
        Animation scaleDown = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down);
        scaleDown.setDuration(150);
        /*Animation scaleUp = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                1f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling*/
        //Animation scaleUp = new ScaleAnimation(0.5f,1.0f,0.5f,1.0f);
        Animation scaleUp = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);

        scaleUp.setDuration(150);
        scaleUp.setStartOffset(150);
        //rootLayout.startAnimation(scaleDown);

        FlipAnimation flipAnimation = new FlipAnimation(cardContainerFront, cardContainerBack);
        flipAnimation.setStartOffset(100);
        flipAnimation.setDuration(200);
        if (cardContainerFront.getVisibility() == View.GONE) {
            flipAnimation.reverse();
            /*if(cvvEt!=null) {
                SdkUIFlowUtil.showKeyboard(getActivity(), cvvEt);
            }*/
            SdkUIFlowUtil.hideKeyboard(getActivity());
        }
        flipAnimation.setAnimationListener(new Animation.AnimationListener() {
                                               @Override
                                               public void onAnimationStart(Animation animation) {

                                               }

                                               @Override
                                               public void onAnimationEnd(Animation animation) {
                                                   /*if (cardContainerFront.getVisibility() == View
                                                           .VISIBLE) {
                                                       SdkUIFlowUtil.hideKeyboard(getActivity());
                                                   } else {
                                                       SdkUIFlowUtil.showKeyboard(getActivity(), cvvEt);
                                                   }*/
                                                   Handler handler = new Handler();
                                                   handler.postDelayed(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           if (cardContainerFront.getVisibility() == View
                                                                   .VISIBLE) {

                                                               SdkUIFlowUtil.hideKeyboard(getActivity());
                                                           } else {
                                                               SdkUIFlowUtil.showKeyboard(getActivity(), cvvEt);
                                                           }
                                                       }
                                                   }, 50);
                                               }

                                               @Override
                                               public void onAnimationRepeat(Animation animation) {

                                               }
                                           }

        );
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(300);
        animationSet.addAnimation(scaleDown);
        animationSet.addAnimation(flipAnimation);
        animationSet.addAnimation(scaleUp);
        rootLayout.startAnimation(animationSet);
        // rootLayout.startAnimation(scaleDown);

        //rootLayout.startAnimation(flipAnimation);
    }
}
