package id.co.veritrans.sdk.coreflow.eventbus.events.snap;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.eventbus.events.BaseSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;

/**
 * Created by ziahaqi on 8/06/16.
 */
public class GetCardsSuccessEvent extends BaseSuccessEvent<ArrayList<SaveCardRequest>> {
    public GetCardsSuccessEvent(ArrayList<SaveCardRequest> responses) {
        super(responses);
    }

    public GetCardsSuccessEvent(ArrayList<SaveCardRequest> responses, String source) {
        super(responses, source);
    }
}
