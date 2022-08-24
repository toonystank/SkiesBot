package com.edward.skiesbot.modules;

import com.edward.skiesbot.SkiesBot;
import com.edward.skiesbot.utils.DefaultEmbed;
import com.edward.skiesbot.utils.PlaceholderParser;
import com.edward.skiesbot.utils.enums.ModuleOptions;
import com.taggernation.taggernationlib.config.ConfigManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleEmbed {

    private final Modules modules;
    private final boolean isEnabled;
    private final ModuleOptions moduleOptions;
    private final String value;
    private String title;
    private final List<String> description;
    private String footerText;
    private String footerIconURL;
    private String thumbnail;
    private final JDA jda;
    private final boolean log;
    private ConfigManager logConfig;
    private final List<String> messageIDs = new ArrayList<>();

    public ModuleEmbed(Modules modules, boolean isEnabled, ModuleOptions moduleOptions, String value, String moduleName, boolean log) throws IOException {
        this.modules = modules;
        this.isEnabled = isEnabled;
        this.moduleOptions = moduleOptions;
        this.value = value;
        messageIDs.addAll(Objects.requireNonNull(modules.getConfig().getConfigurationSection("Embed")).getKeys(false));
        this.title = getEmbedConfig("title");
        this.description = getEmbedConfigDescription();
        this.footerText = getEmbedConfig("footer.text");
        this.footerIconURL = getEmbedConfig("footer.icon");
        this.thumbnail = getEmbedConfig("thumbnail");
        this.log = log;
        this.jda = SkiesBot.getInstance().getJda();
        if (log) this.logConfig = new ConfigManager(SkiesBot.getInstance(), moduleName + "_log.yml", "Data", false, false);
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public ConfigManager getLogConfig() { return logConfig; }
    public List<String> getMessageIDs() { return messageIDs; }

    public void initialize(MessageReceivedEvent event) {
        this.title = PlaceholderParser.parse(title, event);
        this.description.forEach(s -> s = PlaceholderParser.parse(s, event));
        this.footerText = PlaceholderParser.parse(footerText, event);
        this.footerIconURL = PlaceholderParser.parse(footerIconURL, event);
        this.thumbnail = PlaceholderParser.parse(thumbnail, event);
        if (moduleOptions.equals(ModuleOptions.CHANNEL)) this.finalize(title, this.value, description, footerText, footerIconURL, thumbnail);
        else if (moduleOptions.equals(ModuleOptions.MESSAGE)) this.finalize(title, event.getChannel().getId(), description, footerText, footerIconURL, thumbnail);
        // Module option mention role is not done
        if (log) saveToYML(event.getMessageId(), description, footerText, footerIconURL, thumbnail);
    }

    public void initialize(GuildMemberJoinEvent event) {
        this.title = PlaceholderParser.parse(title, event);
        this.description.forEach(s -> s = PlaceholderParser.parse(s, event));
        this.footerText = PlaceholderParser.parse(footerText, event);
        this.footerIconURL = PlaceholderParser.parse(footerIconURL, event);
        this.thumbnail = PlaceholderParser.parse(thumbnail, event);
        this.finalize(title, this.value,description, footerText, footerIconURL, thumbnail);
    }

    public void finalize(String title,String channelID, List<String> description, String footerText, String footerIconURL, String thumbnail) {
        if (!isEnabled) return;
        TextChannel channel = jda.getTextChannelById(channelID);
        if (channel == null) return;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(String.join("\n",description));
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
    public String getEmbedConfig(String type) {
        switch (type) {
            case "title":
                return modules.getConfig().getString("data.Embed.title");
            case "footer.text":
                return modules.getConfig().getString("data.Embed.footer.text");
            case "footer.icon":
                return modules.getConfig().getString("data.Embed.footer.icon");
            case "thumbnail":
                return modules.getConfig().getString("data.Embed.thumbnail");
            default:
                return null;
        }
    }
    public List<String> getEmbedConfigDescription() {
        return modules.getStringList("data.Embed.description");
    }

    public String getEmbedLogConfig(EmbedLogConfigTypes type, String messageID, String reactionID, String actionID) {
        return modules.getString("data.Actions." + actionID + ".data." + reactionID + ".Click.Embed." + messageID + type);
    }
    public List<String> getEmbedLogDescription(String messageID) {
        return modules.getStringList("Embed."+ messageID + "description");
    }
    public void saveToYML(String messageID, List<String> description, String footerText, String footerIconURL, String thumbnail) {
        logConfig.set("Embed." + messageID + ".Description", description);
        logConfig.set("Embed." + messageID + ".Footer.Text", footerText);
        logConfig.set("Embed." + messageID + ".Footer.Icon", footerIconURL);
        logConfig.set("Embed." + messageID + ".Thumbnail", thumbnail);
        logConfig.save();
    }
    public enum EmbedLogConfigTypes {
        DESCRIPTION(".Description"),
        FOOTER_TEXT(".Footer.Text"),
        FOOTER_ICON(".Footer.Icon"),
        THUMBNAIL(".Thumbnail");

        public final String type;
        EmbedLogConfigTypes(String type) {
            this.type = type;
        }
    }


}
