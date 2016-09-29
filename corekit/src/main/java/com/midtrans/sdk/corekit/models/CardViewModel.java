package com.midtrans.sdk.corekit.models;

/**
 * @author rakawm
 */
public class CardViewModel {
    private SaveCardRequest cardDetail;
    private boolean isSelected;

    public CardViewModel(SaveCardRequest cardDetail) {
        setCardDetail(cardDetail);
        setIsSelected(false);
    }

    public SaveCardRequest getCardDetail() {
        return cardDetail;
    }

    public void setCardDetail(SaveCardRequest cardDetail) {
        this.cardDetail = cardDetail;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
