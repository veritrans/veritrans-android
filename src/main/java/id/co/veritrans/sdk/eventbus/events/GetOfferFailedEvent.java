package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.GetOffersResponseModel;

/**
 * @author rakawm
 */
public class GetOfferFailedEvent extends BaseFailedEvent<GetOffersResponseModel> {
    public GetOfferFailedEvent(String message, GetOffersResponseModel response) {
        super(message, response);
    }
}
