package com.midtrans.sdk.ui.widgets;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

/**
 * Created by rakawm on 4/13/17.
 */

public class InstructionTextView extends DefaultTextView {
    private static final String LABEL_URL = "url";

    public InstructionTextView(Context context) {
        super(context);
        init();
    }

    public InstructionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InstructionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLinkMode();
        initLinkColor();
    }

    private void setLinkMode() {
        BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, this).setOnLinkClickListener(new BetterLinkMovementMethod.OnLinkClickListener() {
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
        MidtransUi midtransUi = MidtransUi.getInstance();
        if (midtransUi != null && midtransUi.getColorTheme() != null) {
            int color = midtransUi.getColorTheme().getSecondaryColor();
            if (color != 0) {
                setLinkTextColor(color);
            }
        }
    }
}
