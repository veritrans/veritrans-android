package com.midtrans.sdk.widgets.utils;

import android.support.v4.view.ViewPager;

/**
 * Created by ziahaqi on 9/13/16.
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {

    /**
     * Bind the indicator to a ViewPager.
     *
     * @param view  view pager instance
     */
    void setViewPager(ViewPager view);

    /**
     * Bind the indicator to a ViewPager.
     *
     * @param view              view pager instance
     * @param initialPosition   view pager initial position
     */
    void setViewPager(ViewPager view, int initialPosition);

    /**
     * <p>Set the current page of both the ViewPager and indicator.</p>
     * <p/>
     * <p>This <strong>must</strong> be used if you need to set the page before
     * the views are drawn on screen (e.g., default start page).</p>
     *
     * @param item  item position
     */
    void setCurrentItem(int item);

    /**
     * Set a page change listener which will receive forwarded events.
     *
     * @param listener  page change listener implementation
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    /**
     * Notify the indicator that the fragment list has changed.
     */
    void notifyDataSetChanged();
}
