package com.edward.skiesbot.modules;

import com.edward.skiesbot.SkiesBot;
import com.edward.skiesbot.utils.DefaultEmbed;
import com.edward.skiesbot.utils.PlaceholderParser;
import com.edward.skiesbot.utils.enums.ModuleDataType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;

public class ModuleEmbed {

    private final Modules modules;
    private final boolean isEnabled;
    private final String channelID;
    private String title;
    private final List<String> description;
    private String footerText;
    private String footerIconURL;
    private String thumbnail;
    private final JDA jda;

    public ModuleEmbed(Modules modules,boolean isEnabled, String channelID, String title, List<String> description, String footerText, String footerIconURL, String thumbnail) {
        this.modules = modules;
        this.isEnabled = isEnabled;
        this.channelID = channelID;
        this.title = title;
        this.description = description;
        this.footerText = footerText;
        this.footerIconURL = footerIconURL;
        this.thumbnail = thumbnail;
        this.jda = SkiesBot.getInstance().getJda();
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public String getChannelID() {
        return channelID;
    }
    public String getTitle() {
        return title;
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
    public void initialize(MessageReceivedEvent event) {
        this.title = PlaceholderParser.parse(title, event);
        this.description.forEach(s -> s = PlaceholderParser.parse(s, event));
        this.footerText = PlaceholderParser.parse(footerText, event);
        this.footerIconURL = PlaceholderParser.parse(footerIconURL, event);
        this.thumbnail = PlaceholderParser.parse(thumbnail, event);
        this.finalize(title, description, footerText, footerIconURL, thumbnail);
    }

    public void initialize(GuildMemberJoinEvent event) {
        this.title = PlaceholderParser.parse(title, event);
        this.description.forEach(s -> s = PlaceholderParser.parse(s, event));
        this.footerText = PlaceholderParser.parse(footerText, event);
        this.footerIconURL = PlaceholderParser.parse(footerIconURL, event);
        this.thumbnail = PlaceholderParser.parse(thumbnail, event);
        this.finalize(title, description, footerText, footerIconURL, thumbnail);
    }

    public void finalize(String title, List<String> description, String footerText, String footerIconURL, String thumbnail) {
        if (!isEnabled) return;
        TextChannel channel = jda.getTextChannelById(channelID);
        if (channel == null) return;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(String.join("/n", description));
        if (footerText != null || footerIconURL != null) {
            builder.setFooter(footerText, footerIconURL);
        }
        if (thumbnail != null) {
            builder.setThumbnail(thumbnail);
        }
        DefaultEmbed.setDefault(builder);
        RestAction<Message> action = channel.sendMessageEmbeds(builder.build());
        Message restMessage = action.complete();
        modules.setEmbedMessage(restMessage);
    }


}
