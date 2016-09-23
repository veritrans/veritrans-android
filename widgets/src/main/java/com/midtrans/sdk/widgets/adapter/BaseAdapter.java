package com.midtrans.sdk.widgets.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by ziahaqi on 9/23/16.
 */
public abstract class BaseAdapter<VH extends BaseAdapter.ViewHolder> extends PagerAdapter {

    public static abstract class ViewHolder {
        final View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

    protected Queue<VH> destroyedItems = new LinkedList<>();

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        VH viewHolder = destroyedItems.poll();

        if (viewHolder != null) {
            container.addView(viewHolder.itemView);
            onBindViewHolder(viewHolder, position);
        } else {
            viewHolder = onCreateViewHolder(container);
            onBindViewHolder(viewHolder, position);
            container.addView(viewHolder.itemView);
        }

        return viewHolder;
    }


    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((VH) object).itemView);
        destroyedItems.add((VH) object);
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return ((VH) object).itemView == view;
    }

    public abstract VH onCreateViewHolder(ViewGroup parent);

    public abstract void onBindViewHolder(VH viewHolder, int position);
}