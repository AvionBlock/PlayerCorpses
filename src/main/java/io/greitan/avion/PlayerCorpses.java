package io.greitan.avion;

import org.bukkit.plugin.java.JavaPlugin;

import io.greitan.avion.listeners.*;
import io.greitan.avion.utils.*;

import lombok.Getter;

public class PlayerCorpses extends JavaPlugin {
    private static @Getter PlayerCorpses instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        this.reload();
        Logger.info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
    }
}
