package com.midtrans.sdk.core.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import android.text.TextUtils;

import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;

import java.io.IOException;

/**
 * Created by rakawm on 4/5/17.
 */

public class CreditCardPaymentParamsTypeAdapter extends TypeAdapter<CreditCardPaymentParams> {
    @Override
    public void write(JsonWriter out, CreditCardPaymentParams value) throws IOException {
        out.beginObject();
        if (value.getMaskedCard() != null && !TextUtils.isEmpty(value.getMaskedCard())) {
            out.name("masked_card").value(value.getMaskedCard());
        }

        if (value.getCardToken() != null && !TextUtils.isEmpty(value.getCardToken())) {
            out.name("card_token").value(value.getCardToken());
        }

        if (value.getInstallment() != null && !TextUtils.isEmpty(value.getInstallment())) {
            out.name("installment").value(value.getInstallment());
        }

        out.name("save_card").value(value.isSaveCard());

        if (value.getPoint() != 0.0f) {
            out.name("point").value(value.getPoint());
        }

        out.endObject();
    }

    @Override
    public CreditCardPaymentParams read(JsonReader in) throws IOException {
        final CreditCardPaymentParams params = new CreditCardPaymentParams();

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if ("masked_card".equals(name)) {
                params.setMaskedCard(in.nextString());
            } else if ("card_token".equals(name)) {
                params.setCardToken(in.nextString());
            } else if ("installment".equals(name)) {
                params.setInstallment(in.nextString());
            } else if ("save_card".equals(name)) {
                params.setSaveCard(in.nextBoolean());
            } else if ("point".equals(name)) {
                params.setPoint((float) in.nextDouble());
            }
        }
        in.endObject();
        return params;
    }
}
