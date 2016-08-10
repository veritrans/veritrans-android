package id.co.veritrans.sdk.coreflow.eventbus.events.snap;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.eventbus.events.BaseFailedEvent;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;

/**
 * Created by ziahaqi on 8/06/16.
 */
public class GetCardsFailedEvent extends BaseFailedEvent<ArrayList<SaveCardRequest>> {
    public GetCardsFailedEvent(String message, ArrayList<SaveCardRequest> responses) {
        super(message, responses);
    }

    public GetCardsFailedEvent(String message, ArrayList<SaveCardRequest> responses, String source) {
        super(message, responses, source);
    }
}
