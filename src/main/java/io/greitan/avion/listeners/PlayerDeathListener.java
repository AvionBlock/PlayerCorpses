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
import io.greitan.avion.utils.YamlBase;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PlayerDeathListener implements Listener {

    private final LocaleManager localeManager;

    public PlayerDeathListener(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID corpseUUID = YamlBase.generateUUIDv4();

        ItemStack[] inventoryContents = player.getInventory().getContents();
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        byte[] offhandBytes = player.getInventory().getItemInOffHand() != null
                ? player.getInventory().getItemInOffHand().serializeAsBytes()
                : null;

        YamlConfiguration config = new YamlConfiguration();
        config.set("uuid", corpseUUID.toString());
        config.set("playerName", player.getName());
        config.set("timeOfDeath", LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(3)));

        if (inventoryContents.length > 0) {
            config.set("inventory", YamlBase.itemStackArrayToBase64(inventoryContents));
        }
        if (armorContents.length > 0) {
            config.set("armor", YamlBase.itemStackArrayToBase64(armorContents));
        }
        if (offhandBytes != null) {
            config.set("offhand", YamlBase.bytesToBase64(offhandBytes));
        }

        YamlBase.savePlayerData(player.getName(), corpseUUID.toString(), config);

        NpcData npcData = createNpcData(player, corpseUUID);
        Npc npc = createNpc(npcData);
        spawnNpcForAll(npc);
    }

    private NpcData createNpcData(Player player, UUID corpseUUID) {
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
