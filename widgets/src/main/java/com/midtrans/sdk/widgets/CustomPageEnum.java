package com.midtrans.sdk.widgets;

/**
 * Created by ziahaqi on 9/13/16.
 */
public enum  CustomPageEnum {
    RED("red", R.layout.layout_card_detail),
    BLUE("blue", R.layout.layout_card_detail);

    private String mTitleResId;
    private int mLayoutResId;

    CustomPageEnum(String name, int layoutResId) {
        mTitleResId = name;
        mLayoutResId = layoutResId;
    }

    public String getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
