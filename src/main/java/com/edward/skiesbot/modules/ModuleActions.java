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
                reaction(action);
            }
        }
    }
    public void initialize(MessageReactionAddEvent event) {
        if (!isEnabled) return;
        for (String action : actions) {
            String actionType = modules.getString("Action." + action + ".Type");
            if (actionType.equals(ModuleActionType.REACTION.getType())) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle(modules.getString("Action." + action + "data.click.Embed.Title"));

            }
        }
    }
    public void reaction(String action) {
        for (String reaction : modules.getStringList("Action." + action + ".data")) {
            modules.getEmbedMessage().addReaction(reaction).queue();
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
