/*
 * Comments generated using 0xAlpha AI Comment Generator v1.4.1
 * Copyright (c) 2025 by 0xAlpha. All rights reserved.
 * This software is provided "as-is", without warranty of any kind, express or implied.
 */
package io.greitan.avion.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LocaleManager {
    private final JavaPlugin plugin;
    private final Map<String, String> messages = new HashMap<>();
    private final String locale;

    /**
     * Constructs the LocaleManager and initializes the locale files.
     *
     * @param plugin The plugin instance.
     * @param locale The locale to load.
     */
    public LocaleManager(JavaPlugin plugin, String locale) {
        this.plugin = plugin;
        this.locale = locale;
        copyLocaleFiles();
        loadLocale();
    }

    /**
     * Copies the locale files to the plugin's data folder if they don't already
     * exist.
     */
    private void copyLocaleFiles() {
        File localeDir = new File(plugin.getDataFolder(), "locale");
        if (!localeDir.exists() && !localeDir.mkdirs()) {
            Logger.warn("Could not create locale directory: " + localeDir.getPath());
            return;
        }

        try {
            // Always ensure base english exists
            copyLocaleFile("en_us.yml");

            // Try to copy requested locale if it's not en_us
            if (locale != null && !locale.isBlank() && !"en_us".equalsIgnoreCase(locale)) {
                copyLocaleFile(locale.toLowerCase() + ".yml");
            }
        } catch (IOException e) {
            Logger.error("Error when copying the locale files!");
            Logger.error(e);
        }
    }

    /**
     * Copies a specific locale file to the data folder.
     *
     * @param fileName The name of the locale file.
     * @throws IOException If there is an error copying the file.
     */
    private void copyLocaleFile(String fileName) throws IOException {
        File localeFile = new File(plugin.getDataFolder(), "locale" + File.separator + fileName);
        if (localeFile.exists())
            return;

        try (InputStream in = plugin.getResource("locale/" + fileName)) {
            if (in == null) {
                Logger.warn("Locale file " + fileName + " not found in jar (resources/locale).");
                return;
            }

            // Ensure parent exists
            File parent = localeFile.getParentFile();
            if (parent != null && !parent.exists())
                parent.mkdirs();

            Files.copy(in, localeFile.toPath());
            Logger.info("Locale file " + fileName + " copied to " + localeFile.getPath());
        }
    }

    /**
     * Loads the locale from a file based on the specified locale.
     */
    private void loadLocale() {
        String fileName = (locale == null || locale.isBlank())
                ? "en_us.yml"
                : locale.toLowerCase() + ".yml";

        if (!loadLocaleFromFile(fileName)) {
            Logger.warn("Locale file for " + locale + " not found, falling back to English.");
            loadLocaleFromFile("en_us.yml");
        }
    }

    /**
     * Loads the locale data from a specific file.
     *
     * @param fileName The locale file to load.
     * @return True if the file was loaded successfully, otherwise false.
     */
    private boolean loadLocaleFromFile(String fileName) {
        File localeFile = new File(plugin.getDataFolder(), "locale" + File.separator + fileName);
        if (!localeFile.exists())
            return false;

        messages.clear();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(localeFile);
        for (String key : config.getKeys(true)) {
            if (config.isString(key)) {
                String value = config.getString(key);
                if (value != null) {
                    messages.put(key, value);
                }
            }
        }
        return true;
    }

    /**
     * Retrieves a localized message and replaces placeholders if provided.
     *
     * Placeholders are replaced by index:
     * {0}, {1}, {2} ... in the message.
     *
     * @param key          The key of the message.
     * @param placeholders Optional placeholders to replace in the message.
     * @return The localized message.
     */
    public String getMessage(String key, @Nullable String... placeholders) {
        String message = messages.getOrDefault(key, "Message not found.");

        if (placeholders != null && placeholders.length > 0) {
            for (int i = 0; i < placeholders.length; i++) {
                String replacement = placeholders[i] == null ? "" : placeholders[i];
                message = message.replace("{" + i + "}", replacement);
            }
        }

        return message;
    }
}