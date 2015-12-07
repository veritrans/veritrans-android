package id.co.veritrans.sdk.models;

/**
 *
 * model class created for offers list recycler view.
 * it contains offer title and offers ending date
 *
 * Created by Ankit on 12/7/15.
 */
public class OffersListModel {

    private String offerTitle;
    private String offerEndsOn;
    private boolean isSelected;

    public OffersListModel(String offerTitle, String offerEndsOn, boolean isSelected) {
        this.offerTitle = offerTitle;
        this.offerEndsOn = offerEndsOn;
        this.isSelected = isSelected;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferEndsOn() {
        return offerEndsOn;
    }

    public void setOfferEndsOn(String offerEndsOn) {
        this.offerEndsOn = offerEndsOn;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}