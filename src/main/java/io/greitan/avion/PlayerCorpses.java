package io.greitan.avion;

import lombok.Getter;
import io.greitan.avion.utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerCorpses extends JavaPlugin {
    private static @Getter PlayerCorpses instance;

    @Override
    public void onEnable() {
        instance = this;
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
