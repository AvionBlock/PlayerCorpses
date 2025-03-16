package io.greitan.avion.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class LocaleManager {
    private final JavaPlugin plugin;
    private final Map<String, String> messages = new HashMap<>();
    private final String locale;

    public LocaleManager(JavaPlugin plugin, String locale) {
        this.plugin = plugin;
        this.locale = locale;
        copyLocaleFiles();
        loadLocale();
    }

    private void copyLocaleFiles() {
        File localeDir = new File(plugin.getDataFolder(), "locale");
        if (!localeDir.exists()) {
            localeDir.mkdirs();
        }

        try {
            copyLocaleFile("en_us.yml");
            copyLocaleFile(locale + ".yml");
        } catch (IOException e) {
            Logger.error("Error when copying the locale files!");
            Logger.error(e);
        }
    }

    private void copyLocaleFile(String fileName) throws IOException {
        File localeFile = new File(plugin.getDataFolder(), "locale" + File.separator + fileName);
        if (!localeFile.exists()) {
            try (InputStream in = plugin.getResource("locale/" + fileName);
                    Reader reader = new InputStreamReader(in)) {
                Files.copy(in, localeFile.toPath());
                Logger.info("Locale file " + fileName + " copied to " + localeFile.getPath());
            }
        }
    }

    private void loadLocale() {
        String fileName = locale + ".yml";
        if (!loadLocaleFromFile(fileName)) {
            Logger.warn("Locale file for " + locale + " not found, falling back to English.");
            loadLocaleFromFile("en_us.yml");
        }
    }

    private boolean loadLocaleFromFile(String fileName) {
        File localeFile = new File(plugin.getDataFolder(), "locale" + File.separator + fileName);
        if (localeFile.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(localeFile);
            for (String key : config.getKeys(true)) {
                messages.put(key, config.getString(key));
            }
            return true;
        }
        return false;
    }

    public String getMessage(String key, @Nullable String... placeholders) {
        String message = messages.getOrDefault(key, "Message not found.");
        if (placeholders != null && placeholders.length > 0) {
            for (int i = 0; i < placeholders.length; i++) {
                message = message.replace("{" + i + "}", placeholders[i]);
            }
        }
        return message;
    }
}
