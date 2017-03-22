package com.midtrans.demo.indicator;

/**
 * Created by rakawm on 3/15/17.
 */

public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
}