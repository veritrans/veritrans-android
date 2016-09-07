package com.midtrans.sdk.sample.core;

/**
 * Created by rakawm on 6/2/16.
 */
public class CoreViewModel {
    private String title;
    private int image;

    public CoreViewModel(String title, int image) {
        setTitle(title);
        setImage(image);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
