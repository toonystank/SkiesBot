package com.edward.skiesbot.utils.enums;

public enum ModuleDataType {
    MESSAGE("Message"),
    EMBED("Embed"),
    ACTION("Action");
    ModuleDataType(String data) {
        this.data = data;
    }
    private final String data;
    public String getData() {
        return data;
    }
}
