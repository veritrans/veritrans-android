package com.midtrans.sdk.corekit.core.themes;

import android.graphics.Color;

import com.midtrans.sdk.corekit.core.Logger;

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
        try {
            if (!colorPrimaryHex.startsWith("#")) {
                return Color.parseColor("#" + colorPrimaryHex.toLowerCase());
            }

            return Color.parseColor(colorPrimaryHex.toLowerCase());
        } catch (Exception exception) {
            Logger.e("Color cannot be parsed. Reverted back to default grey color.");
            return Color.parseColor("#999999");
        }
    }

    @Override
    public int getPrimaryDarkColor() {
        try {
            if (!colorPrimaryDarkHex.startsWith("#")) {
                return Color.parseColor("#" + colorPrimaryDarkHex.toLowerCase());
            }

            return Color.parseColor(colorPrimaryDarkHex.toLowerCase());
        } catch (Exception exception) {
            Logger.e("Color cannot be parsed. Reverted back to default grey color.");
            return Color.parseColor("#737373");
        }
    }

    @Override
    public int getSecondaryColor() {
        try {
            if (!colorSecondaryHex.startsWith("#")) {
                return Color.parseColor("#" + colorSecondaryHex.toLowerCase());
            }

            return Color.parseColor(colorSecondaryHex.toLowerCase());
        } catch (Exception exception) {
            Logger.e("Color cannot be parsed. Reverted back to default grey color.");
            return Color.parseColor("#adadad");
        }
    }
}
