package com.midtrans.sdk.uikit.models;

public class Qris {
    private String type;
    private String name;
    private int image;
    private Integer priority = 0;
    private String description;
    private String status;

    public Qris(String bankType, String name, int image, Integer priority, String description, String status) {
        this.type = bankType;
        this.name = name;
        this.image = image;
        this.priority = priority;
        this.description = description;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
