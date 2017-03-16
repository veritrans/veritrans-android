package com.midtrans.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by rakawm on 3/15/17.
 */

public class ImageFragment extends Fragment {

    private static final String PARAM_TYPE = "image_type";

    public static ImageFragment newInstance(int type) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_TYPE, type);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView image = (ImageView) view.findViewById(R.id.product_image);
        int type = getArguments().getInt(PARAM_TYPE);
        switch (type) {
            case 0:
                image.setImageResource(R.drawable.slide_img_1);
                break;
            case 1:
                image.setImageResource(R.drawable.slide_img_2);
                break;
            case 2:
                image.setImageResource(R.drawable.slide_img_3);
                break;
            default:
                image.setImageResource(R.drawable.slide_img_1);
                break;
        }
    }
}
