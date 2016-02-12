package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.GetOffersResponseModel;

/**
 * @author rakawm
 */
public class GetOfferSuccessEvent extends BaseSuccessEvent<GetOffersResponseModel> {
    public GetOfferSuccessEvent(GetOffersResponseModel response) {
        super(response);
    }
}
