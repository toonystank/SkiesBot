package com.edward.skiesbot.modules;

import com.edward.skiesbot.SkiesBot;
import com.edward.skiesbot.utils.enums.ModuleOptions;
import com.edward.skiesbot.utils.enums.ModuleType;
import com.taggernation.taggernationlib.config.ConfigManager;

import java.io.IOException;
import java.util.Locale;

public class Modules extends ConfigManager {


    private final SkiesBot plugin;
    private final ModuleType type;
    private final ModuleOptions optionType;
    private final String optionValue;
    private boolean message;
    private boolean embed;
    private boolean action;

    public Modules(SkiesBot plugin, String fileName) throws IOException {
        super(plugin, fileName, "Modules", false, false);
        this.plugin = plugin;
        this.type = ModuleType.valueOf(this.getString("module.event.type").toLowerCase(Locale.ROOT));
        this.optionType = ModuleOptions.valueOf(this.getString("module.event.option.type").toLowerCase(Locale.ROOT));
        this.optionValue = this.getString("module.event.option.value");

        if (this.getString("data.Message") != null) {
            this.message = true;
        }
        if (this.getString("data.Embed") != null) {
            this.embed = true;
        }
        if (this.getString("data.Action") != null) {
            this.action = true;
        }

    }
    public ModuleType getType() {
        return type;
    }
    public ModuleOptions getOptionType() {
        return optionType;
    }
    public String getOptionValue() {
        return optionValue;
    }
    public boolean isMessage() {
        return message;
    }
    public boolean isEmbed() {
        return embed;
    }
    public boolean isAction() {
        return action;
    }
    public void processMessage() {
        if (this.message) {
            this.plugin.getLogger().info("Message: " + this.getString("data.Message"));
        }
    }


}
