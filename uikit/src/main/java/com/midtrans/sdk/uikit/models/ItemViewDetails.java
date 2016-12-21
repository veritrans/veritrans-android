package com.midtrans.sdk.uikit.models;

/**
 * Created by rakawm on 12/21/16.
 */

public class ItemViewDetails {
    public static final String TYPE_ITEM_HEADER = "item.header";
    public static final String TYPE_ITEM = "item";

    private String key;
    private String value;
    private String type;
    private boolean itemAvailable;

    public ItemViewDetails(String key, String value, String type, boolean itemAvailable) {
        setKey(key);
        setValue(value);
        setType(type);
        setItemAvailable(itemAvailable);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isItemAvailable() {
        return itemAvailable;
    }

    public void setItemAvailable(boolean itemAvailable) {
        this.itemAvailable = itemAvailable;
    }
}
