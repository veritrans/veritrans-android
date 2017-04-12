package com.midtrans.sdk.uikit.widgets;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

/**
 * Created by rakawm on 4/12/17.
 */

public class DefaultTextViewWithLink extends DefaultTextView {
    private static final String LABEL_URL = "url";

    public DefaultTextViewWithLink(Context context) {
        super(context);
        init();
    }

    public DefaultTextViewWithLink(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DefaultTextViewWithLink(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initLinkColor();
        setLinkMode();
    }

    private void setLinkMode() {
        BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, this).setOnLinkClickListener(new BetterLinkMovementMethod.OnLinkClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onClick(TextView textView, String url) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(LABEL_URL, url);
                clipboard.setPrimaryClip(clip);
                // Show toast
                Toast.makeText(getContext(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initLinkColor() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            int color = midtransSDK.getColorTheme().getSecondaryColor();
            if (color != 0) {
                setLinkTextColor(color);
            }
        }
    }
}
