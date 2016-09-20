package com.midtrans.sdk.widgets.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.midtrans.sdk.coreflow.models.SaveCardRequest;
import com.midtrans.sdk.widgets.CardDetailForm;
import com.midtrans.sdk.widgets.R;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 9/13/16.
 */
public class CardPagerAdapter extends PagerAdapter{
    private final Context context;
    private ArrayList<SaveCardRequest> cardDetails = new ArrayList<>();
    private CardDetailForm selectedItem;

    public CardPagerAdapter(Context context, ArrayList<SaveCardRequest>
            cardDetails) {
        this.context = context;
        this.cardDetails.clear();
        this.cardDetails.addAll(cardDetails);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CardDetailForm cardDetailForm =  new CardDetailForm(context, cardDetails.get(position));
        container.addView(cardDetailForm);
        this.selectedItem = cardDetailForm;
        return cardDetailForm;
    }

    @Override
    public int getCount() {
        return cardDetails.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        RelativeLayout imageBack = (RelativeLayout) view.findViewById(R.id.card_container_back_side);
        BitmapDrawable bmpDrawableBack = (BitmapDrawable) imageBack.getBackground();
        if (bmpDrawableBack != null && bmpDrawableBack.getBitmap() != null) {
            Bitmap bitmap = bmpDrawableBack.getBitmap();
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
        }

        RelativeLayout imgViewFront = (RelativeLayout) view.findViewById(R.id.card_container_front_side);
        BitmapDrawable bmpDrawableFront = (BitmapDrawable) imgViewFront.getBackground();
        if (bmpDrawableFront != null && bmpDrawableFront.getBitmap() != null ) {
            Bitmap bitmap = bmpDrawableFront.getBitmap();
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
        }
        container.removeView((View) view);
        view = null;
    }

    public void updateData(ArrayList<SaveCardRequest> creditCardList) {
        this.cardDetails.clear();
        this.cardDetails.addAll(creditCardList);
        this.notifyDataSetChanged();
    }

    public CardDetailForm getSelectedItem() {
        return this.selectedItem;
    }

}
