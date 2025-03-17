/*
 * Comments generated using 0xAlpha AI Comment Generator v1.4.1
 * Copyright (c) 2025 by 0xAlpha. All rights reserved.
 * This software is provided "as-is", without warranty of any kind, express or implied.
 */
package io.greitan.avion.utils;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.greitan.avion.PlayerCorpses;
import net.kyori.adventure.text.Component;

public class MenuBuilder {

    /**
     * Creates an ItemStack with specified properties.
     *
     * @param material The material of the item.
     * @param name     The display name, serialized as MiniMessage.
     * @param lore     The lore of the item, may be null.
     * @param tag      A persistent data tag, may be null.
     * @return The constructed ItemStack.
     */
    public static ItemStack createItem(Material material, String name, @Nullable List<Component> lore,
            @Nullable String tag) {
        NamespacedKey key = new NamespacedKey(PlayerCorpses.getInstance(), "corpse");

        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(PlayerCorpses.getMM().deserialize(name));

            if (tag != null)
                meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, tag);

            if (lore != null)
                meta.lore(lore);

            item.setItemMeta(meta);
        }
        return item;
    }
}
