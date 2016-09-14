package com.midtrans.sdk.widgets.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.coreflow.models.SaveCardRequest;
import com.midtrans.sdk.widgets.CardDetailForm;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 9/13/16.
 */
public class CardPagerAdapter extends PagerAdapter{
    private final Context context;
    private final CardDetailForm.CardDetailListener listener;
    private ArrayList<SaveCardRequest> cardDetails = new ArrayList<>();
    private CardDetailForm selectedItem;

    public CardPagerAdapter(Context context, ArrayList<SaveCardRequest>
            cardDetails, CardDetailForm.CardDetailListener listener) {
        this.listener = listener;
        this.context = context;
        this.cardDetails.clear();
        this.cardDetails.addAll(cardDetails);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CardDetailForm cardDetailForm =  new CardDetailForm(context, cardDetails.get(position), listener);
        container.addView(cardDetailForm);
        this.selectedItem = cardDetailForm;
        return cardDetailForm;
    }

    @Override
    public int getCount() {
        return cardDetails.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void updateData(ArrayList<SaveCardRequest> creditCardList) {
        this.cardDetails.clear();
        this.cardDetails.addAll(creditCardList);
        this.notifyDataSetChanged();
    }

    public CardDetailForm getSelectedItem() {
        return this.selectedItem;
    }
}
