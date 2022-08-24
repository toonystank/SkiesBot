package com.edward.skiesbot.modules;

import com.edward.skiesbot.SkiesBot;
import com.edward.skiesbot.utils.enums.ModuleActionType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ModuleActions {

    private final Modules modules;
    private final boolean isEnabled;
    private final Set<String> actions;
    private final String channelID;
    private final JDA jda;

    public ModuleActions(Modules modules, boolean isEnabled, Set<String> actions, String channelID) {
        this.modules = modules;
        this.isEnabled = isEnabled;
        this.actions = actions;
        this.channelID = channelID;
        this.jda = SkiesBot.getInstance().getJda();
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public Set<String> getActions() {
        return actions;
    }
    public void initialize() {
        if (!isEnabled) return;
        for (String action : actions) {
            String actionType = modules.getString("Action." + action + ".Type");
            if (actionType.equals(ModuleActionType.MENTION.getType())) {
                mention(action);
            } else if (actionType.equals(ModuleActionType.REACTION.getType())) {
                addReaction(action);
            }
        }
    }
    public void initialize(MessageReactionAddEvent event) {
        if (!isEnabled) return;
        for (String action : actions) {
            String actionType = modules.getString("Action." + action + ".Type");
            if (!actionType.equals(ModuleActionType.REACTION.getType())) return;
            for (String reaction : modules.getStringList("Action." + action + ".data")) {

                // THERE MIGHT BE A ISSUE HERE

                Bukkit.getLogger().info(event.getReaction().getReactionEmote().getAsReactionCode());
                if (reaction.equals(event.getReaction().getReactionEmote().getAsReactionCode())) {
                    if (getReactionType(action, reaction) == ReactionConfigTypes.Embed) {
                        processEmbed(action, reaction, event);
                    }
                }
            }

        }
    }
    public void addReaction(String action) {
        for (String reaction : modules.getStringList("Action." + action + ".data")) {
            if (modules.getEmbedMessage() != null) modules.getEmbedMessage().addReaction(reaction).queue();
            if (modules.getTextMessage() != null) modules.getTextMessage().addReaction(reaction).queue();
        }
    }
    public void processEmbed(String action, String reaction, MessageReactionAddEvent event) {
        String messageId = event.getMessageId();
        EmbedBuilder embed = new EmbedBuilder();
        List<String> description = modules.getModuleEmbed().getEmbedLogDescription(messageId);
        String footer_icon = modules.getModuleEmbed().getEmbedLogConfig(ModuleEmbed.EmbedLogConfigTypes.FOOTER_ICON, messageId, reaction, action);
        String footer_text = modules.getModuleEmbed().getEmbedLogConfig(ModuleEmbed.EmbedLogConfigTypes.FOOTER_TEXT, messageId, reaction, action);
        String thumbnail = modules.getModuleEmbed().getEmbedLogConfig(ModuleEmbed.EmbedLogConfigTypes.THUMBNAIL, messageId, reaction, action);
        if (getReactionEmbedProperty(action, reaction, "Title") != null) {
            embed.setTitle(getReactionEmbedProperty(action, reaction, "Title"));
        }else {
            embed.setTitle(modules.getModuleName());
        }
        if (getReactionEmbedProperty(action, reaction, "Description") != null) {
            embed.setDescription(getReactionEmbedProperty(action, reaction, "Description"));
        }else {
            embed.setDescription(String.join("\n", description));
        }
        if (getReactionEmbedProperty(action, reaction, "Footer.Text") != null || getReactionEmbedProperty(action, reaction, "Footer.Icon") != null) {
            embed.setFooter(getReactionEmbedProperty(action, reaction, "Footer.Text"), getReactionEmbedProperty(action, reaction, "Footer.Icon"));
        }
        else {
            embed.setFooter(footer_text, footer_icon);
        }
        if (getReactionEmbedProperty(action, reaction, "Color") != null) {
            embed.setColor(Integer.parseInt(Objects.requireNonNull(getReactionEmbedProperty(action, reaction, "Color"))));
        }
        if (getReactionEmbedProperty(action, reaction, "Thumbnail") != null) {
            embed.setThumbnail(getReactionEmbedProperty(action, reaction, "Thumbnail"));
        }else {
            embed.setThumbnail(thumbnail);
        }
        Objects.requireNonNull(jda.getTextChannelById(channelID), modules.getModuleName() + " provided channel id is not valid").editMessageEmbedsById(messageId, embed.build()).queue();

    }
    @Nullable
    public ReactionConfigTypes getReactionType(String id, String reaction) {
        String embed = modules.getString("Actions." + id + ".data." + reaction + ".Click.Embed");
        String message = modules.getString("Actions." + id + ".data." + reaction + ".Click.Message");
        String role = modules.getString("Actions." + id + ".data." + reaction + ".Click.Role");
        if (embed != null) {
            return ReactionConfigTypes.Embed;
        }
        if (message != null) {
            return ReactionConfigTypes.Message;
        }
        if (role != null) {
            return ReactionConfigTypes.Role;
        }
        return null;
    }
    @Nullable
    public String getReactionEmbedProperty(String id, String reaction, String property) {
        return modules.getString("Actions." + id + ".data." + reaction + ".Click.Embed." + property);
    }
    private enum ReactionConfigTypes {
        Embed("Embed"),
        Message("Message"),
        Role("role");

        public final String type;
        ReactionConfigTypes(String type) {
            this.type = type;
        }
    }
    public void mention(String action) {
        TextChannel channel = jda.getTextChannelById(channelID);
        if (channel == null) return;
        RestAction<Message> restAction;
        if (modules.getBoolean("Action." + action + "data.everyone")) {
            restAction = channel.sendMessage("@everyone");
        } else {
            User user = jda.getUserById(modules.getString("Action." + action + "data.user"));
            if (user == null) return;
            restAction = channel.sendMessage(user.getAsMention());
        }
        Message message = restAction.complete();
        if (modules.getConfig().getLong("Action." + action + ".data.delete_after") == 0) return;
        SkiesBot.getInstance().getServer().getScheduler().runTaskLater(SkiesBot.getInstance(), () -> message.delete().queue(), modules.getConfig().getLong("Action." + action + ".data.delete_after"));
    }


}
