package com.edward.skiesbot.utils.enums;

public enum ModuleType {
    JOIN("JOIN"),
    MESSAGE_RECEIVED_IN_CHANNEL("MESSAGE_RECEIVED_IN_CHANNEL"),
    MESSAGE_RECEIVED("MESSAGE_RECEIVED"),
    ROLE_MENTION("ROLE_MENTION");

    private final String type;

    ModuleType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
