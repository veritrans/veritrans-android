package com.midtrans.sdk.corekit.core.themes;

import android.graphics.Color;

/**
 * Created by rakawm on 2/16/17.
 */

public class CustomColorTheme implements BaseColorTheme {
    private String colorPrimaryHex;
    private String colorPrimaryDarkHex;
    private String colorSecondaryHex;

    public CustomColorTheme(String colorPrimaryHex, String colorPrimaryDarkHex, String colorSecondaryHex) {
        this.colorPrimaryHex = colorPrimaryHex;
        this.colorPrimaryDarkHex = colorPrimaryDarkHex;
        this.colorSecondaryHex = colorSecondaryHex;
    }

    @Override
    public int getPrimaryColor() {
        if (colorPrimaryHex.startsWith("#")) {
            return Color.parseColor(colorPrimaryHex.replace("#", ""));
        }

        return Color.parseColor(colorPrimaryHex);
    }

    @Override
    public int getPrimaryDarkColor() {
        if (colorPrimaryDarkHex.startsWith("#")) {
            return Color.parseColor(colorPrimaryDarkHex.replace("#", ""));
        }

        return Color.parseColor(colorPrimaryDarkHex);
    }

    @Override
    public int getSecondaryColor() {
        if (colorSecondaryHex.startsWith("#")) {
            return Color.parseColor(colorSecondaryHex.replace("#", ""));
        }

        return Color.parseColor(colorSecondaryHex);
    }
}
