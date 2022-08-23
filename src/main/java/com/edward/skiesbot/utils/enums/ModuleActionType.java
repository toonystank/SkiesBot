package com.edward.skiesbot.utils.enums;

public enum ModuleActionType {
    MENTION("Mention"),
    REACTION("Reaction");

    private final String type;

    ModuleActionType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
