package com.midtrans.sdk.ui.models;

/**
 * Created by ziahaqi on 2/21/17.
 */

public class ItemDetail {
    public static final String TYPE_ITEM_HEADER = "item.header";
    public static final String TYPE_ITEM = "item";

    private String key;
    private String value;
    private String type;
    private boolean itemAvailable;

    public ItemDetail(String key, String value, String type, boolean itemAvailable) {
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
