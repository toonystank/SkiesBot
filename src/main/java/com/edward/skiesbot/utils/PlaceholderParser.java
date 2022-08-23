package com.edward.skiesbot.utils;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlaceholderParser {

    public enum Placeholders{
        MESSAGE("{message}"),
        USER("{user}"),
        USER_AVATAR("{user_avatar}"),
        MENTION_USER("{mention_user}");
        private final String placeholder;
        Placeholders(String s) {
            this.placeholder = s;
        }
        public String getPlaceholder() {
            return placeholder;
        }
    }
    public static String parse(String message, MessageReceivedEvent event) {
        message = message.replace(Placeholders.MESSAGE.getPlaceholder(), event.getMessage().getContentRaw());
        message = message.replace(Placeholders.USER.getPlaceholder(), event.getAuthor().getName());
        message = message.replace(Placeholders.USER_AVATAR.getPlaceholder(), event.getAuthor().getEffectiveAvatarUrl());
        message = message.replace(Placeholders.MENTION_USER.getPlaceholder(), event.getAuthor().getAsMention());
        return message;
    }
    public static String parse(String message, GuildMemberJoinEvent event) {
        message = message.replace(Placeholders.USER.getPlaceholder(), event.getMember().getEffectiveName());
        message = message.replace(Placeholders.USER_AVATAR.getPlaceholder(), event.getMember().getUser().getEffectiveAvatarUrl());
        message = message.replace(Placeholders.MENTION_USER.getPlaceholder(), event.getMember().getUser().getAsMention());
        return message;
    }
}
