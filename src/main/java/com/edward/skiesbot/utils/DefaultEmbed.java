package com.edward.skiesbot.utils;

import com.edward.skiesbot.SkiesBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DefaultEmbed {

    private SkiesBot plugin;
    private JDA jda;
    public DefaultEmbed(SkiesBot plugin) {
        this.plugin = plugin;
        this.jda = plugin.getJda();
    }
    public void setDefault(EmbedBuilder embed, MessageReceivedEvent event) {
        embed.setColor(0x2F3136);
        embed.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
        embed.setTimestamp(event.getMessage().getTimeCreated()); 
        embed.setAuthor(event.getAuthor().getName(), event.getAuthor().getAvatarUrl(), event.getAuthor().getAvatarUrl());
    }
    public void setColor(EmbedBuilder embed) {
        embed.setColor(plugin.getConfig().getInt("color"));
    }


}
