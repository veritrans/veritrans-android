package com.midtrans.demo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by rakawm on 3/15/17.
 */

public class DemoImageAdapter extends FragmentPagerAdapter {

    public DemoImageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
