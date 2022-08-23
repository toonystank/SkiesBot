package com.edward.skiesbot.modules;

public class ModuleMessage {

    private final String message;
    private final boolean isEnabled;
    private final String channelID;

    public ModuleMessage(String message, boolean isEnabled, String channelID) {
        this.message = message;
        this.isEnabled = isEnabled;
        this.channelID = channelID;
    }
    public String getMessage() {
        return message;
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public String getChannelID() {
        return channelID;
    }

}
