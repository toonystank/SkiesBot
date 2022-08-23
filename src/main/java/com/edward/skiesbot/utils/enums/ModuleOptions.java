package com.edward.skiesbot.utils.enums;

public enum ModuleOptions {
    CHANNEL("channel"),
    MESSAGE("message"),
    MENTION_ROLE("mention_role");
    private final String option;
    ModuleOptions(String option) {
        this.option = option;
    }
    public String getOption() {
        return option;
    }
}
