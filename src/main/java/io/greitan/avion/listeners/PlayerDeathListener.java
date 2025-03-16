package io.greitan.avion.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.NpcAttribute;
import de.oliver.fancynpcs.api.NpcData;

import io.greitan.avion.utils.JsonBase;
import io.greitan.avion.utils.LocaleManager;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerDeathListener implements Listener {

    private final LocaleManager localeManager;

    public PlayerDeathListener(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID corpseUUID = JsonBase.generateUUIDv4();
        String formattedTime = getFormattedCurrentTime();

        NpcData npcData = createNpcData(player, corpseUUID, formattedTime);
        Npc npc = createNpc(npcData);

        spawnNpcForAll(npc);
    }

    private String getFormattedCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private NpcData createNpcData(Player player, UUID corpseUUID, String formattedTime) {
        NpcData npcData = new NpcData(
                corpseUUID.toString(),
                UUID.fromString("6035afb8-c5bb-4d7b-980d-728a1c407d0e"),
                player.getLocation());

        npcData.setDisplayName(localeManager.getMessage("corpse.name", player.getName()));
        npcData.setType(EntityType.PLAYER);
        addNpcAttributes(npcData);

        return npcData;
    }

    private void addNpcAttributes(NpcData npcData) {
        NpcAttribute npcAttribute = FancyNpcsPlugin.get().getAttributeManager().getAttributeByName(EntityType.PLAYER,
                "pose");
        npcData.addAttribute(npcAttribute, "swimming");
    }

    private Npc createNpc(NpcData npcData) {
        return FancyNpcsPlugin.get().getNpcAdapter().apply(npcData);
    }

    private void spawnNpcForAll(Npc npc) {
        FancyNpcsPlugin.get().getNpcManager().registerNpc(npc);
        npc.create();
        npc.spawnForAll();
    }
}
