package id.co.veritrans.sdk.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.utilities.FlipAnimation;
import id.co.veritrans.sdk.widgets.TextViewFont;
import id.co.veritrans.sdk.widgets.VeritransDialog;

public class CardDetailFragment extends Fragment {

    private static final String ARG_PARAM = "card_detail";
    private CardTokenRequest cardDetail;
    private RelativeLayout rootLayout;
    private RelativeLayout cardContainerBack;
    private RelativeLayout cardContainerFront;
    private TextViewFont bankNameTv;
    private TextViewFont cardNoTv;
    private TextViewFont expTv;
    private ImageView cvvCircle1;
    private ImageView cvvCircle2;
    private ImageView cvvCircle3;
    private EditText cvvEt;
    private Button payNowBt;
    private ImageView deleteIv;
    private Button payNowFrontBt;
    private VeritransSDK veritransSDK;
    private Fragment parentFragment;
    private ImageView imageQuestionmark;
    public CardDetailFragment() {

    }

    public static CardDetailFragment newInstance(CardTokenRequest cardDetails, Fragment parentFragment) {
        CardDetailFragment fragment = new CardDetailFragment();
        fragment.parentFragment = parentFragment;
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, cardDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veritransSDK = VeritransSDK.getVeritransSDK();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            cardDetail = (CardTokenRequest) getArguments().getSerializable(ARG_PARAM);
        }
        cardDetail.setGrossAmount(veritransSDK.getTransactionRequest().getAmount());
        Logger.i("cardDetail:" + cardDetail.getString());

        /*((CreditDebitCardFlowActivity) getActivity()).getSupportActionBar().setTitle(getString(R
                .string.card_details));
        ((CreditDebitCardFlowActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);*/
        View view = inflater.inflate(R.layout.fragment_card_detail, container, false);
        initialiseViews(view);

        return view;
    }

    private void initialiseViews(View view) {
        cardContainerFront = (RelativeLayout) view.findViewById(R.id.card_container_front_side);
        cardContainerBack = (RelativeLayout) view.findViewById(R.id.card_container_back_side);
        rootLayout = (RelativeLayout) view.findViewById(R.id.root_layout);
        float cardWidth = ((CreditDebitCardFlowActivity) getActivity()).getScreenWidth();
        cardWidth = cardWidth - getResources().getDimension(R.dimen.sixteen_dp) * getResources()
                .getDisplayMetrics().density;
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams((int) cardWidth,
                (int) cardHeight);
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
        final String cardType = cardDetail.getCardType();
        if(!TextUtils.isEmpty(cardType)){
            if(cardType.equalsIgnoreCase(Constants.CARD_TYPE_VISA)){
                Drawable visa = getResources().getDrawable(R.drawable.visa);
                cardNoTv.setCompoundDrawablesWithIntrinsicBounds(null, null, visa, null);
            } else if(cardType.equalsIgnoreCase(Constants.CARD_TYPE_MASTERCARD)){
                Drawable masterCard = getResources().getDrawable(R.drawable.mastercard);
                cardNoTv.setCompoundDrawablesWithIntrinsicBounds(null, null, masterCard, null);
            }
        }
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
                    SdkUtil.showSnackbar(getActivity(), getString(R.string
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
        if (veritransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(Constants
                .CARD_CLICK_TYPE_ONE_CLICK)) {
            payNowFrontBt.setVisibility(View.VISIBLE);
        } else {
            payNowFrontBt.setVisibility(View.GONE);
        }
        deleteIv = (ImageView) view.findViewById(R.id.image_delete_card);
        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeritransDialog veritransDialog = new VeritransDialog(getActivity(),getString(R.string.delete)
                        ,getString(R.string.card_delete_message),getString(android.R.string.yes),
                        getString(android.R.string.no));
                View.OnClickListener positiveClickListner = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(parentFragment!=null && parentFragment instanceof SavedCardFragment) {
                            ((SavedCardFragment) parentFragment).deleteCreditCard(cardDetail.getCardNumber());
                        }
                    }
                };
                veritransDialog.setOnAcceptButtonClickListener(positiveClickListner);
                veritransDialog.show();
            }
        });
        imageQuestionmark = (ImageView)view.findViewById(R.id.image_questionmark);
        imageQuestionmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeritransDialog veritransDialog = new VeritransDialog(getActivity(), getResources().getDrawable(R.drawable.cvv_dialog_image,null),
                        getString(R.string.message_cvv), getString(R.string.got_it), "");
                veritransDialog.show();
            }
        });

    }

    private void cardTransactionProcess(String cvv) {
        try {
            cardDetail.setCardCVV(Integer.parseInt(cvv));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        cardDetail.setClientKey(veritransSDK.getClientKey());
        if (veritransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(Constants
                .CARD_CLICK_TYPE_ONE_CLICK)) {
            ((CreditDebitCardFlowActivity) getActivity()).oneClickPayment(cardDetail);
        } else if (veritransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase
                (Constants.CARD_CLICK_TYPE_TWO_CLICK)) {
            ((CreditDebitCardFlowActivity) getActivity()).twoClickPayment(cardDetail);
        } else {
            ((CreditDebitCardFlowActivity) getActivity()).getToken(cardDetail);
        }

        // ((SavedCardFragment)getParentFragment()).paymentUsingCard(cardDetail);
    }

    private void flipCard() {
        if (veritransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(Constants
                .CARD_CLICK_TYPE_ONE_CLICK)) {
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
        SdkUtil.hideKeyboard(getActivity());
        if (cardContainerFront.getVisibility() == View.GONE) {
            flipAnimation.reverse();
            //SdkUtil.hideKeyboard(getActivity());
        } else {

        }
        flipAnimation.setAnimationListener(new Animation.AnimationListener() {
                                               @Override
                                               public void onAnimationStart(Animation animation) {

                                               }

                                               @Override
                                               public void onAnimationEnd(Animation animation) {
                                                   if (cardContainerFront.getVisibility() == View
                                                           .VISIBLE) {
                                                       SdkUtil.hideKeyboard(getActivity());
                                                   } else {
                                                       SdkUtil.showKeyboard(getActivity(), cvvEt);
                                                   }
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
