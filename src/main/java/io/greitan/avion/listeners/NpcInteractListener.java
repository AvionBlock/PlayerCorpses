package io.greitan.avion.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.oliver.fancynpcs.api.events.NpcInteractEvent;
import io.greitan.avion.utils.LocaleManager;
import io.greitan.avion.utils.Logger;

public class NpcInteractListener implements Listener {
    // private final LocaleManager localeManager;

    public NpcInteractListener(LocaleManager localeManager) {
        // this.localeManager = localeManager;
    }

    @EventHandler
    public void npcInteract(NpcInteractEvent e) {
        Logger.info(e.getNpc().getData().getName());
    }
}
