/*
 * Comments generated using 0xAlpha AI Comment Generator v1.4.1
 * Copyright (c) 2025 by 0xAlpha. All rights reserved.
 * This software is provided "as-is", without warranty of any kind, express or implied.
 */
package io.greitan.avion.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class YamlBase {
    private static final File STORE_DIR = new File("./plugins/PlayerCorpses/store");
    private static final File CORPSES_DIR = new File(STORE_DIR, "corpses");

    // -------------------------------------------------------------------------
    // NEW: Corpse storage (global, allows any player to access by corpseId)
    // Path: ./plugins/PlayerCorpses/store/corpses/<corpseId>.yml
    // -------------------------------------------------------------------------

    /**
     * Saves corpse data to a YAML file in a global corpses directory.
     *
     * @param corpseId The corpse id (usually your generated UUID string).
     * @param config   The configuration to be saved.
     */
    public static void saveCorpseData(String corpseId, YamlConfiguration config) {
        ensureDir(CORPSES_DIR);

        File file = new File(CORPSES_DIR, corpseId + ".yml");
        saveYaml(file, config);
    }

    /**
     * Loads corpse data from a YAML file in the global corpses directory.
     *
     * @param corpseId The corpse id.
     * @return The YamlConfiguration containing the corpse data.
     */
    public static YamlConfiguration loadCorpseData(String corpseId) {
        File file = new File(CORPSES_DIR, corpseId + ".yml");
        ensureFileExists(file, "Corpse data file does not exist");
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Deletes the corpse data file from the global corpses directory.
     *
     * @param corpseId The corpse id.
     */
    public static void deleteCorpseData(String corpseId) {
        File file = new File(CORPSES_DIR, corpseId + ".yml");
        deleteFileOrThrow(file, "Failed to delete corpse data file");
    }

    // -------------------------------------------------------------------------
    // Utils
    // -------------------------------------------------------------------------

    private static void ensureDir(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Failed to create directory: " + dir.getAbsolutePath());
        }
    }

    private static void saveYaml(File file, YamlConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save yaml to file: " + file.getAbsolutePath(), e);
        }
    }

    private static void ensureFileExists(File file, String msg) {
        if (!file.exists()) {
            throw new IllegalStateException(msg + ": " + file.getAbsolutePath());
        }
    }

    private static void deleteFileOrThrow(File file, String failMsg) {
        if (!file.exists()) {
            throw new IllegalStateException("Data file does not exist: " + file.getAbsolutePath());
        }
        if (!file.delete()) {
            throw new IllegalStateException(failMsg + ": " + file.getAbsolutePath());
        }
    }

    /**
     * Generates a new UUIDv4 without dashes.
     *
     * @return The generated UUID string.
     */
    public static String generateUUIDv4() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Converts an array of ItemStacks to a Base64 encoded string.
     *
     * @param items The array of ItemStacks to be serialized.
     * @return The Base64 encoded string representing the items.
     */
    public static String itemStackArrayToBase64(ItemStack[] items) {
        try {
            byte[] serialized = ItemStack.serializeItemsAsBytes(items);
            return bytesToBase64(serialized);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to serialize item stacks.", e);
        }
    }

    /**
     * Converts a byte array to a Base64 encoded string.
     *
     * @param serialized The byte array to be converted.
     * @return The Base64 encoded string.
     */
    public static String bytesToBase64(byte[] serialized) {
        try {
            return Base64.getEncoder().encodeToString(serialized);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to convert bytes to Base64", e);
        }
    }

    /**
     * Converts a Base64 encoded string to an array of ItemStacks.
     *
     * @param base64 The Base64 encoded string representing the item stacks.
     * @return The deserialized array of ItemStacks.
     */
    public static ItemStack[] itemStackArrayFromBase64(String base64) {
        try {
            byte[] data = Base64.getDecoder().decode(base64);
            return ItemStack.deserializeItemsFromBytes(data);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to deserialize item stacks.", e);
        }
    }

    /**
     * Converts a Base64 encoded string to a single ItemStack.
     *
     * @param base64 The Base64 encoded string representing the item stack.
     * @return The deserialized ItemStack.
     */
    public static ItemStack itemStackFromBase64(String base64) {
        try {
            byte[] data = Base64.getDecoder().decode(base64);
            return ItemStack.deserializeBytes(data);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to deserialize item stack.", e);
        }
    }
}