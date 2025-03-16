package io.greitan.avion;

import org.bukkit.plugin.java.JavaPlugin;

import io.greitan.avion.listeners.*;
import io.greitan.avion.utils.*;

import lombok.Getter;

public class PlayerCorpses extends JavaPlugin {
    private static @Getter PlayerCorpses instance;
    private static @Getter LocaleManager locManager;

    @Override
    public void onEnable() {
        instance = this;
        reload();

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(locManager), this);

        Logger.info(locManager.getMessage("plugin.enabled"));
    }

    @Override
    public void onDisable() {
        Logger.info(locManager.getMessage("plugin.disabled"));
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();

        String locale = getConfig().getString("config.locale", "en_us");
        locManager = new LocaleManager(this, locale);

        Logger.info("Language set to: " + locale);
    }
}