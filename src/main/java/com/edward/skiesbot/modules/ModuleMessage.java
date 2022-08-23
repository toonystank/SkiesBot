package com.edward.skiesbot.modules;

import com.edward.skiesbot.SkiesBot;
import com.edward.skiesbot.utils.PlaceholderParser;
import com.edward.skiesbot.utils.enums.ModuleDataType;
import com.edward.skiesbot.utils.enums.ModuleOptions;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

public class ModuleMessage {

    private final Modules modules;
    private String message;
    private final boolean isEnabled;
    private String channelID;
    private final JDA jda;


    public ModuleMessage(Modules modules, String message, boolean isEnabled, String channelID) {
        this.modules = modules;
        this.message = message;
        this.isEnabled = isEnabled;
        this.channelID = channelID;
        this.jda = SkiesBot.getInstance().getJda();
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
    public void initialize(MessageReceivedEvent event) {
        this.message = PlaceholderParser.parse(event.getMessage().getContentRaw(), event);
        this.channelID = event.getChannel().getId();
        this.finalize(message);
    }
    public void initialize(GuildMemberJoinEvent event) {
        this.message = PlaceholderParser.parse(message, event);
        this.finalize(message);
    }
    public void finalize(String message) {
        if (!isEnabled) return;
        TextChannel channel = jda.getTextChannelById(channelID);
        if (channel == null) return;
        RestAction<Message> action = channel.sendMessage(message);
        Message restMessage = action.complete();
        modules.setTextMessage(restMessage);
    }
}
