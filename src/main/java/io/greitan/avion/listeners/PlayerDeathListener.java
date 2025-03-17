/*
 * Comments generated using 0xAlpha AI Comment Generator v1.4.1
 * Copyright (c) 2025 by 0xAlpha. All rights reserved.
 * This software is provided "as-is", without warranty of any kind, express or implied.
 */
package io.greitan.avion.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.NpcAttribute;
import de.oliver.fancynpcs.api.NpcData;

import io.greitan.avion.utils.LocaleManager;
import io.greitan.avion.utils.Logger;
import io.greitan.avion.utils.YamlBase;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PlayerDeathListener implements Listener {

    private final LocaleManager localeManager;

    /**
     * Constructor for the PlayerDeathListener.
     * 
     * @param localeManager The locale manager for handling messages.
     */
    public PlayerDeathListener(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    /**
     * Handles the PlayerDeathEvent, creates a corpse NPC, and saves the player's
     * death data.
     *
     * @param event The event triggered by a player’s death.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String corpseUUID = YamlBase.generateUUIDv4();

        // Clear the drops on death
        event.getDrops().clear();

        // Get player's inventory and prepare death data
        ItemStack[] inventoryContents = player.getInventory().getContents();
        YamlConfiguration config = new YamlConfiguration();
        config.set("uuid", corpseUUID);
        config.set("playerName", player.getName());
        config.set("timeOfDeath", LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(3)));

        // Store inventory if it exists
        if (inventoryContents.length > 0) {
            try {
                config.set("inventory", YamlBase.itemStackArrayToBase64(inventoryContents));
            } catch (IllegalStateException e) {
                Logger.error(e);
            }
        }

        // Save player death data
        try {
            YamlBase.savePlayerData(player.getName(), corpseUUID, config);
        } catch (IllegalStateException e) {
            Logger.error(e);
        }

        // Create and spawn NPC representing the corpse
        try {
            NpcData npcData = createNpcData(player, corpseUUID);
            Npc npc = createNpc(npcData);
            spawnNpcForAll(npc);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * Creates the NPC data for the corpse.
     *
     * @param player     The player whose corpse the NPC represents.
     * @param corpseUUID The unique identifier for the corpse.
     * @return The generated NPC data.
     */
    private NpcData createNpcData(Player player, String corpseUUID) {
        NpcData npcData = new NpcData(
                corpseUUID,
                UUID.fromString("6035afb8-c5bb-4d7b-980d-728a1c407d0e"),
                player.getLocation());

        npcData.setDisplayName(localeManager.getMessage("corpse.name", player.getName()));
        npcData.setType(EntityType.PLAYER);
        addNpcAttributes(npcData);

        return npcData;
    }

    /**
     * Adds attributes to the NPC.
     *
     * @param npcData The NPC data to add attributes to.
     */
    private void addNpcAttributes(NpcData npcData) {
        NpcAttribute npcAttribute = FancyNpcsPlugin.get().getAttributeManager().getAttributeByName(EntityType.PLAYER,
                "pose");
        npcData.addAttribute(npcAttribute, "swimming");
    }

    /**
     * Creates an NPC from the given NPC data.
     *
     * @param npcData The NPC data.
     * @return The created NPC.
     */
    private Npc createNpc(NpcData npcData) {
        return FancyNpcsPlugin.get().getNpcAdapter().apply(npcData);
    }

    /**
     * Spawns the NPC for all players.
     *
     * @param npc The NPC to spawn.
     */
    private void spawnNpcForAll(Npc npc) {
        FancyNpcsPlugin.get().getNpcManager().registerNpc(npc);
        npc.create();
        npc.spawnForAll();
    }
}
