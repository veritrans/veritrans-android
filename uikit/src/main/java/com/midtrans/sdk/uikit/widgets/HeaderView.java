package com.midtrans.sdk.uikit.widgets;

/**
 * Created by shivam on 11/17/15.
 */


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.uikit.R;

public class HeaderView extends LinearLayout {

    TextView title;
    TextView subTitle;


    public HeaderView(Context context) {
        super(context);
        subTitle = (TextView) findViewById(R.id.header_view_sub_title);
        title = (TextView) findViewById(R.id.header_view_title);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        subTitle = (TextView) findViewById(R.id.header_view_sub_title);
        title = (TextView) findViewById(R.id.header_view_title);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        subTitle = (TextView) findViewById(R.id.header_view_sub_title);
        title = (TextView) findViewById(R.id.header_view_title);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        subTitle = (TextView) findViewById(R.id.header_view_sub_title);
        title = (TextView) findViewById(R.id.header_view_title);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void bindTo(String titleText) {
        subTitle = (TextView) findViewById(R.id.header_view_sub_title);
        title = (TextView) findViewById(R.id.header_view_title);
        bindTo(titleText, "");
    }

    public void bindTo(String titleText, String subTitleText) {
        subTitle = (TextView) findViewById(R.id.header_view_sub_title);
        title = (TextView) findViewById(R.id.header_view_title);

        hideOrSetText(this.title, titleText);
        hideOrSetText(this.subTitle, subTitleText);
    }

    private void hideOrSetText(TextView tv, String text) {
        if (text == null || text.equals(""))
            tv.setVisibility(GONE);
        else
            tv.setText(text);
    }


    public TextView getTitleTextView() {
        return title;
    }

    public TextView getSubTitleTextView() {
        return subTitle;
    }
}