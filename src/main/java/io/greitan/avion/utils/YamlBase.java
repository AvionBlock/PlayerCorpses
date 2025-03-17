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

    /**
     * Saves player data to a YAML file.
     * 
     * @param name   The player's name.
     * @param id     The unique ID of the player.
     * @param config The configuration to be saved.
     */
    public static void savePlayerData(String name, String id, YamlConfiguration config) {
        File dir = new File("./plugins/PlayerCorpses/store/" + name);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Failed to create directory: " + dir.getAbsolutePath());
        }

        File file = new File(dir, id + ".yml");

        try {
            config.save(file);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save player data to file: " + file.getAbsolutePath(), e);
        }
    }

    /**
     * Loads player data from a YAML file.
     * 
     * @param name The player's name.
     * @param id   The unique ID of the player.
     * @return The YamlConfiguration containing the player data.
     */
    public static YamlConfiguration loadPlayerData(String name, String id) {
        File file = new File("./plugins/PlayerCorpses/store/" + name, id + ".yml");

        if (!file.exists()) {
            throw new IllegalStateException("Player data file does not exist: " + file.getAbsolutePath());
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        return config;
    }

    /**
     * Deletes the player data file.
     * 
     * @param name The player's name.
     * @param id   The unique ID of the player.
     */
    public static void deletePlayerData(String name, String id) {
        File file = new File("./plugins/PlayerCorpses/store/" + name, id + ".yml");

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Player data file deleted: " + file.getAbsolutePath());
            } else {
                throw new IllegalStateException("Failed to delete player data file: " + file.getAbsolutePath());
            }
        } else {
            throw new IllegalStateException("Player data file does not exist: " + file.getAbsolutePath());
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
