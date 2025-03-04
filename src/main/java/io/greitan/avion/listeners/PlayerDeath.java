package io.greitan.avion.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.NpcAttribute;
import de.oliver.fancynpcs.api.NpcData;
import de.oliver.fancynpcs.api.actions.ActionTrigger;
import de.oliver.fancynpcs.api.actions.types.PlayerCommandAction;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String playerName = player.getName();
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);

        NpcData data = new NpcData(playerName + " " + formattedTime,
                UUID.fromString("6035afb8-c5bb-4d7b-980d-728a1c407d0e"),
                player.getLocation());
        data.setDisplayName("<red>" + playerName + " " + formattedTime);
        data.setType(EntityType.SKELETON);
        data.addAction(ActionTrigger.RIGHT_CLICK, 0, new PlayerCommandAction(), "say " + formattedTime);
        data.addAction(ActionTrigger.LEFT_CLICK, 0, new PlayerCommandAction(), "say 1--" + formattedTime);
        NpcAttribute npcAttribute = FancyNpcsPlugin.get().getAttributeManager().getAttributeByName(EntityType.PLAYER,
                "pose");
        data.addAttribute(npcAttribute, "sleeping");

        Npc npc = FancyNpcsPlugin.get().getNpcAdapter().apply(data);
        FancyNpcsPlugin.get().getNpcManager().registerNpc(npc);
        npc.create();
        npc.spawnForAll();
    }
}