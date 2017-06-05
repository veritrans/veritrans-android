package com.midtrans.sdk.core.models.merchant;

import java.io.Serializable;

/**
 * Created by rakawm on 5/11/17.
 */

public class FreeTextItem implements Serializable{
    public final String id;
    public final String en;

    public FreeTextItem(String id, String en) {
        this.id = id;
        this.en = en;
    }
}
