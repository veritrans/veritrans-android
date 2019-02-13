package com.midtrans.sdk.uikit.base.theme;

import android.graphics.Color;

import com.midtrans.sdk.corekit.utilities.Logger;

public class CustomColorTheme implements BaseColorTheme {
    private String colorPrimaryHex;
    private String colorPrimaryDarkHex;
    private String colorAccent;

    public CustomColorTheme(String colorPrimaryHex, String colorPrimaryDarkHex, String colorAccent) {
        this.colorPrimaryHex = colorPrimaryHex;
        this.colorPrimaryDarkHex = colorPrimaryDarkHex;
        this.colorAccent = colorAccent;
    }

    @Override
    public int getPrimaryColor() {
        try {
            if (!colorPrimaryHex.startsWith("#")) {
                return Color.parseColor("#" + colorPrimaryHex.toLowerCase());
            }

            return Color.parseColor(colorPrimaryHex.toLowerCase());
        } catch (Exception exception) {
            Logger.error("Color cannot be parsed. Reverted back to default grey color.");
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
            Logger.error("Color cannot be parsed. Reverted back to default grey color.");
            return Color.parseColor("#737373");
        }
    }

    @Override
    public int getSecondaryColor() {
        try {
            if (!colorAccent.startsWith("#")) {
                return Color.parseColor("#" + colorAccent.toLowerCase());
            }

            return Color.parseColor(colorAccent.toLowerCase());
        } catch (Exception exception) {
            Logger.error("Color cannot be parsed. Reverted back to default grey color.");
            return Color.parseColor("#adadad");
        }
    }
}