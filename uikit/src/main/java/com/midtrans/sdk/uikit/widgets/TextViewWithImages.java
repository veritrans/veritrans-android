package com.midtrans.sdk.uikit.widgets;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

public class TextViewWithImages extends AppCompatTextView {

    private static final String LABEL_URL = "url";
    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    public TextViewWithImages(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewWithImages(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewWithImages(Context context) {
        super(context);
        init();
    }

    private boolean addImages(Context context, Spannable spannable) {
        Pattern refImg = Pattern.compile("\\Q[img src=\\E([a-zA-Z0-9_]+?)\\Q/]\\E");
        boolean hasChanges = false;

        Matcher matcher = refImg.matcher(spannable);
        while (matcher.find()) {
            boolean set = true;
            for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)) {
                if (spannable.getSpanStart(span) >= matcher.start()
                        && spannable.getSpanEnd(span) <= matcher.end()
                        ) {
                    spannable.removeSpan(span);
                } else {
                    set = false;
                    break;
                }
            }
            String resname = spannable.subSequence(matcher.start(1), matcher.end(1)).toString().trim();
            int id = context.getResources().getIdentifier(resname, "drawable", context.getPackageName());
            if (set) {
                hasChanges = true;
                spannable.setSpan(new ImageSpan(context, id),
                        matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }

        return hasChanges;
    }

    private Spannable getTextWithImages(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addImages(context, spannable);
        return spannable;
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

    @Override
    public void setText(CharSequence text, BufferType type) {
        Spannable s = getTextWithImages(getContext(), text);
        super.setText(s, BufferType.SPANNABLE);
    }
}
