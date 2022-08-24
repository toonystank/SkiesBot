package com.edward.skiesbot.modules;

import com.edward.skiesbot.SkiesBot;
import com.edward.skiesbot.utils.PlaceholderParser;
import com.edward.skiesbot.utils.enums.ModuleOptions;
import com.taggernation.taggernationlib.config.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.io.IOException;

public class ModuleMessage {

    private final Modules modules;
    private String message;
    private final boolean isEnabled;
    private final ModuleOptions moduleOptions;
    private String value;
    private final JDA jda;
    private boolean log;
    private ConfigManager logConfig;


    public ModuleMessage(Modules modules, boolean isEnabled, ModuleOptions moduleOptions, String value,String moduleName , boolean log) throws IOException {
        this.modules = modules;
        this.message = this.getMessageConfig();
        this.isEnabled = isEnabled;
        this.moduleOptions = moduleOptions;
        this.value = value;
        this.jda = SkiesBot.getInstance().getJda();
        if (log) this.logConfig = new ConfigManager(SkiesBot.getInstance(), moduleName + "_log.yml", "Data", false, false);
    }
    public String getMessage() {
        return message;
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public String getValue() {
        return value;
    }
    public void initialize(MessageReceivedEvent event) {
        this.message = PlaceholderParser.parse(event.getMessage().getContentRaw(), event);
        this.value = event.getChannel().getId();
        if (moduleOptions.equals(ModuleOptions.CHANNEL)) this.finalize(message, this.value);
        else if (moduleOptions.equals(ModuleOptions.MESSAGE)) this.finalize(message, event.getChannel().getId());
        // Module option mention role is not done
        if (log) saveToYML(message, event.getAuthor().getId(), event.getMessageId());
    }
    public void initialize(GuildMemberJoinEvent event) {
        this.message = PlaceholderParser.parse(message, event);
        this.finalize(message, this.value);
    }
    public void finalize(String message, String channelID) {
        if (!isEnabled) return;
        TextChannel channel = jda.getTextChannelById(channelID);
        if (channel == null) return;
        RestAction<Message> action = channel.sendMessage(message);
        Message restMessage = action.complete();
        modules.setTextMessage(restMessage);
    }
    public String getMessageConfig() {
        return modules.getString("data.Message.Message");
    }
    public void saveToYML(String message, String author, String messageID) {
        logConfig.set("Message." + messageID + ".TEXT", message);
        logConfig.set("Message." + messageID + ".AUTHOR", author);
        logConfig.save();
    }
}
