package com.midtrans.sdk.uikit.utilities;

import android.app.Activity;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.view.model.ItemViewDetails;

import java.util.ArrayList;
import java.util.List;

public class ModelHelper {
    public static List<ItemViewDetails> mappingItemDetails(Activity context, PaymentInfoResponse response) {
        String currency = response.getTransactionDetails().getCurrency();
        List<ItemViewDetails> itemViewDetails = new ArrayList<>();

        for (Item item : response.getItemDetails()) {
            String price = CurrencyHelper.formatAmount(context, item.getPrice(), currency);
            String itemName = item.getName();

            if (item.getQuantity() > 1) {
                itemName = context.getString(
                        R.string.text_item_name_format,
                        item.getName(),
                        item.getQuantity());
            }

            itemViewDetails.add(new ItemViewDetails(itemName,
                    price,
                    ItemViewDetails.TYPE_ITEM,
                    true));
        }
        return itemViewDetails;
    }
}
