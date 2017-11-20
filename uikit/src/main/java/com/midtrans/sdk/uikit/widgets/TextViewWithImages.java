package com.midtrans.sdk.uikit.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import com.midtrans.sdk.corekit.core.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextViewWithImages extends AppCompatTextView {
    private final String TAG = TextViewWithImages.class.getSimpleName();

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    public TextViewWithImages(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TextViewWithImages(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewWithImages(Context context) {
        super(context);
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

    @Override
    public void setText(CharSequence text, BufferType type) {
        try {
            Spannable s = getTextWithImages(getContext(), text);
            super.setText(s, BufferType.SPANNABLE);
        } catch (RuntimeException e) {
            Logger.e(TAG, "setText:" + e.getMessage());
        }
    }
}
