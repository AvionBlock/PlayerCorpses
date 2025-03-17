/*
 * Comments generated using 0xAlpha AI Comment Generator v1.4.1
 * Copyright (c) 2025 by 0xAlpha. All rights reserved.
 * This software is provided "as-is", without warranty of any kind, express or implied.
 */
package io.greitan.avion;

import org.bukkit.plugin.java.JavaPlugin;
import io.greitan.avion.listeners.*;
import io.greitan.avion.utils.*;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Main class for the PlayerCorpses plugin.
 */
public class PlayerCorpses extends JavaPlugin {

    private static @Getter PlayerCorpses instance;
    private static @Getter LocaleManager locManager;
    private static @Getter MiniMessage MM = MiniMessage.miniMessage();

    /**
     * Called when the plugin is enabled. Initializes locale manager and registers
     * event listeners.
     */
    @Override
    public void onEnable() {
        instance = this;
        reload();

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(locManager), this);
        getServer().getPluginManager().registerEvents(new NpcInteractListener(locManager), this);

        Logger.info(locManager.getMessage("plugin.enabled"));
    }

    /**
     * Called when the plugin is disabled. Logs the shutdown message.
     */
    @Override
    public void onDisable() {
        Logger.info(locManager.getMessage("plugin.disabled"));
    }

    /**
     * Reloads the plugin configuration and sets the locale.
     */
    public void reload() {
        saveDefaultConfig();
        reloadConfig();

        String locale = getConfig().getString("config.locale", "en_us");
        locManager = new LocaleManager(this, locale);

        Logger.info("Language set to: " + locale);
    }
}
