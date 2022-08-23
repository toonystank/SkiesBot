package com.edward.skiesbot.utils;

import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;

public class DefaultEmbed {

    public static void setDefault(EmbedBuilder embed) {
        embed.setColor(0x2F3136);
        embed.setTimestamp(Instant.now());
    }
}
