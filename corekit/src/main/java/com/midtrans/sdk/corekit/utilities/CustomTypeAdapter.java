package com.midtrans.sdk.corekit.utilities;

import android.text.TextUtils;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.midtrans.sdk.corekit.models.snap.params.CreditCardPaymentParams;
import java.io.IOException;

/**
 * Created by rakawm on 3/10/17.
 */

public class CustomTypeAdapter extends TypeAdapter<CreditCardPaymentParams> {

    @Override
    public void write(JsonWriter out, CreditCardPaymentParams value) throws IOException {
        out.beginObject();
        if (value.getMaskedCard() != null && !TextUtils.isEmpty(value.getMaskedCard())) {
            out.name("masked_card").value(value.getMaskedCard());
        }

        if (value.getBank() != null && !TextUtils.isEmpty(value.getBank())) {
            out.name("bank").value(value.getBank());
        }

        if (value.getCardToken() != null && !TextUtils.isEmpty(value.getCardToken())) {
            out.name("card_token").value(value.getCardToken());
        }

        if (value.getInstallmentTerm() != null && !TextUtils.isEmpty(value.getInstallmentTerm())) {
            out.name("installment").value(value.getInstallmentTerm());
        }

        out.name("save_card").value(value.isSaveCard());

        if (value.isFromBankPoint()) {
            if(value.getPointRedeemed() == 0.0f){
                out.name("point").value(0);
            }else{
                out.name("point").value(value.getPointRedeemed());
            }
        }

        out.endObject();
    }

    @Override
    public CreditCardPaymentParams read(JsonReader in) throws IOException {
        final CreditCardPaymentParams params = new CreditCardPaymentParams(null, null, null);

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if ("masked_card".equals(name)) {
                params.setMaskedCard(in.nextString());
            } else if ("card_token".equals(name)) {
                params.setCardToken(in.nextString());
            } else if ("bank".equals(name)) {
                params.setBank(in.nextString());
            } else if ("installment".equals(name)) {
                params.setInstallmentTerm(in.nextString());
            } else if ("save_card".equals(name)) {
                params.setSaveCard(in.nextBoolean());
            } else if ("point".equals(name)) {
                params.setPointRedeemed((float) in.nextDouble());
            }
        }
        in.endObject();
        return params;
    }
}
