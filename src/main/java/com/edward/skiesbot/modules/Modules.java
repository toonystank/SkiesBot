package com.edward.skiesbot.modules;

import com.edward.skiesbot.SkiesBot;
import com.edward.skiesbot.utils.enums.ModuleDataType;
import com.edward.skiesbot.utils.enums.ModuleOptions;
import com.edward.skiesbot.utils.enums.ModuleType;
import com.taggernation.taggernationlib.config.ConfigManager;
import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class Modules extends ConfigManager {


    private final SkiesBot plugin;
    private final String moduleName;
    private final ModuleType type;
    private final ModuleOptions optionType;
    private final String optionValue;
    private Message embedMessage;
    private Message textMessage;
    private final boolean log;
    private ModuleMessage moduleMessage;
    private ModuleEmbed moduleEmbed;
    private ModuleActions moduleActions;

    public Modules(SkiesBot plugin, String fileName) throws IOException {
        super(plugin, fileName, "Modules", false, false);
        this.plugin = plugin;
        this.moduleName = fileName.replace(".yml", "");
        this.type = ModuleType.valueOf(this.getString("module.event.type").toLowerCase(Locale.ROOT));
        this.optionType = ModuleOptions.valueOf(this.getString("module.data.option").toLowerCase(Locale.ROOT));
        this.optionValue = this.getString("module.data.value");
        this.log = this.getBoolean("module.event.log");



    }
    public SkiesBot getPlugin() { return plugin; }
    public String getModuleName() { return moduleName; }
    public ModuleType getType() {
        return type;
    }
    public ModuleOptions getOptionType() {
        return optionType;
    }
    public String getOptionValue() {
        return optionValue;
    }
    public Message getEmbedMessage() {return embedMessage; }
    public Message getTextMessage() {return textMessage;}
    public ModuleMessage getModuleMessage() {return moduleMessage;}
    public ModuleEmbed getModuleEmbed() {return moduleEmbed;}
    public ModuleActions getModuleActions() {return moduleActions;}

    public void setEmbedMessage(Message embedMessage) {this.embedMessage = embedMessage;}
    public void setTextMessage(Message textMessage) {this.textMessage = textMessage;}

    public void processMessage() {
    }

    private void initializeModules() {
        if (isEnabled(ModuleDataType.MESSAGE)) {
            try {
                this.moduleMessage = new ModuleMessage(this,isEnabled(ModuleDataType.MESSAGE), optionType, optionValue, moduleName, log);
            }catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        if (isEnabled(ModuleDataType.EMBED)) {
            try {
                this.moduleEmbed = new ModuleEmbed(this, isEnabled(ModuleDataType.EMBED), optionType, optionValue, moduleName, log);
            }catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        if (isEnabled(ModuleDataType.ACTION)) {
            this.moduleActions = new ModuleActions(this, isEnabled(ModuleDataType.ACTION), Objects.requireNonNull(getConfig().getConfigurationSection("data.Actions"), "Actions cant be empty").getKeys(false), optionValue);
        }
    }
    private void initializeEvent() {
        switch (type) {
            case MESSAGE_RECEIVED_IN_CHANNEL:
            case MESSAGE_RECEIVED:
            case ROLE_MENTION:
                break;
            case JOIN:
                break;

        }
    }
    public boolean isEnabled(ModuleDataType type) {
        if (this.getString("data."+ type +".Enable") == null) return false;
        return this.getBoolean("data."+ type +".Enable");
    }







}
