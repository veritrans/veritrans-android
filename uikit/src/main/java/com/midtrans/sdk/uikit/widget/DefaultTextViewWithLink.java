package com.midtrans.sdk.uikit.widget;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.uikit.MidtransKitConfig;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.R;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

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
        BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, this).setOnLinkClickListener((textView, url) -> {
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(LABEL_URL, url);
            clipboard.setPrimaryClip(clip);
            // Show toast
            Toast.makeText(getContext(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void initLinkColor() {
        MidtransKitConfig midtransKitConfig = MidtransKit.getInstance().getMidtransKitConfig();
        if (midtransKitConfig != null && midtransKitConfig.getColorTheme() != null) {
            int color = midtransKitConfig.getColorTheme().getSecondaryColor();
            if (color != 0) {
                setLinkTextColor(color);
            }
        }
    }
}
