package com.edward.skiesbot.events;

public class ModuleMessage {

    private String message;
    private boolean isEnabled;
    private String channelID;

    public ModuleMessage(String message, boolean isEnabled, String channelID) {
        this.message = message;
        this.isEnabled = isEnabled;
        this.channelID = channelID;
    }

}
