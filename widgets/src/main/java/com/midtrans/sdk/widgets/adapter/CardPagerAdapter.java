package com.midtrans.sdk.widgets.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.widgets.R;
import com.midtrans.sdk.widgets.utils.CardUtils;
import com.midtrans.sdk.widgets.utils.FlipAnimation;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 9/13/16.
 */
public class CardPagerAdapter extends BaseAdapter<CardPagerAdapter.MyViewHolder>{
    private ArrayList<SaveCardRequest> cardDetails = new ArrayList<>();
    private CardPagerListner listner;
    private String cardCVV = "";
    private LayoutInflater layoutInflater;

    public CardPagerAdapter(CardPagerListner listner) {
        this.listner = listner;
    }

    public boolean checkCardValidity() {
        return !TextUtils.isEmpty(this.cardCVV);
    }

    public String getCardCVV() {
        return this.cardCVV;
    }

    public void clearAll(ViewPager pager) {
        layoutInflater = null;
        listner = null;
        pager.setAdapter(null);
        cardDetails.clear();
        destroyedItems.clear();
    }

    @Override
    public int getItemPosition(Object object) {
        int index = cardDetails.indexOf (object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext().getApplicationContext());
        }

        // Inflate view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_detail, parent, false);

        // Return view holder
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        cardCVV = "";
        holder.show(cardDetails.get(position));
        holder.editCardDetailcvv.setText("");
        holder.layoutImageCardFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCard(holder.layoutImageCardFront.getContext(), holder);
            }
        });
        holder.layoutImageCardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCard(holder.layoutImageCardBack.getContext(), holder);
            }
        });

        holder.imageNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listner != null){
                    listner.onAddNew();
                }
            }
        });
        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listner != null){
                    listner.onDelete(cardDetails.get(position));
                }
            }
        });

        holder.editCardDetailcvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cardCVV = editable.toString().trim();
            }
        });

    }

    @Override
    public int getCount() {
        return cardDetails == null ? 0 : cardDetails.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public int addViews(ViewPager pager, ArrayList<SaveCardRequest> items){
        pager.setAdapter(null);
        cardDetails.clear();
        cardDetails.addAll(items);
        pager.setAdapter(this);
        notifyDataSetChanged();
        return (cardDetails.size() - 1);
    }

    public int addView(SaveCardRequest item){
        return addView(item, cardDetails.size());
    }

    private int addView(SaveCardRequest item, int position){
        cardDetails.add(position, item);
        return position;
    }

    public int removeView(ViewPager pager, SaveCardRequest card){
        return removeView(pager, cardDetails.indexOf(card));
    }

    private int removeView (ViewPager pager, int position)
    {
        pager.setAdapter (null);
        cardDetails.remove (position);
        pager.setAdapter (this);
        this.notifyDataSetChanged();
        return position;
    }

    public SaveCardRequest getCurrentItem(ViewPager pager)
    {
        return cardDetails.get (pager.getCurrentItem());
    }

    private void flipCard(final Context context, final MyViewHolder holder) {
        Animation scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
        scaleDown.setDuration(150);
        Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);

        scaleUp.setDuration(150);
        scaleUp.setStartOffset(150);

        FlipAnimation flipAnimation = new FlipAnimation(holder.layoutImageCardFront, holder.layoutImageCardBack);
        flipAnimation.setStartOffset(100);
        flipAnimation.setDuration(200);
        if (holder.layoutImageCardFront != null && holder.layoutImageCardFront.getVisibility() == View.GONE) {
            flipAnimation.reverse();
            CardUtils.hideKeyboard(context);
        }
        flipAnimation.setAnimationListener(new Animation.AnimationListener() {
                                               @Override
                                               public void onAnimationStart(Animation animation) {

                                               }

                                               @Override
                                               public void onAnimationEnd(Animation animation) {
                                                   Handler handler = new Handler();
                                                   handler.postDelayed(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           if (holder.layoutImageCardFront.getVisibility() == View
                                                                   .VISIBLE) {

                                                               CardUtils.hideKeyboard(context);
                                                           } else {
                                                               CardUtils.showKeyboard(context, holder.editCardDetailcvv);
                                                           }
                                                       }
                                                   }, 50);

                                                   holder.layoutCardList.setAnimation(null);
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
        holder.layoutCardList.startAnimation(animationSet);
    }

    public interface CardPagerListner {
        void onDelete(SaveCardRequest card);

        void onAddNew();
    }

    public static class MyViewHolder extends BaseAdapter.ViewHolder {
        ImageView imageDelete, imageNew;
        RelativeLayout layoutImageCardFront, layoutImageCardBack, layoutCardList;
        EditText editCardDetailcvv;

        public MyViewHolder(View convertView) {
            super(convertView);
            layoutImageCardBack = (RelativeLayout) convertView.findViewById(R.id.card_container_back_side);
            layoutImageCardFront = (RelativeLayout) convertView.findViewById(R.id.card_container_front_side);
            layoutCardList = (RelativeLayout) convertView.findViewById(R.id.layout_card_list);
            imageDelete = (ImageView) convertView.findViewById(R.id.image_delete_card);
            imageNew = (ImageView) convertView.findViewById(R.id.image_new_card);
            editCardDetailcvv = (EditText) convertView.findViewById(R.id.edit_cvv);
        }

        public void show(SaveCardRequest card) {
            // TODO: update views with foo
        }
    }
}
