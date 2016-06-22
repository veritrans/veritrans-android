package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.GetOffersResponseModel;

/**
 * @author rakawm
 */
public class GetOfferFailedEvent extends BaseFailedEvent<GetOffersResponseModel> {
    public GetOfferFailedEvent(String message, GetOffersResponseModel response) {
        super(message, response);
    }

    public GetOfferFailedEvent(String message, GetOffersResponseModel response, String source) {
        super(message, response, source);
    }
}
