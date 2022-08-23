package com.edward.skiesbot.commands;

import com.edward.skiesbot.SkiesBot;
import com.edward.skiesbot.utils.DefaultEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Say {

    private final SkiesBot bot;

    public Say(SkiesBot bot) {
        this.bot = bot;
    }


    public void sayCommand(@NotNull MessageReceivedEvent event) {
        if (!(event.getChannel() instanceof TextChannel)) return;
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_CHANNEL)) return;
        event.getMessage().delete().queue();
        EmbedBuilder embed = new EmbedBuilder();
        DefaultEmbed.setDefault(embed);

        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel channel;
        String message = event.getMessage().getContentRaw();
        message = message.replaceFirst("!say", "");
        try {
            channel = bot.getJda().getTextChannelById(args[1]);
            message = message.replaceFirst(args[1], "");
        } catch (NumberFormatException e) {
            channel = event.getTextChannel();
        }
        if (channel == null) {
            channel = event.getTextChannel();
        }
        if (!event.getMessage().getAttachments().isEmpty()) {
            embed.setImage(event.getMessage().getAttachments().get(0).getUrl());
        }
        embed.setDescription(message);
        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
