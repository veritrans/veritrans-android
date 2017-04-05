package com.midtrans.sdk.core.models.snap;

import com.midtrans.sdk.core.models.snap.card.Installment;

import java.util.List;

/**
 * Created by rakawm on 10/19/16.
 */

public class CreditCard {
    public final boolean saveCard;
    public final boolean secure;
    public final String tokenId;
    public final String channel;
    public final String bank;
    public final List<SavedToken> savedTokens;
    public final String type;
    public Installment installment;
    public List<String> whitelistBins;


    public CreditCard(boolean saveCard,
                      boolean secure,
                      String tokenId,
                      String channel,
                      String bank,
                      List<SavedToken> savedTokens,
                      String type) {
        this.saveCard = saveCard;
        this.secure = secure;
        this.tokenId = tokenId;
        this.channel = channel;
        this.bank = bank;
        this.savedTokens = savedTokens;
        this.type = type;
    }
}
