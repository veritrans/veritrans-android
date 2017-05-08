package com.midtrans.demo.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ziahaqi on 5/5/17.
 */

public class SavedCards implements Serializable {
    public final List<SavedCard> savedCards;

    public SavedCards(List<SavedCard> savedCards) {
        this.savedCards = savedCards;
    }
}
