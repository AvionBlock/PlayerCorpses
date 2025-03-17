/*
 * Comments generated using 0xAlpha AI Comment Generator v1.4.1
 * Copyright (c) 2025 by 0xAlpha. All rights reserved.
 * This software is provided "as-is", without warranty of any kind, express or implied.
 */
package io.greitan.avion.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.events.NpcInteractEvent;
import de.oliver.fancynpcs.api.events.NpcRemoveEvent;
import de.oliver.fancynpcs.api.events.NpcStopLookingEvent;
import io.greitan.avion.PlayerCorpses;
import io.greitan.avion.utils.LocaleManager;
import io.greitan.avion.utils.Logger;
import io.greitan.avion.utils.MenuBuilder;
import io.greitan.avion.utils.YamlBase;
import net.kyori.adventure.text.Component;

import java.util.stream.IntStream;

public class NpcInteractListener implements Listener {
    private final LocaleManager localeManager;
    private final Component MENU_TITLE;

    /**
     * Constructor initializes the listener with a locale manager.
     *
     * @param localeManager Locale manager for handling messages.
     */
    public NpcInteractListener(LocaleManager localeManager) {
        this.localeManager = localeManager;
        this.MENU_TITLE = PlayerCorpses.getMM().deserialize(localeManager.getMessage("menu.title"));
    }

    /**
     * Handles NPC interaction, shows the menu when a player interacts with an NPC.
     *
     * @param e The NPC interaction event.
     */
    @EventHandler
    public void npcInteract(NpcInteractEvent e) {
        showMenu(e.getPlayer(), e.getNpc());
    }

    /**
     * Handles inventory click events in the menu.
     *
     * @param event The inventory click event.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;
        if (event.getInventory().getType() != InventoryType.CHEST)
            return;
        if (!event.getView().title().equals(MENU_TITLE))
            return;

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;

        // Handle actions based on the clicked item
        String action = switch (clickedItem.getType()) {
            case ARROW -> "break";
            case BARRIER -> "back";
            default -> null;
        };

        if (action != null) {
            player.closeInventory();
            handleAction(player, action, clickedItem);
        }
    }

    /**
     * Executes the appropriate action based on the clicked item.
     *
     * @param player      The player who clicked the item.
     * @param action      The action to perform.
     * @param clickedItem The item clicked.
     */
    private void handleAction(Player player, String action, ItemStack clickedItem) {
        NamespacedKey key = new NamespacedKey(PlayerCorpses.getInstance(), "corpse");
        String npcID = clickedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        switch (action) {
            case "break" -> {
                if (npcID != null)
                    breakCorpse(player, npcID);
                player.sendMessage(PlayerCorpses.getMM().deserialize(localeManager.getMessage("menu.action.break")));
            }
            case "back" -> {
                player.sendMessage(PlayerCorpses.getMM().deserialize(localeManager.getMessage("menu.action.back")));
            }
            default -> {
            }
        }
    }

    /**
     * Breaks the corpse NPC and handles the drop of items from the corpse.
     *
     * @param player The player interacting with the NPC.
     * @param npcID  The NPC ID.
     */
    private void breakCorpse(Player player, String npcID) {
        Npc npc = FancyNpcsPlugin.get().getNpcManager().getNpcById(npcID);
        String containerId = npc.getData().getName();
        YamlConfiguration containerData;

        try {
            containerData = YamlBase.loadPlayerData(player.getName(), containerId);
        } catch (IllegalStateException e) {
            Logger.error(e);
            return;
        }

        Location dropLocation = npc.getData().getLocation();

        // Remove NPC and handle its drops
        if (new NpcRemoveEvent(npc, Bukkit.getServer().getConsoleSender()).callEvent()) {
            npc.removeForAll();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (npc.getIsLookingAtPlayer().getOrDefault(onlinePlayer.getUniqueId(), false)) {
                    npc.getIsLookingAtPlayer().put(onlinePlayer.getUniqueId(), false);
                    new NpcStopLookingEvent(npc, onlinePlayer).callEvent();
                }
            }
            FancyNpcsPlugin.get().getNpcManager().removeNpc(npc);

            // Drop items if any exist
            if (containerData.contains("inventory")) {
                try {
                    ItemStack[] inventoryContents = YamlBase
                            .itemStackArrayFromBase64(containerData.getString("inventory"));
                    for (ItemStack item : inventoryContents) {
                        if (item != null) {
                            dropLocation.getWorld().dropItemNaturally(dropLocation, item);
                        }
                    }
                } catch (IllegalStateException e) {
                    Logger.error(e);
                }
            }

            YamlBase.deletePlayerData(player.getName(), containerId);
        } else {
            player.sendMessage(PlayerCorpses.getMM().deserialize(localeManager.getMessage("corpse.break_error")));
        }
    }

    /**
     * Shows the interaction menu for a player and NPC.
     *
     * @param player The player to show the menu to.
     * @param npc    The NPC the player is interacting with.
     */
    private void showMenu(Player player, Npc npc) {
        Inventory inv = Bukkit.createInventory(null, 27, MENU_TITLE);
        fillBorders(inv);
        inv.setItem(11,
                MenuBuilder.createItem(Material.BARRIER, localeManager.getMessage("menu.button.back"),
                        null, npc.getData().getId()));
        inv.setItem(15,
                MenuBuilder.createItem(Material.ARROW, localeManager.getMessage("menu.button.break"),
                        null, npc.getData().getId()));
        player.openInventory(inv);
    }

    /**
     * Fills the borders of the inventory with decorative items.
     *
     * @param inv The inventory to fill.
     */
    private void fillBorders(Inventory inv) {
        IntStream.range(0, 27)
                .filter(i -> i != 11 && i != 15)
                .forEach(i -> inv.setItem(i,
                        MenuBuilder.createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", null, null)));
    }
}
