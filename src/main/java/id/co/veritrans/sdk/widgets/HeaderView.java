package id.co.veritrans.sdk.widgets;

/**
 * Created by shivam on 11/17/15.
 */


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import id.co.veritrans.sdk.R;

public class HeaderView extends LinearLayout {

    TextViewFont title;
    TextViewFont subTitle;


    public HeaderView(Context context) {
        super(context);
        subTitle = (TextViewFont) findViewById(R.id.header_view_sub_title);
        title = (TextViewFont) findViewById(R.id.header_view_title);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        subTitle = (TextViewFont) findViewById(R.id.header_view_sub_title);
        title = (TextViewFont) findViewById(R.id.header_view_title);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        subTitle = (TextViewFont) findViewById(R.id.header_view_sub_title);
        title = (TextViewFont) findViewById(R.id.header_view_title);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        subTitle = (TextViewFont) findViewById(R.id.header_view_sub_title);
        title = (TextViewFont) findViewById(R.id.header_view_title);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void bindTo(String titleText) {
        subTitle = (TextViewFont) findViewById(R.id.header_view_sub_title);
        title = (TextViewFont) findViewById(R.id.header_view_title);
        bindTo(titleText, "");
    }

    public void bindTo(String titleText, String subTitleText) {
        subTitle = (TextViewFont) findViewById(R.id.header_view_sub_title);
        title = (TextViewFont) findViewById(R.id.header_view_title);

        hideOrSetText(this.title, titleText);
        hideOrSetText(this.subTitle, subTitleText);
    }

    private void hideOrSetText(TextViewFont tv, String text) {
        if (text == null || text.equals(""))
            tv.setVisibility(GONE);
        else
            tv.setText(text);
    }


    public TextViewFont getTitleTextView() {
        return title;
    }

    public TextViewFont getSubTitleTextView() {
        return subTitle;
    }
}