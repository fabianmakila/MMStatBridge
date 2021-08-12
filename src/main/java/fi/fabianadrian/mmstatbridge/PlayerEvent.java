package fi.fabianadrian.mmstatbridge;

import fi.fabianadrian.mmstatbridge.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvent implements Listener {

    private final UserManager userManager;

    public PlayerEvent(MMStatBridge plugin) {
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        userManager.loadUser(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        userManager.unloadUser(e.getPlayer().getUniqueId());
    }
}
