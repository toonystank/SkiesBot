package com.edward.skiesbot;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.config.EssentialsUserConfiguration;
import com.taggernation.taggernationlib.config.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public final class SkiesBot extends JavaPlugin {


    private ConfigManager mainConfig;
    private JDA jda;
    private IEssentials ess;
    private static SkiesBot instance;

    public ConfigManager getMainConfig() {
        return mainConfig;
    }
    public JDA getJda() {
        return jda;
    }
    public IEssentials getEss() {
        return ess;
    }
    public static SkiesBot getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            mainConfig = new ConfigManager(this, "MainConfig.yml", false, true);
        } catch (IOException e) {
            e.printStackTrace();
            this.setEnabled(false);
        }
        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            ess = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
        }

        try {
            this.getLogger().info("Starting JDA...");
            jda = JDABuilder.createDefault(mainConfig.getString("token")).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
        } catch (LoginException e) {
            e.printStackTrace();
            this.setEnabled(false);
        }

    }

    @Override
    public void onDisable() {
        if (this.isEnabled()) {
            jda.shutdown();
        }
    }
}
