package com.edward.skiesbot.events;

import java.util.List;

public class ModuleEmbed {

    private boolean isEnabled;
    private String channelID;
    private String Title;
    private List<String> description;
    private String footerText;
    private String footerIconURL;
    private String thumbnail;

    public ModuleEmbed(boolean isEnabled, String channelID, String Title, List<String> description, String footerText, String footerIconURL, String thumbnail) {
        this.isEnabled = isEnabled;
        this.channelID = channelID;
        this.Title = Title;
        this.description = description;
        this.footerText = footerText;
        this.footerIconURL = footerIconURL;
        this.thumbnail = thumbnail;
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public String getChannelID() {
        return channelID;
    }
    public String getTitle() {
        return Title;
    }
    public List<String> getDescription() {
        return description;
    }
    public String getFooterText() {
        return footerText;
    }
    public String getFooterIconURL() {
        return footerIconURL;
    }
    public String getThumbnail() {
        return thumbnail;
    }


}
