package com.midtrans.sdk.corekit.core.themes;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.midtrans.sdk.corekit.R;

/**
 * Created by rakawm on 2/16/17.
 */

public class ColorTheme implements BaseColorTheme {
    public final static String NAVY_BLUE = "navyblue";
    public final static String GRAY = "gray";
    public final static String AQUAMARINE = "aquamarine";
    public final static String INDIGO = "indigo";
    public final static String AZURE = "azure";
    public final static String LAVENDER = "lavender";
    public final static String HOT_PINK = "hotpink";
    public final static String MAROON = "maroon";
    public final static String CRIMSON = "crimson";
    public final static String SALMON = "salmon";
    public final static String CORAL = "coral";
    public final static String GOLDEN = "golden";
    public final static String KHAKI = "khaki";
    public final static String GREEN_FOREST = "greenforest";
    public final static String OLIVE = "olive";
    public final static String LIME = "lime";
    public final static String MINTY = "minty";
    public final static String TEAL = "teal";

    private final Context context;
    private final String colorTheme;

    public ColorTheme(Context context, String colorTheme) {
        this.context = context;
        this.colorTheme = colorTheme;
    }

    @Override
    public int getPrimaryColor() {
        switch (colorTheme) {
            case NAVY_BLUE:
                return ContextCompat.getColor(context, R.color.navy_blue_primary);
            case GRAY:
                return ContextCompat.getColor(context, R.color.gray_primary);
            case AQUAMARINE:
                return ContextCompat.getColor(context, R.color.aquamarine_primary);
            case AZURE:
                return ContextCompat.getColor(context, R.color.azure_primary);
            case LAVENDER:
                return ContextCompat.getColor(context, R.color.lavender_primary);
            case HOT_PINK:
                return ContextCompat.getColor(context, R.color.hot_pink_primary);
            case MAROON:
                return ContextCompat.getColor(context, R.color.maroon_primary);
            case CRIMSON:
                return ContextCompat.getColor(context, R.color.crimson_primary);
            case SALMON:
                return ContextCompat.getColor(context, R.color.salmon_primary);
            case CORAL:
                return ContextCompat.getColor(context, R.color.coral_primary);
            case GOLDEN:
                return ContextCompat.getColor(context, R.color.golden_primary);
            case KHAKI:
                return ContextCompat.getColor(context, R.color.khaki_primary);
            case GREEN_FOREST:
                return ContextCompat.getColor(context, R.color.green_forest_primary);
            case OLIVE:
                return ContextCompat.getColor(context, R.color.olive_primary);
            case LIME:
                return ContextCompat.getColor(context, R.color.lime_primary);
            case MINTY:
                return ContextCompat.getColor(context, R.color.minty_primary);
            case TEAL:
                return ContextCompat.getColor(context, R.color.teal_primary);
            case INDIGO:
                return ContextCompat.getColor(context, R.color.indigo_primary);
            default:
                return 0;
        }
    }

    @Override
    public int getPrimaryDarkColor() {
        switch (colorTheme) {
            case NAVY_BLUE:
                return ContextCompat.getColor(context, R.color.navy_blue_primary_dark);
            case GRAY:
                return ContextCompat.getColor(context, R.color.gray_primary_dark);
            case AQUAMARINE:
                return ContextCompat.getColor(context, R.color.aquamarine_primary_dark);
            case AZURE:
                return ContextCompat.getColor(context, R.color.azure_primary_dark);
            case LAVENDER:
                return ContextCompat.getColor(context, R.color.lavender_primary_dark);
            case HOT_PINK:
                return ContextCompat.getColor(context, R.color.hot_pink_primary_dark);
            case MAROON:
                return ContextCompat.getColor(context, R.color.maroon_primary_dark);
            case CRIMSON:
                return ContextCompat.getColor(context, R.color.crimson_primary_dark);
            case SALMON:
                return ContextCompat.getColor(context, R.color.salmon_primary_dark);
            case CORAL:
                return ContextCompat.getColor(context, R.color.coral_primary_dark);
            case GOLDEN:
                return ContextCompat.getColor(context, R.color.golden_primary_dark);
            case KHAKI:
                return ContextCompat.getColor(context, R.color.khaki_primary_dark);
            case GREEN_FOREST:
                return ContextCompat.getColor(context, R.color.green_forest_primary_dark);
            case OLIVE:
                return ContextCompat.getColor(context, R.color.olive_primary_dark);
            case LIME:
                return ContextCompat.getColor(context, R.color.lime_primary_dark);
            case MINTY:
                return ContextCompat.getColor(context, R.color.minty_primary_dark);
            case TEAL:
                return ContextCompat.getColor(context, R.color.teal_primary_dark);
            case INDIGO:
                return ContextCompat.getColor(context, R.color.indigo_primary_dark);
            default:
                return 0;
        }
    }

    @Override
    public int getSecondaryColor() {
        switch (colorTheme) {
            case NAVY_BLUE:
                return ContextCompat.getColor(context, R.color.navy_blue_secondary);
            case GRAY:
                return ContextCompat.getColor(context, R.color.gray_secondary);
            case AQUAMARINE:
                return ContextCompat.getColor(context, R.color.aquamarine_secondary);
            case AZURE:
                return ContextCompat.getColor(context, R.color.azure_secondary);
            case LAVENDER:
                return ContextCompat.getColor(context, R.color.lavender_secondary);
            case HOT_PINK:
                return ContextCompat.getColor(context, R.color.hot_pink_secondary);
            case MAROON:
                return ContextCompat.getColor(context, R.color.maroon_secondary);
            case CRIMSON:
                return ContextCompat.getColor(context, R.color.crimson_secondary);
            case SALMON:
                return ContextCompat.getColor(context, R.color.salmon_secondary);
            case CORAL:
                return ContextCompat.getColor(context, R.color.coral_secondary);
            case GOLDEN:
                return ContextCompat.getColor(context, R.color.golden_secondary);
            case KHAKI:
                return ContextCompat.getColor(context, R.color.khaki_secondary);
            case GREEN_FOREST:
                return ContextCompat.getColor(context, R.color.green_forest_secondary);
            case OLIVE:
                return ContextCompat.getColor(context, R.color.olive_secondary);
            case LIME:
                return ContextCompat.getColor(context, R.color.lime_secondary);
            case MINTY:
                return ContextCompat.getColor(context, R.color.minty_secondary);
            case TEAL:
                return ContextCompat.getColor(context, R.color.teal_secondary);
            case INDIGO:
                return ContextCompat.getColor(context, R.color.indigo_secondary);
            default:
                return 0;
        }
    }
}
