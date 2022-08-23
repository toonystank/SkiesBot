package com.edward.skiesbot.utils;

import com.edward.skiesbot.SkiesBot;

import java.util.List;

public enum Config {


    DISCORD_TOKEN("discord_token"),
    COMMANDS("commands"),
    COMMAND_PREFIX("command.prefix"),
    COMMAND_UNKNOWN("command.unknown"),
    MODULES("modules", true),
    ;

    private boolean isList;
    private final String value;
    public String getValue() {
        return value;
    }

    public List<String> getValues() {
        if (!isList) {
            throw new IllegalStateException("This config is not a list");
        }
        return SkiesBot.getInstance().getMainConfig().getStringList(value);
    }

    Config(String type) {
        this.value = type;
    }
    Config(String type, boolean isList) {
        this.value = type;
        this.isList = isList;
    }
}
