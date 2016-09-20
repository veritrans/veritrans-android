package com.midtrans.sdk.widgets.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.coreflow.core.Logger;
import com.midtrans.sdk.widgets.CardDetailForm;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 9/13/16.
 */
public class CardPagerAdapter extends PagerAdapter{
    private ArrayList<CardDetailForm> cardDetails = new ArrayList<>();


    @Override
    public int getItemPosition(Object object) {
        int index = cardDetails.indexOf (object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = cardDetails.get (position);
        container.addView (v);
        return v;
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
        CardDetailForm view = (CardDetailForm) object;

        container.removeView(view);
    }

    public int addView(CardDetailForm form){
        return addView(form, cardDetails.size());
    }

    private int addView(CardDetailForm form, int position){
        cardDetails.add(position, form);
        return position;
    }

    public int removeView(ViewPager pager, CardDetailForm view){
        return removeView(pager, cardDetails.indexOf(view));
    }

    private int removeView (ViewPager pager, int position)
    {
        pager.setAdapter (null);
        cardDetails.remove (position);
        this.notifyDataSetChanged();

        pager.setAdapter (this);
        return position;
    }

    public CardDetailForm getCurrentItem(ViewPager pager)
    {
        return cardDetails.get (pager.getCurrentItem());
    }


}
