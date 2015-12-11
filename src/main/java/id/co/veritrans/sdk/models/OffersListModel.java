package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * model class created for offers list recycler view.
 * it contains offer title and offers ending date
 *
 * Created by Ankit on 12/7/15.
 */
public class OffersListModel implements Serializable{

    @SerializedName("offer name")
    private String offerName;
    @SerializedName("duration")
    private List<Integer> duration = new ArrayList<>();
    @SerializedName("bins")
    private List<String> bins = new ArrayList<>();

    /**
     *
     * @return
     * The offerName
     */
    public String getOfferName() {
        return offerName;
    }

    /**
     *
     * @param offerName
     * The offer name
     */
    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    /**
     *
     * @return
     * The duration
     */
    public List<Integer> getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The duration
     */
    public void setDuration(List<Integer> duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     * The bins
     */
    public List<String> getBins() {
        return bins;
    }

    /**
     *
     * @param bins
     * The bins
     */
    public void setBins(List<String> bins) {
        this.bins = bins;
    }
}