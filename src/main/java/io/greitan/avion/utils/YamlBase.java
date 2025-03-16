package io.greitan.avion.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class YamlBase {

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

    public static UUID generateUUIDv4() {
        return UUID.randomUUID();
    }

    public static String itemStackArrayToBase64(ItemStack[] items) {
        try {
            byte[] serialized = ItemStack.serializeItemsAsBytes(items);
            return bytesToBase64(serialized);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to serialize item stacks.", e);
        }
    }

    public static String bytesToBase64(byte[] serialized) {
        try {
            return Base64.getEncoder().encodeToString(serialized);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to convert bytes to Base64", e);
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String base64) {
        try {
            byte[] data = Base64.getDecoder().decode(base64);
            return ItemStack.deserializeItemsFromBytes(data);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to deserialize item stacks.", e);
        }
    }

    public static ItemStack itemStackFromBase64(String base64) {
        try {
            byte[] data = Base64.getDecoder().decode(base64);
            return ItemStack.deserializeBytes(data);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to deserialize item stack.", e);
        }
    }
}
